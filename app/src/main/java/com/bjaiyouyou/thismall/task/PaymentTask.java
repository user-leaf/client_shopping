package com.bjaiyouyou.thismall.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.activity.MineMemberCenterActivity;
import com.bjaiyouyou.thismall.activity.MineMemberCenterIntegralPayActivity;
import com.bjaiyouyou.thismall.client.HttpUrls;
import com.bjaiyouyou.thismall.fragment.TaskPage;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.widget.LoadingDialog;
import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Ping++ 支付任务
 * Created by Administrator on 2016/11/4.
 */
public class PaymentTask extends AsyncTask<PaymentTask.PaymentRequest, Void, String> {

    private static final java.lang.String TAG = PaymentTask.class.getSimpleName();

    private Activity activity;
    private Fragment fragment;
    // 订单号
    private String orderNo;
    // 支付方式
    private String channel;
    // 去支付点击控件
    private View clickView;
    // 调用此任务的类
    private String className;
    private Context context;
    private LoadingDialog loadingDialog;

    /**
     *
     * @param context   上下文,用于dialog的显示
     * @param activity
     * @param orderNo   订单号
     * @param channel   支付渠道
     * @param clickView 点击的按键,用于按键点击之后的禁用，防止重复点击
     * @param className 创建此任务的类名，用于根据类名走不同的URL
     */
    public PaymentTask(Context context, Activity activity, String orderNo, String channel, View clickView, String className) {
        this.activity = activity;
        this.orderNo = orderNo;
        this.channel = channel;
        this.clickView = clickView;
        this.className = className;
        this.context = context;
        loadingDialog = new LoadingDialog(context);
    }

    /**
     *
     * @param context   上下文,用于dialog的显示
     * @param fragment
     * @param orderNo   订单号
     * @param channel   支付渠道
     * @param clickView 点击的按键,用于按键点击之后的禁用，防止重复点击
     * @param className 创建此任务的类名，用于根据类名走不同的URL
     */
    public PaymentTask(Context context, Fragment fragment, String orderNo, String channel, View clickView, String className) {
        this.context = context;
        this.fragment = fragment;
        this.orderNo = orderNo;
        this.channel = channel;
        this.clickView = clickView;
        this.className = className;
        loadingDialog = new LoadingDialog(context);
    }

    @Override
    protected void onPreExecute() {
        loadingDialog.show();
        //按键点击之后的禁用，防止重复点击
        clickView.setOnClickListener(null);
    }

    @Override
    protected String doInBackground(PaymentRequest... pr) {

        PaymentRequest paymentRequest = pr[0];
        String data = null;
        String json = new Gson().toJson(paymentRequest);
        try {
            //向Your Ping++ Server SDK请求数据
//                String URL = Constants.PingppURL+"?token="+CurrentUserManager.getUserToken() + "&orderNo=" + orderNo;

            LogUtils.d(TAG, "activity: " + activity + "fragment: " + fragment + "orderNo = " + orderNo + ", channel = " + channel + ", className = " + className);

            String URL = "";

            // 任务页
            if (className.equals(TaskPage.TAG)) {
                StringBuilder sb = new StringBuilder(HttpUrls.URL_PAY_VIP);
                sb.append("?token=").append(CurrentUserManager.getUserToken());
                sb.append("&channel=").append(channel);
                URL = sb.toString();
            }

            else if (className.equals(MineMemberCenterIntegralPayActivity.TAG)){
                StringBuilder sb = new StringBuilder(Constants.PingppURL);
                sb.append("?token=").append(CurrentUserManager.getUserToken());
                sb.append("&orderNo=").append(orderNo);
                sb.append("&rechargeType=").append("integration");
                sb.append("&channel=").append(channel);
                 URL = sb.toString();
            }
            else if (className.equals(MineMemberCenterActivity.TAG)){
                StringBuilder sb = new StringBuilder(Constants.PingppURL);
                sb.append("?token=").append(CurrentUserManager.getUserToken());
                sb.append("&orderNo=").append(orderNo);
                sb.append("&rechargeType=").append("gold");
                sb.append("&channel=").append(channel);
                 URL = sb.toString();
            }


            /**
             * 其它页面：订单详情页、确认订单页
             */
            else {
                StringBuilder sb = new StringBuilder(Constants.PingppURL);
                sb.append("?token=").append(CurrentUserManager.getUserToken());
                sb.append("&orderNo=").append(orderNo);
                sb.append("&channel=").append(channel);
                URL = sb.toString();
            }

            LogUtils.d(TAG, "payUrl: " + URL);

            data = postJson(URL, json);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获得服务端的charge，调用ping++ sdk。
     */
    @Override
    protected void onPostExecute(String data) {
        loadingDialog.dismiss();
        if (null == data) {
            showMsg("请求出错", "请检查URL", "URL无法获取charge");
            return;
        }
        Log.d("charge", data);
//            Pingpp.createPayment(ClientSDKActivity.this, data);
        //QQ钱包调起支付方式  “qwalletXXXXXXX”需与AndroidManifest.xml中的data值一致
        //建议填写规则:qwallet + APP_ID
        //==iuu edit==
        if (activity != null) {
            Pingpp.createPayment(activity, data, "qwalletXXXXXXX");
        }else if (fragment != null){
            Pingpp.createPayment(fragment, data, "qwalletXXXXXXX");
        }
    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    private static String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        // post方式
        Request request = new Request.Builder().url(url).post(body).build();

        // get方式
//        Log.d("OrderDetailActivity: ", url);
//        Request request = new Request.Builder().url(url).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

//        LogUtils.d(TAG, "response.body() = " + response.body().string()); // 这句代码会导致，再次获取response.body().string()时拿不到，下一句return null！！
        String responseBody = response.body().string();
        LogUtils.d(TAG, "responseBody: " + responseBody);
        return responseBody;
//        return "{\"id\":\"ch_ezPGWP0GaXbDDS0ybL8KWDO8\",\"object\":\"charge\",\"created\":1473146951,\"livemode\":true,\"paid\":false,\"refunded\":false,\"app\":\"app_SynjLKu1Si5Czrnz\",\"channel\":\"wx\",\"order_no\":\"2016090550535349\",\"client_ip\":\"106.39.193.120\",\"amount\":1,\"amount_settle\":1,\"currency\":\"cny\",\"subject\":\"orderNo : 2016090550535349\",\"body\":\"adsfadf\",\"extra\":[],\"time_paid\":null,\"time_expire\":1473154151,\"time_settle\":null,\"transaction_no\":null,\"refunds\":{\"object\":\"list\",\"url\":\"/v1/charges/ch_ezPGWP0GaXbDDS0ybL8KWDO8/refunds\",\"has_more\":false,\"data\":[]},\"amount_refunded\":0,\"failure_code\":null,\"failure_msg\":null,\"metadata\":[],\"credential\":{\"object\":\"credential\",\"wx\":{\"appId\":\"wxa4650166adbfdcc1\",\"partnerId\":\"1383715402\",\"prepayId\":\"wx20160906152912ba7e15ede20721592606\",\"nonceStr\":\"5be48dae595399ea42e479c679c26aa8\",\"timeStamp\":1473146952,\"packageValue\":\"Sign=WXPay\",\"sign\":\"5384B61FE9EA36D6D41CCC9A9D7B5816\"}},\"description\":null}";
//        return response.body().string();
    }

    public static class PaymentRequest {
        String channel;
        int amount;

        public PaymentRequest(String channel, int amount) {
            this.channel = channel;
            this.amount = amount;
        }
    }

}
