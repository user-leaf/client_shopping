package shop.imake.activity;

import android.os.Bundle;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.adapter.UuDetailAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4ClientOther;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.UuDetailModel;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.widget.IUUTitleBar;

/**
 * UU详情页面
 * @author Alice
 *Creare 2016/11/29 18:34
 */
public class UUDetailActivity extends BaseActivity implements View.OnClickListener ,PullToRefreshBase.OnRefreshListener2{
    private IUUTitleBar mTitle;
    private PullToRefreshListView mLv;
    private UuDetailAdapter mAdapter;
    //网络加载对象
    private UuDetailModel mUuDetailModel;
    //网络加载数据
    private List<UuDetailModel.DataBean> mUuDetails;
    //当前加载页数
    private int mPage=1;
    //是否存在下一页
    private boolean isHaveNextPage=false;

    private Api4ClientOther mClient;
    public static final String TAG=UUDetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uudetail);
        mUuDetails=new ArrayList<>();
        mAdapter=new UuDetailAdapter(mUuDetails,getApplicationContext());
        initData();
        initView();
        setUpView();
        initCtrl();
    }
    private void initView() {
        mTitle = ((IUUTitleBar) findViewById(R.id.title_uu_detail));
        mLv = ((PullToRefreshListView) findViewById(R.id.lv_uu__detail));
    }

    private void setUpView() {
        mTitle.setLeftLayoutClickListener(this);
        mLv.setOnRefreshListener(this);
        mLv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
    }

    private void initData() {
        mClient= (Api4ClientOther) ClientApiHelper.getInstance().getClientApi(Api4ClientOther.class);
        mClient.getUuDetail(TAG, mPage, new DataCallback<UuDetailModel>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
                mLv.onRefreshComplete();
                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
            }

            @Override
            public void onSuccess(Object response, int id) {
                mLv.onRefreshComplete();
                if (response!=null){
                    mUuDetailModel= (UuDetailModel) response;
                    if (mUuDetailModel!=null){
                        List<UuDetailModel.DataBean>  data=mUuDetailModel.getData();
                        if (mUuDetailModel.getNext_page_url()!=null){
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
