package org.sert2521.infiniterecharge2020.autonomous

import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.runPath
import org.sert2521.infiniterecharge2020.powerhouse.banish
import org.sert2521.infiniterecharge2020.powerhouse.closeHouse
import org.sert2521.infiniterecharge2020.powerhouse.welcome
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun auto(startLocation: Pair<Pose2d, Rotation2d>, tasks: List<PathGenerator.tasks>) = doTask {
    val drivetrain = use<Drivetrain>()
    val pathGenerator = PathGenerator()
    drivetrain.odometry.resetPosition(startLocation.first, startLocation.second)

    action {
        tasks.forEach {
            when (it) {
                PathGenerator.tasks.UNLOAD_FROM_POWERPORT -> {
                    val toPowerPortPath = pathGenerator.initToPowerPort()
                    println(toPowerPortPath.states)
                    runPath(drivetrain, toPowerPortPath)

                    val banishJob = launch {
                        banish()
                    }
                    // Run the outtake for 1.2 seconds
                    delay(1200)
                    banishJob.cancelAndJoin()
                    closeHouse()
                }

                PathGenerator.tasks.UNLOAD_FROM_CORNER -> {
                    val toPowerPortFromCornerPath = pathGenerator.cornerToPowerPort()
                    println(toPowerPortFromCornerPath.states)
                    runPath(drivetrain, toPowerPortFromCornerPath)

                    val banishJob = launch {
                        banish()
                    }
//                    // Run the outtake for 1.2 seconds
//                    delay(2000)
//                    banishJob.cancelAndJoin()
//                    closeHouse()
                }

                PathGenerator.tasks.TRENCH_TO_CORNER -> {
                    val trenchToCorner = pathGenerator.trenchToCorner()
                    println(trenchToCorner.states)
                    runPath(drivetrain, trenchToCorner)
                }

                PathGenerator.tasks.CORNER_TO_TRENCH -> {
                    val toTrenchFromPortPath = pathGenerator.powerPortToTrench()
                    println(toTrenchFromPortPath.states)
                    runPath(drivetrain, toTrenchFromPortPath)

                    val welcomeJob = launch {
                        welcome()
                    }
                    // Distance can probably be bumped up further
                    val trenchRunPath = pathGenerator.driveForward(7.0, false)
                    println(trenchRunPath.states)
                    runPath(drivetrain, trenchRunPath)
                    welcomeJob.cancel()
                }

                PathGenerator.tasks.BALLS2 -> {
                    val driveForwardPath = pathGenerator.driveForward(3.75, true)
                    val welcomeJob = launch {
                        welcome()
                    }
                    runPath(drivetrain, driveForwardPath)
                    welcomeJob.cancel()
                }

//                PathGenerator.tasks.BALLS3 -> {
//                    val welcomeJob = launch {
//                        welcome()
//                    }
//
//                    val alignJob = launch {
//                        alignToBall(0.0)
//                    }
//                    onTick {
//                        println(drivetrain.xTranslation)
//                        // Robot takes around 0.87 meters to stop
//                        // Value can be increased to potentially acquire a 4th ball
//                        if (drivetrain.xTranslation < -6.825) {
//                            println("I SHOULD BE STOPPING")
//                            alignJob.cancel()
//                            welcomeJob.cancel()
//                        }
//                    }
//                }

                PathGenerator.tasks.AWAY_FROM_POWERPORT -> {
                    val awayFromPowerPortPath = pathGenerator.awayFromPowerPort()
                    println(awayFromPowerPortPath)
                    runPath(drivetrain, awayFromPowerPortPath)
                }

                // Has not been tested on a real robot
                PathGenerator.tasks.PUSHBACK -> {
                    val pushBackPath = pathGenerator.pushBack(0.30)
                    runPath(drivetrain, pushBackPath)
                }
            }
        }
    }
}
