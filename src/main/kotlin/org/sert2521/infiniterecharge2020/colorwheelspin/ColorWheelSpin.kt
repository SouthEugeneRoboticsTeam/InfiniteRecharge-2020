package org.sert2521.infiniterecharge2020.colorwheelspin

import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.util.Color.*
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.telemetry.linkTableEntry

class ColorWheelSpinner : Subsystem("ColorWheelSpinner") {
    val sensor = ColorSensorV3(I2C.Port.kOnboard)

    val frcColorToString = mapOf('Y' to "Yellow", 'B' to "Blue", 'G' to "Green", 'R' to "Red")

    init {
        RobotScope.linkTableEntry("Target Color", name) { frcColorToString[DriverStation.getInstance().gameSpecificMessage.first()] }
    }

    val frcColorToTargetColor = mapOf('Y' to kGreen, 'B' to kRed, 'G' to kYellow, 'R' to kBlue)

    private val spinner = MotorController(MotorControllers.spinner)

    fun output(value: Double) {
        spinner.setPercentOutput(value)
    }
}
