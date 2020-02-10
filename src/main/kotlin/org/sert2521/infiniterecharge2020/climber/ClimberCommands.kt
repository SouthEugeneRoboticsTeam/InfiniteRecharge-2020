package org.sert2521.infiniterecharge2020.climber

import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun climberUp() = doTask {
    val climber = use<Climber>()
    action {
        try {
            periodic(20) {
                if (!climber.atTop) {
                    climber.climberLiftUp()
                } else {
                    climber.climberStop()
                }
            }
        } finally {
            climber.climberStop()
        }
    }
}


suspend fun climberDown() = doTask {
    val climber = use<Climber>()
    action {
        try {
            periodic(20) {
                if (!climber.atBottom) {
                    climber.climberLiftDown()
                } else {
                    climber.climberStop()
                }
            }
        } finally {
            climber.climberStop()
        }
    }
}



