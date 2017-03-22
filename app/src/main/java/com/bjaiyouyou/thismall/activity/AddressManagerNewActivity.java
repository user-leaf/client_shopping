package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.AddressAdapter2;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.AddressInfo2;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
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

    private IUUTitleBar mTitleBar;

    private ListView mListView;
    private AddressAdapter2 mAdapter;
    private List<AddressInfo2.MemberAddressesBean> mDataList;

    private TextView mTvAddAddr;

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
        loadData();

    }

    private void initVariables() {
        mPageName = getIntent().getStringExtra(EXTRA_PAGE_NAME);
        mDataList = new ArrayList<>();
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.address_title_bar);
        mListView = (ListView) findViewById(R.id.address_listview);
        mTvAddAddr = (TextView) findViewById(R.id.address_add);

        mNoNetView = findViewById(R.id.net_fail);
        mBodyView = findViewById(R.id.body);
        mNoLoginView = findViewById(R.id.no_login);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mTvAddAddr.setOnClickListener(this);

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
        showLoadingDialog();

        ClientAPI.getAddressList(TAG, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
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
