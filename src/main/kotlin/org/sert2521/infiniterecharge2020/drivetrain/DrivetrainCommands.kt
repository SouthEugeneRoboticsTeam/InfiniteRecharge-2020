package org.sert2521.infiniterecharge2020.drivetrain

import com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.wpilibj.GenericHID
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import org.sert2521.infiniterecharge2020.OI.primaryJoystick
import org.sert2521.infiniterecharge2020.utils.deadband
import org.sert2521.infiniterecharge2020.OI.ControlMode
import org.sert2521.infiniterecharge2020.OI.controlMode
import org.sert2521.infiniterecharge2020.OI.primaryController
import org.sert2521.infiniterecharge2020.vision.Vision
import org.sert2521.sertain.control.MotionCurve
import org.sert2521.sertain.control.PidfConfig
import org.sert2521.sertain.control.PidfController
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.motors.EncoderTicks
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import org.sert2521.sertain.telemetry.TableEntry
import org.sert2521.sertain.telemetry.tableEntry
import org.sert2521.sertain.units.Chronic
import org.sert2521.sertain.units.CompositeUnit
import org.sert2521.sertain.units.CompositeUnitType
import org.sert2521.sertain.units.Linear
import org.sert2521.sertain.units.Meters
import org.sert2521.sertain.units.MetricUnit
import org.sert2521.sertain.units.MetricValue
import org.sert2521.sertain.units.Milliseconds
import org.sert2521.sertain.units.Per
import org.sert2521.sertain.units.Seconds
import org.sert2521.sertain.units.convertTo
import org.sert2521.sertain.units.div
import org.sert2521.sertain.units.rps
import org.sert2521.sertain.utils.timer
import kotlin.math.sign
import org.sert2521.sertain.telemetry.Table
import org.sert2521.sertain.telemetry.wpiTable
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.IEEErem
import kotlin.math.abs

class PidfController2(config: PidfConfig, val dt: Double) {
    private val kp = config.kp ?: 0.0
    private val ki = config.ki ?: 0.0
    private val kd = config.kd ?: 0.0
    private val kf = config.kf ?: 0.0

    private var integral = 0.0
    private var lastError = 0.0

    fun next(setPoint: Double, actual: Double): Double {
        val error = setPoint - actual
        integral += error * dt
        val derivative = (error - lastError) / dt
        lastError = error
            return (kp * error) + (ki * integral) + (kd * derivative) + (kf * sign(error))
    }
}

private val throttle
    get() = when (controlMode) {
        ControlMode.CONTROLLER -> primaryController.getY(GenericHID.Hand.kLeft)
        ControlMode.JOYSTICK -> primaryJoystick.y.deadband(0.02)
    }
private val turn
    get() = when (controlMode) {
        ControlMode.CONTROLLER -> primaryController.getX(GenericHID.Hand.kRight)
        ControlMode.JOYSTICK -> primaryJoystick.x.deadband(0.02)
    }

suspend fun controlDrivetrain() = doTask {
    val drivetrain = use<Drivetrain>()
    action {
        onTick {
            val scaledThrottle = (-throttle.sign * (throttle * throttle)).deadband(.05)
            val scaledTurn = (turn.sign * (turn * turn)).deadband(.05)
            drivetrain.arcadeDrive(scaledThrottle, scaledTurn)
        }
    }
}

suspend fun alignToBall() = doTask {
    val drivetrain = use<Drivetrain>()
    var loopsStill = 0
    var visionLastAlive = try {
        wpiTable(listOf("Vision")).getEntry("last_alive")
    } catch (e: Throwable) {
        null
    }
    var visionAngle = try {
        wpiTable(listOf("Vision")).getEntry("xAngOff")
    } catch (e: Throwable) {
        null
    }
    var lastAlive = visionLastAlive?.value?.double
    var lastAngle = visionAngle?.value?.double?.let { it + drivetrain.rawHeading }

    action {
        val pidConfig = PidfConfig()
        pidConfig.kf = 0.0
        pidConfig.kp = 0.01
        pidConfig.ki = 0.0
        pidConfig.kd = 0.0
        val controller = PidfController2(pidConfig, 1.0)
        onTick {
            if (visionLastAlive == null){
                visionLastAlive = try {
                    wpiTable(listOf("Vision")).getEntry("last_alive")
                } catch (e: Throwable) {
                    null
                }
                visionAngle = try {
                    wpiTable(listOf("Vision")).getEntry("xAngOff")
                } catch (e: Throwable) {
                    null
                }
            }
            if (lastAlive != visionLastAlive?.value?.double){
                lastAlive = visionLastAlive?.value?.double
                lastAngle = visionAngle?.value?.double?.let { it + drivetrain.rawHeading }
            }

            val turnValue = controller.next(0.0, lastAngle?.let { (drivetrain.rawHeading - lastAngle!!).IEEErem(360.0) } ?: 0.0)
            drivetrain.arcadeDrive(1.0, -turnValue)

            if (lastAngle?.let { abs((drivetrain.rawHeading - lastAngle!!)) < 0.3 } == true) {
                loopsStill += 1
            } else {
                loopsStill = 0
            }

            if (loopsStill == 11){
                println("done")
                this@action.cancel()
            }
        }
    }
}

typealias Velocity = CompositeUnitType<Per, Linear, Chronic>
typealias Acceleration = CompositeUnitType<Per, Velocity, Chronic>
typealias Jerk = CompositeUnitType<Per, Acceleration, Chronic>

val Number.mpss: MetricValue<Acceleration, MetricUnit<Acceleration>> get() = MetricValue(Meters / Seconds / Seconds, toDouble())
val Number.mpsss: MetricValue<Jerk, MetricUnit<Jerk>> get() = MetricValue(Meters / Seconds / Seconds / Seconds, toDouble())
val Number.mpms get() = MetricValue(Meters / Milliseconds, toDouble())

suspend fun <T : MetricUnit<Linear>> driveCurve(
        jerk: MetricValue<Jerk, MetricUnit<Jerk>>,
        acceleration: MetricValue<Acceleration, MetricUnit<Acceleration>>,
        velocity: MetricValue<CompositeUnitType<Per, Linear, Chronic>, CompositeUnit<Per, Linear, Chronic>>,
        distance: MetricValue<Linear, T>
) = doTask {
    var speedSetpoint by tableEntry(0.0, "Drivetrain", name = "SpeedSetpoint")
    val dt = use<Drivetrain>()
    action {
        dt.zeroEncoders()

        val curve = MotionCurve(
                distance.convertTo(Meters).value,
                velocity.convertTo(Meters / Milliseconds).value,
                acceleration.convertTo(Meters / Milliseconds / Milliseconds).value,
                jerk.convertTo(Meters / Milliseconds / Milliseconds / Milliseconds).value
        )

        timer(20, 0, curve.t7.toLong()) {
            speedSetpoint = curve.v(it.toDouble())
            val setPoint =
                    ((curve.v(it.toDouble()).mpms.convertTo(Meters / Seconds) / wheelRadius).value.rps.convertTo(EncoderTicks(TICKS_PER_REVOLUTION) / Seconds).value * 10).toInt()
            dt.setTargetSpeed(setPoint)
        }
    }
}

