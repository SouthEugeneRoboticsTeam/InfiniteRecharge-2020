package org.sert2521.infiniterecharge2020.autonomous

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.alignToBall
import org.sert2521.infiniterecharge2020.drivetrain.runPath
import org.sert2521.infiniterecharge2020.powerhouse.banish
import org.sert2521.infiniterecharge2020.powerhouse.closeHouse
import org.sert2521.infiniterecharge2020.powerhouse.welcome
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun auto(startLocation: Pair<Pose2d, Rotation2d>, tasks: List<PathGenerator.tasks>) = doTask {
    val drivetrain = use<Drivetrain>()
    val pathGenerator = use<PathGenerator>()
    drivetrain.odometry.resetPosition(startLocation.first, startLocation.second)

    action {
        tasks.forEach {
            if (it == PathGenerator.tasks.UNLOAD) {
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
            }

            if (it == PathGenerator.tasks.TRENCH) {
                val toTrenchFromPortPath = pathGenerator.powerPortToTrench()
                println(toTrenchFromPortPath.states)
                runPath(drivetrain, toTrenchFromPortPath)

                val trenchRunPath = pathGenerator.trenchRun(2.0)
                println(trenchRunPath.states)

                runPath(drivetrain, trenchRunPath)
            }

            if (it == PathGenerator.tasks.BALLS2) {
                val welcomeJob = launch {
                    welcome()
                }

                val alignJob = launch {
                    alignToBall(0.0)
                }

                // TODO: This needs to be tuned. At 3000, I needed to disable in order to stop the robot from crashing into a table
                delay(3000)
                alignJob.cancel()
                welcomeJob.cancel()
            }

            if (it == PathGenerator.tasks.BALLS3) {
                val welcomeJob = launch {
                    welcome()
                }

                val alignJob = launch {
                    alignToBall(0.0)
                }

                // TODO: This needs to be tuned. At 3000, I needed to disable in order to stop the robot from crashing into a table
                delay(3000)
                alignJob.cancel()
                welcomeJob.cancel()
            }

            if (it == PathGenerator.tasks.PUSHBACK) {
                val pushBackPath = pathGenerator.pushBack(0.30)
                runPath(drivetrain, pushBackPath)
            }

            if (it == PathGenerator.tasks.PUSHBACK) {
                val loadingStationPath = pathGenerator.loadingStation()
                runPath(drivetrain, loadingStationPath)
            }
        }
    }
}
