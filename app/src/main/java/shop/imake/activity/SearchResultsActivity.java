package shop.imake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.adapter.SearchResultGoodsAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4ClientOther;
import shop.imake.client.ClientAPI;
import shop.imake.client.ClientApiHelper;
import shop.imake.client.HttpUrls;
import shop.imake.model.SearchResultGoods;
import shop.imake.utils.SpaceItemDecoration;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.widget.IUUTitleBar;
import shop.imake.widget.NoScrollGridView;

/**
 * @author Alice
 *         Creare 2016/6/4 10:44
 *         搜索结果页面
 */

public class SearchResultsActivity extends BaseActivity implements View.OnClickListener{
    //商品列表
    private XRecyclerView mGV;
    //搜索结果数据集合
    private List<SearchResultGoods.DataBean> mData;
    //适配器
    private SearchResultGoodsAdapter mAdapter;
    //标题
    private IUUTitleBar mTitleBar;
    //接收意图
    private Intent mIntent;
    //标题名称
    private String mTitle;
    //数据接口网址
//    private int mUrl;
    private String mKey;
    //列表空白显示
    private ImageView mTVEmpty;
    //当搜索为空的推荐地址
    //test
    private String mNullUrl=ClientAPI.API_POINT+ HttpUrls.SEARCH+"1";
    //为空推荐列表
    private PullToRefreshScrollView mScrollViewNull;
    private NoScrollGridView mGvNull;
    //为空提示
    private TextView mTvNullHing;
    //数据集合
    private List<SearchResultGoods.DataBean> mNullData;
    private LinearLayout mLLUnNetWork;
    private TextView mTVGetDataAgain;

    private int mPage=1;
    private int mLastPage=1;
    private ImageView mIVDataEmpty;
    private boolean isRefresh;
    private boolean isHaveNext=false;

    private Api4ClientOther mClient;
    public static final String TAG=SearchResultsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        mIntent=getIntent();
        mTitle=mIntent.getStringExtra("title");
//        mUrl=mIntent.getIntExtra("url",-1);
        mKey=mIntent.getStringExtra("key");

        //获得传递过来的数据
        initView();
        initControl();
        initData();
        setupView();
    }

    private void initControl() {
        mData=new ArrayList<>();
        mAdapter=new SearchResultGoodsAdapter(mData,getApplicationContext());
        mAdapter.setOnItemClickListener(new SearchResultGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long productID=mData.get(position-1).getId();
//        Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(), GoodsDetailsActivity.class);
                intent.putExtra("productID",productID);
                startActivity(intent);

            }
        });
        mGV.setAdapter(mAdapter);
    }

    private void initView() {
        mLLUnNetWork = ((LinearLayout) findViewById(R.id.ll_unnetwork));
        mTVGetDataAgain = ((TextView) findViewById(R.id.tv_get_data_again));
        mTVEmpty = ((ImageView) findViewById(R.id.iv_data_loading));
        mIVDataEmpty = ((ImageView) findViewById(R.id.iv_search_result_empty));

        GridLayoutManager manager=new GridLayoutManager(getApplicationContext(),2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mGV = ((XRecyclerView) findViewById(R.id.gv_search_result));
        mGV.setLayoutManager(manager);
        mGV.addItemDecoration(new SpaceItemDecoration(0,5,5,5));


        mGV.setEmptyView(mTVEmpty);
        mScrollViewNull = ((PullToRefreshScrollView) findViewById(R.id.scrl_search_result_null));
        mScrollViewNull.setVisibility(View.GONE);
        mGvNull = ((NoScrollGridView) findViewById(R.id.gv_search_result_null));
        mTvNullHing = ((TextView) findViewById(R.id.tv_search_result_null_hing));
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_search_result));
        mTitleBar.setTitle(mTitle);



    }

    private void setupView() {;
        mGV.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefresh=true;
                mPage=1;
                //停止刷新
                initData();
                mGV.setNoMore(false);
            }

            @Override
            public void onLoadMore() {
                isRefresh=false;
                if (isHaveNext){
                    mGV.setNoMore(false);
                    ++mPage;
                    initData();
                }else {
//                    Toast.makeText(getApplicationContext(),"已经加载到最后一页",Toast.LENGTH_SHORT).show();
                    mGV.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mGV.loadMoreComplete();
                            mGV.refreshComplete();
                        }
                    }, 1000);
                    mGV.setNoMore(true);
                }

            }
        });
        mTitleBar.setLeftLayoutClickListener(this);

        mTVGetDataAgain.setOnClickListener(this);

        //搜索为空推荐列表监听
        mGvNull.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();
                long productID=mNullData.get(position).getId();
                Toast.makeText(getApplicationContext(),""+productID,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(), GoodsDetailsActivity.class);
                intent.putExtra("productID",productID);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        if (!TextUtils.isEmpty(mKey)){
            mClient= (Api4ClientOther) ClientApiHelper.getInstance().getClientApi(Api4ClientOther.class);
            mClient.getSearchGoodsData(TAG, mKey, mPage, new DataCallback<SearchResultGoods>(getApplicationContext()) {
                @Override
                public void onFail(Call call, Exception e, int id) {
                    UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
                    //调用网络判断工具类
                    UNNetWorkUtils.isNetHaveConnect(getApplicationContext(),mTVEmpty,mGV,mLLUnNetWork);
                }

                @Override
                public void onSuccess(Object response, int id) {
                    //存在符合商品
                    if (response!=null){
                        mIVDataEmpty.setVisibility(View.GONE);
                        SearchResultGoods searchResultGoods= (SearchResultGoods) response;
                        mLastPage=searchResultGoods.getLast_page();
                        if (searchResultGoods.getNext_page_url()!=null){
                            isHaveNext=true;
                        }else {
                            isHaveNext=false;
                        }
                        UNNetWorkUtils.lvShow(mTVEmpty,mGV,mLLUnNetWork);
                        //如果是下拉刷新
                        if (isRefresh){
                            mAdapter.clear();
                        }
                        mAdapter.addAll(searchResultGoods .getData());
                        mAdapter.notifyDataSetChanged();
                        if (mAdapter.getItemCount()==0){
                            //数据是空,图片形式
                            dataEmpty();
//                            initNull();
                        }

                    }else {
                        dataEmpty();
                    }

                }
            });
        }else {
            Toast.makeText(this,"访问暂无数据",Toast.LENGTH_SHORT).show();
            finish();
        }
        //停止加载
        mGV.loadMoreComplete();
        mGV.refreshComplete();
    }

    private void dataEmpty() {
        mTVEmpty.setVisibility(View.GONE);
        mGV.setVisibility(View.GONE);
        mIVDataEmpty.setVisibility(View.VISIBLE);
    }

    /**
     * 当搜索不存在的时候
     */
    private void initNull() {
        mTVEmpty.setVisibility(View.GONE);
        mGV.setVisibility(View.GONE);

        mScrollViewNull.setVisibility(View.VISIBLE);

        mTvNullHing.setText("暂时没有搜到与“"+mTitle+"”相关商品，您可以换个词再试试");
        //加载推荐商品
        ClientAPI.getTimeGoodsData(mNullUrl, mPage,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
            }

            @Override
            public void onResponse(String response, int id) {
                mNullData=new ArrayList();
                //存在符合商品
                if (!TextUtils.isEmpty(response)){
                    mNullData= new Gson().fromJson(response.toString().trim(),SearchResultGoods.class).getData();
                    mAdapter=new SearchResultGoodsAdapter(mNullData,getApplicationContext());
//                    mGvNull.setAdapter(mAdapter);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_layout:
                finish();
                break;
            //断网后重新加载
            case R.id.tv_get_data_again:
                initData();
                break;
        }
    }
}
