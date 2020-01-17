package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.Joystick
import org.sert2521.infiniterecharge2020.colorwheelspin.spinAndExtend
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.robot

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