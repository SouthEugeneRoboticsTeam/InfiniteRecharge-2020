package org.sert2521.infiniterecharge2020.powerhub

import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun spin(isOuttaking: Boolean) = doTask {
    val powerHub = use<PowerHub>()
    action {
        onTick {
            try {
                periodic(20) {
                    if (isOuttaking) {
                        if (!powerHub.flapOpen) {
                            println("Opening Flapper")
                            powerHub.openFlapper()
                        } else {
                            println("Stopping open")
                            powerHub.stopFlapper()
                        }
                    } else {
                        if (!powerHub.flapClosed) {
                            println("Closing Flapper")
                            powerHub.closeFlapper()
                        } else {
                            println("Stopping close")
                            powerHub.stopFlapper()
                        }
                    }
                    powerHub.spin()
                }
            } finally {
                println("STOPPING EVERYTHING")
                powerHub.stopFlapper()
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



