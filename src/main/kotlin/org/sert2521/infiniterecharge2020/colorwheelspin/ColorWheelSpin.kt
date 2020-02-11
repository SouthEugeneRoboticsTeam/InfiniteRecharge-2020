package org.sert2521.infiniterecharge2020.colorwheelspin

import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.util.Color.*
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem

class ColorWheelSpinner : Subsystem("ColorWheelSpinner") {
    val sensor = ColorSensorV3(I2C.Port.kOnboard)

    //accounts for offset
    val frcColorToTargetColor = mapOf('Y' to kGreen, 'B' to kRed, 'G' to kYellow, 'R' to kBlue)

    private val spinner = MotorController(MotorControllers.spinner)

    fun output(value: Double) {
        spinner.setPercentOutput(value)
    }
}
