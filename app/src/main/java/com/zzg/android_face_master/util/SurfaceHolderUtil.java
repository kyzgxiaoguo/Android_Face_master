package com.zzg.android_face_master.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zzg.android_face_master.holder.SurfaceViewCallback;

/**
 * @author Zhangzhenguo
 * @create 2019/12/26
 * @Email 18311371235@163.com
 * @Describe
 */
public class SurfaceHolderUtil {
     Context mContext;
     SurfaceHolder mSurfaceHolder;
     SurfaceView mSurfaceView;
     SurfaceViewCallback mCallback = new SurfaceViewCallback();
     private int isFrontOrArount=-1;

    /**
     * 设置相机界面SurfaceView的Holder
     * @param context 从相机所在的Activity传入的context
     * @param surfaceView Holder所绑定的响应的SurfaceView
     * */
    public void setSurfaceHolders(Context context, SurfaceView surfaceView,int isFrontOrArount) {
        this.mContext = context;
        this.mSurfaceView = surfaceView;
        mCallback.setContext(context,isFrontOrArount);
        //实例化SurfaceHolder对象
        mSurfaceHolder = surfaceView.getHolder();
        //设置SurfaceView分辨率
        mSurfaceHolder.setFixedSize(1920,1080);
        //设置SurfaceView的缓冲类型
        mSurfaceHolder.setType(android.view.SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //设置屏幕长亮
        mSurfaceHolder.setKeepScreenOn(true);
        //添加CallBack回调事件
        mSurfaceHolder.addCallback(mCallback);
    }

    /**
     * 切换前后摄像头
     * @param isFrontOrArount
     */
    public void setIsFrontOrArount(int isFrontOrArount){
        mCallback.setSwitchFrontOrArount(isFrontOrArount);
    }

}
