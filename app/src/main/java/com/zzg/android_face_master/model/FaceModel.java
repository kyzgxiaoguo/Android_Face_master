package com.zzg.android_face_master.model;

import android.graphics.Bitmap;

import com.zzg.android_face_master.model.FaceCallback;
import com.zzg.android_face_master.view.MainViewCallback;

/**
 * @author Zhangzhenguo
 * @create 2019/12/26
 * @Email 18311371235@163.com
 * @Describe
 */
public class FaceModel implements FaceCallback {

    private MainViewCallback mainViewCallback;
    private FaceCallback faceCallback;
    @Override
    public void success(Bitmap bitmap) {
        mainViewCallback.success(bitmap);
    }

    @Override
    public void error(String err) {
        mainViewCallback.error(err);
    }
    public void setFaceTaskCallback(FaceCallback faceCallback){
        this.faceCallback=faceCallback;
    }
}
