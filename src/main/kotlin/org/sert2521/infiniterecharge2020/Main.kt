package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.geometry.Translation2d
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.autonomous.rightInitPowerPortTrenchRun
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.kinematics
import org.sert2521.infiniterecharge2020.drivetrain.runPath
import org.sert2521.sertain.events.onAuto
import org.sert2521.sertain.events.onEnable
import org.sert2521.sertain.events.whileAuto
import org.sert2521.sertain.events.whileTeleop
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.access
import org.sert2521.sertain.subsystems.add
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import org.sert2521.sertain.telemetry.Table
import org.sert2521.sertain.telemetry.TableEntry
import org.sert2521.sertain.units.*
import kotlin.math.absoluteValue

suspend fun main() = robot {
    println("Robot program starting")
    add<Drivetrain>()

    onEnable {
        val dt = access<Drivetrain>()
        dt.gyro.reset()
    }

    whileTeleop {
        initControls()
    }

    onAuto {
        println("Resetting gyro and encoders")
        val dt = access<Drivetrain>()
        dt.gyro.reset()
        dt.zeroEncoders()
        rightInitPowerPortTrenchRun()
    }
}