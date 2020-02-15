package org.sert2521.infiniterecharge2020.climber

import org.sert2521.infiniterecharge2020.OI
import org.sert2521.infiniterecharge2020.utils.deadband
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import kotlin.math.abs

val winchSpeed get() = OI.secondaryJoystick.y.deadband(0.02)

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

suspend fun runWinch() = doTask {
    val climber = use<Climber>()
    action {
        println("Winch Speed: ${winchSpeed}")
        onTick {
            climber.runWinchClockwise(abs(winchSpeed))
        }
    }
}
