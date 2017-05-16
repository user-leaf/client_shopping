package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.MyIncomeAdapter;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4Mine;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.MyIncomeModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LoadViewHelper;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 我的收益（众汇）
 *
 * Created by JackB on 2017/5/12.
 */
public class MyIncomeActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG  = MyIncomeActivity.class.getSimpleName();

    private View mBackView;
    private View mIncomeDetailView;         // 收益明细
    private View mBodyView;
    private View mBottomView;

    private TextView mTvIncomeAvailable;    // 可用收益
    private TextView mTvYongjin;            // 佣金
    private TextView mTvZhonghuiquan;       // 众汇券
    private TextView mTvAllIncome;          // 累计获得总收益

    // 列表
    private NoScrollListView mListView;
    private List<MyIncomeModel.PushMoneyDetailsBean> mList;
    private MyIncomeAdapter mAdapter;

    private Api4Mine mApi4Mine;
    private LoadViewHelper mLoadViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income);

        mApi4Mine = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
        mList = new ArrayList<>();

        initView();
        setupView();
        initCtrl();

        mLoadViewHelper = new LoadViewHelper(mBodyView);
        mLoadViewHelper.showLoading();
        mBottomView.setVisibility(View.GONE);
        loadData();
    }

    private void initView() {
        mBackView = findViewById(R.id.left_layout);
        mIncomeDetailView = findViewById(R.id.right_layout);
        mBodyView = findViewById(R.id.my_income_body);
        mBottomView = findViewById(R.id.my_income_bottom_banner);

        mTvAllIncome = (TextView) findViewById(R.id.my_income_tv_all_push_money);
        mTvIncomeAvailable = (TextView) findViewById(R.id.my_income_tv_income_available);
        mTvYongjin = (TextView) findViewById(R.id.my_income_tv_yongjin);
        mTvZhonghuiquan = (TextView) findViewById(R.id.my_income_tv_zhonghuiquan);

        mListView = (NoScrollListView) findViewById(R.id.my_income_listview);
    }

    private void setupView() {
        mBackView.setOnClickListener(this);
        mIncomeDetailView.setOnClickListener(this);
    }

    private void initCtrl() {
        mAdapter = new MyIncomeAdapter(this, mList);
        mListView.setAdapter(mAdapter);
    }

    private void loadData() {
        mApi4Mine.getMyIncome(this, new DataCallback<MyIncomeModel>(this) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                mLoadViewHelper.showError(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadData();
                    }
                });

                ToastUtils.showException(e);
                LogUtils.d(TAG, "loadData error: " + e.getMessage());
            }

            @Override
            public void onSuccess(Object response, int id) {
                mLoadViewHelper.restore();
                mBottomView.setVisibility(View.VISIBLE);

                if (response == null) {
                    return;
                }

                MyIncomeModel myIncomeModel = (MyIncomeModel) response;

                // 可用收益
                int usable_push_money = myIncomeModel.getUsable_push_money();
                mTvIncomeAvailable.setText(String.valueOf(usable_push_money) + ".00");

                // 佣金
                String push_money = myIncomeModel.getPush_money();
                mTvYongjin.setText(push_money + "元");

                // 众汇券
                int zhonghuiquan = myIncomeModel.getZhonghuiquan();
                mTvZhonghuiquan.setText(String.valueOf(zhonghuiquan) + ".00元");

                // 累计获得收益
                int all_push_money = myIncomeModel.getAll_push_money();
                mTvAllIncome.setText(String.valueOf(all_push_money) + ".00");

                // 显示列表
                List<MyIncomeModel.PushMoneyDetailsBean> push_money_details = myIncomeModel.getPush_money_details();
                mList.clear();
                mList.addAll(push_money_details);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:  // 返回
                finish();
                break;

            case R.id.right_layout: // 收益明细
                intoDetail();

                break;
        }
    }
    /**
     * 跳转收益明细详情
     */
    private void intoDetail() {
        StringBuffer sb=new StringBuffer(ClientAPI.URL_WX_H5);
        sb.append("myshouyi-detail.html");
        sb.append("?pageType=");
        sb.append("shouyi");
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());
        sb.append("&type=android");

        String webShowUrl=sb.toString().trim();
        WebShowActivity.actionStart(MyIncomeActivity.this,webShowUrl, null);
    }
}
