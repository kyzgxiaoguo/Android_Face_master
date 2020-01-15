package com.zzg.android_face_master.util;

import android.hardware.Camera;
import android.util.Log;

/**
 * @author Zhangzhenguo
 * @create 2020/1/6
 * @Email 18311371235@163.com
 * @Describe
 */

public class CameraErrorCallback implements Camera.ErrorCallback {

    private static final String TAG = "CameraErrorCallback";

    @Override
    public void onError(int error, Camera camera) {
        Log.e(TAG, "Encountered an unexpected camera error: " + error);
    }
}