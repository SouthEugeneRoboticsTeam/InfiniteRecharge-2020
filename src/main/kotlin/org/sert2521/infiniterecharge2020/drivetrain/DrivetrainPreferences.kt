package org.sert2521.infiniterecharge2020.drivetrain

import edu.wpi.first.wpilibj.Preferences
import org.sert2521.sertain.motors.Encoder
import org.sert2521.sertain.units.m

val driveSpeedScalar get() = Preferences.getInstance().getDouble("drive_speed_scalar", 1.0)

const val KP = 0.2
const val KD = 0.1
const val KF = 0.0

// Number of encoder pulses for one revolution of the wheel
const val TICKS_PER_REVOLUTION = 4096

val encoder = Encoder(TICKS_PER_REVOLUTION)

// TODO measure wheels
val wheelRadius = 0.098425.m
