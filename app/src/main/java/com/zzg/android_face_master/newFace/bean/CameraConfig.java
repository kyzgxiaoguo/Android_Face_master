package com.zzg.android_face_master.newFace.bean;

/**
 * @author Zhangzhenguo
 * @create 2020/1/9
 * @Email 18311371235@163.com
 * @Describe
 */
public class CameraConfig {
    //后置
    public static int CAMERA_FACING_BACK=0;
//    前置
    public static int CAMERA_FACING_FRONT=1;

    public static int previewWidth;
    public static int previewHeight;

    public static int prevSettingWidth;
    public static int prevSettingHeight;

    // 检测旋转和方向
    public static int mDisplayRotation;
    public static int mDisplayOrientation;

    public static boolean isThreadWorking = false;

    public static int counter = 0;
    public static int cameraId;
}
