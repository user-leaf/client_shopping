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
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yyydjk.library.DropDownMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

/**
 * 分类详情页
 *
 * Created by kanbin on 2017/3/28.
 */
public class ClassifyDetailFragment extends BaseFragment implements OnItemClickListener, ClassifyAdapter.OnItemClickListener {
    public static final String TAG = ClassifyDetailFragment.class.getSimpleName();

    private View layout;
    // 分类项级别（推荐、一级、二级）
    private int cateType = -1;
    // 网络请求用到的（一级或二级）分类项id
    private int pid;
    // 一级分类id(用于获取二级分类项请求，不限分类的id等，有必要保存)
    private int oneCateId = -1;
    // 分页页码
    private int pageNo = 1;
    private String strDefaultSort = "不限";

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
    private ClassifyListDropDownAdapter mRankAdapter;

    // 广告
    private List<ClassifyCateAdModel.ProductCateAdsBean> mAdModels;
    private LinearLayout mAdContainer;
    private ConvenientBanner mConvenientBanner;

    private Api4Classify mApi4Classify;

    private int requestTime = 0;
    private int printTime = 0;
    private TextView mTvShow;

    public ClassifyDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        loadData(pid, cateType);
    }

    @Override
    public void onPause() {
        super.onPause();
        //广告停止翻页
        mConvenientBanner.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        //广告开始自动翻页
        mConvenientBanner.startTurning(5000);
    }

    private void initVariable() {
        mApi4Classify = (Api4Classify) ClientApiHelper.getInstance().getClientApi(Api4Classify.class);
        mAdModels = new ArrayList<>();
        mListData = new ArrayList<>();

        mPopupViews = new ArrayList<>();
        classifies = new ArrayList<>();
        classifyIds = new ArrayList<>();

        Bundle bundle = getArguments();
        oneCateId = bundle.getInt(ClassifyPage.INTENT_PARAM);

        pid = oneCateId;
        cateType = 1; // 一级分类

        classifies.add(strDefaultSort);
        classifyIds.add(oneCateId);

    }

    private void initView() {
        mDropDownMenu = (DropDownMenu) layout.findViewById(R.id.classify_dropdown_menu);
//        mRecyclerView = (XRecyclerView) layout.findViewById(R.id.classify_recyclerview);
        mRecyclerView = new XRecyclerView(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        View mHeader = LayoutInflater.from(getContext()).inflate(R.layout.classify_recyclerview_header, null, false);
        mAdContainer = (LinearLayout) mHeader.findViewById(R.id.classify_header_container);
        mTvShow = (TextView) mHeader.findViewById(R.id.classify_header_show);
        printInfo();
        ViewGroup.LayoutParams layoutParams = mAdContainer.getLayoutParams();
        layoutParams.height = ScreenUtils.getScreenWidth(getContext()) / 3;
        LogUtils.d(TAG, "ad height: " + layoutParams.height);

        // 广告控件
        mConvenientBanner = new ConvenientBanner(getActivity());
        ViewGroup.LayoutParams adParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mConvenientBanner.setLayoutParams(adParams);

        mRecyclerView.addHeaderView(mHeader);

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

        classifyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mClassifyAdapter.setCheckItem(position);
                // 网络请求
                pid = classifyIds.get(position);
                cateType = 2; // 二级分类
                loadData(pid, cateType);

                mDropDownMenu.setTabText(position == 0 ? headers[0] : classifies.get(position));
                mDropDownMenu.closeMenu();
            }
        });

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
//        // 网络请求
//        pid = classifyIds.get(position);
//        cateType = 2; // 二级分类
//        loadData(cateType);
//                mDropDownMenu.setTabText(position == 0 ? headers[1] : ranks[position]);
//                mDropDownMenu.closeMenu();
//            }
//        });

        mPopupViews.add(classifyGridView);
//        mPopupViews.add(rankView);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), mPopupViews, mRecyclerView);
    }

    private void printInfo() {
        printTime ++;
        mTvShow.setText("requestTime:" + requestTime + ", printTime: " + printTime + ", \nclass: " + this.getId() + ", \npid: " + pid + ", cateType: " + cateType);

    }

    private void setupView() {

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                loadData(pid, cateType);
            }

            @Override
            public void onLoadMore() {
                pageNo++;
                loadData(pid, cateType);
            }
        });

    }

    private void initCtrl() {
        mAdapter = new ClassifyAdapter(getContext(), mListData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshing(false);

        mAdapter.setOnItemClickListener(this);
    }

    private void loadData(int pid, int cateType) {
        requestTime ++;
        printInfo();

        // 根据一级分类获取广告
        mApi4Classify.getCateAd(-1, new DataCallback<ClassifyCateAdModel>(getContext()) {
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

                // 测试数据
                mAdModels.clear();
                // https://gss0.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/d6ca7bcb0a46f21f3c2ad100fe246b600c33ae43.jpg
                ClassifyCateAdModel.ProductCateAdsBean item1 = new ClassifyCateAdModel.ProductCateAdsBean();
                item1.setImage_path("https://gss0.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item");
                item1.setImage_base_name("d6ca7bcb0a46f21f3c2ad100fe246b600c33ae43.jpg");
                item1.setLink("https://www.baidu.com");
                mAdModels.add(item1);

                // https://a-ssl.duitang.com/uploads/item/201607/14/20160714220705_MXRBw.jpeg
                ClassifyCateAdModel.ProductCateAdsBean item2 = new ClassifyCateAdModel.ProductCateAdsBean();
                item2.setImage_path("https://a-ssl.duitang.com/uploads/item/201607/14");
                item2.setImage_base_name("20160714220705_MXRBw.jpeg");
                item2.setLink("https://www.duitang.com/");
                mAdModels.add(item2);

                // https://img3.doubanio.com/view/photo/photo/public/p2447889764.webp
                ClassifyCateAdModel.ProductCateAdsBean item3 = new ClassifyCateAdModel.ProductCateAdsBean();
                item3.setImage_path("https://img3.doubanio.com/view/photo/photo/public");
                item3.setImage_base_name("p2447889764.webp");
//                item3.setLink("https://www.douban.com");
                mAdModels.add(item3);

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
                    if (mAdContainer.getVisibility() == View.VISIBLE) {
                        mAdContainer.setVisibility(View.GONE);
                    }

                } else {
                    if (mAdContainer.getVisibility() == View.GONE) {
                        mAdContainer.setVisibility(View.VISIBLE);
                    }
                    mConvenientBanner.notifyDataSetChanged();
                }

            }

        });

        // 根据一级分类获取二级分类项列表
        mApi4Classify.getTwoLevelCate(oneCateId, new DataCallback<ClassifyTwoCateModel>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onSuccess(Object response, int id) {
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
                classifyIds.add(oneCateId);

                for (ClassifyTwoCateModel.TwoCateListBean item : twoCateList) {
                    classifies.add(item.getCate_name());
                    classifyIds.add(item.getId());
                }

                mClassifyAdapter.notifyDataSetChanged();
            }
        });

        LogUtils.d("@@@", "ClassifyDetailFragment pid: " + pid + ", cateType: " + cateType);
        // 请求商品数据
        mApi4Classify.getProductsData(pid, cateType, pageNo, new DataCallback<ClassifyProductModel>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                mRecyclerView.refreshComplete();
                mRecyclerView.loadMoreComplete();
            }

            @Override
            public void onSuccess(Object response, int id) {
                mRecyclerView.refreshComplete();

                if (response == null) {
                    return;
                }

                ClassifyProductModel model = (ClassifyProductModel) response;
                List<ClassifyProductModel.DataBean> data = model.getData();
                if (data != null) {
                    if (pageNo <= 1) {
                        mListData.clear();
                    }
                    mListData.addAll(data);
                }

                mRecyclerView.refreshComplete();
                mRecyclerView.loadMoreComplete();

                // 最后一页
                if (pageNo >= model.getLast_page()) {
                    mRecyclerView.setIsnomore(true);
                }else {
                    mRecyclerView.setIsnomore(false);
                }

                mAdapter.notifyDataSetChanged();
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
        GoodsDetailsActivity.actionStart(getContext(), dataBean.getId());
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
//                    .load(ImageUtils.getThumb(imagePath, ScreenUtils.getScreenWidth(getContext()), 0))
                    .load(imagePath)
                    .transform(mGlideRoundTransform)
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
