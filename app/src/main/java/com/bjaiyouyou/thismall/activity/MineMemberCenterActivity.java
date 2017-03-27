package com.bjaiyouyou.thismall.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.kyleduo.switchbutton.SwitchButton;
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
import java.util.Set;

import okhttp3.Call;

/**
 * UU充值页
 * 原会员中心页
 *
 * @author kanbin
 * @date 2016/6/20
 */

/**
 * 添加提现跳转
 *
 * @author QuXinhang
 *         Creare 2016/8/8 18:40
 *         <p/>
 *         <p/>
 *         布局添加
 *         对充值功能的实现
 * @author QuXinhang
 *         Creare 2016/8/12 9:35
 */
public class MineMemberCenterActivity extends BaseActivity implements TagFlowLayout.OnTagClickListener,
        View.OnClickListener, TagFlowLayout.OnSelectListener, OnItemClickListener {

    public static final String TAG = MineMemberCenterActivity.class.getSimpleName();

    //充值选项列表
    private TagFlowLayout mTagFlowLayout;
    //列表内容
    private String[] mVals;
    //列表对应的充值费用
    private Long[] mRMBs;
    //列表适配器
    private TagAdapter<String> mTagAdapter;
    private IUUTitleBar mTitleBar;
    //其他金额输入框
    private EditText mEtMoney;
    //充值按钮
    private Button mBtnNext;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mBtnNext.setEnabled(true);
            }
        }
    };
    //列表选择的角标
    private int choice = 0;
    // 放置其它金额数据框的容器
    private View mOtherView;
    // 积分优先
    private SwitchButton mSwitchButtonView;
    //提现入口
    private TextView mTvWithDraw;
    //积分布局警告布局
    private LinearLayout mLLLockNotify;
    //现已积分控件
    private TextView mTVIntegral;
    //积分
    private long mIntegral = 300;
    //充值金额
    private Long mPayMoney;
    //现金支付
    private double mPayRMB;
    //是否是积分优先
    private boolean isIntegral = true;
    //积分是否缺少
    private boolean isIntegralLack = false;
    //充值获得UU
    private long mGetCoin;
    //列表UU数组
    private int[] mCoins;
    //钱数改变界定
    private int mBound = 50;
    //UU余额，来自网络加载
    private long mHavenCoin = 0;
    private TextView mTvCoinHaven;
    //未登录处理
    private ScrollView mSlLogin;
    private RelativeLayout mLLNotLogin;
    private TextView mTvGotoLogin;

    private Intent mIntent;
    private boolean isLogin = false;
    private TextView mTVNotLoginTitle;
    //支付订单号
    private String mOrder_number;
    private String mChannel;

    //支付结果跳转意图
    private Intent mPaySucceedIntent;

    private int mLimitMoneys = 2000;
    //查看提现详情的入口
//    private TextView mTvWithdrawRecord;
    //我的收益入口
    private LinearLayout mLLIsFive;
    //用户类型
    private int mMember_type;
    //微信绑定的openId
    private String mOpenId;
    //跳转的class
    private Class mClass;
    //跳转的Intent
    private Intent mIntentWithDraw;
    //安全码
    private String mSafeCode;
    //是否是测试用户
    private int isInTestUser;
    //uu详情的入口
    private TextView mTvUUDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_member_center);
        mIntent = getIntent();
        mHavenCoin = mIntent.getLongExtra("coin", 0);
        mIntegral = mIntent.getLongExtra("integral", 0);
        isLogin = mIntent.getBooleanExtra("isLogin", false);
        mMember_type = mIntent.getIntExtra("member_type", 0);
        isInTestUser = mIntent.getIntExtra("isInTestUser", 0);
        mSafeCode = mIntent.getStringExtra("safeCode");
        //test
//        mMember_type=5;

        mOpenId = mIntent.getStringExtra("openId");
        //test
//        mOpenId=null;
//        mOpenId="";

        loadData();
        initView();
        setupView();
        initControl();
    }

    private void initView() {
        mTvUUDetail = ((TextView) findViewById(R.id.tv_uu_detail));

        //根据是否是第五季会员控制显示
        mLLIsFive = ((LinearLayout) findViewById(R.id.ll__member_center_coin_income));
        if (mMember_type == 5 || isInTestUser == 1) {
            mLLIsFive.setVisibility(View.GONE);
        }
//        mTvWithdrawRecord = ((TextView) findViewById(R.id.tv_member_center_title_bar_withdraw_record));
        mTitleBar = (IUUTitleBar) findViewById(R.id.member_center_title_bar);
        mTagFlowLayout = (TagFlowLayout) findViewById(R.id.member_center_tfl);
        mEtMoney = (EditText) findViewById(R.id.member_center_et_money);
        mBtnNext = (Button) findViewById(R.id.member_center_btn_next);
        mOtherView = findViewById(R.id.member_center_rl_other);
        mSwitchButtonView = (SwitchButton) findViewById(R.id.member_center_sb);
        mTvWithDraw = ((TextView) findViewById(R.id.member_center_tv_withdraw));
        mLLLockNotify = ((LinearLayout) findViewById(R.id.ll_member_center_integral_lack));
        mTVIntegral = ((TextView) findViewById(R.id.tv_member_center_integral));
        //初始化积分控件
        mTVIntegral.setText("" + mIntegral);
        //初始化UU余额控价
        mTvCoinHaven = ((TextView) findViewById(R.id.tv_member_center_coin_haven));
        mTvCoinHaven.setText(mHavenCoin + "");

        mSlLogin = ((ScrollView) findViewById(R.id.sr_goldcoid_login));
        mLLNotLogin = ((RelativeLayout) findViewById(R.id.ll_not_login));
        mTVNotLoginTitle = ((TextView) findViewById(R.id.tv_not_login_title));
        mTVNotLoginTitle.setText("无法获得支付信息");
        mTvGotoLogin = ((TextView) findViewById(R.id.tv_goto_login));
        if (isLogin) {
            mSlLogin.setVisibility(View.VISIBLE);
            mLLNotLogin.setVisibility(View.GONE);
//            mTvWithdrawRecord.setVisibility(View.VISIBLE);
        } else {
            mSlLogin.setVisibility(View.GONE);
            mLLNotLogin.setVisibility(View.VISIBLE);
//            mTvWithdrawRecord.setVisibility(View.GONE);

        }
    }

    private void setupView() {
        mTvUUDetail.setOnClickListener(this);
        mLLIsFive.setOnClickListener(this);
        //提现详请监听
//        mTvWithdrawRecord.setOnClickListener(this);
        mTitleBar.setLeftLayoutClickListener(this);
        //列表监听
        mTagFlowLayout.setOnTagClickListener(this);
        mTagFlowLayout.setOnSelectListener(this);
        //充值监听
        mBtnNext.setOnClickListener(this);
        //登录跳转监听
        mTvGotoLogin.setOnClickListener(this);

        // 积分优先开关监听
        mSwitchButtonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isIntegral = isChecked;
//                    compareMoney();
                setButton();
            }
        });

        //输入充值变化监听
        mEtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String money = s.toString();
                if (money.length() > 8) {
                    money = money.substring(0, 8);
                    mEtMoney.setText(money);
                    mEtMoney.setSelection(money.length());
                }
                if (TextUtils.isEmpty(money)) {
                    mBtnNext.setText("充值");
                } else {
                    mPayMoney = Long.valueOf(money);
//                    mBtnNext.setText("充值("+mPayMoney+")");
//                    compareMoney();
                    setButton();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //提现监听
        mTvWithDraw.setOnClickListener(this);

        mTagFlowLayout.setOnTagClickListener(this);

    }

    private void loadData() {
        mVals = new String[]{"50UU\n=10元/积分", "100UU\n=20元/积分", "150UU\n=30元/积分",
                "260UU\n=50元/积分", "530UU\n=100元/积分", "1070UU\n=200元/积分", "1610UU\n=300元/积分",
                "2690UU\n=500元/积分", "5390UU\n=1000元/积分", "10790UU\n=2000元/积分", "其它金额"};
        mRMBs = new Long[]{10l, 20l, 30l, 50l, 100l, 200l, 300l, 500l, 1000l, 2000l};
        mCoins = new int[]{50, 100, 150, 260, 530, 1070, 1610, 2690, 5390, 10790};
    }

    private void initControl() {
        mTagAdapter = new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(MineMemberCenterActivity.this)
                        .inflate(R.layout.member_center_tv, mTagFlowLayout, false);

                int width = ScreenUtils.getScreenWidth(getApplicationContext());
                int textWidth = width / 4;

                textView.setText(s);

                LogUtils.e("width", "" + width);
                LogUtils.e("textHeight", textView.getHeight() + "");
                int textSpace = textWidth / 10;

                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(textWidth, textWidth / 2);
                textView.setPadding(0, 10, 0, 10);
                params.setMargins(textSpace, 0, textSpace, textSpace);
                textView.setLayoutParams(params);
                return textView;
            }
        };

        mTagFlowLayout.setAdapter(mTagAdapter);

        // 预设选中
        mTagAdapter.setSelectedList(0);
        mTagFlowLayout.getChildAt(0).setClickable(true);
        //初始化充值钱数
        mPayMoney = mRMBs[0];
        mGetCoin = mCoins[0];
        mBtnNext.setText("充值(" + mGetCoin + ")UU");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;
            case R.id.member_center_btn_next: // 充值
//                //处理充值
//                Dialog dialog= DialogUtils.createConfirmDialog(this, null, "是否确认充值？", "确认", "取消",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
                pay();
//                            }
//                        },
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }
//                );
//                dialog.show();

                //                jump(MineRechargeSuccessActivity.class,false);
                break;
            case R.id.member_center_tv_withdraw: // 提现入口
                mClass = WithdrawRecordActivity.class;
                mIntentWithDraw = new Intent(getApplicationContext(), WithdrawActivity.class);
                mIntentWithDraw.putExtra("havenCoin", mHavenCoin);
                mIntentWithDraw.putExtra("safeCode", mSafeCode);
                goToGetMoney();
//                jump(intentWithdraw,false);
                break;

            case R.id.tv_goto_login: // 登录入口

                jump(new Intent(getApplicationContext(), LoginActivity.class), false);
                finish();
                break;
//            case R.id.tv_member_center_title_bar_withdraw_record: // 提现详请入口
//                jump(WithdrawRecordActivity.class,false);
//                break;
            case R.id.ll__member_center_coin_income: // 我的收益入口
                mClass = UserIncomeActivity.class;
                mIntentWithDraw = new Intent(getApplicationContext(), UserIncomeActivity.class);
//                jump(UserIncomeActivity.class,false);
                goToGetMoney();
                break;
            case R.id.tv_uu_detail: // UU详情入口
                jump(UUDetailActivity.class, false);
                break;
        }

    }

    /**
     * 处理收益和提现页面的跳转
     * 一：绑定微信
     * 根据mClass跳转到
     * 二：没有绑定
     * 弹出框提示去公众号绑定
     */
    private void goToGetMoney() {
        //已经绑定的
        if (!TextUtils.isEmpty(mOpenId)) {
            jump(mIntentWithDraw, false);
        } else {
            //没绑定的，显示弹出框
            showDialog();
        }
    }

    /**
     * 处理充值
     */
    private void pay() {
        //判断积分是否够用
        compareMoney();
        //积分优先支付
        if (isIntegral) {
            if (!isIntegralLack) {
                //积分够用,直接向服务器提交减少积分
                LogUtils.e("UU充值", "积分支付优先支付" + mPayMoney + "还剩积分" + (mIntegral - mPayMoney));
            } else {
                //积分不够用,向服务器提交积分，带需要支付人民数值跳转到支付选择页面
                LogUtils.e("UU充值", "积分支付优先支付" + mIntegral + "积分" + mPayRMB + "人民币");
            }
        } else {
            //直接现金支付，带着值跳转到选择支付方式
            mPayRMB = mPayMoney;
            LogUtils.e("现金支付" + mPayRMB);
        }


        ///////////////生成支付订单///////////////

        final String token = CurrentUserManager.getUserToken();
        if (token != null) {
            int isIntegralPriority = -1;
            if (isIntegral) {
                isIntegralPriority = 1;//积分优先
            } else {
                isIntegralPriority = 0;//不是积分优先
            }

            //防止连续重复点击
            mBtnNext.setEnabled(false);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(0);
                }
            }, 10000);

            ClientAPI.postGoldCodePay(token, mPayMoney, isIntegralPriority, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    LogUtils.e("postIntegralPay--e:", e.toString());
                    if (mPayRMB > mLimitMoneys) {
                        Toast.makeText(getApplicationContext(), "充值失败；支付人民币不能超过2000元", Toast.LENGTH_SHORT).show();
                    } else {
                        UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
                    }
                }

                @Override
                public void onResponse(String response, int id) {
                    if ((isIntegral && isIntegralLack) || !isIntegral) {//积分优先并且积分不足;或者不使用积分.调用支付，否则直接充值成功
                        //拿到订单调用支付
                        //拿到订单调用支付
                        mOrder_number = new Gson().fromJson(response, PayOrderNum.class).getOrder_number();
                        //吊起支付
                        doPayByPingpp();
//                        doPayByPingpp();
                        //支付成功后跳转页
//                    jump(MineRechargeSuccessActivity.class,false);
                    } else {
                        //积分充值不返回订单号
//                        Toast.makeText(getApplicationContext(),"积分消费，UU充值成功",Toast.LENGTH_SHORT).show();
                        //跳转到支付成功页面
//                        mPaySucceedIntent=new Intent(getApplicationContext(),MineRechargeSuccessActivity.class);
//                        mPaySucceedIntent.putExtra("coin",mGetCoin+mHavenCoin);
//                        jump(mPaySucceedIntent,false);
                        MineRechargeSuccessActivity.actionStart(MineMemberCenterActivity.this, 0);
                        finish();
                    }

                }
            });
        }
    }

    /**
     * 判断积分是否够用
     */
    private void compareMoney() {
        isIntegralLack = mIntegral < mPayMoney ? true : false;
//        mBtnNext.setText("充值（"+mPayMoney+")");
        //显示积分不足提示
        if (isIntegral && isIntegralLack) {
            mPayRMB = mPayMoney - mIntegral;
            mLLLockNotify.setVisibility(View.VISIBLE);
        } else {
            mLLLockNotify.setVisibility(View.GONE);
        }
    }

    private void setButton() {
        //判断积分是否够用
        compareMoney();
        if (mPayMoney < mBound) {
            mGetCoin = (long) (mPayMoney * 5);
        } else {

            LogUtils.e("coin", "" + (mPayMoney % mBound) * 5);
            int bs = (int) (mPayMoney / mBound);
            LogUtils.e("coin", "" + bs);
            LogUtils.e("coin", "" + (260 * bs + 10 * (bs - 1)));
            mGetCoin = 260 * bs + 10 * (bs - 1) + ((long) mPayMoney % mBound) * 5;
        }
        mBtnNext.setText("充值（" + mGetCoin + "UU)");
        //积分优先支付
//        if (isIntegral){
//            if (!isIntegralLack){
//                //积分够用,直接向服务器提交减少积分
////                LogUtils.e("UU充值","积分支付优先支付"+mPayMoney+"还剩积分"+(mIntegral-mPayMoney));
//                mBtnNext.setText("充值（"+mPayMoney+"积分)");
//            }else {
//                //积分不够用,向服务器提交积分，带需要支付人民数值跳转到支付选择页面
////                LogUtils.e("UU充值","积分支付优先支付"+mIntegral+"积分"+mPayRMB+"人民币");
//                mBtnNext.setText("充值（"+mIntegral+"积分+"+mPayRMB+"人民币)");
//            }
//        }else {
//            //直接现金支付，带着值跳转到选择支付方式
//            mPayRMB=mPayMoney;
//            mBtnNext.setText("充值（"+mPayRMB+"人民币)");
////            LogUtils.e("现金支付"+mPayRMB);
//        }
    }


    /**
     * TagFlowLayout
     *
     * @param selectPosSet
     */
    @Override
    public void onSelected(Set<Integer> selectPosSet) {

    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        LogUtils.d(TAG, mVals[position]);
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (i != position) {
//                        parent.getChildAt(i).setEnabled(true);
                parent.getChildAt(i).setClickable(false);
            } else {
                parent.getChildAt(i).setClickable(true);
            }
        }
        //处理点击改变
        choice = position;
        changePay();
        return false;
    }

    /**
     * 处理条目点击事件的切换
     */

    private void changePay() {
        //判断是否其它金额，控制输入框的隐藏显示
        if (choice == mVals.length - 1) {
            mBtnNext.setText("充值");
            mLLLockNotify.setVisibility(View.GONE);
            mOtherView.setVisibility(View.VISIBLE);
            mEtMoney.setText("");
            //刚刚切换过来初始化
            mPayMoney = 0l;
            mPayRMB = 0;
        } else {
            //获得充值钱数,并且进行判断
            mPayMoney = mRMBs[choice];
//            compareMoney();
//            mBtnNext.setText("充值("+mPayMoney+")");
            setButton();
            mOtherView.setVisibility(View.GONE);
        }
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

    ///////////////////////////////////////////////////支付
    // 去付款2之调用ping++去付款
    private void doPayByPingpp() {
        // https://github.com/saiwu-bigkoo/Android-AlertView
        new AlertView("选择支付方式", null, "取消", null, new String[]{"微信支付","支付宝"}, this, AlertView.Style.ActionSheet, this).show();
    }

    //  https://github.com/saiwu-bigkoo/Android-AlertView
    // 所需
    @Override
    public void onItemClick(Object o, int position) {
        int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
        switch (position) {
            case 0: // 微信支付
                        new PaymentTask(MineMemberCenterActivity.this, MineMemberCenterActivity.this, mOrder_number, Constants.CHANNEL_WECHAT, mBtnNext, TAG)
                                .execute(new PaymentTask.PaymentRequest(Constants.CHANNEL_WECHAT, 1));

                break;

            case 1: // 支付宝支付
            new PaymentTask(MineMemberCenterActivity.this, MineMemberCenterActivity.this, mOrder_number, Constants.CHANNEL_ALIPAY, mBtnNext, TAG)
                    .execute(new PaymentTask.PaymentRequest(Constants.CHANNEL_ALIPAY, 1));
//                new PaymentTask(mOrder_number).execute(new PaymentRequest(Constants.CHANNEL_ALIPAY, amount));
//                mChannel=Constants.CHANNEL_ALIPAY;
//                break;
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mBtnNext.setOnClickListener(MineMemberCenterActivity.this);

        //支付页面返回处理
        PingppPayResult.setOnPayResultCallback(requestCode, resultCode, data, new PingppPayResult.OnPayResultCallback() {
            @Override
            public void onPaySuccess() {
                //跳转到支付成功页面
//                    mPaySucceedIntent=new Intent(getApplicationContext(),MineRechargeSuccessActivity.class);
//                    mPaySucceedIntent.putExtra("coin",mGetCoin+mHavenCoin);
//                    jump(mPaySucceedIntent,false);

                MineRechargeSuccessActivity.actionStart(MineMemberCenterActivity.this, 0);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MineMemberCenterActivity.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    private static String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        LogUtils.e("MineMemberCenterActivity--:", "url");
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


    // 提示去公众号绑定微信的
    private void showDialog() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.notify_binding_wechat_dialog_item, false)
                .build();
        View view = dialog.getCustomView();
        dialog.setCancelable(false);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_mine_member_dialog_close);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 修改窗口大小
        // http://blog.csdn.net/misly_vinky/article/details/19109517
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        dialogWindow.setGravity(Gravity.CENTER);

        dialog.show();
    }

}
