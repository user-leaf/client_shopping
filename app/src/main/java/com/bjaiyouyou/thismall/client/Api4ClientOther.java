package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.model.HistoryBuy;
import com.bjaiyouyou.thismall.model.MyOrder;
import com.bjaiyouyou.thismall.model.SearchResultGoods;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;

/**
 *author Qxh
 *created at 2017/3/10 13:39
 * 未完成订单页面网络请求
 */
public class Api4ClientOther extends BaseClientApi {

    /**
     *
     *author Qxh
     *created at 2017/3/10 15:48
     * 获取订单信息
     *
     * @param strTag
     * @param orderState
     * @param page
     * @param callback
     */

    public void getMyOrderData(String strTag,  int orderState ,int page,DataCallback<MyOrder> callback) {

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

   /**
    *
    *author Qxh
    *created at 2017/3/13 14:46
    *获取历史购买数据
    */
    public void getHistoryData(String strTag, int page,DataCallback<HistoryBuy> callback) {

        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/order/historyBuy?page=");
        sb.append(page);
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());
        String url=sb.toString().trim();

        LogUtils.d(TAG, "getHistoryData: " + url);

        doGet(url, strTag, null, callback);
    }

    public void getSearchGoodsData(String strTag,String key, int page,DataCallback<SearchResultGoods> callback) {

        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT + ClientAPI.SEARCH);
        sb.append("keyword=" + key);
        sb.append("&page=" + page);
        String url = sb.toString();

        LogUtils.d(TAG, "getSearchGoodsData: " + url);

        doGet(url, strTag, null, callback);
    }




}
