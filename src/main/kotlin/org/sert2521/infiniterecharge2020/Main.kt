package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.climber.Climber
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.practiceBotChooser
import org.sert2521.infiniterecharge2020.powerhouse.PowerHouse
import org.sert2521.infiniterecharge2020.powerhouse.closeHouse
import org.sert2521.infiniterecharge2020.powerhouse.openHouse
import org.sert2521.infiniterecharge2020.vision.Vision
import org.sert2521.sertain.events.onTeleop
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.events.whileAuto
import org.sert2521.sertain.events.whileTeleop
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.access
import org.sert2521.sertain.subsystems.register

val drivetrain = register<Drivetrain>()
val climber = register<Climber>()
val powerHouse = register<PowerHouse>()
val vision = register<Vision>()

suspend fun main() = robot {
    println("Robot program starting")

    onTeleop {
        openHouse()
        closeHouse()
    }

    whileTeleop {
        initControls()
    }

    whileAuto {
        println("Resetting gyro and encoders")
        val dt = access<Drivetrain>()
        dt.gyro.reset()
        dt.zeroEncoders()
//        Takes around 18 seconds currently
//        auto(PathGenerator.startlocation.CENTER, listOf(
//                PathGenerator.tasks.UNLOAD_FROM_POWERPORT,
//                PathGenerator.tasks.CORNER_TO_TRENCH,
//                PathGenerator.tasks.BALLS3))

//         Needs more testing. Vision can be unreliable sometimes. Might be fixed with actual PID tuning
//        auto(
//                PathGenerator.StartLocation.RIGHT_TRENCH,
//                PathGenerator.Task.BALLS2,
//                PathGenerator.Task.TRENCH_TO_CORNER,
//                PathGenerator.Task.UNLOAD_FROM_CORNER
//        )
    }

    launch {
        delay(1000)
        SmartDashboard.putData("Control Mode", OI.controlModeChooser)
        SmartDashboard.putData("Robot Type", practiceBotChooser)
        onTick {
            SmartDashboard.updateValues()
        }
    }
}
