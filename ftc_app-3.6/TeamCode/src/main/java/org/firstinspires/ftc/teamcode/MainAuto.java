package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

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
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;

import org.firstinspires.ftc.teamcode.ClosableVuforiaLocalizer;
import com.disnodeteam.dogecv.CameraViewDisplay;


@Autonomous(name="Main Autonomous")
public class MainAuto extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private com.disnodeteam.dogecv.detectors.JewelDetector jewelDetector = null;
    public static final String TAG = "Vuforia VuMark Sample";
    OpenGLMatrix lastLocation = null;
    ClosableVuforiaLocalizer vuforia;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //Init jewel detector
        jewelDetector = new com.disnodeteam.dogecv.detectors.JewelDetector();
        jewelDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        jewelDetector.areaWeight = 0.02;
        jewelDetector.detectionMode = com.disnodeteam.dogecv.detectors.JewelDetector.JewelDetectionMode.MAX_AREA;
        jewelDetector.debugContours = true;
        jewelDetector.maxDiffrence = 15;
        jewelDetector.ratioWeight = 15;
        jewelDetector.minArea = 700;
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AURQxD//////AAAAmYngwFbO2UCgq8vYsmJSh0AFi5T7oZniQK60vPjR2vPYoVEe2KnQ72Apf4hPpJp/gOwWhPHGyL/qFcKRv9YgHWJ2fkqNAVqrO6Aroh23A6jwpmA3UQccnchVFHSmNhJtk2ulyHo3yEUMKIJ2ZZOEZeAfjWoH07dCJbvfwKGKlu6Kd5aGLw/1rwmxU8cbemfBHi6blHVzqpghl7cNgormrXLhc/ssruqcchZYoCgIx5o2u7KMsyBTec5MjbAVDBMjVe2LW3twIplofuO6FbvSAjL1/GDY7at3WGaLXyZHmtobqmtC1lv91iKmKk0v+uWfbrgNqJAqZeCf+UZPjzGkBfcRQr3T18WzF5ZRNpJ0zkbr";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = new ClosableVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        relicTrackables.activate();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            int exitVuforia = 0;
            while (exitVuforia != 1) {
                RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
                if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                    telemetry.addData("VuMark", "%s visible", vuMark);

                    OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                    telemetry.addData("Pose", format(pose));
                    if (pose != null) {
                        VectorF trans = pose.getTranslation();
                        Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                        // Extract the X, Y, and Z components of the offset of the target relative to the robot
                        double tX = trans.get(0);
                        double tY = trans.get(1);
                        double tZ = trans.get(2);

                        // Extract the rotational components of the target relative to the robot
                        double rX = rot.firstAngle;
                        double rY = rot.secondAngle;
                        double rZ = rot.thirdAngle;
                    }
                exitVuforia = 1;
                }
                else {
                    telemetry.addData("VuMark", "not visible");
                }
            }

            vuforia.close();
            //angle the phone down (just in case)
            //phoneServo.setPosition(phoneServo.getPos()-0.15);

            jewelDetector.enable();
            //Jewel
            int exitJewel = 0;
            telemetry.addLine();
            while (exitJewel != 1) {
                if (jewelDetector.getLastOrder().toString() == "BLUE_RED") {
                    telemetry.addData("Jewel Order: ", "BLUE_RED");
                    exitJewel=1;
                } else if (jewelDetector.getLastOrder().toString() == "RED_BLUE") {
                    telemetry.addData("Jewel Order: ", "RED_BLUE");
                    exitJewel=1;
                } else {
                    //Retry
                }
            }

            jewelDetector.disable();
            telemetry.update();

            //Continue autonomous
        }
    }
    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
