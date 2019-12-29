package com.zzg.android_face_master.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Zhangzhenguo
 * @create 2019/12/29
 * @Email 18311371235@163.com
 * @Describe
 */
public class SupSurfaceCanvasView extends SurfaceView {
    public SupSurfaceCanvasView(Context context) {
        super(context);
    }

    public SupSurfaceCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SupSurfaceCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SupSurfaceCanvasView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

    }

    @Override
    public SurfaceHolder getHolder() {
        return super.getHolder();

    }
}
