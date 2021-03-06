// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.zzg.android_face_master.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.zzg.android_face_master.bean.FaceResultData;

/**
 * @author Zhangzhenguo
 * @create 2020/1/6
 * @Email 18311371235@163.com
 * @Describe
 */
public class RectangleView extends View {

    private Paint mPaint;
    private Paint mTextPaint;
    private int mDisplayOrientation;
    private int mOrientation;
    private int previewWidth;
    private int previewHeight;
    private FaceResultData [] mFaces;
    private double fps;
    private boolean isFront = false;

    public RectangleView(Context context) {
        super(context);
        initialize();
    }
    // 当检测到人脸时绘制矩形边框
    private void initialize() {
        //获取屏幕尺寸
        DisplayMetrics metrics =getResources().getDisplayMetrics();

        //根据手机的像素密度进行像素转换
        int stroke = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics);
//        矩形画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(stroke);
        mPaint.setStyle(Paint.Style.STROKE);
//        文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, metrics);
        mTextPaint.setTextSize(size);
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 设置FPS
     * @param fps
     */
    public void setFPS(double fps) {
        this.fps = fps;
    }

    /**
     * 设置脸部参数集合并刷新矩形框
     * @param faces
     */
    public void setFaces(FaceResultData[] faces) {
        mFaces = faces;
        invalidate();
    }

    //当旋转图像预览时不会重新映射坐标系
    public void setDisplayOrientation(int displayOrientation) {
        mDisplayOrientation = displayOrientation;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFaces != null && mFaces.length > 0) {

            float scaleX = (float) getWidth() / (float) previewWidth;
            float scaleY = (float) getHeight() / (float) previewHeight;

            switch (mDisplayOrientation) {
                case 90:
                case 270:
                    scaleX = (float) getWidth() / (float) previewHeight;
                    scaleY = (float) getHeight() / (float) previewWidth;
                    break;
            }

            canvas.save();
            canvas.rotate(-mOrientation);
            RectF rectF = new RectF();
            for (FaceResultData face : mFaces) {
                PointF mid = new PointF();
                face.getMidPoint(mid);

                if (mid.x != 0.0f && mid.y != 0.0f) {
                    float eyesDis = face.eyesDistance();

                    rectF.set(new RectF(
                            (mid.x - eyesDis * 1.0f) * scaleX,
                            (mid.y - eyesDis * 1.25f) * scaleY,
                            (mid.x + eyesDis * 1.0f) * scaleX,
                            (mid.y + eyesDis * 0.0f) * scaleY));
                    if (isFront) {
                        float left = rectF.left;
                        float right = rectF.right;
                        rectF.left = getWidth() - right;
                        rectF.right = getWidth() - left;
                    }
                    canvas.drawRect(rectF, mPaint);
//                    canvas.drawText("ID " + face.getId(), rectF.left, rectF.bottom + mTextPaint.getTextSize(), mTextPaint);
//                    canvas.drawText("Confidence " + face.getConfidence(), rectF.left, rectF.bottom + mTextPaint.getTextSize() * 2, mTextPaint);
//                    canvas.drawText("EyesDistance " + face.eyesDistance(), rectF.left, rectF.bottom + mTextPaint.getTextSize() * 3, mTextPaint);
                }
            }
            canvas.restore();
        }

//        DecimalFormat df2 = new DecimalFormat(".##");
//        canvas.drawText("Detected_Frame/s: " + df2.format(fps) + " @ " + previewWidth + "x" + previewHeight, mTextPaint.getTextSize(), mTextPaint.getTextSize(), mTextPaint);
    }

    public void setPreviewWidth(int previewWidth) {
        this.previewWidth = previewWidth;
    }

    public void setPreviewHeight(int previewHeight) {
        this.previewHeight = previewHeight;
    }

    public void setFront(boolean front) {
        isFront = front;
    }
}