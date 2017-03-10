package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.model.HomeAdBigModel;
import com.bjaiyouyou.thismall.model.HomeProductModel;
import com.bjaiyouyou.thismall.utils.LogUtils;

/**
 * Created by Administrator on 2017/2/16.
 */
public class Api4Home extends BaseClientApi {

    /**
     * Created bykanbin
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
     *author Qxh
     *created at 2017/3/10 15:48
     * 获取首页为您推荐部分的信息
     * @param strTag
     * @param page
     * @param callback

     */

    public void getRecommendData(String strTag, int page,DataCallback<HomeProductModel> callback) {

        String url = ClientAPI.API_POINT + ClientAPI.EVERYDAY_NEW +page;

        LogUtils.d(TAG, "getHomeAdData: " + url);

        doGet(url, strTag, null, callback);
    }
}
