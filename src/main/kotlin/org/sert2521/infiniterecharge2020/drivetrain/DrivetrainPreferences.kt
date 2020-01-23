package org.sert2521.infiniterecharge2020.drivetrain

import edu.wpi.first.wpilibj.Preferences
import org.sert2521.sertain.units.m

val driveSpeedScalar get() = Preferences.getInstance().getDouble("drive_speed_scalar", 1.0)

// Number of encoder pulses for one revolution of the wheel
// TODO: Tune on carpet
const val PULSES_PER_REVOLUTION = 7442.0
const val WHEEL_RADIUS = 3.0
val wheelRadius = 0.0254.m
