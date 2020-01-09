package com.zzg.android_face_master.newFace.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Guo
 * CreatedDate 2018/12/20.
 * Email 18311371235@163.com
 * Remarks
 */
public class IMGUtils {
    /**
     * base64字符串转化成图片
     */
    public static String GenerateImage(Context context, String imgStr) {

        //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) { //图像数据为空
//            UtilsTools.MsgBox(context, "图片不能为空");
            return "";
        }

        try {
            //Base64解码
            byte[] b = Base64Utils.decode(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            // 新生成的jpg图片
            // 新图片的文件夹, 如果没有, 就创建
            String dirPath = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/zdgj/";
            File fileDir = new File(dirPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            // 文件夹现在存在了, 可以在此文件夹下创建图片了
            String imgFilePath = dirPath + System.currentTimeMillis() + ".jpg";
            File file = new File(imgFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            Log.d("ImageUtils","图片已保存到本地");
            return imgFilePath;
        } catch (Exception e) {
            Log.d("ImageUtils",e.getMessage());
            return "";
        }
    }


    /**
     * 照片转byte二进制
     * @param imagepath 需要转byte的照片路径
     * @return 已经转成的byte
     * @throws Exception
     */
    public static byte[] readStream(String imagepath) throws Exception {
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }


    // 二进制转字符串
    public static String byte2hex(byte[] b)
    {
        StringBuffer sb = new StringBuffer();
        String tmp = "";
        for (int i = 0; i < b.length; i++) {
            tmp = Integer.toHexString(b[i] & 0XFF);
            if (tmp.length() == 1){
                sb.append("0" + tmp);
            }else{
                sb.append(tmp);
            }

        }
        return sb.toString();
    }

    /**
     * 将图片转换成Base64编码的字符串
     *
     * @param path 图片本地路径
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * 在ImageView里展示指定路径的图片
     *
     * @param path      本地路径
     * @param imageView ImageView
     */
    public static void ShowPic2View(Context context, String path, ImageView imageView) {
        File localFile;
        FileInputStream localStream;
        Bitmap bitmap;
        localFile = new File(path);
        if (!localFile.exists()) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            Log.d("ImageUtils","is null.");
        } else {
            try {
                localStream = new FileInputStream(localFile);
                bitmap = BitmapFactory.decodeStream(localStream);
                imageView.setImageBitmap(bitmap);
                //                if (localStream != null) {
                localStream.close();
                //                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("ImageUtils",e.getMessage());
            }
        }
    }


    /**
     * 删除手机里指定路径的图片
     *
     * @param context Context
     * @param imgPath 本地路径
     */
    public static void DeletePicFromMobile(Context context, String imgPath) {
        try {
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = MediaStore.Images.Media.query(resolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=?", new String[]{imgPath}, null);
            boolean result;
            if (cursor.moveToFirst()) {
                long id = cursor.getLong(0);
                Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Uri uri = ContentUris.withAppendedId(contentUri, id);
                int count = context.getContentResolver().delete(uri, null, null);
                result = count == 1;
            } else {
                File file = new File(imgPath);
                result = file.delete();
            }

            if (result) {
                Log.d("ImageUtils","删除成功.");
            }
        } catch (Exception e) {
            Log.d("ImageUtils",e.getMessage());
        }
    }
    /**
     * 自定义规则生成32位编码
     *
     * @return string
     */
    public static String getUUIDByRules(String rules) {
        String radStr = rules;
        int rpoint = 0;
        StringBuffer generateRandStr = new StringBuffer();
        Random rand = new Random();
        int length = 32;
        for (int i = 0; i < length; i++) {
            if (rules != null) {
                rpoint = rules.length();
                int randNum = rand.nextInt(rpoint);
                generateRandStr.append(radStr.substring(randNum, randNum + 1));
            }
        }
        return generateRandStr + "";
    }
    /**
     * @param map     请求数据
     * @param encode  编码格式
     * @param isLower 是否转换大小写
     */
    public static String SortUtil(Map<String, Object> map, String encode, boolean isLower) {
        String params = "";
        List<Map.Entry<String, Object>> items = new ArrayList<>(map.entrySet());
        //对所有传入的参数按照字段名从小到大排序
        //Collections.sort(items); 默认正序
        //可通过实现Comparator接口的compare方法来完成自定义排序
        Collections.sort(items, new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return (o1.getKey().toString().compareTo(o2.getKey()));
            }
        });
        try {
            //拼接字符串
            StringBuffer baseString = new StringBuffer();
            for (Map.Entry<String, Object> item : items) {
                if (!TextUtils.isEmpty(item.getKey())) {
                    if (isLower) {
                        baseString.append(item.getKey().trim()).append("=").append(URLEncoder.encode(item.getValue().toString().toUpperCase().trim(), encode)).append("&");
                    } else {
                        baseString.append(item.getKey().trim()).append("=").append(item.getValue().toString().trim()).append("&");
                    }
                }
            }
            params = baseString.toString();
            if (!params.isEmpty()) {
                params = params.substring(0, params.length() - 1);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            params = "";
        }
        return params;
    }

    /**
     * md5加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        try {
//            生成一个MD5函数
            MessageDigest md = MessageDigest.getInstance("MD5");
//            计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        int options = 90;
        while (baos.toByteArray().length / 1024 > 2000) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }
}
