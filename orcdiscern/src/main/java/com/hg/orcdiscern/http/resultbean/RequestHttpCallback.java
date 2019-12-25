package com.hg.orcdiscern.http.resultbean;

import com.hg.orcdiscern.http.util.HttpDialogUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public interface RequestHttpCallback {
     //网络吐司
     void onToast(String message);
     //成功回调
     void onResponse(String arg0, HttpDialogUtil dialogUtil);
     //失败回调
     void onFailure(int messageType, HttpDialogUtil dialogUtil);
}
