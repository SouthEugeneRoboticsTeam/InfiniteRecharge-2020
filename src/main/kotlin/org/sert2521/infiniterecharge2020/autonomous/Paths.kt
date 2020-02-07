package org.sert2521.infiniterecharge2020.autonomous

import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.kinematics
import org.sert2521.infiniterecharge2020.drivetrain.runPath
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import org.sert2521.sertain.units.Degrees
import org.sert2521.sertain.units.Radians
import org.sert2521.sertain.units.convert
import org.sert2521.sertain.units.mps

suspend fun centerInitPowerPortTrenchRun() = doTask {
    val drivetrain = use<Drivetrain>()
    action {
        drivetrain.odometry.resetPosition(Pose2d(-2.134, 0.0, Rotation2d(0.0)), Rotation2d(0.0))

        val initToPowerPort = TrajectoryGenerator.generateTrajectory(
                Pose2d(-2.134, 0.0, Rotation2d(0.0)),
                listOf(),
                Pose2d(0.0, 0.0, Rotation2d(Math.toRadians(0.0))),
                TrajectoryConfig(3.0, 3.0).setKinematics(kinematics)
        )
        val powerPortToTrench = TrajectoryGenerator.generateTrajectory(
                Pose2d(0.0, 0.0, Rotation2d(0.0)),
                listOf(),
                Pose2d(-.75, -1.75, Rotation2d((-180.0).convert(Degrees to Radians))),
                TrajectoryConfig(2.0, 2.0).setKinematics(kinematics).setReversed(true)
        )

        println("INITIATION LINE TO POWER PORT:")
        println(initToPowerPort.states)
        println("POWER PORT TO TRENCH:")
        println(powerPortToTrench.states)


        println("Time from initiation line to Power Port: ${initToPowerPort.totalTimeSeconds}")
        runPath(
                initToPowerPort,
                { drivetrain.pose },
                RamseteController(),
                kinematics,
                { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) }
        )
        println("Going from Power Port to Trench! Expected runtime: ${powerPortToTrench.totalTimeSeconds}")
        runPath(powerPortToTrench,
                { drivetrain.pose },
                RamseteController(),
                kinematics,
                { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
        val trenchBallPickup = TrajectoryGenerator.generateTrajectory(
                listOf(Pose2d(drivetrain.odometry.poseMeters.translation.x, drivetrain.odometry.poseMeters.translation.y, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                        Pose2d(-.75 - 5.0, -1.75, Rotation2d((-180.0).convert(Degrees to Radians))))
                , TrajectoryConfig(2.0, 2.0).setKinematics(kinematics)
        )
        println("Running down the Trench! Expected runtime: ${trenchBallPickup}")
        println(trenchBallPickup.states)
        runPath(trenchBallPickup,
                { drivetrain.pose },
                RamseteController(),
                kinematics,
                { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
    }
}

suspend fun rightInitPowerPortTrenchRun() = doTask {
    val drivetrain = use<Drivetrain>()
    action {
        drivetrain.odometry.resetPosition(Pose2d(-2.2, -1.22, Rotation2d(0.0)), Rotation2d(0.0))
        // TODO: TUNE MORE ON PRACTICE BOT
        val initToPowerPort = TrajectoryGenerator.generateTrajectory(
                Pose2d(-2.2, -1.22, Rotation2d(0.0)),
                listOf(),
                Pose2d(0.0, 0.0, Rotation2d(Math.toRadians(0.0))),
                TrajectoryConfig(2.0, 2.0).setKinematics(kinematics)
        )

        println("INITIATION LINE TO POWER PORT:")
        println(initToPowerPort.states)

        println("Time from initiation line to Power Port: ${initToPowerPort.totalTimeSeconds}")
        runPath(
                initToPowerPort,
                { drivetrain.pose },
                RamseteController(),
                kinematics,
                { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) }
        )

        val powerPorttoTrench = TrajectoryGenerator.generateTrajectory(
                Pose2d(drivetrain.odometry.poseMeters.translation.x, drivetrain.odometry.poseMeters.translation.y, Rotation2d(0.0)),
                listOf(),
                Pose2d(-.75, -1.95, Rotation2d((-180.0).convert(Degrees to Radians))),
                TrajectoryConfig(2.0, 2.0).setKinematics(kinematics).setReversed(true)
        )

        println("Going from Power Port to Trench! Expected runtime: ${powerPorttoTrench.totalTimeSeconds}")
        println(powerPorttoTrench.states)

        runPath(powerPorttoTrench,
                { drivetrain.pose },
                RamseteController(),
                kinematics,
                { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
        val trenchBallPickup = TrajectoryGenerator.generateTrajectory(
                listOf(Pose2d(drivetrain.odometry.poseMeters.translation.x, drivetrain.odometry.poseMeters.translation.y, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
                        Pose2d(-.75 - 5.0, -1.75, Rotation2d((-180.0).convert(Degrees to Radians))))
                , TrajectoryConfig(2.0, 2.0).setKinematics(kinematics)
        )
        println("Running down the Trench! Expected runtime: ${trenchBallPickup.totalTimeSeconds}")
        println(trenchBallPickup.states)
        runPath(trenchBallPickup,
                { drivetrain.pose },
                RamseteController(),
                kinematics,
                { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
    }
}


//        doTask {
//            val drivetrain = use<Drivetrain>()
//            action {
//                println("Resetting gyro, odometry, and encoders")
//                drivetrain.gyro.reset()
//                drivetrain.zeroEncoders()
//                drivetrain.odometry.resetPosition(Pose2d(-distanceToPowerPort.value, 0.0, Rotation2d(0.0)), Rotation2d(0.0))
//
//                val initToPowerPort = TrajectoryGenerator.generateTrajectory(
//                        Pose2d(-distanceToPowerPort.value, 0.0, Rotation2d(0.0)),
//                        listOf(),
//                        Pose2d(0.0, 0.0, Rotation2d(Math.toRadians(0.0))),
//                        TrajectoryConfig(2.0, 2.0).setKinematics(kinematics)
//                )
//                val powerPorttoTrench = TrajectoryGenerator.generateTrajectory(
//                        Pose2d(0.0, 0.0, Rotation2d(0.0)),
//                        listOf(),
//                        Pose2d(-.75, -1.75, Rotation2d((-180.0).convert(Degrees to Radians))),
//                        TrajectoryConfig(2.0, 2.0).setKinematics(kinematics).setReversed(true)
//                )
//
//                println("INITIATION LINE TO POWER PORT:")
//                println(initToPowerPort.states)
//                println("POWER PORT TO TRENCH:")
//                println(powerPorttoTrench.states)
//
//
//                println("Time from initiation line to Power Port: ${initToPowerPort.totalTimeSeconds}")
//                runPath(
//                        initToPowerPort,
//                        { drivetrain.pose },
//                        RamseteController(),
//                        kinematics,
//                        { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) }
//                )
//                println("Resetting gyro, odometry, and encoders")
////                drivetrain.gyro.reset()
////                drivetrain.zeroEncoders()
////                drivetrain.odometry.resetPosition(Pose2d(0.0, 0.0, Rotation2d(0.0)), Rotation2d(0.0))
//                runPath(powerPorttoTrench,
//                        { drivetrain.pose },
//                        RamseteController(),
//                        kinematics,
//                        { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
//                println("Going from Power Port to Trench! Expected runtime: ${powerPorttoTrench.totalTimeSeconds}")
//                val trenchBallPickup = TrajectoryGenerator.generateTrajectory(
//                        listOf(Pose2d(drivetrain.odometry.poseMeters.translation.x, drivetrain.odometry.poseMeters.translation.y, Rotation2d(drivetrain.heading.convert(Degrees to Radians))),
//                                Pose2d(-.75 - 5.0, -1.75, Rotation2d((-180.0).convert(Degrees to Radians))))
//                        , TrajectoryConfig(2.0, 2.0).setKinematics(kinematics)
//                )
//                println("Adjusted Heading Target: ${drivetrain.heading}")
////                drivetrain.gyro.reset()
//                drivetrain.zeroEncoders()
////                drivetrain.odometry.resetPosition(Pose2d(0.0, 0.0, Rotation2d(0.0)), Rotation2d(0.0))
//                println("TRENCH RUN")
//                println(trenchBallPickup.states)
//                runPath(trenchBallPickup,
//                        { drivetrain.pose },
//                        RamseteController(),
//                        kinematics,
//                        { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
//            }
//        }
//    }
