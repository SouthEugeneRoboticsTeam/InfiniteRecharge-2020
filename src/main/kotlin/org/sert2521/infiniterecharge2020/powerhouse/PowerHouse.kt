package org.sert2521.infiniterecharge2020.powerhouse

import edu.wpi.first.wpilibj.DigitalInput
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.MotorControllers.rollerRight
import org.sert2521.infiniterecharge2020.Sensors.CLOSE_SWITCH
import org.sert2521.infiniterecharge2020.Sensors.OPEN_SWITCH
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem

class PowerHouse : Subsystem("PowerHouse") {
    private val roller = MotorController(
            MotorControllers.rollerLeft, rollerRight
    ) {
        brakeMode = true
        eachFollower {
            inverted = true
        }
    }

    private val flap = MotorController(
            MotorControllers.flap
    ) {
        brakeMode = true
    }

    val topReedSensor = DigitalInput(CLOSE_SWITCH)
    val bottomReedSensor = DigitalInput(OPEN_SWITCH)

    val flapClosed get() = !topReedSensor.get()
    val flapOpen get() = !bottomReedSensor.get()
    private var flapRunning = false

    private var rollerRunning = false

    init {
//        RobotScope.linkTableEntry("Flap Closed", name) { flapClosed }
//        RobotScope.linkTableEntry("Flap Open", name) { flapOpen }
    }

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

    fun openFlap() {
        flap.setPercentOutput(FLAPPER_SPEED)
        flapRunning = true
    }

    fun closeFlap() {
        flap.setPercentOutput(-FLAPPER_SPEED)
        flapRunning = true
    }

    fun stopFlap() {
        flap.disable()
        flapRunning = false
    }
}
