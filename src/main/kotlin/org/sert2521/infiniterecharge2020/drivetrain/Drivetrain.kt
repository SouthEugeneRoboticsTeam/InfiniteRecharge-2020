package org.sert2521.infiniterecharge2020.drivetrain

import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.OI.controlMode
import org.sert2521.sertain.coroutines.RobotScope

import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.telemetry.linkTableEntry

class Drivetrain : Subsystem("Drivetrain", ::controlDrivetrain) {
    private val right = MotorController(MotorControllers.rightFront, MotorControllers.rightBack) {
        inverted = true
        brakeMode = true
        sensorInverted = true
        pidf {
            kp = 0.0
            ki = 0.0
            kd = 0.0
        }
    }
    private val left = MotorController(MotorControllers.leftFront, MotorControllers.leftBack) {
        brakeMode = true
        sensorInverted = true
        pidf {
            kp = 0.0
            ki = 0.0
            kd = 0.0
        }
    }

    val rightPosition get() = right.position
    val leftPosition get() = left.position
    val position get() = (rightPosition + leftPosition) / 2


    init {
        zeroEncoders()
        RobotScope.linkTableEntry("Position", listOf("Operator")) { position }
        RobotScope.linkTableEntry("Control Mode", listOf("Drivetrain")) { controlMode }
    }

    fun arcadeDrive(speed: Double, turn: Double) {
        right.setPercentOutput(speed - turn)
        left.setPercentOutput(speed + turn)
    }

    fun tankDrive(rightSpeed: Double, leftSpeed: Double) {
        right.setPercentOutput(rightSpeed)
        left.setPercentOutput(leftSpeed)
    }

    fun driveToPosition(targetPosition: Int) {
        right.setTargetPosition(targetPosition)
        left.setTargetPosition(targetPosition)
    }

    fun stop() {
        right.setPercentOutput(0.0)
        left.setPercentOutput(0.0)
    }

    fun zeroEncoders() {
        right.position = 0
        left.position = 0
    }

}