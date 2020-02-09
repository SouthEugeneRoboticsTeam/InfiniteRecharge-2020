package org.sert2521.infiniterecharge2020

import org.sert2521.infiniterecharge2020.climber.Climber
import org.sert2521.sertain.events.whileTeleop
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.add

suspend fun main() = robot {
    add<Climber>()

    whileTeleop {

    }
}


