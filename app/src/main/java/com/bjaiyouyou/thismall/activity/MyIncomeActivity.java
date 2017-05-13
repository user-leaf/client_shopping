package com.bjaiyouyou.thismall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.MyIncomeAdapter;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4Mine;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.MyIncomeModel;
import com.bjaiyouyou.thismall.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 我的收益（众汇）
 */
public class MyIncomeActivity extends Activity implements View.OnClickListener {

    public static final String TAG  = MyIncomeActivity.class.getSimpleName();

    private View mBackView;
    private View mIncomeDetailView;
    private Api4Mine mApi4Mine;

    private TextView mTvAllIncome;  // 累计获得总收益
    private TextView mTvIncomeAvailable;    // 可用收益
    private TextView mTvYongjin;            // 佣金
    private TextView mTvZhonghuiquan;       // 众汇券

    // 列表
    private NoScrollListView mListView;
    private List<MyIncomeModel> mList;
    private MyIncomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income);

        mApi4Mine = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
        mList = new ArrayList<>();

        initView();
        setupView();
        initCtrl();
        loadData();
    }

    private void initView() {
        mBackView = findViewById(R.id.left_layout);
        mIncomeDetailView = findViewById(R.id.right_layout);

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
        mApi4Mine.getMyIncome(TAG, new DataCallback<MyIncomeModel>(this) {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response != null) {
                    return;
                }

                MyIncomeModel myIncomeModel = (MyIncomeModel) response;
                int all_push_money = myIncomeModel.getAll_push_money();
                mTvAllIncome.setText(String.valueOf(all_push_money));

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

                break;

        }
    }
}
