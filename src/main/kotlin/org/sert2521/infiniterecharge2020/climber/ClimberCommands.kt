package org.sert2521.infiniterecharge2020.climber

import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun ClimberUp() = doTask {
    val run = use<Climber>()
    action {
        try {
            periodic(20) {
                run.ClimberLiftUp()
                println("Climber lift going up")
            }
        } finally {
            println("Climber lift stop")
            run.StopClimberLift()
        }
    }
}

suspend fun ClimberDown() = doTask {
    val run = use<Climber>()
    action {
        try {
            periodic(20) {
                run.ClimberLiftDown()
                println("Climber lift going down")
            }
        } finally {
            println("Climber lift stop")
            run.StopClimberLift()
        }
    }
}

suspend fun ClimberWinchGo() = doTask {
    val run = use<Climber>()
    action {
        try {
            periodic(20) {
                run.ClimberWinchGo()
                println("Climber Winch going")
            }
        } finally {
            println("Climber Winch stop")
            run.StopClimberWinchGo()
        }
    }
}

