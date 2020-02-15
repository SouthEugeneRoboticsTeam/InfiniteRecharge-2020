package org.sert2521.infiniterecharge2020

import org.sert2521.sertain.motors.TalonId

object MotorControllers {
    val leftFront = TalonId(1)
    val leftBack = TalonId(2)
    val rightFront = TalonId(3)
    val rightBack = TalonId(4)

    val climber = TalonId(5)
    val winchFront = TalonId(9)
    val winchRear = TalonId(10)
}

object Operator {
    val PRIMARY_CONTROLLER = 0
    val PRIMARY_STICK = 1
    val SECONDARY_STICK = 2
}

object Sensors {
    val BOTTOM_LIMIT_SWITCH = 1
    val TOP_LIMIT_SWITCH = 0
}
