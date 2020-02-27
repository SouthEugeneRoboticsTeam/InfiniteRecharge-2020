package org.sert2521.infiniterecharge2020.autonomous

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.alignToBall
import org.sert2521.infiniterecharge2020.drivetrain.runPath
import org.sert2521.infiniterecharge2020.powerhouse.banish
import org.sert2521.infiniterecharge2020.powerhouse.closeHouse
import org.sert2521.infiniterecharge2020.powerhouse.welcome
import org.sert2521.sertain.events.onTick

//suspend fun auto(startLocation: Pair<Pose2d, Rotation2d>, vararg tasks: PathGenerator.Task) = drivetrain { drivetrain ->
//    drivetrain.odometry.resetPosition(startLocation.first, startLocation.second)
//
//    tasks.forEach {
//        when (it) {
//            PathGenerator.Task.UNLOAD_FROM_POWERPORT -> {
//                val toPowerPortPath = PathGenerator.initToPowerPort()
//                println(toPowerPortPath.states)
//                runPath(drivetrain, toPowerPortPath)
//
//                val banishJob = launch {
//                    banish()
//                }
//                // Run the outtake for 1.2 seconds
//                delay(1200)
//                banishJob.cancelAndJoin()
//                closeHouse()
//            }
//
//            PathGenerator.Task.UNLOAD_FROM_CORNER -> {
//                val toPowerPortFromCornerPath = PathGenerator.cornerToPowerPort()
//                println(toPowerPortFromCornerPath.states)
//                runPath(drivetrain, toPowerPortFromCornerPath)
//
//                val banishJob = launch {
//                    banish()
//                }
//                // Run the outtake for 1.2 seconds
//                delay(2000)
//                banishJob.cancelAndJoin()
//                closeHouse()
//            }
//
//            PathGenerator.Task.TRENCH_TO_CORNER -> {
//                val trenchToCorner = PathGenerator.trenchToCorner()
//                println(trenchToCorner.states)
//                runPath(drivetrain, trenchToCorner)
//            }
//
//            PathGenerator.Task.CORNER_TO_TRENCH -> {
//                val toTrenchFromPortPath = PathGenerator.powerPortToTrench()
//                println(toTrenchFromPortPath.states)
//                runPath(drivetrain, toTrenchFromPortPath)
//
//                val trenchRunPath = PathGenerator.trenchRun(2.0)
//                println(trenchRunPath.states)
//
//                runPath(drivetrain, trenchRunPath)
//            }
//
//            PathGenerator.Task.BALLS2 -> {
//                val welcomeJob = launch {
//                    welcome()
//                }
//
//                val alignJob = launch {
//                    alignToBall(0.0)
//                }
//
//                // Stop using vision to pick up balls after the robot has driven into the trench for a certain time
//                delay(2600)
//                alignJob.cancel()
//                welcomeJob.cancel()
//            }
//
//            PathGenerator.Task.BALLS3 -> {
//                val welcomeJob = launch {
//                    welcome()
//                }
//
//                val alignJob = launch {
//                    alignToBall(0.0)
//                }
//                onTick {
//                    println(drivetrain.xTranslation)
//                    // Robot takes around 0.87 meters to stop
//                    // Value can be increased to potentially acquire a 4th ball
//                    if (drivetrain.xTranslation < -6.825) {
//                        println("I SHOULD BE STOPPING")
//                        alignJob.cancel()
//                        welcomeJob.cancel()
//                    }
//                }
//            }
//
//            PathGenerator.Task.PUSHBACK -> {
//                val pushBackPath = PathGenerator.pushBack(0.30)
//                runPath(drivetrain, pushBackPath)
//            }
//        }
//    }
//}

suspend fun unloadFromPowerPort() = drivetrain { drivetrain ->
    val toPowerPortPath = PathGenerator.initToPowerPort()
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

suspend fun unloadFromCorner() = drivetrain { drivetrain ->
    val toPowerPortFromCornerPath = PathGenerator.cornerToPowerPort()
    println(toPowerPortFromCornerPath.states)
    runPath(drivetrain, toPowerPortFromCornerPath)

    val banishJob = launch {
        banish()
    }
    // Run the outtake for 1.2 seconds
    delay(2000)
    banishJob.cancelAndJoin()
    closeHouse()
}

suspend fun trenchToCorner() = drivetrain { drivetrain ->
    val trenchToCorner = PathGenerator.trenchToCorner()
    println(trenchToCorner.states)
    runPath(drivetrain, trenchToCorner)
}

suspend fun cornerToTrench() = drivetrain { drivetrain ->
    val toTrenchFromPortPath = PathGenerator.powerPortToTrench()
    println(toTrenchFromPortPath.states)
    runPath(drivetrain, toTrenchFromPortPath)

    val trenchRunPath = PathGenerator.trenchRun(2.0)
    println(trenchRunPath.states)

    runPath(drivetrain, trenchRunPath)
}

suspend fun powerPortToLoadingStation() = drivetrain { drivetrain ->
    val toLoadingStation = PathGenerator.loadingStation()

    println(toLoadingStation.states)
    runPath(drivetrain, toLoadingStation)
}

suspend fun welcome2() = drivetrain {
    val welcomeJob = launch {
        welcome()
    }

    val alignJob = launch {
        alignToBall(0.0)
    }

    // Stop using vision to pick up balls after the robot has driven into the trench for a certain time
    delay(2600)
    alignJob.cancel()
    welcomeJob.cancel()
}

suspend fun welcome3() = drivetrain { drivetrain ->
    val welcomeJob = launch {
        welcome()
    }

    val alignJob = launch {
        alignToBall(0.0)
    }
    onTick {
        println(drivetrain.xTranslation)
        // Robot takes around 0.87 meters to stop
        // Value can be increased to potentially acquire a 4th ball
        if (drivetrain.xTranslation < -6.825) {
            println("I SHOULD BE STOPPING")
            alignJob.cancel()
            welcomeJob.cancel()
        }
    }
}

suspend fun pushBack() = drivetrain { drivetrain ->
    val pushBackPath = PathGenerator.pushBack(0.30)
    runPath(drivetrain, pushBackPath)
}
