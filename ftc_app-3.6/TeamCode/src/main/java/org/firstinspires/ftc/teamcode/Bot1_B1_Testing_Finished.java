/*
This program combines color sensor and vumark together
*/
package org.firstinspires.ftc.teamcode;

//basic imports
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

//VuMark imports
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

//rev color sensor imports
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import java.util.Locale;

//Gyro imports
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.matrices.DenseMatrixF;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@Autonomous(name="Bot1 B1 Testing Finished", group ="Concept")

public class Bot1_B1_Testing_Finished extends LinearOpMode {
    
    private final double UP_POS = 0.1;
    private final double DOWN_POS = 0.96;
    
    static final double     COUNTS_PER_MOTOR_REV    = 560 ;    // eg: Andymark Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 3.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.2;
    static final double     TURN_SPEED              = 0.15;
    
    //String
    private String color;
    
    //wheels
    private DcMotor left;
    private DcMotor right;
    
    //gyro on rev hub
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;
    
    //Vuforia stuffs
    public static final String TAG = "Vuforia VuMark Sample";
    VuforiaLocalizer vuforia;
    
    //run time
    private ElapsedTime runtime = new ElapsedTime();
    
    //color sensor
    ColorSensor sensorColor;
    
    //servo arm for color sensor
    Servo servo;
    Servo turn;
    
    //servos for glyph
    Servo leftC;
    Servo rightC;
    
    //elevator
    DcMotor elevator;
    
    @Override public void runOpMode() {

        //color sensor
        sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");
        
        servo = hardwareMap.get(Servo.class, "jewel_arm");
        turn = hardwareMap.get(Servo.class, "turn_arm");
        
        leftC = hardwareMap.get(Servo.class, "left_servo");
        rightC = hardwareMap.get(Servo.class, "right_servo");
        
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        elevator.setDirection(DcMotor.Direction.FORWARD);
        //initializing Vuforia on phone camera
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "Aazdgn3/////AAAAGWj/wwSCikKhql6EpveSzXSD0H1p2+9y3wFZTeGaDPArhcfj5j63s3TJs5sanBeaBo0JyvTQlkOvM7JJC04G9r9N7Sp7KP10vKGjvRLHpt+zpMoQX8bsKinccU0A3jMDZOBzuhn1FYS0ekhb7d1DkC1iHBz9A3vq6cdBWCBH5o2tuxkoNsSmO9j5Q8sVIKk/6HSrbaiPug78kX30DYb4cgChGfc99wx4SfkEfwuT0MN+g89ZUOgC1y4D67MZs1EfMWIMLdSdKJM9f0KReS+kqedFVSaj1gEPHGH24E4jXhbVJ7qRYrN+p6CCb52Px/8Qkyvl+Q8Sv/QBEyZHiC4p9T3chDEpH3DDFywl/qdRUPT6";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;     //set the direction of the camera
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        
        //wheels
        left = hardwareMap.get(DcMotor.class, "left_motor");
        right = hardwareMap.get(DcMotor.class, "right_motor");
        left.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.FORWARD);
        
        // encoder setting
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        //gyro on rev hub
        BNO055IMU.Parameters parametersGyro = new BNO055IMU.Parameters();
        parametersGyro.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parametersGyro.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parametersGyro.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parametersGyro.loggingEnabled      = true;
        parametersGyro.loggingTag          = "IMU";
        parametersGyro.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parametersGyro);
        composeTelemetry();
        
        //servo set position
        servo.setPosition(UP_POS);
        turn.setPosition(.7);
        
        //press the start button
        waitForStart();
        leftC.setPosition(.24);
        rightC.setPosition(.63);
        sleep(50);
        //reset time to 0
        runtime.reset();
        
        //gyro reset
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        
        //activate relic location
        relicTrackables.activate();
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        elevator.setPower(.35);
        
 for (double i = UP_POS; i < DOWN_POS; i += 0.05)
        {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            servo.setPosition(i);
            sleep(150);
        }
        vuMark = RelicRecoveryVuMark.from(relicTemplate);
        servo.setPosition(DOWN_POS);
        elevator.setPower(.1);
        vuMark = RelicRecoveryVuMark.from(relicTemplate);
        sleep(300);
        vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if ((sensorColor.red() > sensorColor.blue()))
        {
            color = "red";
        }
        else
        {
            color = "blue";
        }

        if (color.equals("red"))
        {
            turn.setPosition(.5);
        }
        else
        {
            turn.setPosition(.9);
        }
        sleep(300);
        servo.setPosition(.17);
        turn.setPosition(.7);
        encoderDrive(DRIVE_SPEED, -10.5, -10.5, 8.5);
        sleep(200);
        encoderDrive(0.08, .5, .5, 8.5);
        sleep(200);
        encoderDrive(0.08, .5, .5, 8.5);
        sleep(200);
        encoderDrive(0.08, .5, .5, 8.5);
        sleep(200);
        encoderDrive(DRIVE_SPEED, -9, -9, 8.5);
        sleep(200);
        left.setPower(.02);
        right.setPower(-.2);
        while (nitesh() < 41)
        {
            telemetry.update();
        }
        left.setPower(.015);
        right.setPower(-.08);
        while (nitesh() < 53)
        {
            telemetry.update();
        }
        left.setPower(0);
        right.setPower(0);
        sleep(100);
        if (vuMark == RelicRecoveryVuMark.LEFT || vuMark == RelicRecoveryVuMark.UNKNOWN)
        {
            
        }
        if (vuMark == RelicRecoveryVuMark.CENTER)
        {
            left.setPower(.2);
            right.setPower(-.02);
            while (nitesh() < 62)
            {
                telemetry.update();
            }
            left.setPower(.08);
            right.setPower(-.015);
            while (nitesh() < 74)
            {
                telemetry.update();
            }
            left.setPower(0);
            right.setPower(0);
        }
        if (vuMark == RelicRecoveryVuMark.RIGHT)
        {
            left.setPower(.2);
            right.setPower(-.02);
            while (nitesh() < 75)
            {
                telemetry.update();
            }
            left.setPower(.08);
            right.setPower(-.015);
            while (nitesh() < 87)
            {
                telemetry.update();
            }
            left.setPower(0);
            right.setPower(0);
            sleep(100);
            left.setPower(.02);
            right.setPower(-.2);
            while (nitesh() < 94)
            {
                telemetry.update();
            }
            left.setPower(.015);
            right.setPower(-.08);
            while (nitesh() < 106)
            {
                telemetry.update();
            }
            left.setPower(0);
            right.setPower(0);
        }
        sleep(150);
        
        elevator.setPower(-.5);
        sleep(800);
            
        elevator.setPower(0);
            
        leftC.setPosition(.8);
        rightC.setPosition(0);
        sleep(50);
        left.setPower(.2);
        right.setPower(.2);
        sleep(2000);
        left.setPower(0);
        right.setPower(0);
        encoderDrive(DRIVE_SPEED, -2, -2, 5.0);
    }
    public double nitesh() {
       return Math.abs(Double.parseDouble(formatAngle(angles.angleUnit, angles.firstAngle)));
    }
    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
                {
                // Acquiring the angles is relatively expensive; we don't want
                // to do that in each of the three items that need that info, as that's
                // three times the necessary expense.
                angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                gravity  = imu.getGravity();
                }
            });

        telemetry.addLine()
            .addData("status", new Func<String>() {
                @Override public String value() {
                    return imu.getSystemStatus().toShortString();
                    }
                })
            .addData("calib", new Func<String>() {
                @Override public String value() {
                    return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
            .addData("heading", new Func<String>() {
                @Override public String value() {
                    return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
            .addData("roll", new Func<String>() {
                @Override public String value() {
                    return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
            .addData("pitch", new Func<String>() {
                @Override public String value() {
                    return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
            .addData("grvty", new Func<String>() {
                @Override public String value() {
                    return gravity.toString();
                    }
                })
            .addData("mag", new Func<String>() {
                @Override public String value() {
                    return String.format(Locale.getDefault(), "%.3f",
                            Math.sqrt(gravity.xAccel*gravity.xAccel
                                    + gravity.yAccel*gravity.yAccel
                                    + gravity.zAccel*gravity.zAccel));
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
    
    
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = left.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = right.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            left.setTargetPosition(newLeftTarget);
            right.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            left.setPower(Math.abs(speed));
            right.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (left.isBusy() && right.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                                            left.getCurrentPosition(),
                                            right.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            left.setPower(0);
            right.setPower(0);

            // Turn off RUN_TO_POSITION
            left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
