package org.sert2521.infiniterecharge2020.autonomous

import kotlinx.coroutines.CoroutineScope
import org.sert2521.infiniterecharge2020.utils.dropdown
import org.sert2521.infiniterecharge2020.utils.gives
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.telemetry.TableEntry

var auto = getAuto(StartingPose.NO_AUTO, Objective.NONE, Objective.NONE)

val autoMessage = TableEntry("Auto", auto.first, "Auto")
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

val startingPose = dropdown("Starting Pose", "None" gives StartingPose.NO_AUTO) {
    "Right Alliance" gives StartingPose.RIGHT_ALLIANCE
    "Right Trench" gives StartingPose.RIGHT_TRENCH
    "Center" gives StartingPose.CENTER
}

val objective1 = dropdown("Objective 1", "None" gives Objective.NONE) {
    "Power Port" gives Objective.POWER_PORT
    "Trench" gives Objective.TRENCH
    "Loading Station" gives Objective.LOADING_STATION
}

val objective2 = dropdown("Objective 2", "None" gives Objective.NONE) {
    "Power Port" gives Objective.POWER_PORT
    "Trench" gives Objective.TRENCH
    "Loading Station" gives Objective.LOADING_STATION
}

fun getAuto(startingPose: StartingPose, objective1: Objective, objective2: Objective): Pair<String, suspend () -> Any?> =
        when (startingPose) {
            StartingPose.NO_AUTO -> "I find your lack of faith in our auto disturbing." to suspend {}
            StartingPose.DRIVE_FORWARD -> "" to suspend {
            }
            StartingPose.RIGHT_TRENCH ->
                if (objective1 == Objective.TRENCH && objective2 == Objective.POWER_PORT) {
                    "May the auto be with you." to suspend {
                        auto(PathGenerator.startlocation.RIGHT_TRENCH, listOf(
                                PathGenerator.tasks.BALLS2,
                                PathGenerator.tasks.TRENCH_TO_CORNER,
                                PathGenerator.tasks.UNLOAD_FROM_CORNER
                        ))
                    }
                } else {
                    "ERR: Millennium Carwash, you know better than to trust a strange auto." to suspend {}
                }
            StartingPose.RIGHT_ALLIANCE ->
                if (objective1 == Objective.POWER_PORT && objective2 == Objective.TRENCH) {
                    "May the auto be with you." to suspend {
                        auto(PathGenerator.startlocation.RIGHT_ALLIANCE, listOf(
                                PathGenerator.tasks.UNLOAD_FROM_POWERPORT,
                                PathGenerator.tasks.CORNER_TO_TRENCH
                        ))
                    }
                } else if (objective1 == Objective.POWER_PORT && objective2 == Objective.LOADING_STATION) {
                    "There's no mystical code that controls my auto." to suspend {
                        auto(PathGenerator.startlocation.RIGHT_ALLIANCE, listOf(
                                PathGenerator.tasks.UNLOAD_FROM_POWERPORT,
                                PathGenerator.tasks.AWAY_FROM_POWERPORT
                        ))
                    }
                } else {
                    "ERR: The auto you seek is not behind you... it is ahead." to suspend {}
                }
            StartingPose.CENTER -> if (objective1 == Objective.POWER_PORT && objective2 == Objective.TRENCH) {
                    "Try spinning, that's a good trick!" to suspend {
                        auto(PathGenerator.startlocation.CENTER, listOf(
                                PathGenerator.tasks.UNLOAD_FROM_POWERPORT,
                                PathGenerator.tasks.CORNER_TO_TRENCH
                        ))
                    }
                } else if (objective1 == Objective.POWER_PORT && objective2 == Objective.LOADING_STATION) {
                    "There's no mystical code that controls my auto." to suspend {
                        auto(PathGenerator.startlocation.CENTER, listOf(
                                PathGenerator.tasks.UNLOAD_FROM_POWERPORT,
                                PathGenerator.tasks.AWAY_FROM_POWERPORT
                        ))
                    }
                } else {
                    "ERR: The auto you seek is not behind you... it is ahead." to suspend {}
                }
        }

fun CoroutineScope.handleAutoChooser() {
    ({ startingPose.selected }).watch {
        onChange {
            auto = getAuto(it.value, objective1.selected, objective2.selected)
            autoMessage.value = auto.first
        }
    }
    ({ objective1.selected }).watch {
        onChange {
            auto = getAuto(startingPose.selected, value, objective2.selected)
            autoMessage.value = auto.first
        }
    }
    ({ objective2.selected }).watch {
        onChange {
            auto = getAuto(startingPose.selected, objective1.selected, value)
            autoMessage.value = auto.first
        }
    }
}
