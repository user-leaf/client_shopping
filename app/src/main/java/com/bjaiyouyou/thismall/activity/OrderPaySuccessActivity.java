package com.bjaiyouyou.thismall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.OrderPaySuccessAdapter;
import com.bjaiyouyou.thismall.model.OrderPaySuccessProductInfo;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.bjaiyouyou.thismall.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单支付成功页
 *
 * @author kanbin
 * @date 2016/6/28
 */
public class OrderPaySuccessActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    // 查看订单
    private View mThisOrderView;
    // 返回首页
    private View mTvHomePage;
    // 猜你喜欢商品列表
    private NoScrollGridView mGridView;
    private List<OrderPaySuccessProductInfo> mList;
    private OrderPaySuccessAdapter mAdapter;
    private IUUTitleBar mTitleBar;
    private TextView mTvName;
    private TextView mTvTel;
    private TextView mTvAddress;
    private String mOrderNumber;

    /**
     * 启动本活动
     *
     * @param context
     * @param name
     * @param tel
     * @param address
     * @param orderNumber
     */
    public static void actionStart(Context context, String name, String tel, String address, String orderNumber) {
        Intent intent = new Intent(context, OrderPaySuccessActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("tel", tel);
        intent.putExtra("address",address);
        intent.putExtra("orderNumber",orderNumber);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay_success);

        initVariables();
        initView();
        setupView();
        initData();
        initCtrl();
    }

    private void initVariables() {
        mList = new ArrayList<>();
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.order_pay_success_title_bar);
        mThisOrderView = findViewById(R.id.order_pay_success_rl_this_order);
        mGridView = (NoScrollGridView) findViewById(R.id.order_pay_success_gridview);
        mTvName = (TextView) findViewById(R.id.order_pay_success_tv_name);
        mTvTel = (TextView) findViewById(R.id.order_pay_success_tv_tel);
        mTvAddress = (TextView) findViewById(R.id.order_pay_success_tv_address);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mThisOrderView.setOnClickListener(this);
        mGridView.setOnItemClickListener(this);

        // 设置GridView不获取焦点，使ScrollView起始位置从顶部开始
        mGridView.setFocusable(false);

    }

    private void initData() {
        Intent intent = getIntent();
        mTvName.setText(intent.getStringExtra("name"));
        mTvTel.setText(intent.getStringExtra("tel"));
        mTvAddress.setText(intent.getStringExtra("address"));

        mOrderNumber = intent.getStringExtra("orderNumber");

        // 测试数据
//        for (int i = 0; i < 5; i++) {
//            OrderPaySuccessProductInfo productInfo = new OrderPaySuccessProductInfo();
//            productInfo.setImageUrl("http://pic.58pic.com/58pic/14/00/69/66858PICNfJ_1024.jpg");
//            productInfo.setTitle("四件套"+i);
//            productInfo.setPrice(i+"00.00");
//            productInfo.setPoints(""+i*10);
//            mList.add(productInfo);
//        }

    }

    private void initCtrl() {
        mAdapter = new OrderPaySuccessAdapter(this,mList);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtils.showShort("条目"+position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;
            case R.id.order_pay_success_rl_this_order: // 查看订单
                OrderDetailActivity.actionStart(this, mOrderNumber);
                finish();
                break;
        }
    }
}
