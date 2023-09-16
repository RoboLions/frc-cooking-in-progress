// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.lib.statemachine.State;
import frc.robot.lib.statemachine.Transition;

/** Add your docs here. */
public class IntakingState extends State {
    
    @Override
    public void build() {
        transitions.add(new Transition(() -> {
            return RobotMap.manipulatorController.getRawAxis(Constants.ManipulatorControls.INTAKE_AXIS) < 0.25;
        }, IntakeStateMachine.idleState));

    }

    @Override
    public void init(State prevState) {
        
    }

    @Override
    public void execute() {
        RobotMap.intake.runIntake();
    }

    @Override
    public void exit(State nextState) {
        
    }
}
