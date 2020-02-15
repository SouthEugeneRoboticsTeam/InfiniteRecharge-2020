package org.sert2521.infiniterecharge2020.climber

import edu.wpi.first.wpilibj.DigitalInput
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.Sensors.TOP_LIMIT_SWITCH
import org.sert2521.infiniterecharge2020.Sensors.BOTTOM_LIMIT_SWITCH
import org.sert2521.sertain.control.MotionCurve
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.events.onTick
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

    val topLimitSwitch = DigitalInput(BOTTOM_LIMIT_SWITCH)
    val bottomLimitSwitch = DigitalInput(TOP_LIMIT_SWITCH)

    val atBottom get() = topLimitSwitch.get()
    val atTop get() = !bottomLimitSwitch.get()

    var position
        get() = liftMotor.position
        set(value) {
            liftMotor.position = value
        }

    var targetTimeIndex = 0.0

    init {
        liftMotor.position = 0
        RobotScope.linkTableEntry("At Bottom", name) { atBottom }
        RobotScope.linkTableEntry("At Top", name) { atTop }
        RobotScope.linkTableEntry("Climber Position", name) { position }
        ({ atBottom }).watch {
            RobotScope.whenTrue {
                position = POSITION_AT_BOTTOM
            }
        }
        ({ atTop }).watch {
            RobotScope.whenTrue {
                position = POSITION_AT_TOP
            }
        }
    }

    fun setOutput(output: Double) {
        if (position > POSITION_AT_TOP - 500) {
            liftMotor.setPercentOutput(output / 2)
        } else {
            liftMotor.setPercentOutput(output)
        }
    }

    fun climberLiftDown() {
        if (position < POSITION_AT_BOTTOM + 500) {
            liftMotor.setPercentOutput(-CLIMBER_LIFT_SPEED / 2)
        } else {
            liftMotor.setPercentOutput(-CLIMBER_LIFT_SPEED)
        }
    }

    fun climberStop() {
        liftMotor.setPercentOutput(0.0)
    }

    fun runWinchClockwise(speed: Double) {
        println("Speed: ${speed}")
        winchMotor.setPercentOutput(speed)
    }

    fun runWinchCC(speed: Double) {
        winchMotor.setPercentOutput(-speed)
    }
}