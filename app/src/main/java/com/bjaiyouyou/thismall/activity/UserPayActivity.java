package com.bjaiyouyou.thismall.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.utils.DialogUtils;

/**
 * 扫码支付页面
 */
public class UserPayActivity extends Activity implements View.OnClickListener {

    private ImageView mIvHeadImage; // 头像
    private TextView mTvName;       // 姓名
    private TextView mTvMoney;      // 金额
    private Button mBtnPay;         // 支付按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pay);

        initView();
        setupView();
    }

    private void initView() {
        mIvHeadImage = (ImageView) findViewById(R.id.user_pay_iv_head);
        mTvName = (TextView) findViewById(R.id.user_pay_tv_name);
        mTvMoney = (TextView) findViewById(R.id.user_pay_tv_money);
        mBtnPay = (Button) findViewById(R.id.user_pay_btn_pay);
    }

    private void setupView() {
        mBtnPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_pay_btn_pay:
                // 支付弹窗
                View inflate = LayoutInflater.from(this).inflate(R.layout.layout_user_pay, null);
                final Dialog payDialog = DialogUtils.createRandomDialog(this, null, null, null, null, null,
                        inflate
                );

                View ivClose = inflate.findViewById(R.id.user_pay_dialog_iv_close);
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
                ivClose.setOnClickListener(onClickListener);

                payDialog.show();
                break;
        }
    }
}
