package shop.imake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.adapter.HistoryBuyRecycleViewAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4ClientOther;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.HistoryBuy;
import shop.imake.utils.LogUtils;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.SpaceItemDecoration;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.widget.IUUTitleBar;

/**
 * 历史购买
 *
 * @author Alice
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
    //是否存在下一页
    private boolean isHasNextPage = true;
    //再次获取数据按钮
    private TextView mTvGetDataAgain;
    private TextView mTVNotLoginTitle;

    private ImageView mIvEmpty;
    //是否是刷新
    private boolean isRefresh;

    private Api4ClientOther mClient;
    public static final String TAG = HistoryBuyNewActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_buy_show);

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
        initData();
    }


    private void initControl() {
        mAdapter = new HistoryBuyRecycleViewAdapter(HistoryBuyNewActivity.this, mData);
        mAdapter.setOnItemClickListener(new HistoryBuyRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //获取商品的ID
                long productID = mData.get(position - 1).getProduct_id();
                Intent intent = new Intent(getApplicationContext(), GoodsDetailsActivity.class);
                intent.putExtra("productID", productID);
                startActivity(intent);
            }
        });
        mGV.setAdapter(mAdapter);
    }


    private void initData() {
        mClient = (Api4ClientOther) ClientApiHelper.getInstance().getClientApi(Api4ClientOther.class);

        mClient.getHistoryData(TAG, mPage, new DataCallback<HistoryBuy>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
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
                    unNetWorkView();
                }
                //停止刷新
                mGV.loadMoreComplete();
                mGV.refreshComplete();

            }

            @Override
            public void onSuccess(Object response, int id) {
                //停止刷新
                mGV.loadMoreComplete();
                mGV.refreshComplete();
                if (response != null) {
                    HistoryBuy historyBuy = (HistoryBuy) response;
                    //是否含有下一页
                    isHasNextPage = !TextUtils.isEmpty(historyBuy.getNext_page_url());
                    if (isHasNextPage) {
                        mGV.setNoMore(false);
                    } else {
                        mGV.setNoMore(true);
                    }

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
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
        mGV = ((XRecyclerView) findViewById(R.id.gv_history_buy));
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mGV.setLayoutManager(manager);
        mGV.addItemDecoration(new SpaceItemDecoration(0, 5, 5, 0));

        mTVEmpty = (ImageView) findViewById(R.id.iv_data_loading);
        mGV.setEmptyView(mTVEmpty);
        mIvEmpty = ((ImageView) findViewById(R.id.iv_history_buy_empty));

        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_history_buy));
        mTVLogin = ((TextView) findViewById(R.id.tv_goto_login));
        mLLNotLogin = ((RelativeLayout) findViewById(R.id.ll_not_login));
        mTVNotLoginTitle = ((TextView) findViewById(R.id.tv_not_login_title));
        mTVNotLoginTitle.setText("无法获取历史购买信息");
        mTVUnNetWork = ((LinearLayout) findViewById(R.id.ll_unnetwork));
        mTvGetDataAgain = ((TextView) findViewById(R.id.tv_get_data_again));

    }

    private void setupView() {
        mGV.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                onRecycleViewRefresh();

            }

            @Override
            public void onLoadMore() {
                isRefresh = false;
                //加载
                if (isHasNextPage) {
                    ++mPage;
                    initData();
                } else {

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
            case R.id.tv_goto_login://登录
                jump(LoginActivity.class, true);
                break;
            case R.id.tv_get_data_again://重新加载
                onRecycleViewRefresh();
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
