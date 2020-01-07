package com.zzg.android_face_master.newFace.http.api;

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
    @POST("/fcgi-bin/face/face_facecompare")
    Call<ResponseBody> classification(@Body RequestBody arg0
    );
}