package com.zzg.android_face_master.holder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.zzg.android_face_master.thread.FaceTask;
import com.zzg.android_face_master.util.FrontCamera;

import static android.os.AsyncTask.Status.PENDING;
import static android.os.AsyncTask.Status.RUNNING;

/**
 * @author Zhangzhenguo
 * @create 2019/12/25
 * @Email 18311371235@163.com
 * @Describe
 */
public class SurfaceViewCallback implements SurfaceHolder.Callback, Camera.PreviewCallback {
    Context context;
    static final String TAG = "SYRFACECamera";
    FrontCamera mFrontCamera = new FrontCamera();
    boolean previewing = mFrontCamera.getPreviewing();
    Camera mCamera;
    FaceTask mFaceTask;

    public void setContext(Context context) {
        this.context = context;
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //初始化前置摄像头
        mFrontCamera.setCamera(mCamera);
        mCamera = mFrontCamera.initCamera();
        mCamera.setPreviewCallback(this);
        //适配竖排固定角度
        FrontCamera.setCameraDisplayOrientation((Activity) context, mFrontCamera.getCurrentCamIndex(), mCamera);
        Log.i(TAG, "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (previewing) {
            mCamera.stopPreview();
            Log.i(TAG, "停止预览");
        }
        try {
            mCamera.setPreviewDisplay(arg0);
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
            Log.i(TAG, "开始预览");
            //调用旋转屏幕时自适应
            //setCameraDisplayOrientation(MainActivity.this, mCurrentCamIndex, mCamera);
        } catch (Exception e) {
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mFrontCamera.StopCamera(mCamera);
        Log.i(TAG, "surfaceDestroyed");
    }

    /**
     * 相机实时数据的回调
     *
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

    Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Log.i(TAG, "已经获取了bitmap:" + bitmap.toString());
            previewing = false;
            //需要保存可执行下面
/*            new Thread(new Runnable() {
                @Override
                public void run() {
                    String filePath = ImageUtil.getSaveImgePath();
                    File file = new File(filePath);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file, true);
                        fos.write(data);
                        ImageUtil.saveImage(file, data, filePath);
                        fos.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();*/
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();//重新开启预览 ，不然不能继续拍照
            previewing = true;
        }


    };

    //初始化相机
    public Camera initCamera() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        Log.e(TAG, "cameraCount: " + cameraCount);
        previewing = true;

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            //在这里打开的是前置摄像头,可修改打开后置OR前置
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    cam = Camera.open(camIdx);
                    mCurrentCamIndex = camIdx;
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam;
    }

    /**
     * 停止相机
     * @param mCamera 需要停止的相机对象
     * */
    public void StopCamera(Camera mCamera) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        previewing = false;
    }

    /**
     * 旋转屏幕后自动适配（若只用到竖的，也可不要）
     * 已经在manifests中让此Activity只能竖屏了
     * @param activity 相机显示在的Activity
     * @param cameraId 相机的ID
     * @param camera 相机对象
     */
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera)
    {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation)
        {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        }
        else
        {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
