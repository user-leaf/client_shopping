package shop.imake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import shop.imake.Constants;
import shop.imake.R;
import shop.imake.callback.PingppPayResult;
import shop.imake.client.ClientAPI;
import shop.imake.model.PayOrderNum;
import shop.imake.task.PaymentTask;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.LogUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UNNetWorkUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 兑换券充值页面
 * author Alice
 * created at 2017/4/20 18:14
 */
public class RechargeActivity extends BaseActivity {

    public  static String TAG=RechargeActivity.class.getSimpleName();

    private ImageView mTitleBack;
    //确认充值
    private TextView mTvChargeCommit;
    //充值金额
    private EditText mEtRechargeNum;
    //输入钱数位数限制
    private int mMoneyLimit = 9;
    //充值数额
    private int mPayMoney;
    //订单号
    private String mOrder_number;
    //支付方式
    private String mChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initView();
        setupView();
    }

    private void initView() {
        mTitleBack = ((ImageView) findViewById(R.id.tv_recharge_back));
        mTvChargeCommit = ((TextView) findViewById(R.id.tv_recharge_commit));
        mEtRechargeNum = ((EditText) findViewById(R.id.et_recharge_num));
    }

    private void setupView() {
        mTitleBack.setOnClickListener(this);
        mTvChargeCommit.setOnClickListener(this);
        mEtRechargeNum.addTextChangedListener(new TextWatcher() {
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
                    mEtRechargeNum.setText(string);
                    //控制光标的位置
                    mEtRechargeNum.setSelection(string.length());
                }

                if (string.length() > mMoneyLimit) {
                    //不可编辑当长度超长的时候不能删除
//                    mEtWithDraw.setEnabled(false);
//                    mEtWithDraw.setFocusable(false);
                    //超长不显示，还可编辑
                    string = string.substring(0, mMoneyLimit);
                    mEtRechargeNum.setText(string);
                    mEtRechargeNum.setSelection(string.length());
                }
                if (!TextUtils.isEmpty(string)) {
                    if (!TextUtils.isEmpty(string)) {
                        mPayMoney = Integer.valueOf(string);
                    } else {
                        mPayMoney = 0;
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.tv_recharge_back:
                finish();
                break;
            case R.id.tv_recharge_commit:
                //关闭键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                //请求接口确认充值
                chargeCommit();
                break;

            default:
                return;
        }
    }

    /**
     * 请求接口确认充值
     */
    private void chargeCommit() {
        if (mPayMoney==0){
            ToastUtils.showShort("请输入充值金额");
            return;
        }
        String token = CurrentUserManager.getUserToken();
        if (token != null) {
            ClientAPI.postRechargePay(token, mPayMoney, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);

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


    //////////////////////////////////支付
    // 去付款2之调用ping++去付款
    private void doPayByPingpp() {

        // https://github.com/saiwu-bigkoo/Android-AlertView
        new AlertView("选择支付方式", null, "取消", null, new String[]{getString(R.string.pay_alipay)
        }, this, AlertView.Style.ActionSheet, this).show();
    }

    //  https://github.com/saiwu-bigkoo/Android-AlertView
    // 所需
    @Override
    public void onItemClick(Object o, int position) {
        super.onItemClick(o, position);
        int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
        switch (position) {
            case 0: // 支付宝支付
                mChannel = Constants.CHANNEL_ALIPAY;
                toPay();
                break;
            case 1: // 余额支付

                break;
            case 2: // 环迅支付

                break;
            default:
                return;
        }
    }


    private void toPay() {
        new PaymentTask(RechargeActivity.this, RechargeActivity.this, mOrder_number, mChannel, mTvChargeCommit, TAG)
                .execute(new PaymentTask.PaymentRequest(mChannel, 1));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mTvChargeCommit.setOnClickListener(RechargeActivity.this);

        //支付页面返回处理

        PingppPayResult.setOnPayResultCallback(requestCode, resultCode, data, new PingppPayResult.OnPayResultCallback() {
            @Override
            public void onPaySuccess() {
                //支付成功后跳转页
                finish();
            }

            @Override
            public void onPayFail() {

            }
        });
    }
}
