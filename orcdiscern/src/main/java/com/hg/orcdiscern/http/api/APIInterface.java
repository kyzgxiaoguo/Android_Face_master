package com.hg.orcdiscern.http.api;

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
    public static String POSTADDRESS="/icr/recognize_id_card";


    /**
     * 接口总入口
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST("/icr/v3/recognize_id_card?head_portrait=0&id_number_image=0&crop_image=0")
    @POST(POSTADDRESS+"?head_portrait=0&id_number_image=0&crop_image=0")
    Call<ResponseBody> publics(@Body RequestBody arg0);

    /**
     * 证件分类
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST("/icr/v3/recognize_id_card?head_portrait=0&id_number_image=0&crop_image=0")
    @POST("/icr/recognize_id_card?head_portrait=0&id_number_image=0&crop_image=0")
    Call<ResponseBody> classification(@Body RequestBody arg0
    );
    /**
     * 银行卡
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST("/icr/v3/recognize_bank_card?head_portrait=0&id_number_image=0&crop_image=0")
    @POST("/icr/recognize_bank_card?head_portrait=0&id_number_image=0&crop_image=0")
    Call<ResponseBody> recognize_bank_card(@Body RequestBody arg0);
    /**
     * 营业执照
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST("/icr/v3/recognize_biz_license?head_portrait=0&id_number_image=0&crop_image=0")
    @POST("/icr/recognize_biz_license?head_portrait=0&id_number_image=0&crop_image=0")
    Call<ResponseBody> recognize_biz_license(@Body RequestBody arg0);
    /**
     * 护照
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST("/icr/v3/recognize_passport?head_portrait=0&id_number_image=0&crop_image=0")
    @POST("/icr/recognize_passport?head_portrait=0&id_number_image=0&crop_image=0")
    Call<ResponseBody> recognize_passport(@Body RequestBody arg0);
    /**
     * 港澳台居民来往内地通行证
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST("/icr/v3/recognize_taiwan_compatriot?head_portrait=0&id_number_image=0&crop_image=0")
    @POST("/icr/recognize_taiwan_compatriot?head_portrait=0&id_number_image=0&crop_image=0")
    Call<ResponseBody> recognize_taiwan_compatriot(@Body RequestBody arg0);
    /**
     * 港澳台通行证
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST("/icr/v3/recognize_hk_macao_tai_passport?head_portrait=0&id_number_image=0&crop_image=0")
    @POST("/icr/recognize_hk_macao_tai_passport?head_portrait=0&id_number_image=0&crop_image=0")
    Call<ResponseBody> recognize_hk_macao_tai_passport(@Body RequestBody arg0);
}