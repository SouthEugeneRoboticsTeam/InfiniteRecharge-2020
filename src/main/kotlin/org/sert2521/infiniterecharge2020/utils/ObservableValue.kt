package org.sert2521.infiniterecharge2020.utils

import kotlinx.coroutines.CoroutineScope
import org.sert2521.sertain.coroutines.Observable
import org.sert2521.sertain.coroutines.ObservableValue
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.coroutines.watch
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.telemetry.TableEntry

fun <T> RobotScope.withTableEntry(name: String, value: T, vararg location: String, action: (T) -> Unit) =
        withTableEntry(TableEntry(name, value, location.toList()), action)

fun <T> RobotScope.withTableEntry(entry: TableEntry<T>, action: (T) -> Unit): ObservableValue<T> {
    action(entry.value)
    onTick {
        println(entry.value)
    }
    return ({ entry.value }).watch {
        onChange {
            action(value)
        }
    }
}
