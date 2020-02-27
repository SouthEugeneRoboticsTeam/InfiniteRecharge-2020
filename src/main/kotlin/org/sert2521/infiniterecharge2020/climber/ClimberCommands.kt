package org.sert2521.infiniterecharge2020.climber

import kotlin.math.abs
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun climberUp() = doTask {
    val climber = use<Climber>()
    action {
        try {
            onTick {
                if (!climber.atTop && climber.position < POSITION_AT_TOP) {
                    climber.climberLiftUp()
                } else {
                    climber.climberStop()
                }
            }.join()
        } finally {
            climber.climberStop()
        }
    }
}

suspend fun climberDown() = doTask {
    val climber = use<Climber>()
    action {
        try {
            onTick {
                if (!climber.atBottom && climber.position > POSITION_AT_BOTTOM) {
                    climber.climberLiftDown()
                } else {
                    climber.climberStop()
                }
            }.join()
        } finally {
            climber.climberStop()
        }
    }
}

suspend fun runWinch(output: () -> Double) = doTask {
    val climber = use<Climber>()
    action {
        try {
            onTick {
                climber.runWinch(abs(output()))
            }.join()
        } finally {
            climber.stopWinch()
        }
    }
}

suspend fun reverseRunWinch() = doTask {
    val climber = use<Climber>()
    action {
        try {
            periodic(20) {
                climber.runWinch(WINCH_UNWIND_SPEED)
            }
        } finally {
            climber.stopWinch()
        }
    }
}
