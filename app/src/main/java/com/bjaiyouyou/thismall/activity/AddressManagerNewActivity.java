package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.AddressAdapter2;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.AddressInfo2;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 新的收货地址列表页（新）
 * 条目可向左滑动的
 * <p/>
 * 条目不可滑动
 *
 * @author kanbin
 * @date 2016/6/12
 * @see AddressManagerActivity
 */
public class AddressManagerNewActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = AddressManagerNewActivity.class.getSimpleName();
    public static final String EXTRA_PAGE_NAME = "pageName";

    // 地址数据列表
    private ListView mListView;
    // 数据集
    private List<AddressInfo2.MemberAddressesBean> mDataList;
    // 添加地址
    private TextView mTvAdd;
    // 地址列表适配器
    private AddressAdapter2 mAdapter;
    // 标题栏
    private IUUTitleBar mTitleBar;

    // 断网提示 未登录提示
    private View mNoNetView;
    private View mBodyView;
    private View mNoLoginView;
    private String mPageName; // 从上一页传过来的flag，用于标记哪一页，如果是确认订单页，则条目点击后finish

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager_new);
        initVariables();
        initView();
        setupView();
        initCtrl();
//        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 请求数据 更新列表
        loadData();
    }

    private void initVariables() {
        mPageName = getIntent().getStringExtra(EXTRA_PAGE_NAME);
        mDataList = new ArrayList<>();
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.address_title_bar);
        mListView = (ListView) findViewById(R.id.address_listview);
        mTvAdd = (TextView) findViewById(R.id.address_add);

        mNoNetView = findViewById(R.id.net_fail);
        mBodyView = findViewById(R.id.body);
        mNoLoginView = findViewById(R.id.no_login);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mTvAdd.setOnClickListener(this);

    }

    private void initCtrl() {
        mAdapter = new AddressAdapter2(this, mDataList, mPageName);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
            case R.id.address_add: // 添加地址页
                jump(AddressAddActivity.class, false);
                break;
        }
    }

    public void loadData() {
        /**
         * 测试数据
         */
//        mDataList.clear();
//        for (int i = 0; i < 15; i++) {
//            AddressInfo addressInfo = new AddressInfo();
//            addressInfo.setName("某某" + i);
//            addressInfo.setTel("1316121142" + i);
//            addressInfo.setAddress("东贸国际写字楼" + i);
//            if (i == 3) {
//                addressInfo.setDefault(true);
//            } else {
//                addressInfo.setDefault(false);
//            }
//            mDataList.add(addressInfo);
//        }
//
//        mAdapter.setData(mDataList);

        showLoadingDialog();

        ClientAPI.getAddressList(TAG, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
//                        ToastUtils.showShort("请求失败");
                checkNet();
                dismissLoadingDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "onResponse()");
                // 无网、未登录页及body页的显示与隐藏
                mNoNetView.setVisibility(View.GONE);
                mNoLoginView.setVisibility(View.GONE);
                mBodyView.setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(response)) {
                    if (!"[]".equals(response)) {
                        Gson gson = new Gson();
                        AddressInfo2 addressInfo2 = gson.fromJson(response, AddressInfo2.class);
                        List<AddressInfo2.MemberAddressesBean> data = addressInfo2.getMember_addresses();

                        if (data != null) {
                            LogUtils.d("AddressManagerNewActivity", "list = " + data.toString());
                        }
                        mAdapter.setData(data);
                    }else {
//                                ToastUtils.showShort("无数据");
                    }
                }
                dismissLoadingDialog();
            }
        });

    }

    // 检查网络
    private void checkNet() {

        if (!NetStateUtils.isNetworkAvailable(this)) {
            LogUtils.d(TAG, "checkNet  无网");
            mNoNetView.setVisibility(View.VISIBLE);
            mBodyView.setVisibility(View.GONE);

            // 无网络页面中的重新加载按钮
            TextView refreshView = (TextView) mNoNetView.findViewById(R.id.net_fail_refresh);
            refreshView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadData();
                }
            });

        } else {
            // 有网但未登录
            LogUtils.d(TAG, "checkNet  有网，去登录");
            mNoNetView.setVisibility(View.GONE);
            mBodyView.setVisibility(View.GONE);

            gotoLogin();
        }

    }

    // 显示去登录页面
    private void gotoLogin() {
        mNoLoginView.setVisibility(View.VISIBLE);

        TextView tvGotoLogin = (TextView) findViewById(R.id.no_login_goto);
        tvGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(LoginActivity.class,false);
            }
        });
    }

}
