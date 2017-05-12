package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 众汇券详情页面
 *author Alice
 *created at 2017/5/12 11:17
 */

public class ZhongHuiDetailActivity extends BaseActivity {

    //列表
    private PullToRefreshListView mLv;
    private IUUTitleBar mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhong_hui_detail);
        initView();
        initData();
        initCtrl();
        setupView();
    }

    private void initView() {
        mLv = ((PullToRefreshListView) findViewById(R.id.ptrlv_zh_detail));
        mLv.setMode(PullToRefreshBase.Mode.BOTH);
        mTitle = ((IUUTitleBar) findViewById(R.id.title_zh_detail));
    }

    private void setupView() {
        mTitle.setLeftLayoutClickListener(this);

        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载


            }
        });
    }

    private void initData() {

    }

    private void initCtrl() {

    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            //退出
            case R.id.left_layout:
                finish();
                break;

            default:
                return;

        }
    }
}
