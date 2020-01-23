package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.OI.controlModeChooser
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.add

suspend fun main() = robot {
    add<Drivetrain>()

    launch {
        delay(1000)
        SmartDashboard.putData("Control Mode", controlModeChooser);
    }
}
