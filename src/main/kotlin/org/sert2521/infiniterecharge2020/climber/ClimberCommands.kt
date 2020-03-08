package org.sert2521.infiniterecharge2020.climber

import kotlin.math.abs
import org.sert2521.infiniterecharge2020.OI
import org.sert2521.infiniterecharge2020.climber
import org.sert2521.sertain.coroutines.periodic

val winchSpeed get() = OI.secondaryJoystick

suspend fun climberUp() = climber { climber ->
    try {
        periodic(20) {
            if (!climber.atTop && climber.position < POSITION_AT_TOP) {
                climber.climberLiftUp()
            } else {
                climber.climberStop()
            }
        }
    } finally {
        climber.climberStop()
    }
}

suspend fun climberDown() = climber { climber ->
    try {
        periodic(20) {
            if (!climber.atBottom && climber.position > POSITION_AT_BOTTOM) {
                climber.climberLiftDown()
            } else {
                climber.climberStop()
            }
        }
    } finally {
        climber.climberStop()
    }
}

suspend fun runWinch(output: () -> Double) = climber { climber ->
    try {
        periodic(20) {
            climber.runWinch(abs(output()))
        }
    } finally {
        climber.stopWinch()
    }
}

suspend fun reverseRunWinch() = climber { climber ->
    try {
        periodic(20) {
            climber.runWinch(WINCH_UNWIND_SPEED)
        }
    } finally {
        climber.stopWinch()
    }
}
