package org.sert2521.infiniterecharge2020.colorwheelspinner

import com.revrobotics.ColorMatch
import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.util.Color
import org.sert2521.infiniterecharge2020.MotorControllers
import org.sert2521.infiniterecharge2020.Sensors.SPINNER_BOTTOM_LIMIT_SWITCH
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.motors.MotorController
import org.sert2521.sertain.subsystems.Subsystem

class ColorWheelSpinner : Subsystem("ColorWheelSpinner") {
    private val spinnerMotor = MotorController(MotorControllers.spinner) {
        inverted = true
        brakeMode = true
    }

    private val colorSensor = ColorSensorV3(I2C.Port.kOnboard)
    private val bottomLimitSwitch = DigitalInput(SPINNER_BOTTOM_LIMIT_SWITCH)

    private val m_colorMatcher = ColorMatch()

    var spinnerPosition
        get() = spinnerMotor.position
        set(value) {
            spinnerMotor.position = value
        }

    val spinnerAtBottom get() = !bottomLimitSwitch.get()

    val currentColor get() = getColor(colorSensor.color)
    val currentColorString get() = sensorColorToString[getColor(colorSensor.color)]

    private val kBlueTarget: Color = ColorMatch.makeColor(0.185, 0.456, 0.360)
    private val kGreenTarget: Color = ColorMatch.makeColor(0.220, 0.525, 0.255)
    private val kRedTarget: Color = ColorMatch.makeColor(0.359, 0.430, 0.211)
    private val kYellowTarget: Color = ColorMatch.makeColor(0.295, 0.532, 0.173)

    private fun configColorSensor() {
        m_colorMatcher.addColorMatch(kBlueTarget)
        m_colorMatcher.addColorMatch(kGreenTarget)
        m_colorMatcher.addColorMatch(kRedTarget)
        m_colorMatcher.addColorMatch(kYellowTarget)
    }

    private fun getColor(sensorOutput: Color): Color {
        val match = m_colorMatcher.matchClosestColor(sensorOutput)

        return when (match.color) {
            kBlueTarget -> Color.kBlue
            kRedTarget -> Color.kRed
            kGreenTarget -> Color.kGreen
            kBlueTarget -> Color.kBlue
            else -> Color.kWhite
        }
    }

    init {
        configColorSensor()
        spinnerPosition = 0
//        RobotScope.linkTableEntry("Target Color", name) { frcColorToString[frcColor] }
//        RobotScope.linkTableEntry("Sensed Color", name) { sensorColorToString[currentColor] }
//        RobotScope.linkTableEntry("Spinner Position", name) { spinnerPosition }
//        RobotScope.linkTableEntry("Spinner at bottom", name) { spinnerAtBottom }
//        RobotScope.linkTableEntry("Color wheel slow mode engaged", name) { Drivetrain.slowAtColorWheel }

        // Reset spinner encoder to 0 when it hits the bottom limit switch
        ({ spinnerAtBottom }).watch {
            RobotScope.whenTrue {
                spinnerPosition = 0
            }
        }
        // Slow driving after the spinner has raised above a certain point
    }

    fun spin(value: Double) {
        spinnerMotor.setPercentOutput(value)
    }

    fun spinReverse(speed: Double) {
        spinnerMotor.setPercentOutput(speed)
    }

    fun stop() {
        spinnerMotor.disable()
    }
}
