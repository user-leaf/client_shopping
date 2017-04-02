package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.UserInComeModel;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.MathUtil;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

/**
 * 我的收益
 * <p/>
 * Created by kb on 2016/11/11.
 */
public class UserIncomeActivity extends BaseActivity implements View.OnClickListener {
    private IUUTitleBar mTitle;
    //我的总收益
    private TextView mTvIncomeAll;
    //剩余UU
    private TextView mTvUU;
    //安全码输入
    private EditText mEtSafecode;
    //提现金额
    private EditText mEtIncomeWant;
    //收款人
    private EditText mEtPayee;
    //预计24小时到账，“24小时”需要变红
    private TextView mTvIncomeTime;
    //提现按钮
    private Button mBtnWithdraw;
    private View mBodyView;
    // 我的总收益
    private String income = "";
    // 剩余UU
    private String uu = "";
    private UserInComeModel.MemberBean mMember;
    // 错误提示
    private TextView mTvTip;

    /**
     * 用户类型
     */
    public enum UserType {
        user_5_vip,      // 第5季 + vip
        user_test_vip,  // 内测 + vip
        user_nor_vip,   // 普通 + vip
        user_5,          // 第5季
        user_test,      // 内测
        user_nor        // 普通
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_income);
        initView();
        setupView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();

    }

    private void initView() {
        mTitle = ((IUUTitleBar) findViewById(R.id.title_income));
        mTvIncomeAll = ((TextView) findViewById(R.id.tv_income_all));
        mTvUU = ((TextView) findViewById(R.id.tv_income_coin_all));
        mEtSafecode = ((EditText) findViewById(R.id.et_income_safe_code));
        mEtIncomeWant = ((EditText) findViewById(R.id.et_income_want));
        mEtPayee = ((EditText) findViewById(R.id.et_income_payee_name));
        mTvIncomeTime = ((TextView) findViewById(R.id.tv_income_time));
        mBtnWithdraw = ((Button) findViewById(R.id.btn_income_withdraw));
        mBodyView = findViewById(R.id.ll_income_body);
        mTvTip = (TextView) findViewById(R.id.tv_income_tip);
    }

    private void setupView() {
        mTitle.setLeftLayoutClickListener(this);
        mBtnWithdraw.setOnClickListener(this);
        mEtIncomeWant.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                while (str.startsWith("0")){
                    str = str.substring(1, str.length());
                    mEtIncomeWant.setText(str);
                }

                double min = getMoneyMin(income, uu, str);
                double input = 0;
                if (!TextUtils.isEmpty(str)){
                    input = Double.parseDouble(str);
                }
                if (input != min) {
                    mEtIncomeWant.setText("" + (int) min);
                    mEtIncomeWant.setSelection(mEtIncomeWant.getText().length());
                }
            }
        });
//        mTitle.setRightLayoutClickListener(this);
    }

    private void initData() {
        // TODO: 2016/11/11 登录之后存储用户信息

        resetData();
        ClientAPI.getUserIncome(TAG, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
            }

            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response) && !"[]".equals(response)) {
                    Gson gson = new Gson();
                    UserInComeModel userIncomeModel = gson.fromJson(response, UserInComeModel.class);
                    if (userIncomeModel != null) {
                        mMember = userIncomeModel.getMember();

                        if (mMember != null) {
                            UserType userType = getUserType(mMember);

                            switch (userType) {
                                case user_5_vip:
                                case user_nor_vip:
                                case user_test_vip:
                                    // 可提收益 | push_money字段
                                    mBodyView.setVisibility(View.VISIBLE);

                                    income = "" + mMember.getPush_money();
                                    uu = "" + mMember.getMoney_quantity();
                                    double uudouble = Double.parseDouble(uu);
                                    mTvIncomeAll.setText(income);
                                    mTvUU.setText(((int) uudouble + "UU"));
                                    mEtSafecode.setHint(mMember.getSecurity_code_hint() + "******");
                                    break;

                                case user_5:
                                case user_test:
                                    // 只显示收益，隐藏其它 | cannot_push_money字段
                                    mBodyView.setVisibility(View.GONE);

                                    income = "" + mMember.getCannot_push_money();

                                    mTvIncomeAll.setText(income);
                                    mTvIncomeAll.setTextColor(getResources().getColor(R.color.app_gray_bbbbbb));
                                    break;

                                case user_nor:
                                    // 无入口，进不来
                                    break;
                            }
                        }
                    }
                }
            }
        });

    }

    private void resetData() {
        mEtIncomeWant.getText().clear();
        mEtSafecode.getText().clear();
        mEtPayee.getText().clear();
        mEtIncomeWant.requestFocus();
    }

//    @Override
//    public void onClick(View v) {
//    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);

        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;

            case R.id.right_layout:
                Intent intent = new Intent(this, WebShowActivity.class);
                intent.putExtra(WebShowActivity.PARAM_URLPATH, ClientAPI.WITHDRAW_RULE);
                jump(intent, false);
                break;
            case R.id.btn_income_withdraw: // 换取收益
                withdrawIncome();
                break;
        }
    }

    /**
     * 提现收益
     */
    private void withdrawIncome() {
        String strWant = mEtIncomeWant.getText().toString();
        String strSafecode = mEtSafecode.getText().toString();
        String strPayee = mEtPayee.getText().toString();

        LogUtils.d(TAG, "strWant: " + strWant + ", strSafecode: " + strSafecode + ", strPayee: " + strPayee);

        if (TextUtils.isEmpty(strWant)) {
            mTvTip.setText("请输入换取金额");
            return;
        }

        int amount = Integer.parseInt(strWant);

        if (amount <= 0) {
            mTvTip.setText("没有可提收益");
            return;
        }

        if (TextUtils.isEmpty(strSafecode)) {
            mTvTip.setText("请输入安全码");
            return;
        }

        if (TextUtils.isEmpty(strPayee)) {
            mTvTip.setText("请输入姓名");
            return;
        }

        String open_id = mMember.getOpen_id();

        if (canWithdraw(mMember)) {
            showLoadingDialog();
            ClientAPI.postWithDraw(TAG, open_id, amount, strPayee, strSafecode, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    dismissLoadingDialog();
                    ToastUtils.showException(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    dismissLoadingDialog();
                    jump(WithDrawSucceedActivity.class, false);
                }
            });
        } else {
            showDialog();
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

    /**
     * 收益是否可提取
     *
     * @param memberBean 用户信息对象
     * @return
     */
    private boolean canWithdraw(UserInComeModel.MemberBean memberBean) {
        // 如果没有openid无法提现
        boolean ret = false;
        if (memberBean != null) {
            String open_id = memberBean.getOpen_id();
            if (TextUtils.isEmpty(open_id)) {
                ret = false;
            } else {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * 得到可提取的最小值
     *
     * @param income 我的总收益
     * @param uu     剩余UU
     * @param want   提现金额
     * @return
     */
    private double getMoneyMin(String income, String uu, String want) {
        if (null == income || TextUtils.isEmpty(income)) {
            income = "0";
        }
        if (null == uu || TextUtils.isEmpty(uu)) {
            uu = "0";
        }
        if (null == want || TextUtils.isEmpty(want)) {
            want = "0";
        }

        double v1 = Double.parseDouble(income);
        double v2 = Double.parseDouble(uu);
        double v3 = Double.parseDouble(want);

        return Math.min(Math.min(v1, v2), v3);

    }

    /**
     * 返回用户类型
     *
     * @param memberBean 用户对象
     * @return
     */
    private UserType getUserType(UserInComeModel.MemberBean memberBean) {
        boolean isVip = memberBean.getIs_vip() == 2 ? true : false;
        boolean is5 = memberBean.getMember_type() == 5 ? true : false;
        boolean isTest = memberBean.getIs_in_test_user() == 1 ? true : false;

        if (isVip && is5) {
            return UserType.user_5_vip;
        } else if (!isVip && is5) {
            return UserType.user_5;
        } else if (isVip && isTest) {
            return UserType.user_test_vip;
        } else if (!isVip && isTest) {
            return UserType.user_test;
        } else if (isVip && !is5 && !isTest) {
            return UserType.user_nor_vip;
        } else {
            return UserType.user_nor;
        }

    }
}
