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

@TeleOp(name="Bot1 TeleOp Two Guy", group="Linear Opmode")

public class Bot1_TeleOp_2_Guy20180124 extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor left = null;
    private DcMotor right = null;
    private DcMotor elevator;
    private Servo leftC;
    private Servo rightC;
    private int open = 1;

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

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        left.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.FORWARD);
        elevator.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        leftC.setPosition(.7);//.93 for 180 but will knock on the chasis
        rightC.setPosition(.1);
        boolean slow = false;
        
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
        
            if(gamepad1.y || gamepad2.y) {
                slow = !slow;
            }
            if(slow) {
                leftPower    = Range.clip(drive + turn, -0.30, 0.30) ;
                rightPower   = Range.clip(drive - turn, -0.30, 0.30) ;
            }
            else
            {
                leftPower    = Range.clip(drive + turn, -0.55, 0.55) ;
                rightPower   = Range.clip(drive - turn, -0.55, 0.55) ;
            }
            left.setPower(leftPower);
            right.setPower(rightPower);
            if(gamepad2.dpad_up) {
                elevator.setPower(.70);
            } else if(gamepad2.dpad_down) {
                elevator.setPower(-.4);
            } else {
            if (gamepad1.left_trigger > 0.2)
            {
                elevator.setPower(.7);
            }
            else if (gamepad1.right_trigger > 0.2)
            {
                /*if(elevator.getPosition()==0) {
                    
                }
                */
                elevator.setPower(-.4);
                
            }
            else
            {
                elevator.setPower(0.147);
            }
            }
            // Left Closes
            if(gamepad2.right_stick_x >= .1) {
                leftC.setPosition(leftC.getPosition()-.005);
            } else if (gamepad2.right_stick_x <= -.1) {
                if(leftC.getPosition()+.005 <= .7) {
                    leftC.setPosition(leftC.getPosition()+.005);
                    
                }
            }else if(gamepad2.left_stick_x >= .1) {
                if(rightC.getPosition()-.005 >= .1) {
                    rightC.setPosition(rightC.getPosition()-.005);
            
                }
            } else if(gamepad2.left_stick_x <= -.1) {
                rightC.setPosition(rightC.getPosition()+.005);
            }
            else {
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
                    leftC.setPosition(.24);
                    rightC.setPosition(.67);
                    
                    open = 1;
                }
                // Right opens
                if (gamepad1.right_bumper)
                {
                    if (open == 1 && !press)
                    {
                        leftC.setPosition(.60);
                        rightC.setPosition(.20);
                        press = true;
                        open = 2;
                    }
                    else if (open == 2 && !press)
                    {
                        leftC.setPosition(.60);
                        rightC.setPosition(.15);
                        press = true;
                        open = 3;
                    }
                }
                else
                {
                    press = false;
                }
            }
           /* if (gamepad1.right_bumper)
            {
                leftC.setPosition(.24);
                rightC.setPosition(.63);
            }
            else
            {
                leftC.setPosition(.63);
                rightC.setPosition(.20);
            }*/

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Left_Pos", ""+leftC.getPosition());
            telemetry.addData("Right_Pos",rightC.getPosition());
            telemetry.update();
        }
    }
}
