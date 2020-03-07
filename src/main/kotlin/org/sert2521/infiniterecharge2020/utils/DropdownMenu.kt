package org.sert2521.infiniterecharge2020.utils

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

data class Option<T>(val name: String, val value: T)

infix fun <T> String.gives(value: T) = Option(this, value)

class DropdownMenuConfig<T> {
    var options = mutableMapOf<String, T>()

    infix fun String.gives(value: T) {
        options[this] = value
    }
}

fun <T> dropdown(name: String, default: Option<T>, configure: DropdownMenuConfig<T>.() -> Unit): SendableChooser<T> {
    val config = DropdownMenuConfig<T>().apply(configure)
    return SendableChooser<T>().apply {
        config.options.forEach {
            addOption(it.key, it.value)
        }
        setDefaultOption(default.name, default.value)
        SmartDashboard.putData(name, this)
    }
}
