package com.zzg.android_face_master;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hg.orcdiscern.bean.LocalMediaa;
import com.hg.orcdiscern.presenter.PictureConfigs;
import com.hg.orcdiscern.presenter.PictureSelectors;
import com.hg.orcdiscern.util.ImageUtils;
import com.hg.orcdiscern.view.MainView;
import com.zzg.android_face_master.base.BaseActivity;
import com.zzg.android_face_master.model.FaceModel;
import com.zzg.android_face_master.presenter.SurfaceHolders;
import com.zzg.android_face_master.view.MainViewCallback;
import com.zzg.android_face_master.view.MainViews;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainViewCallback {

    @BindView(R.id.btSetFace)
    Button btSetFace;
    @BindView(R.id.btGetFace)
    Button btGetFace;
    @BindView(R.id.mSurFaceView)
    SurfaceView mSurfaceView;
    @BindView(R.id.mSurFaceView1)
    SurfaceView mSurFaceView1;

    private Context mContext;
    private MainView mainView;


    private SurfaceHolders mSurfaceHolders;
    private boolean isFrontOrArount = false;
    private FaceModel faceModel;
    private MainViews mainViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
        listener();
    }

    private void initView() {
        mContext = MainActivity.this;
        mainView = MainView.getInstance(MainActivity.this, MainActivity.this);
        mSurfaceHolders = new SurfaceHolders();
        mSurfaceHolders.setSurfaceHolders(mContext, mSurfaceView, Camera.CameraInfo.CAMERA_FACING_FRONT,this);
        mainViews=new MainViews();
    }

    private void initData() {

    }

    private void listener() {
        btSetFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCameraHardware(mContext)) {
                    Toast.makeText(mContext, ",抱歉此设备暂不支持摄像功能。", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        btGetFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCameraHardware(mContext)) {
                    Toast.makeText(mContext, ",抱歉此设备暂不支持摄像功能。", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    /**
     * 检测摄像头
     */
    private boolean checkCameraHardware(Context context) {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(context, "检测到设备支持摄像头", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "检测到设备不支持摄像头", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    /**
     * 返回图片信息
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("执行", "onActivityResult");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfigs.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMediaa> selectList = PictureSelectors.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    Log.d("执行", selectList.get(0).getPath());
                    try {
                        byte[] b = ImageUtils.readStream(selectList.get(0).getPath());

                        reqEntrance(b);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    PictureSelector.create(MainActivity.this).themeStyle(selectList.get(0).get).openExternalPreview(position, "/custom_file", selectList);
//                    PictureSelector.create(MainActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);
                    break;
            }
        }
    }

    /**
     * 请求入口，如需动态改变端口可进行Port.port执行修改
     *
     * @param file
     */
    private void reqEntrance(byte[] file) {

    }

    /**
     * 返回函数
     * @param bitmap
     */
    @Override
    public void success(Bitmap bitmap) {

    }

    @Override
    public void error(String err) {

    }
}
