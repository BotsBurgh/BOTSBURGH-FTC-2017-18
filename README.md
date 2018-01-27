# BOTSBURGH-FTC-2017-18

Created by: Aden (Aden1350), Sambhav (stiffy2000)

***

## Goal

The goal of this repository is for FTC teams to understand how to make their autonomous smarter.
This is done using DogeCV and some simple math to navigate the playing field.

## How to use

To download and use this, download (it's quite a large file) and open with Android Studio.

### Changing Camera Orientation

In the `org/opencv/android/CameraGLRendererBase.java` file, line 49 and 75; Change both occurrences of `CAMERA_ID_BACK` to `CAMERA_ID_FRONT`.
Also, in the file `org/firstinspires/ftc/teamcode/MainAuto.java` line 56, change it from `parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;` to `parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;`
Lastly, in the file `com/disnodeteam/dogecv/OpenCVPipeline.java`, change line 38 to `init(context, viewDisplay, 1);` for front, and `init(context, viewDisplay, 0);` for back

***

## WARNING

Created with Android Studio 3, may not work with other versions.
Not backwards compatible with v3.5.
Read the license

***

## Sources

[DogeCV](https://github.com/GTHSRobotics/DogeCV "DogeCV at GitHub")
