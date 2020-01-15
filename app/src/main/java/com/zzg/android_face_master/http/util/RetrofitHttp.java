package com.zzg.android_face_master.http.util;

import android.util.Log;

import com.zzg.android_face_master.http.api.APIInterface;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//import retrofit2.GsonConverterFactory;
//import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by Administrator on 2017/3/31 0022.
 * 单例模式
 */

public class RetrofitHttp {
//    private static String baseUrl = "https://api.ai.qq.com";
    private static String baseUrl = "https://aip.baidubce.com";
    private static APIInterface singleton;

    /**
     * 设置okhttp 对象
     */
    private static final OkHttpClient client = new OkHttpClient.Builder().
            addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Log.d("执行", "req==" + request.url().toString());
                    Log.d("执行", "res==" + request.body().toString());
                    Response proceed = chain.proceed(request);
                    return proceed;
                }
            }).
            connectTimeout(30, TimeUnit.SECONDS).
            readTimeout(30, TimeUnit.SECONDS).
            writeTimeout(30, TimeUnit.SECONDS).build();
    /**
     * 获取接口
     * @return
     */
    public static APIInterface getRetrofit() {
        synchronized (RetrofitHttp.class) {
            singleton = createRetrofit(client).create(APIInterface.class);
        }
        return singleton;
    }

    /**
     * 设置请求头并添加数据
     * @param content
     * @return
     */
    public static RequestBody initBody(byte [] content){
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),content);
        return body;
    }

    /**
     * 创建并获取请求对象
     * @param client
     * @return
     */
    private static Retrofit createRetrofit(OkHttpClient client) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

}
