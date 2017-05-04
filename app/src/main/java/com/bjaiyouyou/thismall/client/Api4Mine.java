package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.fragment.MinePage;
import com.bjaiyouyou.thismall.model.BindingAlipayModel;
import com.bjaiyouyou.thismall.model.CheckIfBindingAlipayModel;
import com.bjaiyouyou.thismall.model.ContactMemberModel;
import com.bjaiyouyou.thismall.model.User;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

/**
 * 个人中心模块网络请求
 */
public class Api4Mine extends BaseClientApi {

    /**
     * 获取用户个人信息
     * @param strTag
     * @param callback
     */
    public void getUserMessage(String strTag, DataCallback<User> callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/auth/memberDetail")
                .append("?token=")
                .append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.d(TAG, "getUserMessage: " + url);
        LogUtils.e(TAG, "getUserMessage: " + url);

        doGet(url, strTag, null, callback);
    }

    /**
     * 邀请好友页数据请求
     *
     * @param strTag
     * @param callback
     */
    public void getContactsInfo(String strTag, Map<String, String> params, DataCallback<ContactMemberModel> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/auth/isMemberByContacts")
                .append("?token=")
                .append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        doPost(url, strTag, params, callback);
    }



    /**
     * 判断是否绑定支付宝
     *author Qxh
     *created at 2017/3/31 21:59
     */
    public void getIfBindingAlipay(DataCallback<CheckIfBindingAlipayModel> callback){
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/member/checkIsBoundAlipay");
        sb.append("?token=");
        sb.append(CurrentUserManager.getUserToken());

        String url = sb.toString();
        LogUtils.d("url", url);
        doGet(url, MinePage.TAG, null, callback);

    }

    /**
     * 获得支付宝参数
     *author Qxh
     *created at 2017/3/31 21:59
     */
    public void getAuthorizationParameters( StringCallback callback){
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/ali/appAuthInfo");

        String url = sb.toString();
        LogUtils.d("url", url);
        doGet(url, MinePage.TAG, null, callback);

    }
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
//        doGet(url, MinePage.TAG, null, callback);
//
//    }
    /**
     * 绑定支付宝
     *author Qxh
     *created at 2017/3/31 21:59
     */
    public void bindingAlipay(String userId,DataCallback<BindingAlipayModel> callback){
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/member/boundAlipay");
        sb.append("?alipay_id=");
        sb.append(userId);
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());

        String url = sb.toString();
        LogUtils.e("绑定支付宝地址",url);
        LogUtils.d("url", url);
        doGet(url, MinePage.TAG, null, callback);

    }

    /**
     * 兑换收益
     * @param TAG
     * @param amount
     * @param name
     * @param safecode
     * @param callback
     */
    public void withdrawIncome(String TAG, int amount, String name, String safecode, StringCallback callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/drawing_push_money");
        sb.append("?token=").append(CurrentUserManager.getUserToken());
//        sb.append("&open_id=").append(openId);
        sb.append("&amount=").append(amount);
        sb.append("&user_name=").append(name);
        sb.append("&security_code=").append(safecode);
        String url = sb.toString();

        LogUtils.d(TAG, "withdrawIncome: " + url);

        doPost(url, TAG, null, callback);
    }
}
