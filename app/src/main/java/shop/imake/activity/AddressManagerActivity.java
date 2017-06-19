package shop.imake.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.adapter.AddressAdapter;
import shop.imake.client.ClientAPI;
import shop.imake.model.AddressInfo2;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.LoadViewHelper;
import shop.imake.utils.LogUtils;
import shop.imake.widget.IUUTitleBar;

/**
 * 新的收货地址列表页（新）
 * 条目可向左滑动的
 * <p/>
 * 条目不可滑动
 *
 * @author JackB
 * @date 2016/6/12
 * @see AddressManagerActivity
 */
public class AddressManagerActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = AddressManagerActivity.class.getSimpleName();
    public static final String EXTRA_PAGE_NAME = "pageName";

    private IUUTitleBar mTitleBar;

    private ListView mListView;
    private AddressAdapter mAdapter;
    private List<AddressInfo2.MemberAddressesBean> mDataList;

    private TextView mTvAddAddr;

    // 断网提示 未登录提示
    private View mNoNetView;
    private View mBodyView;
    private View mNoLoginView;
    private String mPageName; // 从上一页传过来的flag，用于标记哪一页，如果是确认订单页，则条目点击后finish
    private LoadViewHelper mLoadViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);
        initVariables();
        initView();
        setupView();
        initCtrl();
        mLoadViewHelper = new LoadViewHelper(mBodyView);
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
        mAdapter = new AddressAdapter(this, mDataList, mPageName);
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
        mLoadViewHelper.showLoading();

        ClientAPI.getAddressList(TAG, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
                mLoadViewHelper.showError(AddressManagerActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadData();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "onResponse()");
                mLoadViewHelper.restore();
                if (!TextUtils.isEmpty(response)) {
                    if (!"[]".equals(response)) {
                        Gson gson = new Gson();
                        AddressInfo2 addressInfo2 = gson.fromJson(response, AddressInfo2.class);
                        List<AddressInfo2.MemberAddressesBean> data = addressInfo2.getMember_addresses();

                        if (data != null) {
                            LogUtils.d("AddressManagerActivity", "list = " + data.toString());
                        }
                        mAdapter.setData(data);
                    }else {

                    }
                }
            }
        });
    }
}
