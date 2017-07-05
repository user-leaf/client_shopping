package shop.imake.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import okhttp3.Call;
import shop.imake.client.ClientAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 图片、图片地址相关
 *
 * User: JackB
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

    public static void getImageFromNet(String url, final ImageCallback callback) {
//        HttpURLConnection conn = null;
//        try {
//            URL mURL = new URL(url);
//            conn = (HttpURLConnection) mURL.openConnection();
//            conn.setRequestMethod("GET"); //设置请求方法
//            conn.setConnectTimeout(10000); //设置连接服务器超时时间
//            conn.setReadTimeout(5000);  //设置读取数据超时时间
//
//            conn.connect(); //开始连接
//
//            int responseCode = conn.getResponseCode(); //得到服务器的响应码
//            if (responseCode == 200) {
//                //访问成功
//                InputStream is = conn.getInputStream(); //获得服务器返回的流数据
//                Bitmap bitmap = BitmapFactory.decodeStream(is); //根据流数据 创建一个bitmap对象
//                return bitmap;
//
//            } else {
//                //访问失败
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (conn != null) {
//                conn.disconnect(); //断开连接
//            }
//        }
//        return null;

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new BitmapCallback(){

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        callback.handle(response);
                    }
                });
    }

    public interface ImageCallback{
        void handle(Bitmap bitmap);
    }
}
