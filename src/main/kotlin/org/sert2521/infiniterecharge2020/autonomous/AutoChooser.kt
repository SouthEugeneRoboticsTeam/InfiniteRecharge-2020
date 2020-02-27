package org.sert2521.infiniterecharge2020.autonomous

import org.sert2521.infiniterecharge2020.drivetrain
import org.sert2521.sertain.telemetry.TableEntry
import org.sert2521.sertain.telemetry.dropdown
import org.sert2521.sertain.telemetry.gives
import java.nio.file.Path

val autoDisplay = TableEntry("Auto", "No auto selected")
val pushBack = TableEntry("Pushback", false)

enum class StartingPose {
    NO_AUTO,
    DRIVE_FORWARD,
    RIGHT_ALLIANCE,
    RIGHT_TRENCH,
    CENTER
}

enum class Objective {
    NONE,
    POWER_PORT,
    TRENCH,
    LOADING_STATION
}

val startingPose = dropdown("None" gives StartingPose.NO_AUTO) {
    "Right Alliance" gives StartingPose.RIGHT_ALLIANCE
    "Right Trench" gives StartingPose.RIGHT_TRENCH
    "Center" gives StartingPose.CENTER
}

val objective1 = dropdown("None" gives Objective.NONE) {
    "Power Port" gives StartingPose.RIGHT_ALLIANCE
    "Trench" gives StartingPose.RIGHT_TRENCH
}

val objective2 = dropdown("None" gives Objective.NONE) {
    "Power Port" gives StartingPose.RIGHT_ALLIANCE
    "Trench" gives StartingPose.RIGHT_TRENCH
}

fun getAuto(startingPose: StartingPose, objective1: Objective, objective2: Objective): Nothing = TODO("Make it do stuff")
