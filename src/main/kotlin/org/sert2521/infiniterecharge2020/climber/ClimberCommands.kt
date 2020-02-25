package org.sert2521.infiniterecharge2020.climber

import kotlin.math.abs
import org.sert2521.infiniterecharge2020.OI
import org.sert2521.infiniterecharge2020.climber
import org.sert2521.sertain.events.onTick

val winchSpeed get() = OI.secondaryJoystick

suspend fun climberUp() = climber { climber ->
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

suspend fun climberDown() = climber { climber ->
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

suspend fun runWinch(output: () -> Double) = climber { climber ->
    try {
        onTick {
            climber.runWinch(abs(output()))
        }.join()
    } finally {
        climber.stopWinch()
    }
}

suspend fun reverseRunWinch(output: () -> Double) = climber { climber ->
    onTick {
        climber.runWinch(-abs(output()))
    }
}
