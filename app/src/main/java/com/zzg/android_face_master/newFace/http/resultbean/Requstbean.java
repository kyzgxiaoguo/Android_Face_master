package com.zzg.android_face_master.newFace.http.resultbean;

/**
 * @author Zhangzhenguo
 * @create 2020/1/7
 * @Email 18311371235@163.com
 * @Describe
 */
public class Requstbean{
    public int app_id=2127336491;
    public int time_stamp=10000;
    private String nonce_str;
    private String sign;
    private String image_a;
    private String image_b;

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public int getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(int time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getImage_a() {
        return image_a;
    }

    public void setImage_a(String image_a) {
        this.image_a = image_a;
    }

    public String getImage_b() {
        return image_b;
    }

    public void setImage_b(String image_b) {
        this.image_b = image_b;
    }
}
