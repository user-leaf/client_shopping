package shop.imake.task;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import shop.imake.ActivityCollector;
import shop.imake.Constants;
import shop.imake.activity.MineMemberCenterActivity;
import shop.imake.activity.MineMemberCenterIntegralPayActivity;
import shop.imake.activity.RechargeActivity;
import shop.imake.activity.TelephoneFeeChargeActivity;
import shop.imake.client.HttpUrls;
import shop.imake.fragment.TaskPage;
import shop.imake.model.PayResultEvent;
import shop.imake.model.PaymentTaskModel;
import shop.imake.model.ResponseModel;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DialogUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.StringUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.widget.LoadingDialog;

/**
 * Ping++ 支付任务
 * Created by Administrator on 2016/11/4.
 */
public class PaymentTask extends AsyncTask<PaymentTask.PaymentRequest, Void, String> {

    private static final java.lang.String TAG = PaymentTask.class.getSimpleName();
    // 响应码
    private static int responseCode;

    private Context context;
    private Activity activity;
    private Fragment fragment;
    // 订单号
    private String orderNo;
    // 支付渠道
    private String channel;
    // 去支付点击控件
    private View clickView;
    // 调用此任务的类
    private String className;
    private LoadingDialog loadingDialog;
    private Map<String,Object> mParamsMap;

    /**
     * @param context   上下文,用于dialog的显示
     * @param activity
     * @param orderNo   订单号
     * @param channel   支付渠道
     * @param clickView 点击的按键,用于按键点击之后的禁用，防止重复点击
     * @param className 创建此任务的类名，用于根据类名走不同的URL
     * @param params
     */
    public PaymentTask(Context context, Activity activity, String orderNo, String channel, View clickView, String className, Map<String, Object> params) {
        this.context = context;
        this.activity = activity;
        this.orderNo = orderNo;
        this.channel = channel;
        this.clickView = clickView;
        this.className = className;
        this.mParamsMap=params;
        loadingDialog = LoadingDialog.getInstance(context);
    }

    /**
     * @param context   上下文,用于dialog的显示
     * @param fragment
     * @param orderNo   订单号
     * @param channel   支付渠道
     * @param clickView 点击的按键,用于按键点击之后的禁用，防止重复点击
     * @param className 创建此任务的类名，用于根据类名走不同的URL
     * @param params
     */
    public PaymentTask(Context context, Fragment fragment, String orderNo, String channel, View clickView, String className, Map<String, Object> params) {
        this.context = context;
        this.fragment = fragment;
        this.orderNo = orderNo;
        this.channel = channel;
        this.clickView = clickView;
        this.className = className;
        this.mParamsMap=params;
        loadingDialog = LoadingDialog.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        loadingDialog.show();
        //按键点击之后的禁用，防止重复点击
//        clickView.setOnClickListener(null);
    }

    @Override
    protected String doInBackground(PaymentRequest... pr) {

        PaymentRequest paymentRequest = pr[0];
        String data = null;
        String json = new Gson().toJson(paymentRequest);
        try {
            //向Your Ping++ Server SDK请求数据
            LogUtils.d(TAG, "activity: " + activity + "fragment: " + fragment + "orderNo = " + orderNo + ", channel = " + channel + ", className = " + className);
            String URL = "";

            // 任务页
            if (className.equals(TaskPage.TAG)) {
                StringBuilder sb = new StringBuilder(HttpUrls.URL_PAY_VIP);
                sb.append("?token=").append(CurrentUserManager.getUserToken());
                sb.append("&channel=").append(channel);
                URL = sb.toString();
                //话费充值
            } else if (className.equals(TelephoneFeeChargeActivity.TAG)){
                StringBuilder sb = new StringBuilder(HttpUrls.URL_PAY_TEL);
                String amout= (String) mParamsMap.get(TelephoneFeeChargeActivity.AMOUNT);
                String tel= (String) mParamsMap.get(TelephoneFeeChargeActivity.TEL);

                sb.append("?token=").append(CurrentUserManager.getUserToken());
                sb.append("&payment_channel=").append(channel);
                sb.append("&amount=").append(amout);
                sb.append("&tel=").append(tel);
                URL = sb.toString();

            }
            else if (className.equals(MineMemberCenterIntegralPayActivity.TAG)) {
                StringBuilder sb = new StringBuilder(Constants.PingppURL);
                sb.append("?token=").append(CurrentUserManager.getUserToken());
                sb.append("&orderNo=").append(orderNo);
                sb.append("&rechargeType=").append("integration");
                sb.append("&channel=").append(channel);
                URL = sb.toString();
            } else if (className.equals(MineMemberCenterActivity.TAG)) {
                StringBuilder sb = new StringBuilder(Constants.PingppURL);
                sb.append("?token=").append(CurrentUserManager.getUserToken());
                sb.append("&orderNo=").append(orderNo);
                sb.append("&rechargeType=").append("gold");
                sb.append("&channel=").append(channel);
                URL = sb.toString();
            } else if (className.equals(RechargeActivity.TAG)) {
                StringBuilder sb = new StringBuilder(Constants.PingppURL);
                sb.append("?token=").append(CurrentUserManager.getUserToken());
                sb.append("&orderNo=").append(orderNo);
                sb.append("&rechargeType=").append("balance");
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
            LogUtils.e("paymenttask", e.toString() + "");
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
            showMsg("请求出错或请求超时", null, null);
            return;
        }

        LogUtils.d(TAG, "charge: " + data);
        // Pingpp.createPayment(ClientSDKActivity.this, data);
        // QQ钱包调起支付方式  “qwalletXXXXXXX”需与AndroidManifest.xml中的data值一致
        // 建议填写规则:qwallet + APP_ID

        LogUtils.d(TAG, "onPostExecute code: " + responseCode);

        if (Constants.CHANNEL_BALANCE.equals(channel)) { // 余额支付
            Gson gson = new Gson();
            if (responseCode == 200) {
                try {
                    PaymentTaskModel paymentTaskModel = gson.fromJson(data, PaymentTaskModel.class);
                    boolean paySuccess = paymentTaskModel.isPaid();
                    String status = paymentTaskModel.getStatus();
                    if (paySuccess && "paid".equals(status)) {
                        ToastUtils.showShort("支付成功");
                        EventBus.getDefault().post(new PayResultEvent(true));
                    }

                } catch (Exception e) {
                    ToastUtils.showShort("解析出错[1]");
                }

            } else {
                try {
                    ResponseModel responseModel = gson.fromJson(data, ResponseModel.class);

                    if (100009 == responseModel.getCode()) { // 兑换券余额不足，是否充值

                        final Dialog confirmDialog = DialogUtils.createConfirmDialog(
                                context,
                                null,
                                responseModel.getMessage(),
                                "确定",
                                null,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                },
                                null
                        );
                        confirmDialog.show();
                    } else {
                        ToastUtils.showShort(StringUtils.getExceptionMessage(responseModel.getMessage()));
                    }
                } catch (Exception e1) {
                    ToastUtils.showShort("解析出错[2]");

                }

            }

        } else {
            if (activity != null) {
                Pingpp.createPayment(activity, data, "qwalletXXXXXXX");
            } else if (fragment != null) {
                Pingpp.createPayment(fragment, data, "qwalletXXXXXXX");
            }

        }
    }

    private void showMsg(String msg0, String msg1, String msg2) {
        String str = msg0;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage(str);
//        builder.setTitle("提示");
//        builder.setPositiveButton("知道了", null);
//        builder.create().show();

        Dialog tipDialog = DialogUtils.createConfirmDialog(context, "提示", str, "知道了", null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, null);

        if (ActivityCollector.sActivities.contains(context) && !tipDialog.isShowing()) {
            tipDialog.show();
        }
    }

    private static String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);

        // post方式
        Request request = new Request.Builder().url(url).post(body).build();
        OkHttpClient client = getOkHttpClient();
        Response response = client.newCall(request).execute();

//        LogUtils.d(TAG, "response.body() = " + response.body().string()); // 这句代码会导致，再次获取response.body().string()时拿不到，下一句return null！！
        responseCode = response.code();
        String responseBody = response.body().string();
        LogUtils.d(TAG, "responseBody: " + responseBody);
        return responseBody;
    }

    public static class PaymentRequest {
        String channel;
        int amount;

        public PaymentRequest(String channel, int amount) {
            this.channel = channel;
            this.amount = amount;
        }
    }

    private static OkHttpClient getOkHttpClient() {
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory = null;

        trustManager = new X509TrustManager() {

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        };

        try {
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new X509TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
        return client;
    }
}
