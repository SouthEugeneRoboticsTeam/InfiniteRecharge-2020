package org.sert2521.infiniterecharge2020

import org.sert2521.sertain.motors.TalonId

object MotorControllers {
    val extender = TalonId(1)
    val spinner = TalonId(2)
}

object Operator {
    val PRIMARY_CONTROLLER = 0

    val SPIN_FOR_COLORS = 14
    val SPIN_TO_COLOR = 13
}