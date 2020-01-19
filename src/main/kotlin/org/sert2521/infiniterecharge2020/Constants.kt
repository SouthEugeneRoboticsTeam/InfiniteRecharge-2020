package org.sert2521.infiniterecharge2020

import org.sert2521.sertain.motors.TalonId

object MotorControllers {
    val extender = TalonId(1)
    val spinner = TalonId(2)
}

object Operator {
    const val PRIMARY_CONTROLLER = 0

    const val SPIN_FOR_COLORS = 14
    const val SPIN_TO_COLOR = 13
}