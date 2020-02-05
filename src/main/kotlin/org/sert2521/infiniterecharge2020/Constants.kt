package org.sert2521.infiniterecharge2020

import org.sert2521.sertain.motors.TalonId
import org.sert2521.sertain.motors.VictorId

object MotorControllers {
    val leftFront = TalonId(1)
    val leftBack = TalonId(3)
    val rightFront = TalonId(2)
    val rightBack = TalonId(4)

    val ROLLER_LEFT = TalonId(-1)
    val ROLLER_RIGHT = TalonId(-1)

    val FLAPPER = VictorId(-1)
}

object Operator {
    val PRIMARY_CONTROLLER = 0
    val PRIMARY_STICK = 1
    val SECONDARY_STICK = 2

}
