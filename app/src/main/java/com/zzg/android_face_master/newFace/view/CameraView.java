package com.zzg.android_face_master.newFace.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
 * @author Zhangzhenguo
 * @create 2020/1/9
 * @Email 18311371235@163.com
 * @Describe
 */
public class CameraView{
    private Camera mCamera=null;

    /**
     * 设置相机
     * @param camera
     */
    public void setCamera(Camera camera){
        this.mCamera=camera;
    }

    /**
     * 打开相机
     */
    public Camera openCamera(int around){
        int aroundA = -1;
        int aroundB = -1;
        Camera camera=null;
//        获取摄像头个数
        int numberOfCameras = mCamera.getNumberOfCameras();
//        获取摄像头详细信息，来决定使用前置或后置摄像头
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i=0;i<numberOfCameras;i++){
            Camera.getCameraInfo(i,cameraInfo);
            if (cameraInfo.facing== Camera.CameraInfo.CAMERA_FACING_BACK){
                aroundA=i;
            }else if (cameraInfo.facing== Camera.CameraInfo.CAMERA_FACING_FRONT){
                aroundB=i;
            }
        }
        if (around==0){
//            打开摄像头
            camera=Camera.open(aroundA);
        }else if (around==1){
            camera=Camera.open(aroundB);
        }
        return camera;
    }

    /**
     * 检查硬件设备
     * @param context
     * @return
     */
    public boolean checkCameraHardwre(Context context){
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        }else {
            return false;
        }
    }
}
