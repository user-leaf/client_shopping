package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.fragment.HomePage;
import com.bjaiyouyou.thismall.model.HomeAdBigModel;
import com.bjaiyouyou.thismall.model.HomeNavigationItemNew;
import com.bjaiyouyou.thismall.model.HomeProductModel;
import com.bjaiyouyou.thismall.model.IsHaveMessageNotRead;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
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
     *author Qxh
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
     *author Qxh
     *created at 2017/3/24 10:47
     *
     * 获取抢购信息
     * @param callback
     */
    public void isHaveMessageNotRead(DataCallback<IsHaveMessageNotRead> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT );
        sb.append("api/v1/message/isHasNoRead");
        sb.append("?device_type=" + "android");
        sb.append("&token=" + CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.d(TAG, "isHaveMessageNotRead: " + url);
        doGet(url, HomePage.TAG, null, callback);
    }

}
