package frc.robot.subsystems.drive;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.lib.RoboLionsPID;
import frc.robot.lib.statemachine.State;
import frc.robot.lib.statemachine.Transition;

/** State for balancing on the charging station autonomously */
public class BalanceState extends State {

    public double pitchP = Constants.BalancePitchPID.P;
    public double pitchI = Constants.BalancePitchPID.D;
    public double pitchD = Constants.BalancePitchPID.I;

    public double rollP = Constants.BalanceRollPID.P;
    public double rollI = Constants.BalanceRollPID.I;
    public double rollD = Constants.BalanceRollPID.D;

    public static RoboLionsPID rollPID = new RoboLionsPID();
    public static RoboLionsPID pitchPID = new RoboLionsPID();

    @Override
    public void build(){
        // if driver joysticks are engaged, transition to teleop state
        addTransition(new Transition(() -> {
            return Math.abs(RobotMap.driverController.getLeftX()) > Constants.STICK_DEADBAND || 
                Math.abs(RobotMap.driverController.getLeftY()) > Constants.STICK_DEADBAND || 
                Math.abs(RobotMap.driverController.getRightX()) > Constants.STICK_DEADBAND ||
                Math.abs(RobotMap.driverController.getRightY()) > Constants.STICK_DEADBAND;
        }, DrivetrainStateMachine.teleopSwerve));
    }

    @Override
    public void init() {
        rollPID.initialize(
            rollD,
            rollI,
            rollD,
            0, 
            5, 
            Constants.Swerve.maxSpeed
        );
        rollPID.enableDeadBand = true;
        rollPID.enableCage = false;

        pitchPID.initialize(
            pitchD,
            pitchI,
            pitchD,
            0, 
            5, 
            Constants.Swerve.maxSpeed
        );
        pitchPID.enableDeadBand = true;
        pitchPID.enableCage = false;
    }

    @Override
    public void execute() {
        RobotMap.swerve.drive(
            new Translation2d(
                rollPID.execute(0.0, RobotMap.gyro.getRoll()),
                pitchPID.execute(0.0, RobotMap.gyro.getPitch())
            ).times(Constants.Swerve.maxSpeed), 
            RobotMap.swerve.getPose().getRotation().getDegrees(), 
            false, 
            true
        );
    }

    @Override
    public void exit() {
        RobotMap.swerve.drive(
            new Translation2d(0.0, 0.0).times(Constants.Swerve.maxSpeed), 
            0.0,
            true,
            true
        );
    }
}