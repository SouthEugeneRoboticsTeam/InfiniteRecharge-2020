package org.sert2521.infiniterecharge2020.powerhouse

import kotlinx.coroutines.cancel
import org.sert2521.sertain.coroutines.delayUntil
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

// Welcomes a ball into the house
suspend fun welcome() = doTask {
    val powerHouse = use<PowerHouse>()
    action {
        try {
            onTick {
                powerHouse.intake()
            }.join()
        } finally {
            powerHouse.stopSpin()
        }
    }
}

// Banishes the ball from the house for all eternity
suspend fun banish() = doTask {
    val powerHouse = use<PowerHouse>()
    action {
        openHouse()
        try {
            onTick {
                powerHouse.intake()
            }.join()
        } finally {
            powerHouse.stopSpin()
        }
    }
}

suspend fun openHouse() = doTask {
    val powerHouse = use<PowerHouse>()
    action {
        try {
            onTick {
                powerHouse.openFlap()
            }
            delayUntil { powerHouse.flapOpen }
            cancel()
        } finally {
            powerHouse.stopFlap()
        }
    }
}

suspend fun closeHouse() = doTask {
    val powerHouse = use<PowerHouse>()
    action {
        try {
            onTick {
                powerHouse.closeFlap()
            }
            delayUntil { powerHouse.flapClosed }
            cancel()
        } finally {
            powerHouse.stopFlap()
        }
    }
}

suspend fun reverseWelcome() = doTask {
    val powerHouse = use<PowerHouse>()
    action {
        onTick {
            powerHouse.spinReverse()
        }
    }
}
