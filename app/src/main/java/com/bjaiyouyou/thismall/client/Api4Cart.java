package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.model.OrderMakeOrderNumberModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/21.
 */

public class Api4Cart extends BaseClientApi {

    /**
     * 生成订单
     * @param url
     * @param tag
     * @param params
     * @param callback
     */
    public void commitOrder(String url, Object tag, Map<String, String> params, DataCallback<OrderMakeOrderNumberModel> callback){
        doPost(url, tag, params, callback);
    }

    /**
     * 弹窗支付 - 判断是否设置了安全码
     * @param callback
     */
    public void isSafeCodeEmpty(StringCallback callback) {

        StringBuilder stringBuilder = new StringBuilder(ClientAPI.API_POINT);
        stringBuilder.append("api/v1/member/getIsSetSecurityCode")
                .append("?token=").append(CurrentUserManager.getUserToken());
        String url = stringBuilder.toString();

        doGet(url, null, callback);
    }

    /**
     * 弹窗支付 - 安全码验证
     * @param safeCode
     * @param callback
     */
    public void validateSafeCode(String safeCode, StringCallback callback){
        StringBuilder stringBuilder = new StringBuilder(ClientAPI.API_POINT);
        stringBuilder.append("api/v1/auth/validateSecurityCode")
                .append("?token=").append(CurrentUserManager.getUserToken())
                .append("&security_code=").append(safeCode);
        String url = stringBuilder.toString();

        LogUtils.d(TAG, "validateSafeCode url: " + url);

        doPost(url, null, callback);
    }

    /**
     * 弹窗支付 - 设置安全码
     * @param safeCode
     * @param callback
     */
    public void setSafeCode(String safeCode, StringCallback callback) {
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/auth/setSecurityCode");
        sb.append("?security_code=");
        sb.append(safeCode);
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        doPost(url, null, callback);
    }
}
