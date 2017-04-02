package com.bjaiyouyou.thismall.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.Withdraw;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 提现页面
 *
 * @author QuXinhang
 *         Creare 2016/8/8 17:27
 */
public class WithdrawActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    //本周全部限额
    private TextView mTvWeekMax;
    //全部消费
    private TextView mTvAllPay;
    //全部UU
    private TextView mTvAllCoin;
    //提取金额
    private EditText mEtWithDraw;
    //提取按钮
    private Button mBtnWithDraw;
    //没超过限额布局
    private LinearLayout mLLNotOver;
    //超过限额布局
    private TextView mTvOver;
    private IUUTitleBar mTitleBar;

    //本周限额
    private double mLimitBalance = 0;
    //UU余额
    private long mCoinBalance = 0;
    //过周消费累计
    private double mPayBalance = 0;
    //本周可提现金额 min(有效的消费，本周限额，账户UU余额)
    private double mCanWithDrawBalance = 0;
    //输入金额
    private long mInPutBalance;
    //是否可提取
    private boolean isCanWithDraw = false;
    //提现是否为输入值
    private boolean isEtChange = false;
    //可输入长度
    private int mEtLength = 15;
    //微信变量初始化
    private IWXAPI api;

    private Intent mIntent;
    //获取微信的OpenId
    private String mOpenID;
    //获得提现信息
    private Withdraw mGetWithdraw;
    //安全码输入框
    private EditText mEtSafeCodeInput;
    //微信钱包实名认证
    private EditText mEtBeneficiary;
    //今日可提现次数
    private TextView mTvWithDrawTimes;
    private String mSafeCode;
    //未登录页面
    private RelativeLayout mLLNotLogin;
    //已经登录页面
    private ScrollView mSlLogin;
    //是否登录
    private boolean isLogin;
    //未登录情况的去登录按钮
    private TextView mTvGotoLogin;
    //我的收益布局
    private LinearLayout mLLIsFive;
    //用户种类标识,5为第五季会员 其它不是第五级会员
    private int mMember_type;
    //测试用户标识
    private int isInTestUser;
    //提现记录页面
    private TextView mTvWithdrawDetail;
    private TextView mTvWeekMaxText;
    //为已升级为vip
    private int isVip;
    private TextView mTvChangeRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        mIntent = getIntent();
        isLogin=mIntent.getBooleanExtra("isLogin",false);
        mCoinBalance = mIntent.getLongExtra("coin", -1);
        mSafeCode = mIntent.getStringExtra("safeCode");
        mMember_type=mIntent.getIntExtra("member_type",0);
        isVip=mIntent.getIntExtra("isVip",0);
        isInTestUser=mIntent.getIntExtra("isInTestUser",0);
        initView();
        setUpView();
        initData();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        mIntent = getIntent();
        mOpenID = mIntent.getStringExtra("openid");
        LogUtils.e("获得的mOpenID:", "" + mOpenID);
        if (!TextUtils.isEmpty(mOpenID)) {
            if (mCanWithDrawBalance != 0) {
                getWithdraw();
            } else {
                Toast.makeText(getApplicationContext(), "0元不可换取哦", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * 获得提现信息
     */
    private void initData() {
        ClientAPI.getWithdraw( new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //错误提示
//                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response)) {
                    mGetWithdraw = new Gson().fromJson(response.trim(), Withdraw.class);
//                    if (!TextUtils.isEmpty(mEtWithDraw.getText())&&mGetWithdraw!=null){
                    if (mGetWithdraw != null) {
                        //本周限额
                        mLimitBalance = mGetWithdraw.getWeek_can_drawings_amount();
                        //UU余额
//                         mCoinBalance=mGetWithdraw.get;
                        //过周消费累计
                        mPayBalance = mGetWithdraw.getAll_can_drawings_amount();
                        //今日可提取次数
                        mTvWithDrawTimes.setText((int)(mGetWithdraw.getToday_can_drawings_number()) + "");
                        //填充数据
                        initControl();
                    }
                }
            }
        });
    }

    /**
     *
     */
    private void initView() {

        mTvChangeRule = ((TextView) findViewById(R.id.tv_withdraw_change_rules));
        //根据是否是第五季会员控制显示
        mLLIsFive = ((LinearLayout) findViewById(R.id.ll_withdraw_income));
        if (mMember_type==5||isInTestUser==1||isVip==2){
            mLLIsFive.setVisibility(View.VISIBLE);
        }
        mTvWithdrawDetail = ((TextView) findViewById(R.id.tv_withdraw_detail));
        mTvWithdrawDetail.setFocusable(true);
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_withdraw));
        mEtWithDraw = ((EditText) findViewById(R.id.et_withdraw));
        mBtnWithDraw = ((Button) findViewById(R.id.btn_withdraw));
        mLLNotOver = ((LinearLayout) findViewById(R.id.ll_withdraw_not_over));
        mTvOver = ((TextView) findViewById(R.id.tv_withdraw_over_notification));
        mTvWeekMax = ((TextView) findViewById(R.id.tv_withdraw_week_max));
        mTvWeekMaxText = ((TextView) findViewById(R.id.tv_withdraw_week_max_text));
        mTvWeekMaxText.setFocusable(true);
        mTvWeekMaxText.setFocusableInTouchMode(true);
        mTvAllPay = ((TextView) findViewById(R.id.tv_withdraw_pay_all));
        mTvAllCoin = ((TextView) findViewById(R.id.tv_withdraw_coin_all));
        mEtSafeCodeInput = ((EditText) findViewById(R.id.et_withdraw_safe_code));
        mEtBeneficiary = ((EditText) findViewById(R.id.et_withdraw_beneficiary_name));
        mTvWithDrawTimes = ((TextView) findViewById(R.id.tv_withdraw_time));
        mLLNotLogin = ((RelativeLayout) findViewById(R.id.ll_not_login));
        mTvGotoLogin = ((TextView) findViewById(R.id.tv_goto_login));
        mSlLogin = ((ScrollView) findViewById(R.id.sl_withdraw_login));
        mLLIsFive.setFocusable(true);

        if (isLogin){
            mSlLogin.setVisibility(View.VISIBLE);
            mLLNotLogin.setVisibility(View.GONE);
        }else {
            mSlLogin.setVisibility(View.GONE);
            mLLNotLogin.setVisibility(View.VISIBLE);
        }

    }

    private void setUpView() {
        mTvChangeRule.setOnClickListener(this);
        mLLIsFive.setOnClickListener(this);
        mTvWithdrawDetail.setOnClickListener(this);
//        mTitleBar.setRightLayoutClickListener(this);
        mTitleBar.setLeftLayoutClickListener(this);
        mBtnWithDraw.setOnClickListener(this);
        mTvGotoLogin.setOnClickListener(this);
        mEtWithDraw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //获得输入金额
                String string = s.toString().trim();

                //限制开头输入不能是“0”
                while (string.startsWith("0")) {
                    string = string.substring(1, string.length());
                    mEtWithDraw.setText(string);
                }
                //控制光标的位置
                mEtWithDraw.setSelection(string.length());

                if (string.length() > mEtLength) {
                    //不可编辑当长度超长的时候不能删除
//                    mEtWithDraw.setEnabled(false);
//                    mEtWithDraw.setFocusable(false);
                    //超长不显示，还可编辑
                    string = string.substring(0, mEtLength);
                    mEtWithDraw.setSelection(string.length());
                    mEtWithDraw.setText(string);
                }
                if (!TextUtils.isEmpty(string)) {
                    mInPutBalance = Long.valueOf(string);
                    if (mInPutBalance!=0L){
                        //根据输入判断是否可提现
                        etChange(mInPutBalance);
                    }else {
                        isEtChange=false;
                }
                } else {
                    //去掉所有的超限提示背景
                    mTvWeekMax.setEnabled(true);
                    mTvAllCoin.setEnabled(true);
                    mTvAllPay.setEnabled(true);
                    mTvOver.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 数据填充
     */

    private void initControl() {
        //填充基本的数据
        mTvWeekMax.setText(String.valueOf(mLimitBalance));
        mTvAllPay.setText(mPayBalance + "");
        mTvAllCoin.setText(+mCoinBalance + "UU");
//        mCanWithDrawBalance=(mLimitBalance<=mPayBalance?mLimitBalance:mPayBalance)<=mCoinBalance?
//                (mLimitBalance<=mPayBalance?mLimitBalance:mPayBalance):mCoinBalance;
        mCanWithDrawBalance = Math.min(Math.min(mLimitBalance, mPayBalance), mCoinBalance);
        String withDrawBalance = mCanWithDrawBalance + "";
        mEtWithDraw.setHint(withDrawBalance);
        mEtSafeCodeInput.setHint("" + mSafeCode + "******");
    }

    /**
     * 进行判断
     *
     * @param inPutBalance
     */

    private void etChange(long inPutBalance) {
        //提现额是输入值
        isEtChange = true;
        //判断
        isCanWithDraw = ((inPutBalance <= mLimitBalance) && (inPutBalance <= mCoinBalance) && (inPutBalance <= mPayBalance))&&inPutBalance!=0L ? true : false;
        //判断处理
        if (!isCanWithDraw) {
            //将最小限额设置为背景可见
            if (mCanWithDrawBalance == mCoinBalance) {
                LogUtils.e("mCanWithDrawBalance", "" + mCanWithDrawBalance);
                mTvWeekMax.setEnabled(true);
                mTvAllCoin.setEnabled(false);
                mTvAllPay.setEnabled(true);
            }
            if (mCanWithDrawBalance == mPayBalance) {
                LogUtils.e("mCanWithDrawBalance", "" + mCanWithDrawBalance);
                mTvWeekMax.setEnabled(true);
                mTvAllCoin.setEnabled(true);
                mTvAllPay.setEnabled(false);
            }
            if (mCanWithDrawBalance == mLimitBalance) {
                LogUtils.e("mCanWithDrawBalance", "" + mCanWithDrawBalance);
                mTvWeekMax.setEnabled(false);
                mTvAllCoin.setEnabled(true);
                mTvAllPay.setEnabled(true);
            }
            //提示框显示
            mTvOver.setVisibility(View.VISIBLE);
        } else {
            mTvOver.setVisibility(View.GONE);
            mTvWeekMax.setEnabled(true);
            mTvAllCoin.setEnabled(true);
            mTvAllPay.setEnabled(true);
        }

    }


    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.right_layout:
                //跳转到webView查看提现规则
//                Toast.makeText(this,"提现疑问解答",Toast.LENGTH_SHORT).show();
                Intent intentWebView = new Intent(this, WebShowActivity.class);
                intentWebView.putExtra("urlpath", ClientAPI.WITHDRAW_RULE);
                jump(intentWebView, false);
                break;
            case R.id.btn_withdraw://提现
                //test
                // jump(WithDrawSucceedActivity.class,false);

                if (!isEtChange){
                    Toast.makeText(this, "请输入换取数额", Toast.LENGTH_SHORT).show();
                }else {
                    if (!isCanWithDraw) {
                        Toast.makeText(this, "输入数额不符合条件，请确认后重新输入", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        getWithdraw();
                    }
                }

//                getPermission();

                break;

            case R.id.tv_goto_login: //登录入口
                jump(new Intent(getApplicationContext(),LoginActivity.class),false);
                finish();
                break;
            case R.id.ll_withdraw_income: // 我的收益
                jump(UserIncomeActivity.class,false);
//                finish();
                break;
            case R.id.tv_withdraw_detail: // 提现记录
                jump(WithdrawRecordActivity.class,false);
//                finish();
                break;
            case R.id.tv_withdraw_change_rules: // 兑换规则,下划线

                jump(WithdrawRecordActivity.class,false);
//                finish();
                break;
        }
    }

    /**
     * 获得权限
     */

    private static final int RC_READ_PHONE_STATE = 123;

    private void getPermission() {
        /**
         * 新权限判断 9.16 by kanbin
         * 原先的权限判断还存在问题：
         *  原因1是onCreate() onResume()都有initData()；
         *  现在进度条一直不消失，先不调试了。
         */
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
            getOpenId();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要开启一些权限",
                    RC_READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE);
        }
    }

    /**
     * 获得微信的OpenId
     */
    private void getOpenId() {
        /**
         * 微信登录获取，存在问题，授权页面一直存在
         */
//        Intent intent=new Intent(this, WXEntryActivity.class);
//        startActivity(intent);
//        finish();
        /**
         * 第三方登录的方式获取
         */
//        authorize(new Wechat(this));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("", "onPermissionsGranted:" + requestCode + ":" + perms.size());
        getOpenId();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        Toast.makeText(getApplicationContext(), "未授权，请到设置中为应用进行授权", Toast.LENGTH_SHORT).show();
        return;
    }

///////////////////////////////////////////////向后台提现///////////////////////////////

    /**
     * 提现
     */
    public void getWithdraw() {
        mCanWithDrawBalance = mInPutBalance;
        String withdrawNum=mEtWithDraw.getText().toString().trim();
        if (TextUtils.isEmpty(withdrawNum)){
            ToastUtils.showShort("请输入换取数额");
            return;
        }
        String safeCode = mEtSafeCodeInput.getText().toString().trim();
        if (TextUtils.isEmpty(safeCode)) {
            ToastUtils.showShort("安全码不可为空");
            return;
        }
        String userName = mEtBeneficiary.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.showShort("实名认证不可为空");
            return;
        }

        String token = CurrentUserManager.getUserToken();
        if (!TextUtils.isEmpty(token)) {
            ClientAPI.withdraw(token, mOpenID, (int) mCanWithDrawBalance, userName, safeCode, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
//                    UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
                }

                @Override
                public void onResponse(String response, int id) {
                    if (!TextUtils.isEmpty(response.trim())) {
                        //跳转到提现成功页面
//                        Toast.makeText(getApplicationContext(),"提现成功，请耐心等待",Toast.LENGTH_SHORT).show();
                        jump(WithDrawSucceedActivity.class, false);
                        //提现成功关闭页面
                        finish();
                    }
                }
            });
        }
    }
    //////////////////////////////////////////shareSDk第三方登录获得openID/////////////////////////////


}
