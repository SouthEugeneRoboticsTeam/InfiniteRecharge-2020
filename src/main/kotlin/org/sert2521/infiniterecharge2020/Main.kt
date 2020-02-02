package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.geometry.Translation2d
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.Velocity
import org.sert2521.infiniterecharge2020.drivetrain.kinematics
import org.sert2521.infiniterecharge2020.drivetrain.runPath
import org.sert2521.sertain.events.onEnable
import org.sert2521.sertain.events.whileAuto
import org.sert2521.sertain.events.whileTeleop
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.access
import org.sert2521.sertain.subsystems.add
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import org.sert2521.sertain.units.MetricUnit
import org.sert2521.sertain.units.MetricValue
import org.sert2521.sertain.units.mps
import kotlin.math.PI

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

    whileAuto {
        doTask {
            val drivetrain = use<Drivetrain>()
            action {
                drivetrain.zeroEncoders()
                drivetrain.gyro.reset()
                drivetrain.odometry.resetPosition(Pose2d(0.0, 0.0, Rotation2d(0.0)), Rotation2d(0.0))
                val trajectory = TrajectoryGenerator.generateTrajectory(
                        Pose2d(0.0, 0.0, Rotation2d(0.0)),
                        listOf(Translation2d(2.0, 1.0), Translation2d(4.0, -1.0)),
                        Pose2d(5.0, 0.0, Rotation2d(0.0)),
                        TrajectoryConfig(1.0, 0.4).setKinematics(kinematics)
                )
                runPath(
                        trajectory,
                        { drivetrain.pose },
                        RamseteController(),
                        kinematics,
                        { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) }
                )
            }
        }
    }
}
