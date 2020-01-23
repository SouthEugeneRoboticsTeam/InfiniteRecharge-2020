package org.sert2521.infiniterecharge2020.drivetrain

import edu.wpi.first.wpilibj.GenericHID
import kotlinx.coroutines.cancel
import org.sert2521.infiniterecharge2020.oi.primaryJoystick
import org.sert2521.infiniterecharge2020.utils.deadband
import org.sert2521.infiniterecharge2020.oi.ControlMode
import org.sert2521.infiniterecharge2020.oi.controlMode
import org.sert2521.infiniterecharge2020.oi.primaryController
import org.sert2521.sertain.control.MotionCurve
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.motors.EncoderTicks
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import org.sert2521.sertain.units.Chronic
import org.sert2521.sertain.units.CompositeUnit
import org.sert2521.sertain.units.CompositeUnitType
import org.sert2521.sertain.units.Linear
import org.sert2521.sertain.units.Meters
import org.sert2521.sertain.units.MetricUnit
import org.sert2521.sertain.units.MetricValue
import org.sert2521.sertain.units.Per
import org.sert2521.sertain.units.Radians
import org.sert2521.sertain.units.Seconds
import org.sert2521.sertain.units.convertTo
import org.sert2521.sertain.units.div
import org.sert2521.sertain.utils.timer
import kotlin.math.sign

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

// Drives the robot in a straight line for a certain distance (in inches) at a given output
suspend fun driveStraight(
        output: Double,
        distance: MetricValue<Linear, MetricUnit<Linear>>
) = doTask {
    val drivetrain = use<Drivetrain>()
    action {
        drivetrain.zeroEncoders()
        onTick {
            drivetrain.arcadeDrive(output, 0.0)
            // Calculates the number of encoder pulses corresponding to a certain distance
            val ticks = (PULSES_PER_REVOLUTION * distance.convertTo(Meters).value / 2.0) / (2 * Math.PI * wheelRadius.value)
            if (drivetrain.position >= ticks) {
                drivetrain.stop()
                this@action.cancel()
            }
        }
    }
}

suspend fun driveCurve(
        speed: MetricValue<CompositeUnitType<Per, Linear, Chronic>, CompositeUnit<Per, Linear, Chronic>>,
        distance: MetricValue<Linear, MetricUnit<Linear>>
) = doTask {
    val drivetrain = use<Drivetrain>()
    action {
        drivetrain.zeroEncoders()

        val ticks = MetricValue(
                Radians,
                distance.convertTo(Meters).value / wheelRadius.value
        ).convertTo(EncoderTicks(PULSES_PER_REVOLUTION.toInt())).value
        val ticksPerSecond = MetricValue(
                Radians / Seconds,
                speed.convertTo(Meters / Seconds).value / wheelRadius.value
        ).convertTo(EncoderTicks(PULSES_PER_REVOLUTION.toInt()) / Seconds).value
        val curve = MotionCurve(ticks, ticksPerSecond, 100.0, 100.0)

        timer(20, 0, (curve.t7 * 1000).toLong()) {
            drivetrain.driveToPosition(curve.d(it / 1000.0).toInt())
        }
    }
}
