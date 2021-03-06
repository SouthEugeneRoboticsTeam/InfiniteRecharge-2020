package org.sert2521.infiniterecharge2020.drivetrain

import edu.wpi.first.wpilibj.Preferences
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import org.sert2521.sertain.motors.Encoder
import org.sert2521.sertain.telemetry.TableEntry
import org.sert2521.sertain.units.m

enum class RobotType {
    PRACTICE, COMP
}

data class BotPid(
    val kp: Double,
    val ki: Double,
    val kd: Double,
    val kfLeft: Double,
    val kfRight: Double
)

val driveSpeedScalar get() = Preferences.getInstance().getDouble("drive_speed_scalar", 1.0)

// Number of encoder pulses for one revolution of the wheel
const val TICKS_PER_REVOLUTION = 4096

val motorEncoder = Encoder(TICKS_PER_REVOLUTION)

val slowMode get() = TableEntry("SlowMode", false)
val autoAlignSpeed = 0.5

val wheelRadius = 0.09525.m

val kinematics = DifferentialDriveKinematics(0.575)

val practiceBotChooser = SendableChooser<RobotType>().apply {
    setDefaultOption("Comp", RobotType.COMP)
    addOption("Practice", RobotType.PRACTICE)
}

val isPracticeBot = practiceBotChooser.selected

val practiceBotPid = BotPid(
        kp = 2.0,
        ki = 0.005,
        kd = 0.00001,
        kfLeft = .265,
        kfRight = .25
)

val compBotPid = BotPid(
        kp = 1.8,
        // Both these have not been played around with
        ki = 0.005,
        kd = 0.00001,
        // These have not been tuned at all
        kfLeft = .265,
        kfRight = .25
)
