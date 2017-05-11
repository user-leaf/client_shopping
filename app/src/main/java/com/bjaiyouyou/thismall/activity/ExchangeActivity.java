package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;

/**
 *兑换页面
 *author Alice
 *created at 2017/4/20 18:13
 */
public class ExchangeActivity extends BaseActivity {

    private TextView mTvBack;
    private TextView mTvCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        initView();
        setupView();
        initData();
        
    }

    private void initView() {
        mTvBack = ((TextView) findViewById(R.id.tv_exchange_back));
        mTvCommit = ((TextView) findViewById(R.id.tv_exchange_commit));
    }
    private void initData() {
    }
    private void setupView() {
        mTvBack.setOnClickListener(this);
        mTvCommit.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0,R.anim.anim_activate_exchange_certificate_close);
    }

    /**
     * @param v
     */
    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            case R.id.tv_exchange_back:
                finish();
                break;

            case R.id.tv_exchange_commit:
                commitExChange();
                break;

            default:
                return;
        }
    }


    /**
     * 提交兑换请求
     */

    private void commitExChange() {
    }
}
