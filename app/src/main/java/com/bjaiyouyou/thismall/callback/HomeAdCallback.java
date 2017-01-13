package com.bjaiyouyou.thismall.callback;


import com.bjaiyouyou.thismall.model.HomeAdBigModel;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * 首页广告数据请求接口回调
 *
 * @author kanbin
 * @date 2016/7/12
 */
public abstract class HomeAdCallback extends Callback<HomeAdBigModel> {

    @Override
    public HomeAdBigModel parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        HomeAdBigModel habm = new Gson().fromJson(string, HomeAdBigModel.class);
        return habm;
    }

}