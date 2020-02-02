package org.sert2521.infiniterecharge2020.drivetrain

import com.ctre.phoenix.motorcontrol.ControlMode
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.telemetry.linkTableEntry
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry
import kotlinx.coroutines.launch
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.telemetry.Table
import org.sert2521.sertain.telemetry.TableEntry
import org.sert2521.sertain.telemetry.withTableEntry
import org.sert2521.sertain.units.Chronic
import org.sert2521.sertain.units.CompositeUnit
import org.sert2521.sertain.units.Linear
import org.sert2521.sertain.units.Meters
import org.sert2521.sertain.units.MetricValue
import org.sert2521.sertain.units.Per
import org.sert2521.sertain.units.Radians
import org.sert2521.sertain.units.Seconds
import org.sert2521.sertain.units.convertTo
import org.sert2521.sertain.units.div
import org.sert2521.sertain.units.rdps
import kotlin.math.IEEErem

class Drivetrain : Subsystem("Drivetrain", ::controlDrivetrain) {
    val table = Table(name)

    init {
        RobotScope.onTick {
            odometry.update(
                    Rotation2d.fromDegrees(heading),
                    MetricValue(motorEncoder.ticks, leftDrive.position.toDouble()).convertTo(Radians).value * wheelRadius.value,
                    MetricValue(motorEncoder.ticks, rightDrive.position.toDouble()).convertTo(Radians).value * wheelRadius.value
                    )
        }
    }

    // Get PID gains from Shuffleboard
    val kp = TableEntry("KP", 2.0, table)
    val ki = TableEntry("KI", 0.005, table)
    val kd = TableEntry("KD", 0.3, table)
    val kfLeft = TableEntry("KF Left", 0.08, table)
    val kfRight = TableEntry("KF Right", 0.0725, table)

    val rightDrive = MotorController(MotorControllers.rightFront, MotorControllers.rightBack) {
        inverted = true
        brakeMode = true
        sensorInverted = true
        encoder = motorEncoder
        RobotScope.withTableEntry(kp) { pidf { kp = it } }
        RobotScope.withTableEntry(ki) { pidf { ki = it } }
        RobotScope.withTableEntry(kd) { pidf { kd = it } }
        RobotScope.withTableEntry(kfRight) { pidf { kf = it } }
    }

    val leftDrive = MotorController(MotorControllers.leftFront, MotorControllers.leftBack) {
        brakeMode = true
        sensorInverted = true
        encoder = motorEncoder
        RobotScope.withTableEntry(kp) { pidf { kp = it } }
        RobotScope.withTableEntry(ki) { pidf { ki = it } }
        RobotScope.withTableEntry(kd) { pidf { kd = it } }
        RobotScope.withTableEntry(kfLeft) { pidf { kf = it } }
    }

    val gyro = AHRS(I2C.Port.kMXP)

    val odometry = DifferentialDriveOdometry(Rotation2d.fromDegrees(heading))
    val pose get() = odometry.poseMeters

    val rightPosition get() = rightDrive.position
    val leftPosition get() = leftDrive.position
    val rightSpeed get() = rightDrive.velocity
    val leftSpeed get() = leftDrive.velocity
    val heading get() = -gyro.angle.IEEErem(360.0)

    init {
        zeroEncoders()
        RobotScope.linkTableEntry("Right Position", name) { rightPosition }
        RobotScope.linkTableEntry("Left Position", name) { leftPosition }
        RobotScope.linkTableEntry("Right Velocity", name) { rightSpeed }
        RobotScope.linkTableEntry("Left Velocity", name) { leftSpeed }
        RobotScope.linkTableEntry("Heading", name) { heading }

        RobotScope.linkTableEntry("OdometryX", name) { odometry.poseMeters.translation.x }
        RobotScope.linkTableEntry("OdometryY", name) { odometry.poseMeters.translation.y }
        RobotScope.linkTableEntry("OdometryAng", name) { odometry.poseMeters.rotation.degrees }
    }

    fun arcadeDrive(speed: Double, turn: Double) {
        leftDrive.setPercentOutput(speed + turn)
        rightDrive.setPercentOutput(speed - turn)
    }

    fun tankDrive(leftSpeed: Double, rightSpeed: Double) {
        leftDrive.setPercentOutput(leftSpeed)
        rightDrive.setPercentOutput(rightSpeed)
    }

    fun setTargetSpeed(leftSpeed: Int, rightSpeed: Int = leftSpeed) {
        leftDrive.ctreMotorController.set(ControlMode.Velocity, leftSpeed.toDouble())
        rightDrive.ctreMotorController.set(ControlMode.Velocity, rightSpeed.toDouble())
    }

    val leftSpeedSetpoint = TableEntry("LeftSpeedSetpoint", 0, "Drivetrain")
    val rightSpeedSetpoint = TableEntry("RightSpeedSetpoint", 0, "Drivetrain")
    fun <V : CompositeUnit<Per, Linear, Chronic>> setTargetSpeed(leftSpeed: MetricValue<Velocity, V>, rightSpeed: MetricValue<Velocity, V>) {
        val leftSetpoint = ((leftSpeed.convertTo(Meters / Seconds).value / wheelRadius.value).rdps.convertTo(motorEncoder.ticksPerSecond).value / 10).toInt()
        leftSpeedSetpoint.value = leftSetpoint
        leftDrive.setTargetVelocity(leftSetpoint)
        val rightSetpoint = ((rightSpeed.convertTo(Meters / Seconds).value / wheelRadius.value).rdps.convertTo(motorEncoder.ticksPerSecond).value / 10).toInt()
        rightSpeedSetpoint.value = rightSetpoint
        rightDrive.setTargetVelocity(rightSetpoint)
    }

    fun setTargetPosition(leftTargetPosition: Int, rightTargetPosition: Int) {
        leftDrive.setTargetPosition(leftTargetPosition)
        rightDrive.setTargetPosition(rightTargetPosition)
    }

    fun stop() {
        rightDrive.setPercentOutput(0.0)
        leftDrive.setPercentOutput(0.0)
    }

    fun zeroEncoders() {
        rightDrive.position = 0
        leftDrive.position = 0
    }
}
