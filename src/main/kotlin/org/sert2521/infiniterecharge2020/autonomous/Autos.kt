package org.sert2521.infiniterecharge2020.autonomous

import org.sert2521.infiniterecharge2020.drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.sertain.utils.timer

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
    timer(20, 0, 2000) {
        drivetrain.tankDrive(0.2, 0.2)
    }
}

// Works from either Right Alliance or Center
suspend fun initToPowerPortToTrench() = drivetrain { drivetrain ->
    drivetrain.setStartingPose(StartingPose.CENTER)
    unloadFromPowerPort()
    trenchToCorner()
    welcome3()
}

suspend fun initToLoadingStation() = drivetrain { drivetrain ->
    drivetrain.setStartingPose(StartingPose.CENTER)
    unloadFromPowerPort()
    powerPortToLoadingStation()
}

// Right Trench -> Trench -> Power Port, Five ball auto
suspend fun rightTrenchToTrenchToPowerPort() = drivetrain { drivetrain ->
    drivetrain.setStartingPose(StartingPose.RIGHT_TRENCH)
    welcome2()
    cornerToTrench()
    unloadFromCorner()
}
