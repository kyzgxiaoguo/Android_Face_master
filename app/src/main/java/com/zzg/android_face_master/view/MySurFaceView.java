package com.zzg.android_face_master.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * @author Zhangzhenguo
 * @create 2019/12/26
 * @Email 18311371235@163.com
 * @Describe
 */
public class MySurFaceView extends SurfaceView {
    private Context mContext;

    public MySurFaceView(Context context) {
        super(context);
    }

    public MySurFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySurFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MySurFaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void initView(){

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
