package org.sert2521.infiniterecharge2020.climber;

import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.MotorControllers.CLIMBER_WINCH_A
import org.sert2521.infiniterecharge2020.MotorControllers.CLIMBER_WINCH_B
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem

class Climber : Subsystem("Climber") {
    private val ClimberLiftMotor = MotorController(
            MotorControllers.CLIMBER_LIFT

    ) {
        inverted = true
        brakeMode = true
        maxOutputRange = -0.75..0.75
    }

    private var intakeRunning = false

    fun ClimberLiftUp() {
        ClimberLiftMotor.setPercentOutput(CLIMBER_LIFT_SPEED)
        intakeRunning = true
    }

    fun ClimberLiftDown() {
        ClimberLiftMotor.setPercentOutput(-CLIMBER_LIFT_SPEED)
        intakeRunning = true
    }

    fun StopClimberLift() {
        ClimberLiftMotor.disable()
        intakeRunning = false

    }

    private val ClimberWinchMotor = MotorController(
            MotorControllers.CLIMBER_WINCH_A, CLIMBER_WINCH_B

    ) {
        inverted = true
        brakeMode = true
        maxOutputRange = -0.75..0.75
    }

    fun ClimberWinchGo() {
            ClimberWinchMotor.setPercentOutput(CLIMBER_WINCH)
            intakeRunning = true
    }

    fun StopClimberWinchGo() {
            ClimberWinchMotor.disable()
            intakeRunning = false
        }
    }


