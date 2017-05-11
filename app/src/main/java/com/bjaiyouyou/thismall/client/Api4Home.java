package com.bjaiyouyou.thismall.client;

import android.text.TextUtils;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.fragment.HomePage;
import com.bjaiyouyou.thismall.model.HomeAdBigModel;
import com.bjaiyouyou.thismall.model.HomeNavigationItemNew;
import com.bjaiyouyou.thismall.model.HomeProductModel;
import com.bjaiyouyou.thismall.model.IsHaveMessageNotRead;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by Administrator on 2017/2/16.
 */
public class Api4Home extends BaseClientApi {

    /**
     * Created byJackB
     * on 2017/2/16.
     */
    /**
     * 首页广告数据请求
     *
     * @param callback
     */
    public void getHomeAdData(String strTag, DataCallback<HomeAdBigModel> callback) {

        String url = ClientAPI.API_POINT + "api/v1/banner/index";

        LogUtils.d(TAG, "getHomeAdData: " + url);

        doGet(url, strTag, null, callback);
    }
    /**
     *
     *author Alice
     *created at 2017/3/10 15:48
     * 获取首页为您推荐部分的信息
     *
     * @param strTag
     * @param page
     * @param callback

     */

    public void getRecommendData(String strTag, int page,DataCallback<HomeProductModel> callback) {

        String url = ClientAPI.API_POINT + HttpUrls.EVERYDAY_NEW +page;

        LogUtils.d(TAG, "getRecommendData: " + url);

        doGet(url, strTag, null, callback);
    }
    /**
     *
     *author Alice
     *created at 2017/3/24 10:47
     *
     * 获取抢购信息
     *
     * @param strTag
     * @param callback
     */
    public void getNavigation(String strTag,DataCallback<HomeNavigationItemNew> callback){
        String url =ClientAPI.API_POINT + HttpUrls.PANICBUY_NEW;
        LogUtils.d(TAG, "getNavigation: " + url);
        doGet(url, strTag, null, callback);
    }

    /**
     * 是否存在未读取信息
     *author Alice
     *created at 2017/4/14 17:03
     */
    public void isHaveMessageNotRead(DataCallback<IsHaveMessageNotRead> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT );
        sb.append("api/v1/message/isHasNoRead");
        if(!TextUtils.isEmpty(CurrentUserManager.getUserToken())){
            sb.append("?token=" + CurrentUserManager.getUserToken());
        }
//        sb.append("&device_type=" + "android");
        String url = sb.toString();

        LogUtils.d(TAG, "isHaveMessageNotRead: " + url);
        doGet(url, HomePage.TAG, null, callback);
    }
    /**
     *author Alice
     *created at 2017/3/24 10:47
     *
     * 获取抢购信息
     * @param callback
     */
    public void getPushMessage(int page,StringCallback callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/message/getList");
        sb.append("?page=");
        sb.append(page);
        if(!TextUtils.isEmpty(CurrentUserManager.getUserToken())){
            sb.append("&token=" + CurrentUserManager.getUserToken());
        }
//        sb.append("&device_type=" + "android");
        String url = sb.toString();
        LogUtils.d("getPushMessage",url);

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(callback);

    }
    /**
     * 删除系统消息
     *author Alice
     *created at 2017/4/19 14:48
     */

    public void deletePushMessage(String id,StringCallback callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/message/delete/");
        sb.append(id);
        sb.append("?token=" + CurrentUserManager.getUserToken());
//        sb.append("&device_type=" + "android");
        String url = sb.toString();
        LogUtils.d("deletePushMessage",url);

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(callback);

    }



}
