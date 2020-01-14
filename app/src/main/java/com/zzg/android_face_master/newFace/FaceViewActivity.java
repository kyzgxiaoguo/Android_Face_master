package com.zzg.android_face_master.newFace;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zzg.android_face_master.R;
import com.zzg.android_face_master.base.BaseActivity;
import com.zzg.android_face_master.newFace.adapter.ImagePreviewAdapter;
import com.zzg.android_face_master.newFace.bean.CameraConfig;
import com.zzg.android_face_master.newFace.bean.FaceResultData;
import com.zzg.android_face_master.newFace.http.baidu.HttpUtil;
import com.zzg.android_face_master.newFace.http.resultbean.Requstbean;
import com.zzg.android_face_master.newFace.http.util.AuthService;
import com.zzg.android_face_master.newFace.http.util.HttpCheckNetWorkUtil;
import com.zzg.android_face_master.newFace.util.Base64Utils;
import com.zzg.android_face_master.newFace.util.BitmapUtils;
import com.zzg.android_face_master.newFace.util.CameraErrorCallback;
import com.zzg.android_face_master.newFace.util.ImageUtils;
import com.zzg.android_face_master.newFace.view.CameraView;
import com.zzg.android_face_master.newFace.view.CircleSurfaceView;
import com.zzg.android_face_master.newFace.view.FaceOverlayView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaceViewActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    @BindView(R.id.mSurfaceview)
    CircleSurfaceView mSurfaceview;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.btOpenGraph)
    Button btOpenGraph;
    @BindView(R.id.btContrast)
    Button btContrast;
    @BindView(R.id.ivImageA)
    ImageView ivImageA;
    @BindView(R.id.ivImageB)
    ImageView ivImageB;
    @BindView(R.id.btCheck)
    Button btCheck;

    public static final String TAG = FaceViewActivity.class.getSimpleName();

    // 人脸跟踪视图
    private FaceOverlayView mFaceView;

    // 摄像头error回调
    private final CameraErrorCallback mErrorCallback = new CameraErrorCallback();

    //    人脸个数最大
    private static final int MAX_FACE = 10;
    private Handler handler;
    private FaceDetectThread detectThread = null;
    private FaceDetector mDetector;

    private byte[] grayBuff;
    private int bufflen;
    private int[] rgbs;

    //    存储检测人脸数据
    private FaceResultData faces[];
    private FaceResultData faces_previous[];
    private int Id = 0;

    private String BUNDLE_CAMERA_ID = "camera";

    //  显示适配器
//    人脸截图后保存并显示
    private HashMap<Integer, Integer> facesCount = new HashMap<>();
    private ImagePreviewAdapter imagePreviewAdapter;
    private ArrayList<Bitmap> facesBitmap;

    Context mContext = FaceViewActivity.this;
    private ProgressDialog mProgressDialog;
    private Bitmap bitmapA;
    private Bitmap bitmapB;

    private String access_token = "";

    private boolean isHide = false;
    private CameraView mCamera;
    private int cameraId = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_view);
        ButterKnife.bind(this);
        listener();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();
    }

    private void init() {
        // 实例化人脸跟踪视图
        mFaceView = new FaceOverlayView(this);
        addContentView(mFaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//        实例化摄像头
        mCamera=new CameraView();
        mCamera.setCamera(mFaceView,FaceViewActivity.this);

        handler = new Handler();
//        设置最大检测数
        faces = new FaceResultData[MAX_FACE];
        faces_previous = new FaceResultData[MAX_FACE];
        for (int i = 0; i < MAX_FACE; i++) {
            faces[i] = new FaceResultData();
            faces_previous[i] = new FaceResultData();
        }
        startPreview();
    }

    //    当当前activity加载完成后调用
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        获取holder实例添加holder回调
        SurfaceHolder holder = mSurfaceview.getHolder();
        holder.addCallback(this);
    }

    private void listener() {
        //拍照
        btOpenGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(jpeg);
            }
        });
        btContrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upIDData(bitmapB);
                if (mCamera.mCamera != null) {
                    mCamera.stopPreview();
                }
            }
        });
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHide){
                    isHide=false;
                    startPreview();
                }else {
                    isHide=true;
                    if (mCamera.mCamera != null) {
                        mCamera.stopPreview();
                    }
                }

            }
        });
    }

    /**
     * 获取图片
     */
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data == null) {
                return;
            }
            // 拍照回掉回来的 图片数据。
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap bm;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Matrix matrix = new Matrix();
                matrix.setRotate(90, 0.1f, 0.1f);
                //前置摄像头旋转图片270度。
                matrix.setRotate(270);
                bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            } else {
                bm = bitmap;
            }
//            //图片缩放到341x481大小
            Bitmap scaleBitmap=Bitmap.createScaledBitmap(bm,mSurfaceview.getWidth(),mSurfaceview.getHeight(),false);
//                //获取取景框内的图片
//            bitmap = BitmapUtils.getRectBitmap(mCenterRect, bm, mPoint);

            ivImageA.setImageBitmap(scaleBitmap);
        }
    };

    /**
     * 人脸对比
     * @param bitmap
     */
    private void upIDData(Bitmap bitmap) {
        String params = "";
        HttpCheckNetWorkUtil checkNetWord = new HttpCheckNetWorkUtil();
        if (!checkNetWord.checkNotWorkAvailable(mContext)) {
            Toast.makeText(mContext, "请检查网络后再重试。", Toast.LENGTH_SHORT).show();
            return;
        }
//        String baseA = Base64Utils.imageToBase64(bitmapA);
//        String strA=baseA.replaceAll("\r|\n","");
//
//        Bitmap bitmap1 = ImageUtils.compressImage(bitmap);
////        ByteArrayOutputStream baosB=new ByteArrayOutputStream();
////        bitmap1.compress(Bitmap.CompressFormat.PNG,100,baosB);
////        byte [] bytesB=baosB.toByteArray();
//        ivImageB.setImageBitmap(bitmap1);
//        String baseB = Base64Utils.imageToBase64(bitmap1);
//        String strB=baseB.replaceAll("\r|\n","");
//
//        String nonce_str = ImageUtils.getUUIDByRules("123456789633ABCD");
//        Map<String, Object> map = new HashMap<>();
//        map.put("app_id", 2127336491);
//        map.put("time_stamp", 10000);
//        map.put("nonce_str", nonce_str);
////        map.put("sign","");
//        map.put("image_a", strA);
//        map.put("image_b", strB);
//        String sign = ImageUtils.getMD5(ImageUtils.SortUtil(map, "UTF-8", true) + "&app_key=W6jiRDDhr1XGG5mW");
//        Log.d("执行", ImageUtils.SortUtil(map, "UTF-8", true) + "&app_key=W6jiRDDhr1XGG5mW");
//        Log.d("执行-Sign", sign);
//
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("app_id", 2127336491);
//        map1.put("time_stamp", 10000);
//        map1.put("nonce_str", nonce_str);
//        map1.put("sign", sign);
//        map1.put("image_a", strA);
//        map1.put("image_b", strB);
//
//        String json = JSON.toJSONString(map1);
//        Log.d("执行-commit", json);

        ByteArrayOutputStream baosA = new ByteArrayOutputStream();
        bitmapA.compress(Bitmap.CompressFormat.PNG, 100, baosA);
        String baseA = Base64Utils.encode(baosA.toByteArray());
        try {
            //释放流
            baosA.flush();
            baosA.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Bitmap bitmap1 = ImageUtils.compressImage(bitmap);
        ByteArrayOutputStream baosB = new ByteArrayOutputStream();
        bitmapB.compress(Bitmap.CompressFormat.PNG, 100, baosB);
        String baseB = Base64Utils.encode(baosB.toByteArray());
        try {
            //释放流
            baosB.flush();
            baosB.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Requstbean> beanS = new ArrayList<>();
        Requstbean bean = new Requstbean();
        bean.setImage(baseA);
        bean.setImage_type("BASE64");
        bean.setFace_type("LIVE");
        bean.setQuality_control("LOW");
        bean.setLiveness_control("HIGH");
        beanS.add(bean);

        Requstbean bean1 = new Requstbean();
        bean1.setImage(baseB);
        bean1.setImage_type("BASE64");
        bean1.setFace_type("LIVE");
        bean1.setQuality_control("LOW");
        bean1.setLiveness_control("HIGH");
        beanS.add(bean1);
        String json = JSON.toJSONString(beanS);


        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在对比中...");
        mProgressDialog.show();
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
        new Thread() {
            @Override
            public void run() {
                super.run();
                access_token = AuthService.getAuth();
                Log.d("access_token:", access_token);
                try {
                    String result = HttpUtil.post(url, access_token, "application/json", json);
                    System.out.println(result);
                    JSONObject jsonObject=JSON.parseObject(result);
                    mProgressDialog.dismiss();
                    if (jsonObject.getString("error_code").equals("0")){

                    }else {
                        showAlertDialog(mContext, "对比结果为：", result, "确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
//        /**
//         * 设置请求头并添加数据
//         * @param content
//         * @return
//         */
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
//        RetrofitHttp.getRetrofit().contrast(body).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.body() != null) {
//                    String str = null;
//                    try {
//                        str = response.body().string();
////                        Log.d("执行success==", str);
//                        showAlertDialog(mContext, "对比结果为：", str, "确认", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        mProgressDialog.dismiss();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("执行error==", "网络请求失败" + call.toString());
//                Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
//                mProgressDialog.dismiss();
//            }
//        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                super.onBackPressed();
//                return true;
//
//            case R.id.switchCam:
//
//                if (numberOfCameras == 1) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("Switch Camera").setMessage("Your device have one camera").setNeutralButton("Close", null);
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                    return true;
//                }
//
//                cameraId = (cameraId + 1) % numberOfCameras;
//                recreate();
//
//                return true;
//
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        if (mCamera.mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetData();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_CAMERA_ID, cameraId);
    }

    //    创建并显示时调用
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        resetData();
        mCamera.openCamera(CameraConfig.CAMERA_FACING_FRONT);

        try {
            //将SurfaceView连接到相机
            mCamera.setPreviewDisplay(mSurfaceview.getHolder());
        } catch (Exception e) {
            Log.e(TAG, "Could not preview the image.", e);
        }
    }

    //    当旋转或尺寸发生变化时调用
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        // 判断如果没有画面直接返回
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        // 停止预览
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.d(TAG,e.getMessage());
        }
//        设置图片预览位置、自动对焦
        mCamera.configureCamera(width, height);
        mCamera.setDisplayOrientation();
        mCamera.setErrorCallback(mErrorCallback);

        // 创建人脸识别
        float aspect = (float) CameraConfig.previewHeight / (float) CameraConfig.previewWidth;
        mDetector = new FaceDetector(CameraConfig.prevSettingWidth, (int) (CameraConfig.prevSettingWidth * aspect), MAX_FACE);

        bufflen = CameraConfig.previewWidth * CameraConfig.previewHeight;
        grayBuff = new byte[bufflen];
        rgbs = new int[bufflen];

        // 实时预览摄像头图像数据
        startPreview();
    }



    //    开始预览-显示实时摄像头图像
    private void startPreview() {
        if (mCamera.mCamera != null) {
            CameraConfig.isThreadWorking = false;
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
            CameraConfig.counter = 0;
        }
    }


    //    当关闭隐藏时调用
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.setPreviewCallbackWithBuffer();
        mCamera.setErrorCallback(null);
        mCamera.release();
        mCamera.mCamera = null;
    }


    long start, end;

    double fps;

    //相机实时数据检测
    @Override
    public void onPreviewFrame(byte[] _data, Camera _camera) {
        if (!CameraConfig.isThreadWorking) {
            if (CameraConfig.counter == 0)
                start = System.currentTimeMillis();

            CameraConfig.isThreadWorking = true;
            waitForFdetThreadComplete();
            detectThread = new FaceDetectThread(handler, this);
            detectThread.setData(_data);
            detectThread.start();
        }
    }

    private void waitForFdetThreadComplete() {
        if (detectThread == null) {
            return;
        }

        if (detectThread.isAlive()) {
            try {
                detectThread.join();
                detectThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Do face detect in thread
     */
    private class FaceDetectThread extends Thread {
        private Handler handler;
        private byte[] data = null;
        private Context ctx;
        private Bitmap faceCroped;

        public FaceDetectThread(Handler handler, Context ctx) {
            this.ctx = ctx;
            this.handler = handler;
        }


        public void setData(byte[] data) {
            this.data = data;
        }

        @Override
        public void run() {
            float aspect = (float) CameraConfig.previewHeight / (float) CameraConfig.previewWidth;
            int w = CameraConfig.prevSettingWidth;
            int h = (int) (CameraConfig.prevSettingWidth * aspect);

            ByteBuffer bbuffer = ByteBuffer.wrap(data);
            bbuffer.get(grayBuff, 0, bufflen);

            gray8toRGB32(grayBuff, CameraConfig.previewWidth, CameraConfig.previewHeight, rgbs);
            Bitmap bitmap = Bitmap.createBitmap(rgbs, CameraConfig.previewWidth, CameraConfig.previewHeight, Bitmap.Config.RGB_565);

            Bitmap bmp = Bitmap.createScaledBitmap(bitmap, w, h, false);

            float xScale = (float) CameraConfig.previewWidth / (float) CameraConfig.prevSettingWidth;
            float yScale = (float) CameraConfig.previewHeight / (float) h;

            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            int rotate = CameraConfig.mDisplayOrientation;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && CameraConfig.mDisplayRotation % 180 == 0) {
                if (rotate + 180 > 360) {
                    rotate = rotate - 180;
                } else
                    rotate = rotate + 180;
            }
            switch (rotate) {
                case 90:
                    bmp = ImageUtils.rotate(bmp, 90);
                    xScale = (float) CameraConfig.previewHeight / bmp.getWidth();
                    yScale = (float) CameraConfig.previewWidth / bmp.getHeight();
                    break;
                case 180:
                    bmp = ImageUtils.rotate(bmp, 180);
                    break;
                case 270:
                    bmp = ImageUtils.rotate(bmp, 270);
                    xScale = (float) CameraConfig.previewHeight / (float) h;
                    yScale = (float) CameraConfig.previewWidth / (float) CameraConfig.prevSettingWidth;
                    break;
            }



            mDetector = new FaceDetector(bmp.getWidth(), bmp.getHeight(), MAX_FACE);

            FaceDetector.Face[] fullResults = new FaceDetector.Face[MAX_FACE];
            mDetector.findFaces(bmp, fullResults);

            for (int i = 0; i < MAX_FACE; i++) {
                if (fullResults[i] == null) {
                    faces[i].clear();
                } else {
                    PointF mid = new PointF();
                    fullResults[i].getMidPoint(mid);

                    mid.x *= xScale;
                    mid.y *= yScale;

                    float eyesDis = fullResults[i].eyesDistance() * xScale;
                    float confidence = fullResults[i].confidence();
                    float pose = fullResults[i].pose(FaceDetector.Face.EULER_Y);
                    int idFace = Id;

                    Rect rect = new Rect(
                            (int) (mid.x - eyesDis * 1.20f),
                            (int) (mid.y - eyesDis * 0.55f),
                            (int) (mid.x + eyesDis * 1.20f),
                            (int) (mid.y + eyesDis * 1.85f));

                    /**
                     * Only detect face size > 100x100
                     */
                    if (rect.height() * rect.width() > 100 * 100) {
                        // Check this face and previous face have same ID?
                        for (int j = 0; j < MAX_FACE; j++) {
                            float eyesDisPre = faces_previous[j].eyesDistance();
                            PointF midPre = new PointF();
                            faces_previous[j].getMidPoint(midPre);

                            RectF rectCheck = new RectF(
                                    (midPre.x - eyesDisPre * 1.5f),
                                    (midPre.y - eyesDisPre * 1.15f),
                                    (midPre.x + eyesDisPre * 1.5f),
                                    (midPre.y + eyesDisPre * 1.85f));

                            if (rectCheck.contains(mid.x, mid.y) && (System.currentTimeMillis() - faces_previous[j].getTime()) < 1000) {
                                idFace = faces_previous[j].getId();
                                break;
                            }
                        }

                        if (idFace == Id) Id++;

                        faces[i].setFace(idFace, mid, eyesDis, confidence, pose, System.currentTimeMillis());

                        faces_previous[i].set(faces[i].getId(), faces[i].getMidEye(), faces[i].eyesDistance(), faces[i].getConfidence(), faces[i].getPose(), faces[i].getTime());

                        /**
                         * 如果聚焦在面5帧->在mRecyclerView中拍摄面显示
                         * 因为有些第一帧的质量不好
                         */
                        if (facesCount.get(idFace) == null) {
                            facesCount.put(idFace, 0);
                        } else {
                            int count = facesCount.get(idFace) + 1;
                            if (count <= 5)
                                facesCount.put(idFace, count);

                            // 复制图像bitmap到新bitmap中
                            if (count == 5) {
                                faceCroped = ImageUtils.cropFace(faces[i], bitmap, rotate);
                                if (faceCroped != null) {
                                    bitmapB = faceCroped;
                                    handler.post(new Runnable() {
                                        public void run() {
                                            imagePreviewAdapter.add(faceCroped);
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    //send face to FaceView to draw rect
                    mFaceView.setFaces(faces);

                    //Calculate FPS (Detect Frame per Second)
                    end = System.currentTimeMillis();
                    CameraConfig.counter++;
                    double time = (double) (end - start) / 1000;
                    if (time != 0)
                        fps = CameraConfig.counter / time;

                    mFaceView.setFPS(fps);

                    if (CameraConfig.counter == (Integer.MAX_VALUE - 1000))
                        CameraConfig.counter = 0;

                    CameraConfig.isThreadWorking = false;
                }
            });
        }

        private void gray8toRGB32(byte[] gray8, int width, int height, int[] rgb_32s) {
            final int endPtr = width * height;
            int ptr = 0;
            while (true) {
                if (ptr == endPtr)
                    break;

                final int Y = gray8[ptr] & 0xff;
                rgb_32s[ptr] = 0xff000000 + (Y << 16) + (Y << 8) + Y;
                ptr++;
            }
        }
    }

    /**
     * 识别列表
     */
    private void resetData() {
        if (imagePreviewAdapter == null) {
            facesBitmap = new ArrayList<>();
            //        截图保存并显示
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            imagePreviewAdapter = new ImagePreviewAdapter(FaceViewActivity.this, facesBitmap, new ImagePreviewAdapter.ViewHolder.OnItemClickListener() {
                @Override
                public void onClick(View v, int position) {
                    imagePreviewAdapter.setCheck(position);
                    imagePreviewAdapter.notifyDataSetChanged();
                }
            });
            mRecyclerView.setAdapter(imagePreviewAdapter);
        } else {
            imagePreviewAdapter.clearAll();
        }
    }

}
