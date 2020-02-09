package org.sert2521.infiniterecharge2020.autonomous

import edu.wpi.first.wpilibj.controller.RamseteController
import edu.wpi.first.wpilibj.geometry.Pose2d
import edu.wpi.first.wpilibj.geometry.Rotation2d
import org.sert2521.infiniterecharge2020.drivetrain.Drivetrain
import org.sert2521.infiniterecharge2020.drivetrain.kinematics
import org.sert2521.infiniterecharge2020.drivetrain.runPath
import org.sert2521.sertain.subsystems.doTask
import org.sert2521.sertain.subsystems.use
import org.sert2521.sertain.units.mps

suspend fun rightInitPowerPort(pushBack: Boolean, endLocation: PathGenerator.endLocation) = doTask {
    val drivetrain = use<Drivetrain>()
    val pathGenerator = use<PathGenerator>()
    drivetrain.odometry.resetPosition(Pose2d(-2.2, -1.22, Rotation2d(0.0)), Rotation2d(0.0))
    action {
        if (pushBack) {
            val pushBackPath = pathGenerator.pushBack(0.30)
            runPath(pushBackPath, { drivetrain.pose }, RamseteController(), kinematics, { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
        }
        val toPowerPortPath = pathGenerator.initToPowerPort()
        println(toPowerPortPath.states)
        runPath(toPowerPortPath, { drivetrain.pose }, RamseteController(), kinematics, { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
        if (endLocation == PathGenerator.endLocation.TRENCH) {
            val toTrenchFromPortPath = pathGenerator.powerPortToTrench()
            println(toTrenchFromPortPath.states)
            runPath(toTrenchFromPortPath, { drivetrain.pose }, RamseteController(), kinematics, { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
            val trenchRunPath = pathGenerator.trenchRun(5.0)
            println(trenchRunPath.states)
            runPath(trenchRunPath, { drivetrain.pose }, RamseteController(), kinematics, { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
        } else if (endLocation == PathGenerator.endLocation.LOADING_STATION) {
            val loadingStationPath = pathGenerator.loadingStation()
            runPath(loadingStationPath, { drivetrain.pose }, RamseteController(), kinematics, { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
        }
    }
}

suspend fun centerInitPowerPort(pushBack: Boolean, endLocation: PathGenerator.endLocation) = doTask {
    val drivetrain = use<Drivetrain>()
    val pathGenerator = use<PathGenerator>()
    drivetrain.odometry.resetPosition(Pose2d(-2.143, 0.0, Rotation2d(0.0)), Rotation2d(0.0))
    action {
        if (pushBack) {
            val pushBackPath = pathGenerator.pushBack(0.30)
            runPath(pushBackPath, { drivetrain.pose }, RamseteController(), kinematics, { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
        }
        val toPowerPortPath = pathGenerator.initToPowerPort()
        println(toPowerPortPath.states)
        runPath(toPowerPortPath, { drivetrain.pose }, RamseteController(), kinematics, { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
        if (endLocation == PathGenerator.endLocation.TRENCH) {
            val toTrenchFromPortPath = pathGenerator.powerPortToTrench()
            println(toTrenchFromPortPath.states)
            runPath(toTrenchFromPortPath, { drivetrain.pose }, RamseteController(), kinematics, { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
            val trenchRunPath = pathGenerator.trenchRun(5.0)
            println(trenchRunPath.states)
            runPath(trenchRunPath, { drivetrain.pose }, RamseteController(), kinematics, { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
        } else if (endLocation == PathGenerator.endLocation.LOADING_STATION) {
            val loadingStationPath = pathGenerator.loadingStation()
            runPath(loadingStationPath, { drivetrain.pose }, RamseteController(), kinematics, { l, r -> drivetrain.setTargetSpeed(l.mps, r.mps) })
        }
    }
}
