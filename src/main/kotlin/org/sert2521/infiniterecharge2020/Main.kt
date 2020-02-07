package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.Joystick
import org.sert2521.infiniterecharge2020.climber.climberDown
import org.sert2521.infiniterecharge2020.climber.climberUp
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.robot

val primaryJoystick by lazy { Joystick(0) }

suspend fun main() = robot {
    { primaryJoystick.getRawButton(7) }.watch {
        whileTrue {
            climberUp()
        }
    };
    { primaryJoystick.getRawButton(8) }.watch {
        whileTrue {
            climberDown()
        }
    }
}
