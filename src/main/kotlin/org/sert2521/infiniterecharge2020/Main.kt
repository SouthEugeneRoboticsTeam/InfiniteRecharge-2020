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
import org.sert2521.sertain.events.onTeleop
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.events.whileAuto
import org.sert2521.sertain.events.whileTeleop
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.access
import org.sert2521.sertain.subsystems.add

suspend fun main() = robot {
    println("Robot program starting")
    add<Drivetrain>()
    add<Climber>()
    add<PowerHouse>()

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
        // Takes around 18 seconds currently
//        auto(PathGenerator.startlocation.CENTER, listOf(
//                PathGenerator.tasks.UNLOAD_FROM_POWERPORT,
//                PathGenerator.tasks.CORNER_TO_TRENCH,
//                PathGenerator.tasks.BALLS3))

        // Needs more testing. Vision can be unreliable sometimes. Might be fixed with actual PID tuning
//        auto(PathGenerator.startlocation.RIGHT_TRENCH, listOf(PathGenerator.tasks.BALLS2,
//                PathGenerator.tasks.TRENCH_TO_CORNER,
//                PathGenerator.tasks.UNLOAD_FROM_CORNER))
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
