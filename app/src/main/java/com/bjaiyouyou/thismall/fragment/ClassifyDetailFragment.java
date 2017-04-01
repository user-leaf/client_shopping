package com.bjaiyouyou.thismall.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.GoodsDetailsActivity;
import com.bjaiyouyou.thismall.activity.WebShowActivity;
import com.bjaiyouyou.thismall.adapter.ClassifyAdapter;
import com.bjaiyouyou.thismall.adapter.ClassifyGridDropDownAdapter;
import com.bjaiyouyou.thismall.adapter.ClassifyListDropDownAdapter;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4Classify;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.ClassifyCateAdModel;
import com.bjaiyouyou.thismall.model.ClassifyProductModel;
import com.bjaiyouyou.thismall.model.ClassifyTwoCateModel;
import com.bjaiyouyou.thismall.other.GlideRoundTransform;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yyydjk.library.DropDownMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

/**
 * 分类详情页
 * <p>
 * Created by kanbin on 2017/3/28.
 */
public class ClassifyDetailFragment extends BaseFragment implements OnItemClickListener, ClassifyAdapter.OnItemClickListener {
    public static final String TAG = ClassifyDetailFragment.class.getSimpleName();

    // debug
    private int classTag;
    private boolean debug = false;
    private int requestTime = 0;
    private int printTime = 0;
    private TextView mTvShow;

    private int currentCategoryType;    // 分类项级别（1/2）
    private int currentCategoryId;      // 当前选中的分类id
    private int firstCateId;            // 一级分类id(用于获取二级分类项请求、不限分类的id等，有必要保存)
    private int currentPageNum;         // 当前商品分页页码
    private String strDefaultSort = "全部分类";
    // flag
    private boolean isSecondCategoryLoadSuccess;    // 二级分类项数据是否请求成功
    private boolean canShowAd;          // 是否可以显示广告栏

    private View layout;
    private XRecyclerView mRecyclerView;
    private ClassifyAdapter mAdapter;
    private ArrayList<ClassifyProductModel.DataBean> mListData;

    // 筛选菜单
    private DropDownMenu mDropDownMenu;
    private String headers[] = {"全部分类"};
    private List<String> classifies;
    private List<Integer> classifyIds;
    private String ranks[] = {strDefaultSort, "销量最高", "上架时间"};
    private List<View> mPopupViews;
    //    private ClassifyListDropDownAdapter mClassifyAdapter;
    private ClassifyGridDropDownAdapter mClassifyAdapter;
    private AdapterView.OnItemClickListener mClassifyClickListener;
    private ClassifyListDropDownAdapter mRankAdapter;

    // 广告
    private List<ClassifyCateAdModel.ProductCateAdsBean> mAdModels;
    private LinearLayout mAdContainer;
    private ConvenientBanner mConvenientBanner;

    private Api4Classify mApi4Classify;
    private TextView mTvNoMoreView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        classTag = getArguments().getInt(ClassifyPage.INTENT_PARAM);

        LogUtils.d("====", "Fragment " + classTag + ": onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("====", "Fragment " + classTag + ": onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.d("====", "Fragment " + classTag + ": onCreateView");

        layout = inflater.inflate(R.layout.fragment_classify_detail, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initVariable();
        initView();
        setupView();
        initCtrl();

        loadData4Ad(firstCateId);
        loadData4SecondCategory(firstCateId);
        loadData4Products(true, currentPageNum, currentCategoryId, currentCategoryType);

        LogUtils.d("====", "Fragment " + classTag + ": onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("====", "Fragment " + classTag + ": onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("====", "Fragment " + classTag + ": onResume");
        //广告开始自动翻页
        if (mConvenientBanner.isCanLoop()) {
            mConvenientBanner.startTurning(5000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("====", "Fragment " + classTag + ": onPause");
        //广告停止翻页
        if (mConvenientBanner.isCanLoop()) {
            mConvenientBanner.stopTurning();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d("====", "Fragment " + classTag + ": onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d("====", "Fragment " + classTag + ": onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d("====", "Fragment " + classTag + ": onDetach");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.d("====", "Fragment " + classTag + ": setUserVisibleHint， isVisibleToUser: " + isVisibleToUser);

        if (isVisibleToUser) {  // 可见时
            if (mRecyclerView != null) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        } else {  // 不可见时
            if (mDropDownMenu != null) {
                if (mDropDownMenu.isShowing()) {
                    mDropDownMenu.closeMenu();
                }

                /**
                 * 设置不可见时就还原为“全部分类”
                 *
                 * 临时解决切换回来要显示全部分类的需求（耗流量）
                 * 不用担心mDropDownMenu为null，如果==null，说明第一次创建，是会走网络请求的
                 */
                ((TextView) ((LinearLayout) mDropDownMenu.getChildAt(0)).getChildAt(0)).setText(strDefaultSort);
                if (mClassifyClickListener != null) {
                    mClassifyClickListener.onItemClick(null, null, 0, 0);
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.d("====", "Fragment " + classTag + ": onHiddenChanged， hidden: " + hidden);
    }

    public ClassifyDetailFragment() {
        // Required empty public constructor
    }

    private void initVariable() {
        currentCategoryType = -1;
        firstCateId = -1;
        currentPageNum = 1;
        isSecondCategoryLoadSuccess = false;
        canShowAd = true;

        mApi4Classify = (Api4Classify) ClientApiHelper.getInstance().getClientApi(Api4Classify.class);
        mAdModels = new ArrayList<>();
        mListData = new ArrayList<>();

        mPopupViews = new ArrayList<>();
        classifies = new ArrayList<>();
        classifyIds = new ArrayList<>();

        Bundle bundle = getArguments();
        firstCateId = bundle.getInt(ClassifyPage.INTENT_PARAM);

        currentCategoryId = firstCateId;
        currentCategoryType = 1; // 一级分类

        classifies.add(strDefaultSort);
        classifyIds.add(firstCateId);

    }

    private void initView() {

//        mRecyclerView = (XRecyclerView) layout.findViewById(R.id.classify_recyclerview);
        mRecyclerView = new XRecyclerView(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRecyclerView.setLayoutParams(lp);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        // 广告控件
        mConvenientBanner = new ConvenientBanner(getActivity());
        ViewGroup.LayoutParams adParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mConvenientBanner.setLayoutParams(adParams);

        // headerView1
        View mHeader = LayoutInflater.from(getContext()).inflate(R.layout.classify_recyclerview_header, null, false);
        mAdContainer = (LinearLayout) mHeader.findViewById(R.id.classify_header_container);
        ViewGroup.LayoutParams layoutParams = mAdContainer.getLayoutParams();
        layoutParams.height = ScreenUtils.getScreenWidth(getContext()) / 4;
        LogUtils.d(TAG, "ad height: " + layoutParams.height);
        mRecyclerView.addHeaderView(mHeader);

        // headerView2
        mTvShow = new TextView(getContext());
        if (debug) {
            mTvShow.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
            mRecyclerView.addHeaderView(mTvShow);
        }
        printInfo();

        // 在setLoadingMoreEnable(true)的时候，会把所有footview都显示出来，nomoreview会闪一下，效果不好
//        mTvNoMoreView = new TextView(getContext());
//        ViewGroup.LayoutParams lpNoMore = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        mTvNoMoreView.setLayoutParams(lpNoMore);
//        mTvNoMoreView.setGravity(Gravity.CENTER);
//        mTvNoMoreView.setText("已经全部加载完毕");
//        mTvNoMoreView.setVisibility(View.GONE);
//        mRecyclerView.addFootView(mTvNoMoreView);

        // 筛选菜单
        // init classify menu
        // 列表样式
//        final ListView classifyView = new ListView(getContext());
//        classifyView.setDividerHeight(0);
//        mClassifyAdapter = new ClassifyListDropDownAdapter(getContext(), classifies);
//        classifyView.setAdapter(mClassifyAdapter);

        // 网格样式
        final View classifyGridView = LayoutInflater.from(getContext()).inflate(R.layout.layout_twocate_classify, null);
        GridView classifyView = (GridView) classifyGridView.findViewById(R.id.classify_two_classify_gridview);
        mClassifyAdapter = new ClassifyGridDropDownAdapter(getContext(), classifies);
        classifyView.setAdapter(mClassifyAdapter);

        mClassifyClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mClassifyAdapter.setCheckItem(position);

                // 网络请求
                currentPageNum = 1;
                currentCategoryId = classifyIds.get(position);
                // 点击筛选中的“全部”时
                if (position == 0) {
                    canShowAd = true;
                    loadData4Ad(firstCateId);   // 请求广告数据
                    currentCategoryType = 1;    // 属于一级分类
                } else {
                    canShowAd = false;
                    setAdViewVisible(false);
                    currentCategoryType = 2; // 二级分类
                }
                loadData4Products(true, currentPageNum, currentCategoryId, currentCategoryType);

                mRecyclerView.smoothScrollToPosition(0);  // 解决有时不从第1条开始显示的问题

                mDropDownMenu.setTabText(position == 0 ? headers[0] : classifies.get(position));
                mDropDownMenu.closeMenu();
            }
        };
        classifyView.setOnItemClickListener(mClassifyClickListener);

        mPopupViews.add(classifyGridView);

        // 排序菜单去掉了
//        // init rank menu
//        final ListView rankView = new ListView(getContext());
//        rankView.setDividerHeight(0);
//        mRankAdapter = new ClassifyListDropDownAdapter(getContext(), Arrays.asList(ranks));
//        rankView.setAdapter(mRankAdapter);
//
//        rankView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mRankAdapter.setCheckItem(position);
//                mDropDownMenu.setTabText(position == 0 ? headers[1] : ranks[position]);
//                mDropDownMenu.closeMenu();
//            }
//        });
//        mPopupViews.add(rankView);

        //init dropdownview
        mDropDownMenu = (DropDownMenu) layout.findViewById(R.id.classify_dropdown_menu);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), mPopupViews, mRecyclerView);
    }

    /**
     * 调试，先把XR的headerView中的textView设为可见
     */
    private void printInfo() {
        if (debug) {
            printTime++;
            mTvShow.setText("requestTime:" + requestTime + ", printTime: " + printTime + ", \nfirstCateId: " + firstCateId + ", \ncurrentCategoryId: " + currentCategoryId + ", currentCategoryType: " + currentCategoryType);
        }
    }

    private void setupView() {
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.CubeTransition);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                currentPageNum = 1;

                // 请求商品数据
                loadData4Products(true, currentPageNum, currentCategoryId, currentCategoryType);

                // 请求广告数据
                loadData4Ad(firstCateId);

                // 上次请求成功则不再请求
                if (!isSecondCategoryLoadSuccess) {
                    loadData4SecondCategory(firstCateId);
                }
            }

            @Override
            public void onLoadMore() {
                loadData4Products(false, currentPageNum, currentCategoryId, currentCategoryType);
            }
        });

    }

    private void initCtrl() {
        mAdapter = new ClassifyAdapter(getContext(), mListData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshing(false);

        mAdapter.setOnItemClickListener(this);
    }

    /**
     * 请求分类下的广告数据
     *
     * @param firstCategoryId 一级分类id
     */
    private void loadData4Ad(int firstCategoryId) {

        // 根据一级分类获取广告
        mApi4Classify.getCategoryAd(firstCategoryId, new DataCallback<ClassifyCateAdModel>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response == null) {
                    return;
                }

                ClassifyCateAdModel model = (ClassifyCateAdModel) response;
                List<ClassifyCateAdModel.ProductCateAdsBean> productCateAds = model.getProductCateAds();
                if (productCateAds == null) {
                    return;
                }
                mAdModels.clear();
                mAdModels.addAll(productCateAds);

                // 测试数据  要glide加载原图而非缩略图
//                mAdModels.clear();
//                // https://gss0.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/d6ca7bcb0a46f21f3c2ad100fe246b600c33ae43.jpg
//                ClassifyCateAdModel.ProductCateAdsBean item1 = new ClassifyCateAdModel.ProductCateAdsBean();
//                item1.setImage_path("https://gss0.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item");
//                item1.setImage_base_name("d6ca7bcb0a46f21f3c2ad100fe246b600c33ae43.jpg");
//                item1.setLink("https://www.baidu.com");
//                mAdModels.add(item1);
//
//                // https://a-ssl.duitang.com/uploads/item/201607/14/20160714220705_MXRBw.jpeg
//                ClassifyCateAdModel.ProductCateAdsBean item2 = new ClassifyCateAdModel.ProductCateAdsBean();
//                item2.setImage_path("https://a-ssl.duitang.com/uploads/item/201607/14");
//                item2.setImage_base_name("20160714220705_MXRBw.jpeg");
//                item2.setLink("https://www.duitang.com/");
//                mAdModels.add(item2);
//
//                // https://img3.doubanio.com/view/photo/photo/public/p2447889764.webp
//                ClassifyCateAdModel.ProductCateAdsBean item3 = new ClassifyCateAdModel.ProductCateAdsBean();
//                item3.setImage_path("https://img3.doubanio.com/view/photo/photo/public");
//                item3.setImage_base_name("p2447889764.webp");
////                item3.setLink("https://www.douban.com");
//                mAdModels.add(item3);

                int adNum = mAdModels.size();
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

                if (mAdModels.isEmpty()) { // mConvenientBanner貌似不支持数据减为空时的刷新
                    mAdContainer.removeView(mConvenientBanner);
                    setAdViewVisible(false);
                } else {
                    setAdViewVisible(true);
                    mConvenientBanner.notifyDataSetChanged();
                }

            }

        });

    }

    /**
     * 请求二级分类项数据（筛选框信息）
     *
     * @param firstCateId 一级分类id
     */
    private void loadData4SecondCategory(final int firstCateId) {

        // 根据一级分类获取二级分类项列表
        mApi4Classify.getTwoLevelCate(firstCateId, new DataCallback<ClassifyTwoCateModel>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onSuccess(Object response, int id) {
                isSecondCategoryLoadSuccess = true;

                if (response == null) {
                    return;
                }

                ClassifyTwoCateModel model = (ClassifyTwoCateModel) response;
                List<ClassifyTwoCateModel.TwoCateListBean> twoCateList = model.getTwoCateList();
                if (twoCateList == null) {
                    return;
                }

                classifies.clear();
                classifyIds.clear();

                classifies.add(strDefaultSort);
                classifyIds.add(firstCateId);

                for (ClassifyTwoCateModel.TwoCateListBean item : twoCateList) {
                    classifies.add(item.getCate_name());
                    classifyIds.add(item.getId());
                }

                mClassifyAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 请求推荐或分类下的商品数据
     *
     * @param pageNum      页码
     * @param resetPageNo  是否重设页码
     * @param categoryId   分类id
     * @param categoryType 分类级别
     */
    private void loadData4Products(boolean resetPageNo, final int pageNum, final int categoryId, int categoryType) {
        LogUtils.d("@@@", classTag + " : loadData4Products");

//        requestTime ++;
//        printInfo();

        if (resetPageNo) {
            currentPageNum = 1;
        }

        // 请求商品数据
        mApi4Classify.getProductsData(categoryId, categoryType, pageNum, new DataCallback<ClassifyProductModel>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                mRecyclerView.refreshComplete();
                mRecyclerView.loadMoreComplete();
            }

            @Override
            public void onSuccess(Object response, int id) {
                mRecyclerView.refreshComplete();
                mRecyclerView.loadMoreComplete();

                if (response == null) {
                    return;
                }

                ClassifyProductModel model = (ClassifyProductModel) response;
                List<ClassifyProductModel.DataBean> data = model.getData();
                if (data != null) {
                    if (pageNum <= 1) {
                        mListData.clear();
                    }
                    mListData.addAll(data);
                }

                // 最后一页
                if (currentPageNum >= model.getLast_page()) {
//                    mRecyclerView.setIsnomore(true);
                    mRecyclerView.setLoadingMoreEnabled(false);
//                    if (mTvNoMoreView.getVisibility() == View.GONE) {
//                        mTvNoMoreView.setVisibility(View.VISIBLE);
//                    }
                } else {
//                    mRecyclerView.setIsnomore(false);
                    mRecyclerView.setLoadingMoreEnabled(true);
//                    if (mTvNoMoreView.getVisibility() == View.VISIBLE) {
//                        mTvNoMoreView.setVisibility(View.GONE);
//                    }
                }

                mAdapter.notifyDataSetChanged();

                currentPageNum++;
            }
        });

    }

    private void setupAdView() {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        mConvenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, mAdModels);
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.mipmap.list_indicate_nor, R.mipmap.list_indicate_sel})
//                //设置指示器的位置
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

        // 为广告轮播设置条目点击监听
        mConvenientBanner.setOnItemClickListener(this);

        // 把广告控件加入到广告栏容器中
        mAdContainer.addView(mConvenientBanner);

    }

    @Override
    public void onItemClick(View view, int position) {
        ClassifyProductModel.DataBean dataBean = mListData.get(position);
        if (dataBean == null) {
            return;
        }

        ClassifyProductModel.DataBean.ProductBean productBean = dataBean.getProduct();
        if (productBean == null) {
            return;
        }

        GoodsDetailsActivity.actionStart(getContext(), productBean.getId());
    }

    /**
     * 设置广告栏是否显示
     *
     * @param isShow
     */
    public void setAdViewVisible(boolean isShow) {
        if (isShow && canShowAd) {
            if (mAdContainer.getVisibility() == View.GONE) {
                mAdContainer.setVisibility(View.VISIBLE);
            }

        } else {
            if (mAdContainer.getVisibility() == View.VISIBLE) {
                mAdContainer.setVisibility(View.GONE);
            }
        }
    }

    public class LocalImageHolderView implements Holder<ClassifyCateAdModel.ProductCateAdsBean> {
        private ImageView imageView;
        private GlideRoundTransform mGlideRoundTransform;

        @Override
        public View createView(Context context) {
            mGlideRoundTransform = new GlideRoundTransform(getContext(), 8);
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, ClassifyCateAdModel.ProductCateAdsBean adModel) {
            StringBuilder sb = new StringBuilder();
            sb.append(adModel.getImage_path())
                    .append(File.separator)
                    .append(adModel.getImage_base_name());
            String imagePath = sb.toString();

            LogUtils.d(TAG, "adimage url: " + imagePath);

            Glide.with(getActivity())
                    .load(ImageUtils.getThumb(imagePath, ScreenUtils.getScreenWidth(getContext()), 0))
//                    .load(imagePath)
                    .transform(mGlideRoundTransform)
                    .into(imageView);

        }

    }

    /**
     * 广告轮播条目点击
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        String link = mAdModels.get(position).getLink();
        // 广告link不为空则跳转，为空不跳转
        if (!TextUtils.isEmpty(link) && !"#".equals(link) && (link.startsWith("http://") || link.startsWith("https://"))) {
            //带值跳转到广告页面
            Intent intent = new Intent(getActivity(), WebShowActivity.class);
            intent.putExtra(WebShowActivity.PARAM_URLPATH, link + "?token=" + CurrentUserManager.getUserToken() + "&type=android");
            startActivity(intent);

        }

    }

    // TODO: 2017/3/29 没起作用，以后再说
    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

}
