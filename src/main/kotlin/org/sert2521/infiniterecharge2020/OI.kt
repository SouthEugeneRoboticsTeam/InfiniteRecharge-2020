package org.sert2521.infiniterecharge2020

import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.OI.primaryController
import org.sert2521.infiniterecharge2020.OI.secondaryJoystick
import org.sert2521.infiniterecharge2020.OI.setClimberCamera
import org.sert2521.infiniterecharge2020.OI.setNextDriverCamera
import org.sert2521.infiniterecharge2020.climber.climberDown
import org.sert2521.infiniterecharge2020.climber.climberUp
import org.sert2521.infiniterecharge2020.climber.runWinch
import org.sert2521.infiniterecharge2020.colorwheelspinner.extend
import org.sert2521.infiniterecharge2020.colorwheelspinner.retract
import org.sert2521.infiniterecharge2020.colorwheelspinner.spinForColors
import org.sert2521.infiniterecharge2020.colorwheelspinner.spinToColor
import org.sert2521.infiniterecharge2020.drivetrain.alignToBall
import org.sert2521.infiniterecharge2020.powerhouse.PowerHouse
import org.sert2521.infiniterecharge2020.powerhouse.banish
import org.sert2521.infiniterecharge2020.powerhouse.closeHouse
import org.sert2521.infiniterecharge2020.powerhouse.openHouse
import org.sert2521.infiniterecharge2020.powerhouse.reverseWelcome
import org.sert2521.infiniterecharge2020.powerhouse.welcome
import org.sert2521.infiniterecharge2020.utils.deadband
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.coroutines.doAll
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import org.sert2521.sertain.telemetry.linkTableEntry

object OI {
    enum class ControlMode {
        CONTROLLER, JOYSTICK
    }

    enum class DriverCameraSource(val key: String) {
        FRONT("Front"), Ball("Ball")
    }

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

    // CLIMBER
    ({ secondaryJoystick.getRawButton(5) }).watch {
        whileTrue {
            println("GOING UP")
            climberUp()
        }
    }
    ({ secondaryJoystick.getRawButton(10) }).watch {
        whileTrue {
            println("GOING DOWN")
            climberDown()
        }
    }
    ({ secondaryJoystick.getRawButton(9) && secondaryJoystick.trigger }).watch {
        whileTrue {
            println("Winching: ${secondaryJoystick.y.deadband(0.02)}")
            runWinch { secondaryJoystick.y.deadband(0.02) }
        }
    }

    // INTAKE
    ({ secondaryJoystick.getRawButton(3) }).watch {
        whileTrue {
            println("Should be intaking")
            welcome()
        }
    }
    ({ secondaryJoystick.getRawButton(4) }).watch {
        whileTrue {
            println("Should be outtaking")
            banish()
        }
        // Reverse the brushes briefly after outtaking
        whenFalse {
            doTask {
                use<PowerHouse>()
                action {
                    doAll {
                        action {
                            closeHouse()
                        }
                        action {
                            launch { reverseWelcome() }
                            delay(250)
                            cancel()
                        }
                    }
                }
            }
        }
    }
    ({ secondaryJoystick.getRawButton(2) }).watch {
        whileTrue {
            println("Reversing the intake")
            reverseWelcome()
        }
    }
    ({ secondaryJoystick.getRawButton(7) }).watch {
        whenTrue {
            openHouse()
        }
    }
    ({ secondaryJoystick.getRawButton(8) }).watch {
        whenTrue {
            closeHouse()
        }
    }

    // COLOR SPINNER
    ({ secondaryJoystick.getRawButton(12) }).watch {
        whenTrue {
            spinForColors()
        }
    }
    ({ secondaryJoystick.getRawButton(15) }).watch {
        whenTrue {
            spinToColor()
        }
    }
    ({ secondaryJoystick.getRawButton(11) }).watch {
        whileTrue {
            extend()
        }
    }
    ({ secondaryJoystick.getRawButton(16) }).watch {
        whileTrue {
            retract()
        }
    }

    // AUTO-ALIGN
    ({ primaryController.yButton }).watch {
        whileTrue {
            println("Should be aligning")
            doAll {
                action {
                    welcome()
                }
                action {
                    alignToBall(3.5)
                }
            }
        }
    }

    // DRIVER CAMERAS
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
