package com.zzg.android_face_master.http.util;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2016/8/23.
 */
public class HttpCheckNetWorkUtil extends Application {
    /**
     * 返回true代表有网络，false反之
     * @param context
     * @return
     */
    public  boolean checkNotWorkAvailable(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager==null){
            return false;
        }else {
            NetworkInfo[] info=connectivityManager.getAllNetworkInfo();
            if(info!=null){
                for(int i=0;i<info.length;i++){
                    if(info[i].getState()== NetworkInfo.State.CONNECTED){  //
                        NetworkInfo networkInfo=info[i];
                        if(networkInfo.getType()== ConnectivityManager.TYPE_WIFI){
                            return true;
                        }else if(networkInfo.getType()== ConnectivityManager.TYPE_MOBILE){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    };
}
