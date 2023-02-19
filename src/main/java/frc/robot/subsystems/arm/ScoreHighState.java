// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import frc.robot.lib.statemachine.State;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.lib.statemachine.Transition;

/** Add your docs here. */
public class ScoreHighState extends State {

    @Override
    public void build() {
        // Go to IDLE Transitions
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButtonPressed(Constants.ManipulatorButtons.IDLE_BUTTON);
        }, ArmStateMachine.idleState));

        // transition to mid level
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButtonPressed(Constants.ManipulatorButtons.MID_SCORE_BUTTON);
        }, ArmStateMachine.scoreMidState));

        // transition to hybrid level
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButtonPressed(Constants.ManipulatorButtons.LOW_SCORE_BUTTON);
        }, ArmStateMachine.scoreLowState));

        // Go to scoring Transitions
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawButtonPressed(Constants.DriverButtons.SCORING_BUTTON);
        }, ArmStateMachine.scoringState));
    }
    
    @Override
    public void init() {
        Color current_color = RobotMap.claw.getColor();
        if (current_color == null) {
            RobotMap.armStateMachine.setCurrentState(ArmStateMachine.idleState);
            return;
        }

        double modifier = RobotMap.arm.getScoringDirectionModifier();

        if (current_color == Constants.Claw.CUBE_COLOR) {
            RobotMap.arm.moveArmPosition(
                modifier * Constants.HIGH_SCORE_CUBE.SHOULDER_POSITION, 
                modifier * Constants.HIGH_SCORE_CUBE.ELBOW_POSITION, 
                modifier * Constants.HIGH_SCORE_CUBE.WRIST_POSITION
            );
            return;
        }

        if (current_color == Constants.Claw.CONE_COLOR) {
            RobotMap.arm.moveArmPosition(
                modifier * Constants.HIGH_SCORE_CONE.SHOULDER_POSITION, 
                modifier * Constants.HIGH_SCORE_CONE.ELBOW_POSITION, 
                modifier * Constants.HIGH_SCORE_CONE.WRIST_POSITION
            );
            return;
        }
    }

    @Override
    public void execute() {
    }

    @Override
    public void exit() {
        
    }

}
