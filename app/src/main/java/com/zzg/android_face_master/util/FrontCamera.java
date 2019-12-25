package com.zzg.android_face_master.util;

import android.app.Activity;
import android.hardware.Camera;

/**
 * @author Zhangzhenguo
 * @create 2019/12/25
 * @Email 18311371235@163.com
 * @Describe
 */
public class FrontCamera {
    static final String TAG = "Camera";
    Camera mCamera;
    int mCurrentCamIndex = 0;
    boolean previewing;



    /**
     * 设置摄像头
     * @param camera
     */
    public void setCamera(Camera camera){
        this.mCamera=camera;
    }

    public int getCurrentCamIndex() {
        return this.mCurrentCamIndex;
    }

    public boolean getPreviewing() {
        return this.previewing;
    }
    Camera.ShutterCallback shutterCallback=new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };
    Camera.PreviewCallback previewCallback=new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {

        }
    };

    /**
     * 适配竖排固定角度
     * @param context
     * @param currentCamIndex
     * @param mCamera
     */
    public static void setCameraDisplayOrientation(Activity context, int currentCamIndex, Camera mCamera) {

    }
}
