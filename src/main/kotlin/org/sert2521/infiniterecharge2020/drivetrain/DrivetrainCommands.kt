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
import org.sert2521.infiniterecharge2020.drivetrain
import org.sert2521.infiniterecharge2020.utils.PidfController2
import org.sert2521.infiniterecharge2020.utils.deadband
import org.sert2521.sertain.control.PidfConfig
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.telemetry.TableEntry
import org.sert2521.sertain.units.Chronic
import org.sert2521.sertain.units.CompositeUnitType
import org.sert2521.sertain.units.Linear
import org.sert2521.sertain.units.Meters
import org.sert2521.sertain.units.MetricUnit
import org.sert2521.sertain.units.MetricValue
import org.sert2521.sertain.units.Milliseconds
import org.sert2521.sertain.units.Per
import org.sert2521.sertain.units.Seconds
import org.sert2521.sertain.units.div
import org.sert2521.sertain.units.mps
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

suspend fun controlDrivetrain() = drivetrain { drivetrain ->
    periodic(20) {
        val controlPanelSpeedScalar = if (Drivetrain.slowAtColorWheel) 0.14 else 1.0
        val scaledThrottle = (-throttle.sign * (throttle * throttle)).deadband(.05) * controlPanelSpeedScalar
        val scaledTurn = turn.deadband(.05)
        drivetrain.arcadeDrive(scaledThrottle, scaledTurn)
    }
}

suspend fun alignToBall(offset: Double, oneSide: Boolean = false) = drivetrain { drivetrain ->
    var visionLastAlive = TableEntry("last_alive", 0.0, "Vision")
    var visionAngle = TableEntry("xAngOff", 0.0, "Vision")
    var lastAlive = visionLastAlive.value + drivetrain.rawHeading
    var lastAngle = visionAngle.value + drivetrain.rawHeading

    val pidConfig = PidfConfig()
    pidConfig.kf = 0.0
    pidConfig.kp = 0.01
    pidConfig.ki = 0.0
    pidConfig.kd = 0.0
    val controller = PidfController2(pidConfig, 1.0)
    try {
        periodic(20) {
            if (lastAlive != visionLastAlive.value) {
                lastAlive = visionLastAlive.value + drivetrain.rawHeading
                if (oneSide) {
                    lastAngle = visionAngle.value - drivetrain.rawHeading + offset
                } else {
                    lastAngle = visionAngle.value - drivetrain.rawHeading + (sign(visionAngle.value) * offset)
                }
            }

            val turnValue = controller.next(0.0, (drivetrain.rawHeading - lastAngle).IEEErem(360.0))
            drivetrain.arcadeDrive(autoAlignSpeed, -turnValue)
        }
    } finally {
        drivetrain.stop()
    }
}

typealias Velocity = CompositeUnitType<Per, Linear, Chronic>
typealias Acceleration = CompositeUnitType<Per, Velocity, Chronic>
typealias Jerk = CompositeUnitType<Per, Acceleration, Chronic>

val Number.mpss: MetricValue<Acceleration, MetricUnit<Acceleration>> get() = MetricValue(Meters / Seconds / Seconds, toDouble())
val Number.mpsss: MetricValue<Jerk, MetricUnit<Jerk>> get() = MetricValue(Meters / Seconds / Seconds / Seconds, toDouble())
val Number.mpms get() = MetricValue(Meters / Milliseconds, toDouble())

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
