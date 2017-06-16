package shop.imake.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shop.imake.MainApplication;
import shop.imake.R;
import shop.imake.adapter.LogisticsAdapter;
import shop.imake.model.ExpressDetailModel;
import shop.imake.utils.LogUtils;
import shop.imake.widget.IUUTitleBar;

/**
 * 查看物流
 *
 * @author Alice
 *         Creare 2016/10/25 15:24
 */
public class CheckTheLogisticsActivity extends BaseActivity implements View.OnClickListener {
    //到达城市
    private TextView mTVArrivedCity;
    //公司
    private TextView mTVCompany;
    //订单号
    private TextView mTVWayBillNum;
    //总布局
    private ScrollView mScrollView;
    //接收上一页传递过来的对象
    ExpressDetailModel expressDetailModel;
    //订单数据
    List<ExpressDetailModel.DataBean.TracesBean.TraceBean> data;
    //适配器
    private LogisticsAdapter adapter;
    private ListView mLV;
    private LinearLayout mLLHavent;
    private IUUTitleBar mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_the_logistics);
        //获得数据
        expressDetailModel = (ExpressDetailModel) MainApplication.getInstance().getData();
        initView();
        setUpView();
        initData();
    }

    private void initView() {
        mTitle = ((IUUTitleBar) findViewById(R.id.title_check_the_logistics));
        mScrollView = ((ScrollView) findViewById(R.id.ptrsv_check_the_logistics));
        mTVArrivedCity = ((TextView) findViewById(R.id.tv_check_this_logistic_arrived_city));
        mTVCompany = ((TextView) findViewById(R.id.tv_check_this_logistic_logistics_company));
        mTVWayBillNum = ((TextView) findViewById(R.id.tv_check_the_logistics_waybill_number));
        mLV = ((ListView) findViewById(R.id.lv_check_the_logistics));
        mLV.setFocusable(false);
        mLLHavent = ((LinearLayout) findViewById(R.id.ll_check_the_logistics_havent));
    }

    private void setUpView() {
//        mScrollView.setOnRefreshListener(this);
        mTitle.setOnClickListener(this);
    }

    private void initData() {
        data = new ArrayList<>();
        if (expressDetailModel != null) {
            if (expressDetailModel.getData().isResult()) {
                mScrollView.setVisibility(View.VISIBLE);
                mLLHavent.setVisibility(View.GONE);
                data = expressDetailModel.getData().getTraces().getTrace();
                initCtrl();
            } else {
                mLLHavent.setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);
            }
        } else {
            mLLHavent.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);

        }
    }

    private void initCtrl() {
        if (data != null && data.size() != 0) {
            mTVArrivedCity.setText(data.get(0).getStation());
            mTVWayBillNum.setText(expressDetailModel.getData().getMailno());
            adapter = new LogisticsAdapter(data, getApplicationContext());
            LogUtils.e("expressDetailModel count:", adapter.getCount() + "");
            mLV.setAdapter(adapter);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_check_the_logistics:
                finish();
                break;
        }
    }
}
