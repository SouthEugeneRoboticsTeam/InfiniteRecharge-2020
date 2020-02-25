package org.sert2521.infiniterecharge2020

import org.sert2521.sertain.motors.TalonId
import org.sert2521.sertain.motors.VictorId

object MotorControllers {
    val leftFront = TalonId(1)
    val leftBack = TalonId(2)
    val rightFront = TalonId(3)
    val rightBack = TalonId(4)

    val climber = TalonId(5)
    val winchFront = TalonId(9)
    val winchRear = TalonId(10)

    val rollerLeft = TalonId(11)
    val rollerRight = TalonId(12)

    val flap = VictorId(8)
}

object Operator {
    const val PRIMARY_CONTROLLER = 0
    const val PRIMARY_STICK = 1
    const val SECONDARY_STICK = 2
}

object Sensors {
    const val CLOSE_SWITCH = 3
    const val OPEN_SWITCH = 2

    val TOP_LIMIT_SWITCH = 0
    val BOTTOM_LIMIT_SWITCH = 1
    val RUNG_LIMIT_SWTICH = 4
}
