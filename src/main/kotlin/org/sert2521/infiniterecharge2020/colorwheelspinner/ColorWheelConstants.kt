package org.sert2521.infiniterecharge2020.colorwheelspinner

import edu.wpi.first.wpilibj.util.Color

// Accounts for the difference in position of the field sensor and the robot's sensor. When we sense red, field senses blue, etc.
val frcColorToTargetColor = mapOf('Y' to Color.kGreen, 'B' to Color.kRed, 'G' to Color.kYellow, 'R' to Color.kBlue)
// Maps the color sent by the FMS to the full word
val frcColorToString = mapOf('Y' to "Yellow", 'B' to "Blue", 'G' to "Green", 'R' to "Red")
// Maps the color outputted by the color matcher to the full word
val sensorColorToString = mapOf(Color.kYellow to "Yellow", Color.kBlue to "Blue", Color.kGreen to "Green", Color.kRed to "Red")

// How many color changes to stop spinning the wheel. 32 = 4 full revolutions of the wheel
const val NUM_TRIANGLES = 32

// The speed we run the motor to raise/lower the spinner
const val EXTEND_SPEED = 0.5
// The speed we run the motor to spin the wheel
const val SPIN_SPEED = 0.16
// How high the color wheel can go before slow mode is engaged
const val POSITION_TO_SLOW = 100
// Approximate position of spinner when it's fully extended
const val SPINNER_AT_TOP = 4500

// For tuning until linktablentries are fixed
// SmartDashboard.putNumber("Red", colorsensor.sensor.color.red)
// SmartDashboard.putNumber("Green", colorsensor.sensor.color.green)
// SmartDashboard.putNumber("Blue", colorsensor.sensor.color.blue)
// SmartDashboard.putString("Current Color ", colorsensor.currentColor)
// SmartDashboard.putNumber("Extender position", colorsensor.spinnerPosition.toDouble())
