package com.zzg.android_face_master;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.surfaceview)
    SurfaceView surfaceview;
    private SurfaceHolder mSurfaceHolder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        mSurfaceHolder = surfaceview.getHolder();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
                //图片的参数(这个参数要有，不然找不到人脸)
                bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap myBitmap;
                //获取图片资源
                myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qqq,bitmapOption);
                //假设最多有5张脸
                int numOfFaces = 5;
                FaceDetector mFaceDetector = new FaceDetector(myBitmap.getWidth(),myBitmap.getHeight(),numOfFaces);
                FaceDetector.Face[] mFace = new FaceDetector.Face[numOfFaces];
                //获取实际上有多少张脸
                numOfFaces = mFaceDetector.findFaces(myBitmap, mFace);
                Log.v("------------->", ""+numOfFaces);
                //锁定整个SurfaceView
                Canvas mCanvas = mSurfaceHolder.lockCanvas();
                //画图
                mCanvas.drawBitmap(myBitmap, 0f, 0f, null);
                //绘制完成，提交修改
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                //重新锁一次
                mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                Paint mPaint = new Paint();
                //画笔颜色
                mPaint.setColor(Color.GREEN);
                //画笔的样式是矩形框，不是矩形块
                mPaint.setStyle(Paint.Style.STROKE);
                //线宽
                mPaint.setStrokeWidth(2.0f);
                float eyesDistance = 0f;
                //将所有检测到的脸框出来
                for(int i=0;i < numOfFaces;i++) {
                    PointF eyeMidPoint = new PointF();
                    //两眼的中点距离
                    mFace[i].getMidPoint(eyeMidPoint);
                    //两眼之间的距离
                    eyesDistance = mFace[i].eyesDistance();
                    //锁定整个SurfaceView
                    mCanvas = mSurfaceHolder.lockCanvas();
                    //画矩形框
                    mCanvas.drawRect((int) (eyeMidPoint.x - eyesDistance),
                            (int) (eyeMidPoint.y - eyesDistance),
                            (int) (eyeMidPoint.x + eyesDistance),
                            (int) (eyeMidPoint.y + eyesDistance),
                            mPaint);
                    //绘制完成，提交修改
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                    //重新锁一次
                    mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        });
    }
}
