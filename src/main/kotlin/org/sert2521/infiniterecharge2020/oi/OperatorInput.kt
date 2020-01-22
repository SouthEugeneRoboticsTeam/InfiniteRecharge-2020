package org.sert2521.infiniterecharge2020.oi

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import org.sert2521.infiniterecharge2020.Operator


enum class ControlMode {
    CONTROLLER, JOYSTICK
}

val controlModeChooser = SendableChooser<ControlMode>().apply {
    addOption("Joystick", ControlMode.JOYSTICK)
    addOption("Controller", ControlMode.CONTROLLER)
}

val controlMode get() = controlModeChooser.selected ?: ControlMode.JOYSTICK

val primaryJoystick by lazy { Joystick(Operator.PRIMARY_STICK) }
val secondaryJoystick by lazy { Joystick(Operator.SECONDARY_STICK) }
val primaryController by lazy { XboxController(Operator.PRIMARY_CONTROLLER) }

