package com.bjaiyouyou.thismall.client;

/**
 * urls
 * <p/>
 * User: JackB
 * Date: 2016/11/2
 */
public class HttpUrls {

    // 会员开通支付接口
//    public static final String URL_PAY_VIP = "http://testapi.bjaiyouyou.com/"+"api/v1/active_vip";
    public static final String URL_PAY_VIP = ClientAPI.API_POINT + "api/v1/active_vip";


    //抢购拼接地址
    public static final String PANICBUY = "api/v1/product/allRushToPurchase?page=";
    public static final String PANICBUY_NEW = "api/v1/product/defaultRushToPurchase";
    //抢购更多
    public static final String PANICBUY_MORE = "api/v1/product/allRushToPurchase/";
    //搜索拼接地址
    public static final String SEARCH = "api/v1/product/searchAll?";
    //每日上新拼接地址
    public static final String EVERYDAY_NEW = "api/v1/product/allNow?page=";
    //提现规则地址
    public static final String WITHDRAW_RULE = "http://wxweb.bjaiyouyou.com/tixianstep.html";
}
