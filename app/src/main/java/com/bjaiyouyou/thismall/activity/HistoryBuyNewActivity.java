package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.HistoryBuyRecycleViewAdapter;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.HistoryBuy;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.SpaceItemDecoration;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 历史购买
 *
 * @author QuXinhang
 *         Creare 2016/8/15 14:56
 */
public class HistoryBuyNewActivity extends BaseActivity {
    //商品列表
    private XRecyclerView mGV;
    //数据集合
    private List<HistoryBuy.DataBean> mData;
    //适配器
    private HistoryBuyRecycleViewAdapter mAdapter;
    //标题
    private IUUTitleBar mTitleBar;
    //接收意图
    private Intent mIntent;
    //标题名称
    private String mTitle;
    //数据加载为空的显示
    private ImageView mTVEmpty;
    //登录入口
    private TextView mTVLogin;
    //未登录显示
    private RelativeLayout mLLNotLogin;
    //网络连接失败显示
    private LinearLayout mTVUnNetWork;
    //默认显示，数据加载的页数
    private int mPage = 1;
    private int mLastPage;
    //是否存在下一页
    private boolean isHasNextPage = true;
    private TextView mTvGetDataAgain;
    private TextView mTVNotLoginTitle;

    private ImageView mIvEmpty;
    //是否是刷新
    private boolean isRefresh;
    //footView 提示没有更多信息
    private View footView;
    //最后一行
    private boolean isLast;
    //向下滑
    private boolean isDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_buy_show);

        mIntent = getIntent();
        mTitle = mIntent.getStringExtra("title");
        //适配器数据
        mData = new ArrayList<>();
        //获得传递过来的数据
        initView();
        initData();
        initControl();
        setupView();
    }

    private void onRecycleViewRefresh() {
        //刷新
        mPage = 1;
//        mAdapter.clear();
//        mAdapter.notifyDataSetChanged();
        initData();
        //停止刷新
//        mGV.onRefreshComplete();
    }


    private void initControl() {
        mAdapter = new HistoryBuyRecycleViewAdapter(HistoryBuyNewActivity.this, mData);
        mAdapter.setOnItemClickListener(new HistoryBuyRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //获取商品的ID
                long productID = mData.get(position-1).getProduct_id();
//        Toast.makeText(this,""+productID,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), GoodsDetailsActivity.class);
                intent.putExtra("productID", productID);
                startActivity(intent);
            }
        });
        mGV.setAdapter(mAdapter);
    }


    private void initData() {
        ClientAPI.getHistoryBuyData(CurrentUserManager.getUserToken(), mPage, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
                //判断时候是网络不可用
                if (NetStateUtils.isNetworkAvailable(getApplicationContext())) {
                    LogUtils.e("网络请求异常-e", e.toString());
                    LogUtils.e("网络请求异常-id", "id--" + id);
                    //2、当网络可用未登录
                    gvHide();
                    LogUtils.e("historyBuy", "数据为空");
                } else {
                    //1、当网络不可用的时候、显示网络不可用
                    Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    unNetWorkView();
                }
                //停止刷新
                mGV.loadMoreComplete();
                mGV.refreshComplete();
            }

            @Override
            public void onResponse(String response, int id) {
                //停止刷新
                mGV.loadMoreComplete();
                mGV.refreshComplete();
                if (!TextUtils.isEmpty(response)) {
                    HistoryBuy historyBuy = new Gson().fromJson(response.toString().trim(), HistoryBuy.class);
                    //是否含有下一页
                    isHasNextPage = !TextUtils.isEmpty(historyBuy.getNext_page_url());
                    mLastPage = historyBuy.getLast_page();
                    if (historyBuy.getData().size() != 0) {
                        //显示列表
                        gvShow();
                        if (isRefresh) {
                            mAdapter.clear();
                            mAdapter.notifyDataSetChanged();
                        }
                        mData.addAll(historyBuy.getData());
                        mAdapter.notifyDataSetChanged();

                        LogUtils.e("historyBuy", "数据不为空");
                        LogUtils.e("网络请求正常-id", "id--" + id);
                    } else {

                        gvEmpty();
                    }

                    //test
                } else {
                    gvEmpty();
                }
                //停止刷新
                mGV.loadMoreComplete();
                mGV.refreshComplete();
            }

        });
    }

    private void initView() {
        GridLayoutManager manager=new GridLayoutManager(getApplicationContext(),2);
        mGV = ((XRecyclerView) findViewById(R.id.gv_history_buy));
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mGV.setLayoutManager(manager);
        mGV.addItemDecoration(new SpaceItemDecoration(0,5,5,0));

        mTVEmpty = (ImageView) findViewById(R.id.iv_data_loading);
        mGV.setEmptyView(mTVEmpty);
        mIvEmpty = ((ImageView) findViewById(R.id.iv_history_buy_empty));

        footView= LayoutInflater.from(this).inflate(R.layout.fragment_home_foot,null);
        mGV.addFootView(footView);


        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_history_buy));
        mTVLogin = ((TextView) findViewById(R.id.tv_goto_login));
        mLLNotLogin = ((RelativeLayout) findViewById(R.id.ll_not_login));
        mTVNotLoginTitle = ((TextView) findViewById(R.id.tv_not_login_title));
        mTVNotLoginTitle.setText("无法获取历史购买信息");
        mTitleBar.setTitle(mTitle);
        mTVUnNetWork = ((LinearLayout) findViewById(R.id.ll_unnetwork));
        mTvGetDataAgain = ((TextView) findViewById(R.id.tv_get_data_again));

    }

    private void setupView() {
        mGV.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//                mLLFoot.setVisibility(View.VISIBLE);
                isRefresh = true;
                onRecycleViewRefresh();

            }

            @Override
            public void onLoadMore() {
                isRefresh = false;
                //加载
//        if (isHasNextPage){
                if (isHasNextPage) {
                    ++mPage;
                    initData();
                } else {
                    isLast = true;
//                    Toast.makeText(getApplicationContext(), "已经加载到最后一页了", Toast.LENGTH_SHORT).show();
                    mGV.loadMoreComplete();
                    mGV.refreshComplete();
                }

            }
        });

        mTitleBar.setLeftLayoutClickListener(this);
        mTvGetDataAgain.setOnClickListener(this);
        mTVLogin.setOnClickListener(this);
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.tv_goto_login:
//                jump(new Intent(this,LoginActivity.class),false);
//                startActivity(new Intent(this, LoginActivity.class));
                jump(LoginActivity.class,true);
                break;
            case R.id.tv_get_data_again:
                LogUtils.e("重新加载", "重新加载");
                initData();
                break;
        }
    }


    /**
     * 数据正常显示
     */
    private void gvShow() {
        mIvEmpty.setVisibility(View.GONE);
        mLLNotLogin.setVisibility(View.GONE);
        mTVEmpty.setVisibility(View.VISIBLE);
        mGV.setVisibility(View.VISIBLE);
        mTVUnNetWork.setVisibility(View.GONE);
    }

    /**
     * 未登录
     */
    private void gvHide() {
        mIvEmpty.setVisibility(View.GONE);
        mLLNotLogin.setVisibility(View.VISIBLE);
        mTVEmpty.setVisibility(View.GONE);
        mGV.setVisibility(View.GONE);
        mTVUnNetWork.setVisibility(View.GONE);
    }

    /**
     * 断网
     */
    private void unNetWorkView() {
        mIvEmpty.setVisibility(View.GONE);
        mLLNotLogin.setVisibility(View.GONE);
        mTVEmpty.setVisibility(View.GONE);
        mGV.setVisibility(View.GONE);
        mTVUnNetWork.setVisibility(View.VISIBLE);
    }

    /**
     * 历史购买为空
     */
    private void gvEmpty() {
        mLLNotLogin.setVisibility(View.GONE);
        mTVEmpty.setVisibility(View.GONE);
        mGV.setVisibility(View.GONE);
        mTVUnNetWork.setVisibility(View.GONE);
        //仅仅是历史购买为空页面可见
        mIvEmpty.setVisibility(View.VISIBLE);
    }

}
