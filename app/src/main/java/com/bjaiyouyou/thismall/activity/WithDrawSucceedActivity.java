package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

/**
 * 换取成功页面
 * @author QuXinhang
 *Creare 2016/11/11 18:41
 */
public class WithDrawSucceedActivity extends BaseActivity implements View.OnClickListener{

    private IUUTitleBar mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw_succeed);
        initView();
        setUpView();
    }

    private void setUpView() {
        mTitle.setOnClickListener(this);
    }

    private void initView() {
        mTitle = ((IUUTitleBar) findViewById(R.id.title_withdraw_succeed));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_withdraw_succeed:
                finish();
                break;
        }
    }
}
