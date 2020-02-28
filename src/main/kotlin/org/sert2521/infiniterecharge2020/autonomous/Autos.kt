package org.sert2521.infiniterecharge2020.autonomous

import org.sert2521.infiniterecharge2020.drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.runPath

fun Drivetrain.setStartingPose(startingPose: StartingPose) {
    when (startingPose) {
        StartingPose.RIGHT_TRENCH ->
            odometry.resetPosition(PathGenerator.StartLocation.RIGHT_TRENCH.first, PathGenerator.StartLocation.RIGHT_TRENCH.second)
        StartingPose.CENTER ->
            odometry.resetPosition(PathGenerator.StartLocation.CENTER.first, PathGenerator.StartLocation.CENTER.second)
        StartingPose.RIGHT_ALLIANCE ->
            odometry.resetPosition(PathGenerator.StartLocation.RIGHT_ALLIANCE.first, PathGenerator.StartLocation.CENTER.second)
    }
}

suspend fun driveForward() = drivetrain { drivetrain ->
    val driveForward = PathGenerator.driveForward(0.5, false)
    runPath(drivetrain, driveForward)
}

// Works from either Right Alliance or Center
suspend fun centerToPowerPortToTrench() = drivetrain { drivetrain ->
    drivetrain.setStartingPose(StartingPose.CENTER)
    unloadFromInit()
    powerPortToCornerToTrench()
}

// Works from either Right Alliance or Center
suspend fun rightToPowerPortToTrench() = drivetrain { drivetrain ->
    drivetrain.setStartingPose(StartingPose.RIGHT_ALLIANCE)
    unloadFromInit()
    powerPortToCornerToTrench()
}

suspend fun centerToPowerPortToLoadingStation() = drivetrain { drivetrain ->
    drivetrain.setStartingPose(StartingPose.CENTER)
    unloadFromInit()
    powerPortToLoadingStation()
}

suspend fun rightToPowerPortToLoadingStation() = drivetrain { drivetrain ->
    drivetrain.setStartingPose(StartingPose.RIGHT_ALLIANCE)
    unloadFromInit()
    powerPortToLoadingStation()
}

// Right Trench -> Trench -> Power Port, Five ball auto
suspend fun rightTrenchToTrenchToPowerPort() = drivetrain { drivetrain ->
    drivetrain.setStartingPose(StartingPose.RIGHT_TRENCH)
    welcome2()
    trenchToCorner()
    unloadFromCorner()
}
