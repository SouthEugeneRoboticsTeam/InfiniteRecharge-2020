package org.sert2521.infiniterecharge2020.climber;

import edu.wpi.first.wpilibj.DigitalInput
import kotlinx.coroutines.CoroutineScope
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.Sensors.BOTTOM_LIMIT_SWITCH
import org.sert2521.infiniterecharge2020.Sensors.TOP_LIMIT_SWITCH
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.telemetry.linkTableEntry

class Climber : Subsystem("Climber") {
    private val climberLiftMotor = MotorController(
            MotorControllers.CLIMBER_LIFT
    )

    val topLimitSwitch = DigitalInput(BOTTOM_LIMIT_SWITCH)
    val bottomLimitSwitch = DigitalInput(TOP_LIMIT_SWITCH)

    val atBottom get() = !topLimitSwitch.get()
    val atTop get() = !bottomLimitSwitch.get()

    val position = climberLiftMotor.position

    init {
        climberLiftMotor.position = 0
        RobotScope.linkTableEntry("At Bottom", name) { atBottom }
        RobotScope.linkTableEntry("At Top", name) { atTop }
        RobotScope.linkTableEntry("Climber Position", name) { position }
    }

    fun climberLiftUp() {
        climberLiftMotor.setPercentOutput(CLIMBER_LIFT_SPEED)
    }

    fun climberLiftDown() {
        climberLiftMotor.setPercentOutput(-CLIMBER_LIFT_SPEED)
    }

    fun climberStop() {
        climberLiftMotor.setPercentOutput(0.0)
    }
}

