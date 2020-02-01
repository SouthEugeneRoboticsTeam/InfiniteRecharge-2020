package org.sert2521.infiniterecharge2020

import org.sert2521.sertain.motors.TalonId

object MotorControllers {
    val leftFront = TalonId(1)
    val leftBack = TalonId(3)
    val rightFront = TalonId(2)
    val rightBack = TalonId(4)

    val INTAKE_A = TalonId(-1)
    val INTAKE_B = TalonId(-1)

    val FLAPPER = TalonId(-1)
}

object Operator {
    val PRIMARY_CONTROLLER = 0
    val PRIMARY_STICK = 1
    val SECONDARY_STICK = 2

}
