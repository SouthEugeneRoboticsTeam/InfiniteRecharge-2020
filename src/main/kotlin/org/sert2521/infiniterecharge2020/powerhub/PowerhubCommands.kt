package org.sert2521.infiniterecharge2020.powerhub

import kotlinx.coroutines.cancel
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun intake() = doTask {
    val powerHub = use<PowerHub>()
    action {
        onTick {
            powerHub.spin()
        }
    }
}

suspend fun raiseflapper() = doTask {
    val powerhub = use<PowerHub>()
    val time = System.currentTimeMillis()
    action {
        onTick {
            powerhub.raiseFlapper()
            if (System.currentTimeMillis() - time <= 1000) {
                this@action.cancel()
            }
        }
    }
}

suspend fun lowerflapper() = doTask {
    val powerhub = use<PowerHub>()
    val time = System.currentTimeMillis()
    action {
        onTick {
            powerhub.lowerFlapper()
            if (System.currentTimeMillis() - time <= 1000) {
                this@action.cancel()
            }
        }
    }
}




