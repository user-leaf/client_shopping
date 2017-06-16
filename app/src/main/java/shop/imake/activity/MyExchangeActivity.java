package shop.imake.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4ClientOther;
import shop.imake.client.ClientAPI;
import shop.imake.client.ClientApiHelper;
import shop.imake.fragment.ExchangeCertificateFragment;
import shop.imake.fragment.IncomeFragment;
import shop.imake.model.ActivateInfoModel;
import shop.imake.model.User;
import shop.imake.utils.LogUtils;

/**
 * 我的兑换券页面
 * author Alice
 * created at 2017/4/20 18:14
 */
public class MyExchangeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    //返回
    private ImageView mIvBack;
    private RadioGroup mRg;
    //切换按钮集合
    private List<RadioButton> mRbs;
    //碎片集合
    private List<Fragment> mFragments;
    //兑换券按钮
    private RadioButton mRbExchangeCertificate;
    //收益按钮
    private RadioButton mRbIncome;
    // 记录当前是那个Fragment
    private int mCurrent = 0;
    //兑换规则
    private ImageView mTvExchangeRule;
    //用户
    private User mUser;
    private TextView mTvNotHaveIncome;
    //会员类型
    private int mMember_type;
    //测试用户类型
    private int isInTestUser;
    //Vip用户类型
    private int isVip;
    private Api4ClientOther mClient;
    //用户信息对象
    private ActivateInfoModel mDataModel;
    private ActivateInfoModel.UserAboutCashInfoBean mUserAboutCashInfo;
    //用户等级
    private int mUserLeve;
    //是否是Vip用户
    private boolean isVipUser;

    public static String MYEXCHANGE_USER = "myexchange_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_exchange);
        initView();
        setupView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("getExchangeData", "MyExchangeonResume");
        initData();
    }

    /**
     *
     */
    private void initView() {
        //不存在收益权的标题显示
        mTvNotHaveIncome = ((TextView) findViewById(R.id.title_not_have_income));

        mIvBack = ((ImageView) findViewById(R.id.iv_my_exchange_back));
        //兑换规则
        mTvExchangeRule = ((ImageView) findViewById(R.id.tv_my_exchange_rule));
        //切换按钮,存在收益权的标题显示
        mRg = ((RadioGroup) findViewById(R.id.rg_my_exchange));
        mRg.setOnCheckedChangeListener(this);

    }

    /**
     * 创建Fragment
     */
    private void createFragment() {
        mRbs = new ArrayList<>();
        mRbExchangeCertificate = ((RadioButton) findViewById(R.id.rb_exchange));
        mRbs.add(mRbExchangeCertificate);
        mRbIncome = ((RadioButton) findViewById(R.id.rb_income));
        mRbs.add(mRbIncome);

        mFragments = new ArrayList<>();
        ExchangeCertificateFragment exchangeCertificateFragment = new ExchangeCertificateFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(MYEXCHANGE_USER, mUserAboutCashInfo);
        exchangeCertificateFragment.setArguments(bundle);

        mFragments.add(exchangeCertificateFragment);

        IncomeFragment incomeFragment = new IncomeFragment();
        mFragments.add(incomeFragment);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.ll_my_exchange_fragment, mFragments.get(0))
                .add(R.id.ll_my_exchange_fragment, mFragments.get(1)).hide(mFragments.get(1))
                .show(mFragments.get(0))
                .commitAllowingStateLoss();

        /**
         报错Can not perform this action after onSaveInstanceState
         大致意思是说我使用的 commit方法是在Activity的onSaveInstanceState()之后调用的，这样会出错，因为
         onSaveInstanceState方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存玩状态后
         再给它添加Fragment就会出错。解决办法就是把commit（）方法替换成 commitAllowingStateLoss()就行
         了，其效果是一样的。
         */
    }

    private void setupView() {
        mIvBack.setOnClickListener(this);
        mTvExchangeRule.setOnClickListener(this);
    }


    /**
     * 获得数据
     */
    private void initData() {
        ///////////////////////////////获取数据//////////////////////////////
        mClient = (Api4ClientOther) ClientApiHelper.getInstance().getClientApi(Api4ClientOther.class);
        mClient.getExchangeData(new DataCallback<ActivateInfoModel>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                LogUtils.e("getExchangeData", "失败");
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response != null) {
                    mDataModel = (ActivateInfoModel) response;
                    if (mDataModel != null) {
                        mUserAboutCashInfo = mDataModel.getUserAboutCashInfo();
                        if (mUserAboutCashInfo != null) {
                            setData();
                        }
                    }
                }
            }
        });
    }

    /**
     * 处理数据
     */
    private void setData() {
        //用户等级
        mUserLeve = mUserAboutCashInfo.getUser_lever();
        if (mUserLeve == 2 || mUserLeve == 5) {
            isVipUser = true;
        } else {
            isVipUser = false;
        }
        //显示我的收益
        if (isVipUser) {
            mTvNotHaveIncome.setVisibility(View.GONE);
            mRg.setVisibility(View.VISIBLE);
            //不显示我的收益
        } else {
            mTvNotHaveIncome.setVisibility(View.VISIBLE);
            mRg.setVisibility(View.GONE);

        }
        createFragment();

    }


    /**
     * 处理点击事件
     *
     * @param v
     */

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            //返回
            case R.id.iv_my_exchange_back:
                finish();
                break;
            //webView兑换规则跳转
            case R.id.tv_my_exchange_rule:
                intoRule();
                break;
            default:
                return;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        LogUtils.d("checkedId", checkedId + "");
        for (int i = 0; i < group.getChildCount(); i++) {
            if (mRbs.get(i).getId() == checkedId) {
                LogUtils.d("checkedId", i + "--id--" + mRbs.get(i).getId());
                mRbs.get(i).setChecked(true);
                mRbs.get(i).setTextColor(Color.WHITE);
                changeFragment(i);
            } else {
                mRbs.get(i).setChecked(false);
                mRbs.get(i).setTextColor(getResources().getColor(R.color.app_red));

            }
        }

    }

    // 执行替换Fragment的操作
    private void changeFragment(int i) {
        // 拿到事物管理器
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // 判断当前Fragment 是否被添加过
        if (!mFragments.get(i).isAdded()) {
            // 没有被添加过 添加 之后将当前的Fragment隐藏
            ft.add(R.id.ll_my_exchange_fragment, mFragments.get(i)).hide(mFragments.get(mCurrent)).commit();
        } else {
            // 添加过 将目标Fragment 显示 并将当前的Fragment 隐藏
            ft.show(mFragments.get(i)).hide(mFragments.get(mCurrent)).commit();
        }
        // 替换后目标id为 当前的current
        mCurrent = i;

    }

    /**
     * 跳转消息详情
     */
    private void intoRule() {
        StringBuffer sb = new StringBuffer(ClientAPI.URL_WX_H5);
        sb.append("myduihuanquan-role.html");

        String webShowUrl = sb.toString().trim();
        WebShowActivity.actionStart(MyExchangeActivity.this, webShowUrl, null);
    }
}
