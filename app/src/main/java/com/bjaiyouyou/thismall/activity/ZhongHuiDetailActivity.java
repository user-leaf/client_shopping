package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.bjaiyouyou.thismall.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 众汇券详情页面
 *author Alice
 *created at 2017/5/12 11:17
 */

public class ZhongHuiDetailActivity extends AppCompatActivity {

    private PullToRefreshListView mLv;

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
    }

    private void setupView() {
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
}
