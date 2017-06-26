package shop.imake.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import shop.imake.Constants;
import shop.imake.R;
import shop.imake.adapter.MyOrderRecycleViewAdapter;
import shop.imake.callback.PingppPayResult;
import shop.imake.fragment.MyOrderFinishFragment;
import shop.imake.fragment.MyOrderNotFinishFragment;
import shop.imake.fragment.MyOrderPaymentFragment;
import shop.imake.model.PayResultEvent;
import shop.imake.model.PayResultMyOrderRefreshEvent;
import shop.imake.utils.ACache;
import shop.imake.utils.DialUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.ScreenUtils;
import shop.imake.widget.IUUTitleBar;


/**
 * @author Alice
 *         Creare 2016/6/8 15:35
 *         我的订单页面
 */
public class MyOrderActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private RadioGroup rg;
    // 全部
    private RadioButton rbAll;
    // 待付款
    private RadioButton rbPayment;
    // 待发货
    private RadioButton rbSend;
    // 待收货
    private RadioButton rbReceive;
    private List<Fragment> fragments;
    private List<RadioButton> radios;
    // 全部
    // 待付款
    private MyOrderPaymentFragment fPayment;
    // 待发货
    // 待收货
    // 记录当前是那个Fragment
    private int current;
    private View mback;
    //未完成
    private MyOrderNotFinishFragment frNotFinish;
    //已经完成
    private MyOrderFinishFragment frFinish;
    //获取当前Token
    private String mToken;
    //已经占用高度
    private int tackUpHeight;
    private IUUTitleBar mTitleBar;
    //屏幕高度
    private int mHeight;

    private Bundle mBundle;
    //确认收货提醒
    private TextView mTVRemainder;
    //控制确认收货提醒消失
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 0) {
                mTVRemainder.setVisibility(View.GONE);
            }
        }
    };

    //用于Adapter里面吊起支付
    public static FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        mFragmentManager = getSupportFragmentManager();
        //延时10秒发送指令，隐藏提醒
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 10000);

        //注册evenbus处理余额支付解决处理
        EventBus.getDefault().register(this);
    }

    private void initView() {
        mTitleBar = ((IUUTitleBar) findViewById(R.id.my_order_title_bar));
        mback = ((View) findViewById(R.id.left_layout));
        mback.setOnClickListener(this);
        //确认收货提醒
        mTVRemainder = ((TextView) findViewById(R.id.tv_confirm_receiving_reminder));

        rg = ((RadioGroup) findViewById(R.id.rg_MyOrder));
        rg.setOnCheckedChangeListener(this);

        //获取已占用高度
        tackUpHeight = (int) (getResources().getDimension(R.dimen.height_top_bar)) * 2;
        //屏幕高度
        mHeight = ScreenUtils.getScreenHeight(getApplicationContext());
        mHeight = mHeight - tackUpHeight;

        mBundle = new Bundle();
        mBundle.putInt("mHeight", mHeight);
        Log.e("ORDER_HEIGHT", "rg_height：" + rg.getHeight() + "---height_top_bar:" + (int) (getResources().getDimension(R.dimen.height_top_bar)) + "---mHeight:" + ScreenUtils.getScreenHeight(getApplicationContext()));


        radios = new ArrayList<>();
        rbPayment = ((RadioButton) findViewById(R.id.rb_MyOrder_Payment));
        radios.add(rbPayment);
        rbSend = ((RadioButton) findViewById(R.id.rb_MyOrder_Send));
        radios.add(rbSend);
        rbReceive = ((RadioButton) findViewById(R.id.rb_MyOrder_Receiving));
        radios.add(rbReceive);

        fragments = new ArrayList<>();

        fPayment = new MyOrderPaymentFragment();
//        fPayment.setArguments(mBundle);
        fragments.add(fPayment);

        frNotFinish = new MyOrderNotFinishFragment();
//        frNotFinish.setArguments(mBundle);
        fragments.add(frNotFinish);

        frFinish = new MyOrderFinishFragment();
//        frFinish.setArguments(mBundle);
        fragments.add(frFinish);


        getSupportFragmentManager().beginTransaction().add(R.id.ll_myOrser_Fragment, fragments.get(0)).commit();


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount() - 1; i++) {
            if (radios.get(i).getId() == checkedId) {
                radios.get(i).setChecked(true);
                radios.get(i).setTextColor(Color.RED);
                changeFragment(i);
            } else {
                radios.get(i).setChecked(false);
                radios.get(i).setTextColor(Color.GRAY);

            }
        }
    }


    // 执行替换Fragment的操作
    private void changeFragment(int i) {
        // 拿到事物管理器
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // 判断当前Fragment 是否被添加过
        if (!fragments.get(i).isAdded()) {
            // 没有被添加过 添加 之后将当前的Fragment隐藏
            ft.add(R.id.ll_myOrser_Fragment, fragments.get(i)).hide(fragments.get(current)).commit();
        } else {
            // 添加过 将目标Fragment 显示 并将当前的Fragment 隐藏
            ft.show(fragments.get(i)).hide(fragments.get(current)).commit();
        }
        // 替换后目标id为 当前的current
        current = i;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
        }
    }

    //处理Adapter的 onActivityResult（）事件
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //电话授权检测
        callPermissionsResult(requestCode, resultCode);

        PingppPayResult.setOnPayResultCallback(requestCode, resultCode, data, new PingppPayResult.OnPayResultCallback() {
            @Override
            public void onPaySuccess() {
//                fPayment.refreshData();
//                fPayment.mRefreshHandler.sendEmptyMessage(0);
                EventBus.getDefault().post(new PayResultMyOrderRefreshEvent(true));
                LogUtils.e("立即支付", "Activity支付成功onActivityResult");
            }

            @Override
            public void onPayFail() {
                //跳转到支付失败页面,传递订单号
//                orderPayFail();
            }
        });
    }

    /**
     * 在OnActivityResult()方法中调用，检查MyOrderAdapter中授权结果
     *
     * @param requestCode
     * @param resultCode
     */
    public void callPermissionsResult(int requestCode, int resultCode) {
        LogUtils.e("order", "授权结果");
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == Constants.CALL_PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            Toast.makeText(this, "未授权，不能拨打电话", Toast.LENGTH_SHORT).show();
//            finish();
        } else if (requestCode == Constants.CALL_PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
            DialUtils.callCentre(this, ACache.get(getApplicationContext()).getAsString(DialUtils.PHONE_GET_SAFE_CODE_KEY));
        }
    }

    /**
     * 支付失败调用的跳转方法
     */
    private void orderPayFail() {
        Intent intentPayFail = new Intent(this, OrderPayFailActivity.class);
        intentPayFail.putExtra("mOrderNumber", MyOrderRecycleViewAdapter.getOrderNum());
        startActivity(intentPayFail);
//        activity.finish();
    }

    ////////////////////////注册EvenBus，实现订单支付/////////////////////////
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消余额支付回调处理的EvenBus
        EventBus.getDefault().unregister(this);
    }

    /**
     * 余额支付回调
     *
     * @param event
     */
    @Subscribe
    public void onBalancePayEvent(PayResultEvent event) {
        LogUtils.e("立即支付", "Activity支付成功onBalancePayEvent_out");
        if (event.isPaySuccess()) {
            //刷新数据
//            fPayment.refreshData();
//            fPayment.mRefreshHandler.sendEmptyMessage(0);
            EventBus.getDefault().post(new PayResultMyOrderRefreshEvent(true));

            LogUtils.e("立即支付", "Activity支付成功onBalancePayEvent");

        } else {
            //跳转到支付失败页面,传递订单号

        }
    }

}