package com.zzg.android_face_master.base;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zzg.android_face_master.newFace.http.util.AlertDialogUtil;

import io.reactivex.functions.Consumer;

/**
 * @author Zhangzhenguo
 * @create 2019/10/24
 * @Email 18311371235@163.com
 * @Describe
 */
public class BaseActivity extends AppCompatActivity implements AlertDialogUtil {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        /* 不允许横竖屏切换 */
        setRequestedOrientation(1);
    }


////    设置通知栏
//    public  void setNotification() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.newTitleBackground);//通知栏所需颜色
//    }
//    @TargetApi(19)
//    private void setTranslucentStatus(boolean on) {
//        Window win = getWindow();
//        WindowManager.LayoutParams winParams = win.getAttributes();
//        final
//        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        if (on) {
//            winParams.flags |= bits;
//        } else {
//            winParams.flags &= ~bits;
//        }
//        win.setAttributes(winParams);
//    }
    private void checkPermissions(){
        final RxPermissions rxPermissions=new RxPermissions(BaseActivity.this);
        rxPermissions.requestEachCombined(
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        ).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted){
                    //权限全部通过
                }else if (permission.shouldShowRequestPermissionRationale){
                    //当有遗漏的权限继续提醒用户开启，防止内部使用时出现异常崩溃
                    checkPermissions();
                }else {
//                    跳转到设置
                }
            }
        });
    }

}
