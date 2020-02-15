package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import kotlinx.coroutines.CoroutineScope
import org.sert2521.infiniterecharge2020.OI.primaryController
import org.sert2521.infiniterecharge2020.powerhouse.banish
import org.sert2521.infiniterecharge2020.powerhouse.closeHouse
import org.sert2521.infiniterecharge2020.powerhouse.reverseWelcome
import org.sert2521.infiniterecharge2020.powerhouse.welcome
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.telemetry.linkTableEntry

object OI {
    enum class ControlMode {
        CONTROLLER, JOYSTICK
    }

    // TODO: Figure out why this isn't working
    val controlModeChooser = SendableChooser<ControlMode>().apply {
        addOption("Joystick", ControlMode.JOYSTICK)
        addOption("Controller", ControlMode.CONTROLLER)
    }

    val controlMode get() = controlModeChooser.selected ?: ControlMode.CONTROLLER

    init {
        RobotScope.linkTableEntry("Control Mode", "OI") { controlMode.name }
    }

    val primaryController by lazy { XboxController(Operator.PRIMARY_CONTROLLER) }
    val primaryJoystick by lazy { Joystick(Operator.PRIMARY_STICK) }
    val secondaryJoystick by lazy { Joystick(Operator.SECONDARY_STICK) }
}

fun CoroutineScope.initControls() {
    // INTAKE
    ({ primaryController.getBumper(GenericHID.Hand.kRight) }).watch {
        whileTrue {
            println("Should be intaking")
            welcome()
        }
    }
    ({ primaryController.getBumper(GenericHID.Hand.kLeft) }).watch {
        whileTrue {
            println("Should be outtaking")
            banish()
        }
        whenFalse {
            closeHouse()
        }
    }
    ({ primaryController.getTriggerAxis(GenericHID.Hand.kLeft) > .5 }).watch {
        whileTrue {
            println("Reversing the intake")
            reverseWelcome()
        }
    }
}
