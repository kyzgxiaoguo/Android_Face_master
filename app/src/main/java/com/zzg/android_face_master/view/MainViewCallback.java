package com.zzg.android_face_master.view;

import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.List;

/**
 * @author Zhangzhenguo
 * @create 2019/12/26
 * @Email 18311371235@163.com
 * @Describe
 */
public interface MainViewCallback {
    void success(Bitmap bitmap1, Bitmap bitmap, List<FaceDetector.Face> face, int realFaceNum, SurfaceHolder surfaceHolder);
    void error(String err);
}
