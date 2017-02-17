package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.model.HomeAdBigModel;
import com.bjaiyouyou.thismall.utils.LogUtils;

/**
 * Created by Administrator on 2017/2/16.
 */
public class Api4Home extends BaseClientApi {


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
}
