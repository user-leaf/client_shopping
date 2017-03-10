package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.model.MyOrder;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;

/**
 *author Qxh
 *created at 2017/3/10 13:39
 * 成订单页面网络请求
 */
public class Api4MyOrderFinishFragment extends BaseClientApi {

    /**
     *
     *author Qxh
     *created at 2017/3/10 15:48
     * 获取首页为您推荐部分的信息
     *
     * @param strTag
     * @param orderState
     * @param page
     * @param callback
     */

    public void getOrderData(String strTag,  int orderState ,int page,DataCallback<MyOrder> callback) {

        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/order/queryAllByOrderState/")
                .append(orderState)
                .append("?page=")
                .append(page)
                .append("&token=")
                .append(CurrentUserManager.getUserToken());
        String url = sb.toString();


        LogUtils.d(TAG, "getOrderData: " + url);

        doGet(url, strTag, null, callback);
    }
}
