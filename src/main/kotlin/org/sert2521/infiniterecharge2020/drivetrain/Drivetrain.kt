package org.sert2521.infiniterecharge2020.drivetrain

import com.ctre.phoenix.motorcontrol.ControlMode
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.motors.Encoder
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.telemetry.linkTableEntry
import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.sertain.coroutines.delayUntil
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.motors.MotorPidf
import org.sert2521.sertain.telemetry.Table
import org.sert2521.sertain.telemetry.TableEntry
import org.sert2521.sertain.telemetry.tableEntry
import org.sert2521.sertain.telemetry.withTableEntry

class Drivetrain : Subsystem("Drivetrain", ::controlDrivetrain) {
    val table = Table(name)

    val kp = TableEntry("KP", 2.0, table)
    val ki = TableEntry("KI", 0.005, table)
    val kd = TableEntry("KD", 0.3, table)
    val kfLeft = TableEntry("KF Left", 0.08, table)
    val kfRight = TableEntry("KF Right", 0.0725, table)

    val rightDrive = MotorController(MotorControllers.rightFront, MotorControllers.rightBack) {
        inverted = true
        brakeMode = true
        sensorInverted = true
//        encoder = Encoder(PULSES_PER_REVOLUTION)
        RobotScope.withTableEntry(kp) { pidf { kp = it } }
        RobotScope.withTableEntry(ki) { pidf { ki = it } }
        RobotScope.withTableEntry(kd) { pidf { kd = it } }
        RobotScope.withTableEntry(kfRight) { pidf { kf = it } }
    }

    val leftDrive = MotorController(MotorControllers.leftFront, MotorControllers.leftBack) {
        brakeMode = true
        sensorInverted = true
//        encoder = Encoder(PULSES_PER_REVOLUTION)
        RobotScope.withTableEntry(kp) { pidf { kp = it } }
        RobotScope.withTableEntry(ki) { pidf { ki = it } }
        RobotScope.withTableEntry(kd) { pidf { kd = it } }
        RobotScope.withTableEntry(kfLeft) { pidf { kf = it } }
    }

    val gyro = AHRS(I2C.Port.kMXP)

    val rightPosition get() = rightDrive.position
    val leftPosition get() = leftDrive.position
    val rightSpeed get() = rightDrive.velocity
    val leftSpeed get() = leftDrive.velocity
    val heading get() = gyro.angle

    init {
        zeroEncoders()
        RobotScope.linkTableEntry("Right Position", name) { rightPosition }
        RobotScope.linkTableEntry("Left Position", name) { leftPosition }
        RobotScope.linkTableEntry("Right Velocity", name) { rightSpeed }
        RobotScope.linkTableEntry("Left Velocity", name) { leftSpeed }
        RobotScope.linkTableEntry("Heading", name) { heading }
    }

    fun arcadeDrive(speed: Double, turn: Double) {
        rightDrive.setPercentOutput(speed - turn)
        leftDrive.setPercentOutput(speed + turn)
    }

    fun tankDrive(rightSpeed: Double, leftSpeed: Double) {
        rightDrive.setPercentOutput(rightSpeed)
        leftDrive.setPercentOutput(leftSpeed)
    }

    fun setTargetSpeed(leftSpeed: Int, rightSpeed: Int = leftSpeed) {
        leftDrive.ctreMotorController.set(ControlMode.Velocity, leftSpeed.toDouble())
        rightDrive.ctreMotorController.set(ControlMode.Velocity, rightSpeed.toDouble())
    }

    fun setTargetPosition(targetPosition: Int, t: Int = targetPosition) {
        rightDrive.setTargetPosition(targetPosition)
        leftDrive.setTargetPosition(targetPosition)
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
