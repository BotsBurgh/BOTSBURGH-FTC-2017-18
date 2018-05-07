package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class Grabber {
    //Set some constants. Position of the Glyph Grabbers
    public static int GROPEN  = 0;
    public static int GLOPEN  = 0;
    public static int GRCLOSE = 0;
    public static int GLCLOSE = 0;
    Servo left, right;
    Grabber(Servo left, Servo right) {
        this.left  = left;
        this.right = right;
    }
    public void open() {
        //Open the grabber
        left.setPosition(GLOPEN);
        right.setPosition(GROPEN);
    }
    public void close() {
        //Close the grabber
        left.setPosition(GLCLOSE);
        right.setPosition(GRCLOSE);
    }
}
