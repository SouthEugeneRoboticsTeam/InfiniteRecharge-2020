package org.sert2521.infiniterecharge2020.powerhouse

import kotlinx.coroutines.cancel
import org.sert2521.infiniterecharge2020.powerHouse
import org.sert2521.sertain.coroutines.delayUntil
import org.sert2521.sertain.events.onTick

// Welcomes a ball into the house
suspend fun welcome() = powerHouse { powerHouse ->
    try {
        onTick {
            powerHouse.spin()
        }.join()
    } finally {
        powerHouse.stopSpin()
    }
}

// Banishes the ball from the house for all eternity
suspend fun banish() = powerHouse { powerHouse ->
    openHouse()
    try {
        onTick {
            powerHouse.spin()
        }.join()
    } finally {
        powerHouse.stopSpin()
    }
}

suspend fun openHouse() = powerHouse { powerHouse ->
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

suspend fun closeHouse() = powerHouse { powerHouse ->
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

suspend fun reverseWelcome() = powerHouse { powerHouse ->
    try {
        onTick {
            powerHouse.spinReverse()
        }.join()
    } finally {
        powerHouse.stopSpin()
    }
}
