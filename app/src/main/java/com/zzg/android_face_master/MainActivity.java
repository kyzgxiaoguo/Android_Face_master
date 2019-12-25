package com.zzg.android_face_master;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
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
import com.luck.picture.lib.PictureSelector;
import com.zzg.android_face_master.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btSetFace)
    Button btSetFace;
    @BindView(R.id.btGetFace)
    Button btGetFace;
    @BindView(R.id.mSurFaceView)
    SurfaceView mSurfaceView;

    private Context mContext;
    private MainView mainView;

    private SurfaceHolder mSurfaceHolder;

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
        //设置缓冲类型
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //设置SurfaceView的分辨率
        mSurfaceView.getHolder().setFixedSize(1920, 1080);
        //设置屏幕长亮
        mSurfaceView.getHolder().setKeepScreenOn(true);
        mSurfaceView.getHolder().addCallback(new SurfaceCallback());

    }

    private void initData() {

    }

    private void listener() {
        btSetFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainView.openPopupWindowDialog(v);
            }
        });
    }
    class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d("执行","surfaceCreated");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (holder != null) {

            }
            Log.d("执行","surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d("执行","surfaceDestroyed");
        }
    }

    /**
     * 检测摄像头
     */
    private boolean checkCameraHardware(Context context){
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast.makeText(context, "检测到设备支持摄像头", Toast.LENGTH_SHORT).show();
            return true;
        }else {
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
}
