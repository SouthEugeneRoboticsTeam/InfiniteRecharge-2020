package org.sert2521.infiniterecharge2020.climber

import kotlin.math.abs
import org.sert2521.infiniterecharge2020.OI
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

val winchSpeed get() = OI.secondaryJoystick

suspend fun climberUp() = doTask {
    val climber = use<Climber>()
    action {
        try {
            onTick {
                if (!climber.atTop && climber.position < POSITION_AT_TOP) {
                    climber.setOutput(CLIMBER_LIFT_SPEED)
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
        onTick {
            climber.runWinch(abs(output()))
        }
    }
}

suspend fun reverseRunWinch(output: () -> Double) = doTask {
    val climber = use<Climber>()
    action {
        onTick {
            climber.runWinch(-abs(output()))
        }
    }
}
