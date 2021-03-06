package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.autonomous.handleAutoChooser
import org.sert2521.infiniterecharge2020.autonomous.objective1
import org.sert2521.infiniterecharge2020.autonomous.objective2
import org.sert2521.infiniterecharge2020.autonomous.startingPose
import org.sert2521.infiniterecharge2020.climber.Climber
import org.sert2521.infiniterecharge2020.colorwheelspinner.ColorWheelSpinner
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.practiceBotChooser
import org.sert2521.infiniterecharge2020.powerhouse.PowerHouse
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.events.whileAuto
import org.sert2521.sertain.events.whileTeleop
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.access
import org.sert2521.sertain.subsystems.register

val drivetrain = register<Drivetrain>()
val climber = register<Climber>()
val powerHouse = register<PowerHouse>()
val colorWheelSpinner = register<ColorWheelSpinner>()

suspend fun main() = robot {
    println("Robot program starting")

    startingPose
    objective1
    objective2

    handleAutoChooser()

//    onTeleop {
//        openHouse()
//        closeHouse()
//    }

    whileTeleop {
        initControls()
    }

    whileAuto {
        println("Resetting gyro and encoders")
        val dt = access<Drivetrain>()
        dt.gyro.reset()
        dt.zeroEncoders()

        // BORKED
//        auto(PathGenerator.startlocation.CENTER, listOf(PathGenerator.tasks.DRIVE_FORWARD))
//         3 ball auto with 3 ball trench pickup
//        auto(PathGenerator.startlocation.CENTER, listOf(
//                PathGenerator.tasks.UNLOAD_FROM_POWERPORT))
        // 5 ball auto
//        auto(PathGenerator.startlocation.RIGHT_TRENCH, listOf(PathGenerator.tasks.BALLS2,
//                PathGenerator.tasks.TRENCH_TO_CORNER,
//                PathGenerator.tasks.UNLOAD_FROM_CORNER))        auto(PathGenerator.startlocation.RIGHT_TRENCH, listOf(PathGenerator.tasks.BALLS2,
//                PathGenerator.tasks.TRENCH_TO_CORNER,
//                PathGenerator.tasks.UNLOAD_FROM_CORNER))
        // 3 ball auto from right side.
//        auto(PathGenerator.startlocation.RIGHT_ALLIANCE, listOf(PathGenerator.tasks.UNLOAD_FROM_POWERPORT))

//         Additional end options: PathGenerator.tasks.AWAY_FROM_POWERPORT
//         Additional start option(untested): PathGenerator.tasks.PUSHBACK
//         DO NOT USE BALLS3
//        auto.second()
    }

    launch {
        delay(1000)
        SmartDashboard.putData("Control Mode", OI.controlModeChooser)
        SmartDashboard.putData("Robot Type", practiceBotChooser)
//        val camera = CameraServer.getInstance().startAutomaticCapture()
//        camera.setResolution(640, 360)
        periodic(20) {
            SmartDashboard.updateValues()
        }
    }

//    GlobalScope.launch(RobotDispatcher) {
//        periodic(20, 0) {
//            var rootGroup = Thread.currentThread().threadGroup
//            var parentGroup: ThreadGroup
//            while (rootGroup.parent.also { parentGroup = it } != null) {
//                rootGroup = parentGroup
//            }
//
//            var threads = arrayOfNulls<Thread>(rootGroup.activeCount())
//            while (rootGroup.enumerate(threads, true) == threads.size) {
//                threads = arrayOfNulls(threads.size * 2)
//            }
//
//            println("NumThreads: ${threads.size}")
//        }
//    }
}
