package org.sert2521.infiniterecharge2020

import org.sert2521.sertain.motors.TalonId

object MotorControllers {
    val leftFront = TalonId(1)
    val rightFront = TalonId(4)
    val leftRear = TalonId(2)
    val rightRear = TalonId(4)

    val CLIMBER_LIFT = TalonId(-1)
    val CLIMBER_WINCH_A = TalonId(-1)
    val CLIMBER_WINCH_B = TalonId(-1)

    val INTAKE_A = TalonId(-1)
    val INTAKE_B = TalonId(-1)

    val FLAPPER = TalonId(-1)
}
