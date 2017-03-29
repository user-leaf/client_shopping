package com.bjaiyouyou.thismall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.SearchGoodsActivity;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4Classify;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.ClassifyOneCateModel;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 分类页
 * Created by kanbin on 2017/3/28.
 */
/**
 *author Qxh
 *created at 2017/3/29 10:47
 *添加一级分类
 */
public class ClassifyPage extends BaseFragment {
    public static final String TAG = ClassifyPage.class.getSimpleName();

    //布局填充器
    private LayoutInflater inflater;
    //一级菜单栏的条目
    private ScrollView scrollView;
    //一级菜单栏，条目布局数组
    private View[] views;
    //一级菜单栏，标题控件数组
    private TextView[] tvList;
    //一级菜单栏，选中标记数组
    private ImageView[] ivList;

    //一级菜单栏的数据
    private List<ClassifyOneCateModel.OneCateListBean> list;
    //当前选中的一级菜单项
    private int currentItem = 0;
    //菜单选项对应的内容容器
    private ViewPager viewpager;
    //菜单选中内容适配器
    private ShopAdapter shopAdapter;

    protected View layout;

    private Api4Classify mApi4Client;

    private  int mOneCateId=-1;
    //搜索框
    private EditText mEtSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        inflater = LayoutInflater.from(getContext());
        this.inflater=inflater;
        layout = inflater.inflate(R.layout.fragment_classify, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadData();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //引起报错
//        loadData();
        LogUtils.e("onHiddenChanged","onHiddenChanged");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (list.size()==0){
            loadData();
        }
    }

    private void initView() {
        viewpager = (ViewPager) layout.findViewById(R.id.viewpager_goods_classify);
        scrollView = (ScrollView)layout.findViewById(R.id.scrollview_level_one_classify);
        mEtSearch = ((EditText) layout.findViewById(R.id.editText_search_classify));
    }

    private void setupView() {
        viewpager.setOnPageChangeListener(onPageChangeListener);
        mEtSearch.setOnClickListener(this);

    }

    private void initCtrl() {
        shopAdapter = new ShopAdapter(getFragmentManager());
        viewpager.setAdapter(shopAdapter);
    }

    private void loadData() {
        //https://testapi2.bjaiyouyou.com/api/v1/product/getProductCount  获取商品数量  搜索栏
        mApi4Client= (Api4Classify) ClientApiHelper.getInstance().getClientApi(Api4Classify.class);

        /////////////////////////获取一级菜单数据/////////////////////////////////////////////
        list=new ArrayList<>();
        mApi4Client.getOneLevelCate(new DataCallback<ClassifyOneCateModel>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response!=null){
                    ClassifyOneCateModel classifyOneCateModel= (ClassifyOneCateModel) response;
                    list=classifyOneCateModel.getOneCateList();
                    LogUtils.e("一级标题的数目",list.size()+"");
                    showToolsView();
                    setupView();
                    initCtrl();
                }
            }
        });

        /////////////////////////获得可搜索商品数目//////////////////////////////////////////////////////
        mApi4Client.getClassifyGoodsNumber(new DataCallback<String>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response!=null){
                    String canSearchGoodsNum= (String) response;
                    mEtSearch.setHint("搜索商品，共"+canSearchGoodsNum+"款好物");
                }
            }
        });
    }
    /**
     * 动态生成显示items中的textview和imageView
     */
    private void showToolsView() {
        //获取一级菜单数据
//        list = Model.toolsList;
        LinearLayout toolsLayout = (LinearLayout)layout.findViewById(R.id.ll_level_one_classify);
        if (list!=null&&list.size()!=0){
            tvList = new TextView[list.size()];
            ivList =new ImageView[list.size()];
            views = new View[list.size()];

            for (int i = 0; i < list.size(); i++) {
                View view = inflater.inflate(R.layout.item_level_one_classify, null);
                view.setId(i);
                view.setOnClickListener(toolsItemListener);
                TextView textView = (TextView) view.findViewById(R.id.text_level_one_classify);
                ImageView imageView= (ImageView) view.findViewById(R.id.line);
                ClassifyOneCateModel.OneCateListBean oneCateListBean=list.get(i);
                if (oneCateListBean!=null){
                    String cateName=oneCateListBean.getCate_name();
                    if (!TextUtils.isEmpty(cateName)){
                        textView.setText(cateName);
                        toolsLayout.addView(view);
                        LogUtils.e("oneCateName",""+cateName);
                        tvList[i] = textView;
                        ivList[i]=imageView;
                        views[i] = view;
                    }
                    mOneCateId=oneCateListBean.getId();
                }
            }
            changeTextColor(0);
        }
    }

    /**
     * 菜单条目点击事件，控制内容切换
     */
    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewpager.setCurrentItem(v.getId());
//            ToastUtils.showLong(""+list.get(v.getId()).getCate_name());
        }
    };

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if (viewpager.getCurrentItem() != arg0)
                viewpager.setCurrentItem(arg0);
            if (currentItem != arg0) {
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem = arg0;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    };



    /**
     * ViewPager 加载选项卡
     *
     * @author Administrator
     *
     */
    private class ShopAdapter extends FragmentPagerAdapter {
        public ShopAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            Fragment fragment = new ClassifyDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("OneCateId", mOneCateId);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return list==null?0:list.size();
        }
    }

    /**
     * 改变textView的颜色
     *
     * @param id    t
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < tvList.length; i++) {
            //非选中条目
            if (i != id) {
                tvList[i].setBackgroundColor(0x00000000);
//                tvList[i].setTextColor(0xFF000000);
                tvList[i].setTextColor(0xFF222222);
                String name=tvList[i].getText().toString();
                LogUtils.e("getText",+i+"******"+name);
                tvList[i].setTextSize(14);
                ivList[i].setVisibility(View.INVISIBLE);
            }
        }
        //选中条目
        tvList[id].setBackgroundColor(0xFFFFFFFF);
        tvList[id].setTextColor(0xFFe10c28);
//        tvList[id].setTextColor(0xFFFF5D5E);
        tvList[id].setTextSize(17);
        ivList[id].setVisibility(View.VISIBLE);
        mOneCateId=list.get(id).getId();
    }

    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getTop());
        scrollView.smoothScrollTo(0, x);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            case  R.id.editText_search_classify:
                startActivity(new Intent(getActivity(), SearchGoodsActivity.class));
                break;
        }
    }
}
