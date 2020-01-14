package org.sert2521.infiniterecharge2020.colorwheelspin

import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import edu.wpi.first.wpilibj.util.Color

suspend fun extend(isExtendingOut: Boolean) = doTask{
    val colorWheelSpin = use<ColorWheelSpin>()
    action{
        onTick{
            if(isExtendingOut) {
                colorWheelSpin.extendOut(0.3)
            }else{
                colorWheelSpin.extendIn(-0.3)
            }
        }
    }
}

suspend fun spinToColor() = doTask {
    val colorWheelSpin = use<ColorWheelSpin>()
    action{
        var extended: Boolean = false
        var spun: Boolean = false
        var sensorColor: Color = Color.kBlack
        var colorToSpinTo: Color = Color.kBlue
        onTick{
            if(!extended) {
                extend(true)
            }else{
                if(!spun){
                    sensorColor = colorWheelSpin.sensor.color
                    if((sensorColor.red + 25 > colorToSpinTo.red && colorToSpinTo.red > sensorColor.red - 25) &&
                            (sensorColor.blue + 25 > colorToSpinTo.blue && colorToSpinTo.blue > sensorColor.blue - 25) &&
                            (sensorColor.green + 25 > colorToSpinTo.green && colorToSpinTo.green > sensorColor.green - 25)){
                        spun = true
                    }
                    colorWheelSpin.spin(0.3)
                }else{
                    extend(false)
                }
            }
        }
    }
}

suspend fun spinForColors() = doTask {
    val colorWheelSpin = use<ColorWheelSpin>()
    action{
        var extended: Boolean = false
        var spun: Boolean = false
        var triangleSpins: Int = 0
        var sensorColor: Color = colorWheelSpin.sensor.color
        var pastColor: Color = Color.kWhite
        onTick{
            if(!extended) {
                extend(true)
            }else{
                if(!spun){
                    sensorColor = colorWheelSpin.sensor.color
                    if((sensorColor.red + 25 > pastColor.red && pastColor.red > sensorColor.red - 25) &&
                            (sensorColor.blue + 25 > pastColor.blue && pastColor.blue > sensorColor.blue - 25) &&
                            (sensorColor.green + 25 > pastColor.green && pastColor.green > sensorColor.green - 25)){
                        pastColor = sensorColor
                        triangleSpins++
                    }
                    if(triangleSpins >= 32){
                        spun = true
                    }
                    pastColor = sensorColor
                    colorWheelSpin.spin(0.3)
                }else{
                    extend(false)
                }
            }
        }
    }
}