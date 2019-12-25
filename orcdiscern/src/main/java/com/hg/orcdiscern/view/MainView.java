package com.hg.orcdiscern.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hg.orcdiscern.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

/**
 * @author Zhangzhenguo
 * @create 2019/10/29
 * @Email 18311371235@163.com
 * @Describe
 */
public class MainView extends AppCompatActivity {
    private static TextView tvGallery;
    private static TextView tvCapture;
    private static TextView tvCancel;
    //view行高
    private static int navigationHeight;
    private static PopupWindow mPopupWindow;
    private static ProgressDialog mProgressDialog;
    private static int indexID = 0;

    private static Context mContext;
    private static Activity mActivity;

    private static MainView mainView;

    /**
     * 有参构造，实例化
     * @param context
     * @param activity
     */
    public MainView(Context context,Activity activity) {
        this.mContext=context;
        this.mActivity=activity;
        int resourceId = mActivity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationHeight = mActivity.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 返回实例对象
     * @param context
     * @param activity
     * @return
     */
    public static MainView getInstance(Context context, Activity activity){
        if (mainView==null){
            Log.d("执行","getInstance");
            mainView=new MainView(context,activity);
        }
        return mainView;
    }

    /**
     * 初始化底部弹框
     * @param view
     */
    public void openPopupWindowDialog(View view) {
        //防止重复按按钮
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
        View popupContentView = LayoutInflater.from(mContext).inflate(R.layout.view_popupwindow, null);
        tvGallery = popupContentView.findViewById(R.id.tv_gallery);
        tvCapture = popupContentView.findViewById(R.id.tv_capture);
        tvCancel = popupContentView.findViewById(R.id.tv_cancel);
        mPopupWindow = new PopupWindow(
                popupContentView,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置点击Dialog外空白处关闭View
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        //设置背景,这个没什么效果，不添加会报错
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置弹入弹出动画
        mPopupWindow.setAnimationStyle(R.style.popupWindowAnimation);
        //设置位置
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, navigationHeight - 100);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopupWindow.dismiss();
                setBackgroundAlpha(1f);
            }
        });
        //设置背景色
        setBackgroundAlpha(0.4f);
        //拍照、选图、取消
        tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("执行","tvGallery");
                getPictureFromCapture();
                mPopupWindow.dismiss();
            }
        });
        tvCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("执行","tvCapture");
                getPictureFromGallery();
                mPopupWindow.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = alpha;
        mActivity.getWindow().setAttributes(lp);
    }
    /**
     * 调用拍照
     */
    public void getPictureFromCapture() {
        PictureSelector.create(mActivity)
                .openCamera(PictureMimeType.ofImage())
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 调用图库
     */
    public void getPictureFromGallery() {
        PictureSelector.create(mActivity)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    public void preViewPhoto(){

    }


}
