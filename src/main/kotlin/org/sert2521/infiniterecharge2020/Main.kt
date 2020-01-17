package org.sert2521.infiniterecharge2020

import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.util.Color
import org.sert2521.infiniterecharge2020.colorwheelspin.spinAndExtend
import org.sert2521.infiniterecharge2020.colorwheelspin.spinForColors
import org.sert2521.infiniterecharge2020.colorwheelspin.spinToColor
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.events.True
import org.sert2521.sertain.events.onTeleop
import org.sert2521.sertain.robot
import kotlin.math.abs

val primaryJoystick by lazy { Joystick(Operator.PRIMARY_CONTROLLER) }

suspend fun main() = robot {
    { primaryJoystick.getRawButton(Operator.SPIN_FOR_COLORS) }.watch {
        whenFalse {
            spinAndExtend(false)
        }
    };
    { primaryJoystick.getRawButton(Operator.SPIN_TO_COLOR) }.watch {
        whenFalse {
            spinAndExtend(true)
        }
    }
}