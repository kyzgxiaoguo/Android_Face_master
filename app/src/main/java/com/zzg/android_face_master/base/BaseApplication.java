package com.zzg.android_face_master.base;

import android.app.Application;
import android.content.Context;

/**
 * @author Zhangzhenguo
 * @create 2019/10/25
 * @Email 18311371235@163.com
 * @Describe
 */
public class BaseApplication extends Application {

    public static Context appContext;
    public static BaseApplication app;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        app = this;
    }

    public static Context getContext() {
        return appContext;
    }

    public static BaseApplication getApp() {
        return app;
    }
}
