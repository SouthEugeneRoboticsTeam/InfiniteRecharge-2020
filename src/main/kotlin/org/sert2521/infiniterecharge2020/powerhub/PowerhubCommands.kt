package org.sert2521.infiniterecharge2020.powerhub

import org.sert2521.sertain.coroutines.doAll
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun spin(intake: Boolean) = doTask {
    val powerHub = use<PowerHub>()
    action {
        doAll {
            onTick {
                periodic(20) {
                    powerHub.spin()
                    if (intake) {
                        println("Trying to intake")
                        powerHub.lowerFlapper()

                    } else {
                        println("Trying to outtake")
                        powerHub.raiseFlapper()
                    }
                }
            }
        }
    }
}
