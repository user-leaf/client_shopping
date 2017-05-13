package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.activity.MyCommissionActivity;
import com.bjaiyouyou.thismall.activity.MyCommissionDetailActivity;
import com.bjaiyouyou.thismall.activity.MyIncomeActivity;
import com.bjaiyouyou.thismall.activity.MyZhongHuiQuanActivity;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.fragment.MinePage;
import com.bjaiyouyou.thismall.model.BindingAlipayModel;
import com.bjaiyouyou.thismall.model.CheckIfBindingAlipayModel;
import com.bjaiyouyou.thismall.model.CommissionModel;
import com.bjaiyouyou.thismall.model.ContactMemberModel;
import com.bjaiyouyou.thismall.model.MyCommissionModel;
import com.bjaiyouyou.thismall.model.MyIncomeModel;
import com.bjaiyouyou.thismall.model.User;
import com.bjaiyouyou.thismall.model.ZhongHuiQuanModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

/**
 * 个人中心模块网络请求
 */
public class Api4Mine extends BaseClientApi {

    public static final String TAG = Api4Mine.class.getSimpleName();

    /**
     * 获取用户个人信息
     * @param tag
     * @param callback
     */
    public void getUserMessage(Object tag, DataCallback<User> callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/auth/memberDetail")
                .append("?token=")
                .append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.d(TAG, "getUserMessage: " + url);
        LogUtils.e(TAG, "getUserMessage: " + url);

        doGet(url, tag, null, callback);
    }

    /**
     * 邀请好友页数据请求
     *
     * @param tag
     * @param callback
     */
    public void getContactsInfo(Object tag, Map<String, String> params, DataCallback<ContactMemberModel> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/auth/isMemberByContacts")
                .append("?token=")
                .append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        doPost(url, tag, params, callback);
    }



    /**
     * 判断是否绑定支付宝
     *author Alice
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
     *author Alice
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
//     *author Alice
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
     *author Alice
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
     * @param tag
     * @param amount
     * @param name
     * @param safecode
     * @param callback
     */
    public void withdrawIncome(Object tag, int amount, String name, String safecode, StringCallback callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/drawing_push_money");
        sb.append("?token=").append(CurrentUserManager.getUserToken());
//        sb.append("&open_id=").append(openId);
        sb.append("&amount=").append(amount);
        sb.append("&user_name=").append(name);
        sb.append("&security_code=").append(safecode);
        String url = sb.toString();

        LogUtils.d(TAG, "withdrawIncome: " + url);

        doPost(url, tag, null, callback);
    }


    /**
     * 获得我的众汇券数据
     * @param callback
     */
    public void getZhongHuiQuanData(Object tag,DataCallback<ZhongHuiQuanModel> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/member/myZhongHuiQuan");
        sb.append("?token=").append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.d(MyZhongHuiQuanActivity.TAG, "getZhongHuiQuanData: " + url);

        doGet(url,tag, null, callback);
    }
    /**
     * 获得我的佣金数据
     * @param callback
     */
    public void getCommissionData(Object tag,DataCallback<CommissionModel> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/member/myPushMoney");
        sb.append("?token=").append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.d(MyCommissionActivity.TAG, "getCommissionData: " + url);

        doGet(url,tag, null, callback);
    }

    /**
     * 获取 众汇-我的收益页面 数据
     * @param TAG
     */
    public void getMyIncome(Object TAG, DataCallback<MyIncomeModel> callback){
        StringBuilder stringBuilder = new StringBuilder(ClientAPI.API_POINT);
        stringBuilder.append("api/v1/member/pushMoneyDetail")
                .append("?token=").append(CurrentUserManager.getUserToken());

        String url = stringBuilder.toString();
        doGet(url, TAG, null, callback);
    }
    /**
     * 获得我的佣金、我的兑换券、我的佣金的详情
     * @param callback
     */
    public void getMyPropertyData(Object tag,String className,int page,DataCallback<MyCommissionModel> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        if (className!=null){
            if (MyZhongHuiQuanActivity.TAG.equals(className)){
                //mTitle.setTitle("众汇券明细");
                sb.append("api/v1/member/myPushMoney");
            }else if (MyCommissionActivity.TAG.equals(className)){
                //mTitle.setTitle("佣金明细");
                sb.append("api/v1/member/shouyiDetails");
            }else if (MyIncomeActivity.TAG.equals(className)){
                //mTitle.setTitle("收益明细");
                sb.append("api/v1/member/myPushMoney");
            }
        }
        sb.append("?page=").append(page);
        sb.append("&token=").append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.d(MyCommissionDetailActivity.TAG, "getMyPropertyData: " + url);
        doGet(url, tag, null, callback);
    }
}
