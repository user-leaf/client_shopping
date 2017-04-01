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
    public static final String INTENT_PARAM = "OneCateId";

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

    private List<ClassifyOneCateModel.OneCateListBean> listAll;
    public static final int RECOMMEND_ID_CLASSIFY=-1;
    //当前选中的一级菜单项
    private int currentItem = 0;
    //菜单选项对应的内容容器
    private ViewPager viewpager;
    //菜单选中内容适配器
    private ShopAdapter shopAdapter;

    protected View layout;

    private Api4Classify mApi4Client;

    private  int mOneCateId=RECOMMEND_ID_CLASSIFY;
    //搜索框
    private EditText mEtSearch;

    private boolean isFristStart=true;

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
        setupView();
        initVariable();
        loadData();
        initCtrl();
    }

    private void initVariable() {
        if (isFristStart){
            //网络加载数据
            list=new ArrayList<>();
            //初试列表第一项
            listAll=new ArrayList<>();
            ClassifyOneCateModel.OneCateListBean one=new ClassifyOneCateModel.OneCateListBean();
            one.setCate_name("推荐");
            one.setId(RECOMMEND_ID_CLASSIFY);
            listAll.add(one);
            showToolsView();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        reLoadData();
        LogUtils.e("ClassifyPage-onHiddenChanged","onHiddenChanged");
    }

    @Override
    public void onResume() {
        super.onResume();
        reLoadData();
        LogUtils.e("ClassifyPage-onResume","onResume");
    }

    /**
     * 重新加载一级分类数据
     */
    private void reLoadData() {
        if (list!=null&&listAll!=null&&list.size()==0&&listAll.size()==1){
            loadData();
            LogUtils.e("reLoadData","重新加载一级菜单数据");
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
        isFristStart=false;
        //https://testapi2.bjaiyouyou.com/api/v1/product/getProductCount  获取商品数量  搜索栏
        mApi4Client= (Api4Classify) ClientApiHelper.getInstance().getClientApi(Api4Classify.class);

        /////////////////////////获取一级菜单数据/////////////////////////////////////////////

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
                    if (listAll.size()==1){
                        listAll.addAll(list);
                        shopAdapter.notifyDataSetChanged();
                    }
                    //Viewpager的数据源数目改变，及时通知否则报错
                    //java.lang.IllegalStateException: The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: 4, found: 0 Pager id: com.bjaiyouyou.thismall:id/viewpager_goods_classify Pager class: class android.support.v4.view.ViewPager Problematic adapter: class com.bjaiyouyou.thismall.fragment.ClassifyPage$ShopAdapter
                    //at android.support.v4.view.ViewPager.populate(ViewPager.java:1139)
                    LogUtils.e("一级标题的数目",listAll.size()+"");
                    showToolsView();
                }
            }
        });


        /////////////////////////获得可搜索商品数目//////////////////////////////////////////////////////
//        mApi4Client.getClassifyGoodsNumber(new DataCallback<String>(getContext()) {
//            @Override
//            public void onFail(Call call, Exception e, int id) {
//                UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
//            }
//
//            @Override
//            public void onSuccess(Object response, int id) {
//                if (response!=null){
//                    String canSearchGoodsNum= (String) response;
//                    mEtSearch.setHint("搜索商品，共"+canSearchGoodsNum+"款好物");
//                }
//            }
//        });
    }
    /**
     * 动态生成显示items中的textview和imageView
     */
    private void showToolsView() {
        //获取一级菜单数据
//        list = Model.toolsList;
        LinearLayout toolsLayout = (LinearLayout)layout.findViewById(R.id.ll_level_one_classify);
        toolsLayout.removeAllViews();

        if (listAll!=null&&listAll.size()!=0){
            tvList = new TextView[listAll.size()];
            ivList =new ImageView[listAll.size()];
            views = new View[listAll.size()];

            for (int i = 0; i < listAll.size(); i++) {
                View view = inflater.inflate(R.layout.item_level_one_classify, null);
                view.setId(i);
                view.setOnClickListener(toolsItemListener);
                TextView textView = (TextView) view.findViewById(R.id.text_level_one_classify);
                ImageView imageView= (ImageView) view.findViewById(R.id.line);
                ClassifyOneCateModel.OneCateListBean oneCateListBean=listAll.get(i);
                if (oneCateListBean!=null){
                    String cateName=oneCateListBean.getCate_name();
                    if (!TextUtils.isEmpty(cateName)){
                        textView.setText(cateName);
                        toolsLayout.addView(view);
//                        LogUtils.e("oneCateName",""+cateName);
                        tvList[i] = textView;
                        ivList[i]=imageView;
                        views[i] = view;
                    }
//                    mOneCateId=oneCateListBean.getId();
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
            //默认有切换动画
//            viewpager.setCurrentItem(v.getId());
            //去掉切换动画
            viewpager.setCurrentItem(v.getId(),false);
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
                //改变选中条目状态
                changeTextColor(arg0);
                //改变选中条目滚动位置
//                changeTextLocation(arg0);
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
            if (listAll!=null){
                ClassifyOneCateModel.OneCateListBean oneCateListBean=listAll.get(index);
                if (oneCateListBean!=null){
                    mOneCateId=listAll.get(index).getId();
                    LogUtils.e("mOneCateId传递值","*********"+mOneCateId);
                    bundle.putInt(INTENT_PARAM, mOneCateId);
                    fragment.setArguments(bundle);
                }
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return listAll==null?0:listAll.size();
        }

//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            return super.instantiateItem(container, position);
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
////            super.destroyItem(container, position, object);
//        }
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
                tvList[i].setTextSize(14);
                ivList[i].setVisibility(View.INVISIBLE);
            }
        }
        //选中条目
        tvList[id].setBackgroundColor(0xFFFFFFFF);
        tvList[id].setTextColor(0xFFe10c28);
//        tvList[id].setTextColor(0xFFFF5D5E);
        tvList[id].setTextSize(15);
        ivList[id].setVisibility(View.VISIBLE);
        int Id=listAll.get(id).getId();
        String name=tvList[id].getText().toString();
        LogUtils.e("选中的是第",+id+"条*****"+name+"*******Id:"+Id);
    }

    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getTop());
        //置顶
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
