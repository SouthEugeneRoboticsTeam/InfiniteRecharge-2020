package org.sert2521.infiniterecharge2020

import org.sert2521.sertain.motors.TalonId

object MotorControllers {
    val leftFront = TalonId(1)
    val leftBack = TalonId(3)
    val rightFront = TalonId(2)
    val rightBack = TalonId(4)

    val extender = TalonId(1)
    val spinner = TalonId(2)
}

object Operator {
    val PRIMARY_CONTROLLER = 0
    val PRIMARY_STICK = 1
    val SECONDARY_STICK = 2

    const val SPIN_FOR_COLORS = 14
    const val SPIN_TO_COLOR = 13
}
