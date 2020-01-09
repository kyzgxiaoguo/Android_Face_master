package com.zzg.android_face_master.newFace.callback;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.zzg.android_face_master.newFace.bean.CameraConfig;
import com.zzg.android_face_master.newFace.view.CameraView;

/**
 * @author Zhangzhenguo
 * @create 2020/1/9
 * @Email 18311371235@163.com
 * @Describe
 */
public class CircleSurfaceHolderCallBack implements SurfaceHolder.Callback,Camera.PreviewCallback{
    private Context mContext;
    private Camera mCamera;
    private CameraView mCameraView;

    public CircleSurfaceHolderCallBack(Context context,Camera mCamera,CameraView cameraView){
        this.mContext=context;
        this.mCamera=mCamera;
        this.mCameraView=cameraView;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera=mCameraView.openCamera(CameraConfig.CAMERA_FACING_FRONT);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }
}
