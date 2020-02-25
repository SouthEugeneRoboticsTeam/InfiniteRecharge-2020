package org.sert2521.infiniterecharge2020.drivetrain

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.trajectory.Trajectory
import kotlin.math.IEEErem
import kotlin.math.sign
import org.sert2521.infiniterecharge2020.OI.ControlMode
import org.sert2521.infiniterecharge2020.OI.controlMode
import org.sert2521.infiniterecharge2020.OI.primaryController
import org.sert2521.infiniterecharge2020.OI.primaryJoystick
import org.sert2521.infiniterecharge2020.utils.PidfController2
import org.sert2521.infiniterecharge2020.utils.deadband
import org.sert2521.infiniterecharge2020.utils.timer
import org.sert2521.sertain.control.MotionCurve
import org.sert2521.sertain.control.PidfConfig
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
import org.sert2521.sertain.units.mps
import org.sert2521.sertain.units.rps
import org.sert2521.sertain.utils.timer

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

suspend fun alignToBall(offset: Double, oneside: Boolean = false) = doTask {
    val drivetrain = use<Drivetrain>()
    var visionLastAlive = TableEntry("last_alive", 0.0, "Vision")
    var visionAngle = TableEntry("xAngOff", 0.0, "Vision")
    var lastAlive = visionLastAlive.value + drivetrain.rawHeading
    var lastAngle = visionAngle.value + drivetrain.rawHeading

    action {
        val pidConfig = PidfConfig()
        pidConfig.kf = 0.0
        pidConfig.kp = 0.01
        pidConfig.ki = 0.0
        pidConfig.kd = 0.0
        val controller = PidfController2(pidConfig, 1.0)
        onTick {
            if (lastAlive != visionLastAlive.value) {
                lastAlive = visionLastAlive.value + drivetrain.rawHeading
                if(oneside) {
                    lastAngle = visionAngle.value + drivetrain.rawHeading - offset
                }else{
                    lastAngle = visionAngle.value + drivetrain.rawHeading - (sign(visionAngle.value) * offset)
                }
            }

            val turnValue = controller.next(0.0, (drivetrain.rawHeading - lastAngle).IEEErem(360.0))
            drivetrain.arcadeDrive(autoAlignSpeed, -turnValue)
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

suspend fun runPath(
    drivetrain: Drivetrain,
    trajectory: Trajectory,
    getPose: () -> Pose2d = { drivetrain.pose },
    follower: RamseteController = RamseteController(),
    pathKinematics: DifferentialDriveKinematics = kinematics,
    outputMetersPerSecond: (left: Double, right: Double) -> Unit = { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) }
) {
    timer(20, 0, (trajectory.totalTimeSeconds * 1000).toLong().also { println("Path time: $it") }) {
        val curTime = it.toDouble() / 1000

        val targetWheelSpeeds = pathKinematics.toWheelSpeeds(
                follower.calculate(getPose(), trajectory.sample(curTime))
        )

        val leftSpeedSetpoint = targetWheelSpeeds.leftMetersPerSecond
        val rightSpeedSetpoint = targetWheelSpeeds.rightMetersPerSecond

        outputMetersPerSecond(leftSpeedSetpoint, rightSpeedSetpoint)
    }
}
