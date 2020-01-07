package com.zzg.android_face_master.newFace.http.resultbean;

import com.hg.orcdiscern.http.util.HttpDialogUtil;

public interface RequestHttpCallback {
     //网络吐司
     void onToast(String message);
     //成功回调
     void onResponse(String arg0, HttpDialogUtil dialogUtil);
     //失败回调
     void onFailure(int messageType, HttpDialogUtil dialogUtil);
}
