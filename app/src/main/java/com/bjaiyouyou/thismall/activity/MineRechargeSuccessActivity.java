package com.bjaiyouyou.thismall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.RechargeSuccessAdapter;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.RechargeSuccessModel;
import com.bjaiyouyou.thismall.model.User;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 充值成功页
 * 积分和UU的充值成功页
 *
 * User: JackB
 * Date: 2016/8/15
 */
public class MineRechargeSuccessActivity extends BaseActivity implements View.OnClickListener {
    // 数据集
    private List<RechargeSuccessModel> mList;
    // GridView
    private GridView mGridView;
    // 列表适配器
    private RechargeSuccessAdapter mAdapter;
    // 标题栏
    private IUUTitleBar mTitleBar;
    // UU栏
    private View mGoldCoinView;
    // UU
    private TextView mTvGoldCoin;
    // 积分栏
    private View mPointsView;
    // 积分
    private TextView mTvPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_recharge_success);

        initVariables();
        initView();
        setupView();
        initCtrl();
        loadData();

    }

    /**
     * 启动本活动
     *
     * @param context
     * @param type  页面类型
     */
    public static void actionStart(Context context, int type){
        Intent intent = new Intent(context, MineRechargeSuccessActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    private void initVariables() {
        mList = new ArrayList<>();
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.recharge_success_gv);
        mTitleBar = (IUUTitleBar) findViewById(R.id.recharge_success_titlebar);

        mGoldCoinView = findViewById(R.id.recharge_rl_gold_coin);
        mPointsView = findViewById(R.id.recharge_rl_points);

        mTvGoldCoin = (TextView) findViewById(R.id.recharge_success_tv_gold_coin);
        mTvPoints = (TextView) findViewById(R.id.recharge_success_tv_points);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
    }


    private void initCtrl() {
        mAdapter = new RechargeSuccessAdapter(this, mList);
        mGridView.setAdapter(mAdapter);
    }

    private void loadData() {
        // 获取页面类型（0UU、1积分）
        Intent intent = getIntent();
        int type = intent.getIntExtra("type",-1);

        // 根据页面类型显示页面
        showByType(type);


        /**
         * 测试数据
         */
//        mList.clear();
//        for (int i = 0; i < 10; i++) {
//            RechargeSuccessModel model = new RechargeSuccessModel();
//            model.setImageurl("https://www.baidu.com/img/bd_logo1.png");
//            model.setName("百度一下");
//            model.setPrice("100");
//            model.setPoints("10");
//            mList.add(model);
//        }
//        mAdapter.notifyDataSetChanged();

        // 数据请求
        ClientAPI.getMineRechargeSuccessGoldCoin(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showException(e);
            }

            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response) && !"[]".equals(response)){
                    Gson gson = new Gson();
                    User user = gson.fromJson(response, User.class);
                    if (user != null) {
                        User.MemberBean member = user.getMember();
                        if (member != null) {
                            // UU数量
                            long money_quantity = member.getMoney_quantity();
                            mTvGoldCoin.setText(money_quantity+"");

                            // 积分数量
                            long integration = member.getIntegration();
                            mTvPoints.setText(""+integration);
                        }
                    }
                }
            }
        });

    }

    // 根据页面类型(0UU、1积分)显示页面
    private void showByType(int type) {
        switch (type) {
            // UU
            case 0:
                mGoldCoinView.setVisibility(View.VISIBLE);
                mPointsView.setVisibility(View.GONE);
                break;

            // 积分
            case 1:
                mGoldCoinView.setVisibility(View.GONE);
                mPointsView.setVisibility(View.VISIBLE);
                break;

            case -1:
                mGoldCoinView.setVisibility(View.INVISIBLE);
                mPointsView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;
        }
    }
}
