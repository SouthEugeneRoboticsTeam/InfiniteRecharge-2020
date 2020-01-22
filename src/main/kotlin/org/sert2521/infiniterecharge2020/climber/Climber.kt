package org.sert2521.infiniterecharge2020.climber;

import org.sert2521.infiniterecharge2020.MotorControllers
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

    private var intakeRunning = false

    fun ClimberLiftUp() {
        intakeMotor.setPercentOutput(CLIMBER_LIFT_SPEED)
        intakeRunning = true
    }

    fun ClimberLiftDown() {
        intakeMotor.setPercentOutput(-CLIMBER_LIFT_SPEED)
        intakeRunning = true
    }

    fun StopClimberLift() {
        intakeMotor.disable()
        intakeRunning = false
    }
}

class ClimberWench : Subsystem("ClimberWench") {
    private val intakeMotor = MotorController(
            MotorControllers.CLIMBER_WENCH
    ) {
        inverted = true
        brakeMode = true
        maxOutputRange = -0.75..0.75
    }

    private var intakeRunning = false

    fun ClimberWenchGo() {
        intakeMotor.setPercentOutput(CLIMBER_WENCH)
        intakeRunning = true
    }

    fun StopClimberWenchGo() {
        intakeMotor.disable()
        intakeRunning = false
    }
}

