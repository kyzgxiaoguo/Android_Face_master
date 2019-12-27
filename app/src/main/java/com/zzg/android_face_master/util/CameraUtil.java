package com.zzg.android_face_master.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.util.Log;
import android.view.Surface;

/**
 * 相机类，相机的调用
 */
public class CameraUtil {
    public static final String TAG = "Camera";
    public Camera mCamera;
    public int mCurrentCamIndex = 0;
    public boolean previewing;
    public static int orientionOfCamera;

    private FaceDetector.Face [] faces;

    public void setCamera(Camera camera) {
        this.mCamera = camera;
    }

    public int getCurrentCamIndex() {
        return this.mCurrentCamIndex;
    }

    public boolean getPreviewing() {
        return this.previewing;
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

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
    public Camera initCamera(int isFrontOrArount) {
        int frontIndex =-1;
        int backIndex = -1;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        Log.e(TAG, "cameraCount: " + cameraCount);
        previewing = true;
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            try {
                //在这里打开的是前置摄像头,可修改打开后置OR前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    backIndex=camIdx;
                }else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    frontIndex=camIdx;
                }
                if (isFrontOrArount == 0) {
                    cam = Camera.open(backIndex);
                }else if (isFrontOrArount == 1) {
                    cam = Camera.open(frontIndex);
                }
            } catch (RuntimeException e) {
                Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
            }
            mCurrentCamIndex = camIdx;
            Log.d(TAG, "前置后置"+camIdx);
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
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                orientionOfCamera=0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                orientionOfCamera=0;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                orientionOfCamera=0;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                orientionOfCamera=0;
                break;
        }
        orientionOfCamera = info.orientation;
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        }
        else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * 检测人脸
     *@paramdata预览的图像数据
     */
    private void detectionFaces(byte[] data) {
        BitmapFactory.Options options =new BitmapFactory.Options();
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(data,0,data.length, options);
        int width = bitmap1.getWidth();
        int height = bitmap1.getHeight();
        Matrix matrix =new Matrix();
        Bitmap bitmap2 =null;
        FaceDetector detector =null;
        switch(orientionOfCamera) {
            case 0:
                detector =new FaceDetector(width,height,10);
                matrix.postRotate(0.0f,width /2,height /2);
                // 以指定的宽度和高度创建一张可变的bitmap（图片格式必须是RGB_565，不然检测不到人脸）
                bitmap2 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
                break;
            case 90:
                detector =new FaceDetector(height,width,1);
                matrix.postRotate(-270.0f,height /2,width /2);
                bitmap2 = Bitmap.createBitmap(height,width,Bitmap.Config.RGB_565);
                break;
            case 180:
                detector =new FaceDetector(width,height,1);
                matrix.postRotate(-180.0f,width /2,height /2);
                bitmap2 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
                break;
            case 270:
                detector =new FaceDetector(height,width,1);
                matrix.postRotate(-90.0f,height /2,width /2);
                bitmap2 = Bitmap.createBitmap(height,width,Bitmap.Config.RGB_565);
                break;
                default:
                    break;
        }
        faces=new FaceDetector.Face[10];
        Paint paint =new Paint();
        paint.setDither(true);
        Canvas canvas =new Canvas();
        canvas.setBitmap(bitmap2);
        canvas.setMatrix(matrix);
        // 将bitmap1画到bitmap2上（这里的偏移参数根据实际情况可能要修改）
        canvas.drawBitmap(bitmap1,0,0,paint);
        int faceNumber = detector.findFaces(bitmap2, faces);
        Log.e("---------->","faceNumber:"+faceNumber+"faces.size:"+faces.length);
        //mTV.setText("facnumber----" + faceNumber);
//        mTV.setTextColor(Color.RED);
        if(faceNumber!=0) {
//            mFindFaceView.setVisibility(View.VISIBLE);
//            mFindFaceView.drawRect(faces,faceNumber);
        }else{
//            mFindFaceView.setVisibility(View.GONE);
        }
        bitmap2.recycle();
        bitmap1.recycle();

//        canvas.drawRect((int) ((mWidth-midPoint.x) - (eyesDistance*2+eyesDistance/2)),
//                (int) (midPoint.y-eyesDistance)+210,
//         (int) ( (mWidth-midPoint.x) + (eyesDistance/3)),
//         (int) (midPoint.y+(eyesDistance*2+eyesDistance/2))+210,paint);
    }

}
