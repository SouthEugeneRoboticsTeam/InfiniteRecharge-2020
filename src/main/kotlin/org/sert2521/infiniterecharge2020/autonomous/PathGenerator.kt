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
        // Facing the Alliance Station. ~2.2 meters from power port | Aligned with line marking trench boundary
        val RIGHT_ALLIANCE = Pair(Pose2d(-2.2, -1.32, Rotation2d(0.0)), Rotation2d(0.0))
        // Facing the Trench. Balls aligned to center of robot | Edge of bumpers contacting initiation line
        // Increase x to make robot go further
        val RIGHT_TRENCH = Pair(Pose2d(3.2, 1.2264, Rotation2d(0.0)), Rotation2d(0.0))
        // Centered about Lower Port and facing Alliance Station. ~2.2 meters from power port | Centered on power port
        val CENTER = Pair(Pose2d(-2.143, 0.0, Rotation2d(0.0)), Rotation2d(0.0))
    }

    enum class tasks {
        CORNER_TO_TRENCH, BALLS2, PUSHBACK, TRENCH_TO_CORNER, UNLOAD_FROM_CORNER,
        UNLOAD_FROM_POWERPORT, AWAY_FROM_POWERPORT, DRIVE_FORWARD
    }

    // Drives the robot straight backwards for given distance.
    // HAS NOT BEEN TESTED PUSHING A REAL ROBOT
    fun pushBack(pushDistance: Double): Trajectory {
        return TrajectoryGenerator.generateTrajectory(
                listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(0.0)),
                        Pose2d(drivetrain.xTranslation - pushDistance, drivetrain.yTranslation, Rotation2d((0.0)))),
                TrajectoryConfig(1.0, 1.0).setKinematics(kinematics).setReversed(true)
        )
    }

    // Drives the robot forward from the initiation line to the Power Port. Works for center and right starting positions
    fun initToPowerPort(): Trajectory {
        return TrajectoryGenerator.generateTrajectory(
                listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                        Pose2d(0.0, 0.0, Rotation2d((0.0)))),
                TrajectoryConfig(3.0, 1.0).setKinematics(kinematics)
        )
    }

    // Drives the robot backwards in an arc to a corner in front of Alliance Station 1
    fun powerPortToCorner(): Trajectory {
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                Pose2d(-.75, -1.65, Rotation2d((174.0).convert(Degrees to Radians)))),
                TrajectoryConfig(3.0, 1.0).setKinematics(kinematics).setReversed(true))
    }

    // Drives the robot straight forwards for a given distance
    fun driveForward(distance: Double, startAwayFromAS: Boolean): Trajectory {
        // startAwayFromAS: if robot starts facing away from the alliance station.
        val modifier = if (startAwayFromAS) 1.0 else -1.0
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                Pose2d(drivetrain.xTranslation + (modifier * distance), drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians)))),
                TrajectoryConfig(3.0, 1.0).setKinematics(kinematics))
    }

    // Drives the robot in reverse to the corner in front of Alliance Station 1
    fun trenchToCorner(): Trajectory {
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                Pose2d(.75, 1.2264, Rotation2d((0.0).convert(Degrees to Radians)))),
                TrajectoryConfig(3.0, 1.0).setKinematics(kinematics).setReversed(true))
    }

    // Drives the robot in an arc from the Alliance Station corner to the Power Port
    fun cornerToPowerPort(): Trajectory {
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                // Make y more negative to make the robot translate more to the left facing the power port
                Pose2d(0.0, -0.75, Rotation2d((-180.0).convert(Degrees to Radians)))),
                TrajectoryConfig(2.0, 1.0).setKinematics(kinematics))
    }

    // Drives the robot left backwards in an arc to a position that clears the Power Port
    fun awayFromPowerPort(): Trajectory {
        // TODO: Increase 0.5 later                 \/
        return TrajectoryGenerator.generateTrajectory(listOf(Pose2d(drivetrain.xTranslation, drivetrain.yTranslation, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                Pose2d(drivetrain.xTranslation - 0.5, drivetrain.yTranslation + 2.5, Rotation2d((-90.0).convert(Degrees to Radians)))),
                TrajectoryConfig(2.0, 1.0).setKinematics(kinematics).setReversed(true))
    }
}
