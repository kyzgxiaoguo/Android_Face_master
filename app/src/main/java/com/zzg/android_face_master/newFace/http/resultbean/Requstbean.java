package com.zzg.android_face_master.newFace.http.resultbean;

/**
 * @author Zhangzhenguo
 * @create 2020/1/7
 * @Email 18311371235@163.com
 * @Describe
 */
public class Requstbean{
    public String image;
    public String image_type;
    private String face_type;
    private String quality_control;
    private String liveness_control;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    public String getFace_type() {
        return face_type;
    }

    public void setFace_type(String face_type) {
        this.face_type = face_type;
    }

    public String getQuality_control() {
        return quality_control;
    }

    public void setQuality_control(String quality_control) {
        this.quality_control = quality_control;
    }

    public String getLiveness_control() {
        return liveness_control;
    }

    public void setLiveness_control(String liveness_control) {
        this.liveness_control = liveness_control;
    }
}
