package com.bjaiyouyou.thismall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.OnItemClickListener;
import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.OrderPayFailAdapter;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.fragment.MyOrderPaymentFragment;
import com.bjaiyouyou.thismall.fragment.PayDetailFragment;
import com.bjaiyouyou.thismall.model.OrderPayFail;
import com.bjaiyouyou.thismall.task.PaymentTask;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.DoubleTextUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.PayUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.bjaiyouyou.thismall.widget.NoScrollListView;
import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 *
 * @author Alice
 *Creare 2016/6/28 11:32
 *
 * 支付失败页面
 */

public class OrderPayFailActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener,OnItemClickListener{
    public static final String PARAM_ORDER_NUMBER = "mOrderNumber";
    private static String TAG=OrderPayFailActivity.class.getSimpleName();
    // 订单商品列表
    private NoScrollListView mLv;
    //  标题
    private IUUTitleBar mTitleBar;
    //    数据
    private List<OrderPayFail.OrderBean.OrderDetailBean> mData;
    //    适配器
    private OrderPayFailAdapter mAdapter;
    //    取消订单
    private TextView mBtCancle1;
    //    其他支付方式
    private TextView mBtOtherPay1;
    private TextView mBtCancle2;
    private TextView mBtOtherPay2;
    //    获得积分
    private TextView mTvIntegral;
    //    收货用户名字
    private TextView mTVName;
    //    收货电话
    private TextView mTVPhone;
    //    收货地址
    private TextView mTVAddress;
    //    配送方式
    private TextView mDistributionMethod;
    //    消费金额
    private TextView mTVMoney;
    //    订单号
    private TextView mTVOrderNum;
    //    下单日期
    private TextView mTvTime;
    //网络获取订单
    private OrderPayFail.OrderBean mOrder;
    //获得积分
    private TextView mTvGetIntegral;
    //订单号
    private String mOrderNumber;
    //支付方式
    private String mChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay_fail);
        initView();
        initData();
        initControl();
        setViewUp();
    }


    private void initControl() {
        mAdapter=new OrderPayFailAdapter(mData,this);
        mLv.setAdapter(mAdapter);
    }

    private void initView() {
        mLv = ((NoScrollListView) findViewById(R.id.lv_orderpay_fail));

        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_orderpay_fail));

        mBtCancle1 = ((TextView) findViewById(R.id.bt_orderpay_fail_cancle1));
        mBtOtherPay1 = ((TextView) findViewById(R.id.bt_orderpay_fail_otherpay1));
        mBtCancle2 = ((TextView) findViewById(R.id.bt_orderpay_fail_cancle2));
        mBtOtherPay2 = ((TextView) findViewById(R.id.bt_orderpay_fail_otherpay2));

        mTVName = ((TextView) findViewById(R.id.tv_orderpayfail_address_name));
        mTVPhone = ((TextView) findViewById(R.id.tv_orderpayfail_address_phone));
        mTVAddress = ((TextView) findViewById(R.id.tv_orderpayfail_address));
        mDistributionMethod = ((TextView) findViewById(R.id.tv_orderpayfail_distribution_method));
        mTvGetIntegral = ((TextView) findViewById(R.id.tv_orderpayfail_integral));
        mTvIntegral = ((TextView) findViewById(R.id.tv_orderpayfail_pay_integral));
        mTVMoney = ((TextView) findViewById(R.id.tv_orderpayfail_pay_money));
        mTVOrderNum = ((TextView) findViewById(R.id.tv_orderpayfail_order_num));
        mTvTime = ((TextView) findViewById(R.id.tv_orderpayfail_order_time));

    }

    private void setViewUp() {
        mLv.setOnItemClickListener(this);
        mTitleBar.setLeftLayoutClickListener(this);
        mBtCancle1.setOnClickListener(this);
        mBtCancle2.setOnClickListener(this);
        mBtOtherPay1.setOnClickListener(this);
        mBtOtherPay2.setOnClickListener(this);
    }

    /**
     * 获取网络数据
     */
    private void initData() {
        //获取订单编号
        final String token= CurrentUserManager.getUserToken();
        mOrderNumber=getIntent().getStringExtra(PARAM_ORDER_NUMBER);
//        //test
//        orderNumber="2016072553551005";

        mData=new ArrayList<>();
        ClientAPI.getOrderDetailsData(mOrderNumber, token, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
            }
            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response)){
                    mOrder=new Gson().fromJson(response,OrderPayFail.class).getOrder();
                    mData= mOrder.getOrder_detail();
                    mAdapter.clear();
                    mAdapter.addAll(mData);
                    mAdapter.notifyDataSetChanged();
                    setData();
                }
                else {
                    Toast.makeText(getApplicationContext(),"网络加载数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void setData() {
        mTVName.setText(mOrder.getAddressee());
        mTVPhone.setText(mOrder.getPhone());
        mTVAddress.setText(mOrder.getAddress());
        mTvGetIntegral.setText(mOrder.getGet_gold()+"");
        mTvIntegral.setText(mOrder.getDeduct_integration()+"");

        mTVMoney.setText("￥"+DoubleTextUtils.setDoubleUtils(mOrder.getAmount())+"");

        mTVOrderNum.setText(mOrder.getOrder_number());
        mTvTime.setText(mOrder.getCreated_at());
        mDistributionMethod.setText(DoubleTextUtils.setDoubleUtils(mOrder.getPostage())+")");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_layout:
                finish();
                break;
            case R.id.bt_orderpay_fail_cancle1:
//                Toast.makeText(this,"bt_orderpay_fail_cancle1",Toast.LENGTH_SHORT).show();
                cancleOrder();
                break;
            case R.id.bt_orderpay_fail_cancle2:
                cancleOrder();
//                Toast.makeText(this,"bt_orderpay_fail_cancle2",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_orderpay_fail_otherpay1:
                doPayByPingpp();
//                Toast.makeText(this,"bt_orderpay_fail_otherpay1",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_orderpay_fail_otherpay2:
                doPayByPingpp();
//                Toast.makeText(this,"bt_orderpay_fail_otherpay2",Toast.LENGTH_SHORT).show();
                break;

        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this,"商品编号:"+mData.get(position).getProduct().getId(),Toast.LENGTH_SHORT).show();
        long productID=mData.get(position).getProduct_id();
        Intent intent=new Intent(this,GoodsDetailsActivity.class);
        intent.putExtra("productID",productID);
        jump(intent,false);
    }

    /**
     * 取消订单
     */
    private void cancleOrder() {

        final String url= ClientAPI.API_POINT+"api/v1/order/cancel/"
                +mOrderNumber
                +"?token="
                + CurrentUserManager.getUserToken();

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("取消订单：",e.toString().trim());
                        UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("cancleOrder--","cancleOrder--"+mOrderNumber);
                        Toast.makeText(getApplicationContext(),"订单已经取消",Toast.LENGTH_SHORT).show();
                        OrderPayFailActivity.this.finish();
                    }
                });
    }


    ///////////////////////////////////ping++支付
      private void doPayByPingpp() {
        // https://github.com/saiwu-bigkoo/Android-AlertView

//        new AlertView("选择支付方式", null, "取消", null, new String[]{context.getString(R.string.pay_alipay), context.getString(R.string.pay_balance), context.getString(R.string.pay_hx)
//        }, activity, AlertView.Style.ActionSheet, this).show();

        double amount=mOrder.getAll_amount();

        PayUtils.pay(MyOrderActivity.mFragmentManager, MyOrderPaymentFragment.TAG, amount, new PayDetailFragment.PayCallback() {
            @Override
            public void onPayCallback(String channel) {
                int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
                new PaymentTask(
                        getApplicationContext(),
                        OrderPayFailActivity.this,
                        mOrderNumber,
                        channel,
                       mBtOtherPay2,
                        OrderPayFailActivity.TAG
                ).execute(new PaymentTask.PaymentRequest(channel, amount));

            }
        });
    }

    //  https://github.com/saiwu-bigkoo/Android-AlertView
    // 所需
    @Override
    public void onItemClick(Object o, int position) {
        //调用父类的方法给出提示
//        super.onItemClick(o,position);


        int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
        switch (position) {
            case 0: // 支付宝支付
                mChannel = Constants.CHANNEL_ALIPAY;
                toPay();
                break;
            case 1: // 余额支付
                ToastUtils.showShort("正在开通中...");
                break;

            case 2: // 环迅支付
                ToastUtils.showShort("正在开通中...");
                break;
            default:
                return;
        }

    }

    private void toPay() {
        new com.bjaiyouyou.thismall.task.PaymentTask(getApplicationContext(), this, mOrderNumber, mChannel, mBtOtherPay2, OrderPayFailActivity.TAG)
                .execute(new com.bjaiyouyou.thismall.task.PaymentTask.PaymentRequest(mChannel, 1));
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        mTvPayNow.setOnClickListener(OrderDetailActivity.this);
        mBtOtherPay1.setOnClickListener(this);
        mBtOtherPay2.setOnClickListener(this);

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

                if ("success".equals(result)) {
                    ToastUtils.showShort("支付成功");
                } else if ("fail".equals(result)) {
                    ToastUtils.showShort("支付失败");
                } else if ("cancel".equals(result)) {
                    ToastUtils.showShort("用户取消");
                } else if ("invalid".equals(result)) {
                    ToastUtils.showShort("失效");
                }
            }
        }
    }
}
