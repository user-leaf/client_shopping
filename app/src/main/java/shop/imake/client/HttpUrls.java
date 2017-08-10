package shop.imake.client;

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

    // ----------------------------首页----------------------------------------------------------
    public static final String PANICBUY = "api/v1/product/allRushToPurchase?page=";                 //抢购拼接地址
    public static final String PANICBUY_NEW = "api/v1/product/defaultRushToPurchase";
    public static final String PANICBUY_MORE = "api/v1/product/allRushToPurchase/";                 //抢购更多
    public static final String SEARCH = "api/v1/product/searchAll?";                                //搜索拼接地址
    public static final String EVERYDAY_NEW = "api/v1/product/allNow?page=";                        //每日上新拼接地址
    public static final String WITHDRAW_RULE = "http://wxweb.bjaiyouyou.com/tixianstep.html";       //提现规则地址

    // ------------------ ----佣金 收益 众汇券----------------------------------------------------
    public static final String URL_SCAN_PAY_SHOP_INFO = "api/v1/member/memberBusinessDetail/";      // 扫码支付 - 商户信息地址
    public static final String URL_SCAN_PAY_QRCODEPAY = "api/v1/qrcodepay";                         // 扫码支付 - 支付地址

    // --------------------------个人中心----------------------------------------------------------
    public static final String MINE_README_PLANE = "user-instructions-plane.html";
    public static final String MINE_README_TRAIN = "user-instructions-train.html";
}
