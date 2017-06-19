package shop.imake.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.adapter.MyCommissionDetailAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4Mine;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.MyCommissionModel;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DataHolder;
import shop.imake.utils.LogUtils;
import shop.imake.widget.IUUTitleBar;

/**
 * 众汇券详情页面
 * author Alice
 * created at 2017/5/12 11:17
 */

public class MyCommissionDetailActivity extends BaseActivity {
    public static String TAG = MyCommissionDetailActivity.class.getSimpleName();
    private IUUTitleBar mTitle;
    //列表
    private PullToRefreshListView mLv;
    //数据请求的页
    private int mPage;
    //是否是下拉刷新
    private boolean isRefresh;
    //是否是有下一页
    private boolean isHaveNextPage;
    //数据类型
    private List<MyCommissionModel.DataBean> mList;
    //列表适配器
    private MyCommissionDetailAdapter mAdapter;
    //吊起ActivityName
    private String mClassName;
    private Api4Mine mApi4Mine;
    //网络请求数据
    private MyCommissionModel mProperty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_detail);
        initView();
        initVariable();
        setupView();
        initCtrl();
        initData();
    }

    private void initView() {
        mLv = ((PullToRefreshListView) findViewById(R.id.ptrlv_zh_detail));
        mLv.setMode(PullToRefreshBase.Mode.BOTH);

        mClassName = DataHolder.getInstance().getData();
        mTitle = ((IUUTitleBar) findViewById(R.id.title_zh_detail));
        //设置标题
        if (mClassName != null) {
            if (MyZhongHuiQuanActivity.TAG.equals(mClassName)) {
                mTitle.setTitle("众汇券明细");
            } else if (MyCommissionActivity.TAG.equals(mClassName)) {
                mTitle.setTitle("佣金明细");
            } else if (MyIncomeActivity.TAG.equals(mClassName)) {
                mTitle.setTitle("收益明细");
            }
        }
    }

    private void setupView() {
        mTitle.setLeftLayoutClickListener(this);

        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                mPage = 1;
                isRefresh = true;
                initData();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefresh = false;
                //上拉加载
                if (isHaveNextPage) {
                    mPage++;
                    initData();
                }
            }
        });
    }

    private void initVariable() {
        mApi4Mine = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
    }

    private void initData() {
        mApi4Mine.getMyPropertyData(this, mClassName, mPage, new DataCallback<MyCommissionModel>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
                LogUtils.d("getMyPropertyData", e.getMessage());
                if (isRefresh) {
                    isRefresh = false;
                }
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response != null) {
                    mProperty = (MyCommissionModel) response;
                }
                //判断是否有下一页
                //判断是否有下一页
                String nextUrl = mProperty.getNext_page_url();
                if (!TextUtils.isEmpty(nextUrl)) {
                    isHaveNextPage = true;
                } else {
                    isHaveNextPage = false;
                }
                //判断是不是下拉刷新，进行不同的操作
                //isRefresh=false;
                List<MyCommissionModel.DataBean> dataBeen = mProperty.getData();
                if (dataBeen != null) {
                    if (isRefresh) {
                        mAdapter.clear();
                        mAdapter.addAll(dataBeen);
                        isRefresh = false;
                    } else {
                        mAdapter.addAll(dataBeen);
                    }
                }
            }
        });
    }

    private void initCtrl() {
        mList = new ArrayList<>();
        mAdapter = new MyCommissionDetailAdapter(mList, getApplicationContext());
        mLv.setAdapter(mAdapter);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            //退出
            case R.id.left_layout:
                finish();
                break;

            default:
                return;

        }
    }
}
