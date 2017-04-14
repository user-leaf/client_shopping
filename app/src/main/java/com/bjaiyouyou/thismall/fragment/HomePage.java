package com.bjaiyouyou.thismall.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.GoodsDetailsActivity;
import com.bjaiyouyou.thismall.activity.GoodsListShowActivity;
import com.bjaiyouyou.thismall.activity.HistoryBuyNewActivity;
import com.bjaiyouyou.thismall.activity.SearchGoodsActivity;
import com.bjaiyouyou.thismall.activity.WebShowActivity;
import com.bjaiyouyou.thismall.adapter.HomeEveryDayEmptyAdapter;
import com.bjaiyouyou.thismall.adapter.HomeGoodGridNewAdapter;
import com.bjaiyouyou.thismall.adapter.HomeNavigationNewAdapter;
import com.bjaiyouyou.thismall.adapter.HomeNavigationNewEmptyAdapter;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4Home;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.client.HttpUrls;
import com.bjaiyouyou.thismall.model.HomeAdBigModel;
import com.bjaiyouyou.thismall.model.HomeAdModel;
import com.bjaiyouyou.thismall.model.HomeNavigationItem;
import com.bjaiyouyou.thismall.model.HomeNavigationItemNew;
import com.bjaiyouyou.thismall.model.HomeNavigationItemNewEmpty;
import com.bjaiyouyou.thismall.model.HomeProductModel;
import com.bjaiyouyou.thismall.model.IsHaveMessageNotRead;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.RefreshUtils;
import com.bjaiyouyou.thismall.utils.SPUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.zxing.activity.CaptureActivity;
import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * @author QuXinhang
 *         Update 2016/7/3 17:44
 *         添加抢购区域
 *         <p>
 *         填充每日上新数据
 */

public class HomePage extends BaseFragment implements View.OnClickListener, OnItemClickListener {

    public static final String TAG = HomePage.class.getSimpleName();

    private long mExitTime;

    // 搜索栏
    private EditText mEtSearch;

    private ImageView ivScanCode;
    // 系统推送消息入口
    private View mLLPushMessage;
    // 广告位容器
    private RelativeLayout mAdContainer;
    // 广告栏控件
    private ConvenientBanner mConvenientBanner;
    // 每日上新物品列表
    private XRecyclerView mGvShow;

    private XRecyclerView mEveryDayEmpty;

    // 今日新品物品数据集
    private List<HomeProductModel.DataBean> mProductInfoList;
    //每日上新的分页控制
    private int mEveryDayPage = 1;
    //控制是否可分页
    private boolean isHaveNextNews = false;
    //每日上新的类
    private HomeProductModel mHomeProductModel;

    // 物品列表适配器
    private HomeGoodGridNewAdapter mHomeGoodGridAdapter;
    //限时抢购
    //    private ArrayList<Object> mIVList;
    //导航按钮数量
    private int mNavigationNum = 4;
    //导航按键容器
    private LinearLayout mLLNavigationPanicBuying;
    //限时抢购的适配器

    private HomeNavigationNewAdapter mNavigationAdapterNew;
    //过时角标
    private int mOldIndex = 0;
    //现在的角标
    private int mCurrentIndex = 0;
    //限时抢购列表
    private GridView mGVPanicBuying;
    //侧滑更多
    private SwipeLayout sample1;
    //更多按钮
    private ImageView mIVPanicBuyMore;
    //限时抢购的导航按钮
    private LinearLayout mLLPanicBuy1;
    private LinearLayout mLLPanicBuy2;
    private LinearLayout mLLPanicBuy3;
    private LinearLayout mLLPanicBuy4;
    //抢购导航按钮集合
    private List<LinearLayout> mLLPanicBuys;

    //抢购的列表数据
    private List<HomeNavigationItem.DataBean> mNavigationData;
    //抢购网络获取数据
    private List<HomeNavigationItemNew.RushToPurchaseTimeFrameBean> mTimeFrameBeans;
    private List<HomeNavigationItemNew.RushToPurchaseTimeFrameBean.DetailBean> mNavigationDataNew;
    private TextView mTVPanicBuyNull;

    private List<HomeAdModel> mNetImages;
    //抢购区域空白显示页
    private GridView mGVPanicBuyingEmpty;
    private LinearLayout mLLUNNetWork;
    //每日上线为空的适配器
    private SimpleAdapter emptyAdapter;

    private HomeNavigationNewEmptyAdapter emptyAdapterNavigation;
    //抢购页的角标
    private int mIndexs[] = {1, 2, 3, 4};
    //获取系统当前小时
    private int mHour;
    private int mMinuts;
    private int mSecond;
    //计时的时间差
    private long mTimeDifference = 0L;
    //初始化时间节点
    private int mTimes[] = {9, 12, 15, 18};
    private int mTimeLast = 21;
    //抢购导航状态
    private TextView mTVPanicBuyState1;
    private TextView mTVPanicBuyState2;
    private TextView mTVPanicBuyState3;
    private TextView mTVPanicBuyState4;
    private TextView mTimeStateTVs[];
    //抢购导航时间
    private TextView mTVPanicBuyTime1;
    private TextView mTVPanicBuyTime2;
    private TextView mTVPanicBuyTime3;
    private TextView mTVPanicBuyTime4;
    private TextView mTimeTVs[];

    //计时启动标识
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    initTime();
            }
        }
    };

    //计时器
    private Timer mTimer;
    //异步方法
    private TimerTask mTimeTask;

    //第一次进入页面
    private boolean mFirstComeIn = true;
    //屏幕宽
    private int width2;

    //抢购的时间节点下标
    private int timeIndex = 0;
    //抢购不存在显示
    private LinearLayout mLLPanicBuyEmpty;
    //抢购布局
    private HorizontalScrollView mSCRlPanicBuyEmpty;
    //每日上新的头部
    private View headView;
    private View footView;
    private TextView mTVEveryDayTitle;
    //每日上新为空的布局适配器
    private HomeEveryDayEmptyAdapter everyDayEmptyAdapter;
    //是否是刷新加载数据
    private boolean isRefresh;
    private Api4Home mClientApi;
    //系统消息入口图标
    private ImageView mIvPushMessage;
    //历史购买入口
    private View mTvPurchaseHistory;
    //扫描页面入口
    private View mLLScan;
    //显示是否存在未读系统信息，存在显示，不存在隐藏
    private TextView mTvMessageNotRead;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_home, container, false);
        mTimeFrameBeans = new ArrayList<>();
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initVariables();
        initView();
        setUpView();
        //处理每日上新+广告
        initData();
        initCtrl();

        //处理抢购状态
//        //创建计时器
//        initTimer();
//        initTime();
        //处理抢购区域
//        initPanicBuying();
//        initPanicBuyingEmpty();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            initTime();
            //处理抢购区域
//        initPanicBuying();
//            initPanicBuyingEmpty();
            if (timeIndex == 4 || timeIndex == -1) {
//                changeNavigation(0);
            } else {
//                changeNavigation(timeIndex);
            }
        }
    }



    /**
     * 初始化变量
     */
    private void initVariables() {
        mClientApi = (Api4Home) ClientApiHelper.getInstance().getClientApi(Api4Home.class);
        //商品列表
        mProductInfoList = new ArrayList<>();
        // 广告数据集
        mNetImages = new ArrayList<>();
    }

    private void initCtrl() {
        mHomeGoodGridAdapter = new HomeGoodGridNewAdapter(getActivity(), mProductInfoList);
        mHomeGoodGridAdapter.setOnItemClickListener(new HomeGoodGridNewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //获取商品的ID
                position = position - 2;
                LogUtils.e("onItemClick", "" + position);
                long productID = mProductInfoList.get(position).getId();
//                Toast.makeText(getActivity(),""+productID,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
                intent.putExtra("productID", productID);
                startActivity(intent);
            }
        });
        mGvShow.setAdapter(mHomeGoodGridAdapter);

        everyDayEmptyAdapter = new HomeEveryDayEmptyAdapter(getActivity());
        mEveryDayEmpty.setAdapter(everyDayEmptyAdapter);
    }



    /**
     * 初始化控件
     */
    private void initView() {
        mLLPushMessage = (layout.findViewById(R.id.ll_home_push_message));
        mTvMessageNotRead = ((TextView) layout.findViewById(R.id.tv_home_message_not_read));


        //历史购买入口
//        mTvPurchaseHistory = layout.findViewById(R.id.home_tv_purchase_history);
        //获取屏幕的宽度
        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        width2 = outMetrics.widthPixels;

        SPUtils.put(getContext(), "mFirstComeIn", !mFirstComeIn);
        int size = 8;
        Map<String, Object> empty;
        List<Map<String, Object>> emptys = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            empty = new HashMap<>();
            empty.put("img", R.mipmap.list_image_loading);
            empty.put("price", "￥0.00\n+0积分");
            emptys.add(empty);
        }
        emptyAdapter = new SimpleAdapter(getActivity(), emptys, R.layout.item_home_navigatiom_gv_empty, new String[]{"img", "price"}, new int[]{R.id.iv_home_navigation_item_empty, R.id.tv_home_navigation_item_empty});

        //无网提示
        mLLUNNetWork = ((LinearLayout) layout.findViewById(R.id.ll_home_un_network));
        //最外层的ScrollView

        mEtSearch = ((EditText) layout.findViewById(R.id.home_et_search));
        mLLScan = ( layout.findViewById(R.id.ll_home_scan));
//        ivScanCode = ((ImageView) layout.findViewById(R.id.home_iv_scan));
//        mLLPushMessage = layout.findViewById(R.id.ll_home_push_message);

        initHeadView();

        mGvShow = (XRecyclerView) layout.findViewById(R.id.home_gv_show);
        GridLayoutManager mgr = new GridLayoutManager(getContext(), 2);
        mgr.setOrientation(LinearLayoutManager.VERTICAL);
        mGvShow.setLayoutManager(mgr);
//       RecyclerView.LayoutManager mgr1=new GridLayoutManager(getContext(),2);
//        mGvShow.setLayoutManager(mgr1);
        //设置Item间距
//        mGvShow.addItemDecoration(new SpaceItemDecoration(0,5,5,0));
        //添加头部
        mGvShow.addHeaderView(headView);
//        mGvShow.addFootView(footView);

        mEveryDayEmpty = ((XRecyclerView) layout.findViewById(R.id.rv_everyday_empty));
        GridLayoutManager mgr2 = new GridLayoutManager(getContext(), 2);
        mgr2.setOrientation(LinearLayoutManager.VERTICAL);
        mGvShow.setLayoutManager(mgr2);
        mEveryDayEmpty.setLayoutManager(mgr);
//        mEveryDayEmpty.addItemDecoration(new SpaceItemDecoration(0,5,5,0));
//        mEveryDayEmpty.addHeaderView(headView);
//        mGvShow.setEmptyView(mEveryDayEmpty);
    }

    /**
     * 初始化头部
     */
    private void initHeadView() {
        headView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_head, null, false);
        footView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_foot, null, false);
//        mTvfoot = ((TextView) footView.findViewById(R.id.tv_fragment_home_food));

        mAdContainer = (RelativeLayout) headView.findViewById(R.id.home_rl_ad);
        mAdContainer.getLayoutParams().height = ScreenUtils.getScreenWidth(getActivity()) / 3;
//        //解决首页不从顶端显示
//        mAdContainer.setFocusable(true);
//        mAdContainer.setFocusableInTouchMode(true);
//        mAdContainer.requestFocus();

        // 抢购区域
        mLLNavigationPanicBuying = ((LinearLayout) headView.findViewById(R.id.ll_home_navigation_panicbuying));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width2, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLLNavigationPanicBuying.setLayoutParams(params);

        mGVPanicBuying = ((GridView) headView.findViewById(R.id.gv_home_panicbuying));
        // 解决页面不从顶部开始
//        mGVPanicBuying.setFocusable(false);
//        mLLNavigationPanicBuying.setFocusable(false);
        mGVPanicBuyingEmpty = ((GridView) headView.findViewById(R.id.gv_home_panicbuying_empty));
        mIVPanicBuyMore = ((ImageView) headView.findViewById(R.id.iv_home_navigation));
        //抢购导航区
        mLLPanicBuy1 = ((LinearLayout) headView.findViewById(R.id.ll_home_navigation_panicbuying1));
        mLLPanicBuy2 = ((LinearLayout) headView.findViewById(R.id.ll_home_navigation_panicbuying2));
        mLLPanicBuy3 = ((LinearLayout) headView.findViewById(R.id.ll_home_navigation_panicbuying3));
        mLLPanicBuy4 = ((LinearLayout) headView.findViewById(R.id.ll_home_navigation_panicbuying4));
        //设置按钮标签
        mLLPanicBuy1.setTag(0);
        mLLPanicBuy2.setTag(1);
        mLLPanicBuy3.setTag(2);
        mLLPanicBuy3.setTag(3);
        //抢购默认选中
//        mLLPanicBuy1.setEnabled(false);

        mLLPanicBuys = new ArrayList<>();
        mLLPanicBuys.add(mLLPanicBuy1);
        mLLPanicBuys.add(mLLPanicBuy2);
        mLLPanicBuys.add(mLLPanicBuy3);
        mLLPanicBuys.add(mLLPanicBuy4);
        //抢购的状态
        mTVPanicBuyState1 = ((TextView) headView.findViewById(R.id.tv_home_navigation_panicbuying_state1));
        mTVPanicBuyState2 = ((TextView) headView.findViewById(R.id.tv_home_navigation_panicbuying_state2));
        mTVPanicBuyState3 = ((TextView) headView.findViewById(R.id.tv_home_navigation_panicbuying_state3));
        mTVPanicBuyState4 = ((TextView) headView.findViewById(R.id.tv_home_navigation_panicbuying_state4));
        mTimeStateTVs = new TextView[]{mTVPanicBuyState1, mTVPanicBuyState2, mTVPanicBuyState3, mTVPanicBuyState4};

        mTVPanicBuyTime1 = ((TextView) headView.findViewById(R.id.tv_home_navigation_panicbuying_time1));
        mTVPanicBuyTime2 = ((TextView) headView.findViewById(R.id.tv_home_navigation_panicbuying_time2));
        mTVPanicBuyTime3 = ((TextView) headView.findViewById(R.id.tv_home_navigation_panicbuying_time3));
        mTVPanicBuyTime4 = ((TextView) headView.findViewById(R.id.tv_home_navigation_panicbuying_time4));
        mTimeTVs = new TextView[]{mTVPanicBuyTime1, mTVPanicBuyTime2, mTVPanicBuyTime3, mTVPanicBuyTime4};
        for (int i = 0; i < mTimes.length; i++) {
            mTimeTVs[i].setText(mTimes[i] + ":00");
        }

        mLLPanicBuyEmpty = ((LinearLayout) headView.findViewById(R.id.ll_gv_home_panicbuying_empty));
        mSCRlPanicBuyEmpty = ((HorizontalScrollView) headView.findViewById(R.id.scrl_home_panicbuying));

        // 广告控件
        mConvenientBanner = new ConvenientBanner(getActivity());
        ViewGroup.LayoutParams adParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mConvenientBanner.setLayoutParams(adParams);

        //每日上新头部
        mTVEveryDayTitle = ((TextView) headView.findViewById(R.id.tv_home_everyday_title));
    }

    /**
     * 设置属性、监听等
     */
    private void setUpView() {
//        mTvPurchaseHistory.setOnClickListener(this);
        mEtSearch.setOnClickListener(this);
        mLLPushMessage.setOnClickListener(this);
//        ivScanCode.setOnClickListener(this);
        mLLScan.setOnClickListener(this);
        mGVPanicBuying.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取商品的ID
                long productID = mNavigationDataNew.get(position).getProduct_id();

//                Toast.makeText(getActivity(),""+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
                intent.putExtra("productID", productID);
                startActivity(intent);

            }
        });
        mIVPanicBuyMore.setOnClickListener(this);

        // 解决页面不从顶部开始
        mGvShow.setFocusable(false);
        //对每日新列表进行监听

        //抢购导航设置监听
        for (int i = 0; i < mLLPanicBuys.size(); i++) {
            mLLPanicBuys.get(i).setTag(i);
            mLLPanicBuys.get(i).setOnClickListener(this);
        }

//        mGvShow.setLoadingMoreProgressStyle(ProgressStyle.CubeTransition);
        RefreshUtils.setLoadMoreProgressStyleStyle(mGvShow);
        mGvShow.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (NetStateUtils.isNetworkAvailable(getContext())) {
                    //刷新抢购
//                   initPanicBuying();
//                    refreshPanicBuying();
                    //刷新每日上新
                    mEveryDayPage = 1;
                    isHaveNextNews = true;
                    isRefresh = true;
                    initData();
//                    initTime();

                } else {
                    mLLUNNetWork.setVisibility(View.VISIBLE);
                    mGvShow.refreshComplete();
                }
            }

            @Override
            public void onLoadMore() {

                if (isHaveNextNews) {
                    ++mEveryDayPage;
                    initData();
                } else {
                    mGvShow.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            showToast("最后一页");
                        }
                    }, 1000);
                    mGvShow.loadMoreComplete();
                }
            }
        });

    }



    /**
     * 每日上新数据
     */
    private void initData() {
        mClientApi.getRecommendData(TAG, mEveryDayPage, new DataCallback<HomeProductModel>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (!NetStateUtils.isNetworkAvailable(getContext())) {
                    mLLUNNetWork.setVisibility(View.VISIBLE);
                } else {
                    mLLUNNetWork.setVisibility(View.GONE);
                    UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                }
                dismissLoadingDialog();
                mGvShow.refreshComplete();
                mGvShow.loadMoreComplete();
            }

            @Override
            public void onSuccess(Object response, int id) {
                mGvShow.refreshComplete();
                mGvShow.loadMoreComplete();
                //有网隐藏提示
                mLLUNNetWork.setVisibility(View.GONE);
                //获得数据
                if (response!=null) {
                    //是刷新清空数据
                    if (isRefresh) {
                        mHomeGoodGridAdapter.clear();
                    }
                    //恢复不是刷新状态
                    isRefresh = false;
                    mHomeProductModel = (HomeProductModel) response;
                    isHaveNextNews = (mHomeProductModel.getNext_page_url() != null);
                    List<HomeProductModel.DataBean> listAdd = mHomeProductModel.getData();
                    reSetEveryDayNew(listAdd);
                }
                dismissLoadingDialog();
            }
        });


        // 请求广告数据
        mClientApi.getHomeAdData(TAG, new DataCallback<HomeAdBigModel>(getContext()) {
                    @Override
                    public void onFail(Call call, Exception e, int id) {
                        if (NetStateUtils.isNetworkAvailable(getContext())) {
                            UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                        }
                    }

                    @Override
                    public void onSuccess(Object response, int id) {
                        mNetImages.clear();

                        if (response != null) {
                            HomeAdBigModel model = (HomeAdBigModel) response;

                            if (mNetImages != null) {

                                mNetImages.addAll(model.getBanners());

                                int adNum = mNetImages.size();
                                if (adNum == 1) {
                                    mConvenientBanner.setPageIndicator(new int[]{android.R.color.transparent, android.R.color.transparent});
                                    mConvenientBanner.setCanLoop(false);

                                } else if (adNum > 1){
                                    mConvenientBanner.setPageIndicator(new int[]{R.mipmap.list_indicate_nor, R.mipmap.list_indicate_sel});
                                    mConvenientBanner.setCanLoop(true);
                                }

                                if (mAdContainer.getChildCount() <= 0) {
                                    setupAdView();
                                }

                            }
                        }

                        if (mNetImages.isEmpty()){ // mConvenientBanner貌似不支持数据减为空时的刷新
                            mAdContainer.removeView(mConvenientBanner);
                        }else {
                            mConvenientBanner.notifyDataSetChanged();
                        }
                    }
                }

        );

        /////加载信息判断是否存在未读系统消息
        mClientApi.isHaveMessageNotRead(new DataCallback<IsHaveMessageNotRead>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                //test
                mTvMessageNotRead.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response!=null){
                    IsHaveMessageNotRead isHaveMessageNotRead= (IsHaveMessageNotRead) response;
                    int message=isHaveMessageNotRead.getIsRead();
                    //不存在未读消息
                    if (message==0){
                        mTvMessageNotRead.setVisibility(View.INVISIBLE);
                        //存在未读消息
                    }else if(message==1){

                        mTvMessageNotRead.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    /**
     * 重新设置每日推荐数据
     *
     * @param productInfoList
     */

    private void reSetEveryDayNew(List<HomeProductModel.DataBean> productInfoList) {
        mHomeGoodGridAdapter.addAll(productInfoList);
        //test
//        mHomeGoodGridAdapter.clear();
//        mHomeGoodGridAdapter.notifyDataSetChanged();
    }

    /**
     * 添加控件点击事件处理
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_et_search: // 搜索框
                startActivity(new Intent(getActivity(), SearchGoodsActivity.class));
                break;
            case R.id.ll_home_scan:
                //跳转到拍照

                //跳转到扫码
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivity(intent);
                break;
//            case R.id.home_iv_scan:
//                //跳转到拍照
//
//                //跳转到扫码
//                Intent intent = new Intent(getActivity(), CaptureActivity.class);
//                startActivity(intent);
//                break;

            //抢购查看更多.
            case R.id.iv_home_navigation:
                Intent intentNavigation = new Intent(getActivity(), GoodsListShowActivity.class);
                //携带数据接口
                intentNavigation.putExtra("title", "限时抢购");
                //页数
                String moreUrl = ClientAPI.API_POINT + HttpUrls.PANICBUY_MORE + mIndexs[mCurrentIndex];
                LogUtils.e("moreUrl", moreUrl);
                intentNavigation.putExtra("url", moreUrl);
                startActivity(intentNavigation);
                break;

            //抢购导航按钮处理
            case R.id.ll_home_navigation_panicbuying1:
//                mLLPanicBuy1.setTag(0);
                changeNavigation(0);
                break;
            case R.id.ll_home_navigation_panicbuying2:
//                mLLPanicBuy2.setTag(1);
                changeNavigation(1);
                break;
            case R.id.ll_home_navigation_panicbuying3:
//                mLLPanicBuy3.setTag(2);
                changeNavigation(2);
                break;
            case R.id.ll_home_navigation_panicbuying4:
//                mLLPanicBuy4.setTag(3);
                changeNavigation(3);
                break;
//            case R.id.home_tv_purchase_history: // 历史购买入口
//                Intent phIntent = new Intent(getActivity(), HistoryBuyNewActivity.class);
//                //携带数据接口
//                phIntent.putExtra("title", "");
//                startActivity(phIntent);
//                break;
            case R.id.ll_home_push_message: // 系统消息入口
                Intent phIntent = new Intent(getActivity(), HistoryBuyNewActivity.class);
                //携带数据接口
                phIntent.putExtra("title", "");
                startActivity(phIntent);
                break;
            default:
                return;
        }


    }

    private void setupAdView() {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        mConvenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, mNetImages);
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.mipmap.list_indicate_nor, R.mipmap.list_indicate_sel})
//                //设置指示器的位置
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

        // 为广告轮播设置条目点击监听
        mConvenientBanner.setOnItemClickListener(this);

        // 把广告控件加入到广告栏容器中
        mAdContainer.addView(mConvenientBanner);

    }

    /**
     * 广告栏控件 ConvenientBanner 所需
     */
    public class LocalImageHolderView implements Holder<HomeAdModel> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, HomeAdModel homeAdModel) {
            StringBuilder sb = new StringBuilder();
            sb.append(homeAdModel.getImage_path())
                    .append(File.separator)
                    .append(homeAdModel.getImage_base_name());
            String imagePath = sb.toString();

            Glide.with(getActivity())
                    .load(ImageUtils.getThumb(imagePath, ScreenUtils.getScreenWidth(getContext()), 0))
                    .into(imageView);

        }

    }

    /**
     * 广告轮播条目点击监听
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        String link = mNetImages.get(position).getLink();
        // 广告link不为空则跳转，为空不跳转
        if (!TextUtils.isEmpty(link) && !"#".equals(link) && (link.startsWith("http://") || link.startsWith("https://"))) {
            //带值跳转到广告页面
            Intent intent = new Intent(getActivity(), WebShowActivity.class);
            intent.putExtra(WebShowActivity.PARAM_URLPATH, link + "?token=" + CurrentUserManager.getUserToken() + "&type=android");
            startActivity(intent);

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        //广告停止翻页
        mConvenientBanner.stopTurning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mTimer.cancel(); //退出计时器
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == getActivity().RESULT_OK && data != null) {
            if (data.getStringExtra("result")!=null){
                Toast.makeText(getContext(), "Home----" + data.getStringExtra("result").toString(), Toast.LENGTH_SHORT).show();
//            Log.e("QRCode",data.getData().toString());
                Log.e("QRCode", data.getStringExtra("result").toString());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //广告开始自动翻页
        mConvenientBanner.startTurning(5000);
//        initTime();

    }



    /**
     * 处理抢购区域
     */

    private void initPanicBuying() {
        mNavigationDataNew = new ArrayList<>();
        initNavigationDataNew(ClientAPI.API_POINT + HttpUrls.PANICBUY_NEW);
        mNavigationAdapterNew = new HomeNavigationNewAdapter(mNavigationDataNew, getActivity());
        mGVPanicBuying.setAdapter(mNavigationAdapterNew);
        initControlNavigation();
    }
    private void initPanicBuyingEmpty() {
        //计算宽度
        int size = 4;
        int length = width2 / 3;
        int gridviewWidth = (int) (size * (length + 4));
        int itemWidth = (int) (length);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        //抢购空白显示
        mGVPanicBuyingEmpty.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        mGVPanicBuyingEmpty.setColumnWidth(itemWidth); // 设置列表项宽
        mGVPanicBuyingEmpty.setHorizontalSpacing(5); // 设置列表项水平间距
        mGVPanicBuyingEmpty.setStretchMode(GridView.NO_STRETCH);

        mGVPanicBuyingEmpty.setNumColumns(size); // 设置列数量=列表集合数
        List<HomeNavigationItemNewEmpty> navigationItemNewEmpty = new ArrayList<>();
        emptyAdapterNavigation = new HomeNavigationNewEmptyAdapter(navigationItemNewEmpty, getContext());
        mGVPanicBuyingEmpty.setAdapter(emptyAdapterNavigation);
//        mGVPanicBuying.setEmptyView(mGVPanicBuyingEmpty);
        mGVPanicBuying.setEmptyView(mLLPanicBuyEmpty);
    }

    /**
     * 抢购数据，集合控制数据的数据获取
     */

    private void initNavigationDataNew(final String url) {
        LogUtils.e("抢购数据:", url);

        mClientApi.getNavigation(TAG, new DataCallback<HomeNavigationItemNew>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (NetStateUtils.isNetworkAvailable(getContext())) {
                    UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                }

            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response!=null) {
                    HomeNavigationItemNew homeNavigationItemNew= (HomeNavigationItemNew) response;
                    mTimeFrameBeans = homeNavigationItemNew.getRush_to_purchase_time_frame();
                    //是否显示更多按钮
                    mNavigationDataNew = mTimeFrameBeans.get(mCurrentIndex).getDetail();
                    checkGetMore();
                    //test
//                    mNavigationDataNew.clear();
                    LogUtils.e("mNavigationDataNew:", mNavigationDataNew.size() + "");
//                    moreChange(mNavigationDataNew);
                    mNavigationAdapterNew.clear();
//                    mNavigationAdapterNew.notifyDataSetChanged();
                    mNavigationAdapterNew.addAll(mNavigationDataNew);
//                    mNavigationAdapterNew.notifyDataSetChanged();
                    initControlNavigation();
                }

            }
        });

//        ClientAPI.getGoodsData(url, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                if (NetStateUtils.isNetworkAvailable(getContext())) {
//                    UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
//                }
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                if (!TextUtils.isEmpty(response.toString().trim())) {
//                    mTimeFrameBeans = new Gson().fromJson(response, HomeNavigationItemNew.class).getRush_to_purchase_time_frame();
//                    //是否显示更多按钮
//                    mNavigationDataNew = mTimeFrameBeans.get(mCurrentIndex).getDetail();
//                    checkGetMore();
//                    //test
////                    mNavigationDataNew.clear();
//                    LogUtils.e("mNavigationDataNew:", mNavigationDataNew.size() + "");
////                    moreChange(mNavigationDataNew);
//                    mNavigationAdapterNew.clear();
////                    mNavigationAdapterNew.notifyDataSetChanged();
//                    mNavigationAdapterNew.addAll(mNavigationDataNew);
////                    mNavigationAdapterNew.notifyDataSetChanged();
//                    initControlNavigation();
//                }
//            }
//        });
    }

    /**
     * 处理抢购数据
     */

    private void initControlNavigation() {
        //计算宽度
        int size = mNavigationDataNew.size();
        //检查是否需要显示查看更多按钮
        checkGetMore();
//        Log.e("size",""+size);
        int length = width2 / 3;
        int gridviewWidth = (int) (size * (length + 4));
        int itemWidth = (int) (length);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        //抢购列表显示
        mGVPanicBuying.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        mGVPanicBuying.setColumnWidth(itemWidth); // 设置列表项宽
        mGVPanicBuying.setHorizontalSpacing(5); // 设置列表项水平间距
        mGVPanicBuying.setStretchMode(GridView.NO_STRETCH);
        mGVPanicBuying.setNumColumns(size); // 设置列数量=列表集合数
        //创建适配器填充数据

    }


    /**
     * 是否显示更多按钮
     *
     * @param
     */

    private void moreChange(List<HomeNavigationItemNew.RushToPurchaseTimeFrameBean.DetailBean> data) {
        if (data == null) {
            mIVPanicBuyMore.setVisibility(View.GONE);
        } else {
            if (data.size() == 0) {
                mIVPanicBuyMore.setVisibility(View.GONE);
            } else {
                mIVPanicBuyMore.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 处理导航的切换
     *
     * @param position
     */
    private void changeNavigation(int position) {
//        initNavigationData(0);
        mOldIndex = mCurrentIndex;
        mLLNavigationPanicBuying.getChildAt(mOldIndex).setEnabled(true);
        mCurrentIndex = position;
        //改变数据
//        initNavigationData(mCurrentIndex);
        if (mTimeFrameBeans.size() == 4) {
            mNavigationDataNew = mTimeFrameBeans.get(mCurrentIndex).getDetail();
            //控制查看更多的按钮是否显示
            checkGetMore();
//            Toast.makeText(getActivity(),""+mTimeFrameBeans.get(mCurrentIndex).getId(),Toast.LENGTH_SHORT).show();
        }
        mNavigationAdapterNew.clear();
        mNavigationAdapterNew.addAll(mNavigationDataNew);
//        mNavigationAdapterNew.notifyDataSetChanged();
//        moreChange(mNavigationDataNew);
        initControlNavigation();
        mLLNavigationPanicBuying.getChildAt(mCurrentIndex).setEnabled(false);
    }

    /**
     * 检查是否需要显示查看抢购更多按钮
     */
    private void checkGetMore() {
        //test
//        mNavigationDataNew.clear();
        if (mNavigationDataNew.size() == 0) {//为空
            mIVPanicBuyMore.setVisibility(View.GONE);
        } else {
            if (mNavigationDataNew.size() < 3) {//非空小于3
                mIVPanicBuyMore.setVisibility(View.GONE);
            } else {
                mIVPanicBuyMore.setVisibility(View.VISIBLE);
            }
        }
    }
    /**
     * 刷新抢购页
     */
    private void refreshPanicBuying() {
        mNavigationDataNew = new ArrayList<>();
        initNavigationDataNew(ClientAPI.API_POINT + HttpUrls.PANICBUY_NEW);
//        mNavigationAdapterNew = new HomeNavigationNewAdapter (mNavigationDataNew, getActivity());
//        mGVPanicBuying.setAdapter(mNavigationAdapterNew);
//        initControlNavigation();
    }

    /**
     * 处理系统时间，与抢购时间
     */
    private void initTime() {
//        initTimer();
        //获取24时间制的时间
        Date date = new Date();
        mHour = date.getHours();
        mMinuts = date.getMinutes();
        mSecond = date.getSeconds();
        LogUtils.e("time", "hour:" + mHour + "--minute:" + mMinuts + "--second:" + mSecond);
        setTimeTV();
    }

    private void setTimeTV() {
        for (int i = 0; i < mTimes.length; i++) {
            if (i < mTimes.length - 1) {
                //在限制区间内
                if (mTimes[i] <= mHour && mHour < mTimes[i + 1]) {
                    LogUtils.e("time", "mTimes[i]:" + mTimes[i] + "--mTimes[i+1]:" + mTimes[i + 1]);
                    timeIndex = i;
                    mTimeDifference = (mTimes[timeIndex + 1] - mHour) * 3600000 - mMinuts * 60000 - mSecond * 1000;
                    break;
                }
            }
            //在限制区间外
            else {
                //最后的时间点到结束
                if (mTimes[mTimes.length - 1] <= mHour && mHour < mTimeLast) {
                    timeIndex = mTimes.length - 1;
                    mTimeDifference = (mTimeLast - mHour) * 3600000 - mMinuts * 60000 - mSecond * 1000;
                }
                //结束时间到零点
                else if (mTimeLast <= mHour && mHour <= 23) {
                    timeIndex = mTimes.length;
                    mTimeDifference = (23 - mHour) * 3600000 - mMinuts * 60000 - mSecond * 1000;
                }
                //零点到开始之前
                else if (0 <= mHour && mHour < mTimes[1]) {
                    timeIndex = -1;
                    mTimeDifference = (10 - mHour) * 3600000 - mMinuts * 60000 - mSecond * 1000;
                }
            }
        }
        //修改状态栏
        for (int i = 0; i < mTimeStateTVs.length; i++) {
            //恢复所有的切换按钮的状态
            mLLPanicBuys.get(i).setEnabled(true);
            //抢购导航按钮处理
            if (i < timeIndex) {
                mTimeStateTVs[i].setText("已结束");
            } else if (i > timeIndex) {
                mTimeStateTVs[i].setText("即将开始");
            } else {
                mTimeStateTVs[i].setText("疯抢中");
                //将抢购的状态设置为选中
                if (mLLPanicBuys.get(i) != null) {
                    mCurrentIndex = i;
                    mLLPanicBuys.get(i).setEnabled(false);
                }
            }
        }
        //计算时间
//        mTimer.schedule(mTimeTask,0L); //延时1000ms后执行，1000ms执行一次
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, mTimeDifference);
    }

    /**
     * 创建计时器
     */
    private void initTimer() {
        mTimer = new Timer(true);
        mTimeTask = new TimerTask() {
            public void run() {
                LogUtils.e("time", "mTimeDifference--" + mTimeDifference);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //重新获取时间
                        initTime();
                    }
                }, mTimeDifference);
            }
        };
    }

}

