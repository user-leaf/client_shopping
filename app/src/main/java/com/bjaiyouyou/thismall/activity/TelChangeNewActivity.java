package com.bjaiyouyou.thismall.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.fragment.TelChangeFragment;
import com.bjaiyouyou.thismall.fragment.TelChangeLoseFragment;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

/**
 * 更换手机号页（新）
 *
 * http://www.cnblogs.com/tianzhijiexian/p/4459216.html
 *
 * @author kanbin
 * @date 2016/6/20
 */
public class TelChangeNewActivity extends BaseActivity implements View.OnClickListener {

    private FragmentManager fm;
    private TelChangeFragment mTelChangeFragment;
    private TelChangeLoseFragment mTelChangeLoseFragment;
    private IUUTitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_change_new);

        initView();
        setupView();
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.tel_change_new_title_bar);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);

        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mTelChangeFragment = new TelChangeFragment();
        mTelChangeLoseFragment = new TelChangeLoseFragment();
        transaction.add(R.id.tel_change_new_container,mTelChangeFragment,TelChangeFragment.TAG);
//        transaction.add(R.id.tel_change_new_container,mTelChangeLoseFragment,mTelChangeLoseFragment.TAG);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
        }
    }
}
