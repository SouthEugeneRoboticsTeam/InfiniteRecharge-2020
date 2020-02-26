package org.sert2521.infiniterecharge2020.colorwheelspinner

import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.I2C
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.telemetry.linkTableEntry

class ColorWheelSpinner : Subsystem("ColorWheelSpinner") {
    val sensor = ColorSensorV3(I2C.Port.kOnboard)

    init {
        RobotScope.linkTableEntry("Target Color", name) { frcColorToString[DriverStation.getInstance().gameSpecificMessage.first()] }
    }

    private val spinner = MotorController(MotorControllers.spinner)

    fun output(value: Double) {
        spinner.setPercentOutput(value)
    }

    fun retract() {
        spinner.setPercentOutput(-SPINNER_SPEED)
    }

    fun stop() {
        spinner.disable()
    }
}
