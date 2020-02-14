package org.sert2521.infiniterecharge2020

import org.sert2521.infiniterecharge2020.autonomous.PathGenerator
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
    add<PathGenerator>()
    add<PowerHouse>()

    onEnable {
        closeHouse()
        val dt = access<Drivetrain>()
        dt.gyro.reset()
        dt.zeroEncoders()
    }

    whileTeleop {
        initControls()
    }

    whileAuto {
        println("Resetting gyro and encoders")
        val dt = access<Drivetrain>()
        dt.gyro.reset()
        dt.zeroEncoders()
    }
}