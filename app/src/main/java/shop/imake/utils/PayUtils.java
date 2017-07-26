package shop.imake.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import shop.imake.Constants;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4Cart;
import shop.imake.client.Api4Task;
import shop.imake.client.ClientApiHelper;
import shop.imake.fragment.PayDetailFragment;
import shop.imake.model.PayTypeModel;
import shop.imake.task.PaymentTask;
import shop.imake.widget.LoadingDialog;

/**
 * Created by Administrator on 2017/4/21.
 */
public class PayUtils {
    public static final String EXTRA_MONEY = "money";
    public static final String EXTRA_ORDER_NUM = "order_num";

    /**
     * 支付
     *
     * @param fragmentManager
     * @param tag             Fragment的TAG，根据TAG，fragmentManager可查找到该fragment
     * @param money           支付金额
     */
    public static void pay(FragmentManager fragmentManager, String tag, double money, PayDetailFragment.PayCallback callback) {
        PayDetailFragment payDetailFragment = new PayDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(EXTRA_MONEY, money);
        payDetailFragment.setArguments(bundle);
        payDetailFragment.setOnPayCallback(callback);
        payDetailFragment.show(fragmentManager, tag);
    }


    /**
     * 底部alert支付
     * 没有余额支付
     *
     * @param context    上下文
     * @param isActivity 是否是activity
     * @param activity   环迅支付所需，如果是activity则传activity，如果是fragment则传所在的activity，否则传null
     * @param fragment   如果是fragment则传fragment，否则传null
     * @param orderNum   订单号
     * @param clickView  点击控件，可以传null
     * @param clazzName  类名
     */
    public static void payAlert(final Context context, final boolean isActivity, final Activity activity, final Fragment fragment, final String orderNum, final View clickView, final String clazzName) {
        final Api4Cart api4Cart = (Api4Cart) ClientApiHelper.getInstance().getClientApi(Api4Cart.class);

        final LoadingDialog loadingDialog = LoadingDialog.getInstance(context);
        loadingDialog.show();

        // 获取支付方式列表
        api4Cart.getPayWayList(false, new DataCallback<PayTypeModel>(context) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                ToastUtils.showException(e);
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (response == null) {
                    return;
                }

                PayTypeModel payTypeModel = (PayTypeModel) response;
                List<PayTypeModel.PayTypesBean> pay_types = payTypeModel.getPay_types();
                if (pay_types == null) {
                    return;
                }

                // 得到支付方式列表
                final List<String> payWayNames = new ArrayList<>();
                final Map<String, String> payWayMap = new HashMap<String, String>();
                for (PayTypeModel.PayTypesBean item : pay_types) {
                    payWayNames.add(item.getName());
                    payWayMap.put(item.getName(), item.getPay_param());
                }
                String[] payWays = payWayNames.toArray(new String[payWayNames.size()]);

                // 弹出选择框
                OnItemClickListener onItemClickListener = new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position < 0) {
                            return;
                        }

                        String strPayWay = payWayMap.get(payWayNames.get(position));

                        int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
                        String channel = null;

                        if ("alipay".equals(strPayWay)) {    // 支付宝支付
                            channel = Constants.CHANNEL_ALIPAY;

                        } else if ("wx".equals(strPayWay)) {   // 微信支付
                            channel = Constants.CHANNEL_WECHAT;

                        }

                        if (!isActivity) {  // 在fragment中调用
                            new PaymentTask(
                                    context,
                                    fragment,
                                    orderNum,
                                    channel,
                                    clickView,
                                    clazzName
                            ).execute(new PaymentTask.PaymentRequest(channel, amount));

                        } else {    // 在activity中调用
                            new PaymentTask(
                                    context,
                                    activity,
                                    orderNum,
                                    channel,
                                    clickView,
                                    clazzName
                            ).execute(new PaymentTask.PaymentRequest(channel, amount));

                        }

                    }
                };

                new AlertView.Builder().setContext(context)
                        .setStyle(AlertView.Style.ActionSheet)
                        .setTitle("请选择支付方式")
                        .setMessage(null)
                        .setCancelText("取消")
                        .setOthers(payWays)
                        .setOnItemClickListener(onItemClickListener)
                        .build()
                        .show();
            }
        });
    }
}
