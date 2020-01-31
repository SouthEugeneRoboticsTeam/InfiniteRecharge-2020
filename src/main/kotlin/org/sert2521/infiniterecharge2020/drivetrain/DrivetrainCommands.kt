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

//suspend fun drivePath(
//        trajectory: Trajectory,
//        pose: () -> Pose2d,
//        controller: RamseteController,
//        feedforward: SimpleMotorFeedforward,
//        kinematics: DifferentialDriveKinematics,
//        wheelSpeeds: () -> DifferentialDriveWheelSpeeds,
//        leftController: PIDController,
//        rightController: PIDController,
//        outputVolts: (Double, Double) -> Unit
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
//            ).leftMetersPerSecond
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
