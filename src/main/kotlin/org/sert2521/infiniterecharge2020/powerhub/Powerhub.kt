package org.sert2521.infiniterecharge2020.powerhub

import edu.wpi.first.wpilibj.DigitalInput
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.MotorControllers.ROLLER_RIGHT
import org.sert2521.infiniterecharge2020.Sensors.REED_SENSOR_BOTTOM
import org.sert2521.infiniterecharge2020.Sensors.REED_SENSOR_TOP
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.telemetry.linkTableEntry

class PowerHub : Subsystem("PowerHub") {
    private val roller = MotorController(
            MotorControllers.ROLLER_LEFT, ROLLER_RIGHT
    ) {
        inverted = true
        brakeMode = true
    }

    private val flapperMotor = MotorController(
            MotorControllers.FLAPPER
    ) {
        brakeMode = true
    }

    val topReedSensor = DigitalInput(REED_SENSOR_TOP)
    val bottomReedSensor = DigitalInput(REED_SENSOR_BOTTOM)

    val flapClosed get() = !topReedSensor.get()
    val flapOpen get() = !bottomReedSensor.get()

    private var rollerRunning = false

    init {
        RobotScope.linkTableEntry("Flap Closed", name) { flapClosed }
        RobotScope.linkTableEntry("Flap Open", name) { flapOpen }
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

    private var flapperRunning = false

    fun openFlapper() {
        flapperMotor.setPercentOutput(FLAPPER_SPEED)
        flapperRunning = true
    }

    fun closeFlapper() {
        flapperMotor.setPercentOutput(-FLAPPER_SPEED)
        flapperRunning = true
    }
}
