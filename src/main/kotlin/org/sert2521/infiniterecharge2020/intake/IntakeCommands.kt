package org.sert2521.infiniterecharge2020.intake

import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun intake() = doTask {
    val intake = use<Intake>()
    action {
        try {
            periodic(20) {
                intake.spinIntake()
                println("Spinning intake")
            }
        } finally {
            println("Intake should stop")
            intake.stopSpin()
        }
    }
}

suspend fun outtake() = doTask {
    val intake = use<Intake>()
    action {
        try {
            periodic(20) {
                intake.spinReverse()
                println("Spinning intake")
            }
        } finally {
            println("Intake should stop")
            intake.stopSpin()
        }
    }
}

suspend fun flapperup() = doTask {
    val intake = use<Intake>()
    action {
        try {
            periodic(20) {
                intake.spinIntake()
                println("Spinning flapper up")
            }
        } finally {
            println("Flapper should stop")
            intake.stopSpin()
        }
    }
}

suspend fun flapperdown() = doTask {
    val intake = use<Intake>()
    action {
        try {
            periodic(20) {
                intake.spinReverse()
                println("Spinning flapper down")
            }
        } finally {
            println("Flapper should stop")
            intake.stopSpin()
        }
    }
}
