package org.sert2521.infiniterecharge2020.drivetrain

import com.ctre.phoenix.motorcontrol.ControlMode
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry
import kotlinx.coroutines.launch
import kotlin.math.IEEErem
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.telemetry.Table
import org.sert2521.sertain.telemetry.TableEntry
import org.sert2521.sertain.telemetry.withTableEntry
import org.sert2521.sertain.units.ChronicUnit
import org.sert2521.sertain.units.LinearUnit
import org.sert2521.sertain.units.Meters
import org.sert2521.sertain.units.MetricValue
import org.sert2521.sertain.units.Radians
import org.sert2521.sertain.units.Seconds
import org.sert2521.sertain.units.VelocityValue
import org.sert2521.sertain.units.convertTo
import org.sert2521.sertain.units.div
import org.sert2521.sertain.units.rdps

class Drivetrain : Subsystem("Drivetrain", ::controlDrivetrain) {
    companion object {
        var slowAtColorWheel = false
    }

    val table = Table(name)

    init {
        RobotScope.launch {
            periodic(20) {
                odometry.update(
                        Rotation2d.fromDegrees(heading),
                        MetricValue(motorEncoder.ticks, leftDrive.position.toDouble()).convertTo(Radians).value * wheelRadius.value,
                        MetricValue(motorEncoder.ticks, rightDrive.position.toDouble()).convertTo(Radians).value * wheelRadius.value
                )
            }
        }
    }

    val drivetrainPIDF = if (isPracticeBot == RobotType.PRACTICE) practiceBotPid else compBotPid

    // Get PID gains from Shuffleboard
    val kp = TableEntry("KP", drivetrainPIDF.kp, table)
    val ki = TableEntry("KI", drivetrainPIDF.ki, table)
    val kd = TableEntry("KD", drivetrainPIDF.kd, table)
    val kfLeft = TableEntry("KF Left", drivetrainPIDF.kfLeft, table)
    val kfRight = TableEntry("KF Right", drivetrainPIDF.kfRight, table)

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
    // Return the robot's heading in degrees, from -180 to 180
    val heading get() = -gyro.angle.IEEErem(360.0)
    val rawHeading get() = -gyro.angle

    val xTranslation get() = odometry.poseMeters.translation.x
    val yTranslation get() = odometry.poseMeters.translation.y

    init {
        zeroEncoders()
//        RobotScope.linkTableEntry("Right Position", name) { rightPosition }
//        RobotScope.linkTableEntry("Left Position", name) { leftPosition }
//        RobotScope.linkTableEntry("Right Velocity", name) { rightSpeed }
//        RobotScope.linkTableEntry("Left Velocity", name) { leftSpeed }
//        RobotScope.linkTableEntry("Heading", name) { heading }
//
//        RobotScope.linkTableEntry("X Translation", name) { xTranslation }
//        RobotScope.linkTableEntry("Y Translation", name) { yTranslation }
//        RobotScope.linkTableEntry("Transformation Angle", name) { odometry.poseMeters.rotation.degrees }
    }

    fun arcadeDrive(speed: Double, turn: Double) {
        if (slowMode.value) {
            leftDrive.setPercentOutput((speed / 2) + turn)
            rightDrive.setPercentOutput((speed / 2) - turn)
        }
        leftDrive.setPercentOutput(speed + turn)
        rightDrive.setPercentOutput(speed - turn)
    }

    fun tankDrive(leftSpeed: Double, rightSpeed: Double) {
        if (slowMode.value) {
            leftDrive.setPercentOutput(leftSpeed / 2)
            rightDrive.setPercentOutput(rightSpeed / 2)
        }
        leftDrive.setPercentOutput(leftSpeed)
        rightDrive.setPercentOutput(rightSpeed)
    }

    fun setTargetSpeed(leftSpeed: Int, rightSpeed: Int = leftSpeed) {
        leftDrive.ctreMotorController.set(ControlMode.Velocity, leftSpeed.toDouble())
        rightDrive.ctreMotorController.set(ControlMode.Velocity, rightSpeed.toDouble())
    }

    val leftSpeedSetpoint = TableEntry("LeftSpeedSetpoint", 0, "Drivetrain")
    val rightSpeedSetpoint = TableEntry("RightSpeedSetpoint", 0, "Drivetrain")
    val leftSpeedError = TableEntry("LeftSpeedError", 0, "Drivetrain")
    val rightSpeedError = TableEntry("RightSpeedError", 0, "Drivetrain")

    fun <L : LinearUnit, C : ChronicUnit> setTargetSpeed(leftSpeed: VelocityValue<L, C>, rightSpeed: VelocityValue<L, C>) {
        // Convert speed setpoints from meters-per-second into encoder ticks
        val leftSetpoint = ((leftSpeed.convertTo(Meters / Seconds).value / wheelRadius.value).rdps.convertTo(motorEncoder.ticksPerSecond).value / 10).toInt()
        val rightSetpoint = ((rightSpeed.convertTo(Meters / Seconds).value / wheelRadius.value).rdps.convertTo(motorEncoder.ticksPerSecond).value / 10).toInt()
        // Log speed setpoints to NT
        leftSpeedSetpoint.value = leftSetpoint
        rightSpeedSetpoint.value = rightSetpoint
        leftSpeedError.value = this.leftSpeed - leftSetpoint
        rightSpeedError.value = this.rightSpeed - rightSetpoint

        leftDrive.setTargetVelocity(leftSetpoint)
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
