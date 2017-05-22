package shop.imake;

import shop.imake.client.ClientAPI;

/**
 * APP中大部分的常量定义
 *
 * @author JackB
 * @date 2016/6/1
 */
public class Constants {
    public static final int TASK_CODE_OK = 0;
    public static final int TASK_CODE_NO_DATA = 1;
    public static final int TASK_CODE_JSON_ERROR = 2;

    public static final int ADDRESS_ADD = 0;
    public static final int ADDRESS_EDIT = 1;

    //==============【Ping++】=====================

    // Ping++支付
    public static final String PingppURL = ClientAPI.API_POINT+"api/v1/pay";

    /**
     * 余额支付渠道
     */
    public static final String CHANNEL_BALANCE = "balance";
    /**
     * 银联支付渠道
     */
    public static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    public static final String CHANNEL_WECHAT = "wx";
    /**
     * 微信支付渠道
     */
    public static final String CHANNEL_QPAY = "qpay";
    /**
     * 支付支付渠道
     */
    public static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    public static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    public static final String CHANNEL_JDPAY_WAP = "jdpay_wap";

    //=============================================

    // 确认订单页，不包邮时的邮费
    public static final int ORDER_MAKE_POSTAGE = 20;


    ////////////拨打电话权限变量初始化
    public  static final int CALL_PERMISSIONS_REQUEST_CODE = 100; // 请求码PermissionsActivity

    /**
     * 微信开发者账号的常量
     */
    public  static final String  APP_ID = "wxa4650166adbfdcc1";
    public  static final String  APP_Secret = "e6690254dd53c6a974432d05ec121a9b";
    /**
     *  获取OpenID的RequestCode
     */
    public  static final int  GET_OPENID_REQUEST_CODE =3000;

    /**
     * 客服电话
     */
    public static final String KEFU_TEL = "4001599586";

    // 确认订单页 超过9kg增加运费 重量基准
    public static final int ORDER_MAKE_POSTAGE_WEIGHT_BASE = 9;
    //历史搜搜的key
    public static final String  HISTORY_SEARCH_KEY= "historySearch";
    //用户信息
    public static final String  USER= "userMessage";

    public static final String PACKAGE_NAME_WECHAT = "com.tencent.mm";

    // 是否显示公告(购物车页、确认订单页)
    public static final boolean showTip = false;
}
