package com.zzg.android_face_master.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import com.zzg.android_face_master.FaceViewActivity;
import com.zzg.android_face_master.bean.CameraConfig;
import com.zzg.android_face_master.util.CameraErrorCallback;
import com.zzg.android_face_master.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhangzhenguo
 * @create 2020/1/9
 * @Email 18311371235@163.com
 * @Describe
 */
public class CameraView{

    private static final String TAG ="CameraView" ;
    public Camera mCamera;

    private RectangleView mFaceView;
    private Activity mActivity;

    public Bitmap bitmap;
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
     * 设置相机
     */
    public void setCamera(RectangleView faceView, Activity activity){
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
     * 设置预览进行显示
     * @param holder
     */
    public void setPreviewDisplay(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始预览
     */
    public void startPreview(){
        mCamera.startPreview();
    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        mCamera.stopPreview();
    }

    /**
     * 释放相机，并初始化为null
     */
    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    public void setPreviewCallbackWithBuffer(){
        mCamera.setPreviewCallbackWithBuffer(null);
    }

    /**
     * 相机Error回调
     * @param mErrorCallback
     */
    public void setErrorCallback(CameraErrorCallback mErrorCallback) {
        mCamera.setErrorCallback(mErrorCallback);
    }


    /**
     *设置相机参数、图像预览大小、自动对焦
     * @param width
     * @param height
     */
    public void configureCamera(int width, int height) {
        Camera.Parameters params = mCamera.getParameters();
        setOptimalPreviewSize(params, width, height);
        setAutoFocus(params);
        mCamera.setParameters(params);
    }

    /**
     * 设置预览大小和自动对焦
     * @param params
     * @param width
     * @param height
     */
    public void setOptimalPreviewSize(Camera.Parameters params, int width, int height) {
        if (params.getMaxNumFocusAreas()>0){
            List<Camera.Area> areas=new ArrayList<>();
//            指定图像中心的区域
            Rect areaRect=new Rect(-100, -100, 100, 100);
//            将权重设置为60%
            areas.add(new Camera.Area(areaRect,600));
//            指定图像右上角的区域
            Rect areaRectRight=new Rect(800, -1000, 1000, -800);
            areas.add(new Camera.Area(areaRectRight,400));
            params.setMeteringAreas(areas);
        }

        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        float targetRatio = (float) width / height;
        Camera.Size previewSize = Util.getOptimalPreviewSize(mActivity, previewSizes, targetRatio);
        CameraConfig.previewWidth = previewSize.width;
        CameraConfig.previewHeight = previewSize.height;

        Log.e(TAG, "previewWidth" + CameraConfig.previewWidth );
        Log.e(TAG, "previewHeight" + CameraConfig.previewHeight);

        /**
         * 计算大小以将全帧位图缩放为较小的位图
         * 缩放位图中的人脸检测比全位图具有更高的性能。
         * 图像尺寸越小->检测速度越快，但检测人脸的距离越短，
         * 所以按照你的目的计算尺寸
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
        //设置图像宽高
        params.setPreviewSize(previewSize.width, previewSize.height);
        mFaceView.setPreviewWidth(CameraConfig.previewHeight);
        mFaceView.setPreviewHeight(CameraConfig.previewHeight);

    }

    /**
     * 自动连续对焦
     * @param cameraParameters
     */
    public void setAutoFocus(Camera.Parameters cameraParameters) {
        List<String> focusModes = cameraParameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
            cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    /**
     * 当旋转图像预览也不会重新映射坐标系
     */
    public void setDisplayOrientation() {
        CameraConfig.mDisplayRotation = Util.getDisplayRotation(mActivity);
        CameraConfig.mDisplayOrientation = Util.getDisplayOrientation(CameraConfig.mDisplayRotation, CameraConfig.cameraId);

        mCamera.setDisplayOrientation(CameraConfig.mDisplayOrientation);

        if (mFaceView != null) {
            mFaceView.setDisplayOrientation(CameraConfig.mDisplayOrientation);
        }
    }

    /**
     * 当不使用相机时进行释放
     */
    public void release() {
        mCamera.release();
    }

    /**
     *
     * @param faceViewActivity
     */
    public void setPreviewCallback(FaceViewActivity faceViewActivity) {
        mCamera.setPreviewCallback(faceViewActivity);
    }

    /**
     * 捕获图像
     */
    public void takePicture(Camera.PictureCallback mPicture){
        mCamera.takePicture(null,null,mPicture);
    }

}
