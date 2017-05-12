package com.bjaiyouyou.thismall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;

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
                break;
        }
    }
}
