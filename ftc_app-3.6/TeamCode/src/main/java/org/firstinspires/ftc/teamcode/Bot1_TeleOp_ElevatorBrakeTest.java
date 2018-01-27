/*
TeleOp
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Bot1 TeleOp Elevator", group="Linear Opmode")

public class Bot1_TeleOp_ElevatorBrakeTest extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor left = null;
    private DcMotor right = null;
    private DcMotor elevator;
    private Servo leftC;
    private Servo rightC;
    private double leftPower;
    private double rightPower;
    
    private double open = 1;

    private boolean press = false;
    
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        left  = hardwareMap.get(DcMotor.class, "left_motor");
        right = hardwareMap.get(DcMotor.class, "right_motor");
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        leftC = hardwareMap.get(Servo.class, "left_servo");
        rightC = hardwareMap.get(Servo.class, "right_servo");
        double BRAKE_CONST = 0;

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        left.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.FORWARD);
        elevator.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        leftC.setPosition(.8);//.93 for 180 but will knock on the chasis
        rightC.setPosition(0);
        
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            if (gamepad1.left_trigger > 0.2)
            {
                leftPower    = Range.clip(drive + turn, -0.3, 0.3) ;
                rightPower   = Range.clip(drive - turn, -0.3, 0.3) ;
            }
            else
            {
                leftPower    = Range.clip(drive + turn, -0.4, 0.4) ;
                rightPower   = Range.clip(drive - turn, -0.4, 0.4) ;
            }
            left.setPower(leftPower);
            right.setPower(rightPower);
            
            if (gamepad2.x)
            {
                elevator.setPower(.7);
            }
            else if (gamepad2.y)
            {
                elevator.setPower(-.4);
            }
            else if (gamepad2.b)
            {
                BRAKE_CONST += .01;
            }
            else if (gamepad2.a)
            {
                BRAKE_CONST += .1;
            } 
            else if (gamepad2.left_bumper) {
                BRAKE_CONST -= .01;
            }
            else if (gamepad2.right_bumper) {
                BRAKE_CONST -= .1;
            }
            else
            {
                elevator.setPower(BRAKE_CONST);
            }
            // Left Closes
            if (gamepad1.left_bumper)
            {
                /*
                for(int i = 2; i > 0; i--) {
                    leftC.setPosition(.43+.1*i);
                    rightC.setPosition(.40-.1*i);
                    sleep(100);
                }
                */
                leftC.setPosition(.38);
                rightC.setPosition(.45);
                open = 1;
            }
            // Right opens
            if (gamepad1.right_bumper)
            {
                if (open == 1 && !press)
                {
                    leftC.setPosition(.63);
                    rightC.setPosition(.20);
                    press = true;
                    open = 2;
                }
                else if (open == 2 && !press)
                {
                    leftC.setPosition(.8);
                    rightC.setPosition(.0);
                    press = true;
                    open = 3;
                }
            }
            else
            {
                press = false;
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Elevator Power", ""+BRAKE_CONST);
            telemetry.update();
        }
    }
}
