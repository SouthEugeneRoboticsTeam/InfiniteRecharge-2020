package org.sert2521.infiniterecharge2020.vision

import org.sert2521.sertain.subsystems.Subsystem
import org.sert2521.sertain.telemetry.TableEntry

class Vision : Subsystem("Vision") {
    val offsetAngle = TableEntry("xAngOff", 0.0, "settings")
}
