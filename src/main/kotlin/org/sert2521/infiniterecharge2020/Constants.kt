package org.sert2521.infiniterecharge2020

import org.sert2521.sertain.motors.TalonId
import org.sert2521.sertain.motors.VictorId

object MotorControllers {
    val leftFront = TalonId(1)
    val leftBack = TalonId(2)
    val rightFront = TalonId(3)
    val rightBack = TalonId(4)

    val ROLLER_LEFT = TalonId(11)
    val ROLLER_RIGHT = TalonId(12)

    val FLAPPER = VictorId(8)
}

object Operator {
    const val PRIMARY_CONTROLLER = 0
    const val PRIMARY_STICK = 1
    const val SECONDARY_STICK = 2
}

object Sensors {
    const val CLOSE_SWITCH = 3
    const val OPEN_SWITCH = 2
}
