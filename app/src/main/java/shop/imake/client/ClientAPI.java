package shop.imake.client;

import android.text.TextUtils;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import shop.imake.Constants;
import shop.imake.MainApplication;
import shop.imake.callback.DataCallback;
import shop.imake.task.TaskCallback;
import shop.imake.task.TaskResult;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.LogUtils;
import shop.imake.utils.UNNetWorkUtils;

/**
 * 网络请求(旧)
 *
 * @author JackB
 * @date 2016/6/1
 * @see ClientApiHelper
 * ClientApiHelper.getInstance().getClientApi(Api4XXX.class).xxx()
 */
public class ClientAPI {

    public static final String TAG = ClientAPI.class.getSimpleName();

    public static String API_POINT;
    public static String URL_WX_H5;

    /**
     * false 正式环境  true 测试环境
     */
    private static boolean flag_test = false;

    static {
        if (flag_test) {    // 测试环境
            API_POINT = "http://api3.bjiuu.com/";
            URL_WX_H5 = "http://wxweb2.bjiuu.com/zhweb/";

        } else {    // 正式环境
            API_POINT = "https://zhapi.bjaiyouyou.com/";
            URL_WX_H5 = "https://zhweb.bjaiyouyou.com/";

        }
    }

    private ClientAPI() {
    }

//    /**
//     * 获取首页物品数据
//     *
//     * @param stage_id77777''
//     * @param page
//     * @param limit
//     * @param callback
//     */
//    public static void getHomeGoodData(int stage_id, int page, int limit, final TaskCallback callback) {
//        StringBuilder sb = new StringBuilder(API_POINT);
//        sb.append("cf/dish_list.php?")
//                .append("stage_id=").append(stage_id)
//                .append("&page=").append(page)
//                .append("&limit=").append(limit);
//
//        String url = sb.toString();
//
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                TaskResult result = new TaskResult();
//                if (!TextUtils.isEmpty(response)) {
//                    //请求成功
//                    result.mCode = Constants.TASK_CODE_OK;
//                    result.mData = response;
//                } else {
//                    // 数据为空
//                    result.mCode = Constants.TASK_CODE_NO_DATA;
//                }
//
//                callback.onTaskFinished(result);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//
//        RequestManager.addRequest(request, null);
//    }

    /**
     * 抢购的数据获取
     *
     * @param page
     * @param callback
     */

    public static void getHomePanicBuyData(final int page, final TaskCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/product/allRushToPurchase?page=").append(page + 1);
        String url = sb.toString();
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        UNNetWorkUtils.unNetWorkOnlyNotify(MainApplication.getContext(), e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        TaskResult result = new TaskResult();
                        if (!TextUtils.isEmpty(response)) {
                            Log.e("请求成功", "" + page);
                            //请求成功
                            result.mCode = Constants.TASK_CODE_OK;
                            result.mData = response;
                        } else {
                            // 数据为空
                            result.mCode = Constants.TASK_CODE_NO_DATA;
                        }
                        //接口回调回传数据
                        callback.onTaskFinished(result);
                    }
                });
    }

    /**
     * 获得搜索商品价格
     *
     * @param key
     * @param page
     */

    public static void getSearchGoodsData(final String key, int page, final StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT + HttpUrls.SEARCH);
        sb.append("keyword=" + key);
        sb.append("&page=" + page);
        String url = sb.toString();

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(callback);

    }

    /**
     * 获得商品信息
     *
     * @param url
     * @param callback
     */
    public static void
    getGoodsData(final String url, final StringCallback callback) {

        LogUtils.d(TAG, "getGoodsData: " + url);

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(callback);

    }

    /**
     * 获取限时抢购商品数据
     *
     * @param url
     * @param callback
     */

    public static void getTimeGoodsData(final String url, int page, final StringCallback callback) {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?page=");
        sb.append(page);
        String mUrl = sb.toString();
        OkHttpUtils
                .get()
                .url(mUrl)
                .build()
                .execute(callback);
    }

    /**
     * 购物车页面数据请求
     *
     * @param callback
     */
    public static void getCartData(StringCallback callback) {
        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/shoppingCart/index")
                .append("?token=")
                .append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.d(TAG, "getCartData: " + url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 我的订单数据请求
     *
     * @param token
     * @param orderState
     * @param callback
     */
    public static void getMyOrderData(String token, int orderState, int page, StringCallback callback) {
        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/order/queryAllByOrderState/")
                .append(orderState)
                .append("?page=")
                .append(page)
                .append("&token=")
                .append(token);
        String url = sb.toString();
        LogUtils.d(TAG, "getMyOrderData: " + url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 用户个人信息
     *
     * @param token
     * @param callback
     */
    public static void getUserData(String token, StringCallback callback) {
        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/auth/memberDetail")
                .append("?token=")
                .append(token);
        String url = sb.toString();

//        LogUtils.e("getUserData", "getUserData: " + url);
        LogUtils.d(TAG, "getUserData: " + url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 获取订单详情
     *
     * @param orderNumber
     * @param token
     * @param callback
     */

    public static void getOrderDetailsData(String orderNumber, String token, StringCallback callback) {
//        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
//        sb.append("api/v1/order/queryByOrderNumber/")
//                .append(orderNumber)
//                .append("?token=")
//                .append(token);
//        String url = sb.toString();
//        LogUtils.e("getOrderDetailsData", url);
//
//        OkHttpUtils.get()
//                .url(url)
//                .build()
//                .execute(callback);

        getOrderDetail(null, orderNumber, callback);
    }

    /**
     * 商品详情页
     *
     * @param productID
     * @param callback
     */
    public static void getProductDetailsData(Long productID, StringCallback callback) {
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/product/getDetail/")
                .append(productID);
        String url = sb.toString();
        LogUtils.e("url", url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 扫描商品详情页
     *
     * @param callback
     */
    public static void getScanProductDetailsData(String productScanID, StringCallback callback) {
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/product/getDetailForBarCode/")
                .append(productScanID);
        String url = sb.toString();
        LogUtils.e("url", url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 热门搜索
     */
    public static void getSearchHotData(StringCallback callback) {
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/product/hotSearch");
        String url = sb.toString();
        LogUtils.e("url", url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 历史购买
     */
    public static void getHistoryBuyData(String token, int page, StringCallback callback) {
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/order/historyBuy?page=");
        sb.append(page);
        sb.append("&token=");
        sb.append(token);
        String url = sb.toString();
        LogUtils.e("url", url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 设置安全码
     * 邮箱未设置
     */
    public static void postSetSafeCode(String token, String safeCode, String email, StringCallback callback) {
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/auth/setSecurityCode2");
        sb.append("?security_code=");
        sb.append(safeCode);
        sb.append("&email=");
        sb.append(email);
        sb.append("&token=");
        sb.append(token);
        String url = sb.toString();
        LogUtils.e("url", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 设置安全码
     * 邮箱已设置
     */
    public static void postSetSafeCode(String token, String safeCode, StringCallback callback) {
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/auth/setSecurityCode");
        sb.append("?security_code=");
        sb.append(safeCode);
        sb.append("&token=");
        sb.append(token);
        String url = sb.toString();
        LogUtils.e("url", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 验证安全码
     */
    public static void postValidateSafeCode(String token, String safeCode, StringCallback callback) {
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/auth/validateSecurityCode");
        sb.append("?security_code=");
        sb.append(safeCode);
        sb.append("&token=");
        sb.append(token);
        String url = sb.toString();
        LogUtils.e("url", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 修改安全码
     */
    public static void postUpdateSafeCode(String token, String OldSafeCode, String newSafeCode, StringCallback callback) {
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/auth/updateSecurityCode");
        sb.append("?old_security_code=");
        sb.append(OldSafeCode);
        sb.append("&new_security_code=");
        sb.append(newSafeCode);
        sb.append("&token=");
        sb.append(token);
        String url = sb.toString();
        LogUtils.e("url", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 获取手机验证码
     */
    public static void getUpdateUserVerification(String phone, StringCallback callback) {
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/auth/sendVerification");
        sb.append("?phone=");
        sb.append(phone);
        String url = sb.toString();
        LogUtils.e("url", url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }


    /**
     * 订单详情页确认收货
     *
     * @param orderNumber
     * @param callback
     */
    public static void confirmGoodsReceipt(String orderNumber, StringCallback callback) {

        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/order/confirm/")
                .append(orderNumber)
                .append("?token=")
                .append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);

    }

    /**
     * 订单详情页订单删除
     *
     * @param orderNumber
     * @param callback
     */
    public static void deleteOrder(String orderNumber, StringCallback callback) {

        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/order/delete/")
                .append(orderNumber)
                .append("?token=")
                .append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);

    }

    /**
     * 修改个人信息
     *
     * @param parameterMap
     * @param callback
     */

    public static void updateUserMessage(Map parameterMap, String token, StringCallback callback) {
        boolean isFirst = true;
        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/auth/setMemberInfo");

        Set<String> keys = parameterMap.keySet();
        LogUtils.e("set lenght---", keys.size() + "");

        Iterator it = parameterMap.keySet().iterator();

        do {
            String key = (String) it.next();

            if (isFirst) {
                sb.append("?");
                isFirst = false;
            } else {
                sb.append("&");
            }
            sb.append(key);
            sb.append("=");
            sb.append(parameterMap.get(key));
        } while (it.hasNext());

        if (isFirst) {
            sb.append("?");
            isFirst = false;
        } else {
            sb.append("&");
        }
        sb.append("token");
        sb.append("=");
        sb.append(token);

        String url = sb.toString();
        LogUtils.e("updateUserUrl--", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);

    }


    /**
     * 发送重置安全码的邮件
     *
     * @param token
     * @param email
     * @param callback
     */

    public static void sendEmailResetCode(String token, String email, StringCallback callback) {
        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/auth/sendResetSecurityCodeByEmail/");
        sb.append(email);
        sb.append("?token=");
        sb.append(token);

        String url = sb.toString();
        LogUtils.e("SendEmailResetCode--", url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);

    }

    /**
     * 登录
     *
     * @param phone
     * @param password
     * @param callback
     */
    public static void postLogin(String phone, String password, String verification, StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/auth/login");
        sb.append("?phone=").append(phone);
        sb.append("&password=").append(password);

        if (!TextUtils.isEmpty(verification)) {
            sb.append("&invitation_code=").append(verification);
        }

        String url = sb.toString();
        LogUtils.e(TAG, "postLogin: " + url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);

    }

    /**
     * 绑定手机号
     *
     * @param phone
     * @param callback
     */

    public static void getLoginVerification(String phone, StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/auth/getRandPassword");
        sb.append("?phone=");
        sb.append(phone);

        String url = sb.toString();
        LogUtils.e("postLogin--", url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);

    }


    /**
     * 积分充值
     *
     * @param token
     * @param callback
     */

    public static void postIntegralPay(String token, int money, StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/recharge/integration");
        sb.append("?token=");
        sb.append(token);
        sb.append("&amount=");
        sb.append(money);

        String url = sb.toString();
        LogUtils.e("postLogin--", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);

    }

    /**
     * 充值
     *
     * @param token
     * @param callback
     */

    public static void postRechargePay(String token, int money, StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/recharge/balance");
        sb.append("?token=");
        sb.append(token);
        sb.append("&amount=");
        sb.append(money);

        String url = sb.toString();
        LogUtils.e("postLogin--", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);

    }


    /**
     * UU充值
     *
     * @param token
     * @param callback
     */

    public static void postGoldCodePay(String token, double money, int isIntegral, StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/recharge/gold");
        sb.append("?token=");
        sb.append(token);
        sb.append("&amount=");
        sb.append(money);
        sb.append("&use_integration=");
        sb.append(isIntegral);

        String url = sb.toString();
        LogUtils.e("postLogin--", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);

    }


    /**
     * 意见反馈接口
     *
     * @param token
     * @param type
     * @param content
     * @param callback
     */

    public static void postSubmitOpinion(String token, int type, String content, StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/feedback/submit");
        sb.append("?token=");
        sb.append(token);
        sb.append("&type=");
        sb.append(type);
        sb.append("&content=");
        sb.append(content);

        String url = sb.toString();
        LogUtils.e("postSubmitOpinion--", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);

    }

    //不带token的意见反馈
    public static void postSubmitOpinion(int type, String content, StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/feedback/submit");
        sb.append("?type=");
        sb.append(type);
        sb.append("&content=");
        sb.append(content);

        String url = sb.toString();
        LogUtils.e("postSubmitOpinion--", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);

    }

    /**
     * 退款进度（退款详情页）数据请求
     *
     * @param orderNumber
     * @param callback
     */
    public static void getOrderReturn(String orderNumber, StringCallback callback) {

        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/order/queryRefundState/").append(orderNumber);
        sb.append("?token=").append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.d(TAG, "getOrderReturn: " + url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 获取最新版本
     */
    public static void getLastVersion(StringCallback callback) {

        final String url = API_POINT + "api/v1/package/lastVersion";

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);

    }

    /**
     * 获取提现信息
     *
     * @param callback
     */
    public static void getWithdraw(StringCallback callback) {

        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/member/getDrawingsInfo");
        sb.append("?token=").append(CurrentUserManager.getUserToken());
        String url = sb.toString();
        Log.e("getWithdraw", url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 提现
     */
    public static void withdraw(String token, double amount, String userName, String safeCode, DataCallback callback) {
        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/transfer");
        sb.append("?token=").append(token);
//        sb.append("&open_id=").append(openID);
        sb.append("&amount=").append(amount);
        sb.append("&user_name=").append(userName);
        sb.append("&security_code=").append(safeCode);
        String url = sb.toString();
        LogUtils.e("withdraw_url", "" + url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * UU充值成功页获取UU数量
     *
     * @param callback
     */
    public static void getMineRechargeSuccessGoldCoin(StringCallback callback) {

        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/auth/memberDetail")
                .append("?token=").append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.d(TAG, "getMineRechargeSuccessGoldCoin: " + url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);

    }

    /**
     * 获取物流数据
     *
     * @param orderNumber
     * @param callback
     */
    public static void getOrderDetailExpressDetail(String orderNumber, StringCallback callback) {
        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/order/catYundaProgress/");
        sb.append(orderNumber);
        sb.append("?token=").append(CurrentUserManager.getUserToken());
        String url = sb.toString();
        LogUtils.e("getOrderDetailExpressDetail url:", "" + url);

        LogUtils.d(TAG, "getOrderDetailExpressDetail: " + url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 获取提现记录
     *
     * @param callback
     */
    public static void getWithdrawRecord(int page, StringCallback callback) {
        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/memberDrawingslog/memberDetail");
        sb.append("?page=").append(page);
        sb.append("&token=").append(CurrentUserManager.getUserToken());
        String url = sb.toString();
        LogUtils.e("getWithdrawRecord url:", "" + url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 获取积分详情
     *
     * @param callback
     */
    public static void getIntegralDetail(int page, StringCallback callback) {
        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/member/getIntegrationUpdateDetail");
        sb.append("?page=").append(page);
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());
        //test
//        sb.append("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIxMTEsImlzcyI6Imh0dHA6XC9cL3Rlc3RhcGkuYmphaXlvdXlvdS5jb21cL2FwaVwvdjFcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNDc5NDYyNTk4LCJleHAiOjE0ODIwNTQ1OTgsIm5iZiI6MTQ3OTQ2MjU5OCwianRpIjoiNzY0N2VkOTczNTE4NjMxM2Q5N2Y1ZDdmMzc0MjBlNTgifQ.1fPWO1LwXfJoH9wIiM9iIfOVxnPwSgagncUQh_P99pg");
        String url = sb.toString();
        LogUtils.e("getIntegerDetail url:", "" + url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 获取UU详情
     *
     * @param callback
     */
    public static void getUuDetail(int page, StringCallback callback) {
        StringBuilder sb = new StringBuilder(API_POINT);
        sb.append("api/v1/member/getUUupdateDetail");
        sb.append("?page=").append(page);
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());
        //test
//        sb.append("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIxMTEsImlzcyI6Imh0dHA6XC9cL3Rlc3RhcGkuYmphaXlvdXlvdS5jb21cL2FwaVwvdjFcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNDc5NDYyNTk4LCJleHAiOjE0ODIwNTQ1OTgsIm5iZiI6MTQ3OTQ2MjU5OCwianRpIjoiNzY0N2VkOTczNTE4NjMxM2Q5N2Y1ZDdmMzc0MjBlNTgifQ.1fPWO1LwXfJoH9wIiM9iIfOVxnPwSgagncUQh_P99pg");
        String url = sb.toString();
        LogUtils.e("getUuDetail url:", "" + url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 获取任务页签到信息
     *
     * @param callback
     */
    public static void getTaskSignInfo(StringCallback callback) {

        String userToken = CurrentUserManager.getUserToken();
        String url = ClientAPI.API_POINT + "api/v1/auth/getSignInInfo" + "?token=" + userToken;

        LogUtils.d(TAG, "getTaskSignInfo: " + url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 获取任务页广告
     *
     * @param token
     * @param callback
     */
    public static void getTaskAD(String token, int pageno, StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/task/index2?page=");
        sb.append(pageno);

        if (!TextUtils.isEmpty(token)) {
            sb.append("&token=").append(token);
        }

        String url = sb.toString();

        LogUtils.d(TAG, "getTaskAD.url: " + url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * 获取我的收益信息
     *
     * @param callback
     */
    public static void getUserIncome(String TAG, StringCallback callback) {


        String token = CurrentUserManager.getUserToken();
        getUserData(token, callback);

//        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
//        sb.append("api/v1/auth/memberDetail");
//        sb.append("?token=");
//        sb.append(token);
//        String url = sb.toString();
//
//        LogUtils.d(TAG, "getUserIncome.url: " + url);
//
//        OkHttpUtils.get()
//                .url(url)
//                .tag(TAG)
//                .build()
//                .execute(callback);
    }

    /**
     * 提取收益
     *
     * @param TAG      TAG
     * @param openId   微信用户唯一标识
     * @param amount   提现金额
     * @param name     微信用户钱包认证身份证姓名
     * @param safecode 安全码
     * @param callback
     */
    public static void postWithDraw(String TAG, String openId, int amount, String name, String safecode, StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/drawing_push_money");
        sb.append("?token=").append(CurrentUserManager.getUserToken());
        sb.append("&open_id=").append(openId);
        sb.append("&amount=").append(amount);
        sb.append("&user_name=").append(name);
        sb.append("&security_code=").append(safecode);
        String url = sb.toString();

        LogUtils.d(TAG, "postWithDraw: " + url);

        OkHttpUtils.post()
                .url(url)
                .tag(TAG)
                .build()
                .execute(callback);
    }

    /**
     * 加载邮费数据
     *
     * @param TAG
     * @param address
     * @param weight
     * @param callback
     */
    public static void getPostage(String TAG, String address, double weight, StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/order/getPostage");
        sb.append("?address=" + address);
        sb.append("&weight=" + weight);
        sb.append("&token=" + CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.d(ClientAPI.TAG, "getPostage: " + url);
        LogUtils.d(ClientAPI.TAG, "getPostage: " + "address: " + address + ", weight: " + weight);

        OkHttpUtils.post()
                .url(url)
//                .addParams("address", address)
//                .addParams("weight", "" + weight)
//                .addParams("token", CurrentUserManager.getUserToken())
                .build()
                .execute(callback);
    }

    /**
     * 用户头像修改
     *
     * @param file
     * @param callback
     */
    public static void postUserImg(File file, StringCallback callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/auth/setMemberAvatar");
        sb.append("?token=" + CurrentUserManager.getUserToken());
        sb.append("&avatar_file_name=");
        sb.append("file_name");
        String url = sb.toString();

        LogUtils.e(ClientAPI.TAG, "postUserImg: " + url);

        OkHttpUtils.post()
                .url(url)
                .addFile("userImg_", "userImg.jpg", file)
                .build()
                .execute(callback);
    }

    /**
     * 邀请好友-我邀请的
     *
     * @param requestTAG
     * @param callback
     */
    public static void getInviteByMe(String requestTAG, StringCallback callback) {

        String userToken = CurrentUserManager.getUserToken();
        String url = ClientAPI.API_POINT + "api/v1/auth/getInvitedByMe" + "?token=" + userToken;
        LogUtils.d(TAG, "getInviteByMe: " + url);

        OkHttpUtils.get()
                .url(url)
                .tag(requestTAG)
                .build()
                .execute(callback);
    }

    /**
     * 任务页 - 同步积分
     *
     * @param pageTag
     * @param callback
     */
    public static void taskSync(String pageTag, StringCallback callback) {
        String userToken = CurrentUserManager.getUserToken();
        String url = ClientAPI.API_POINT + "v1/member/synchronize" + "?token=" + userToken;

        LogUtils.d(TAG, "taskSync: " + url);

        OkHttpUtils.get()
                .url(url)
                .tag(pageTag)
                .build()
                .execute(callback);
    }

    /**
     * 地址管理页
     *
     * @param callback
     */
    public static void getAddressList(String pageTag, StringCallback callback) {

        String url = ClientAPI.API_POINT + "api/v1/member/allAddress?token=" + CurrentUserManager.getUserToken();

        LogUtils.d(TAG, "getAddressList:" + url);

        OkHttpUtils.get()
                .url(url)
                .tag(pageTag)
                .build()
                .execute(callback);
    }

    /**
     * 订单详情页数据
     *
     * @param pageTag
     * @param orderNumber
     * @param callback
     */
    public static void getOrderDetail(String pageTag, String orderNumber, StringCallback callback) {

        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/order/queryByOrderNumber/")
                .append(orderNumber)
                .append("?token=")
                .append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.e(TAG, "getOrderDetail: " + url);

        GetBuilder builder = OkHttpUtils.get().url(url);
        if (!TextUtils.isEmpty(pageTag)) {
            builder.tag(pageTag);
        }
        builder.build().execute(callback);
    }

    /**
     * 确认订单页地址
     *
     * @param callback
     */
    public static void getOrderMakeAddressList(String pageTag, StringCallback callback) {
        getAddressList(pageTag, callback);

    }

}
