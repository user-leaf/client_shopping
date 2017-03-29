package com.bjaiyouyou.thismall.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.ClassifyAdapter;
import com.bjaiyouyou.thismall.adapter.ClassifyListDropDownAdapter;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4Classify;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.ClassifyTwoCateModel;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassifyDetailFragment extends BaseFragment {
    public static final String TAG = ClassifyDetailFragment.class.getSimpleName();

    private View layout;
    private XRecyclerView mRecyclerView;
    private ClassifyAdapter mAdapter;
    private ArrayList<String> listData;
    private ConvenientBanner mConvenientBanner;

    private int refreshTime = 0;
    private int times = 0;

    // 筛选菜单
    private DropDownMenu mDropDownMenu;
    private String headers[] = {"全部分类", "综合排序"};
    private List<String> classifies;
    private List<Integer> classifyIds;
    private String ranks[] = {"不限", "销量最高", "上架时间"};
    private List<View> mPopupViews = new ArrayList<>();
    private ClassifyListDropDownAdapter mClassifyAdapter;
    private ClassifyListDropDownAdapter mRankAdapter;

    private Api4Classify mApi4Classify;

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
        loadData();
    }

    private void initVariable() {
        mApi4Classify = (Api4Classify) ClientApiHelper.getInstance().getClientApi(Api4Classify.class);
        classifies = new ArrayList<>();
        classifies.add("不限");
        classifies.add("北京");
        classifies.add("上海");
        classifies.add("广州");
        classifies.add("深圳");
        classifyIds = new ArrayList<>();
    }

    private void initView() {
        mDropDownMenu = (DropDownMenu) layout.findViewById(R.id.classify_dropdown_menu);
//        mRecyclerView = (XRecyclerView) layout.findViewById(R.id.classify_recyclerview);
        mRecyclerView = new XRecyclerView(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        View header = LayoutInflater.from(getContext()).inflate(R.layout.classify_recyclerview_header, null, false);
        LinearLayout AdContainer = (LinearLayout) header.findViewById(R.id.classify_header_container);
        ViewGroup.LayoutParams layoutParams = AdContainer.getLayoutParams();
//        if (layoutParams != null) {
//            LogUtils.d(TAG, "-1-height: " + layoutParams.height + ", width: " + layoutParams.width);
//            layoutParams.height = layoutParams.width / 3;
//            LogUtils.d(TAG, "not null");
//            LogUtils.d(TAG, "-2-height: " + layoutParams.height + ", width: " + layoutParams.width);
//        }
        // TODO: 2017/3/29 广告高度
        layoutParams.height = ScreenUtils.getScreenWidth(getContext()) / 3;

        // 广告控件
        mConvenientBanner = new ConvenientBanner(getActivity());
        ViewGroup.LayoutParams adParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mConvenientBanner.setLayoutParams(adParams);
        AdContainer.addView(mConvenientBanner, 0);
        mRecyclerView.addHeaderView(header);

        // 筛选菜单
        // init classify menu
        final ListView classifyView = new ListView(getContext());
        classifyView.setDividerHeight(0);
        mClassifyAdapter = new ClassifyListDropDownAdapter(getContext(), classifies);
        classifyView.setAdapter(mClassifyAdapter);

        // init rank menu
        final ListView rankView = new ListView(getContext());
        rankView.setDividerHeight(0);
        mRankAdapter = new ClassifyListDropDownAdapter(getContext(), Arrays.asList(ranks));
        rankView.setAdapter(mRankAdapter);

        mPopupViews.add(classifyView);
        mPopupViews.add(rankView);

        classifyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mClassifyAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : classifies.get(position));
                mDropDownMenu.closeMenu();
            }
        });

        rankView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mRankAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : ranks[position]);
                mDropDownMenu.closeMenu();
            }
        });

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), mPopupViews, mRecyclerView);
    }

    private void setupView() {

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        listData.clear();
                        for (int i = 0; i < 15; i++) {
                            listData.add("item" + i + "after " + refreshTime + " times of refresh");
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                if (times < 2) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            for (int i = 0; i < 15; i++) {
                                listData.add("item" + (1 + listData.size()));
                            }
                            mRecyclerView.loadMoreComplete();
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            for (int i = 0; i < 9; i++) {
                                listData.add("item" + (1 + listData.size()));
                            }
                            mRecyclerView.setIsnomore(true);
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }
                times++;
            }
        });

        listData = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            listData.add("item" + i);
        }
    }

    private void initCtrl() {
        mAdapter = new ClassifyAdapter(listData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setRefreshing(true);
    }

    private void loadData() {
        // 根据一级分类获取广告

        // 根据一级分类获取二级分类项列表
        mApi4Classify.getTwoLevelCate(1, new DataCallback<ClassifyTwoCateModel>(getContext()) {
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

                for (ClassifyTwoCateModel.TwoCateListBean item : twoCateList) {
                    classifies.add(item.getCate_name());
                    classifyIds.add(item.getId());
                }

                mClassifyAdapter.notifyDataSetChanged();
            }
        });

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
