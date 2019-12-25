package com.zzg.android_face_master.thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * @author Zhangzhenguo
 * @create 2019/12/25
 * @Email 18311371235@163.com
 * @Describe
 */
public class FaceTask extends AsyncTask {
    private byte[] mData;
    Camera mCamera;
    private static final String TAG = "CameraTag";
    //构造函数
    FaceTask(byte[] data , Camera camera)
    {
        this.mData = data;
        this.mCamera = camera;

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
            Bitmap rawbitmap = BitmapFactory.decodeByteArray(outputstream.toByteArray(), 0, outputstream.size());
            Log.i(TAG, "onPreviewFrame: rawbitmap:" + rawbitmap.toString());

            //若要存储可以用下列代码，格式为jpg
            /* BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/fp.jpg"));
            img.compressToJpeg(rect, 100, bos);
            bos.flush();
            bos.close();
            mCamera.startPreview();
            */
        }
        catch (Exception e)
        {
            Log.e(TAG, "onPreviewFrame: 获取相机实时数据失败" + e.getLocalizedMessage());
        }
        return null;
    }
}
