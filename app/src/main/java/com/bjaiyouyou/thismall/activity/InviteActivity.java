package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.fragment.InviteAllFragment;
import com.bjaiyouyou.thismall.fragment.InviteMineFragment;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请好友页
 *
 * User: kanbin
 */
public class InviteActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private IUUTitleBar mTitleBar;
    // 栏目
    private RadioGroup mRadioGroup;

    private List<Fragment> fragments;
    // 记录当前是哪个Fragment
    private int current;

    private InviteAllFragment inviteAllFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        initView();
        setupView();

    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.invite_title_bar);
        mRadioGroup = (RadioGroup) findViewById(R.id.invite_rg);

        /**
         * fragments
         */
        fragments = new ArrayList<>();
        inviteAllFragment = new InviteAllFragment();
        InviteMineFragment inviteMineFragment = new InviteMineFragment();
        fragments.add(inviteAllFragment);
        fragments.add(inviteMineFragment);

        getSupportFragmentManager().beginTransaction().add(R.id.invite_container, inviteAllFragment).commit();

    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton rbtn = (RadioButton) group.getChildAt(i);
            if (rbtn.getId() == checkedId) {
                rbtn.setChecked(true);
                changeFragment(i);
            } else {
                rbtn.setChecked(false);
            }
        }

    }

    // 执行替换Fragment的操作
    private void changeFragment(int i) {
        // 拿到事务管理器
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 判断当前Fragment 是否被添加过
        if (!fragments.get(i).isAdded()) {
            // 没有被添加过 添加 之后将当前的Fragment隐藏
            transaction.add(R.id.invite_container, fragments.get(i)).hide(fragments.get(current)).commit();
        } else {
            // 添加过 将目标Fragment 显示 并将当前的Fragment 隐藏
            transaction.show(fragments.get(i)).hide(fragments.get(current)).commit();
        }
        // 替换后目标id为 当前的current
        current = i;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getContactPermissionsResult(requestCode,resultCode);

    }

    public void getContactPermissionsResult(int requestCode,int resultCode) {
        LogUtils.e("order","授权结果");
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == Constants.CALL_PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            Toast.makeText(this,"未授权，不能获取通讯录",Toast.LENGTH_SHORT).show();
            finish();
        }else if(requestCode == Constants.CALL_PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED){
            inviteAllFragment.getContact();
        }
    }
}
