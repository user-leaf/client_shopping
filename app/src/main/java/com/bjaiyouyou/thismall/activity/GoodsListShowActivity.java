package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.GoodsAdapter;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.Goods;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
  *商品浏览列表页面
 * 1、抢购更多列表
  * @author QuXinhang
  *Creare 2016/7/11 11:25
  * 完成搭建
  *
  */
public class GoodsListShowActivity extends BaseActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener2,AdapterView.OnItemClickListener{
    //商品列表
    private PullToRefreshGridView mGV;
    //数据集合
    private List<Goods.DataBean> mData;
    //适配器
    private GoodsAdapter mAdapter;
    //标题
    private IUUTitleBar mTitleBar;
    //接收意图
    private Intent mIntent;
    //标题名称
    private String mTitle;
    //数据接口网址
//    private int mUrl;
    private String mUrl;
    private ImageView mTVEmpty;
    //断网布局
    private LinearLayout mLLUnNetWork;
    //重新加载数据的点击
    private TextView mTVGetDataAgain;

    private int mPage=1;
    private int mLastPage=1;
    //数据为空的显示
    private ImageView mIvDataBuy;
    //是否是下拉刷新
    private boolean isRefersh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list_show);

        mIntent=getIntent();
        mTitle=mIntent.getStringExtra("title");
//        mUrl=mIntent.getIntExtra("url",-1);
        mUrl=mIntent.getStringExtra("url");
        //获得传递过来的数据
        initView();
        setupView();
        initControl();
        initData();
    }

    private void initControl() {
        mData=new ArrayList<>();
        mAdapter=new GoodsAdapter(mData,GoodsListShowActivity.this);
        mGV.setAdapter(mAdapter);
    }

    private void initData() {
        mData=new ArrayList<>();
//       if (mUrl!=-1){
        if (!TextUtils.isEmpty(mUrl)){
           ClientAPI.getTimeGoodsData(mUrl,mPage ,new StringCallback() {
               @Override
               public void onError(Call call, Exception e, int id) {
                   UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
//                   UNNetWorkUtils.isNetHaveConnect(getApplicationContext(),mTVEmpty,mGV,mLLUnNetWork);
                   if (!NetStateUtils.isNetworkAvailable(getApplicationContext())){
                       unNetWorkView();
                   }
               }

               @Override
               public void onResponse(String response, int id) {
//                   UNNetWorkUtils.lvShow(mTVEmpty,mGV,mLLUnNetWork);
                   gvShow();
                   if (!TextUtils.isEmpty(response)){
                       Goods goods=new Gson().fromJson(response.toString().trim(),Goods.class);
                       mLastPage=goods.getLast_page();
                       if (goods!=null){
                           if (goods.getData()!=null){
                               //如果是下拉刷新清除数据
                               if (isRefersh){
                                   mAdapter.clear();
                               }
                               mAdapter.addAll(goods.getData());
                               mAdapter.notifyDataSetChanged();
                           }else {
                               dataEmpty();
                           }
                       }
                   }
               }
           });
       }else {
           Toast.makeText(this,"访问暂无数据",Toast.LENGTH_SHORT).show();
           finish();
       }
        //停止刷新
        mGV.onRefreshComplete();
    }



    private void initView() {
        mIvDataBuy = ((ImageView) findViewById(R.id.iv_more_buy_empty));
        mLLUnNetWork = ((LinearLayout) findViewById(R.id.ll_unnetwork));
        mTVGetDataAgain = ((TextView) findViewById(R.id.tv_get_data_again));
        mTVEmpty = ((ImageView) findViewById(R.id.iv_more_buy_empty));
        mGV = ((PullToRefreshGridView) findViewById(R.id.gv_purhchase_history));
        mGV.setEmptyView(mTVEmpty);
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_purhchase_history));
        mTitleBar.setTitle(mTitle);
    }

    private void setupView() {
        mGV.setOnItemClickListener(this);
        mGV.setOnRefreshListener(this);
        mGV.setMode(PullToRefreshBase.Mode.BOTH);
        mTitleBar.setLeftLayoutClickListener(this);
        mTVGetDataAgain.setOnClickListener(this);
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
    //上拉加载下拉刷新监听
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        isRefersh=true;
        mPage=1;
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        isRefersh=false;
        if (mPage<mLastPage){
            ++mPage;
            initData();
        }else {
            mGV.onRefreshComplete();
            Toast.makeText(getApplicationContext(),"已经加载到最后一页",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //获取商品的ID
        long productID=mAdapter.getItem(position).getProduct_id();
        LogUtils.e("productID","productID:"+productID);
//        Toast.makeText(getApplicationContext(),"productID:"+productID,Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getApplicationContext(), GoodsDetailsActivity.class);
        intent.putExtra("productID",productID);
        startActivity(intent);
    }

    private void gvShow(){
        mIvDataBuy.setVisibility(View.GONE);
        mTVEmpty.setVisibility(View.VISIBLE);
        mGV.setVisibility(View.VISIBLE);
        mLLUnNetWork.setVisibility(View.GONE);
        mIvDataBuy.setVisibility(View.GONE);
    }
    private void unNetWorkView(){
        mIvDataBuy.setVisibility(View.GONE);
        mTVEmpty.setVisibility(View.GONE);
        mGV.setVisibility(View.GONE);
        mLLUnNetWork.setVisibility(View.VISIBLE);
        mIvDataBuy.setVisibility(View.GONE);
    }
    private void dataEmpty() {
        mIvDataBuy.setVisibility(View.GONE);
        mTVEmpty.setVisibility(View.GONE);
        mGV.setVisibility(View.GONE);
        mLLUnNetWork.setVisibility(View.GONE);
        mIvDataBuy.setVisibility(View.VISIBLE);
    }
}
