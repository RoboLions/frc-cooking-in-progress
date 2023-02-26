// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import frc.robot.lib.statemachine.StateMachine;
import frc.robot.subsystems.arm.manual.ManualMoveState;
import frc.robot.subsystems.arm.manual.ScoreHighCubeState;

public class ArmStateMachine extends StateMachine {

    public static IdleState idleState = new IdleState();
    public static ManualMoveState manualMoveState = new ManualMoveState();
    public static SubstationIntakeState substationIntakeState = new SubstationIntakeState();
    public static OuttakeState outtakeState = new OuttakeState();
    public static FPickupState groundPickupState = new FPickupState();
    public static ScoreHighState scoreHighState = new ScoreHighState();
    public static ScoreMidState scoreMidState = new ScoreMidState();
    public static ScoreLowState scoreLowState = new ScoreLowState();
    public static ScoringState scoringState = new ScoringState();
    //public static ScoreHighCubeState scoreHighCubeState = new ScoreHighCubeState();

    public ArmStateMachine() {

        // nutz.build();
        // deez.build();
        idleState.build();
        manualMoveState.build();
        substationIntakeState.build();
        outtakeState.build();
        groundPickupState.build();
        scoreHighState.build();
        scoreMidState.build();
        scoreLowState.build();
        scoringState.build();
        //scoreHighCubeState.build();

        setCurrentState(idleState);
    }
}
