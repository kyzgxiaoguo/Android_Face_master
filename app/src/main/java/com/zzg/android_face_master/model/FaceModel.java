package com.zzg.android_face_master.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.zzg.android_face_master.view.MainViewCallback;

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
    public void success(Bitmap bitmap) {
        Log.d("执行","Model");
        mainViewCallback.success(bitmap);
    }

    @Override
    public void error(String err) {
        Log.d("执行","Model");
        mainViewCallback.error(err);
    }

}
