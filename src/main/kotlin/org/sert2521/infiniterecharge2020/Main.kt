package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
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
import org.sert2521.sertain.telemetry.Table
import org.sert2521.sertain.telemetry.TableEntry
import org.sert2521.sertain.units.*

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

    val table = Table("Drivetrain")
    val distanceToPowerPort = TableEntry("PowerPort Distance", 2.134, table)

    whileAuto {
        doTask {
            val drivetrain = use<Drivetrain>()
            action {
                drivetrain.zeroEncoders()
                drivetrain.gyro.reset()
                drivetrain.odometry.resetPosition(Pose2d(0.0, 0.0, Rotation2d(0.0)), Rotation2d(0.0))
                println("Distance (m) to Power Port: ${distanceToPowerPort.value}")
                val initToPowerPort = TrajectoryGenerator.generateTrajectory(
                        Pose2d(0.0, 0.0, Rotation2d(0.0)),
                        listOf(),
                        Pose2d(distanceToPowerPort.value, 0.0, Rotation2d(Math.toRadians(0.0))),
                        TrajectoryConfig(2.0, 2.0).setKinematics(kinematics)
                )
                println("Time from initiation line to Power Port: ${initToPowerPort.totalTimeSeconds}")
                runPath(
                        initToPowerPort,
                        { drivetrain.pose },
                        RamseteController(),
                        kinematics,
                        { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) }
                )
                println("Resetting gyro, odometry, and encoders")
                drivetrain.gyro.reset()
                drivetrain.zeroEncoders()
                drivetrain.odometry.resetPosition(Pose2d(0.0, 0.0, Rotation2d(0.0)), Rotation2d(0.0))
                val powerPorttoTrench = TrajectoryGenerator.generateTrajectory(
                        Pose2d(0.0, 0.0, Rotation2d(0.0)),
                        listOf(),
                        Pose2d(-1.00, -1.60, Rotation2d((-180.0).convert(Degrees to Radians))),
                        TrajectoryConfig(2.0, 2.0).setKinematics(kinematics).setReversed(true)
                )
                println("Going from Power Port to Trench! Expected runtime: ${powerPorttoTrench.totalTimeSeconds}")
                runPath(powerPorttoTrench,
                        { drivetrain.pose },
                        RamseteController(),
                        kinematics,
                        { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
                println("INITIATION LINE TO POWER PORT:")
                println(initToPowerPort.states)
                println("POWER PORT TO TRENCH:")
                println(powerPorttoTrench.states)
            }
        }
    }
}