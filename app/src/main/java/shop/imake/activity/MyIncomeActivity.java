package shop.imake.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.adapter.MyIncomeAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4Mine;
import shop.imake.client.ClientAPI;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.MyIncomeModel;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DoubleTextUtils;
import shop.imake.utils.LoadViewHelper;
import shop.imake.utils.LogUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.widget.NoScrollListView;

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

    private TextView mTvIncomeAvailable;    // 累计收益
    private TextView mTvYongjin;            // 佣金
    private TextView mTvZhonghuiquan;       // 众汇券
//    private TextView mTvAllIncome;          // 累计获得总收益

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
        loadData();
    }

    private void initView() {
        mBackView = findViewById(R.id.left_layout);
        mIncomeDetailView = findViewById(R.id.right_layout);
        mBodyView = findViewById(R.id.my_income_body);

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
                CurrentUserManager.TokenDue(e);
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

                if (response == null) {
                    return;
                }

                MyIncomeModel myIncomeModel = (MyIncomeModel) response;

                // 累计收益
                double usable_push_money = myIncomeModel.getUsable_push_money();
                mTvIncomeAvailable.setText(String.format(Locale.CHINA, "%.2f", usable_push_money));

                // 佣金
                String push_money = myIncomeModel.getPush_money();
                mTvYongjin.setText(DoubleTextUtils.setDoubleUtils(Double.valueOf(push_money)));

                // 众汇券
                double zhonghuiquan = myIncomeModel.getZhonghuiquan();
                mTvZhonghuiquan.setText(String.format(Locale.CHINA, "%.2f", zhonghuiquan));

                // 累计获得收益 去掉了-20170524
//                double all_push_money = myIncomeModel.getAll_push_money();
//                mTvAllIncome.setText(String.format(Locale.CHINA, "%.2f", all_push_money));

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
                gotoDetail();

                break;
        }
    }

    /**
     * 跳转到收益明细页面
     */
    private void gotoDetail() {
        StringBuffer sb=new StringBuffer(ClientAPI.URL_WX_H5);
        sb.append("myshouyi-detail.html");
        sb.append("?pageType=");
        sb.append("shouyi");
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());
        sb.append("&type=android");
        sb.append("&vt=").append(System.currentTimeMillis());

        String webShowUrl=sb.toString().trim();
        LogUtils.e("webShowUrl",webShowUrl);
        WebShowActivity.actionStart(MyIncomeActivity.this,webShowUrl, null);
    }
}
