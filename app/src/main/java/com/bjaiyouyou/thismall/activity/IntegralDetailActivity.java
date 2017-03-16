package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.IntegralDetailAdapter;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4ClientOther;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.IntegralDetailModel;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 积分详情页面
 * @author QuXinhang
 *Creare 2016/11/29 18:34
 */
public class IntegralDetailActivity extends BaseActivity implements View.OnClickListener ,PullToRefreshBase.OnRefreshListener2 {


    private IUUTitleBar mTitle;
    private PullToRefreshListView mLv;
    private IntegralDetailAdapter mAdapter;
    //网络加载对象
    private IntegralDetailModel mIntegralDetailModel;
    //网络加载数据
    private List<IntegralDetailModel.DataBean> mIntegralDetails;
    //当前加载页数
    private int mPage=1;
    //是否存在下一页
    private boolean isHaveNextPage=false;

    private Api4ClientOther mClient;
    public static final String TAG=IntegralDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integer_detail);
        mIntegralDetails=new ArrayList<>();
        mAdapter=new IntegralDetailAdapter(mIntegralDetails,getApplicationContext());
        initData();
        initView();
        setUpView();
        initCtrl();
    }
    private void initView() {
        mTitle = ((IUUTitleBar) findViewById(R.id.title_integral_detail));
        mLv = ((PullToRefreshListView) findViewById(R.id.lv_integer_detail));
    }

    private void setUpView() {
        mTitle.setLeftLayoutClickListener(this);
        mLv.setOnRefreshListener(this);
        mLv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    private void initData() {
        mClient= (Api4ClientOther) ClientApiHelper.getInstance().getClientApi(Api4ClientOther.class);
        mClient.getIntegralDetail(TAG, mPage, new DataCallback<IntegralDetailModel>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                mLv.onRefreshComplete();
                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
            }

            @Override
            public void onSuccess(Object response, int id) {
                mLv.onRefreshComplete();
                if (response!=null){
                    mIntegralDetailModel= (IntegralDetailModel) response;
                    if (mIntegralDetailModel!=null){
                        List<IntegralDetailModel.DataBean>  data=mIntegralDetailModel.getData();
                        if (mIntegralDetailModel.getNext_page_url()!=null){
                            isHaveNextPage=true;
                        }else {
                            isHaveNextPage=false;
                        }
                        if (data!=null){
                            mAdapter.addAll(data);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void initCtrl() {
        mLv.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_layout:
                finish();
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        mAdapter.clear();
        mPage=1;
        initData();
        mLv.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (isHaveNextPage){
            ++mPage;
            initData();
        }else {
            mLv.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mLv.onRefreshComplete();
                }
            }, 1000);
            ToastUtils.showLong("已经是最后一页，没有更多了");

        }


    }
}
