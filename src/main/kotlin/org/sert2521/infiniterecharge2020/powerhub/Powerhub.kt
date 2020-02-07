package org.sert2521.infiniterecharge2020.powerhub

import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.MotorControllers.ROLLER_RIGHT
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem

class PowerHub : Subsystem("PowerHub") {
    private val roller = MotorController(
            MotorControllers.ROLLER_LEFT, ROLLER_RIGHT
    ) {
        inverted = true
        brakeMode = true
    }

    private var rollerRunning= false

    fun spin() {
        roller.setPercentOutput(ROLLER_SPEED)
        rollerRunning = true
    }

    fun spinReverse() {
        roller.setPercentOutput(-ROLLER_SPEED)
        rollerRunning = true
    }

    fun stopSpin() {
        roller.disable()
        rollerRunning = false
    }

    private val flapperMotor = MotorController(
        MotorControllers.FLAPPER
    ) {
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
}
