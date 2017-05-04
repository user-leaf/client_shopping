package com.bjaiyouyou.thismall.callback;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.pingplusplus.android.Pingpp;

/**
 * 支付结果
 *
 * Created by kanbin on 2017/3/23.
 */
public class PingppPayResult {

    public static void setOnPayResultCallback(int requestCode, int resultCode, Intent data, OnPayResultCallback callback){

        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");

                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                showMsg(result, errorMsg, extraMsg);

                LogUtils.d("p++", "errorMsg: " + errorMsg + ", extraMsg: " + extraMsg);

                if ("success".equals(result)) {
                    ToastUtils.showShort("支付成功");
                    callback.onPaySuccess();

                } else if ("fail".equals(result)) {
//                    if ("invalid_charge_no_credential".contains(errorMsg)){
                    //无效的交易证书
                    if (!TextUtils.isEmpty(errorMsg)&&errorMsg.contains("invalid_charge_no_credential")){
                        ToastUtils.showLong("支付功能正在升级，暂停支付功能，给您造成不便，敬请谅解");
                    }else {
                        ToastUtils.showShort("支付失败");
                    }
                    callback.onPayFail();
                    LogUtils.e("errorMsg", errorMsg+" **********resultCode:"+resultCode+ "extraMsg" + extraMsg);

                } else if ("cancel".equals(result)) {
                    ToastUtils.showShort("已取消支付");

                } else if ("invalid".equals(result)) {
                    // https://help.pingxx.com/article/115895/
                    ToastUtils.showShort("支付软件未安装或不支持");
                }

            }
        }
    }

//    public void showMsg(String title, String msg1, String msg2) {
//        String str = title;
//        if (null != msg1 && msg1.length() != 0) {
//            str += "\n" + msg1;
//        }
//        if (null != msg2 && msg2.length() != 0) {
//            str += "\n" + msg2;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(OrderMakeActivity.this);
//        builder.setMessage(str);
//        builder.setTitle("提示");
//        builder.setPositiveButton("OK", null);
//        builder.create().show();
//    }
//
//    private static String postJson(String url, String json) throws IOException {
//        MediaType type = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(type, json);
//        Request request = new Request.Builder().url(url).post(body).build();
//
//        OkHttpClient client = new OkHttpClient();
//        Response response = client.newCall(request).execute();
////        LogUtils.d(TAG, "OrderMakeActivity->response.body() = " + response.body().string()); // 这句代码会导致，再次获取response.body().string()时拿不到，下一句return null！！
//        return response.body().string();
//    }
//
//    class PaymentRequest {
//        String channel;
//        int amount;
//
//        public PaymentRequest(String channel, int amount) {
//            this.channel = channel;
//            this.amount = amount;
//        }
//    }

    public interface OnPayResultCallback {
        void onPaySuccess();
        void onPayFail();
    }
}
