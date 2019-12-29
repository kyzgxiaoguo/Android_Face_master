package com.zzg.android_face_master.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.zzg.android_face_master.util.CameraUtil;
import com.zzg.android_face_master.view.MainViewCallback;

import java.io.ByteArrayOutputStream;

/**
 * @author Zhangzhenguo
 * @create 2019/12/25
 * @Email 18311371235@163.com
 * @Describe
 */
public class FaceTask extends AsyncTask{
    private byte[] mData;
    Camera mCamera;
    private static final String TAG = "CameraTag";
    private FaceModel model;
    private SurfaceHolder surfaceHolder;
    //构造函数
    public FaceTask(byte[] data, Camera camera, MainViewCallback mainViewCallback, SurfaceHolder surfaceHolder) {
        this.mData = data;
        this.mCamera = camera;
        this.surfaceHolder = surfaceHolder;

        this.model=new FaceModel(mainViewCallback);
    }


    @Override
    protected Object doInBackground(Object[] params) {
        Camera.Parameters parameters = mCamera.getParameters();
        int imageFormat = parameters.getPreviewFormat();
        int w = parameters.getPreviewSize().width;
        int h = parameters.getPreviewSize().height;

        Rect rect = new Rect(0, 0, w, h);
        YuvImage yuvImg = new YuvImage(mData, imageFormat, w, h, null);
        try {
            ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
            yuvImg.compressToJpeg(rect, 100, outputstream);
            byte[] byteArray = outputstream.toByteArray();
            detectionFaces(byteArray);
        }
        catch (Exception e) {
            model.error(e.getLocalizedMessage());
            Log.e(TAG, "onPreviewFrame: 获取相机实时数据失败" + e.getLocalizedMessage());
        }
        return null;
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
        ;
        switch(CameraUtil.orientionOfCamera) {
            case 0:
                detector =new FaceDetector(width,height,2);
                matrix.postRotate(0.0f,width /2,height /2);
                // 以指定的宽度和高度创建一张可变的bitmap（图片格式必须是RGB_565，不然检测不到人脸）
                bitmap2 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
                break;
            case 90:
                detector =new FaceDetector(height,width,2);
                matrix.postRotate(-270.0f,height /2,width /2);
                bitmap2 = Bitmap.createBitmap(height,width,Bitmap.Config.RGB_565);
                break;
            case 180:
                detector =new FaceDetector(width,height,2);
                matrix.postRotate(-180.0f,width /2,height /2);
                bitmap2 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
                break;
            case 270:
                detector =new FaceDetector(height,width,2);
                matrix.postRotate(-90.0f,height /2,width /2);
                bitmap2 = Bitmap.createBitmap(height,width,Bitmap.Config.RGB_565);
                break;
            default:
                break;
        }
        FaceDetector.Face [] faces=new FaceDetector.Face[10];
        Paint paint =new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);

        Canvas canvas =new Canvas();
        canvas.setBitmap(bitmap2);
        canvas.setMatrix(matrix);
//         将bitmap1画到bitmap2上（这里的偏移参数根据实际情况可能要修改）
        canvas.drawBitmap(bitmap1,0,0,paint);
        int faceNumber = detector.findFaces(bitmap2, faces);
        Log.e("---------->","faceNumber:"+faceNumber+"faces.size:"+faces.length);
        Log.i(TAG, "onPreviewFrame: rawbitmap:" + bitmap2.toString());
        if(faceNumber!=0) {
            model.success(bitmap1,bitmap2,faces,faceNumber);
        }

        //若要存储可以用下列代码，格式为jpg
            /* BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/fp.jpg"));
            img.compressToJpeg(rect, 100, bos);
            bos.flush();
            bos.close();
            mCamera.startPreview();
            */

//        canvas.drawRect((int) ((mWidth-midPoint.x) - (eyesDistance*2+eyesDistance/2)),
//                (int) (midPoint.y-eyesDistance)+210,
//         (int) ( (mWidth-midPoint.x) + (eyesDistance/3)),
//         (int) (midPoint.y+(eyesDistance*2+eyesDistance/2))+210,paint);
    }
}
