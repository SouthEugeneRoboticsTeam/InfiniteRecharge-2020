package org.sert2521.infiniterecharge2020.autonomous

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.kinematics
import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.subsystems.access
import org.sert2521.sertain.units.Degrees
import org.sert2521.sertain.units.Radians
import org.sert2521.sertain.units.convert

class PathGenerator() : Subsystem("Path Generator") {
    val drivetrain = access<Drivetrain>()

    fun pushBack(pushDistance: Double): Trajectory {
        return TrajectoryGenerator.generateTrajectory(
                listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(0.0)),
                        Pose2d(drivetrain.xTranslation - pushDistance, drivetrain.yTranslation, Rotation2d((0.0)))),
                TrajectoryConfig(3.0, 3.0).setKinematics(kinematics).setReversed(true)
        )
    }

    fun initToPowerPort(): Trajectory {
        return TrajectoryGenerator.generateTrajectory(
                listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(0.0)),
                        Pose2d(0.0, 0.0, Rotation2d((0.0)))),
                TrajectoryConfig(2.0, 3.0).setKinematics(kinematics)
        )
    }

    fun powerPortToTrench(): Trajectory {
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                Pose2d(-.75, -1.65, Rotation2d((171.0).convert(Degrees to Radians)))),
                TrajectoryConfig(2.0, 2.0).setKinematics(kinematics).setReversed(true))
    }

    fun trenchRun(distance: Double): Trajectory {
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                Pose2d(drivetrain.xTranslation - distance, drivetrain.yTranslation, Rotation2d((-180.0).convert(Degrees to Radians))))
                , TrajectoryConfig(2.0, 2.0).setKinematics(kinematics))
    }
}