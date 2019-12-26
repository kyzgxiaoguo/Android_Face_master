package com.zzg.android_face_master.view;

import android.graphics.Bitmap;

/**
 * @author Zhangzhenguo
 * @create 2019/12/26
 * @Email 18311371235@163.com
 * @Describe
 */
public interface MainViewCallback {
    void success(Bitmap bitmap);
    void error(String err);
}
