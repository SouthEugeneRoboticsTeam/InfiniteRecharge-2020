package org.sert2521.infiniterecharge2020.utils

import kotlinx.coroutines.CoroutineScope
import org.sert2521.sertain.control.PidfConfig
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.telemetry.Table
import org.sert2521.sertain.telemetry.TableEntry
import kotlin.math.sign

class PidfController2(config: PidfConfig, val dt: Double) {
    private val kp = config.kp ?: 0.0
    private val ki = config.ki ?: 0.0
    private val kd = config.kd ?: 0.0
    private val kf = config.kf ?: 0.0

    private var integral = 0.0
    private var lastError = 0.0

    fun next(setPoint: Double, actual: Double): Double {
        val error = setPoint - actual
        integral += error * dt
        val derivative = (error - lastError) / dt
        lastError = error
        return (kp * error) + (ki * integral) + (kd * derivative) + (kf * sign(error))
    }
}

fun <T> CoroutineScope.linkTableEntry(name: String, parent: Table, get: () -> T) = run {
    val entry = TableEntry(name, get(), parent)
    onTick {
        entry.value = get()
    }
}

fun <T> CoroutineScope.linkTableEntry(name: String, vararg location: String, get: () -> T) = run {
    val entry = TableEntry(name, get(), *location)
    onTick {
        entry.value = get()
    }
}
