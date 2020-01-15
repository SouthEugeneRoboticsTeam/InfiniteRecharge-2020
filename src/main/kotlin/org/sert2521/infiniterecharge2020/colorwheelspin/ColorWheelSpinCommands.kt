package org.sert2521.infiniterecharge2020.colorwheelspin

import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import edu.wpi.first.wpilibj.util.Color
import kotlinx.coroutines.cancel
import org.sert2521.sertain.coroutines.doOne
import java.lang.System.currentTimeMillis

suspend fun extend(isExtendingOut: Boolean, time: Long) = doTask {
    val colorWheelSpin = use<ColorWheelSpin>()
    val startTime = currentTimeMillis()
    action {
        onTick {
            if (isExtendingOut) {
                colorWheelSpin.extendOut(0.3)
            } else {
                colorWheelSpin.extendIn(-0.3)
            }
            if(currentTimeMillis() - startTime < time){
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
            val sensorColor = colorWheelSpin.sensor.color
            if ((sensorColor.red + 25 > colorToSpinTo.red && colorToSpinTo.red > sensorColor.red - 25) &&
                    (sensorColor.blue + 25 > colorToSpinTo.blue && colorToSpinTo.blue > sensorColor.blue - 25) &&
                    (sensorColor.green + 25 > colorToSpinTo.green && colorToSpinTo.green > sensorColor.green - 25)) {
                this@action.cancel()
            }
            colorWheelSpin.spin(0.3)
        }
    }
}

suspend fun spinForColors() = doTask {
    val colorWheelSpin = use<ColorWheelSpin>()
    action {
        var triangleSpins = 0
        var pastColor: Color = Color.kWhite
        onTick {
            val sensorColor = colorWheelSpin.sensor.color
            if ((sensorColor.red + 25 > pastColor.red && pastColor.red > sensorColor.red - 25) &&
                    (sensorColor.blue + 25 > pastColor.blue && pastColor.blue > sensorColor.blue - 25) &&
                    (sensorColor.green + 25 > pastColor.green && pastColor.green > sensorColor.green - 25)) {
                pastColor = sensorColor
                triangleSpins++
            }
            if (triangleSpins >= 32) {
               this@action.cancel()
            }
            pastColor = sensorColor
            colorWheelSpin.spin(0.3)
        }
    }
}

suspend fun spinAndExtend(isSpinningToColor: Boolean) = doTask {
    action {
        doOne {
            action {
                extend(true, 9223372036854775807)//basically infinite time (biggest possible value)
            }
            action {
                if(isSpinningToColor){
                    spinToColor()
                }else{
                    spinForColors()
                }
            }
        }
        extend(false, 3000)
    }
}