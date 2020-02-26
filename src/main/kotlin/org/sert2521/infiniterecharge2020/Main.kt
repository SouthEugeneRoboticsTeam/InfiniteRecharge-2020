package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.autonomous.PathGenerator
import org.sert2521.infiniterecharge2020.autonomous.centerInitPowerPort
import org.sert2521.infiniterecharge2020.climber.Climber
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.practiceBotChooser
import org.sert2521.infiniterecharge2020.powerhouse.PowerHouse
import org.sert2521.infiniterecharge2020.powerhouse.closeHouse
import org.sert2521.infiniterecharge2020.powerhouse.openHouse
import org.sert2521.sertain.events.onEnable
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
    add<PathGenerator>()

    onEnable {
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
        centerInitPowerPort(false, PathGenerator.endLocation.TRENCH)
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
