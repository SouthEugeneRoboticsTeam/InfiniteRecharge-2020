package org.sert2521.infiniterecharge2020.climber;

import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.MotorControllers.CLIMBER_WINCH_A
import org.sert2521.infiniterecharge2020.MotorControllers.CLIMBER_WINCH_B
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem

class Climber : Subsystem("Climber") {
    private val climberLiftMotor = MotorController(
            MotorControllers.CLIMBER_LIFT
    )

    fun climberLiftUp() {
        climberLiftMotor.setPercentOutput(CLIMBER_LIFT_SPEED)
    }

    fun climberLiftDown() {
        climberLiftMotor.setPercentOutput(-CLIMBER_LIFT_SPEED)
    }
}

