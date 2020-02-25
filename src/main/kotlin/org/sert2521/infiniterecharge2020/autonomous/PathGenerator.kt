package org.sert2521.infiniterecharge2020.autonomous

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.trajectory.Trajectory
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.kinematics
import org.sert2521.sertain.subsystems.access
import org.sert2521.sertain.units.Degrees
import org.sert2521.sertain.units.Radians
import org.sert2521.sertain.units.convert

class PathGenerator {
    val drivetrain = access<Drivetrain>()

    object startlocation {
        // Facing the Alliance Station. 2.2 meters from power port | Halfway between trench boundary and railing
        val RIGHT_ALLIANCE = Pair(Pose2d(-2.2, -1.22, Rotation2d(0.0)), Rotation2d(0.0))
        // Facing the Trench. 2.2 meters from power port | Bumpers aligned to trench boundary line
        val RIGHT_TRENCH = Pair(Pose2d(-2.2, -1.6264, Rotation2d(0.0)), Rotation2d(0.0))
        // Centered about Lower Port and facing Alliance Station. 2.2 meters from power port | Centered on power port
        val CENTER = Pair(Pose2d(-2.143, 0.0, Rotation2d(0.0)), Rotation2d(0.0))
    }

    enum class tasks {
        UNLOAD, CORNER_TO_TRENCH, BALLS3, BALLS2, PUSHBACK, TRENCH_TO_CORNER, UNLOAD_FROM_CORNER,
        UNLOAD_FROM_POWERPORT
    }

    fun pushBack(pushDistance: Double): Trajectory {
        return TrajectoryGenerator.generateTrajectory(
                listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(0.0)),
                        Pose2d(drivetrain.xTranslation - pushDistance, drivetrain.yTranslation, Rotation2d((0.0)))),
                TrajectoryConfig(1.0, 1.0).setKinematics(kinematics).setReversed(true)
        )
    }

    fun initToPowerPort(): Trajectory {
        return TrajectoryGenerator.generateTrajectory(
                listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                        Pose2d(0.0, 0.0, Rotation2d((0.0)))),
                TrajectoryConfig(1.0, 1.0).setKinematics(kinematics)
        )
    }

    fun powerPortToTrench(): Trajectory {
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                Pose2d(-.75, -1.65, Rotation2d((180.0).convert(Degrees to Radians)))),
                TrajectoryConfig(1.0, 1.0).setKinematics(kinematics).setReversed(true))
    }

    fun trenchRun(distance: Double): Trajectory {
        // TODO: I think the end rotation should be 180? Test soon
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                Pose2d(drivetrain.xTranslation - distance, drivetrain.yTranslation, Rotation2d((-171.0).convert(Degrees to Radians)))),
                TrajectoryConfig(1.0, 1.0).setKinematics(kinematics))
    }

    // NEEDS TO BE REWRITTEN
    fun trenchToCorner(): Trajectory {
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                Pose2d(-.75, -1.65, Rotation2d((180.0).convert(Degrees to Radians)))),
                TrajectoryConfig(0.5, 1.0).setKinematics(kinematics).setReversed(true))
    }

    // NEEDS TO BE REWRITTEN
    fun cornerToTrench(): Trajectory {
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                Pose2d(0.0, 0.0, Rotation2d((0.0).convert(Degrees to Radians)))),
                TrajectoryConfig(0.5, 1.0).setKinematics(kinematics).setReversed(true))
    }

    // TODO: THE TARGET TRANSLATION SHOULD BE SOMETHING MORE LIKE 2.5m INSTEAD OF 1.0m
    fun loadingStation(): Trajectory {
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                Pose2d(drivetrain.xTranslation - 1.0, drivetrain.yTranslation + 1.0, Rotation2d((-90.0).convert(Degrees to Radians)))),
                TrajectoryConfig(1.0, 1.0).setKinematics(kinematics).setReversed(true))
    }
}
