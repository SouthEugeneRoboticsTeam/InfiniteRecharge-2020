package org.sert2521.infiniterecharge2020.powerhub

import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.MotorControllers.INTAKE_B
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem

class PowerHub : Subsystem("PowerHub") {
    private val roller = MotorController(
            MotorControllers.INTAKE_A, INTAKE_B
    ) {
        inverted = true
        brakeMode = true
    }

    private var intakeRunning = false

    fun spin() {
        roller.setPercentOutput(INTAKE_SPEED)
        intakeRunning = true
    }

    fun spinReverse() {
        roller.setPercentOutput(-INTAKE_SPEED)
        intakeRunning = true
    }

    fun stopSpin() {
        roller.disable()
        intakeRunning = false
    }

    private val flapperMotor = MotorController(
        MotorControllers.FLAPPER
    ) {
        inverted = true
        brakeMode = true
    }

    private var flapperRunning = false

    fun raiseFlapper() {
        roller.setPercentOutput(FLAPPER_SPEED)
        flapperRunning = true
    }

    fun lowerFlapper() {
        roller.setPercentOutput(-FLAPPER_SPEED)
        flapperRunning = true
    }

    fun stopFlapper() {
        roller.disable()
        flapperRunning = false
    }
}

