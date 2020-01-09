package com.zzg.android_face_master.newFace.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * @author Zhangzhenguo
 * @create 2020/1/9
 * @Email 18311371235@163.com
 * @Describe
 */
public class CircleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

//    注意：相机预览不必处于横屏模式。从 Android 2.2（API 级别 8）开始，您可使用 setDisplayOrientation() 方法设置预览图像的旋转。
//    为了在用户重定向手机时改变预览屏幕方向，请在您预览类的 surfaceChanged() 方法中，首先使用 Camera.stopPreview() 停止预览并更改屏幕方向，
//    然后使用 Camera.startPreview() 重新启动预览。

    private static final String TAG = "CircleSurfaceView";
//    上下文
    private Context mContext;
//    圆半径大小
    private int radius;
//    中心点
    private Point centerPoint;
//    剪切路径
    private Path mCliPath;
//    画笔
    private Paint mCliPaint;
    private CameraView mCameraView;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;



    public CircleSurfaceView(Context context, Camera camera) {
        super(context);
        //获取相机和SurfaceHolder实例
        mCamera=camera;
        mSurfaceHolder=getHolder();
        //添加回调事件
        mSurfaceHolder.addCallback(this);
//        不推荐使用的设置，但在3.0之前的Android版本上是必需的
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        init(context);
    }


    /**
     * 实例化
     * @param mContext
     */
    private void init(Context mContext){
        this.mContext=mContext;
        this.mCliPath=new Path();
        this.centerPoint=new Point();
        mCameraView=new CameraView();
        mCameraView.checkCameraHardwre(mContext);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽高大小
        int mWidthSize=MeasureSpec.getSize(widthMeasureSpec);
        int mHeightSize=MeasureSpec.getSize(heightMeasureSpec);
//        根据宽度和高度获取中心点坐标 >>表示size向右移一位，等于size/2
        centerPoint.x=mWidthSize >> 1;
        centerPoint.y=mHeightSize >> 1;
//        计算出圆的半径
        radius = ( centerPoint.x > centerPoint.y) ? centerPoint.y : centerPoint.x;
        Log.i(TAG, "onMeasure: " + centerPoint.toString());
//        重置路径
        mCliPath.reset();
//        添加内容-画圆
        mCliPath.addCircle(centerPoint.x, centerPoint.y, radius, Path.Direction.CCW);
//        存储测量的宽度和高度
        setMeasuredDimension(mWidthSize, mHeightSize);
    }

    /**
     * SurfaceView自带画布
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        if (Build.VERSION.SDK_INT>=26){
            canvas.clipPath(mCliPath);
        }else {
            canvas.clipPath(mCliPath, Region.Op.REPLACE);
        }
        super.draw(canvas);
    }
    /**
     * 画布
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
//            创建画面并设置相机在哪绘制
            mCamera.setPreviewDisplay(holder);
//            开始预览
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "surfaceCreated: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface()==null){
            return;
        }
//        更新前停止预览
        mCamera.stopPreview();
//        重新创建画面进行绘制并显示
        try {
            mCamera.setPreviewDisplay(holder);
//            重新预览
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "surfaceChanged: " + e.getMessage());
        }
//        如果您想为相机预览设置特定尺寸，请在 surfaceChanged() 方法中进行设置，如上述注释中所述。
//        设置预览尺寸时，您必须使用来自 getSupportedPreviewSizes() 的值。请勿在 setPreviewSize() 方法中设置任意值。
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


}
