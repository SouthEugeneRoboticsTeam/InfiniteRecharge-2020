package org.sert2521.infiniterecharge2020.drivetrain

import edu.wpi.first.wpilibj.GenericHID
import org.sert2521.infiniterecharge2020.OI.primaryJoystick
import org.sert2521.infiniterecharge2020.utils.deadband
import org.sert2521.infiniterecharge2020.OI.ControlMode
import org.sert2521.infiniterecharge2020.OI.controlMode
import org.sert2521.infiniterecharge2020.OI.primaryController
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
import org.sert2521.sertain.units.convert
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
            drivetrain.setTargetPosition(curve.d(it / 1000.0).toInt())
        }
    }
}

//suspend fun drivePath(
//        trajectory: Trajectory,
//        pose: () -> Pose2d,
//        controller: RamseteController,
//        feedforward: SimpleMotorFeedforward,
//        kinematics: DifferentialDriveKinematics,
//        wheelSpeeds: () -> DifferentialDriveWheelSpeeds,
//        leftController: PIDController,
//        rightController: PIDController,
//        outputVolts: (Metric, Double) -> Unit
//) = doTask {
//    val drivetrain = use<Drivetrain>()
//    action {
//        // initialize
//        val initialState = trajectory.sample(0.0)
//
//        var prevTime = 0.s
//        var prevSpeeds = kinematics.toWheelSpeeds(ChassisSpeeds(
//                initialState.velocityMetersPerSecond,
//                0.0,
//                initialState.curvatureRadPerMeter * initialState.velocityMetersPerSecond
//        ))
//        timer(20, 0, -1) {
//            val curTime = it.ms.convertTo(Seconds)
//            val dt = curTime - prevTime
//
//            val targetWheelSpeeds = kinematics.toWheelSpeeds(
//                    controller.calculate(pose(), trajectory.sample(curTime.value))
//            )
//
//            val leftTargetSpeed = targetWheelSpeeds.leftMetersPerSecond.mps
//            val rightTargetSpeed = targetWheelSpeeds.rightMetersPerSecond.mps
//
//            val leftyTargetSpeed = (leftTargetSpeed / wheelRadius).value.rps.convertTo(encoder.ticksPerSecond)
//            val rightyTargetSpeed = (rightTargetSpeed / wheelRadius).value.rps.convertTo(encoder.ticksPerSecond)
//
//        }
//    }
//}
