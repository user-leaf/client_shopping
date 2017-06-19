package shop.imake.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import shop.imake.MainActivity;
import shop.imake.MainApplication;
import shop.imake.R;
import shop.imake.adapter.OrderDetailAdapter;
import shop.imake.callback.PingppPayResult;
import shop.imake.client.ClientAPI;
import shop.imake.fragment.PayDetailFragment;
import shop.imake.model.AddAlltoCartNew;
import shop.imake.model.ExpressDetailModel;
import shop.imake.model.OrderDetailModel;
import shop.imake.model.PayResultEvent;
import shop.imake.task.PaymentTask;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DialogUtils;
import shop.imake.utils.DoubleTextUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.PayUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.widget.IUUTitleBar;
import shop.imake.widget.NoScrollListView;

/**
 * 订单详情页
 *
 * @author JackB
 * @date 2016/6/21
 * <p/>
 * 6种订单状态（-1数据错误、0待付款、1未发货、2已发货、3申请退款、4已退款、5已收货）都用这个订单详情页，
 * 根据type不同来区分
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, OnItemClickListener {

    public static final String TAG = OrderDetailActivity.class.getSimpleName();
    public static final String PARAM_ORDER_NUMBER = "orderNumber";

    private IUUTitleBar mTitleBar;

    private double mAllAmount;  // 订单总额

    // 订单状态
    private TextView mTvOrderStatus;
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

    private TextView mTvName;
    private TextView mTvTel;
    private TextView mTvAddress;

    private String mStrName;
    private String mStrTel;
    private String mStrAddress;

    // 底部栏
    private TextView mTvCancelOrder;    // 取消订单按钮
    private TextView mTvPayNow;         // 立即付款按钮
    private TextView mTvRefundApply;    // 申请退款按钮
    private TextView mTvReceiveOk;      // 确认收货按钮
    private TextView mTvAfterSale;      // 申请售后按钮
    private TextView mTvBuyAgain;       // 再次购买按钮
    private TextView mTvUncompleteBugAgain;     // 未完成分组中的重新购买按钮
    private TextView mTvUncompleteReturn;       // 未完成分组中的退款详情按钮
    private TextView mTvCompleteOrderDelete;    // 已完成分组中的订单删除按钮
    private TextView mTvCompleteRefundDetail;   // 已完成分组中的退款详情按钮

    private String mOrderNumber;    // 订单编号
    private TextView mTvPostage;    // 运费
    private TextView mTvUU;         // 本次消费可获得UU
    private TextView mTvCreateTime; // 下单时间
    private TextView mTvMoney;      // 实付金额
    private TextView mTvJifen;     // 实付众汇券

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
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

        EventBus.getDefault().register(this);

        initVariables();
        initView();
        setupView();
//        setTime();
        initCtrl();
        loadData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

//        mTvUU = (TextView) findViewById(R.id.order_detail_tv_goldcoin);
        mTvMoney = (TextView) findViewById(R.id.order_detail_tv_money);
        mTvJifen = (TextView) findViewById(R.id.order_detail_tv_points);

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

//        timeView = (TextView) findViewById(R.id.order_detail_tv_time_count);
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

        showLoadingDialog();

        ClientAPI.getOrderDetail(TAG, mOrderNumber, new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
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
                            mStrName = order.getAddressee();
                            mStrTel = order.getPhone();
                            mStrAddress = order.getAddress();

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

                            // 运费
                            mTvPostage.setText("¥" + DoubleTextUtils.setDoubleUtils(order.getPostage()));

                            // 实付金额栏
                            mAllAmount = order.getAll_amount();
                            mTvMoney.setText("¥" + DoubleTextUtils.setDoubleUtils(mAllAmount));

                            // 只显示价格20170513
//                            mTvJifen.setText("+" + order.getDeduct_integration() + "众汇券");
//                            // 本次消费可获得UU
//                            mTvUU.setText(order.getGet_gold() + "UU");

                            // 下单时间
                            mTvCreateTime.setText(order.getCreated_at());
                            // 订单状态
                            mTvOrderStatus.setText(order.getShow_state_msg());
                            // 根据页面类型显示相应布局
                            showByStatus(order.getShow_state());
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
                CurrentUserManager.TokenDue(e);
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
        mAdapter = new OrderDetailAdapter(this, mData);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 根据订单状态不同显示不同UI
     * <p>
     * 订单状态
     * -1：数据错误
     * 0：待付款
     * 1：未发货
     * 2：已发货
     * 3：申请退款
     * 4：已退款
     * 5：已收货
     *
     * @param status 页面类型
     */
    public void showByStatus(int status) {
        switch (status) {
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
                payNow();
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
                                        CurrentUserManager.TokenDue(e);
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
                                        CurrentUserManager.TokenDue(e);
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
     * 退款详情
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
                        CurrentUserManager.TokenDue(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        /**
                         * 商品添加到购物车后，跳转到购物车页
                         * @see shop.imake.adapter.MyOrderAdapter
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
                        CurrentUserManager.TokenDue(e);
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

    private void payNow() {
//        new AlertView(
//                "选择支付方式",
//                null,
//                "取消",
//                null,
//                new String[]{getString(R.string.pay_alipay), getString(R.string.pay_balance), getString(R.string.pay_hx)},
//                this,
//                AlertView.Style.ActionSheet, this
//        ).show();

        PayUtils.pay(getSupportFragmentManager(), TAG, mAllAmount, new PayDetailFragment.PayCallback() {
            @Override
            public void onPayCallback(String channel) {
                int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
                new PaymentTask(
                        OrderDetailActivity.this,
                        OrderDetailActivity.this,
                        mOrderNumber,
                        channel,
                        mTvPayNow,
                        TAG
                ).execute(new PaymentTask.PaymentRequest(channel, amount));

            }
        });

    }

//    @Override
//    public void onItemClick(Object o, int position) {
//        super.onItemClick(o, position);
//
//        if (position < 0) {
//            return;
//        }
//
//        switch (position) {
//            case 0: // 支付宝支付
//                int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
//                String channel = Constants.CHANNEL_ALIPAY;
//                new PaymentTask(OrderDetailActivity.this, OrderDetailActivity.this, mOrderNumber, channel, mTvPayNow, TAG).execute(new PaymentTask.PaymentRequest(channel, amount));
//                break;
//
//            case 1: // 余额支付
//                break;
//
//            case 2: // 环迅支付
//                break;
//
//            default:
//                break;
//        }
//
//    }

    /**
     * 支付成功后的操作
     */
    private void paySuccess() {
        OrderPaySuccessActivity.actionStart(OrderDetailActivity.this, mStrName, mStrTel, mStrAddress, mOrderNumber);
        mHandler.sendEmptyMessage(0);
    }

    /**
     * 余额支付后的“回调”
     *
     * @param event
     */
    @Subscribe
    public void onBalancePayEvent(PayResultEvent event) {
        if (event.isPaySuccess()) {
            paySuccess();
        }
    }

    /**
     * 第三方支付后的“回调”
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        loadingDialog.dismiss();
        mTvPayNow.setOnClickListener(OrderDetailActivity.this);

        PingppPayResult.setOnPayResultCallback(requestCode, resultCode, data, new PingppPayResult.OnPayResultCallback() {
            @Override
            public void onPaySuccess() {
                paySuccess();
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
}
