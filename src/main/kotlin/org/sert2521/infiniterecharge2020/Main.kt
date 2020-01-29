package org.sert2521.infiniterecharge2020

import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.sertain.events.onTeleop
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.events.whileAuto
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.access
import org.sert2521.sertain.subsystems.add
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import org.sert2521.sertain.telemetry.tableEntry

suspend fun main() = robot {
    println("Robot program starting")

    add<Drivetrain>()

    val distance by tableEntry(1000, "Drivetrain")

    var error by tableEntry(0, "Drivetrain")
    error

    whileAuto {
        doTask {
            val dt = use<Drivetrain>()
            action {
                onTick {
                    println("Trying to set the target speed")
                    dt.setTargetSpeed(1000, 1000)
                }
            }
        }
    }

    onTeleop {
        val dt = access<Drivetrain>()
        dt.zeroEncoders()
    }
}