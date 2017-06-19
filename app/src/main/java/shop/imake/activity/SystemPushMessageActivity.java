package shop.imake.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.adapter.SystemPushMessageAdapter;
import shop.imake.client.Api4Home;
import shop.imake.client.ClientApiHelper;
import shop.imake.fragment.HomePage;
import shop.imake.model.PushMessage;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.SPUtils;
import shop.imake.widget.IUUTitleBar;

/**
 * 系统消息列表类
 * author Alice
 * created at 2017/4/14 14:19
 */
public class SystemPushMessageActivity extends BaseActivity implements View.OnClickListener {

    //消息列表
    private PullToRefreshListView mRclView;
    //标题
    private IUUTitleBar mTitle;
    //是否是刷新加载数据
    public boolean isRefresh=false;
    //是否有下一页
    private boolean isHasNextPage=false;

    private int mPage=2;
    //消息列表
    private List<PushMessage.DataBean> mPushMessageData;
    //适配器
    private SystemPushMessageAdapter mAdapter;

    private Api4Home mClientApi;
    //是否是第一次加载数据
    private boolean isFrist=true;

    private Handler mHandler =new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mRclView.onRefreshComplete();
                    break;
                default:
                    return;
            }

        }
    };
    private LocalBroadcastManager mLocalBroadcastManager;
    private IntentFilter intentFilter;
    private LocalJPushReceiver localReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_push_message);
        initReceiver();
        initView();
        initVariable();
        setupView();
        initCtl();
        initData();
    }

    /**
     * 读完消息后刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (!isFrist){
            //下拉刷新
            isRefresh = true;
            onRecycleViewRefresh();
        }
    }

    /**
     * 初始化变量
     */
    private void initVariable() {
        mClientApi= (Api4Home) ClientApiHelper.getInstance().getClientApi(Api4Home.class);
        mPushMessageData=new ArrayList<>();
        mAdapter=new SystemPushMessageAdapter(mPushMessageData,getApplicationContext(),SystemPushMessageActivity.this,mClientApi);
        if (!CurrentUserManager.isLoginUser()){
            mAdapter.closeAllItems();
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mTitle = ((IUUTitleBar) findViewById(R.id.title_system_message));
        //列表设置
        mRclView = ((PullToRefreshListView) findViewById(R.id.ptrlv_push_message));

        mRclView.setMode(PullToRefreshBase.Mode.BOTH);

    }

    /**
     * 设置监听事件
     */

    private void setupView() {
        mTitle.setLeftLayoutClickListener(this);
        mRclView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                isRefresh = true;
                onRecycleViewRefresh();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载
                isRefresh = false;
                //加载
                if (isHasNextPage) {
                    initLoadMore();
                } else {
//                    Toast.makeText(getApplicationContext(), "已经加载到最后一页了", Toast.LENGTH_SHORT).show();
                    Message message=new Message();
                    message.what=0;
                    mHandler.sendMessageDelayed(message,500);
//                    mRclView.onRefreshComplete();
                }
            }
        });
    }

    /**
     * 初始化数据
     */

    private void initData() {
        String datas1 = (String) SPUtils.get(getApplicationContext(), SPUtils.PUSH_MESSAGE_KEY_ONE, "");
        setData(datas1);
        String datas2 = (String) SPUtils.get(getApplicationContext(), SPUtils.PUSH_MESSAGE_KEY_TWO, "");
        setData(datas2);
        isFrist=false;
    }

    /**
     * 处理数据
     * @param datas
     */
    private void setData(String datas) {
        if (!TextUtils.isEmpty(datas)) {
            PushMessage pushMessage = new Gson().fromJson(datas, PushMessage.class);
            //获得数据
            isHasNextPage=TextUtils.isEmpty(pushMessage.getNext_page_url())?false:true;
            List<PushMessage.DataBean> dataBeanList=pushMessage.getData();
            //刷新
            if (isRefresh){
                mAdapter.reSetData(dataBeanList);
                initLoadMore();
                isRefresh=false;
                //相当于 mAdapter.clear();
//                mPushMessageData.clear();
                //相当于mAdapter.addAll(dataBeanList);
//                mPushMessageData.addAll(dataBeanList);
                //加载更多
            }else {
                mAdapter.addData(dataBeanList);
                if (mRclView!=null&&mRclView.isRefreshing()){
                    mRclView.onRefreshComplete();
                }
            }

        }

    }

    /**
     * 填充数据
     */
    private void initCtl() {
        mRclView.setAdapter(mAdapter);
    }


    /**
     * 加载更多数据
     */
    private void initLoadMore() {
        mPage++;
        loadDate();
    }


    /**
     * 刷新列表数据
     */
    public void onRecycleViewRefresh() {
        mPage=1;
        loadDate();
    }

    /**
     *加载网络数据
     */
    private void loadDate() {
        mClientApi.getPushMessage(mPage, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
                if (mRclView!=null&&mRclView.isRefreshing()){
                    mRclView.onRefreshComplete();
                }

            }

            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response)){
                    String data=response.toString().trim();
                    setData(data);
                }else {
                    if (mRclView!=null&&mRclView.isRefreshing()){
                        mRclView.onRefreshComplete();
                    }
                }

            }
        });

    }


    /**
     * @param v
     */
    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;

            default:
                return;

        }
    }

    /**
     * 本地广播处理系统消息接收时更新数据
     */
    public static String MESSAGE_RECEIVER_ACTION="shop.imake.intent.ACTION_JPUSH_MESSAGE_RECEIVE_SYSTEM";
    private void initReceiver() {
        //本地广播接收器
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        //新建intentFilter并给其action标签赋值。
        intentFilter = new IntentFilter();
        intentFilter.addAction(HomePage.MESSAGE_RECEIVER_ACTION);
        //创建广播接收器实例，并注册。将其接收器与action标签进行绑定。
        localReceiver = new LocalJPushReceiver();
        mLocalBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    class LocalJPushReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            isRefresh=true;
            onRecycleViewRefresh();
        }
    }
}
