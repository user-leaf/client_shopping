package com.bjaiyouyou.thismall.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.utils.DialogUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

import java.text.DecimalFormat;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 扫码支付页面
 */
public class UserPayActivity extends Activity implements View.OnClickListener {

    public static final String TAG = UserPayActivity.class.getSimpleName();
    private IUUTitleBar mTitleBar;
    private ImageView mIvHead;  // 头像
    private TextView mTvName;   // 姓名

    // 固定金额支付
    private View mLlPayBanner;          // 支付栏
    private TextView mTvMoney;          // 固定金额
    private Button mBtnPay;             // 支付按钮

    // 自定义金额支付
    private View mLlPayBannerCustomMoney;  // 支付栏
    private EditText mEtMoney;             // 金额输入框
    private Button mBtnPayCustomMoney;     // 支付按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pay);

        initView();
        setupView();
        showBanner(2);
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.title_bar);

        mIvHead = (ImageView) findViewById(R.id.user_pay_iv_head);
        mTvName = (TextView) findViewById(R.id.user_pay_tv_name);

        mLlPayBanner = findViewById(R.id.user_pay_ll);
        mTvMoney = (TextView) findViewById(R.id.user_pay_tv_money);
        mBtnPay = (Button) findViewById(R.id.user_pay_btn_pay);

        mLlPayBannerCustomMoney = findViewById(R.id.user_pay_ll_custom_money);
        mEtMoney = (EditText) findViewById(R.id.user_pay_et_money);
        mBtnPayCustomMoney = (Button) findViewById(R.id.user_pay_btn_pay_custom_money);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mEtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 限定只能输入两位小数
                // http://blog.csdn.net/yinzhijiezhan/article/details/46819261
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        mEtMoney.setText(s);
                        mEtMoney.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    mEtMoney.setText(s);
                    mEtMoney.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mEtMoney.setText(s.subSequence(0, 1));
                        mEtMoney.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBtnPay.setOnClickListener(this);
        mBtnPayCustomMoney.setOnClickListener(this);

    }

    /**
     * 选择显示支付栏
     * @param i 1或者2
     */
    private void showBanner(int i) {
        switch (i) {
            case 1:
                mLlPayBanner.setVisibility(View.VISIBLE);
                mLlPayBannerCustomMoney.setVisibility(View.GONE);
                break;
            case 2:
                mLlPayBanner.setVisibility(View.GONE);
                mLlPayBannerCustomMoney.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;

            case R.id.user_pay_btn_pay: // 固定金额支付
                showPayDialog(123.);
                break;

            case R.id.user_pay_btn_pay_custom_money:    // 自定义金额支付
                String strMoney = mEtMoney.getText().toString();
                if (TextUtils.isEmpty(strMoney) || "0".equals(strMoney)){
                    ToastUtils.showShort("请输入支付金额");
                    return;
                }
                Double aDouble = Double.valueOf(strMoney);
                showPayDialog(aDouble);
                break;
        }
    }

    /**
     * 支付弹窗
     * @param money 金额
     */
    private void showPayDialog(Double money) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_user_pay, null);
        final Dialog payDialog = DialogUtils.createRandomDialog(this, null, null, null, null, null,
                inflate
        );

        View ivClose = inflate.findViewById(R.id.user_pay_dialog_iv_close);
        TextView tvMoney = (TextView) inflate.findViewById(R.id.user_pay_dialog_tv_money);
        EditText etSafecode = (EditText) inflate.findViewById(R.id.user_pay_dialog_et_psw);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.user_pay_dialog_iv_close:
                        if (payDialog != null && payDialog.isShowing()) {
                            payDialog.dismiss();
                        }
                        break;
                }
            }
        };

        tvMoney.setText(String.valueOf(money));
        ivClose.setOnClickListener(onClickListener);

        payDialog.show();
    }
}
