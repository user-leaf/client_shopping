package shop.imake.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import shop.imake.R;
import shop.imake.widget.IUUTitleBar;

public class WechatAddActivity extends BaseActivity implements View.OnClickListener {

    private IUUTitleBar mTitleBar;
    private EditText mEtId;
    private EditText mEtPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_add);
        initView();
        setUpView();
    }

    private void initView() {
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_wechat));
        mEtId = ((EditText) findViewById(R.id.et_wechat_id));
        mEtPassWord = ((EditText) findViewById(R.id.et_wechat_password));
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
