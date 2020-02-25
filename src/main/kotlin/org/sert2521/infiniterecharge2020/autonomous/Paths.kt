package org.sert2521.infiniterecharge2020.autonomous

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.alignToBall
import org.sert2521.infiniterecharge2020.drivetrain.runPath
import org.sert2521.infiniterecharge2020.pathGenerator
import org.sert2521.infiniterecharge2020.powerHouse
import org.sert2521.infiniterecharge2020.powerhouse.banish
import org.sert2521.infiniterecharge2020.powerhouse.closeHouse
import org.sert2521.infiniterecharge2020.powerhouse.welcome
import org.sert2521.sertain.subsystems.use

// NOTE: Currently not running in under 15 seconds
suspend fun rightInitPowerPort(pushBack: Boolean, endLocation: PathGenerator.endLocation) = use(pathGenerator, drivetrain) { pathGenerator, drivetrain ->
    drivetrain.odometry.resetPosition(Pose2d(-2.2, -1.22, Rotation2d(0.0)), Rotation2d(0.0))

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

    // TODO: Could be even shorter
    delay(2000)
    banishJob.cancelAndJoin()
    closeHouse()

    if (endLocation == PathGenerator.endLocation.TRENCH) {
        val toTrenchFromPortPath = pathGenerator.powerPortToTrench()
        println(toTrenchFromPortPath.states)
        runPath(drivetrain, toTrenchFromPortPath)

        val trenchRunPath = pathGenerator.trenchRun(2.0)
        println(trenchRunPath.states)

        val welcomeJob = launch {
            welcome()
        }

        runPath(drivetrain, trenchRunPath)

        val alignJob = launch {
            alignToBall(0.0)
        }

        // TODO: This needs to be tuned. At 3000, I needed to disable in order to stop the robot from crashing into a table
        delay(3000)
        alignJob.cancel()
        welcomeJob.cancel()
    } else if (endLocation == PathGenerator.endLocation.LOADING_STATION) {
        val loadingStationPath = pathGenerator.loadingStation()
        runPath(drivetrain, loadingStationPath)
    }
}
// NOTE: Currently not running in under 15 seconds
suspend fun centerInitPowerPort(pushBack: Boolean, endLocation: PathGenerator.endLocation) = use(pathGenerator, drivetrain) { pathGenerator, drivetrain ->
    drivetrain.odometry.resetPosition(Pose2d(-2.143, 0.0, Rotation2d(0.0)), Rotation2d(0.0))

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

    // TODO: Could be even shorter?
    delay(2000)
    banishJob.cancelAndJoin()
    closeHouse()

    if (endLocation == PathGenerator.endLocation.TRENCH) {
        val toTrenchFromPortPath = pathGenerator.powerPortToTrench()
        println(toTrenchFromPortPath.states)
        runPath(drivetrain, toTrenchFromPortPath)

        val trenchRunPath = pathGenerator.trenchRun(2.0)
        println(trenchRunPath.states)

        val welcomeJob = launch {
            welcome()
        }

        runPath(drivetrain, trenchRunPath)

        val alignJob = launch {
            alignToBall(0.0)
        }

        // TODO: This needs to be tuned. At 3000, I needed to disable in order to stop the robot from crashing into a table
        delay(3000)
        alignJob.cancel()
        welcomeJob.cancel()
    } else if (endLocation == PathGenerator.endLocation.LOADING_STATION) {
        val loadingStationPath = pathGenerator.loadingStation()
        runPath(drivetrain, loadingStationPath)
    }
}
