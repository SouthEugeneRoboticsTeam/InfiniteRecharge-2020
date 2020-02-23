package org.sert2521.infiniterecharge2020

import org.sert2521.infiniterecharge2020.autonomous.PathGenerator
import org.sert2521.infiniterecharge2020.autonomous.auto
import org.sert2521.infiniterecharge2020.climber.Climber
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.powerhouse.PowerHouse
import org.sert2521.infiniterecharge2020.powerhouse.closeHouse
import org.sert2521.sertain.events.onEnable
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

    onEnable {
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
        auto(PathGenerator.startlocation.CENTER, listOf(PathGenerator.tasks.LOADINGSTATION,
                                                        PathGenerator.tasks.UNLOAD,
                                                        PathGenerator.tasks.TRENCH,
                                                        PathGenerator.tasks.BALLS3))
    }
}
