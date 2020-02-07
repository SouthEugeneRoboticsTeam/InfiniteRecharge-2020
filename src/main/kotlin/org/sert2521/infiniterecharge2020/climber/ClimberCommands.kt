package org.sert2521.infiniterecharge2020.climber

import edu.wpi.first.wpilibj.DigitalInput
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun climberUp() = doTask {
    val climber = use<Climber>()
    action {
        onTick {
            if (!DigitalInput(0).get()) {
                climber.climberLiftUp()
            }
        }
    }
}

suspend fun climberDown() = doTask {
    val climber = use<Climber>()
    action {
        onTick {
            if (!DigitalInput(1).get()) {
                climber.climberLiftDown()
            }
        }
    }
}


