package com.bjaiyouyou.thismall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bjaiyouyou.thismall.R;

/**
 * 我的收益（众汇）
 */
public class MyIncomeActivity extends Activity implements View.OnClickListener {

    private View mBackView;
    private View mIncomeDetailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income);

        initView();
        setupView();

    }

    private void initView() {
        mBackView = findViewById(R.id.left_layout);
        mIncomeDetailView = findViewById(R.id.right_layout);
    }

    private void setupView() {
        mBackView.setOnClickListener(this);
        mIncomeDetailView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:  // 返回
                finish();
                break;

            case R.id.right_layout: // 收益明细

                break;


        }
    }
}
