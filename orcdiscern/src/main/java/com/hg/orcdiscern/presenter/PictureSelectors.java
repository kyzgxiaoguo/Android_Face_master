package com.hg.orcdiscern.presenter;

import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.hg.orcdiscern.bean.LocalMediaa;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * @author Zhangzhenguo
 * @create 2019/10/30
 * @Email 18311371235@163.com
 * @Describe
 */
public class PictureSelectors {
    private static List<LocalMedia> localMedia;
    private static List<LocalMediaa> localMediaa;

    public static List<LocalMediaa> obtainMultipleResult(Intent data){
        localMedia=PictureSelector.obtainMultipleResult(data);
        String dataStr=JSON.toJSONString(localMedia);
        localMediaa= JSON.parseArray(dataStr,LocalMediaa.class);
        return localMediaa;
    }
}
