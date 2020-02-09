package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import kotlinx.coroutines.CoroutineScope
import org.sert2521.infiniterecharge2020.OI.primaryController
import org.sert2521.infiniterecharge2020.OI.primaryJoystick
import org.sert2521.infiniterecharge2020.OI.secondaryJoystick
import org.sert2521.infiniterecharge2020.autonomous.PathGenerator
import org.sert2521.infiniterecharge2020.autonomous.centerInitPowerPort
import org.sert2521.infiniterecharge2020.autonomous.rightInitPowerPort
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
    ({ primaryController.xButton }).watch {
        whenTrue {
            centerInitPowerPort(true, PathGenerator.endLocation.TRENCH)
        }
    }
    ({ primaryController.yButton }).watch {
        whenTrue {
            rightInitPowerPort(true, PathGenerator.endLocation.TRENCH)
        }
    }
    ({ primaryController.aButton }).watch {
        whenTrue {
            centerInitPowerPort(true, PathGenerator.endLocation.LOADING_STATION)
        }
    }
    ({ primaryController.bButton }).watch {
        whenTrue {
            rightInitPowerPort(true, PathGenerator.endLocation.LOADING_STATION)
        }
    }
}
