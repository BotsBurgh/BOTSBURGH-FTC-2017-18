package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.CryptoboxDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="Cryptobox Detector", group="DogeCV")

public class CryptoboxDetectorFile extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private CryptoboxDetector cryptoboxDetector = null;
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        //Init detector
        cryptoboxDetector = new CryptoboxDetector();
        cryptoboxDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        cryptoboxDetector.rotateMat = false;
        cryptoboxDetector.enable();
    }

    @Override
    public void init_loop() {
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        int leftColumnAdjusted=roundTens(cryptoboxDetector.getCryptoBoxLeftPosition());
        int centerColumnAdjusted=roundTens(cryptoboxDetector.getCryptoBoxCenterPosition());
        int rightColumnAdjusted=roundTens(cryptoboxDetector.getCryptoBoxRightPosition());
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("isCryptoBoxDetected", cryptoboxDetector.isCryptoBoxDetected());
        telemetry.addData("isColumnDetected ",  cryptoboxDetector.isColumnDetected());
        telemetry.addData("Column Left ",  cryptoboxDetector.getCryptoBoxLeftPosition());
        telemetry.addData("Column Center ",  cryptoboxDetector.getCryptoBoxCenterPosition());
        telemetry.addData("Column Right ",  cryptoboxDetector.getCryptoBoxRightPosition());
        telemetry.addData("Adjusted Left", leftColumnAdjusted);
        telemetry.addData("Adjusted Center", centerColumnAdjusted);
        telemetry.addData("Adjusted Right", rightColumnAdjusted);

    }

    @Override
    public void stop() {
        cryptoboxDetector.disable();
    }
    public int roundTens(int val) {
        int x = val;
        for(int i = 0; i < 9; i++) {
            if (x % 10 == 0) {
                break;
            } else {
                x++;
            }
        }
        return x;
    }
}