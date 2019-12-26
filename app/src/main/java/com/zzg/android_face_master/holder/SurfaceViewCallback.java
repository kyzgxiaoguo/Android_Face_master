package com.zzg.android_face_master.holder;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import com.zzg.android_face_master.thread.FaceTask;
import com.zzg.android_face_master.util.CameraUtil;

/**
 * @author Zhangzhenguo
 * @create 2019/12/25
 * @Email 18311371235@163.com
 * @Describe
 * SurfaceHolder.Callback可以访问底层表面，其次是一个辅助线程，
 * 提供了三个方法，正在创建，正在运行，正在销毁。使用它将画面绘制到屏幕表面。
 * 注意，调用它通常是在主线程中，它们需要与绘制线程也接触到任何状态正确同步。
 */

public class SurfaceViewCallback implements SurfaceHolder.Callback, Camera.PreviewCallback {
     private Context context;
     private static final String TAG = "SYRFACECamera";
     private CameraUtil mFrontCamera = new CameraUtil();
     private boolean previewing = mFrontCamera.getPreviewing();
     private Camera mCamera;
     private FaceTask mFaceTask;
     private Camera.Parameters parameters;
     private int isFrontOrArount;

    public void setContext(Context context,int isFrontOrArount) {
        this.context = context;
        this.isFrontOrArount = isFrontOrArount;
    }

    /**
     * 正在创建并显示SurfaceView
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //初始化前置摄像头
        mFrontCamera.setCamera(mCamera);
        mCamera = mFrontCamera.initCamera(isFrontOrArount);
        mCamera.setPreviewCallback(this);
        //适配竖排固定角度
        mFrontCamera.setCameraDisplayOrientation((Activity) context, mFrontCamera.getCurrentCamIndex(), mCamera);
        Log.i(TAG, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (previewing) {
            mCamera.stopPreview();
            Log.i(TAG, "停止预览");
        }
        if (mCamera!=null){
            parameters=mCamera.getParameters();
            //预选区域格式
            parameters.setPictureFormat(PixelFormat.JPEG);
            //设置预选区域大小
            parameters.setPictureSize(width,height);
            parameters.setJpegQuality(80);
        }

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
            Log.i(TAG, "开始预览");
            //调用旋转屏幕时自适应
            //适配竖排固定角度
            mFrontCamera.setCameraDisplayOrientation((Activity) context, mFrontCamera.getCurrentCamIndex(), mCamera);
        } catch (Exception e) {
        }
    }

    /**
     * 正在销毁并隐藏SurfaceView
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mFrontCamera.StopCamera(mCamera);
        Log.i(TAG, "surfaceDestroyed");
    }

    /**
     * 相机实时数据的回调
     * @param data   相机获取的数据，格式是YUV
     * @param camera 相应相机的对象
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mFaceTask != null) {
            switch (mFaceTask.getStatus()) {
                case RUNNING:
                    return;
                case PENDING:
                    mFaceTask.cancel(false);
                    break;
            }

        }
        mFaceTask = new FaceTask(data, camera);
        mFaceTask.execute((Void) null);

        //Log.i(TAG, "onPreviewFrame: 启动了Task");
    }

    /**
     * 切换前后置摄像头
     */
    public void setSwitchFrontOrArount(int isFrontOrArount){
        mFrontCamera.initCamera(isFrontOrArount);
    }

}
