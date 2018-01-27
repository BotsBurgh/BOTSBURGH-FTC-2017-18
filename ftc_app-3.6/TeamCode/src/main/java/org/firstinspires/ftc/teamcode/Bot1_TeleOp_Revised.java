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

@TeleOp(name="Bot1 TeleOp Revised", group="Linear Opmode")

public class Bot1_TeleOp_Revised extends LinearOpMode {

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
        
        
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            double slowDrive = -gamepad1.left_stick_y;
            double slowTurn  =  gamepad1.left_stick_x;
            double fastDrive = -gamepad1.right_stick_y;
            double fastTurn = gamepad1.right_stick_x;
            if (Math.abs(slowDrive) >= .1|| Math.abs(slowTurn) >= .1)
            {
                leftPower    = Range.clip(slowDrive + slowTurn, -0.3, 0.3) ;
                rightPower   = Range.clip(slowDrive - slowTurn, -0.3, 0.3) ;
            }
            
            else
            {
                leftPower    = Range.clip(fastDrive + fastTurn, -0.50, 0.50) ;
                rightPower   = Range.clip(fastDrive - fastTurn, -0.50, 0.50) ;
            }
            left.setPower(leftPower);
            right.setPower(rightPower);
            
            if (gamepad2.dpad_up)
            {
                elevator.setPower(.7);
            }
            else if (gamepad2.dpad_down)
            {
                elevator.setPower(-.4);
            }
            else
            {
                if(gamepad1.dpad_up) {
                    elevator.setPower(.7);
                } else if (gamepad1.dpad_down) {
                    elevator.setPower(-.4);
                } else {
                    elevator.setPower(.147);
                }
            }
            double leftPos = gamepad2.left_stick_x/2+.5;
            double rightPos = gamepad2.right_stick_x/2+.5;
            
            if(leftPos >= .6 || leftPos <= .4) {
                leftC.setPosition(leftPos);
            } else if(rightPos >= .6 || leftPos <= .4) {
                rightC.setPosition(1-rightPos);
            } else {
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
                    rightC.setPosition(.63);
                    
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
                        leftC.setPosition(.7);
                        rightC.setPosition(.1);
                        press = true;
                        open = 3;
                    }
                }
                else
                {
                    press = false;
                }
            }
            

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
