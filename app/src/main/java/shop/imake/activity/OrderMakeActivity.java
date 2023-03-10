package shop.imake.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import shop.imake.ActivityCollector;
import shop.imake.Constants;
import shop.imake.MainApplication;
import shop.imake.R;
import shop.imake.adapter.OrderMakeAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.callback.PingppPayResult;
import shop.imake.client.Api4Cart;
import shop.imake.client.ClientAPI;
import shop.imake.client.ClientApiHelper;
import shop.imake.fragment.CartPage;
import shop.imake.fragment.PayDetailFragment;
import shop.imake.model.AddressInfo2;
import shop.imake.model.CartBigModel;
import shop.imake.model.CartItem2;
import shop.imake.model.CartModel;
import shop.imake.model.OrderMakeOrderNumberModel;
import shop.imake.model.OrderMakeUploadModel;
import shop.imake.model.PayResultEvent;
import shop.imake.task.PaymentTask;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DialogUtils;
import shop.imake.utils.DoubleTextUtils;
import shop.imake.utils.LoadViewHelper;
import shop.imake.utils.LogUtils;
import shop.imake.utils.MathUtil;
import shop.imake.utils.PayUtils;
import shop.imake.utils.StringUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.Utility;
import shop.imake.widget.IUUTitleBar;
import shop.imake.widget.NoScrollListView;

/**
 * 确认订单页
 *
 * @author JackB
 * @date 2016/6/12
 */
public class OrderMakeActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {

    public static final String TAG = OrderMakeActivity.class.getSimpleName();
    public static final String PARAM_PAGE_TYPE = "pageType";
    public static final int REQUEST_CODE = 0;

    private int mPageType;          // 上一页页面类型，0购物车页，1商品详情页
    private String mStrOrderNum;    // 订单编号
    private double mFinalPay;       // 金额

    private IUUTitleBar mTitleBar;
    // 延误提示
    private LinearLayout mLlBodyView;
    private TextView mTvDelayTip;

    // 收货地址栏：姓名、电话、地址、默认标识
    private View mNoAddressView;    // 无地址栏
    private View mAddressView;
    private TextView mTvName;
    private TextView mTvTel;
    private TextView mTvAddress;
    private TextView mTvAddressDefault;

    // 订单列表
    private NoScrollListView mListView;
    private OrderMakeAdapter mAdapter;
    //    private List<OrderMakeItem> mData;
    private List<CartItem2> mData;

    private List<CartItem2> mInRushList = new ArrayList<>();    // 统计商品集合mData中的抢购中商品
    private List<CartItem2> mFinalList = new ArrayList<>();     // 要提交的商品集合

    // 订单详情
    private View mTvPostageTipView;         // 满150包邮的提示
    //    private TextView mTvExpressCompany;   // 配送方式
    private TextView mTvPostage;            // 邮费
    private TextView mTvPostageExtra;       // 增收运费
    private TextView mTvWeight;             // 重量
    private double mSumWeight;              // 商品总重量

    private TextView mTvProductCount;       // 商品数量
    //    private TextView mTvGoldSum;            // 本次消费可得积分
    private TextView mTvMoneySum;           // 商品总额
    private TextView mTvPointsSum;          // 商品总额中的积分

    private TextView mTvTotalPay;           // 底部实付金额
    private TextView mTvTotalPoints;        // 底部实付积分
    private TextView mTvPay;                // 去付款

    // 异常：断网、未登录等处理
    private View mNoNetView;
    private View mNoLoginView;
    private View mBodyView;

    // 要传递的
    private String mName;
    private String mAddress;
    private String mPhone;

    // flag
    // 标志：地址是否是选择返回过来的
    private boolean isAddressSelected = false;
    // 标志：接口返回的地址中是否有默认地址
    private boolean hasDefaultAddress = false;

    // 法1，由于loadListData()中包含loadData()，为防止第一次加载时重复执行loadData()，
    // 在loadData()中设置是否第一次执行标记（但是loadListData()不一定会执行loadData()）

    // 法2，判断是否已执行完hasLoadListData()，未执行完则不执行loadData()，
    // 防止重复执行(但是loadListData()先执行的话并不能避免，不过一般不会发生，因为要请求完毕之后才会执行)
    private boolean hasLoadListData = false;
    // 只执行一次，执行完毕就不再执行了
    private int loadListDataSuccess = -1;

    // 标志：限制总额中的邮费只累加1次
    private int flagPostagePlusOnlyOnce = -1;

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    // 销毁本页
                    OrderMakeActivity.this.finish();
                    break;

            }
        }
    };

    private Api4Cart mApi4Cart;
    private LoadViewHelper mLoadViewHelper;
    private View.OnClickListener onViewHelperErrorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loadData();
            loadListData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_make);

        mApi4Cart = (Api4Cart) ClientApiHelper.getInstance().getClientApi(Api4Cart.class);
        EventBus.getDefault().register(this);

        initException();
        initVariables();
        initView();
        setupView();
        initCtrl();

        mLoadViewHelper = new LoadViewHelper(mBodyView);
        mLoadViewHelper.showLoading();
        // 加载列表数据，只加载一次
        loadListData();
//        loadData();
    }

    // 1. 不加，地址为空时没法添加地址后自动选择地址
    // 2. 加上，没法自由选择地址
    // 已解决。isAddressSelected
    @Override
    protected void onResume() {
        super.onResume();
        if (!isAddressSelected) {
            loadData();
        }
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
     * @param goodList 传递本页所需的 要购买的商品列表
     * @param pageType 上一页的页面类型：默认-1，0购物车页；1商品详情页。
     */
    public static void actionStart(Context context, List<CartItem2> goodList, int pageType) {
        Intent intent = new Intent(context, OrderMakeActivity.class);
        MainApplication.getInstance().setData(goodList);
        intent.putExtra(PARAM_PAGE_TYPE, pageType);
        context.startActivity(intent);
    }

    private void initException() {
        mNoNetView = findViewById(R.id.net_fail);
        mNoLoginView = findViewById(R.id.no_login);
        mBodyView = findViewById(R.id.body);

    }

    private void initVariables() {
        mData = new ArrayList<>();
        // 获取上一页页面类型pageType
        mPageType = getIntent().getIntExtra(PARAM_PAGE_TYPE, -1);
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.order_make_title_bar);
        mLlBodyView = (LinearLayout) findViewById(R.id.order_make_ll);

        mListView = (NoScrollListView) findViewById(R.id.order_make_listview);
        mAddressView = findViewById(R.id.order_make_address);
        mTvName = (TextView) findViewById(R.id.order_make_tv_name);
        mTvTel = (TextView) findViewById(R.id.order_make_tv_tel);
        mTvAddress = (TextView) findViewById(R.id.order_make_tv_address);
        mTvAddressDefault = (TextView) findViewById(R.id.order_make_tv_address_default_tag);

        mTvPay = (TextView) findViewById(R.id.order_confirm_tv_pay);

        mTvPostageTipView = findViewById(R.id.order_confirm_rl_postage_tip);
        mTvWeight = (TextView) findViewById(R.id.order_make_tv_weight);
        mTvPostage = (TextView) findViewById(R.id.order_make_tv_postage);
        mTvPostageExtra = (TextView) findViewById(R.id.order_make_tv_postage_extra);
        mTvProductCount = (TextView) findViewById(R.id.order_confirm_tv_product_count);
        mTvMoneySum = (TextView) findViewById(R.id.order_make_tv_money_sum);
        mTvPointsSum = (TextView) findViewById(R.id.order_make_tv_points_sum);

        mTvTotalPay = (TextView) findViewById(R.id.order_confirm_tv_total_pay);
        mTvTotalPoints = (TextView) findViewById(R.id.order_confirm_tv_total_points);

        mNoAddressView = findViewById(R.id.order_make_ll_no_address);

        // 头部提示
        View tipView = LayoutInflater.from(this).inflate(R.layout.layout_tip, null);
        mTvDelayTip = (TextView) tipView.findViewById(R.id.tip_tv_content);
        if (Constants.showTip) {
            mLlBodyView.addView(tipView, 0);
        }
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mAddressView.setOnClickListener(this);
        // 设置ListView不获取焦点，使ScrollView起始位置从顶部开始
        mListView.setFocusable(false);
        mTvPay.setOnClickListener(this);
        mNoAddressView.setOnClickListener(this);

    }

    private void initCtrl() {
        mAdapter = new OrderMakeAdapter(this, mData);
        mListView.setAdapter(mAdapter);

    }

    private void loadListData() {

        if (loadListDataSuccess == 1){
            // 如果执行过此方法成功加载数据，就不再执行。
            return;
        }

        // 获取购物车页传过来的数据
        final List<CartItem2> goodList = (List<CartItem2>) MainApplication.getInstance().getData();
//        MainApplication.getInstance().setData(null); // 断网之后刷新，重新获取会获取不到数据，null，导致空指针异常

        LogUtils.d(TAG, "loadListData: " + goodList);

        if (goodList.isEmpty()) {
            return;
        }

        // 使用此刻的服务器数据还是本地数据(是否校准数据)
        boolean useDataInServer = true;

        if (useDataInServer) {

            // 请求购物车接口获取此刻服务器数据
            ClientAPI.getCartData(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    CurrentUserManager.TokenDue(e);
                    hasLoadListData = true;
                    mLoadViewHelper.showError(OrderMakeActivity.this, onViewHelperErrorClickListener);
                }

                @Override
                public void onResponse(String response, int id) {
                    dismissLoadingDialog();
                    mLoadViewHelper.restore();
                    hasLoadListData = true;
                    loadListDataSuccess = 1;

                    if (response != null && !"[]".equals(response)) {
                        Gson gson = new Gson();
                        CartBigModel cartBigModel = gson.fromJson(response, CartBigModel.class);
                        List<CartModel> shopping_carts = cartBigModel.getShopping_carts();
                        HashMap<String, CartModel> nowCartMap = new HashMap<String, CartModel>();
                        if (shopping_carts != null && !shopping_carts.isEmpty()) {
                            for (CartModel item : shopping_carts) {
                                long product_id = item.getProduct_id();
                                long product_size_id = item.getProduct_size_id();
                                nowCartMap.put("" + product_id + "+" + product_size_id, item);
                            }
                        }

                        // 从map中取出上一页选中的那些
                        List<CartItem2> chooseList = new ArrayList<CartItem2>();
                        for (CartItem2 item : goodList) {
                            CartModel cartModel = item.getCartModel();

                            if (cartModel != null) {
                                long product_id = cartModel.getProduct_id();
                                long product_size_id = cartModel.getProduct_size_id();

                                CartModel cm = nowCartMap.get("" + product_id + "+" + product_size_id);
                                if (cm != null) {
                                    CartItem2 cartItem2 = new CartItem2();
                                    cartItem2.setCartModel(cm);
                                    chooseList.add(cartItem2);

                                }
                            }
                        }

                        mAdapter.setData(chooseList);
                        setDataForPage(chooseList);
                        loadData(); // 加载地址+计算邮费
                    }

                }
            });
        } else {
            hasLoadListData = true;

            mAdapter.setData(goodList);
            setDataForPage(goodList);
        }

    }

    // 给页面赋值
    private void setDataForPage(List<CartItem2> goodList) {
        checkReach10(goodList);

        // 统计商品集合中的在抢购中商品，只在第一次进入该页面时统计一次
        mInRushList.clear();
        for (CartItem2 item : mData) {
//            if (CartPage.CartHelper.isRushGoodInRush(item)) {
//                mInRushList.add(item);
//            }
            CartModel cartModel = item.getCartModel();
            if (cartModel != null) {
                if (CartPage.CartHelper.isProductSelfInRush(cartModel)) {
                    mInRushList.add(item);
                }

            }
        }

        // 重量
        mSumWeight = 0;
        // 计算本次消费可得UU
        int sumGold = 0;
        // 计算商品总额，底部实付金额
        double sumMoney = 0;
        // 底部实付积分
        int totalPoints = 0;
        // 商品数量
        int productCount = 0;

        // 统计该订单的总金额、总积分、总重量
        for (CartItem2 item : goodList) {
            if (item == null) {
                continue;
            }
            CartModel cartModel = item.getCartModel();
            if (cartModel == null) {
                continue;
            }

            productCount += cartModel.getNumber();

            CartModel.ProductSizeBean product_size = cartModel.getProduct_size();

            if (product_size != null) {
                int number = cartModel.getNumber();
                double price = Double.valueOf(product_size.getPrice());

                // 抢购中商品按抢购价
                CartModel.ProductBean product = cartModel.getProduct();
                if (product != null) {
                    boolean isRushGood = product.getProduct_type() == 0 ? true : false;

                    if (isRushGood) {
                        CartModel.ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();

                        if (product_time_frame != null) {
                            CartModel.ProductSizeBean.ProductTimeFrameBean.TimeFrameBean time_frame = product_time_frame.getTime_frame();

                            boolean if_rush_to_purchasing = product_time_frame.isIf_rush_to_purchasing();
                            if (if_rush_to_purchasing) {
                                price = Double.valueOf(product_size.getRush_price());
                            }

                            // 用数据本身判断，不使用本地时间判断了
//                                    if (time_frame != null) {
//                                        String time_frame1 = time_frame.getTime_frame();
//                                        boolean inRush = CartPage.CartHelper.isRushGoodInRush(time_frame1);
//
//                                        if (inRush) {
//                                            price = Double.valueOf(product_size.getRush_price());
//                                        }
//                                    }
                        }
                    }
                }

                sumMoney += price * number;
                totalPoints += product_size.getIntegration_price() * number;
                String weightStr = product_size.getWeight();
                if (weightStr != null) {
                    double v = Double.parseDouble(weightStr);
                    double weight = v * number;
                    mSumWeight += weight;
                }
            }
        }

        // 商品数量
        mTvProductCount.setText("共" + productCount + "件产品");
        // 重量
        mTvWeight.setText(MathUtil.round(mSumWeight, 2) + "kg");
        // 商品总额
        mTvMoneySum.setText("¥" + DoubleTextUtils.setDoubleUtils(sumMoney));

        mFinalPay = sumMoney;
        // 底部实付金额
        mTvTotalPay.setText("¥" + DoubleTextUtils.setDoubleUtils(mFinalPay));

    }

    /**
     * 检查商品集合是否存在数量超10商品
     *
     * @param list
     */
    private void checkReach10(List<CartItem2> list) {
        if (!Constants.showTip) {
            return;
        }

        boolean isReach = false;
        for (CartItem2 item : list) {
            if (item == null) {
                continue;
            }

            CartModel cartModel = item.getCartModel();
            if (cartModel == null) {
                continue;
            }

            if (cartModel.getNumber() >= 10) {
                isReach = true;
                break;
            }
        }

        if (isReach) {
            mTvDelayTip.setText(getResources().getString(R.string.tip_send_2));
        } else {
            mTvDelayTip.setText(getResources().getString(R.string.tip_send));
        }
    }

    public void loadData() {
        if (!hasLoadListData) {
            return;
        }

        // 设置默认地址
        ClientAPI.getOrderMakeAddressList(TAG, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mLoadViewHelper.showError(OrderMakeActivity.this, onViewHelperErrorClickListener);
                resetAddress();
                checkAddressEmpty();
                dismissLoadingDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                mLoadViewHelper.restore();

                hasDefaultAddress = false;
                if (response != null && !"[]".equals(response)) {

                    Gson gson = new Gson();
                    AddressInfo2 addressInfo2 = gson.fromJson(response, AddressInfo2.class);
                    List<AddressInfo2.MemberAddressesBean> data = addressInfo2.getMember_addresses();

                    mTvAddressDefault.setVisibility(View.GONE);
                    for (int i = 0; i < data.size(); i++) {
                        AddressInfo2.MemberAddressesBean memberAddressesBean = data.get(i);
                        if (memberAddressesBean.isIs_default()) {
                            hasDefaultAddress = true;

                            mTvName.setText(memberAddressesBean.getContact_person());
                            mTvTel.setText(memberAddressesBean.getContact_phone());
                            String address = memberAddressesBean.getProvince() + memberAddressesBean.getCity() + memberAddressesBean.getDistrict() + memberAddressesBean.getAddress_detail();
                            mTvAddress.setText(address);
                            mTvAddressDefault.setVisibility(View.VISIBLE);
                            break;
                        }
                    }

                    // 如果地址列表没有默认地址，选择第一个地址
//                            if (!hasDefaultAddress && data.size() >= 1) {
//                                AddressInfo2.MemberAddressesBean memberAddressesBean = data.get(0);
//                                mTvName.setText(memberAddressesBean.getContact_person());
//                                mTvTel.setText(memberAddressesBean.getContact_phone());
//                                String address = memberAddressesBean.getProvince() + memberAddressesBean.getCity() + memberAddressesBean.getDistrict() + memberAddressesBean.getAddress_detail();
//                                mTvAddress.setText(address);
//                            }
                }

                if (hasDefaultAddress) {
                    loadPostageData();
                } else {
                    resetAddress();
                }
                // 检查地址栏是否为空
                checkAddressEmpty();
            }
        });

    }

    private void resetAddress() {
        mTvName.setText("");
        mTvTel.setText("");
        mTvAddress.setText("");
        mTvPostage.setText("");
    }

    /**
     * 加载邮费
     */
    private void loadPostageData() {
        String address = mTvAddress.getText().toString();

        if (TextUtils.isEmpty(address)) {
            return;
        }

        if (mSumWeight <= 0) {
            return;
        }

        ClientAPI.getPostage(TAG, address, mSumWeight, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
                LogUtils.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "onResponse: " + response);
                mTvPostage.setText("");
                if (!TextUtils.isEmpty(response) && !"[]".equals(response)) {
                    mTvPostage.setText("¥" + DoubleTextUtils.setDoubleUtils(Double.valueOf(response)));
                    if (flagPostagePlusOnlyOnce != 1) {
                        flagPostagePlusOnlyOnce = 1;
                        mFinalPay += Double.valueOf(response);
                    }
                    mTvTotalPay.setText(String.valueOf(DoubleTextUtils.setDoubleUtils(mFinalPay)));
                }
            }
        });
    }

    private void checkAddressEmpty() {
        // 如果地址为空，显示 去添加 页面
        if (TextUtils.isEmpty(mTvName.getText().toString())
                || TextUtils.isEmpty(mTvTel.getText().toString())
                || TextUtils.isEmpty(mTvAddress.getText().toString())
                ) {
            mNoAddressView.setVisibility(View.VISIBLE);
            mAddressView.setVisibility(View.GONE);

        } else {
            mNoAddressView.setVisibility(View.GONE);
            mAddressView.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;

            case R.id.order_make_ll_no_address:// 没有收货地址，去添加
            case R.id.order_make_address: // 选择收货地址
                Intent intent = new Intent(this, AddressManagerActivity.class);
                intent.putExtra(AddressManagerActivity.EXTRA_PAGE_NAME, TAG);
                startActivityForResult(intent, REQUEST_CODE);
                break;

            case R.id.order_confirm_tv_pay: // 去付款
                if (Utility.isFastDoubleClick()) {
                    return;
                }

                // 对在本页停留时由原本抢购中变为过抢购期的商品进行处理，不包括之前页面继续购买的过抢购期的商品

                // mFinalList = mData - mInRushList中此时已过期的商品
                mFinalList.clear();
                mFinalList.addAll(mData);

                // 获取原本在抢购中的商品，去付款时过抢购期的商品
                final List<CartItem2> expiredList = getExpiredList(mInRushList);

                // 是否存在过期商品
                final boolean existExpired = expiredList.isEmpty() ? false : true;
                if (existExpired) {
                    final MaterialDialog dialog = new MaterialDialog.Builder(this)
                            .customView(R.layout.layout_dialog_rush_expired, false)
                            .build();
                    View customView = dialog.getCustomView();
                    View passView = customView.findViewById(R.id.rush_expired_dialog_tv_pass);  // 不买它了
                    View goonView = customView.findViewById(R.id.rush_expired_dialog_tv_goon);  // 继续购买

                    // 不买它了
                    passView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mFinalList.removeAll(expiredList);
                            if (!mFinalList.isEmpty()) {
                                commitOrder();
                            }

                            dialog.dismiss();
                        }
                    });

                    // 继续购买
                    goonView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!mFinalList.isEmpty()) {
                                commitOrder();
                            }

                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                } else {
                    commitOrder();
                }

                break;
        }
    }

    /**
     * 得到商品集合中的过期商品
     *
     * @param data 商品集合--该页面打开时在抢购中的商品集合
     * @return
     */
    private List<CartItem2> getExpiredList(List<CartItem2> data) {
        List<CartItem2> ret = new ArrayList<>();

        if (data != null && !data.isEmpty()) {
            for (CartItem2 item : data) {
                CartModel cartModel = item.getCartModel();
                if (cartModel == null) {
                    continue;
                }

                CartModel.ProductBean product = cartModel.getProduct();
                if (product == null) {
                    continue;
                }

                boolean isRush = product.getProduct_type() == 0 ? true : false;
                if (isRush) { // 属于抢购商品
                    CartModel.ProductSizeBean product_size = cartModel.getProduct_size();
                    if (product_size == null) {
                        continue;
                    }

                    CartModel.ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();
                    if (product_time_frame != null) { // 判断抢购商品有没有过期，非抢购商品这个字段为null
                        CartModel.ProductSizeBean.ProductTimeFrameBean.TimeFrameBean time_frame = product_time_frame.getTime_frame();
                        if (time_frame == null) {
                            continue;
                        }
                        String time_frame1 = time_frame.getTime_frame();
                        boolean isRushExpired = CartPage.CartHelper.isRushGoodExpired(time_frame1);
                        if (isRushExpired) {
                            ret.add(item);
                        }
                    }
                }
            }
        }

        return ret;
    }

    /**
     * 提交订单
     */
    private void commitOrder() {

        // https://github.com/saiwu-bigkoo/Android-AlertView
//        new AlertView("选择支付方式", null, "取消", null, new String[]{getString(R.string.pay_alipay), getString(R.string.pay_balance), getString(R.string.pay_hx)}, this, AlertView.Style.ActionSheet, this).show();

        if (!TextUtils.isEmpty(mStrOrderNum)) {  // 有订单号则直接调起支付
            payOrder();

        } else { // 没有订单号，生成订单并支付

            //  要上传的收件人信息
            mName = mTvName.getText().toString();
            mAddress = mTvAddress.getText().toString();
            mPhone = mTvTel.getText().toString();

            if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mAddress) || TextUtils.isEmpty(mPhone)) {
                ToastUtils.showShort("未选择收货地址");
                return;
            }

            showLoadingDialog();

            // 要上传的商品列表
            List<OrderMakeUploadModel> uploadModelList = new ArrayList<>();
            for (CartItem2 ci : mFinalList) {
                OrderMakeUploadModel orderMakeUploadModel = new OrderMakeUploadModel();
                CartModel cartModel = ci.getCartModel();
                if (cartModel != null) {
                    orderMakeUploadModel.setProduct_id(cartModel.getProduct_id());
                    orderMakeUploadModel.setProduct_size_id(cartModel.getProduct_size_id());
                    orderMakeUploadModel.setNumber(cartModel.getNumber());
                }
                uploadModelList.add(orderMakeUploadModel);
            }

            LogUtils.d(TAG, "uploadModelList" + uploadModelList.toString());

            Gson gson = new Gson();
            String productListJson = gson.toJson(uploadModelList);
            LogUtils.d(productListJson);

            String userToken = CurrentUserManager.getUserToken();
            StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);

            /**
             * 跟据上一页的页面类型，走不同接口，提交订单
             * 如果是默认-1
             * 如果是0（购物车页）...
             * 如果是1（商品详情页）...
             */
            switch (mPageType) {
                // 上一页是购物车页
                case 0:
                    sb.append("api/v1/order/submit");
                    sb.append("?token=").append(userToken);
                    break;

                // 上一页是商品详情页
                case 1:
                    sb.append("api/v1/order/buyNow");
                    sb.append("?token=").append(userToken);
                    break;
            }
            String url = sb.toString();

            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("address", mAddress);
            paramsMap.put("phone", mPhone);
            paramsMap.put("addressee", mName);
            paramsMap.put("product_list", productListJson);

            mApi4Cart.commitOrder(url, this, paramsMap, new DataCallback<OrderMakeOrderNumberModel>(this) {
                @Override
                public void onFail(Call call, Exception e, int id) {
                    dismissLoadingDialog();
                    Dialog dialog = DialogUtils.createMessageDialog(
                            OrderMakeActivity.this,
                            null,
                            StringUtils.getExceptionMessage(e.getMessage()), "确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                    if (ActivityCollector.sActivities.contains(OrderMakeActivity.this) && !dialog.isShowing() && !isFinishing()) {
                        dialog.show();
                    }
                }

                @Override
                public void onSuccess(Object response, int id) {
                    dismissLoadingDialog();
                    if (response == null) {
                        return;
                    }
                    OrderMakeOrderNumberModel orderMakeOrderNumberModel = (OrderMakeOrderNumberModel) response;
                    mStrOrderNum = orderMakeOrderNumberModel.getOrder_number();
                    payOrder();

                }
            });
        }
    }

    private void payOrder() {

        PayUtils.pay(getSupportFragmentManager(), TAG, mFinalPay,
                new PayDetailFragment.PayCallback() {
                    @Override
                    public void onPayCallback(String channel) {
                        int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
                        new PaymentTask(OrderMakeActivity.this, OrderMakeActivity.this, mStrOrderNum, channel, mTvPay, TAG, null)
                                .execute(new PaymentTask.PaymentRequest(channel, amount));
                    }
                }
        );

    }

    /**
     * 支付成功后的操作
     */
    private void paySuccess() {
        OrderPaySuccessActivity.actionStart(OrderMakeActivity.this, mName, mPhone, mAddress, mStrOrderNum);
        mHandler.sendEmptyMessage(0); // 销毁页面
    }

    /**
     * 余额支付后的“回调”
     *
     * @param event
     */
    @Subscribe
    public void onPayEvent(PayResultEvent event) {
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

        mTvPay.setOnClickListener(OrderMakeActivity.this);

        PingppPayResult.setOnPayResultCallback(requestCode, resultCode, data, new PingppPayResult.OnPayResultCallback() {
            @Override
            public void onPaySuccess() {
                paySuccess();
            }

            @Override
            public void onPayFail() {
                Intent intent = new Intent(OrderMakeActivity.this, OrderPayFailActivity.class);
                intent.putExtra(OrderPayFailActivity.PARAM_ORDER_NUMBER, mStrOrderNum);
                startActivity(intent);
                OrderMakeActivity.this.finish();
            }
        });

        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    isAddressSelected = true;

                    String name = data.getStringExtra("name");
                    String tel = data.getStringExtra("tel");
                    String address = data.getStringExtra("address");
                    boolean isDefault = data.getBooleanExtra("isDefault", false);

                    LogUtils.d(TAG, "address: " + address);

                    mTvName.setText(name);
                    mTvTel.setText(tel);
                    mTvAddress.setText(address);
                    mTvAddressDefault.setVisibility(isDefault ? View.VISIBLE : View.GONE);

                    checkAddressEmpty();
                    loadPostageData();
                    break;

                default:
                    break;
            }
        }
    }
//
//    // 检查网络
//    private void checkNet() {
//
//        if (!NetStateUtils.isNetworkAvailable(this)) {
//            LogUtils.d(TAG, "checkNet  无网");
//            mNoNetView.setVisibility(View.VISIBLE);
//            mBodyView.setVisibility(View.GONE);
//
//            // 无网络页面中的重新加载按钮
//            TextView refreshView = (TextView) mNoNetView.findViewById(R.id.net_fail_refresh);
//            refreshView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    loadData();
//                }
//            });
//
//        } else {
//            // 有网但未登录
//            LogUtils.d(TAG, "checkNet  有网，去登录");
//            mNoNetView.setVisibility(View.GONE);
//            mBodyView.setVisibility(View.GONE);
//
//            gotoLogin();
//        }
//
//    }
//
//    // 显示去登录页面
//    private void gotoLogin() {
//        mNoLoginView.setVisibility(View.VISIBLE);
//
//        TextView tvGotoLogin = (TextView) findViewById(R.id.no_login_goto);
//        tvGotoLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                jump(LoginActivity.class, false);
//            }
//        });
//    }

}
