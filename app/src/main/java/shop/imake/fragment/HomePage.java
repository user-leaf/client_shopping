package shop.imake.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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
import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.activity.GoodsDetailsActivity;
import shop.imake.activity.GoodsListShowActivity;
import shop.imake.activity.SearchGoodsActivity;
import shop.imake.activity.SystemPushMessageActivity;
import shop.imake.activity.WebShowActivity;
import shop.imake.adapter.HomeEveryDayEmptyAdapter;
import shop.imake.adapter.HomeGoodGridNewAdapter;
import shop.imake.adapter.HomeNavigationNewAdapter;
import shop.imake.adapter.HomeNavigationNewEmptyAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4Home;
import shop.imake.client.ClientAPI;
import shop.imake.client.ClientApiHelper;
import shop.imake.client.HttpUrls;
import shop.imake.model.HomeAdBigModel;
import shop.imake.model.HomeAdModel;
import shop.imake.model.HomeNavigationItem;
import shop.imake.model.HomeNavigationItemNew;
import shop.imake.model.HomeNavigationItemNewEmpty;
import shop.imake.model.HomeProductModel;
import shop.imake.model.IsHaveMessageNotRead;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.ImageUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.RefreshUtils;
import shop.imake.utils.SPUtils;
import shop.imake.utils.ScreenUtils;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.zxing.activity.CaptureActivity;

/**
 * @author Alice
 *         Update 2016/7/3 17:44
 *         ??????????????????
 *         <p/>
 *         ????????????????????????
 */

public class HomePage extends BaseFragment implements View.OnClickListener, OnItemClickListener {

    public static final String TAG = HomePage.class.getSimpleName();

    private long mExitTime;

    // ?????????
    private EditText mEtSearch;

    private ImageView ivScanCode;
    // ????????????????????????
    private View mLLPushMessage;
    // ???????????????
    private RelativeLayout mAdContainer;
    // ???????????????
    private ConvenientBanner mConvenientBanner;
    // ????????????????????????
    private XRecyclerView mGvShow;

    private XRecyclerView mEveryDayEmpty;

    // ???????????????????????????
    private List<HomeProductModel.DataBean> mProductInfoList;
    //???????????????????????????
    private int mEveryDayPage = 1;
    //?????????????????????
    private boolean isHaveNextNews = false;
    //??????????????????
    private HomeProductModel mHomeProductModel;

    // ?????????????????????
    private HomeGoodGridNewAdapter mHomeGoodGridAdapter;
    //????????????
    //    private ArrayList<Object> mIVList;
    //??????????????????
    private int mNavigationNum = 4;
    //??????????????????
    private LinearLayout mLLNavigationPanicBuying;
    //????????????????????????

    private HomeNavigationNewAdapter mNavigationAdapterNew;
    //????????????
    private int mOldIndex = 0;
    //???????????????
    private int mCurrentIndex = 0;
    //??????????????????
    private GridView mGVPanicBuying;
    //????????????
    private SwipeLayout sample1;
    //????????????
    private ImageView mIVPanicBuyMore;
    //???????????????????????????
    private LinearLayout mLLPanicBuy1;
    private LinearLayout mLLPanicBuy2;
    private LinearLayout mLLPanicBuy3;
    private LinearLayout mLLPanicBuy4;
    //????????????????????????
    private List<LinearLayout> mLLPanicBuys;

    //?????????????????????
    private List<HomeNavigationItem.DataBean> mNavigationData;
    //????????????????????????
    private List<HomeNavigationItemNew.RushToPurchaseTimeFrameBean> mTimeFrameBeans;
    private List<HomeNavigationItemNew.RushToPurchaseTimeFrameBean.DetailBean> mNavigationDataNew;
    private TextView mTVPanicBuyNull;

    private List<HomeAdModel> mNetImages;
    //???????????????????????????
    private GridView mGVPanicBuyingEmpty;
    private LinearLayout mLLUNNetWork;
    //??????????????????????????????
    private SimpleAdapter emptyAdapter;

    private HomeNavigationNewEmptyAdapter emptyAdapterNavigation;
    //??????????????????
    private int mIndexs[] = {1, 2, 3, 4};
    //????????????????????????
    private int mHour;
    private int mMinuts;
    private int mSecond;
    //??????????????????
    private long mTimeDifference = 0L;
    //?????????????????????
    private int mTimes[] = {9, 12, 15, 18};
    private int mTimeLast = 21;
    //??????????????????
    private TextView mTVPanicBuyState1;
    private TextView mTVPanicBuyState2;
    private TextView mTVPanicBuyState3;
    private TextView mTVPanicBuyState4;
    private TextView mTimeStateTVs[];
    //??????????????????
    private TextView mTVPanicBuyTime1;
    private TextView mTVPanicBuyTime2;
    private TextView mTVPanicBuyTime3;
    private TextView mTVPanicBuyTime4;
    private TextView mTimeTVs[];

    //??????????????????
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

    //?????????
    private Timer mTimer;
    //????????????
    private TimerTask mTimeTask;

    //?????????????????????
    private boolean mFirstComeIn = true;
    //?????????
    private int width2;

    //???????????????????????????
    private int timeIndex = 0;
    //?????????????????????
    private LinearLayout mLLPanicBuyEmpty;
    //????????????
    private HorizontalScrollView mSCRlPanicBuyEmpty;
    //?????????????????????
    private View headView;
    private View footView;
    private TextView mTVEveryDayTitle;
    //????????????????????????????????????
    private HomeEveryDayEmptyAdapter everyDayEmptyAdapter;
    //???????????????????????????
    private boolean isRefresh;
    private Api4Home mClientApi;
    //????????????????????????
    private ImageView mIvPushMessage;
    //??????????????????
    private View mTvPurchaseHistory;
    //??????????????????
    private View mLLScan;
    //?????????????????????????????????????????????????????????????????????
    private TextView mTvMessageNotRead;

    //?????????????????????
    private LocalBroadcastManager mLocalBroadcastManager;
    private IntentFilter intentFilter;
    private LocalJPushReceiver localReceiver;
    //???????????????????????????
    public static final String MESSAGE_RECEIVER_ACTION = "shop.imake.intent.ACTION_JPUSH_MESSAGE_RECEIVE";
    //???????????????
    private Dialog messageDialog;
    //???????????????
    private IsHaveMessageNotRead isHaveMessageNotRead;

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
        initReceiver();
        initView();
        setUpView();
        //??????????????????+??????
        initData();
        initCtrl();

        //??????????????????
//        //???????????????
//        initTimer();
//        initTime();
        //??????????????????
//        initPanicBuying();
//        initPanicBuyingEmpty();
    }

    /**
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //????????????????????????
            getJpushData();
//            initTime();
            //??????????????????
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
     * ???????????????
     */
    private void initVariables() {
        mClientApi = (Api4Home) ClientApiHelper.getInstance().getClientApi(Api4Home.class);
        //????????????
        mProductInfoList = new ArrayList<>();
        // ???????????????
        mNetImages = new ArrayList<>();
    }

    private void initCtrl() {
        mHomeGoodGridAdapter = new HomeGoodGridNewAdapter(getActivity(), mProductInfoList);
        mHomeGoodGridAdapter.setOnItemClickListener(new HomeGoodGridNewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //???????????????ID
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
     * ???????????????
     */
    private void initView() {
        mLLPushMessage = (layout.findViewById(R.id.ll_home_push_message));
        mTvMessageNotRead = ((TextView) layout.findViewById(R.id.tv_home_message_not_read));


        //??????????????????
//        mTvPurchaseHistory = layout.findViewById(R.id.home_tv_purchase_history);
        //?????????????????????
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
            empty.put("price", "???0.00\n+0??????");
            emptys.add(empty);
        }
        emptyAdapter = new SimpleAdapter(getActivity(), emptys, R.layout.item_home_navigatiom_gv_empty, new String[]{"img", "price"}, new int[]{R.id.iv_home_navigation_item_empty, R.id.tv_home_navigation_item_empty});

        //????????????
        mLLUNNetWork = ((LinearLayout) layout.findViewById(R.id.ll_home_un_network));
        //????????????ScrollView

        mEtSearch = ((EditText) layout.findViewById(R.id.home_et_search));
        mLLScan = (layout.findViewById(R.id.ll_home_scan));
//        ivScanCode = ((ImageView) layout.findViewById(R.id.home_iv_scan));
//        mLLPushMessage = layout.findViewById(R.id.ll_home_push_message);

        initHeadView();

        mGvShow = (XRecyclerView) layout.findViewById(R.id.home_gv_show);
        GridLayoutManager mgr = new GridLayoutManager(getContext(), 2);
        mgr.setOrientation(LinearLayoutManager.VERTICAL);
        mGvShow.setLayoutManager(mgr);
//       RecyclerView.LayoutManager mgr1=new GridLayoutManager(getContext(),2);
//        mGvShow.setLayoutManager(mgr1);
        //??????Item??????
//        mGvShow.addItemDecoration(new SpaceItemDecoration(0,5,5,0));
        //????????????
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
     * ???????????????
     */
    private void initHeadView() {
        headView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_head, null, false);
        footView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_foot, null, false);
//        mTvfoot = ((TextView) footView.findViewById(R.id.tv_fragment_home_food));

        mAdContainer = (RelativeLayout) headView.findViewById(R.id.home_rl_ad);
        mAdContainer.getLayoutParams().height = ScreenUtils.getScreenWidth(getActivity()) / 3;
//        //??????????????????????????????
//        mAdContainer.setFocusable(true);
//        mAdContainer.setFocusableInTouchMode(true);
//        mAdContainer.requestFocus();

        // ????????????
        mLLNavigationPanicBuying = ((LinearLayout) headView.findViewById(R.id.ll_home_navigation_panicbuying));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width2, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLLNavigationPanicBuying.setLayoutParams(params);

        mGVPanicBuying = ((GridView) headView.findViewById(R.id.gv_home_panicbuying));
        // ??????????????????????????????
//        mGVPanicBuying.setFocusable(false);
//        mLLNavigationPanicBuying.setFocusable(false);
        mGVPanicBuyingEmpty = ((GridView) headView.findViewById(R.id.gv_home_panicbuying_empty));
        mIVPanicBuyMore = ((ImageView) headView.findViewById(R.id.iv_home_navigation));
        //???????????????
        mLLPanicBuy1 = ((LinearLayout) headView.findViewById(R.id.ll_home_navigation_panicbuying1));
        mLLPanicBuy2 = ((LinearLayout) headView.findViewById(R.id.ll_home_navigation_panicbuying2));
        mLLPanicBuy3 = ((LinearLayout) headView.findViewById(R.id.ll_home_navigation_panicbuying3));
        mLLPanicBuy4 = ((LinearLayout) headView.findViewById(R.id.ll_home_navigation_panicbuying4));
        //??????????????????
        mLLPanicBuy1.setTag(0);
        mLLPanicBuy2.setTag(1);
        mLLPanicBuy3.setTag(2);
        mLLPanicBuy3.setTag(3);
        //??????????????????
//        mLLPanicBuy1.setEnabled(false);

        mLLPanicBuys = new ArrayList<>();
        mLLPanicBuys.add(mLLPanicBuy1);
        mLLPanicBuys.add(mLLPanicBuy2);
        mLLPanicBuys.add(mLLPanicBuy3);
        mLLPanicBuys.add(mLLPanicBuy4);
        //???????????????
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

        // ????????????
        mConvenientBanner = new ConvenientBanner(getActivity());
        ViewGroup.LayoutParams adParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mConvenientBanner.setLayoutParams(adParams);

        //??????????????????
        mTVEveryDayTitle = ((TextView) headView.findViewById(R.id.tv_home_everyday_title));
    }

    /**
     * ????????????????????????
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
                //???????????????ID
                long productID = mNavigationDataNew.get(position).getProduct_id();

//                Toast.makeText(getActivity(),""+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), GoodsDetailsActivity.class);
                intent.putExtra("productID", productID);
                startActivity(intent);

            }
        });
        mIVPanicBuyMore.setOnClickListener(this);

        // ??????????????????????????????
        mGvShow.setFocusable(false);
        //??????????????????????????????

        //????????????????????????
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
                    //????????????
//                   initPanicBuying();
//                    refreshPanicBuying();
                    //??????????????????
                    mEveryDayPage = 1;
                    isHaveNextNews = true;
                    isRefresh = true;
                    initData();
                    //???????????????????????????
                    getJpushData();
//                    initTime();

                } else {
                    mLLUNNetWork.setVisibility(View.VISIBLE);
                    mGvShow.refreshComplete();
                }
                mGvShow.setNoMore(false);
            }

            @Override
            public void onLoadMore() {

                if (isHaveNextNews) {
                    ++mEveryDayPage;
                    initData();
                    mGvShow.setNoMore(false);
                } else {
                    mGvShow.postDelayed(new Runnable() {

                        @Override
                        public void run() {
//                            ToastUtils.showShort("????????????");
                            mGvShow.setNoMore(true);
                            mGvShow.loadMoreComplete();
                        }
                    }, 1000);

                }
            }
        });

    }


    /**
     * ??????????????????
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
                //??????????????????
                mLLUNNetWork.setVisibility(View.GONE);
                //????????????
                if (response != null) {
                    //?????????????????????
                    if (isRefresh) {
                        mHomeGoodGridAdapter.clear();
                    }
                    //????????????????????????
                    isRefresh = false;
                    mHomeProductModel = (HomeProductModel) response;
                    isHaveNextNews = (mHomeProductModel.getNext_page_url() != null);
                    List<HomeProductModel.DataBean> listAdd = mHomeProductModel.getData();
                    reSetEveryDayNew(listAdd);
                }
                dismissLoadingDialog();
            }
        });


        // ??????????????????
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

                                } else if (adNum > 1) {
                                    mConvenientBanner.setPageIndicator(new int[]{R.mipmap.list_indicate_nor, R.mipmap.list_indicate_sel});
                                    mConvenientBanner.setCanLoop(true);
                                }

                                if (mAdContainer.getChildCount() <= 0) {
                                    setupAdView();
                                }

                            }
                        }

                        if (mNetImages.isEmpty()) { // mConvenientBanner??????????????????????????????????????????
                            mAdContainer.removeView(mConvenientBanner);
                        } else {
                            mConvenientBanner.notifyDataSetChanged();
                        }
                    }
                }

        );
    }

    /**
     * ???????????????????????????
     */
    private void getJpushData() {

        /////????????????????????????????????????????????????

        mClientApi.isHaveMessageNotRead(new DataCallback<IsHaveMessageNotRead>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
                //???????????????????????????????????????
                mTvMessageNotRead.setVisibility(View.INVISIBLE);
                LogUtils.d("isHaveMessageNotRead", "??????");
                dismissMessageDialog();
            }

            @Override
            public void onSuccess(Object response, int id) {
                LogUtils.d("isHaveMessageNotRead", "??????");
                if (response != null) {
                    LogUtils.d("isHaveMessageNotRead", "?????????");
                    isHaveMessageNotRead = (IsHaveMessageNotRead) response;
                    int message = isHaveMessageNotRead.getHasNoRead();
                    LogUtils.d("isHaveMessageNotRead", message + "message");
                    //?????????????????????
                    if (message == 0) {
                        mTvMessageNotRead.setVisibility(View.INVISIBLE);
                        dismissMessageDialog();
                        //??????????????????
                    } else if (message == 1) {
                        mTvMessageNotRead.setVisibility(View.VISIBLE);
                        //???????????????????????????
//                        if (messageDialog == null) {
//                            messageDialog = DialogUtils.createConfirmDialog(getContext(), null, "????????????????????????????????????", "??????", "",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            //????????????????????????
//                                            IsHaveMessageNotRead.LatestMessageBean bean = isHaveMessageNotRead.getLatestMessage();
//                                            if (bean != null) {
//                                                String id = bean.getId();
//                                                LogUtils.e("id", id);
//                                                //??????Id??????
//                                                StringBuffer sb = new StringBuffer(ClientAPI.URL_WX_H5);
//                                                sb.append("message_detail.html?id=");
//                                                sb.append(id);
//                                                sb.append("&token=");
//                                                sb.append(CurrentUserManager.getUserToken());
//                                                sb.append("&type=");
//                                                sb.append("android");
//
//                                                String webShowUrl = sb.toString().trim();
//                                                WebShowActivity.actionStart(getActivity(), webShowUrl);
//                                            }
//                                            dialog.dismiss();
//                                        }
//                                    },
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                        }
//                                    }
//                            );
//                            //??????????????????????????????
//                            messageDialog.setCancelable(false);
//                        }
//
//                        if (messageDialog!=null&&!messageDialog.isShowing()){
//                            messageDialog.show();
//                        }

                    }

                } else {
                    mTvMessageNotRead.setVisibility(View.INVISIBLE);
                    dismissMessageDialog();
                    LogUtils.d("isHaveMessageNotRead", "??????");
                }
            }
        });


        ////////////////////???????????????????????????????????????????????????

        mClientApi.getPushMessage(1, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);

            }

            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response)) {
                    Context context = getContext();
                    String pushMessages = response.trim();
                    if (pushMessages != null && context != null) {
                        SPUtils.put(getContext(), SPUtils.PUSH_MESSAGE_KEY_ONE, pushMessages);
                        //???????????????
                        mClientApi.getPushMessage(2, new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                CurrentUserManager.TokenDue(e);

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (!TextUtils.isEmpty(response)) {
                                    String pushMessages = response.trim();
                                    if (pushMessages != null) {
                                        SPUtils.put(getContext(), SPUtils.PUSH_MESSAGE_KEY_TWO, pushMessages);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    /**
     * ??????????????????????????????
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
     * ??????????????????????????????
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_et_search: // ?????????
                startActivity(new Intent(getActivity(), SearchGoodsActivity.class));
                break;
            case R.id.ll_home_scan:
                //???????????????
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivity(intent);
                break;
//            case R.id.home_iv_scan:
//                //???????????????
//
//                //???????????????
//                Intent intent = new Intent(getActivity(), CaptureActivity.class);
//                startActivity(intent);
//                break;

            //??????????????????.
            case R.id.iv_home_navigation:
                Intent intentNavigation = new Intent(getActivity(), GoodsListShowActivity.class);
                //??????????????????
                intentNavigation.putExtra("title", "????????????");
                //??????
                String moreUrl = ClientAPI.API_POINT + HttpUrls.PANICBUY_MORE + mIndexs[mCurrentIndex];
                LogUtils.e("moreUrl", moreUrl);
                intentNavigation.putExtra("url", moreUrl);
                startActivity(intentNavigation);
                break;

            //????????????????????????
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
//            case R.id.home_tv_purchase_history: // ??????????????????
//                Intent phIntent = new Intent(getActivity(), HistoryBuyNewActivity.class);
//                //??????????????????
//                phIntent.putExtra("title", "");
//                startActivity(phIntent);
//                break;
            case R.id.ll_home_push_message: // ??????????????????
//                Intent phIntent = new Intent(getActivity(), SystemPushMessageActivity.class);
                //test
                Intent phIntent = new Intent(getActivity(), SystemPushMessageActivity.class);
                //??????????????????
                phIntent.putExtra("title", "");
                startActivity(phIntent);
                break;
            default:
                return;
        }


    }

    private void setupAdView() {
        //???????????????Holder?????????????????????????????????????????????????????????????????????????????????????????????
        mConvenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, mNetImages);
//                //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????,????????????????????????????????????
//                .setPageIndicator(new int[]{R.mipmap.list_indicate_nor, R.mipmap.list_indicate_sel})
//                //????????????????????????
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

        // ???????????????????????????????????????
        mConvenientBanner.setOnItemClickListener(this);

        // ??????????????????????????????????????????
        mAdContainer.addView(mConvenientBanner);

    }

    /**
     * ??????????????? ConvenientBanner ??????
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
     * ??????????????????????????????
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        String link = mNetImages.get(position).getLink();
        // ??????link????????????????????????????????????
        if (!TextUtils.isEmpty(link) && !"#".equals(link) && (link.startsWith("http://") || link.startsWith("https://"))) {
            //???????????????????????????
            Intent intent = new Intent(getActivity(), WebShowActivity.class);
            intent.putExtra(WebShowActivity.PARAM_URLPATH, link + "?token=" + CurrentUserManager.getUserToken() + "&type=android");
            startActivity(intent);

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        //??????????????????
        mConvenientBanner.stopTurning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mTimer.cancel(); //???????????????

        //????????????????????????unregisterReceiver()????????????????????????????????????  
        mLocalBroadcastManager.unregisterReceiver(localReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == getActivity().RESULT_OK && data != null) {
            if (data.getStringExtra("result") != null) {
                Toast.makeText(getContext(), "Home----" + data.getStringExtra("result").toString(), Toast.LENGTH_SHORT).show();
//            Log.e("QRCode",data.getData().toString());
                Log.e("QRCode", data.getStringExtra("result").toString());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //????????????????????????
        mConvenientBanner.startTurning(5000);
        //????????????????????????
        getJpushData();
//        initTime();

    }


    /**
     * ??????????????????
     */

    private void initPanicBuying() {
        mNavigationDataNew = new ArrayList<>();
        initNavigationDataNew(ClientAPI.API_POINT + HttpUrls.PANICBUY_NEW);
        mNavigationAdapterNew = new HomeNavigationNewAdapter(mNavigationDataNew, getActivity());
        mGVPanicBuying.setAdapter(mNavigationAdapterNew);
        initControlNavigation();
    }

    private void initPanicBuyingEmpty() {
        //????????????
        int size = 4;
        int length = width2 / 3;
        int gridviewWidth = (int) (size * (length + 4));
        int itemWidth = (int) (length);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        //??????????????????
        mGVPanicBuyingEmpty.setLayoutParams(params); // ??????GirdView????????????,?????????????????????
        mGVPanicBuyingEmpty.setColumnWidth(itemWidth); // ??????????????????
        mGVPanicBuyingEmpty.setHorizontalSpacing(5); // ???????????????????????????
        mGVPanicBuyingEmpty.setStretchMode(GridView.NO_STRETCH);

        mGVPanicBuyingEmpty.setNumColumns(size); // ???????????????=???????????????
        List<HomeNavigationItemNewEmpty> navigationItemNewEmpty = new ArrayList<>();
        emptyAdapterNavigation = new HomeNavigationNewEmptyAdapter(navigationItemNewEmpty, getContext());
        mGVPanicBuyingEmpty.setAdapter(emptyAdapterNavigation);
//        mGVPanicBuying.setEmptyView(mGVPanicBuyingEmpty);
        mGVPanicBuying.setEmptyView(mLLPanicBuyEmpty);
    }

    /**
     * ????????????????????????????????????????????????
     */

    private void initNavigationDataNew(final String url) {
        LogUtils.e("????????????:", url);

        mClientApi.getNavigation(TAG, new DataCallback<HomeNavigationItemNew>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (NetStateUtils.isNetworkAvailable(getContext())) {
                    UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                }

            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response != null) {
                    HomeNavigationItemNew homeNavigationItemNew = (HomeNavigationItemNew) response;
                    mTimeFrameBeans = homeNavigationItemNew.getRush_to_purchase_time_frame();
                    //????????????????????????
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
//                    //????????????????????????
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
     * ??????????????????
     */

    private void initControlNavigation() {
        //????????????
        int size = mNavigationDataNew.size();
        //??????????????????????????????????????????
        checkGetMore();
//        Log.e("size",""+size);
        int length = width2 / 3;
        int gridviewWidth = (int) (size * (length + 4));
        int itemWidth = (int) (length);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        //??????????????????
        mGVPanicBuying.setLayoutParams(params); // ??????GirdView????????????,?????????????????????
        mGVPanicBuying.setColumnWidth(itemWidth); // ??????????????????
        mGVPanicBuying.setHorizontalSpacing(5); // ???????????????????????????
        mGVPanicBuying.setStretchMode(GridView.NO_STRETCH);
        mGVPanicBuying.setNumColumns(size); // ???????????????=???????????????
        //???????????????????????????

    }


    /**
     * ????????????????????????
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
     * ?????????????????????
     *
     * @param position
     */
    private void changeNavigation(int position) {
//        initNavigationData(0);
        mOldIndex = mCurrentIndex;
        mLLNavigationPanicBuying.getChildAt(mOldIndex).setEnabled(true);
        mCurrentIndex = position;
        //????????????
//        initNavigationData(mCurrentIndex);
        if (mTimeFrameBeans.size() == 4) {
            mNavigationDataNew = mTimeFrameBeans.get(mCurrentIndex).getDetail();
            //???????????????????????????????????????
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
     * ????????????????????????????????????????????????
     */
    private void checkGetMore() {
        //test
//        mNavigationDataNew.clear();
        if (mNavigationDataNew.size() == 0) {//??????
            mIVPanicBuyMore.setVisibility(View.GONE);
        } else {
            if (mNavigationDataNew.size() < 3) {//????????????3
                mIVPanicBuyMore.setVisibility(View.GONE);
            } else {
                mIVPanicBuyMore.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * ???????????????
     */
    private void refreshPanicBuying() {
        mNavigationDataNew = new ArrayList<>();
        initNavigationDataNew(ClientAPI.API_POINT + HttpUrls.PANICBUY_NEW);
//        mNavigationAdapterNew = new HomeNavigationNewAdapter (mNavigationDataNew, getActivity());
//        mGVPanicBuying.setAdapter(mNavigationAdapterNew);
//        initControlNavigation();
    }

    /**
     * ????????????????????????????????????
     */
    private void initTime() {
//        initTimer();
        //??????24??????????????????
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
                //??????????????????
                if (mTimes[i] <= mHour && mHour < mTimes[i + 1]) {
                    LogUtils.e("time", "mTimes[i]:" + mTimes[i] + "--mTimes[i+1]:" + mTimes[i + 1]);
                    timeIndex = i;
                    mTimeDifference = (mTimes[timeIndex + 1] - mHour) * 3600000 - mMinuts * 60000 - mSecond * 1000;
                    break;
                }
            }
            //??????????????????
            else {
                //???????????????????????????
                if (mTimes[mTimes.length - 1] <= mHour && mHour < mTimeLast) {
                    timeIndex = mTimes.length - 1;
                    mTimeDifference = (mTimeLast - mHour) * 3600000 - mMinuts * 60000 - mSecond * 1000;
                }
                //?????????????????????
                else if (mTimeLast <= mHour && mHour <= 23) {
                    timeIndex = mTimes.length;
                    mTimeDifference = (23 - mHour) * 3600000 - mMinuts * 60000 - mSecond * 1000;
                }
                //?????????????????????
                else if (0 <= mHour && mHour < mTimes[1]) {
                    timeIndex = -1;
                    mTimeDifference = (10 - mHour) * 3600000 - mMinuts * 60000 - mSecond * 1000;
                }
            }
        }
        //???????????????
        for (int i = 0; i < mTimeStateTVs.length; i++) {
            //????????????????????????????????????
            mLLPanicBuys.get(i).setEnabled(true);
            //????????????????????????
            if (i < timeIndex) {
                mTimeStateTVs[i].setText("?????????");
            } else if (i > timeIndex) {
                mTimeStateTVs[i].setText("????????????");
            } else {
                mTimeStateTVs[i].setText("?????????");
                //?????????????????????????????????
                if (mLLPanicBuys.get(i) != null) {
                    mCurrentIndex = i;
                    mLLPanicBuys.get(i).setEnabled(false);
                }
            }
        }
        //????????????
//        mTimer.schedule(mTimeTask,0L); //??????1000ms????????????1000ms????????????
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, mTimeDifference);
    }

    /**
     * ???????????????
     */
    private void initTimer() {
        mTimer = new Timer(true);
        mTimeTask = new TimerTask() {
            public void run() {
                LogUtils.e("time", "mTimeDifference--" + mTimeDifference);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //??????????????????
                        initTime();
                    }
                }, mTimeDifference);
            }
        };
    }


    /**
     * ???????????????????????????????????????????????????
     */
    private void initReceiver() {
        //?????????????????????
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        //??????intentFilter?????????action???????????????
        intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_RECEIVER_ACTION);
        //????????????????????????????????????????????????????????????action?????????????????????
        localReceiver = new LocalJPushReceiver();
        mLocalBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    class LocalJPushReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //????????????????????????????????????????????????????????????????????????
            getJpushData();
            mTvMessageNotRead.setVisibility(View.VISIBLE);

        }
    }

    /**
     * ???????????????????????????
     */
    public void dismissMessageDialog() {
        if (messageDialog != null && messageDialog.isShowing()) {
            messageDialog.dismiss();
        }
    }

}

