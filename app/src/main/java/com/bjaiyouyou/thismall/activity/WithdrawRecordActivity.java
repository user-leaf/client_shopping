package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.WithdrawRecordAdapter;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.WithdrawReCordModel;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 提现记录页面
 * @author QuXinhang
 *Creare 2016/11/1 16:55
 *
 *
 */
public class WithdrawRecordActivity extends BaseActivity implements View.OnClickListener ,OnRefreshListener2{

    private IUUTitleBar mTitle;
    private PullToRefreshListView mLv;
    private WithdrawRecordAdapter mAdapter;
    //网络加载对象
    private WithdrawReCordModel mWithdrawReCordModel;
    //网络加载数据
    private List<WithdrawReCordModel.DataBean> mWithdrawRecords;
    //当前加载页数
    private int mPage=1;
    //是否存在下一页
    private boolean isHaveNextPage=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_record);
        mWithdrawRecords=new ArrayList<>();
        mAdapter=new WithdrawRecordAdapter(mWithdrawRecords,getApplicationContext());
        initData();
        initView();
        setUpView();
        initCtrl();
    }
    private void initView() {
        mTitle = ((IUUTitleBar) findViewById(R.id.title_withdraw_record));
        mLv = ((PullToRefreshListView) findViewById(R.id.lv_withdraw_record));
    }

    private void setUpView() {
        mTitle.setLeftLayoutClickListener(this);
        mLv.setOnRefreshListener(this);
        mLv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    private void initData() {
        ClientAPI.getWithdrawRecord( mPage,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mLv.onRefreshComplete();
                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
            }

            @Override
            public void onResponse(String response, int id) {
                mLv.onRefreshComplete();
                if (!TextUtils.isEmpty(response)){
                    mWithdrawReCordModel=new Gson().fromJson(response,WithdrawReCordModel.class);
                     if (mWithdrawReCordModel!=null){
                            List<WithdrawReCordModel.DataBean>  data=mWithdrawReCordModel.getData();
                        if (mWithdrawReCordModel.getNext_page_url()!=null){
                            isHaveNextPage=true;
                        }else {
                            isHaveNextPage=false;
                        }
                         if (data!=null){
//                             mWithdrawRecords.addAll(data);
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
        }
        ToastUtils.showLong("已经是最后一页，没有更多了");

    }
}
