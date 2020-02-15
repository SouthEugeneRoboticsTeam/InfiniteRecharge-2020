package org.sert2521.infiniterecharge2020.autonomous

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.runPath
import org.sert2521.infiniterecharge2020.powerhouse.banish
import org.sert2521.infiniterecharge2020.powerhouse.welcome
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun rightInitPowerPort(pushBack: Boolean, endLocation: PathGenerator.endLocation) = doTask {
    val drivetrain = use<Drivetrain>()
    val pathGenerator = use<PathGenerator>()
    drivetrain.odometry.resetPosition(Pose2d(-2.2, -1.22, Rotation2d(0.0)), Rotation2d(0.0))

    action {
        if (pushBack) {
            val pushBackPath = pathGenerator.pushBack(0.30)
            runPath(drivetrain, pushBackPath)
        }

        val toPowerPortPath = pathGenerator.initToPowerPort()
        println(toPowerPortPath.states)
        runPath(drivetrain, toPowerPortPath)

        val banishJob = launch {
            banish()
        }

        delay(3000)
        banishJob.cancelAndJoin()
        delay(1000)

        if (endLocation == PathGenerator.endLocation.TRENCH) {
            val toTrenchFromPortPath = pathGenerator.powerPortToTrench()
            println(toTrenchFromPortPath.states)
            runPath(drivetrain, toTrenchFromPortPath)

            val trenchRunPath = pathGenerator.trenchRun(5.0)

            val welcomeJob = launch {
                welcome()
            }

            println(trenchRunPath.states)
            runPath(drivetrain, trenchRunPath)
            welcomeJob.cancel()
        } else if (endLocation == PathGenerator.endLocation.LOADING_STATION) {
            val loadingStationPath = pathGenerator.loadingStation()
            runPath(drivetrain, loadingStationPath)
        }
    }
}

suspend fun centerInitPowerPort(pushBack: Boolean, endLocation: PathGenerator.endLocation) = doTask {
    val drivetrain = use<Drivetrain>()
    val pathGenerator = use<PathGenerator>()
    drivetrain.odometry.resetPosition(Pose2d(-2.143, 0.0, Rotation2d(0.0)), Rotation2d(0.0))

    action {
        if (pushBack) {
            val pushBackPath = pathGenerator.pushBack(0.30)
            runPath(drivetrain, pushBackPath)
        }

        val toPowerPortPath = pathGenerator.initToPowerPort()
        println(toPowerPortPath.states)
        runPath(drivetrain, toPowerPortPath)

        val banishJob = launch {
            banish()
        }

        delay(3000)
        banishJob.cancelAndJoin()
        delay(1000)

        if (endLocation == PathGenerator.endLocation.TRENCH) {
            val toTrenchFromPortPath = pathGenerator.powerPortToTrench()
            println(toTrenchFromPortPath.states)
            runPath(drivetrain, toTrenchFromPortPath)

            val trenchRunPath = pathGenerator.trenchRun(6.0)
            val welcomeJob = launch {
                welcome()
            }

            println(trenchRunPath.states)
            runPath(drivetrain, trenchRunPath)
            welcomeJob.cancel()
        } else if (endLocation == PathGenerator.endLocation.LOADING_STATION) {
            val loadingStationPath = pathGenerator.loadingStation()
            runPath(drivetrain, loadingStationPath)
        }
    }
}