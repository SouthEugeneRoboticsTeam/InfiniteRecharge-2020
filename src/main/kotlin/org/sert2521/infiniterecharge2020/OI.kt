package org.sert2521.infiniterecharge2020

import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import kotlinx.coroutines.CoroutineScope
import org.sert2521.infiniterecharge2020.OI.setNextDriverCamera
import org.sert2521.infiniterecharge2020.OI.primaryController
import org.sert2521.infiniterecharge2020.OI.setClimberCamera
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.telemetry.linkTableEntry

object OI {
    enum class ControlMode {
        CONTROLLER, JOYSTICK
    }

    enum class DriverCameraSource(val key: String) {
        FRONT("Front"), Ball("Ball")
    }


    // TODO: Figure out why this isn't working
    val controlModeChooser = SendableChooser<ControlMode>().apply {
        addOption("Joystick", ControlMode.JOYSTICK)
        addOption("Controller", ControlMode.CONTROLLER)
    }

    val controlMode get() = controlModeChooser.selected ?: ControlMode.CONTROLLER

    val currentCamera = NetworkTableInstance.getDefault().getEntry("/current_camera")
    var currentCameraIndex = 0

    init {
        RobotScope.linkTableEntry("Control Mode", "OI") { controlMode.name }
        currentCamera.setString(DriverCameraSource.FRONT.key)
    }

    val primaryController by lazy { XboxController(Operator.PRIMARY_CONTROLLER) }
    val primaryJoystick by lazy { Joystick(Operator.PRIMARY_STICK) }
    val secondaryJoystick by lazy { Joystick(Operator.SECONDARY_STICK) }

    fun setNextDriverCamera() {
        val camera = DriverCameraSource.values()[currentCameraIndex]
        currentCamera.setString(camera.key)
        if (currentCameraIndex == 1) currentCameraIndex = 0 else currentCameraIndex++
    }

    fun setClimberCamera() {
        NetworkTableInstance.getDefault().getEntry("/current_camera").setString("Climber")
    }
}

fun CoroutineScope.initControls() {
    ({ primaryController.aButton }).watch {
        whenTrue {
            setNextDriverCamera()
        }
    }

    ({ primaryController.bButton }).watch {
        whenTrue {
            setClimberCamera()
        }
    }
}
