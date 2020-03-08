package org.sert2521.infiniterecharge2020.powerhouse

import kotlinx.coroutines.cancel
import org.sert2521.infiniterecharge2020.powerHouse
import org.sert2521.sertain.coroutines.delayUntil
import org.sert2521.sertain.coroutines.periodic

// Welcomes a ball into the house
suspend fun welcome() = powerHouse { powerHouse ->
    try {
        periodic(20) {
            powerHouse.spin()
        }
    } finally {
        powerHouse.stopSpin()
    }
}

// Banishes the ball from the house for all eternity
suspend fun banish() = powerHouse { powerHouse ->
    openHouse()
    try {
        periodic(20) {
            powerHouse.spin()
        }
    } finally {
        powerHouse.stopSpin()
    }
}

suspend fun openHouse() = powerHouse { powerHouse ->
    try {
        periodic(20) {
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
        periodic(20) {
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
        periodic(20) {
            powerHouse.spinReverse()
        }
    } finally {
        powerHouse.stopSpin()
    }
}
