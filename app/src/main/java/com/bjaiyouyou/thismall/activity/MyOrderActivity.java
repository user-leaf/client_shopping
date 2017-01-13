package com.bjaiyouyou.thismall.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.MyOrderRecycleViewAdapter;
import com.bjaiyouyou.thismall.fragment.MyOrderFinishFragment;
import com.bjaiyouyou.thismall.fragment.MyOrderNotFinishFragment;
import com.bjaiyouyou.thismall.fragment.MyOrderPaymentFragment;
import com.bjaiyouyou.thismall.utils.DialUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.pingplusplus.android.Pingpp;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author QuXinhang
 *Creare 2016/6/8 15:35
 * 我的订单页面
 *
 */
public class MyOrderActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{

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
    private  int current;
    private View mback;
    //未完成
    private  MyOrderNotFinishFragment frNotFinish;
    //已经完成
    private  MyOrderFinishFragment frFinish;
    //获取当前Token
    private String mToken;
    //已经占用高度
    private  int tackUpHeight;
    private IUUTitleBar mTitleBar;
    //屏幕高度
    private int mHeight;

    private Bundle mBundle;
    //确认收货提醒
    private TextView mTVRemainder;
    //控制确认收货提醒消失
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1==0){
                mTVRemainder.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        //延时10秒发送指令，隐藏提醒
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        },10000);
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
        tackUpHeight= (int) (getResources().getDimension(R.dimen.height_top_bar))*2;
        //屏幕高度
        mHeight= ScreenUtils.getScreenHeight(getApplicationContext());
        mHeight=mHeight-tackUpHeight;

        mBundle=new Bundle();
        mBundle.putInt("mHeight",mHeight);
        Log.e("ORDER_HEIGHT", "rg_height："+rg.getHeight()+"---height_top_bar:"+(int)(getResources().getDimension(R.dimen.height_top_bar))+"---mHeight:"+ScreenUtils.getScreenHeight(getApplicationContext()));


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
        for (int i=0;i<group.getChildCount()-1;i++) {
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
        switch (v.getId()){
            case  R.id.left_layout:
                finish();
                break;
        }
    }

    //处理Adapter的 onActivityResult（）事件
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //电话授权检测
        callPermissionsResult(requestCode,resultCode);

        //拨打电话
        orderPayResult(requestCode,resultCode,data);
    }

    /**
     * 在OnActivityResult()方法中调用，检查MyOrderAdapter中授权结果
     * @param requestCode
     * @param resultCode
     */
    public void callPermissionsResult(int requestCode,int resultCode) {
        LogUtils.e("order","授权结果");
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == Constants.CALL_PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            Toast.makeText(this,"未授权，不能拨打电话",Toast.LENGTH_SHORT).show();
//            finish();
        }else if(requestCode == Constants.CALL_PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED){
            DialUtils.callCentre(this,DialUtils.CENTER_NUM);
        }
    }
    /**
     * 在OnActivityResult()方法中调用，检查MyOrderAdapter中授权结果
     * @param requestCode
     * @param resultCode
     */
    public void orderPayResult(int requestCode,int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                LogUtils.e("order：","支付结果");
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//
                LogUtils.e("errorMsg:",errorMsg);
                if ("success".equals(result)) {
                    ToastUtils.showShort("支付成功");
                } else if ("fail".equals(result)) {
                    ToastUtils.showShort("支付失败");
                    //跳转到支付失败页面,传递订单号
                    orderPayFail();

                } else if ("cancel".equals(result)) {
                    ToastUtils.showShort("用户取消");
                } else if ("invalid".equals(result)) {
                    ToastUtils.showShort("失效");
                }
            }
        }

        //Test
//        orderPayFail();
    }
    /**
     * 支付失败
     */
    private void orderPayFail() {
        Intent intentPayFail = new Intent(this, OrderPayFailActivity.class);
        intentPayFail.putExtra("mOrderNumber", MyOrderRecycleViewAdapter.getOrderNum());
        startActivity(intentPayFail);
//        activity.finish();
    }

}