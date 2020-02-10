package org.sert2521.infiniterecharge2020.powerhub

import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun spin(isOuttaking: Boolean) = doTask {
    val powerHub = use<PowerHub>()
    action {
        onTick {
            try {
                if (isOuttaking) {
                    if (!powerHub.flapOpen) {
                        powerHub.openFlapper()
                    }
                } else {
                    if (!powerHub.flapClosed) {
                        powerHub.closeFlapper()
                    }
                }
                powerHub.spin()
            } finally {
                powerHub.stopSpin()
            }
        }
    }
}

suspend fun reverseIntake() = doTask {
    val powerHub: PowerHub = use<PowerHub>()
    action {
        onTick {
            powerHub.spinReverse()
        }
    }
}


//suspend fun raiseflapper() = doTask {
//    val powerhub = use<PowerHub>()
//    val time = System.currentTimeMillis()
//    action {
//        onTick {
//            powerhub.closeFlapper()
//            if (System.currentTimeMillis() - time <= 1000) {
//                this@action.cancel()
//            }
//        }
//    }
//}
//
//suspend fun lowerflapper() = doTask {
//    val powerhub = use<PowerHub>()
//    val time = System.currentTimeMillis()
//    action {
//        onTick {
//            powerhub.openFlapper()
//            if (System.currentTimeMillis() - time <= 1000) {
//                this@action.cancel()
//            }
//        }
//    }
//}
//



