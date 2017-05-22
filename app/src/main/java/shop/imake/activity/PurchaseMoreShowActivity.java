package shop.imake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import shop.imake.R;
import shop.imake.adapter.GoodsAdapter;
import shop.imake.client.ClientAPI;
import shop.imake.model.Goods;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.widget.IUUTitleBar;
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
  * @author Alice
  *Creare 2016/7/11 11:25
  * 完成搭建
  *
  */
public class PurchaseMoreShowActivity extends BaseActivity implements AdapterView.OnItemClickListener,View.OnClickListener,PullToRefreshBase.OnRefreshListener2{
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
    private TextView mTVEmpty;
    private LinearLayout mLLUnNetWork;
    private TextView mTVGetDataAgain;

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
        initData();
        setupView();
    }

    private void initData() {
        //停止刷新
        mGV.onRefreshComplete();

        mData=new ArrayList<>();
//       if (mUrl!=-1){
        if (!TextUtils.isEmpty(mUrl)){
           ClientAPI.getGoodsData(mUrl, new StringCallback() {
                       @Override
                       public void onError(Call call, Exception e, int id) {
                           UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
                           UNNetWorkUtils.isNetHaveConnect(getApplicationContext(),mTVEmpty,mGV,mLLUnNetWork);
                       }

                       @Override
                       public void onResponse(String response, int id) {
                           UNNetWorkUtils.lvShow(mTVEmpty,mGV,mLLUnNetWork);
                           if (!TextUtils.isEmpty(response)){
                               mData= new Gson().fromJson(response.toString().trim(),Goods.class).getData();
                               mAdapter=new GoodsAdapter(mData,PurchaseMoreShowActivity.this);
                               mGV.setAdapter(mAdapter);
                           }
                       }
                   }
           );
       }else {
           Toast.makeText(this,"访问暂无数据",Toast.LENGTH_SHORT).show();
           finish();
       }
    }

    private void initView() {
        mLLUnNetWork = ((LinearLayout) findViewById(R.id.ll_unnetwork));
        mTVGetDataAgain = ((TextView) findViewById(R.id.tv_get_data_again));
        mTVEmpty = ((TextView) findViewById(R.id.tv_data_loading));
        mGV = ((PullToRefreshGridView) findViewById(R.id.gv_purhchase_history));
        mGV.setEmptyView(mTVEmpty);
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_purhchase_history));
        mTitleBar.setTitle(mTitle);
    }

    private void setupView() {
        mGV.setOnItemClickListener(this);
        mGV.setOnRefreshListener(this);
        mTitleBar.setLeftLayoutClickListener(this);
        mTVGetDataAgain.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,""+position,Toast.LENGTH_SHORT).show();

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
       initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }
}
