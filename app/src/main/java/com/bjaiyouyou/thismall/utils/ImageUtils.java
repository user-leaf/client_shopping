package com.bjaiyouyou.thismall.utils;

import com.bjaiyouyou.thismall.client.ClientAPI;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 图片、图片地址相关
 *
 * User: kanbin
 * Date: 2016/9/30
 */
public class ImageUtils {

    private static final java.lang.String TAG = ImageUtils.class.getSimpleName();

    /**
     * 获取缩略图URL
     * @return
     */
    public static String getThumb(String url, int width, int height){
        // 主要是把一些特殊字符转换成转移字符，比如：&要转换成&amp;这样的。
        String encodedUrl = null;
        try {
            encodedUrl = URLEncoder.encode(url,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/image/thumb")
                .append("?url=").append(encodedUrl)
                .append("&width=").append(width)
                .append("&height=").append(height);

        LogUtils.d(TAG, url);
        LogUtils.d(TAG, sb.toString());

        return sb.toString();
    }
}
