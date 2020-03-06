package org.sert2521.infiniterecharge2020.climber

import edu.wpi.first.wpilibj.DigitalInput
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.Sensors.CLIMBER_BOTTOM_LIMIT_SWITCH
import org.sert2521.infiniterecharge2020.Sensors.CLIMBER_TOP_LIMIT_SWITCH
import org.sert2521.infiniterecharge2020.Sensors.RUNG_LIMIT_SWTICH
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.telemetry.linkTableEntry

class Climber : Subsystem("Climber") {
    private val liftMotor = MotorController(
            MotorControllers.climber
    )
    private val winchMotor = MotorController(
            MotorControllers.winchFront, MotorControllers.winchRear
    )

    val bottomLimitSwitch = DigitalInput(CLIMBER_BOTTOM_LIMIT_SWITCH)
    val topLimitSwitch = DigitalInput(CLIMBER_TOP_LIMIT_SWITCH)
    val rungLimitSwitch = DigitalInput(RUNG_LIMIT_SWTICH)

    val atBottom get() = !bottomLimitSwitch.get()
    val atTop get() = !topLimitSwitch.get()
    // Should return true when pushed back, false when not
    val contactingRung get() = rungLimitSwitch.get()

    var position
        get() = liftMotor.position
        set(value) {
            liftMotor.position = value
        }

    var targetTimeIndex = 0.0

    init {
        liftMotor.position = 0
        // See if these are lagging inputs when climbing
        RobotScope.linkTableEntry("At Bottom", name) { atBottom }
        RobotScope.linkTableEntry("At Top", name) { atTop }
        RobotScope.linkTableEntry("Climber Position", name) { position }
        RobotScope.linkTableEntry("Contacting Rung", name) { contactingRung }
        ({ atBottom }).watch {
            RobotScope.whenTrue {
                position = 0
            }
        }
        ({ atTop }).watch {
            RobotScope.whenTrue {
                position = POSITION_AT_TOP
            }
        }
    }

    fun climberLiftUp() {
        if (position > POSITION_AT_TOP - 5000) {
            println("Encoder: $position, Going Up Half Speed")
            println("Slowing down going up")
            liftMotor.setPercentOutput(CLIMBER_LIFT_SPEED / 4)
        } else {
            println("Encoder: $position, Going Up Normal Speed")
            liftMotor.setPercentOutput(CLIMBER_LIFT_SPEED)
        }
    }

    fun climberLiftDown() {
        if (position < POSITION_AT_BOTTOM + 5000) {
            println("Encoder: $position, Going Up Half Speed")
            println("Slowing down going up")
            liftMotor.setPercentOutput(-CLIMBER_LIFT_SPEED / 4)
        } else {
            println("Encoder: $position, Going Up Normal Speed")
            liftMotor.setPercentOutput(-CLIMBER_LIFT_SPEED)
        }
    }

    fun climberStop() {
        liftMotor.setPercentOutput(0.0)
    }

    fun runWinch(speed: Double) {
        println("Speed: $speed")
        winchMotor.setPercentOutput(speed)
    }

    fun stopWinch() {
        winchMotor.disable()
    }
}
