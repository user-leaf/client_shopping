package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

/**
 * 个人中心-客服
 *
 * @author kanbin
 * @date 2016/6/16
 */
public class MineCustomerServiceActivity extends BaseActivity implements View.OnClickListener {

    private View mSuggestionView;
    private IUUTitleBar mTitleBar;
    private View mContactView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_customer_service);

        initView();
        setupView();
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.customer_service_title_bar);
        mSuggestionView = findViewById(R.id.customer_service_suggestion);
        mContactView = findViewById(R.id.customer_service_contacts);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mSuggestionView.setOnClickListener(this);
        mContactView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;
            case R.id.customer_service_suggestion: // 意见反馈
                jump(MineCustomerServiceSuggestionActivity.class, false);
                break;

            case R.id.customer_service_contacts: // 手机通讯录页
                jump(MobileContactActivity.class,false);
//                jump(InviteActivity.class, false);
                break;
        }
    }
}
