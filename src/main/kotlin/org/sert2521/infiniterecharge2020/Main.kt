package org.sert2521.infiniterecharge2020

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.utils.withTableEntry
import org.sert2521.sertain.control.PidfConfig
import org.sert2521.sertain.control.PidfController
import org.sert2521.sertain.coroutines.RobotScope
import org.sert2521.sertain.events.onTick
import org.sert2521.sertain.events.whileAuto
import org.sert2521.sertain.robot
import org.sert2521.sertain.subsystems.add
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import org.sert2521.sertain.telemetry.TableEntry
import org.sert2521.sertain.telemetry.tableEntry

suspend fun main() = robot {
    println("Robot program starting")

    add<Drivetrain>()

    val distance by tableEntry(1000, "Drivetrain")
    distance

    var error by tableEntry(0, "Drivetrain")
    error

    val kp = TableEntry("KP", 0.0, listOf("Drivetrain"))
    val ki = TableEntry("KI", 0.0, listOf("Drivetrain"))
    val kd = TableEntry("KD", 0.0, listOf("Drivetrain"))
    val kf = TableEntry("KF", 0.0, listOf("Drivetrain"))

    whileAuto {
        val pidf = PidfController(PidfConfig().apply {
            RobotScope.withTableEntry(kp) { this.kp = kp.value }
            RobotScope.withTableEntry(kp) { this.ki = ki.value }
            RobotScope.withTableEntry(kp) { this.kd = kd.value }
            RobotScope.withTableEntry(kp) { this.kf = kf.value }
        }, 20.0)
        doTask {
            val dt = use<Drivetrain>()
            action {
                onTick {
                    val output = pidf.next(distance.toDouble(), (dt.leftSpeed + dt.rightSpeed) / 2.0)
                    dt.tankDrive(output, output)
                    error = distance - ((dt.leftSpeed + dt.rightSpeed) / 2)
                }
            }
        }
    }

    launch {
        delay(100)
        val sendy = SendableChooser<Int>().apply {
            addOption("lol", 0)
            addOption("lmao", 1)
        }
        SmartDashboard.putData(sendy)
        delay(10000)
    }
}
