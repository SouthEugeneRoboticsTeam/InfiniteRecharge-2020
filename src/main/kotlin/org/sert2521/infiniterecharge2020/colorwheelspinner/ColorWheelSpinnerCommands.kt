package org.sert2521.infiniterecharge2020.colorwheelspinner

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.util.Color
import kotlin.math.abs
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import org.sert2521.sertain.utils.timer

suspend fun retract(time: Long) = doTask {
    val colorWheelSpinner = use<ColorWheelSpinner>()
    action {
        timer(20, 0, time) {
            colorWheelSpinner.output(-0.5)
        }
    }
}

suspend fun spinToColor() = doTask {
    val colorWheelSpinner = use<ColorWheelSpinner>()
    val frcColor = DriverStation.getInstance().gameSpecificMessage.first()

    val targetColor = colorWheelSpinner.frcColorToTargetColor[frcColor] ?: return@doTask

    action {
        onTick {
            val color = getColor(colorWheelSpinner.sensor.color)
            if (color == targetColor) {
                this@action.cancel()
            }
            colorWheelSpinner.output(0.5)
        }
    }
}

suspend fun spinForColors() = doTask {
    val colorWheelSpinner = use<ColorWheelSpinner>()
    action {
        var triangleSpins = 0
        var pastColor: Color = Color.kWhite
        onTick {
            val sensorColor = getColor(colorWheelSpinner.sensor.color)
            if (sensorColor != pastColor) {
                pastColor = sensorColor
                triangleSpins++
            }
            if (triangleSpins >= 32) {
                this@action.cancel()
            }
            pastColor = sensorColor
            colorWheelSpinner.output(0.5)
        }
    }
}

suspend fun spinAndExtend(isSpinningToColor: Boolean) {
    coroutineScope {
        if (isSpinningToColor) {
            spinToColor()
        } else {
            spinForColors()
        }
        retract(3000)
    }
}

fun getColor(sensorOutput: Color): Color {
    val distanceToRed = abs(sensorOutput.blue - 0.13) + abs(sensorOutput.green - 0.34) + abs(sensorOutput.red - 0.52)
    val distanceToBlue = abs(sensorOutput.blue - 0.45) + abs(sensorOutput.green - 0.42) + abs(sensorOutput.red - 0.11)
    val distanceToGreen = abs(sensorOutput.blue - 0.25) + abs(sensorOutput.green - 0.58) + abs(sensorOutput.red - 0.16)
    val distanceToYellow = abs(sensorOutput.blue - 0.11) + abs(sensorOutput.green - 0.56) + abs(sensorOutput.red - 0.31)

    return when {
        distanceToRed < distanceToBlue && distanceToRed < distanceToGreen && distanceToRed < distanceToYellow ->
            Color.kRed
        distanceToBlue < distanceToGreen && distanceToBlue < distanceToYellow ->
            Color.kBlue
        distanceToGreen < distanceToYellow ->
            Color.kGreen
        else ->
            Color.kYellow
    }
}
