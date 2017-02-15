package com.bjaiyouyou.thismall.client;


import android.content.Context;
import android.text.TextUtils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Map;

/**
 * 网络请求base类
 * <p>
 * Created by kanbin on 2016/12/8.
 */
public class BaseClientApi {
    public static final String TAG = BaseClientApi.class.getSimpleName();

    public Context mContext;
    /** 请求超时时间 **/
    private static final long TIME_OUT_SPAN = OkHttpUtils.DEFAULT_MILLISECONDS; // 60 * 1000

    /**
     * 初始化方法
     */
    public void init() {

    }

    /**
     * 设置上下文环境
     *
     * @param context
     */
    public void setContext(Context context) {
        mContext = context;
    }

    /**
     * 通过postString请求数据
     *
     * @param url
     * @param tag      取消请求标识
     * @param callback
     */
    public void doByJson(String url, String tag, Callback callback) {
        PostStringBuilder builder = OkHttpUtils.postString().url(url);
        if (!TextUtils.isEmpty(tag)){
            builder.tag(tag);
        }
        builder.build()
                .connTimeOut(TIME_OUT_SPAN)
                .readTimeOut(TIME_OUT_SPAN)
                .writeTimeOut(TIME_OUT_SPAN)
                .execute(callback);
    }

    /**
     * 通过get请求数据
     *
     * @param url
     * @param tag      取消请求标识
     * @param params   参数列表
     * @param callback
     */
    public void doGet(String url, String tag, Map<String, String> params, Callback callback) {
        GetBuilder builder = OkHttpUtils.get().url(url);
        if (!TextUtils.isEmpty(tag)) {
            builder.tag(tag);
        }
        if (null != params) {
            for (String key : params.keySet()) {
                builder.addParams(key, params.get(key));
            }
        }
        builder.build()
                .connTimeOut(TIME_OUT_SPAN)
                .readTimeOut(TIME_OUT_SPAN)
                .writeTimeOut(TIME_OUT_SPAN)
                .execute(callback);
    }

    /**
     * 通过get请求数据
     *
     * @param url
     * @param params   参数列表
     * @param callback
     */
    public void doGet(String url, Map<String, String> params, Callback callback) {
        doGet(url, null, params, callback);
    }

    /**
     * 通过post请求数据
     *
     * @param url
     * @param tag      取消请求标识
     * @param params   参数列表
     * @param callback
     */
    public void doPost(String url, String tag, Map<String, String> params, Callback callback) {
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        if (!TextUtils.isEmpty(tag)) {
            builder.tag(tag);
        }
        if (params != null) {
            for (String key : params.keySet()) {
                builder.addParams(key, params.get(key));
            }
        }
        builder.build()
                .connTimeOut(TIME_OUT_SPAN)
                .readTimeOut(TIME_OUT_SPAN)
                .writeTimeOut(TIME_OUT_SPAN)
                .execute(callback);
    }

    /**
     * 通过post请求数据
     *
     * @param url
     * @param params   参数列表
     * @param callback
     */
    public void doPost(String url, Map<String, String> params, Callback callback) {
        doPost(url, null, params, callback);
    }

}
