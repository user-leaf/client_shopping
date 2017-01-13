package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

public class ZhiFuBaoAddActivity extends BaseActivity implements View.OnClickListener {

    private IUUTitleBar mTitleBar;
    private EditText mEtId;
    private EditText mEtPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhi_fu_bao_add);
        initView();
        setUpView();
    }

    private void initView() {
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_zhifubao));
        mEtId = ((EditText) findViewById(R.id.et_zhifubao_id));
        mEtPassWord = ((EditText) findViewById(R.id.et_zhifubao_password));

    }

    private void setUpView() {
        mTitleBar.setLeftLayoutClickListener(this);
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
