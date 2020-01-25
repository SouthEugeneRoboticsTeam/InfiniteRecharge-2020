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

class Drivetrain : Subsystem("Drivetrain", ::controlDrivetrain) {
    val rightDrive = MotorController(MotorControllers.rightFront, MotorControllers.rightBack) {
        inverted = true
        brakeMode = true
        sensorInverted = true
        encoder = Encoder(PULSES_PER_REVOLUTION)
        pidf {
            kp = .202
            ki = 0.0
            kd = 0.3
        }
    }

    val leftDrive = MotorController(MotorControllers.leftFront, MotorControllers.leftBack) {
        brakeMode = true
        sensorInverted = true
        encoder = Encoder(PULSES_PER_REVOLUTION)
        pidf {
            kp = .202
            ki = 0.0
            kd = 0.3
        }
    }

    val gyro = AHRS(I2C.Port.kMXP)

    val rightPosition get() = rightDrive.position
    val leftPosition get() = leftDrive.position
    val rightSpeed get() = rightDrive.velocity
    val leftSpeed get() = leftDrive.velocity
    val heading get() = gyro.angle

    init {
        zeroEncoders()
        RobotScope.linkTableEntry("Right Position", listOf(name)) { rightPosition }
        RobotScope.linkTableEntry("Left Position", listOf(name)) { leftPosition }
        RobotScope.linkTableEntry("Right Velocity", listOf(name)) { rightSpeed }
        RobotScope.linkTableEntry("Left Velocity", listOf(name)) { leftSpeed }
        RobotScope.linkTableEntry("Heading", listOf(name)) { heading }
    }

    fun arcadeDrive(speed: Double, turn: Double) {
        rightDrive.setPercentOutput(speed - turn)
        leftDrive.setPercentOutput(speed + turn)
    }

    fun tankDrive(rightSpeed: Double, leftSpeed: Double) {
        rightDrive.setPercentOutput(rightSpeed)
        leftDrive.setPercentOutput(leftSpeed)
    }

    fun setTargetSpeeds(leftSpeed: Int, rightSpeed: Int = leftSpeed) {
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
