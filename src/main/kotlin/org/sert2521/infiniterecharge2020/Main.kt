package org.sert2521.infiniterecharge2020

import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.driveCurve
import org.sert2521.infiniterecharge2020.drivetrain.mpss
import org.sert2521.infiniterecharge2020.drivetrain.mpsss
import org.sert2521.sertain.events.onTeleop
import org.sert2521.sertain.events.whileAuto
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.access
import org.sert2521.sertain.subsystems.add
import org.sert2521.sertain.telemetry.tableEntry
import org.sert2521.sertain.units.m
import org.sert2521.sertain.units.mps

suspend fun main() = robot {
    println("Robot program starting")

    add<Drivetrain>()

    val d by tableEntry(0.0, "Drivetrain", name = "TargetDistance")
    val v by tableEntry(0.0, "Drivetrain", name = "MaxVelocity")
    val a by tableEntry(0.0, "Drivetrain", name = "MaxAcceleration")
    val j by tableEntry(0.0, "Drivetrain", name = "MaxJerk")
    d
    v
    a
    j

    whileAuto {
        driveCurve(j.mpsss, a.mpss, v.mps, d.m)
    }

    onTeleop {
        val dt = access<Drivetrain>()
        dt.zeroEncoders()
    }
}
