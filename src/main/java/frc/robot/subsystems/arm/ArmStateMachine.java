// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import frc.robot.lib.statemachine.StateMachine;

public class ArmStateMachine extends StateMachine {

    public static IdleState idleState = new IdleState();
    public static ManualMoveState manualMoveState = new ManualMoveState();
    public static SubstationIntakeState substationIntakeState = new SubstationIntakeState();
    public static OuttakeState outtakeState = new OuttakeState();
    public static FPickupState fPickupState = new FPickupState();
    public static BPickupState bPickupState = new BPickupState();
    public static ScoreHighState scoreHighState = new ScoreHighState();
    public static ScoreMidState scoreMidState = new ScoreMidState();
    public static ScoreLowState scoreLowState = new ScoreLowState();
    public static ScoringState scoringState = new ScoringState();

    public ArmStateMachine() {

        // nutz.build();
        // deez.build();
        idleState.build();
        manualMoveState.build();
        substationIntakeState.build();
        outtakeState.build();
        fPickupState.build();
        bPickupState.build();
        scoreHighState.build();
        scoreMidState.build();
        scoreLowState.build();
        scoringState.build();

        setCurrentState(idleState);
    }
}
