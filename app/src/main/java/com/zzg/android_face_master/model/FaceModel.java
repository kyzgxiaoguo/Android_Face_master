package com.zzg.android_face_master.model;

import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.util.Log;
import android.view.SurfaceHolder;

import com.zzg.android_face_master.view.MainViewCallback;

import java.util.List;

/**
 * @author Zhangzhenguo
 * @create 2019/12/26
 * @Email 18311371235@163.com
 * @Describe
 */
public class FaceModel implements FaceModelCallback {

    private MainViewCallback mainViewCallback;

    public FaceModel(MainViewCallback mainViewCallback){
        this.mainViewCallback=mainViewCallback;
    }


    @Override
    public void success(Bitmap bitmap1, Bitmap bitmap, List<FaceDetector.Face> faces, int realFaceNum, SurfaceHolder surfaceHolder) {
        Log.d("执行","Model");
        mainViewCallback.success(bitmap1,bitmap,faces,realFaceNum,surfaceHolder);
    }

    @Override
    public void error(String err) {
        Log.d("执行","Model");
        mainViewCallback.error(err);
    }

}
