package org.sert2521.infiniterecharge2020.intake

import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.MotorControllers.FLAPPER
import org.sert2521.infiniterecharge2020.MotorControllers.INTAKE_B
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem

class Intake : Subsystem("Intake") {
    private val intakeMotor = MotorController(
            MotorControllers.INTAKE_A, INTAKE_B
    ) {
        inverted = true
        brakeMode = true
        maxOutputRange = -0.75..0.75
    }

    private var intakeRunning = false

    fun spinIntake() {
        intakeMotor.setPercentOutput(INTAKE_SPEED)
        intakeRunning = true
    }

    fun spinReverse() {
        intakeMotor.setPercentOutput(-INTAKE_SPEED)
        intakeRunning = true
    }

    fun stopSpin() {
        intakeMotor.disable()
        intakeRunning = false
    }
}

class Flapper : Subsystem("Flapper") {
    private val intakeMotor = MotorController(
            MotorControllers.FLAPPER
    ) {
        inverted = true
        brakeMode = true
        maxOutputRange = -0.75..0.75
    }

    private var flapperRunning = false

    fun spinFlapper() {
        intakeMotor.setPercentOutput(FLAPPER_SPEED)
        flapperRunning = true
    }

    fun reverseFlapper() {
        intakeMotor.setPercentOutput(-FLAPPER_SPEED)
        flapperRunning = true
    }

    fun stopFlapper() {
        intakeMotor.disable()
        flapperRunning = false
    }
}
