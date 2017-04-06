package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.model.HistoryBuy;
import com.bjaiyouyou.thismall.model.IntegralDetailModel;
import com.bjaiyouyou.thismall.model.MyOrder;
import com.bjaiyouyou.thismall.model.ProductDetail;
import com.bjaiyouyou.thismall.model.SearchHot;
import com.bjaiyouyou.thismall.model.SearchResultGoods;
import com.bjaiyouyou.thismall.model.UuDetailModel;
import com.bjaiyouyou.thismall.model.WithdrawReCordModel;
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
    *
    * @param strTag
    * @param page
    * @param callback
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
    
    /**
     * 
     *author Qxh
     *created at 2017/3/14 17:37
     *获取搜索结果的数据
     *
     * @param strTag
     * @param key
     * @param page
     * @param callback
     */

    public void getSearchGoodsData(String strTag,String key, int page,DataCallback<SearchResultGoods> callback) {

        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT + HttpUrls.SEARCH);
        sb.append("keyword=" + key);
        sb.append("&page=" + page);
        String url = sb.toString();

        LogUtils.d(TAG, "getSearchGoodsData: " + url);

        doGet(url, strTag, null, callback);
    }
    /**
     *
     *author Qxh
     *created at 2017/3/14 17:52
     *获取热门搜索数据
     *
     * @param strTag
     * @param callback
     */
    public void getHotSearchGoodsData(String strTag,DataCallback<SearchHot> callback) {

        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/product/hotSearch");
        String url = sb.toString();

        LogUtils.d(TAG, "getHotSearchGoodsData: " + url);

        doGet(url, strTag, null, callback);
    }

    /**
     *
     *author Qxh
     *created at 2017/3/15 15:08
     * 获得商品扫描数据
     *
     *
     * @param strTag
     * @param productScanID
     * @param callback
     */

    public  void getScanGoodDetailData(String strTag,String  productScanID, DataCallback<ProductDetail> callback){
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/product/getDetailForBarCode/")
                .append(productScanID);
        String url = sb.toString();
        LogUtils.d(TAG, "getScanGoodDetailData: " + url);

        doGet(url, strTag, null, callback);
    }
    /**
     *
     *author Qxh
     *created at 2017/3/15 15:08
     * 获得商品数据
     *
     *
     * @param strTag
     * @param productID
     * @param callback
     */

    public  void getGoodDetailData(String strTag,Long  productID, DataCallback<ProductDetail> callback){
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/product/getDetail/")
                .append(productID);
        String url = sb.toString();
        LogUtils.e("url", url);
        LogUtils.d(TAG, "getGoodDetailData: " + url);

        doGet(url, strTag, null, callback);
    }

    /**
     *
     *author Qxh
     *created at 2017/3/16 16:25
     *
     * 获得体现记录数据
     *
     * @param strTag
     * @param page
     * @param callback
     */

    public void getWithdrawRecord(String strTag,int   page, DataCallback<WithdrawReCordModel> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/memberDrawingslog/memberDetail");
        sb.append("?page=").append(page);
        sb.append("&token=").append(CurrentUserManager.getUserToken());
        String url = sb.toString();
        LogUtils.d(TAG, "getWithdrawRecord: " + url);

        doGet(url, strTag, null, callback);

    }


    /**
     *
     *author Qxh
     *created at 2017/3/16 16:50
     * 获得积分详情数据
     *
     * @param strTag
     * @param page
     * @param callback
     */

    public void getIntegralDetail(String strTag,int   page, DataCallback<IntegralDetailModel> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/member/getIntegrationUpdateDetail");
        sb.append("?page=").append(page);
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());
        //test
//        sb.append("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIxMTEsImlzcyI6Imh0dHA6XC9cL3Rlc3RhcGkuYmphaXlvdXlvdS5jb21cL2FwaVwvdjFcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNDc5NDYyNTk4LCJleHAiOjE0ODIwNTQ1OTgsIm5iZiI6MTQ3OTQ2MjU5OCwianRpIjoiNzY0N2VkOTczNTE4NjMxM2Q5N2Y1ZDdmMzc0MjBlNTgifQ.1fPWO1LwXfJoH9wIiM9iIfOVxnPwSgagncUQh_P99pg");
        String url = sb.toString();
        LogUtils.d("getIntegerDetail url:", "" + url);
        doGet(url, strTag, null, callback);
    }


    /**
     *
     *author Qxh
     *created at 2017/3/16 16:50
     * 获得UU详情数据
     *
     * @param strTag
     * @param page
     * @param callback
     */

    public void getUuDetail(String strTag,int   page, DataCallback<UuDetailModel> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/member/getUUupdateDetail");
        sb.append("?page=").append(page);
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());
        //test
//        sb.append("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIxMTEsImlzcyI6Imh0dHA6XC9cL3Rlc3RhcGkuYmphaXlvdXlvdS5jb21cL2FwaVwvdjFcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNDc5NDYyNTk4LCJleHAiOjE0ODIwNTQ1OTgsIm5iZiI6MTQ3OTQ2MjU5OCwianRpIjoiNzY0N2VkOTczNTE4NjMxM2Q5N2Y1ZDdmMzc0MjBlNTgifQ.1fPWO1LwXfJoH9wIiM9iIfOVxnPwSgagncUQh_P99pg");
        String url = sb.toString();
        LogUtils.d("getUuDetail url:", "" + url);
        doGet(url, strTag, null, callback);
    }

    /**
     *
     *author Qxh
     *created at 2017/3/17 15:34
     * 修改个人信息，获取验证码
     *
     */

    public void getUpdateUserVerification(String strTag,String phone, DataCallback<String> callback){
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/auth/sendVerification");
        sb.append("?phone=");
        sb.append(phone);
        String url = sb.toString();
        LogUtils.d("url", url);
        doGet(url, strTag, null, callback);
    }
//
//
//    /**
//     * 判断是否绑定支付宝
//     *author Qxh
//     *created at 2017/3/31 21:59
//     */
//    public void getIfBindingAlipay(DataCallback<Boolean> callback){
//        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
//        sb.append("api/v1/member/checkIsBoundAlipay");
//        sb.append("?token=");
//        sb.append(CurrentUserManager.getUserToken());
//
//        String url = sb.toString();
//        LogUtils.d("url", url);
//        doGet(url, WithdrawActivity.TAG, null, callback);
//
//    }
//
//    /**
//     * 获得支付宝参数
//     *author Qxh
//     *created at 2017/3/31 21:59
//     */
//    public void getAuthorizationParameters(DataCallback<String> callback){
//        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
//        sb.append("api/v1/ali/appAuthInfo");
//
//        String url = sb.toString();
//        LogUtils.d("url", url);
//        doGet(url, WithdrawActivity.TAG, null, callback);
//
//    }
//    /**
//     * 绑定支付宝
//     *author Qxh
//     *created at 2017/3/31 21:59
//     */
//    public void bindingAlipay(String userId,DataCallback<String> callback){
//        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
//        sb.append("api/v1/member/boundAlipay");
//        sb.append("?alipay_id=");
//        sb.append(userId);
//
//        String url = sb.toString();
//        LogUtils.d("url", url);
//        doGet(url, WithdrawActivity.TAG, null, callback);
//
//    }



}
