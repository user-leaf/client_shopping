package com.bjaiyouyou.thismall.activity;
/**
 * 关于我么页面
 * @author Alice
 *Creare 2016/8/27 19:57
 *
 *
 */
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.utils.AppUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;


public class AboutIUUActivity extends BaseActivity implements View.OnClickListener{

    private IUUTitleBar mTitleBar;
    private View mCompanyView;
    private TextView mTvVersions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_iuu);
        initView();
        setUpView();
        setData();
    }



    private void initView() {
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_about_iuu));
        mCompanyView = findViewById(R.id.tv_about_iuu_company);
        mTvVersions = ((TextView) findViewById(R.id.tv_about_iuu_versions));

    }

    private void setUpView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mCompanyView.setOnClickListener(this);
    }
    private void setData() {
        mTvVersions.setText("版本 v"+AppUtils.getVersionName(getApplicationContext())+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_layout:
                finish();
                break;

            case R.id.tv_about_iuu_company:
//                jump(MineRechargeSuccessActivity.class, false);
                break;
        }
    }
}
