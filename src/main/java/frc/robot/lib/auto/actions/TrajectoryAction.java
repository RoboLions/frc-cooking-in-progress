// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.lib.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.RobotMap;
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.Trajectory;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

/**
 * An action that uses two PID controllers ({@link PIDController}) and a ProfiledPIDController
 * ({@link ProfiledPIDController}) to follow a trajectory {@link Trajectory} with a swerve drive.
 *
 * <p>This action outputs the raw desired Swerve Module States ({@link SwerveModuleState}) in an
 * array. The desired wheel and module rotation velocities should be taken from those and used in
 * velocity PIDs.
 *
 * <p>The robot angle controller does not follow the angle given by the trajectory but rather goes
 * to the angle given in the final state of the trajectory.
 */
public class TrajectoryAction implements Action {
    private final Timer m_timer = new Timer();
    private final PathPlannerTrajectory m_trajectory;
    private final Supplier<Pose2d> m_pose;
    // private final Supplier<Rotation2d> m_desired_rotation;
    private final SwerveDriveKinematics m_kinematics;
    //private final HolonomicDriveController m_controller;
    private final PPHolonomicDriveController m_controller;
    private final Consumer<SwerveModuleState[]> m_outputModuleStates;

    /**
   * Constructs a new SwerveTrajectoryAction that when executed will follow the provided
   * trajectory. This command will not return output voltages but rather raw module states from the
   * position controllers which need to be put into a velocity PID.
   *
   * <p>Note: The controllers will *not* set the outputVolts to zero upon completion of the path-
   * this is left to the user, since it is not appropriate for paths with nonstationary endstates.
   *
   * @param trajectory The trajectory to follow.
   * @param pose A function that supplies the robot pose - use one of the odometry classes to
   *     provide this.
   * @param kinematics The kinematics for the robot drivetrain.
   * @param xController The Trajectory Tracker PID controller for the robot's x position.
   * @param yController The Trajectory Tracker PID controller for the robot's y position.
   * @param thetaController The Trajectory Tracker PID controller for angle for the robot.
   * @param outputModuleStates The raw output module states from the position controllers.
   */
    // public TrajectoryAction(
    //     PathPlannerTrajectory trajectory,
    //     Supplier<Pose2d> pose,
    //     Supplier<Rotation2d> desired_rotation,
    //     SwerveDriveKinematics kinematics,
    //     PIDController xController,
    //     PIDController yController,
    //     ProfiledPIDController thetaController,
    //     Consumer<SwerveModuleState[]> outputModuleStates) {
    //         m_trajectory = trajectory;
    //         m_pose = pose;
    //         m_desired_rotation = desired_rotation;
    //         m_kinematics = kinematics;
    //         m_controller = new HolonomicDriveController(xController, yController, thetaController);
    //         m_outputModuleStates = outputModuleStates;
    // }

    public TrajectoryAction(
        PathPlannerTrajectory trajectory,
        Supplier<Pose2d> pose,
        SwerveDriveKinematics kinematics,
        PIDController xController,
        PIDController yController,
        PIDController thetaController,
        Consumer<SwerveModuleState[]> outputModuleStates) {
            m_trajectory = trajectory;
            m_pose = pose;
            m_kinematics = kinematics;
            m_controller = new PPHolonomicDriveController(xController, yController, thetaController);
            m_outputModuleStates = outputModuleStates;
    }

    @Override
    public boolean isFinished() {
        return m_timer.hasElapsed(m_trajectory.getTotalTimeSeconds());
    }

    @Override
    public void start() {
        m_timer.reset();
        m_timer.start();
    }

    @Override
    public void update() {
        double curTime = m_timer.get();
        var desiredState = (PathPlannerState) m_trajectory.sample(curTime);

        var rotation_feedback = m_pose.get().getRotation().getDegrees();
        var rotation_command = desiredState.holonomicRotation.getDegrees();

        //System.out.println(rotation_command + ", " + rotation_feedback + ", " + desiredState.poseMeters.getX() + ", " + m_pose.get().getX() + ", " + desiredState.poseMeters.getY() + ", " + m_pose.get().getY());

        //System.out.println(rotation_command + ", " + rotation_feedback);

        var targetChassisSpeeds = m_controller.calculate(m_pose.get(), desiredState);
        // var targetChassisSpeeds =
        //     m_controller.calculate(m_pose.get(), desiredState, desiredState.holonomicRotation);
        // System.out.println(desiredState.holonomicRotation.getDegrees() + ", " + m_pose.get().getRotation().getDegrees());
        var targetModuleStates = m_kinematics.toSwerveModuleStates(targetChassisSpeeds);
        
        m_outputModuleStates.accept(targetModuleStates);
    }

    @Override
    public void done() {
        m_timer.stop();
        m_outputModuleStates.accept(m_kinematics.toSwerveModuleStates((ChassisSpeeds.fromFieldRelativeSpeeds(0, 0, 0, Rotation2d.fromDegrees(0)))));
    }

    // get initial pose
    public Pose2d getInitialPose() {
        return m_trajectory.getInitialPose();
    }
}
