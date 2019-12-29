package com.zzg.android_face_master.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.util.Log;
import android.view.SurfaceHolder;

import com.zzg.android_face_master.R;
import com.zzg.android_face_master.model.FaceModel;
import com.zzg.android_face_master.model.FaceTask;
import com.zzg.android_face_master.util.CameraUtil;
import com.zzg.android_face_master.view.MainViewCallback;

import java.io.IOException;
import java.util.logging.Handler;

/**
 * @author Zhangzhenguo
 * @create 2019/12/25
 * @Email 18311371235@163.com
 * @Describe
 * SurfaceHolders.Callback可以访问底层表面，其次是一个辅助线程，
 * 提供了三个方法，正在创建，正在运行，正在销毁。使用它将画面绘制到屏幕表面。
 * 注意，调用它通常是在主线程中，它们需要与绘制线程也接触到任何状态正确同步。
 */

public class SurfaceViewCallback implements SurfaceHolder.Callback, Camera.PreviewCallback{
     private Context context;
     private static final String TAG = "SYRFACECamera";
     private CameraUtil mFrontCamera = new CameraUtil();
     private boolean previewing = mFrontCamera.getPreviewing();
     private Camera mCamera;
     private FaceTask mFaceTask;
     private int isFrontOrArount;
     private FaceModel model;
    private MainViewCallback mainViewCallback;
    private SurfaceHolder surfaceHolder;

    public void setContext(Context context, int isFrontOrArount, MainViewCallback mainViewCallback,SurfaceHolder surfaceHolder) {
        this.context = context;
        this.isFrontOrArount = isFrontOrArount;
        this.surfaceHolder = surfaceHolder;
        this.mainViewCallback = mainViewCallback;
        if (model==null){
            model=new FaceModel(mainViewCallback);
        }
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
        //初始化前置摄像头
        mCamera.setPreviewCallback(this);
        //适配竖排固定角度
        CameraUtil.setCameraDisplayOrientation((Activity) context, mFrontCamera.getCurrentCamIndex(), mCamera);
        Log.i(TAG, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (previewing) {
            mCamera.stopPreview();
            Log.i(TAG, "停止预览");
        }
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
            Log.i(TAG, "开始预览");
            //调用旋转屏幕时自适应
            //setCameraDisplayOrientation(MainActivity.this, mCurrentCamIndex, mCamera);
        } catch (Exception e) {
        }

//        if (mCamera!=null){
//            parameters=mCamera.getParameters();
//            parameters.setPictureFormat(PixelFormat.RGB_565);
//           // 设置预览区域的大小
//            parameters.setPreviewSize(width,height);
//           // 设置每秒钟预览帧数  帧数越大识别速度越快，但建议范围25-60帧数，视手机自身性能而定
//            parameters.setPreviewFrameRate(25);
//           // 设置预览图片的大小
//            parameters.setPictureSize(width,height);
//            parameters.setJpegQuality(80);
//        }
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
                case PENDING:
                    mFaceTask.cancel(false);
                    break;
                case RUNNING:

                    return;
                case FINISHED:
                    break;
            }
        }
        mFaceTask = new FaceTask(data, camera,mainViewCallback,surfaceHolder);
        mFaceTask.execute((Void) null);
        Log.i(TAG, "onPreviewFrame: 启动了Task");
    }

}
