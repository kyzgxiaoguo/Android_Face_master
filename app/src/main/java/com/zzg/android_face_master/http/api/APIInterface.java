package com.zzg.android_face_master.http.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2019/10/24
 */
public interface APIInterface {
    /**
     * 人脸对比
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    腾讯
//    @POST("/fcgi-bin/face/face_facecompare")
//    百度
    @POST("/rest/2.0/face/v3/match")
    Call<ResponseBody> contrast(@Body RequestBody arg0);
}