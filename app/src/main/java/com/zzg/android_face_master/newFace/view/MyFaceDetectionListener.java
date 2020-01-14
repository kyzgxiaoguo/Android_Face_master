package com.zzg.android_face_master.newFace.view;

import android.hardware.Camera;
import android.util.Log;

/**
 * @author Zhangzhenguo
 * @create 2020/1/13
 * @Email 18311371235@163.com
 * @Describe 面部检测事件监听
 */
public class MyFaceDetectionListener implements Camera.FaceDetectionListener {

    @Override
    public void onFaceDetection(Camera.Face[] faces, Camera camera) {
        if (faces.length>0){
            Log.d("FaceDetection", "face detected: "+ faces.length +
                    " Face 1 Location X: " + faces[0].rect.centerX() +
                    "Y: " + faces[0].rect.centerY() );
        }
    }
}
