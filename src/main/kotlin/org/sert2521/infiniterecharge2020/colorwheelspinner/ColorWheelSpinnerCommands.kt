package org.sert2521.infiniterecharge2020.colorwheelspinner

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.util.Color
import kotlinx.coroutines.cancel
import org.sert2521.sertain.coroutines.periodic
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use

suspend fun extend() = doTask {
    val colorWheelSpinner = use<ColorWheelSpinner>()
    action {
        try {
            periodic(20) {
                    println("Attempting to go up")
                    colorWheelSpinner.spin(EXTEND_SPEED)
            }
        } finally {
            colorWheelSpinner.stop()
        }
    }
}

suspend fun retract() = doTask {
    val colorWheelSpinner = use<ColorWheelSpinner>()
    val startTime = System.currentTimeMillis()
    action {
        try {
            periodic(20) {
                if (!colorWheelSpinner.spinnerAtBottom) {
                    var currentTime = System.currentTimeMillis()
                    if (currentTime - startTime > 500) {
                        colorWheelSpinner.spin(-EXTEND_SPEED)
                    } else {
                        colorWheelSpinner.spin(-EXTEND_SPEED / 2)
                    }
                } else {
                    colorWheelSpinner.stop()
                }
            }
        } finally {
            colorWheelSpinner.stop()
        }
    }
}

suspend fun spinToColor() = doTask {
    val colorWheelSpinner = use<ColorWheelSpinner>()
    // Get color to spin to from the FMS
    val frcColor: Char = DriverStation.getInstance().gameSpecificMessage.first() ?: 'Z'
    if (frcColor in ((listOf('R', 'G', 'B', 'Y')))) {
        println("Attempting Position Control to color: $frcColor")
        val targetColor = frcColorToTargetColor[frcColor] ?: return@doTask

        action {
            try {
                periodic(20) {
                    val color = colorWheelSpinner.currentColor
                    if (color == targetColor) {
                        this@action.cancel()
                    }
                    colorWheelSpinner.spin(SPIN_SPEED)
                }
            } finally {
                colorWheelSpinner.stop()
            }
        }
    }
}

suspend fun spinForColors() = doTask {
    val colorWheelSpinner = use<ColorWheelSpinner>()
    action {
        println("Starting")
        var triangleSpins = 0
        var pastColor: Color = Color.kWhite
        try {
            periodic(20) {
                val sensorColor = colorWheelSpinner.currentColor
                if (sensorColor != pastColor) {
                    pastColor = sensorColor
                    triangleSpins++
                }
                if (triangleSpins >= NUM_TRIANGLES) {
                    this@action.cancel()
                }
                pastColor = sensorColor
                println("Color : ${sensorColorToString[sensorColor]}")
                println("Num colors counted : $triangleSpins")
                colorWheelSpinner.spin(SPIN_SPEED)
            }
        } finally {
            colorWheelSpinner.stop()
        }
    }
}

// fun getColor2(sensorOutput: Color): Color {
//    // Needs more tuning
//    val distanceToRed = abs(sensorOutput.blue - 0.13) + abs(sensorOutput.green - 0.34) + abs(sensorOutput.red - 0.52)
//    val distanceToBlue = abs(sensorOutput.blue - 0.45) + abs(sensorOutput.green - 0.42) + abs(sensorOutput.red - 0.11)
//    val distanceToGreen = abs(sensorOutput.blue - 0.25) + abs(sensorOutput.green - 0.58) + abs(sensorOutput.red - 0.16)
//    val distanceToYellow = abs(sensorOutput.blue - 0.11) + abs(sensorOutput.green - 0.56) + abs(sensorOutput.red - 0.31)
//
//    return when {
//        distanceToRed < distanceToBlue && distanceToRed < distanceToGreen && distanceToRed < distanceToYellow ->
//            Color.kRed
//        distanceToBlue < distanceToGreen && distanceToBlue < distanceToYellow ->
//            Color.kBlue
//        distanceToGreen < distanceToYellow ->
//            Color.kGreen
//        else ->
//            Color.kYellow
//    }
// }
