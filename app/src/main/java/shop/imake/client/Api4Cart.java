package shop.imake.client;

import shop.imake.callback.DataCallback;
import shop.imake.model.OrderMakeOrderNumberModel;
import shop.imake.model.PayTypeModel;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.LogUtils;

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

    /**
     * 获取支付方式列表
     * @param hasBalance 是否获取余额支付
     * @param callback
     */
    public void getPayWayList(boolean hasBalance, DataCallback<PayTypeModel> callback){
        StringBuilder stringBuilder = new StringBuilder(ClientAPI.API_POINT);
        stringBuilder.append("api/v1/paytype");
        if (!hasBalance){
            stringBuilder.append("/a");
        }
        String url = stringBuilder.toString();
        LogUtils.d(TAG, "getPayWayList url: " + url);
        doGet(url, null, callback);
    }
}
