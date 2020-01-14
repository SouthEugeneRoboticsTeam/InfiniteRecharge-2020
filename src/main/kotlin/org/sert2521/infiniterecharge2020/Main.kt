package org.sert2521.infiniterecharge2020

import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.util.ColorShim
import edu.wpi.first.wpilibj.I2C
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.events.onTeleop
import org.sert2521.sertain.robot

suspend fun main() = robot {
    onTeleop {
        val sensor = ColorSensorV3(I2C.Port.kOnboard)
        periodic(20) {
            val data = sensor.rawColor
            var red = 0
            var green = 0
            var blue = 0
            {
                val mag = data.red + data.green + data.blue
                red = data.red / mag
                green = data.green / mag
                blue = data.blue / mag

            }
            print("red: ")
            print(red)
            println("%")
            print("green: ")
            print(green)
            println("%")
            print("blue: ")
            print(blue)
            println("%")
        }
    }
}