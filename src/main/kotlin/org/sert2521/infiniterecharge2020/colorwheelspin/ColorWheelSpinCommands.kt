package org.sert2521.infiniterecharge2020.colorwheelspin

import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun extend(isExtending: Boolean) = doTask{
    val colorWheelSpin = use<ColorWheelSpin>()
    action{
        onTick{
            if(isExtending) {
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
        //var sensorColor: Color = put black here
        onTick{
            if(!extended) {
                extend(true)
            }else{
                if(!spun){
                    //sensorColor = sensor.getColor()
                    //if((sensorColor.red + 25 > color.red > sensorColor.red - 25) &&
                    //  (sensorColor.blue + 25 > color.blue > sensorColor.blue - 25) &&
                    //  (sensorColor.green + 25 > color.green > sensorColor.green - 25) &&){
                    //STOP
                    //}
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
        //var sensorColor: Color = put black here
        //var pastColor: Color = put white here
        onTick{
            if(!extended) {
                extend(true)
            }else{
                if(!spun){
                    //sensorColor = sensor.getColor()
                    //if((sensorColor.red + 25 > color.red > sensorColor.red - 25) &&
                    //  (sensorColor.blue + 25 > color.blue > sensorColor.blue - 25) &&
                    //  (sensorColor.green + 25 > color.green > sensorColor.green - 25) &&){
                        triangleSpins++
                    //}
                    //pastColor = sensorColor
                    colorWheelSpin.spin(0.3)
                }else{
                    extend(false)
                }
            }
        }
    }
}