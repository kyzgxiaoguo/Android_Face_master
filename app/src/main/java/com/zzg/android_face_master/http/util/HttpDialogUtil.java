package com.zzg.android_face_master.http.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @author Zhangzhenguo
 * @create 2019/10/24
 * @Email 18311371235@163.com
 * @Describe
 */
public class HttpDialogUtil {
    private static ProgressDialog mProgressDialog;
    private static Context mContext;
    private static HttpDialogUtil mHttpDialogUtil;

    /**
     * 有参构造
     * @param mContext
     */
    public HttpDialogUtil(Context mContext){
        this.mContext=mContext;
        initProgressDialog();
    }

    /**
     * 实例化
     * @param mContext
     * @return
     */
    public static HttpDialogUtil getInstance(Context mContext){
        if (mHttpDialogUtil==null){
            mHttpDialogUtil=new HttpDialogUtil(mContext);
        }
        return mHttpDialogUtil;
    };

    /**
     * 创建并初始化
     * @return
     */
    public static ProgressDialog initProgressDialog(){
        if (mProgressDialog==null){
            mProgressDialog=new ProgressDialog(mContext);
        }
        return mProgressDialog;
    }
    /**
     * 创建并初始化
     * @param message
     * @return
     */
    public static void showProgressDialog(String message){
        if (mProgressDialog==null){
            mProgressDialog=new ProgressDialog(mContext);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    /**
     * 关闭弹框
     */
    public static void dismissProgressDialog(){
        if (mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }

}
