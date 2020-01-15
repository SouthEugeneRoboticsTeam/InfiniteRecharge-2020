package org.sert2521.infiniterecharge2020

import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.I2C
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.events.onTeleop
import org.sert2521.sertain.robot
import edu.wpi.first.wpilibj.util.Color

suspend fun main() = robot {
    onTeleop {
        val sensor = ColorSensorV3(I2C.Port.kOnboard)
        periodic(20) {
            val color: Color = sensor.color
            print("red: ")
            println(color.red)
            print("green: ")
            println(color.green)
            print("blue: ")
            println(color.blue)
        }
    }
}