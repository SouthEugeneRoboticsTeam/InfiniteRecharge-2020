package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.oi.controlModeChooser
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.add

suspend fun main() = robot {
    add<Drivetrain>()

    launch {
        SmartDashboard.putData(controlModeChooser)
    }
}
