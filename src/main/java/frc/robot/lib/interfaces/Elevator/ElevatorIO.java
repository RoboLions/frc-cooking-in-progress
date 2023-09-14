// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.lib.interfaces.Elevator;

import org.littletonrobotics.junction.AutoLog;

/** Add your docs here. */
public interface ElevatorIO {
    @AutoLog
    public static class ElevatorIOInputs{
        double elevatorSensorPosition;
        double elevatorSensorvelocity;
        double elevetorPercentOutput;
    }
    public default void setMotorPositionOutput(double position){}
    public default void resetEncoder(){}
    public default void setMotorPercentOutput(double output){}
    public default void updateInputs(ElevatorIOInputs inputs){}
    public default void setBrakeMode(){}
}
