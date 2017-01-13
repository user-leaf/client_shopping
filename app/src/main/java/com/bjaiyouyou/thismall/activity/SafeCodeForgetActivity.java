package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

/**
 * 安全码重置链接发送
 * @author QuXinhang
 *Creare 2016/8/25 11:19
 *
 *
 */
public class SafeCodeForgetActivity extends BaseActivity implements View.OnClickListener{

    private IUUTitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_code_forget);
        initView();
        setUpView();
    }

    private void setUpView() {
        mTitleBar.setLeftLayoutClickListener(this);
    }

    private void initView() {
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_safe_code_forget));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_layout:
                finish();
                break;
        }
    }
}
