package com.zzg.android_face_master.newFace.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import com.zzg.android_face_master.newFace.FaceViewActivity;
import com.zzg.android_face_master.newFace.bean.CameraConfig;
import com.zzg.android_face_master.newFace.util.CameraErrorCallback;
import com.zzg.android_face_master.newFace.util.Util;

import java.io.IOException;
import java.util.List;

/**
 * @author Zhangzhenguo
 * @create 2020/1/9
 * @Email 18311371235@163.com
 * @Describe
 */
public class CameraView{

    private static final String TAG ="CameraView" ;
    private Camera mCamera=null;

    private FaceOverlayView mFaceView;
    private Activity mActivity;
    /**
     * 设置相机
     */
    public void setCamera(FaceOverlayView faceView, Activity activity){
        this.mFaceView=faceView;
        this.mActivity=activity;
    }

    /**
     * 打开相机
     */
    public Camera openCamera(int around){
        int aroundA = -1;
        int aroundB = -1;
//        获取摄像头个数
        int numberOfCameras = mCamera.getNumberOfCameras();
//        获取摄像头详细信息，来决定使用前置或后置摄像头
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i=0;i<numberOfCameras;i++){
            Camera.getCameraInfo(i,cameraInfo);
            if (cameraInfo.facing== Camera.CameraInfo.CAMERA_FACING_BACK){
                aroundA=i;
            }else if (cameraInfo.facing== Camera.CameraInfo.CAMERA_FACING_FRONT){
                mFaceView.setFront(true);
                aroundB=i;
            }
        }
        if (around==0){
//            打开摄像头
            mCamera=Camera.open(aroundA);
        }else if (around==1){
            mCamera=Camera.open(aroundB);
        }
        return mCamera;
    }

    /**
     * 检查硬件设备
     * @param context
     * @return
     */
    public boolean checkCameraHardwre(Context context){
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 开始预览
     */
    public void startPreview(){
        mCamera.startPreview();
    }
    /**
     *
     * @param holder
     */
    public void setPreviewDisplay(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPreview() {
        mCamera.stopPreview();
    }

    public void setErrorCallback(CameraErrorCallback mErrorCallback) {
        mCamera.setErrorCallback(mErrorCallback);
    }


    public void configureCamera(int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        // Set the PreviewSize and AutoFocus:
        setOptimalPreviewSize(parameters, width, height);
        setAutoFocus(parameters);
        // And set the parameters:
        mCamera.setParameters(parameters);
    }

    public void setOptimalPreviewSize(Camera.Parameters cameraParameters, int width, int height) {
        List<Camera.Size> previewSizes = cameraParameters.getSupportedPreviewSizes();
        float targetRatio = (float) width / height;
        Camera.Size previewSize = Util.getOptimalPreviewSize(mActivity, previewSizes, targetRatio);
        CameraConfig.previewWidth = previewSize.width;
        CameraConfig.previewHeight = previewSize.height;

        Log.e(TAG, "previewWidth" + CameraConfig.previewWidth );
        Log.e(TAG, "previewHeight" + CameraConfig.previewHeight);

        /**
         * Calculate size to scale full frame bitmap to smaller bitmap
         * Detect face in scaled bitmap have high performance than full bitmap.
         * The smaller image size -> detect faster, but distance to detect face shorter,
         * so calculate the size follow your purpose
         */
        if (CameraConfig.previewWidth / 4 > 360) {
            CameraConfig.prevSettingWidth = 360;
            CameraConfig.prevSettingHeight = 270;
        } else if (CameraConfig.previewWidth / 4 > 320) {
            CameraConfig.prevSettingWidth = 320;
            CameraConfig.prevSettingHeight = 240;
        } else if (CameraConfig.previewWidth / 4 > 240) {
            CameraConfig.prevSettingWidth = 240;
            CameraConfig.prevSettingHeight = 160;
        } else {
            CameraConfig.prevSettingWidth = 160;
            CameraConfig.prevSettingHeight = 120;
        }

        cameraParameters.setPreviewSize(previewSize.width, previewSize.height);

        mFaceView.setPreviewWidth(CameraConfig.previewHeight);
        mFaceView.setPreviewHeight(CameraConfig.previewHeight);
    }

    public void setAutoFocus(Camera.Parameters cameraParameters) {
        List<String> focusModes = cameraParameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    public void setDisplayOrientation() {
        // Now set the display orientation:
        mDisplayRotation = Util.getDisplayRotation(mActivity);
        mDisplayOrientation = Util.getDisplayOrientation(mDisplayRotation, cameraId);

        mCamera.setDisplayOrientation(mDisplayOrientation);

        if (mFaceView != null) {
            mFaceView.setDisplayOrientation(mDisplayOrientation);
        }
    }
}
