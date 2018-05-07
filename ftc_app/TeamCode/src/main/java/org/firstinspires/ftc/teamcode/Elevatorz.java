/*
Copyright 2018 FIRST Tech Challenge Team 11792

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Elevatorz {
    DcMotor elevator;
    double ELEVATOR_POWER = 0.3;
    Elevatorz(DcMotor elevator_m) {
        this.elevator = elevator_m;
    }
    public void elevatorMove(int value) { //Value is location
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //Reset encoder
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Set mode to encoder
        elevator.setTargetPosition(value); //Set the position to run to
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION); //Set to use encoder...
        elevator.setPower(ELEVATOR_POWER); //Go!
    }
}
