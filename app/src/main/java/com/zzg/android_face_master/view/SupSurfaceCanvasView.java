package com.zzg.android_face_master.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Rectangle;

import com.zzg.android_face_master.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhangzhenguo
 * @create 2019/12/29
 * @Email 18311371235@163.com
 * @Describe
 */
public class SupSurfaceCanvasView extends View {

    private final List<RectF> rectangles = new ArrayList<>();
    private final Paint strokePaint = new Paint();
    private Bitmap mBitmap;
    private List<FaceDetector.Face> faces;


    public SupSurfaceCanvasView(Context context) {
        super(context);
    }

    public SupSurfaceCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SupSurfaceCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SupSurfaceCanvasView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.RectanglesView);
        try {
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setColor(attributes.getColor(R.styleable.RectanglesView_rectanglesColor, Color.WHITE)
            );
            strokePaint.setStrokeWidth(
                    attributes.getDimensionPixelSize(R.styleable.RectanglesView_rectanglesStrokeWidth, 2)
            );
        } finally {
            attributes.recycle();
        }
    }

    /**
     * Updates rectangles which will be drawn.
     */
    public void setRectangles(@NonNull List<FaceDetector.Face> faces,Bitmap bitmap) {
        this.faces=faces;
        this.mBitmap=bitmap;
        invalidate();
    }
    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);
        Rect destBounds = new Rect(0, 0, (int) (imageWidth * scale), (int) (imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
        return scale;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mBitmap != null) && (faces != null)) {
//            double scale = drawBitmap(canvas);
//            canvas.drawBitmap(mBitmap, 0, 0, null);
            drawFaceBox(canvas, 0);
        }
    }

    /**
     * 绘制人脸矩形框
     * @param canvas
     * @param scale
     */
    private void drawFaceBox(Canvas canvas, double scale) {
        ensureMainThread();
        this.rectangles.clear();
        for (FaceDetector.Face face: faces) {
            PointF pointF = new PointF();
            face.getMidPoint(pointF);//获取人脸中心点
            RectF rectF=new RectF();
            if (pointF.x!=0.0f && pointF.y!=0.0f){
                float eyesDistance = face.eyesDistance();//获取人脸两眼的间距
                final int left = (int) (pointF.x - eyesDistance * 1.2f);
                final int top = (int) (pointF.y - eyesDistance * 0.55f);
                final int right = (int) (pointF.x + eyesDistance * 1.2f);
                final int bottom = (int) (pointF.y + eyesDistance * 1.85f);
                Log.d("执行","left:"+left+"top:"+top+"top:"+right+"top:"+bottom);
                rectF.set(new Rect(left,top,right,bottom));
                canvas.drawRect(rectF, strokePaint);
            }
        }
    }

    private void ensureMainThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalThreadStateException("This method must be called from the main thread");
        }
    }
}
