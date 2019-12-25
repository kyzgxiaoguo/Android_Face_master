package com.hg.orcdiscern.http.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.hg.orcdiscern.http.resultbean.RequestHttpCallback;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpRequestUtil {
    private static HttpRequestUtil mHttpRequestUtil;
    private static RequestBody mBody;
    private static HttpDialogUtil mHttpDialogUtil;
    private static HttpCheckNetWorkUtil mHttpCheckNetWorkUtil;
    private static Context mContext;
    private static ProgressDialog mProgressDialog;
    /**
     * 无参构造
     */
    public HttpRequestUtil(){
    }
    /**
     * 有参构造
     * @param httpRequestUtil
     */
    public HttpRequestUtil(HttpRequestUtil httpRequestUtil){
        this.mHttpRequestUtil=httpRequestUtil;
    }

    /**
     * 静态单例模式获取httpRequestUtil对象
     * mHttpRequestUtil网络请求工具
     * mHttpCheckNetWorkUtil网络检查工具
     * mHttpDialogUtil网络请求进度条工具
     * @param context
     * @return
     */
    public static HttpRequestUtil getInstance(Context context){
        mContext=context;
        if (mHttpRequestUtil==null){
            mHttpRequestUtil=new HttpRequestUtil();
        }
        if (mHttpCheckNetWorkUtil==null){
            mHttpCheckNetWorkUtil=new HttpCheckNetWorkUtil();
        }
        if (mHttpDialogUtil==null){
            mHttpDialogUtil=HttpDialogUtil.getInstance(mContext);
        }
        return mHttpRequestUtil;
    }

    /**
     * 静态单例模式获取requestBody对象
     * @param file
     * @return
     */
    public static RequestBody getRequeBody(byte [] file){
        mBody= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), file);
        return mBody;
    }
    /**
     * 根据上传数据生成body对象传入请求队列中
     * 检查网络，网络进度条，请求传参，网络回调接口
     * @param progreDialogMessage  进度条提示语
     * @param file  传参（可根据自己需求进行修改）
     * @param httpCallback  请求成功失败回调
     */
    public void reqBodyByFileRequest(String progreDialogMessage, byte [] file,RequestHttpCallback httpCallback){
        if (mHttpCheckNetWorkUtil.checkNotWorkAvailable(mContext)) {
            mHttpDialogUtil.showProgressDialog(progreDialogMessage);
            mBody= getRequeBody(file);
            RetrofitHttp.getRetrofit().classification(mBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        String str = null;
                        try {
                            str = response.body().string();
                            httpCallback.onResponse(str,mHttpDialogUtil);
//                        Log.d("执行success==", str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        httpCallback.onFailure(2,mHttpDialogUtil);
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //1请求失败，原因如下:（请求无响应，请求不到服务器）2请求后返回数据为null
                    httpCallback.onFailure(1,mHttpDialogUtil);
                }
            });
        }else {
            httpCallback.onToast("当前无网络，请检查");
        }
    }
}
