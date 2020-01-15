package org.sert2521.infiniterecharge2020.colorwheelspin

import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.I2C
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem

class ColorWheelSpin : Subsystem("ColorWheelSpin") {
    val sensor = ColorSensorV3(I2C.Port.kOnboard)

    private val extender = MotorController(MotorControllers.extender)
    private val spinner = MotorController(MotorControllers.spinner)

    fun extendOut(speed: Double){
        extender.setPercentOutput(speed)
    }

    fun extendIn(speed: Double){
        extender.setPercentOutput(-speed)
    }

    fun spin(speed: Double){
        spinner.setPercentOutput(speed)
    }
}