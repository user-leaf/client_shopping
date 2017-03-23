package com.bjaiyouyou.thismall.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.MainActivity;
import com.bjaiyouyou.thismall.MainApplication;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.OrderDetailAdapter;
import com.bjaiyouyou.thismall.callback.PingppPayResult;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.AddAlltoCartNew;
import com.bjaiyouyou.thismall.model.ExpressDetailModel;
import com.bjaiyouyou.thismall.model.OrderDetailModel;
import com.bjaiyouyou.thismall.task.PaymentTask;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.AppPackageChecked;
import com.bjaiyouyou.thismall.utils.DialogUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.bjaiyouyou.thismall.widget.NoScrollListView;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * 订单详情页
 *
 * @author kanbin
 * @date 2016/6/21
 * <p/>
 * 6种订单状态（-1数据错误、0待付款、1未发货、2已发货、3申请退款、4已退款、5已收货）都用这个订单详情页，
 * 根据type不同来区分
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, OnItemClickListener {

    public static final String TAG = OrderDetailActivity.class.getSimpleName();
    public static final String PARAM_ORDER_NUMBER = "orderNumber";

    private IUUTitleBar mTitleBar;
    /**
     * 订单详情类型
     * -1：数据错误
     * 0：待付款
     * 1：未发货
     * 2：已发货
     * 3：申请退款
     * 4：已退款
     * 5：已收货
     */
    private int type = -1;

    // 订单状态
    private TextView mTvOrderStatus;
    // 消费可得UU栏
    private View mGoldCoinView;
    // 支付方式栏
    private View mPayView;

    // [我的订单页分栏] - [按钮文字] - [设计图名|状态]
    // 按钮group0 <未付款栏> - <取消订单、立即付款> - <待付款>
    private View mButtonGroup0;
    // 按钮group1 <未完成栏> - <申请退款、确认收货> - <未发货|已发货>（对应设计图名称）
    private View mButtonGroup1;
    // 按钮group2 <已完成栏> - <申请售后、再次购买> - <已完成>
    private View mButtonGroup2;
    // 按钮group3 <未完成栏> - <重新购买、退款详情> - <申请退款>
    private View mButtonGroup3;
    // 按钮group4 <已完成栏> - <删除订单、退款详情> - <已退款>
    private View mButtonGroup4;

    // 列表
    private NoScrollListView mListView;
    private OrderDetailAdapter mAdapter;
    private List<OrderDetailModel.OrderBean.OrderDetailBean> mData;
    // 订单号
    private TextView mTvOrderNumber;
    // 收货地址栏
    private View mAddressView;
    // 收货人姓名、电话、地址
    private TextView mTvName;
    private TextView mTvTel;
    private TextView mTvAddress;

    // 数据
    private String mName;
    private String mTel;
    private String mAddress;

    // 取消订单按钮
    private TextView mTvCancelOrder;
    // 立即付款按钮
    private TextView mTvPayNow;
    // 申请退款按钮
    private TextView mTvRefundApply;
    // 确认收货按钮
    private TextView mTvReceiveOk;
    // 申请售后按钮
    private TextView mTvAfterSale;
    // 再次购买按钮
    private TextView mTvBuyAgain;
    // 未完成分组中的重新购买按钮
    private TextView mTvUncompleteBugAgain;
    // 未完成分组中的退款详情按钮
    private TextView mTvUncompleteReturn;
    // 已完成分组中的订单删除按钮
    private TextView mTvCompleteOrderDelete;
    // 已完成分组中的退款详情按钮
    private TextView mTvCompleteRefundDetail;


    /**
     * 时间，倒计时
     */
    private static long currentMillis = 0;
    private static int hour = -1;
    private static int minute = -1;
    private static int second = -1;
    private TextView timeView;
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            // 弃用
            // http://www.cnblogs.com/dyllove98/archive/2013/06/25/3155614.html
//            showTimeWithHour();
//            showTimeWithoutHour();

            showTime();
        }
    };

    // 订单编号
    private String mOrderNumber;

    // 商品重量
//    private TextView mTvWeight;
    // 配送方式
//    private TextView mTvExpressCompany;
    // 运费
    private TextView mTvPostage;
    // 本次消费可获得UU
    private TextView mTvGoldCoin;

    // 下单时间
    private TextView mTvCreateTime;

    // 实付金额
    private TextView mTvMoney;
    // 实付积分
    private TextView mTvPoints;

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    // 销毁本页
                    OrderDetailActivity.this.finish();
                    break;
            }
        }
    };

    // 运单号栏
    private View mExpress2View;
    private TextView mTvExpress2Number;
    private TextView mTvExpress2Content;
    private TextView mTvExpress2Time;
    private View mExpress2JumpView;
    // 运单数据
    private ExpressDetailModel mExpressDetailModel;
    // 是否有物流信息
    private boolean hasExpressDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        initVariables();
        initView();
        setupView();
//        setTime();
        initCtrl();
        loadData();

    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask = null;
        }
        hour = -1;
        minute = -1;
        second = -1;
        super.onDestroy();
    }

    /**
     * 启动本活动
     *
     * @param context
     */
    public static void actionStart(Context context, String orderNumber) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(PARAM_ORDER_NUMBER, orderNumber); // 订单号
        context.startActivity(intent);
    }

    private void initVariables() {
        mData = new ArrayList<>();

        Intent intent = getIntent();
        // 获取订单号
        mOrderNumber = intent.getStringExtra(PARAM_ORDER_NUMBER);

    }

    private void initView() {
        mTvOrderStatus = (TextView) findViewById(R.id.order_detail_order_status);
        mGoldCoinView = findViewById(R.id.order_detail_rl_gold_coin);
        mPayView = findViewById(R.id.order_detail_rl_pay);

        mButtonGroup0 = findViewById(R.id.order_detail_rl_buttongroup0);
        mButtonGroup1 = findViewById(R.id.order_detail_rl_buttongroup1);
        mButtonGroup2 = findViewById(R.id.order_detail_rl_buttongroup2);
        mButtonGroup3 = findViewById(R.id.order_detail_rl_buttongroup3);
        mButtonGroup4 = findViewById(R.id.order_detail_rl_buttongroup4);

        mTitleBar = (IUUTitleBar) findViewById(R.id.order_detail_title_bar);
        mTvOrderNumber = (TextView) findViewById(R.id.order_detail_order_number);
        mListView = (NoScrollListView) findViewById(R.id.order_detail_listview);
        mAddressView = findViewById(R.id.order_detail_address);
        mTvName = (TextView) findViewById(R.id.order_detail_tv_name);
        mTvTel = (TextView) findViewById(R.id.order_detail_tv_tel);
        mTvAddress = (TextView) findViewById(R.id.order_detail_tv_address);
//        mTvWeight = (TextView) findViewById(R.id.order_detail_tv_weight);
//        mTvExpressCompany = (TextView) findViewById(R.id.order_detail_tv_express_way);
        mTvPostage = (TextView) findViewById(R.id.order_detail_tv_postage);

        mTvGoldCoin = (TextView) findViewById(R.id.order_detail_tv_goldcoin);
        mTvMoney = (TextView) findViewById(R.id.order_detail_tv_money);
        mTvPoints = (TextView) findViewById(R.id.order_detail_tv_points);

        mTvCancelOrder = (TextView) findViewById(R.id.order_detail_tv_cancel_order);
        mTvPayNow = (TextView) findViewById(R.id.order_detail_tv_pay_now);
        mTvRefundApply = (TextView) findViewById(R.id.order_detail_tv_refund_apply);
        mTvReceiveOk = (TextView) findViewById(R.id.order_detail_tv_receipt_ok);
        mTvAfterSale = (TextView) findViewById(R.id.order_detail_tv_after_sale);
        mTvBuyAgain = (TextView) findViewById(R.id.order_detail_tv_buy_again);

        mTvUncompleteBugAgain = (TextView) findViewById(R.id.order_detail_tv_uncomplete_buy_again);
        mTvUncompleteReturn = (TextView) findViewById(R.id.order_detail_tv_uncomplete_refund_detail);
        mTvCompleteOrderDelete = (TextView) findViewById(R.id.order_detail_tv_complete_order_delete);
        mTvCompleteRefundDetail = (TextView) findViewById(R.id.order_detail_tv_complete_refund_detail);

        timeView = (TextView) findViewById(R.id.order_detail_tv_time_count);
        mTvCreateTime = (TextView) findViewById(R.id.order_detail_tv_create_time);

        mExpress2View = findViewById(R.id.order_detail_ll_express2);
        mTvExpress2Number = (TextView) findViewById(R.id.order_detail_tv_express2_number);
        mTvExpress2Content = (TextView) findViewById(R.id.order_detail_tv_express2_content);
        mTvExpress2Time = (TextView) findViewById(R.id.order_detail_tv_express2_time);
        mExpress2JumpView = findViewById(R.id.order_detail_iv_express2_jump);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mAddressView.setOnClickListener(this);
        // 设置ListView不获取焦点，使ScrollView起始位置从顶部开始
        mListView.setFocusable(false);
        mListView.setOnItemClickListener(this);

        mTvCancelOrder.setOnClickListener(this);
        mTvPayNow.setOnClickListener(this);
        mTvRefundApply.setOnClickListener(this);
        mTvReceiveOk.setOnClickListener(this);
        mTvAfterSale.setOnClickListener(this);
        mTvBuyAgain.setOnClickListener(this);
        mTvUncompleteBugAgain.setOnClickListener(this);
        mTvUncompleteReturn.setOnClickListener(this);
        mTvCompleteOrderDelete.setOnClickListener(this);
        mTvCompleteRefundDetail.setOnClickListener(this);

        mExpress2View.setOnClickListener(this);
    }

    private void loadData() {

//        // 订单失效时间
//        hour = 3;
//        minute = 0;
//        second = 0;

        showLoadingDialog();

        ClientAPI.getOrderDetail(TAG, mOrderNumber, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dismissLoadingDialog();
            }

            @Override
            public void onResponse(String response, int id) {

                if (!TextUtils.isEmpty(response) && !"[]".equals(response)) {
                    Gson gson = new Gson();
                    OrderDetailModel orderDetailModel = gson.fromJson(response, OrderDetailModel.class);
                    if (orderDetailModel != null) {
                        OrderDetailModel.OrderBean order = orderDetailModel.getOrder();
                        if (order != null) {
                            // 订单编号
                            mTvOrderNumber.setText(order.getOrder_number());
                            // 地址
                            mName = order.getAddressee();
                            mTel = order.getPhone();
                            mAddress = order.getAddress();

                            mTvAddress.setText(order.getAddress());
                            mTvName.setText(order.getAddressee());
                            mTvTel.setText(order.getPhone());

                            // 运单
                            mTvExpress2Number.setText("" + order.getWaybill_number());

                            // 商品列表
                            List<OrderDetailModel.OrderBean.OrderDetailBean> order_detail = order.getOrder_detail();
                            // 购物列表
                            mData.addAll(order_detail);
                            mAdapter.setData(order_detail);

                            // 商品重量
                            if (order_detail != null) {

                                double sumWeight = 0;
                                for (OrderDetailModel.OrderBean.OrderDetailBean good : order_detail) {
                                    OrderDetailModel.OrderBean.OrderDetailBean.ProductSizeBean product_size = good.getProduct_size();

                                    if (product_size != null) {

                                        String weight1 = product_size.getWeight();
                                        int sales_volume = good.getNumber();
                                        if (weight1 != null) {
                                            double weight = Double.parseDouble(weight1);
                                            sumWeight += (weight * sales_volume);

                                        }
                                    }
                                }

                                LogUtils.d(TAG, "sumWeight: " + sumWeight);
                                // 保留2位小数
//                                        mTvWeight.setText("" + MathUtil.round(sumWeight, 2) + "公斤");

                            }

                            // 运费
                            mTvPostage.setText(order.getPostage() + "元");

                            // 实付金额栏
                            mTvMoney.setText("¥" + order.getAll_amount());
                            mTvPoints.setText("+" + order.getDeduct_integration() + "积分");

                            // 本次消费可获得UU
                            mTvGoldCoin.setText(order.getGet_gold() + "UU");

                            // 下单时间
                            mTvCreateTime.setText(order.getCreated_at());
                            // 订单状态
                            mTvOrderStatus.setText(order.getShow_state_msg());
                            // 根据页面类型显示相应布局
                            type = order.getShow_state();
                            showByType(type);
                        }
                    }
                }

                dismissLoadingDialog();
            }
        });

        /**
         * 获取物流信息
         * 应该只有当不是未付款状态的时候才请求物流接口
         * 但那样延迟太高，显示效果不好
         */
        ClientAPI.getOrderDetailExpressDetail(mOrderNumber, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mTvExpress2Number.setText("无");
                mTvExpress2Content.setText("物流信息获取失败");
                mTvExpress2Time.setVisibility(View.GONE);
                mExpress2JumpView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onResponse(String response, int id) {
                if (response != null && "[]" != response) {
                    Gson gson = new Gson();
                    mExpressDetailModel = gson.fromJson(response, ExpressDetailModel.class);
                    ExpressDetailModel.DataBean data = mExpressDetailModel.getData();
                    if (data != null) {
                        // 使用订单详情接口中的运单号，因为一箱可能装不下，导致一个订单对应多个运单，所以以订单详情接口中的那个为准即可
//                        mTvExpress2Number.setText(data.getMailno());

                        if (data.isResult()) { // 有物流信息
                            hasExpressDate = true;
                            ExpressDetailModel.DataBean.TracesBean traces = data.getTraces();

                            if (traces != null) {
                                List<ExpressDetailModel.DataBean.TracesBean.TraceBean> traceBeanList = traces.getTrace();

                                if (traceBeanList != null) {
                                    ExpressDetailModel.DataBean.TracesBean.TraceBean traceBean = traceBeanList.get(0);

                                    if (traceBean != null) {
                                        mTvExpress2Content.setText("[" + traceBean.getStation() + "]" + traceBean.getRemark());
                                        mTvExpress2Time.setText(traceBean.getTime());
                                    }
                                }
                            }
                        } else { // 无物流信息
                            String remark = data.getRemark();
                            mTvExpress2Content.setText(remark);
                            mTvExpress2Time.setVisibility(View.GONE);
                            mExpress2JumpView.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });
    }

    private void initCtrl() {
        mAdapter = new OrderDetailAdapter(this, mData, type);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 根据type不同显示不同UI
     *
     * @param type 页面类型
     */
    public void showByType(int type) {
        switch (type) {
            case 0: // 未付款栏 - 取消订单、立即付款 // 待付款
                // 预计到达时间
                mButtonGroup0.setVisibility(View.VISIBLE);
                mExpress2View.setVisibility(View.GONE);
                break;

            case 1: // 未完成栏 - 申请退款、确认收货 // 未发货（对应设计图名称）
                mButtonGroup1.setVisibility(View.VISIBLE);
                // 去掉右上角的显示；隐藏右下角的确认收货按钮
                mTvOrderStatus.setVisibility(View.INVISIBLE);
                mTvReceiveOk.setVisibility(View.GONE);
                mExpress2View.setVisibility(View.GONE);
                break;
            case 2: // 未完成栏 - 申请退款、确认收货 // 已发货
                mButtonGroup1.setVisibility(View.VISIBLE);
                // 已发货状态去掉右上角的显示
                mTvOrderStatus.setVisibility(View.GONE);
                mExpress2View.setVisibility(View.VISIBLE);
                break;

            case 3: // 未完成栏 - 重新购买、退款详情 // 申请退款
                mButtonGroup3.setVisibility(View.VISIBLE);
                mExpress2View.setVisibility(View.GONE);
                break;

            case 4: // 已完成栏 - 删除订单、退款详情 // 已退款
                mButtonGroup4.setVisibility(View.VISIBLE);
                mExpress2View.setVisibility(View.GONE);
                break;

            case 5: // 已完成栏 - 申请售后、再次购买 //已完成
                mButtonGroup2.setVisibility(View.VISIBLE);
                mExpress2View.setVisibility(View.VISIBLE);
                break;

            default:
                ToastUtils.showShort("订单错误，请稍后重试");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;

            //=============【待付款状态】=========================
            // 取消订单按钮
            case R.id.order_detail_tv_cancel_order:
                Dialog cancelDialog = DialogUtils.createConfirmDialog(this, null, "取消此订单？", "确认", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelOrder();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
                cancelDialog.show();
                break;

            // 立即付款按钮
            case R.id.order_detail_tv_pay_now:
                // 调用ping++付款
                doPayByPingpp();
                break;

            //=============【未发货|已发货状态】=========================
            // 申请退款按钮
            case R.id.order_detail_tv_refund_apply:
                // 跳转到申请退款页
                Intent intent = new Intent(this, OrderReturnActivity.class);
                MainApplication.getInstance().setData(mData);
                intent.putExtra(OrderReturnActivity.PARAM_ORDER_NUMBER, mOrderNumber);
                startActivity(intent);
                break;

            // 确认收货按钮
            case R.id.order_detail_tv_receipt_ok:
                Dialog confirmDialog = DialogUtils.createConfirmDialog(this, null, "确认收货后不能恢复，是否继续？", "确认", "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ClientAPI.confirmGoodsReceipt(mOrderNumber, new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        e.printStackTrace();
                                        ToastUtils.showException(e);
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        finish();
                                    }
                                });
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }

                );
                confirmDialog.show();
                break;

            //=============【已完成状态】=========================
            // 申请售后按钮
            case R.id.order_detail_tv_after_sale: // 去掉了
                ToastUtils.showShort("申请售后");
                break;

            // 再次购买按钮
            case R.id.order_detail_tv_buy_again:
                buyAgain();
                break;

            //=============【申请退款状态】=========================
            // 重新购买按钮
            case R.id.order_detail_tv_uncomplete_buy_again:
                buyAgain();
                break;

            // 退款详情按钮
            case R.id.order_detail_tv_uncomplete_refund_detail:
                doReturn();
                break;

            //=============【已退款状态】=========================
            // 删除订单按钮
            case R.id.order_detail_tv_complete_order_delete:
                Dialog deleteDialog = DialogUtils.createConfirmDialog(this, null, "确认删除订单？", "确认", "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ClientAPI.deleteOrder(mOrderNumber, new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        e.printStackTrace();
                                        ToastUtils.showException(e);
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        finish();
                                    }
                                });
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }

                );
                deleteDialog.show();
                break;

            // 退款详情按钮
            case R.id.order_detail_tv_complete_refund_detail:
                doReturn();
                break;

            //=======================================================

            // 运单号栏
            case R.id.order_detail_ll_express2:
                if (hasExpressDate) { // 有物流信息
                    // 传递运单数据mExpressDetailModel
                    MainApplication.getInstance().setData(mExpressDetailModel);
                    jump(CheckTheLogisticsActivity.class, false);
                }
                break;
        }
    }

    /**
     * 退款
     */
    private void doReturn() {
        Intent returnIntent = new Intent(this, OrderReturnDealActivity.class);
        returnIntent.putExtra(OrderReturnDealActivity.PARAM_ORDER_NUMBER, mOrderNumber);
        startActivity(returnIntent);
    }

    /**
     * 再次购买
     */
    private void buyAgain() {

        List<AddAlltoCartNew> newProducts = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < mData.size(); i++) {
            AddAlltoCartNew product = new AddAlltoCartNew();
            product.setProduct_id(mData.get(i).getProduct_id());
            product.setNumber(mData.get(i).getNumber());
            product.setProduct_size_id(mData.get(i).getProduct_size_id());
            newProducts.add(product);
        }
        //提交的Json字符串
        final String jsonString = gson.toJson(newProducts);

        //网络添加
        final String url = ClientAPI.API_POINT + "api/v1/shoppingCart/addAll"
                + "?product_list="
                + jsonString
                + "&token="
                + CurrentUserManager.getUserToken();
        LogUtils.e("url", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        UNNetWorkUtils.unNetWorkOnlyNotify(context);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        /**
                         * 商品添加到购物车后，跳转到购物车页
                         * @see com.bjaiyouyou.thismall.adapter.MyOrderAdapter
                         */
                        Intent mainIntent = new Intent(OrderDetailActivity.this, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mainIntent.putExtra(MainActivity.PARAM_ORDER, "order");
                        startActivity(mainIntent);

                        finish();

                    }
                });
    }

    /**
     * 取消订单
     */
    private void cancelOrder() {
        String url = ClientAPI.API_POINT + "api/v1/order/cancel/"
                + mOrderNumber
                + "?token="
                + CurrentUserManager.getUserToken();
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        UNNetWorkUtils.unNetWorkOnlyNotify(context);
                        ToastUtils.showShort("订单取消失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        finish();
                    }

                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 跳转到商品详情页，传productID过去
        Intent intent = new Intent(this, GoodsDetailsActivity.class);
        intent.putExtra(GoodsDetailsActivity.PARAM_PRODUCT_ID, mData.get(position).getProduct_id());
        startActivity(intent);
    }

    /**
     * 订单失效倒计时
     */
    private void setTime() {
        /**
         * 初始化
         */
        currentMillis = System.currentTimeMillis();

        if (hour == -1 && minute == -1 && second == -1) {
            hour = 2;
            minute = 1;
            second = 3;
        }

        timeView.setText(hour + ":" + minute + ":" + second);

        timerTask = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 显示订单失效时间
     */
    private void showTime() {
        currentMillis -= 1000; // 每秒减1000毫秒
        if (currentMillis <= 0) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(currentMillis);
        timeView.setText(time);
    }

    // 调用ping++去付款
    private void doPayByPingpp() {

        // https://github.com/saiwu-bigkoo/Android-AlertView
//        new AlertView("选择支付方式", null, "取消", null, new String[]{"微信支付", "支付宝支付"}, this, AlertView.Style.ActionSheet, this).show();
        new AlertView("选择支付方式", null, "取消", null, new String[]{"微信支付"}, this, AlertView.Style.ActionSheet, this).show();

    }

    // https://github.com/saiwu-bigkoo/Android-AlertView
    @Override
    public void onItemClick(Object o, int position) {
        final int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
        String channel = "";
        switch (position) {
            case 0: // 微信支付
//                channel = Constants.CHANNEL_WECHAT;
                new PaymentTask(OrderDetailActivity.this, OrderDetailActivity.this, mOrderNumber, Constants.CHANNEL_WECHAT, mTvPayNow, TAG).execute(new PaymentTask.PaymentRequest(Constants.CHANNEL_WECHAT, amount));
                break;

            case 1: // 支付宝支付
                channel = Constants.CHANNEL_ALIPAY;
                break;
        }
    }

//    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {
//
//        // 订单号
//        private String orderNo;
//        // 支付方式
//        private String channel;
//
//        public PaymentTask(String orderNo, String channel) {
//            this.orderNo = orderNo;
//            this.channel = channel;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            loadingDialog.show();
//            //按键点击之后的禁用，防止重复点击
//            mTvPayNow.setOnClickListener(null);
//        }
//
//        @Override
//        protected String doInBackground(PaymentRequest... pr) {
//
//            PaymentRequest paymentRequest = pr[0];
//            String data = null;
//            String json = new Gson().toJson(paymentRequest);
//            try {
//                //向Your Ping++ Server SDK请求数据
////                String URL = Constants.PingppURL+"?token="+CurrentUserManager.getUserToken() + "&orderNo=" + orderNo;
//
//                LogUtils.d(TAG, "orderNo = " + orderNo + ", channel = " + channel);
//
//                StringBuilder sb = new StringBuilder(Constants.PingppURL);
//                sb.append("?token=").append(CurrentUserManager.getUserToken());
//                sb.append("&orderNo=").append(orderNo);
//                sb.append("&channel=").append(channel);
//                String URL = sb.toString();
//
//                data = postJson(URL, json);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return data;
//        }
//
//        /**
//         * 获得服务端的charge，调用ping++ sdk。
//         */
//        @Override
//        protected void onPostExecute(String data) {
//            if (null == data) {
//                showMsg("请求出错", "请检查URL", "URL无法获取charge");
//                return;
//            }
//            Log.d("charge", data);
////            Pingpp.createPayment(ClientSDKActivity.this, data);
//            //QQ钱包调起支付方式  “qwalletXXXXXXX”需与AndroidManifest.xml中的data值一致
//            //建议填写规则:qwallet + APP_ID
//            Pingpp.createPayment(OrderDetailActivity.this, data, "qwalletXXXXXXX");
//        }
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        loadingDialog.dismiss();
        mTvPayNow.setOnClickListener(OrderDetailActivity.this);

        PingppPayResult.setOnPayResultCallback(requestCode, resultCode, data, new PingppPayResult.OnPayResultCallback() {
            @Override
            public void onPaySuccess() {
                OrderPaySuccessActivity.actionStart(OrderDetailActivity.this, mName, mTel, mAddress, mOrderNumber);
                mHandler.sendEmptyMessage(0);

            }

            @Override
            public void onPayFail() {
                Intent intent = new Intent(OrderDetailActivity.this, OrderPayFailActivity.class);
                intent.putExtra(OrderPayFailActivity.PARAM_ORDER_NUMBER, mOrderNumber);
                startActivity(intent);
                mHandler.sendEmptyMessage(0);

            }
        });

    }

//    public void showMsg(String title, String msg1, String msg2) {
//        String str = title;
//        if (null != msg1 && msg1.length() != 0) {
//            str += "\n" + msg1;
//        }
//        if (null != msg2 && msg2.length() != 0) {
//            str += "\n" + msg2;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
//        builder.setMessage(str);
//        builder.setTitle("提示");
//        builder.setPositiveButton("OK", null);
//        builder.create().show();
//    }
//
//    private static String postJson(String url, String json) throws IOException {
//        MediaType type = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(type, json);
//        // post方式
//        Request request = new Request.Builder().url(url).post(body).build();
//
//        // get方式
////        Log.d("OrderDetailActivity: ", url);
////        Request request = new Request.Builder().url(url).build();
//
//        OkHttpClient client = new OkHttpClient();
//        Response response = client.newCall(request).execute();
//
////        LogUtils.d(TAG, "response.body() = " + response.body().string()); // 这句代码会导致，再次获取response.body().string()时拿不到，下一句return null！！
////        String responseBody = response.body().string();
////        return responseBody;
////        return "{\"id\":\"ch_ezPGWP0GaXbDDS0ybL8KWDO8\",\"object\":\"charge\",\"created\":1473146951,\"livemode\":true,\"paid\":false,\"refunded\":false,\"app\":\"app_SynjLKu1Si5Czrnz\",\"channel\":\"wx\",\"order_no\":\"2016090550535349\",\"client_ip\":\"106.39.193.120\",\"amount\":1,\"amount_settle\":1,\"currency\":\"cny\",\"subject\":\"orderNo : 2016090550535349\",\"body\":\"adsfadf\",\"extra\":[],\"time_paid\":null,\"time_expire\":1473154151,\"time_settle\":null,\"transaction_no\":null,\"refunds\":{\"object\":\"list\",\"url\":\"/v1/charges/ch_ezPGWP0GaXbDDS0ybL8KWDO8/refunds\",\"has_more\":false,\"data\":[]},\"amount_refunded\":0,\"failure_code\":null,\"failure_msg\":null,\"metadata\":[],\"credential\":{\"object\":\"credential\",\"wx\":{\"appId\":\"wxa4650166adbfdcc1\",\"partnerId\":\"1383715402\",\"prepayId\":\"wx20160906152912ba7e15ede20721592606\",\"nonceStr\":\"5be48dae595399ea42e479c679c26aa8\",\"timeStamp\":1473146952,\"packageValue\":\"Sign=WXPay\",\"sign\":\"5384B61FE9EA36D6D41CCC9A9D7B5816\"}},\"description\":null}";
//        return response.body().string();
//    }
//
//    class PaymentRequest {
//        String channel;
//        int amount;
//
//        public PaymentRequest(String channel, int amount) {
//            this.channel = channel;
//            this.amount = amount;
//        }
//    }

}
