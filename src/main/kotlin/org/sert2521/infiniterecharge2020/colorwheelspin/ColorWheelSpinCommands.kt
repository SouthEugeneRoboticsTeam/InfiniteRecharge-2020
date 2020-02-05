package org.sert2521.infiniterecharge2020.colorwheelspin

import edu.wpi.first.wpilibj.util.Color
import jdk.nashorn.internal.objects.NativeJava.extend
import kotlinx.coroutines.cancel
import org.sert2521.sertain.coroutines.doAll
import org.sert2521.sertain.coroutines.doOne
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import java.lang.System.currentTimeMillis
import kotlin.math.abs

suspend fun retract (time: Long) = doTask {
    val colorWheelSpin = use<ColorWheelSpin>()
    val startTime = currentTimeMillis()
    action {
        onTick {
            colorWheelSpin.extend(-0.2)
            if (currentTimeMillis() - startTime < time) {
                this@action.cancel()
            }
        }
    }
}

suspend fun spinToColor() = doTask {
    val colorWheelSpin = use<ColorWheelSpin>()
    action {
        val colorToSpinTo: Color = Color.kBlue
        onTick {
            val sensorColor = getColor(colorWheelSpin.sensor.color)
            if (sensorColor != colorToSpinTo) {
                this@action.cancel()
            }
            colorWheelSpin.spin(0.5)
        }
    }
}

suspend fun spinForColors() = doTask {
    val colorWheelSpin = use<ColorWheelSpin>()
    action {
        var triangleSpins = 0
        var pastColor: Color = Color.kWhite
        onTick {
            val sensorColor = getColor(colorWheelSpin.sensor.color)
            if (sensorColor != pastColor) {
                pastColor = sensorColor
                triangleSpins++
            }
            if (triangleSpins >= 32) {
                this@action.cancel()
            }
            pastColor = sensorColor
            colorWheelSpin.spin(0.5)
        }
    }
}

suspend fun spinAndExtend(isSpinningToColor: Boolean) = doTask {
    action {
        doAll {
            action {
                if (isSpinningToColor) {
                    spinToColor()
                } else {
                    spinForColors()
                }
            }
        }
        doAll {
            action {
                retract(3000)
            }
        }
    }
}

fun getColor(sensorOutput: Color): Color {
    val distanceToRed = abs(sensorOutput.blue - 0.13) + abs(sensorOutput.green - 0.34) + abs(sensorOutput.red - 0.52)
    val distanceToBlue = abs(sensorOutput.blue - 0.45) + abs(sensorOutput.green - 0.42) + abs(sensorOutput.red - 0.11)
    val distanceToGreen = abs(sensorOutput.blue - 0.25) + abs(sensorOutput.green - 0.58) + abs(sensorOutput.red - 0.16)
    val distanceToYellow = abs(sensorOutput.blue - 0.11) + abs(sensorOutput.green - 0.56) + abs(sensorOutput.red - 0.31)

    return if (distanceToRed < distanceToBlue && distanceToRed < distanceToGreen && distanceToRed < distanceToYellow) {
        Color.kRed
    } else if (distanceToBlue < distanceToGreen && distanceToBlue < distanceToYellow) {
        Color.kBlue
    } else if (distanceToGreen < distanceToYellow) {
        Color.kGreen
    } else {
        Color.kYellow
    }
}
