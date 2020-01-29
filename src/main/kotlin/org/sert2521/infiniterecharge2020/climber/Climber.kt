package org.sert2521.infiniterecharge2020.climber;

import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.MotorControllers.CLIMBER_WINCH_A
import org.sert2521.infiniterecharge2020.MotorControllers.CLIMBER_WINCH_B
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem

class ClimberLift : Subsystem("ClimberLift") {
    private val intakeMotor = MotorController(
            MotorControllers.CLIMBER_LIFT
    ) {
        inverted = true
        brakeMode = true
        maxOutputRange = -0.75..0.75
    }

    private var climberLiftRunning = false

    fun ClimberLiftUp() {
        intakeMotor.setPercentOutput(CLIMBER_LIFT_SPEED)
        climberLiftRunning = true
    }

    fun ClimberLiftDown() {
        intakeMotor.setPercentOutput(-CLIMBER_LIFT_SPEED)
        climberLiftRunning = true
    }

    fun StopClimberLift() {
        intakeMotor.disable()
        climberLiftRunning = false
    }
}

class ClimberWinch : Subsystem("ClimberWinch") {
    private val intakeMotor = MotorController(
            CLIMBER_WINCH_A, CLIMBER_WINCH_B
    ) {
        inverted = true
        brakeMode = true
        maxOutputRange = -0.75..0.75
    }

    private var climberWinchRunning = false

    fun ClimberWinchGo() {
        intakeMotor.setPercentOutput(CLIMBER_WINCH)
        climberWinchRunning = true
    }

    fun StopClimberWinchGo() {
        intakeMotor.disable()
        climberWinchRunning = false
    }
}

