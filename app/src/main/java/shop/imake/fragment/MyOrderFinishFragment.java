package shop.imake.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.activity.LoginActivity;
import shop.imake.activity.OrderDetailActivity;
import shop.imake.adapter.MyOrderRecycleViewAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4ClientOther;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.MyOrder;
import shop.imake.utils.LogUtils;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.SpaceItemDecoration;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UNNetWorkUtils;

/**
 * @author Alice
 *         Creare 2016/6/12 14:32
 *         完成订单页面
 */
public class MyOrderFinishFragment extends BaseFragment {
    private static final String TAG = MyOrderFinishFragment.class.getSimpleName();
    //布局
    private View layout;
    //数据为空的显示
    private ImageView ivEmpty;
    //数据源
    private MyOrder myOrder;
    private List<MyOrder.DataBean> data;
    //适配器
    private MyOrderRecycleViewAdapter adapter;
    //列表
    private XRecyclerView lv;
    //是否还有下一页
    private boolean isHaveNextNews;
    //Orderstate
    private int mOrderstate = 1;
    //数据空白显示
    private ImageView mTvDataEmpty;
    //未登录布局
    private RelativeLayout mLLNotLogin;
    //登录入口
    private TextView mTVGoToLogin;
    //网络未连接布局
    private LinearLayout mLLUnNetWork;
    //网络检查入口
    private TextView mTvGetDataAgain;
    //判断列表是否已经滑到底部
    private boolean isBottom = false;

    //现在加载页
    private int mPage = 1;
    //最后一页
    private int mLastPage = 1;
    //是否向下滑
    private boolean isDown = false;
    private int mLastY = 0;
    private int mFirstItem = -1;
    private boolean isScroll = false;
    //判断列表是否已经滑顶
    private boolean isTop = false;

    private int mMoveLastY;
    private TextView mTVNotLoginTitle;

    private boolean isPullDown = false;

    private Api4ClientOther mClientApi;

    //ListView控件的高度
//    private int mLvHeight;
    //用于Adapter更新页面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initData();
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mLvHeight=getArguments().getInt("mHeight");
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        isPullDown = true;
        mPage = 1;
//        adapter.clear();
//        adapter.notifyDataSetChanged();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_myorder, container, false);
        initView();
        setUpView();
        controlView();
        return layout;
    }


    private void initView() {
        lv = ((XRecyclerView) layout.findViewById(R.id.lv_fragment_myOrder));
        mTvDataEmpty = ((ImageView) layout.findViewById(R.id.iv_data_loading));
        lv.setEmptyView(mTvDataEmpty);
//        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,mLvHeight-100);
//        lv.setLayoutParams(params);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lv.setLayoutManager(manager);
        lv.addItemDecoration(new SpaceItemDecoration(0, 0, 5, 5));


        mLLNotLogin = ((RelativeLayout) layout.findViewById(R.id.ll_not_login));
        mTVGoToLogin = ((TextView) layout.findViewById(R.id.tv_goto_login));
        mTVNotLoginTitle = (TextView) layout.findViewById(R.id.tv_not_login_title);
        mTVNotLoginTitle.setText("无法获取订单信息");

        mLLUnNetWork = ((LinearLayout) layout.findViewById(R.id.ll_unnetwork));
        mTvGetDataAgain = ((TextView) layout.findViewById(R.id.tv_get_data_again));
        //没有订单
        ivEmpty = ((ImageView) layout.findViewById(R.id.tv_myOrderEmpty));
    }

    private void setUpView() {
        mTvGetDataAgain.setOnClickListener(this);
        mTVGoToLogin.setOnClickListener(this);
        lv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }

            @Override
            public void onLoadMore() {


                isPullDown = false;
                if (isHaveNextNews) {
                    ++mPage;
                    initData();
                } else {

                    lv.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            lv.loadMoreComplete();
                            lv.refreshComplete();
                        }
                    }, 1000);
//                    Toast.makeText(getContext(), "已经加载到最后一页", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void controlView() {
        data = new ArrayList<>();
        adapter = new MyOrderRecycleViewAdapter(getActivity(), this, data, getActivity(), mHandler);
        adapter.setOnItemClickListener(new MyOrderRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
//                String orderNumber = adapter.getItem(position-1).getOrder_number();
                String orderNumber = data.get(position - 1).getOrder_number();
                // 跳转到订单详情页，传递页面类型和订单编号
                OrderDetailActivity.actionStart(getActivity(), orderNumber);

            }
        });
        lv.setAdapter(adapter);
    }

    /**
     * 加载网络数据
     */
    private void initData() {
        mClientApi = (Api4ClientOther) ClientApiHelper.getInstance().getClientApi(Api4ClientOther.class);

        mClientApi.getMyOrderData(TAG, mOrderstate, mPage, new DataCallback<MyOrder>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                //判断时候是网络不可用
                if (NetStateUtils.isNetworkAvailable(getContext())) {
                    String eString = e.toString();
                    //登录过期
//                    if ("400".equals(eString)||"401".equals(eString)){
                    if (eString.contains("400") || eString.contains("401")) {
                        //2、当网络可用未登录
                        lvHide();
                    } else {
                        UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                    }
                } else {
                    //1、当网络不可用的时候、显示网络不可用
                    unNetWorkView();
                    ToastUtils.showShort("当前网络不可用，请检查网络设置");
                }
                lv.loadMoreComplete();
                lv.refreshComplete();

            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response != null) {
                    myOrder = (MyOrder) response;
                    mLastPage = myOrder.getLast_page();
                    isHaveNextNews = (myOrder.getNext_page_url() != null);
                    lvShow();
                    List<MyOrder.DataBean> listAdd = myOrder.getData();
                    if (isPullDown) {
                        adapter.clear();
                    }
                    reSetDataNew(listAdd);
                }
                lv.loadMoreComplete();
                lv.refreshComplete();
            }

        });

    }


    /**
     * 重新数据
     *
     * @param list
     */

    private void reSetDataNew(List<MyOrder.DataBean> list) {
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
        int count = adapter.getItemCount();
        if (count == 0) {
            dataEmpty();
        }

    }

    /**
     * 跳转页面
     *
     * @param cls      要填转到的页面
     * @param isFinish 是否关闭本页
     */
    public void jump(Class<?> cls, boolean isFinish) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
    }

    private void lvShow() {
        ivEmpty.setVisibility(View.GONE);
        mLLNotLogin.setVisibility(View.GONE);
        mTvDataEmpty.setVisibility(View.VISIBLE);
        lv.setVisibility(View.VISIBLE);
        mLLUnNetWork.setVisibility(View.GONE);
    }

    private void lvHide() {
        ivEmpty.setVisibility(View.GONE);
        mLLNotLogin.setVisibility(View.VISIBLE);
        mTvDataEmpty.setVisibility(View.GONE);
        lv.setVisibility(View.GONE);
        mLLUnNetWork.setVisibility(View.GONE);
    }

    private void unNetWorkView() {
        ivEmpty.setVisibility(View.GONE);
        mLLNotLogin.setVisibility(View.GONE);
        mTvDataEmpty.setVisibility(View.GONE);
        lv.setVisibility(View.GONE);
        mLLUnNetWork.setVisibility(View.VISIBLE);
    }

    private void dataEmpty() {
        mLLNotLogin.setVisibility(View.GONE);
        mTvDataEmpty.setVisibility(View.GONE);
        lv.setVisibility(View.GONE);
        mLLUnNetWork.setVisibility(View.GONE);
        ivEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_goto_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.tv_get_data_again:
                LogUtils.e("重新加载", "重新加载");
                refreshData();
                break;
        }
    }

    /**
     * @param hidden Fragment切换的时候调用这个方法
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshData();
        }
    }


}
