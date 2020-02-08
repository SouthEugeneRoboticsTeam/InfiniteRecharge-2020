package org.sert2521.infiniterecharge2020.colorwheelspin

import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.I2C
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem

class ColorWheelSpinner : Subsystem("ColorWheelSpinner") {
    val sensor = ColorSensorV3(I2C.Port.kOnboard)

    private val spinner = MotorController(MotorControllers.spinner)

    fun useMotor(speed: Double) {
        spinner.setPercentOutput(speed)
    }
}
