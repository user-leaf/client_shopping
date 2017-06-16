package shop.imake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import shop.imake.Constants;
import shop.imake.R;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4ClientOther;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.HistorySearchItem;
import shop.imake.model.SearchHot;
import shop.imake.utils.LogUtils;
import shop.imake.utils.SPUtils;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.zxing.activity.CaptureActivity;

/**
 * 商品搜索界面
 *
 * @author Alice
 *         Create 2016/6/2 11:55
 *         <p>
 *         修改
 * @author JackB
 * @date 2016/6/16
 */
@ContentView(R.layout.activity_search_goods)
public class SearchGoodsActivity extends BaseActivity implements View.OnClickListener {
    //获得控价
    //返回
    @ViewInject(R.id.left_image_search)
    private ImageView mIvBack;
    //搜索按钮
    @ViewInject(R.id.right_layout_search)
    private RelativeLayout rl_rightTitle;
    //清除搜索输入记录
    @ViewInject(R.id.iv_search_etClear)
    private ImageView iv_etClear;
    @ViewInject(R.id.iv_search_scan)
    private ImageView iv_scan;
    //拍照入口
//    @ViewInject(R.id.iv_search_photo)
//    private ImageView iv_photo;
    //搜索商品输入框
    @ViewInject(R.id.et_search)
    private EditText etSearch;
    //热门搜索区
    @ViewInject(R.id.flowLayout_HotSearch)
    private TagFlowLayout hotTFL;
    //搜索历史区
    @ViewInject(R.id.flowLayout_HistorySearch)
    private TagFlowLayout historyTFL;
    //清除历史搜索按钮
    @ViewInject(R.id.iv_search_historyClear)
    private ImageView iv_historyClear;
    //搜索数据集合
    private List<String> hotList;
    private List<String> historyList;
    //历史搜索
    private Set<String> historySet;
    //搜索数据的适配器
    private TagAdapter<String> hotAdapter;
    private TagAdapter<String> historyAdapter;
    //跳转地址
    private String mUrl;
    //搜索关键字
    private String mKeyWord;
    //网络加载数据
    private List<SearchHot.SearchRecordsBean> mHotList;
    //布局填充器
    private LayoutInflater mInflater = null;

    private boolean isNotFrist = false;
    //历史搜索限制
    private int mHistoryLimit = 10;
    //父布局
    private FlowLayout mCurrentParent;
    private boolean isFristOnTagOnclick = true;
    //用于标识控件
    private String mTag = "";

    private String historyJsonString = "";
    private List<HistorySearchItem> historyJsonList;

    private Api4ClientOther mClient;
    public static final String TAG = SearchGoodsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyJsonList = new ArrayList<>();

        //注入注解
        ViewUtils.inject(this);
        //设置控件的监听事件
        setUpView();
        //搜索记录表的布局填充器
        //获得布局填充器
        mInflater = LayoutInflater.from(this);
        //对搜索框的处理
        infoEditText();

        //热门搜索处理网络数据下载/////////////////////////////////////////////////////////////////////////
        loadHotSearchData();

    }

    /**
     * 初始化历史搜索列表
     */
    private void initHistoryView() {
        historySet = new LinkedHashSet<>();
        historyList = new ArrayList<String>();
        /**
         * 获取Set
         */
//        if (SPUtils.contains(this,SPUtils.HISTORY_SEARCH_KEY)){
//            historySet= (HashSet<String>) SPUtils.get(this,SPUtils.HISTORY_SEARCH_KEY,historySet);
//            Iterator<String> iterator=historySet.iterator();
//            while(iterator.hasNext()){
////                if (i<mHistoryLimit){
//                historyList.add(iterator.next());
////                    ++i;
////                }else {
////                    break;
////                }
//            }
//        }

        /**
         * 获取String
         */

        if (SPUtils.contains(this, Constants.HISTORY_SEARCH_KEY)) {
            historyJsonString = (String) SPUtils.get(this, Constants.HISTORY_SEARCH_KEY, historyJsonString);
            if (!historyJsonString.isEmpty()) {
                historyJsonList = new Gson().fromJson(historyJsonString, new TypeToken<List<HistorySearchItem>>() {
                }.getType());
                int size = historyJsonList.size();
                for (int i = 0; i < size; i++) {
                    historyList.add(historyJsonList.get(i).getName().toString().trim());
                }
            }
        }


        historyAdapter = new TagAdapter<String>(historyList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.search_item_tv, historyTFL, false);
                tv.setText(s);
                return tv;
            }


            //某个字符串为初始化选中状态
            @Override
            public boolean setSelected(int position, String s) {
                return s.equals("");
            }
        };
        historyTFL.setAdapter(historyAdapter);
        initHistorySearch(historyTFL, historyList);
    }

    /**
     * 设置控价监听
     */
    private void setUpView() {
//        rl_leftTitle.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        rl_rightTitle.setOnClickListener(this);
        iv_etClear.setOnClickListener(this);
//        iv_photo.setOnClickListener(this);
        iv_historyClear.setOnClickListener(this);
        iv_scan.setOnClickListener(this);
    }

    private void infoEditText() {
        //添加搜索编辑框的实时监听
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = etSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(s)) {
                    photoHide();

                } else {
                    photoShow();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 隐藏相机图标
     */

    private void photoHide() {
//        iv_photo.setVisibility(View.GONE);
        iv_etClear.setVisibility(View.VISIBLE);
    }

    /**
     * 显示相机图标
     */
    private void photoShow() {
//        iv_photo.setVisibility(View.VISIBLE);
        iv_etClear.setVisibility(View.GONE);
    }


    /**
     * 热门搜索的处理
     */
    private void initHotSearch(final TagFlowLayout TFL, final List<String> data) {
        //处理历史数据列表条目的点击事件
        TFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //对被电击条目的数据进行处理
                etSearch.setText(data.get(position));
                //跳转到搜索详情页面
                etSearch.setSelection(data.get(position).length());
//                Toast.makeText(getApplicationContext(), hotList.get(position), Toast.LENGTH_SHORT).show();

                //添加条件保证必须有一个条目被选
//                view.setEnabled(false);
//                view.setClickable(true);


                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (i != position) {
//                        parent.getChildAt(i).setEnabled(true);
                        parent.getChildAt(i).setClickable(false);
                    } else {
                        parent.getChildAt(i).setClickable(true);
                    }
                }
                parent.setTag("hot");
                initHistoryView();

//                historyAdapter.setSelectedList(-1);
                return true;
            }
        });
        TFL.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {

            }
        });
    }

    /**
     * 历史搜索的处理
     */
    private void initHistorySearch(final TagFlowLayout TFL, final List<String> data) {
        //处理历史数据列表条目的点击事件
        TFL.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //对被电击条目的数据进行处理
                etSearch.setText(data.get(position));
                //跳转到搜索详情页面
                etSearch.setSelection(data.get(position).length());
//                Toast.makeText(getApplicationContext(), hotList.get(position), Toast.LENGTH_SHORT).show();
                //添加条件保证必须有一个条目被选
//                view.setEnabled(false);
//                view.setClickable(true);

                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (i != position) {
//                        parent.getChildAt(i).setEnabled(true);
                        parent.getChildAt(i).setClickable(false);
                    } else {
                        parent.getChildAt(i).setClickable(true);
                    }
                }
                initSearchHot();
                parent.setTag("history");
//                hotAdapter.setSelectedList(-1);
                return true;
            }
        });
        TFL.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {

            }
        });
    }


    /**
     * 热门搜索的数据模拟
     */
    private void initHotSearchData(List<String> data) {
        for (int i = 0; i < mHotList.size(); i++) {
            String keyWord = mHotList.get(i).getKeyword();
//            LogUtils.e("keyWord",keyWord);
            if (!TextUtils.isEmpty(keyWord)) {
                data.add(keyWord);
            }
        }
    }

    /**
     * 下载接口热门数据
     */
    private void loadHotSearchData() {
        mHotList = new ArrayList<>();
        mClient = (Api4ClientOther) ClientApiHelper.getInstance().getClientApi(Api4ClientOther.class);
        mClient.getHotSearchGoodsData(TAG, new DataCallback<SearchHot>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response != null) {
                    SearchHot searchHot = (SearchHot) response;
                    mHotList = searchHot.getSearch_records();
                    LogUtils.e("size", mHotList.size() + "");
                    initSearchHot();
                }

            }
        });
    }

    /**
     * 热门搜索设置数据
     */
    private void initSearchHot() {
        isNotFrist = true;
        hotList = new ArrayList<>();
        initHotSearchData(hotList);
        //创建适配器
        hotAdapter = new TagAdapter<String>(hotList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.search_item_tv, hotTFL, false);
                tv.setText(s);
                return tv;
            }

            //某个字符串为初始化选中状态
            @Override
            public boolean setSelected(int position, String s) {
                return s.equals("");
            }
        };
        //填充适配器
        hotTFL.setAdapter(hotAdapter);
        initHotSearch(hotTFL, hotList);
    }

    /**
     * 处理控件的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //清除编辑框
            case R.id.iv_search_etClear:
                etSearch.setText("");
                photoShow();
                break;
//            //跳转图片处理界面
//            case R.id.iv_search_photo:
//                jump(TakePhotoActivity.class, false);
//                break;
            //返回
            case R.id.left_image_search:
                finish();
                break;
            //删除所有数据
            case R.id.iv_search_historyClear:
                historyList.clear();
                historyAdapter.notifyDataChanged();
                //清除本地数据
                SPUtils.remove(this, Constants.HISTORY_SEARCH_KEY);
                //通知服务器进行处理
                break;
            case R.id.iv_search_scan:
                //跳转到扫描界面  进行扫描
                jump(CaptureActivity.class, 300, false);
                break;
            //搜索
            case R.id.right_layout_search:
                clickSearch();
                break;
        }

    }

    /**
     * 处理搜索按钮响应事件
     */
    private void clickSearch() {
        String search = etSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(search)) {
            //跳转搜索详情页面
            searchGoods(search);
            //添加到历史列表，并搜索
            addHistoryData(search);
        } else {
            if (hotList != null) {
                //热门搜索第一个
                if (hotList.size() != 0) {
                    etSearch.setText(hotList.get(0));
                    etSearch.setSelection(hotList.get(0).length());
                    searchGoods(hotList.get(0));
                    addHistoryData(hotList.get(0));
                } else {
                    //历史搜索中最近搜索的一条，即列表最上边的一条
                    if (historyList.size() != 0) {
                        etSearch.setText(historyList.get(0));
                        etSearch.setSelection(historyList.get(0).length());
                        searchGoods(historyList.get(0));
                        addHistoryData(historyList.get(0));
                    }
                    Toast.makeText(this, "搜索内容内容不可为空", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    /**
     * 向搜索历史中添加数据
     */
    private void addHistoryData(String search) {
        int size = historyList.size();
        for (int i = 0; i < size; i++) {
            if (search.equals(historyList.get(i))) {
                historyList.remove(i);
                --size;
            }
        }


//        /**
//         * 存储set
//         */
//        //控制历史搜索的个数是 mHistoryLimit
//
//        if (historyList.size()>=mHistoryLimit){
//            historySet.remove(historyList.get(size-1));
//            historyList.remove(size-1);
//        }
//        //最多只能保存10历史搜素
//        historyList.add(0, search);
//        historyAdapter.notifyDataChanged();
//        //更新本地数据
//        historySet.add(search);
//        SPUtils.put(this,SPUtils.HISTORY_SEARCH_KEY,historySet);
//        //将数据上传服务器

        /**
         * 存储String
         */
        //控制历史搜索的个数是 mHistoryLimit

        if (historyList.size() >= mHistoryLimit) {
            historyList.remove(size - 1);
        }
        //最多只能保存10历史搜素
        historyList.add(0, search);
        historyAdapter.notifyDataChanged();

        historyJsonList.clear();
        for (int i = 0; i < historyList.size(); i++) {
            historyJsonList.add(new HistorySearchItem(historyList.get(i)));
        }
        //更新本地数据
        historyJsonString = new Gson().toJson(historyJsonList);
        LogUtils.e("historyJsonString:", historyJsonString);
        SPUtils.put(this, Constants.HISTORY_SEARCH_KEY, historyJsonString);
        //将数据上传服务器
    }

    /**
     * 搜索商品的方法
     */
    private void searchGoods(String key) {
        mKeyWord = etSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(mKeyWord)) {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra("title", key);
//            mUrl= ClientAPI.API_POINT+ClientAPI.SEARCH+mKeyWord;
            intent.putExtra("key", mKeyWord);
            jump(intent, false);
        } else {
            Toast.makeText(this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
            return;
        }
//        jump(SearchResultsActivity.class, false);
//        jump(GoodsDetailsActivity.class,false);

    }

    /**
     * 收到返回的二维码结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == RESULT_OK && data != null) {
//            Toast.makeText(this, data.getStringExtra("result").toString(), Toast.LENGTH_SHORT).show();
//            Log.e("QRCode",data.getData().toString());
            Log.e("QRCode", data.getStringExtra("result").toString());
        }
    }


    /**
     * 重置页面
     */
    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("onResume", "onResume");
        //历史搜索处理//////////////////////////////////////////////////////////////////////////////////
        //限制历史搜索的搜索的条目个数
        initHistoryView();
        //清空搜索框
        etSearch.setText("");
        if (isNotFrist) {
            //加载热门搜索
            loadHotSearchData();
        }
    }
}
