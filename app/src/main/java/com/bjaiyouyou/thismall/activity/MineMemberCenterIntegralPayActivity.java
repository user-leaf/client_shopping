package com.bjaiyouyou.thismall.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.callback.PingppPayResult;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.PayOrderNum;
import com.bjaiyouyou.thismall.task.PaymentTask;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 积分充值页面
 *
 * @author QuXinhang
 *         Creare 2016/8/13 15:31
 */
public class MineMemberCenterIntegralPayActivity extends BaseActivity implements View.OnClickListener, TagFlowLayout.OnTagClickListener, OnItemClickListener {

    public static final String TAG = MineMemberCenterIntegralPayActivity.class.getSimpleName();
    //列表数据
    private List<String> mIntegrals;
    //列表对应积分
    private List<Integer> mIntegralNums;
    //列表对应积分
    private List<Integer> mMoneys;
    //剩余积分网络加载获得
    private long mHavenIntegral;
    //本次充值获得积分
    private long mGetIntegral;
    //本次充值钱数
    private int mPayMoney;
    //选择角标
    private int mChooseIndex;
    //兑换计算边界值
    private int mBoundMoney = 50;

    private IUUTitleBar mTitleBar;
    //剩余积分控件
    private TextView mTvHavenIntegral;
    //列表
    private TagFlowLayout mTFL;
    //充值按钮
    private Button mBtnPay;
    //金额输入框
    private EditText mEtMoney;
    //列表适配器
    private TagAdapter mTagAdapter;
    //输入钱数位数限制
    private int mMoneyLimit = 9;
    private RelativeLayout mRlEtPayMoney;
    //没登录布局
    private RelativeLayout mLLNotLogin;
    //登录布局
    private ScrollView mSlLogin;
    //获取是否登录
    private boolean isLogin = false;
    private Intent mIntent;
    private TextView mTvGotoLogin;
    private TextView mTVNotLoginTitle;
    //支付订单号
    private String mOrder_number;
    //支付方式
    private String mChannel;

    //人民币充值上线
    private int mLimitMonyes;
    //积分详情入口
    private TextView mTvIntegralDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_member_center_integral_pay);
        mIntent = getIntent();
        mHavenIntegral = mIntent.getLongExtra("mIntegral", 0);
        isLogin = mIntent.getBooleanExtra("isLogin", false);

        loadData();
        initView();
        setUpView();
        initControl();
    }

    private void loadData() {
        mIntegrals = new ArrayList<>();
        mIntegrals.add("10积分\n=10元");
        mIntegrals.add("20积分\n=20元");
        mIntegrals.add("30积分\n=30元");
        mIntegrals.add("51积分\n=50元");
        mIntegrals.add("103积分\n=100元");
        mIntegrals.add("207积分\n=200元");
        mIntegrals.add("311积分\n=300元");
        mIntegrals.add("519积分\n=500元");
        mIntegrals.add("1039积分\n=1000元");
        mIntegrals.add("2079积分\n=2000元");
        mIntegrals.add("其他金额");

        mIntegralNums = new ArrayList<>();
        mIntegralNums.add(10);
        mIntegralNums.add(20);
        mIntegralNums.add(30);
        mIntegralNums.add(51);
        mIntegralNums.add(103);
        mIntegralNums.add(207);
        mIntegralNums.add(311);
        mIntegralNums.add(519);
        mIntegralNums.add(1039);
        mIntegralNums.add(2079);

        mMoneys = new ArrayList<>();
        mMoneys.add(10);
        mMoneys.add(20);
        mMoneys.add(30);
        mMoneys.add(50);
        mMoneys.add(100);
        mMoneys.add(200);
        mMoneys.add(300);
        mMoneys.add(500);
        mMoneys.add(1000);
        mMoneys.add(2000);

    }

    private void initView() {
        mTvIntegralDetail = ((TextView) findViewById(R.id.tv_integral_detail));

        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_member_center_integral));
        mTvHavenIntegral = ((TextView) findViewById(R.id.tv_member_center_integral_haven));
        mTFL = ((TagFlowLayout) findViewById(R.id.tfl_member_center_integral));
        mBtnPay = ((Button) findViewById(R.id.btn_pay_member_center_integral));
        mEtMoney = ((EditText) findViewById(R.id.et_money_member_center_integral));

        mRlEtPayMoney = ((RelativeLayout) findViewById(R.id.rl_et_money_member_center_integral));
        mSlLogin = ((ScrollView) findViewById(R.id.sl_integral_pay_log));
        mTvGotoLogin = ((TextView) findViewById(R.id.tv_goto_login));
        mLLNotLogin = ((RelativeLayout) findViewById(R.id.ll_not_login));
        mTVNotLoginTitle = ((TextView) findViewById(R.id.tv_not_login_title));
        mTVNotLoginTitle.setText("无法获得支付信息");
        if (isLogin) {
            mSlLogin.setVisibility(View.VISIBLE);
            mLLNotLogin.setVisibility(View.GONE);
        } else {
            mSlLogin.setVisibility(View.GONE);
            mLLNotLogin.setVisibility(View.VISIBLE);
        }
    }

    private void setUpView() {
        mTvIntegralDetail.setOnClickListener(this);
        mTitleBar.setLeftLayoutClickListener(this);
        mBtnPay.setOnClickListener(this);
        mTFL.setOnTagClickListener(this);
        mTvGotoLogin.setOnClickListener(this);
        mEtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String money = s.toString().trim();
                if (money.length() > mMoneyLimit) {
                    money = money.substring(0, mMoneyLimit);
                    mEtMoney.setText(money);
                    mEtMoney.setSelection(mMoneyLimit);
                }
                if (!TextUtils.isEmpty(money)) {
                    mPayMoney = Integer.valueOf(money);
                    getIntegral();
                } else {
                    mPayMoney = 0;
                    mBtnPay.setText("充值");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTFL.setOnTagClickListener(this);
    }


    private void initControl() {
        mTvHavenIntegral.setText(mHavenIntegral + "");

        final int width = ScreenUtils.getScreenWidth(getApplicationContext());
        final int textWidth = width / 4;
        final int textSpace = textWidth / 10;
        int mTFLSpace=textWidth*3+textSpace*6;
        final RelativeLayout.LayoutParams  paramsTFL=new RelativeLayout.LayoutParams(mTFLSpace, ViewGroup.LayoutParams.WRAP_CONTENT);

//        mTFL.setLayoutParams(paramsTFL);

        mTagAdapter = new TagAdapter<String>(mIntegrals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.member_center_tv, mTFL, false);



                textView.setText(s);

                LogUtils.e("width", "" + width);
                LogUtils.e("textHeight", textView.getHeight() + "");

//                parent.setLayoutParams(paramsTFL);


                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(textWidth, textWidth / 2);
                textView.setPadding(0, 0, 0, 0);

//                params.setMargins(5, 0, 5, 5);
                params.setMargins(textSpace, 0, textSpace, textSpace);
//                params.setMargins(0, 0, 0, 0);
                textView.setLayoutParams(params);

                return textView;
            }
        };
        mTFL.setAdapter(mTagAdapter);
        //初始化页面状态
        mTagAdapter.setSelectedList(0);
        mTFL.getChildAt(0).setClickable(true);
        mPayMoney = mMoneys.get(0);
        mGetIntegral = mIntegralNums.get(0);
        setButtonIntegral();
    }

    /**
     * 实时计算获得积分
     */
    private void getIntegral() {
        if (mPayMoney < mBoundMoney) {
            mGetIntegral = mPayMoney;
        } else {
            int bs = mPayMoney / mBoundMoney;
            mGetIntegral = 51 * bs + bs - 1 + mPayMoney % mBoundMoney;
        }
        setButtonIntegral();
    }

    /**
     * 设置Button
     */
    private void setButtonIntegral() {
        LogUtils.e("mPayMoney", "" + mPayMoney);
        if (mPayMoney == 0) {
            mBtnPay.setText("充值");
        }
        mBtnPay.setText("充值（" + mGetIntegral + "积分)");
    }


    /**
     * 点击其他区域收起手机输入软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.left_layout://返回
                finish();
                break;
            case R.id.btn_pay_member_center_integral://充值支付
                LogUtils.e("mPayMoney", "" + mPayMoney);
                pay();
                break;
            case R.id.tv_goto_login: // 登录入口
                jump(new Intent(getApplicationContext(), LoginActivity.class), false);
                finish();
                break;
            case R.id.tv_integral_detail: // 积分详情入口
                jump(IntegralDetailActivity.class, false);
                break;

        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.left_layout://返回
//                finish();
//                break;
//            case R.id.btn_pay_member_center_integral://充值支付
//                LogUtils.e("mPayMoney", "" + mPayMoney);
//                pay();
//                break;
//            case R.id.tv_goto_login: // 登录入口
//                jump(new Intent(getApplicationContext(), LoginActivity.class), false);
//                finish();
//                break;
//            case R.id.tv_integral_detail: // 积分详情入口
//                jump(IntegralDetailActivity.class, false);
//                break;
//
//        }
//    }

    /**
     * 积分充值支付
     */
    private void pay() {
        String token = CurrentUserManager.getUserToken();
        if (token != null) {
            ClientAPI.postIntegralPay(token, mPayMoney, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    if (mPayMoney > mLimitMonyes) {
                        Toast.makeText(getApplicationContext(), "充值失败；支付人民币不能超过2000元", Toast.LENGTH_SHORT).show();
                    } else {
                        UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
                    }
                }

                @Override
                public void onResponse(String response, int id) {
                    //拿到订单调用支付
                    LogUtils.e("response:", response);
                    mOrder_number = new Gson().fromJson(response, PayOrderNum.class).getOrder_number();
                    //吊起支付
                    if (!TextUtils.isEmpty(mOrder_number)) {
                        doPayByPingpp();
                    }
                }
            });
        }

    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (i != position) {
//                        parent.getChildAt(i).setEnabled(true);
                parent.getChildAt(i).setClickable(false);
            } else {
                parent.getChildAt(i).setClickable(true);
            }
        }
        if (position == mIntegralNums.size()) {
            mBtnPay.setText("充值");
            mRlEtPayMoney.setVisibility(View.VISIBLE);
            //刚刚切换过来初始化
            mPayMoney = 0;
            mGetIntegral = 0;
        } else {
            mEtMoney.setText("");
            mRlEtPayMoney.setVisibility(View.GONE);
            mChooseIndex = position;
            mPayMoney = mMoneys.get(mChooseIndex);
            mGetIntegral = mIntegralNums.get(mChooseIndex);
            setButtonIntegral();
        }
        return true;
    }

    //////////////////////////////////支付
    // 去付款2之调用ping++去付款
    private void doPayByPingpp() {

        // https://github.com/saiwu-bigkoo/Android-AlertView
        new AlertView("选择支付方式", null, "取消", null, new String[]{getString(R.string.pay_wx), getString(R.string.pay_alipay)}, this, AlertView.Style.ActionSheet, this).show();

    }

    //  https://github.com/saiwu-bigkoo/Android-AlertView
    // 所需
    @Override
    public void onItemClick(Object o, int position) {
        int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
        switch (position) {
            case 0: // 微信支付
                mChannel=Constants.CHANNEL_WECHAT;
                break;
            case 1: // 支付宝支付
                mChannel=Constants.CHANNEL_ALIPAY;
                break;
        }
        new PaymentTask(MineMemberCenterIntegralPayActivity.this, MineMemberCenterIntegralPayActivity.this, mOrder_number, mChannel, mBtnPay, TAG)
                .execute(new PaymentTask.PaymentRequest(mChannel, 1));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mBtnPay.setOnClickListener(MineMemberCenterIntegralPayActivity.this);

        //支付页面返回处理

        PingppPayResult.setOnPayResultCallback(requestCode, resultCode, data, new PingppPayResult.OnPayResultCallback() {
            @Override
            public void onPaySuccess() {
                //支付成功后跳转页
//                    jump(MineRechargeSuccessActivity.class,false);
                MineRechargeSuccessActivity.actionStart(MineMemberCenterIntegralPayActivity.this, 1);
                finish();
            }

            @Override
            public void onPayFail() {

            }
        });
    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MineMemberCenterIntegralPayActivity.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    private static String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(url).post(body).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    class PaymentRequest {
        String channel;
        int amount;

        public PaymentRequest(String channel, int amount) {
            this.channel = channel;
            this.amount = amount;
        }
    }
}
