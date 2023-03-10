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
 * ???????????????
 *
 * @author JackB
 * @date 2016/6/12
 */
public class OrderMakeActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {

    public static final String TAG = OrderMakeActivity.class.getSimpleName();
    public static final String PARAM_PAGE_TYPE = "pageType";
    public static final int REQUEST_CODE = 0;

    private int mPageType;          // ????????????????????????0???????????????1???????????????
    private String mStrOrderNum;    // ????????????
    private double mFinalPay;       // ??????

    private IUUTitleBar mTitleBar;
    // ????????????
    private LinearLayout mLlBodyView;
    private TextView mTvDelayTip;

    // ?????????????????????????????????????????????????????????
    private View mNoAddressView;    // ????????????
    private View mAddressView;
    private TextView mTvName;
    private TextView mTvTel;
    private TextView mTvAddress;
    private TextView mTvAddressDefault;

    // ????????????
    private NoScrollListView mListView;
    private OrderMakeAdapter mAdapter;
    //    private List<OrderMakeItem> mData;
    private List<CartItem2> mData;

    private List<CartItem2> mInRushList = new ArrayList<>();    // ??????????????????mData?????????????????????
    private List<CartItem2> mFinalList = new ArrayList<>();     // ????????????????????????

    // ????????????
    private View mTvPostageTipView;         // ???150???????????????
    //    private TextView mTvExpressCompany;   // ????????????
    private TextView mTvPostage;            // ??????
    private TextView mTvPostageExtra;       // ????????????
    private TextView mTvWeight;             // ??????
    private double mSumWeight;              // ???????????????

    private TextView mTvProductCount;       // ????????????
    //    private TextView mTvGoldSum;            // ????????????????????????
    private TextView mTvMoneySum;           // ????????????
    private TextView mTvPointsSum;          // ????????????????????????

    private TextView mTvTotalPay;           // ??????????????????
    private TextView mTvTotalPoints;        // ??????????????????
    private TextView mTvPay;                // ?????????

    // ????????????????????????????????????
    private View mNoNetView;
    private View mNoLoginView;
    private View mBodyView;

    // ????????????
    private String mName;
    private String mAddress;
    private String mPhone;

    // flag
    // ?????????????????????????????????????????????
    private boolean isAddressSelected = false;
    // ??????????????????????????????????????????????????????
    private boolean hasDefaultAddress = false;

    // ???1?????????loadListData()?????????loadData()??????????????????????????????????????????loadData()???
    // ???loadData()?????????????????????????????????????????????loadListData()??????????????????loadData()???

    // ???2???????????????????????????hasLoadListData()???????????????????????????loadData()???
    // ??????????????????(??????loadListData()???????????????????????????????????????????????????????????????????????????????????????????????????)
    private boolean hasLoadListData = false;
    // ????????????????????????????????????????????????
    private int loadListDataSuccess = -1;

    // ??????????????????????????????????????????1???
    private int flagPostagePlusOnlyOnce = -1;

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    // ????????????
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
        // ????????????????????????????????????
        loadListData();
//        loadData();
    }

    // 1. ???????????????????????????????????????????????????????????????
    // 2. ?????????????????????????????????
    // ????????????isAddressSelected
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
     * ???????????????
     *
     * @param context
     * @param goodList ????????????????????? ????????????????????????
     * @param pageType ?????????????????????????????????-1???0???????????????1??????????????????
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
        // ???????????????????????????pageType
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

        // ????????????
        View tipView = LayoutInflater.from(this).inflate(R.layout.layout_tip, null);
        mTvDelayTip = (TextView) tipView.findViewById(R.id.tip_tv_content);
        if (Constants.showTip) {
            mLlBodyView.addView(tipView, 0);
        }
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mAddressView.setOnClickListener(this);
        // ??????ListView?????????????????????ScrollView???????????????????????????
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
            // ???????????????????????????????????????????????????????????????
            return;
        }

        // ????????????????????????????????????
        final List<CartItem2> goodList = (List<CartItem2>) MainApplication.getInstance().getData();
//        MainApplication.getInstance().setData(null); // ?????????????????????????????????????????????????????????null????????????????????????

        LogUtils.d(TAG, "loadListData: " + goodList);

        if (goodList.isEmpty()) {
            return;
        }

        // ????????????????????????????????????????????????(??????????????????)
        boolean useDataInServer = true;

        if (useDataInServer) {

            // ????????????????????????????????????????????????
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

                        // ???map?????????????????????????????????
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
                        loadData(); // ????????????+????????????
                    }

                }
            });
        } else {
            hasLoadListData = true;

            mAdapter.setData(goodList);
            setDataForPage(goodList);
        }

    }

    // ???????????????
    private void setDataForPage(List<CartItem2> goodList) {
        checkReach10(goodList);

        // ??????????????????????????????????????????????????????????????????????????????????????????
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

        // ??????
        mSumWeight = 0;
        // ????????????????????????UU
        int sumGold = 0;
        // ???????????????????????????????????????
        double sumMoney = 0;
        // ??????????????????
        int totalPoints = 0;
        // ????????????
        int productCount = 0;

        // ???????????????????????????????????????????????????
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

                // ???????????????????????????
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

                            // ??????????????????????????????????????????????????????
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

        // ????????????
        mTvProductCount.setText("???" + productCount + "?????????");
        // ??????
        mTvWeight.setText(MathUtil.round(mSumWeight, 2) + "kg");
        // ????????????
        mTvMoneySum.setText("??" + DoubleTextUtils.setDoubleUtils(sumMoney));

        mFinalPay = sumMoney;
        // ??????????????????
        mTvTotalPay.setText("??" + DoubleTextUtils.setDoubleUtils(mFinalPay));

    }

    /**
     * ???????????????????????????????????????10??????
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

        // ??????????????????
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

                    // ????????????????????????????????????????????????????????????
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
                // ???????????????????????????
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
     * ????????????
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
                    mTvPostage.setText("??" + DoubleTextUtils.setDoubleUtils(Double.valueOf(response)));
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
        // ??????????????????????????? ????????? ??????
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
            case R.id.left_layout: // ??????
                finish();
                break;

            case R.id.order_make_ll_no_address:// ??????????????????????????????
            case R.id.order_make_address: // ??????????????????
                Intent intent = new Intent(this, AddressManagerActivity.class);
                intent.putExtra(AddressManagerActivity.EXTRA_PAGE_NAME, TAG);
                startActivityForResult(intent, REQUEST_CODE);
                break;

            case R.id.order_confirm_tv_pay: // ?????????
                if (Utility.isFastDoubleClick()) {
                    return;
                }

                // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????

                // mFinalList = mData - mInRushList???????????????????????????
                mFinalList.clear();
                mFinalList.addAll(mData);

                // ?????????????????????????????????????????????????????????????????????
                final List<CartItem2> expiredList = getExpiredList(mInRushList);

                // ????????????????????????
                final boolean existExpired = expiredList.isEmpty() ? false : true;
                if (existExpired) {
                    final MaterialDialog dialog = new MaterialDialog.Builder(this)
                            .customView(R.layout.layout_dialog_rush_expired, false)
                            .build();
                    View customView = dialog.getCustomView();
                    View passView = customView.findViewById(R.id.rush_expired_dialog_tv_pass);  // ????????????
                    View goonView = customView.findViewById(R.id.rush_expired_dialog_tv_goon);  // ????????????

                    // ????????????
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

                    // ????????????
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
     * ????????????????????????????????????
     *
     * @param data ????????????--?????????????????????????????????????????????
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
                if (isRush) { // ??????????????????
                    CartModel.ProductSizeBean product_size = cartModel.getProduct_size();
                    if (product_size == null) {
                        continue;
                    }

                    CartModel.ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();
                    if (product_time_frame != null) { // ??????????????????????????????????????????????????????????????????null
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
     * ????????????
     */
    private void commitOrder() {

        // https://github.com/saiwu-bigkoo/Android-AlertView
//        new AlertView("??????????????????", null, "??????", null, new String[]{getString(R.string.pay_alipay), getString(R.string.pay_balance), getString(R.string.pay_hx)}, this, AlertView.Style.ActionSheet, this).show();

        if (!TextUtils.isEmpty(mStrOrderNum)) {  // ?????????????????????????????????
            payOrder();

        } else { // ???????????????????????????????????????

            //  ???????????????????????????
            mName = mTvName.getText().toString();
            mAddress = mTvAddress.getText().toString();
            mPhone = mTvTel.getText().toString();

            if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mAddress) || TextUtils.isEmpty(mPhone)) {
                ToastUtils.showShort("?????????????????????");
                return;
            }

            showLoadingDialog();

            // ????????????????????????
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
             * ???????????????????????????????????????????????????????????????
             * ???????????????-1
             * ?????????0??????????????????...
             * ?????????1?????????????????????...
             */
            switch (mPageType) {
                // ????????????????????????
                case 0:
                    sb.append("api/v1/order/submit");
                    sb.append("?token=").append(userToken);
                    break;

                // ???????????????????????????
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
                            StringUtils.getExceptionMessage(e.getMessage()), "??????",
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
                        int amount = 1; // ?????? ???????????????????????????????????????????????????????????????????????????
                        new PaymentTask(OrderMakeActivity.this, OrderMakeActivity.this, mStrOrderNum, channel, mTvPay, TAG, null)
                                .execute(new PaymentTask.PaymentRequest(channel, amount));
                    }
                }
        );

    }

    /**
     * ????????????????????????
     */
    private void paySuccess() {
        OrderPaySuccessActivity.actionStart(OrderMakeActivity.this, mName, mPhone, mAddress, mStrOrderNum);
        mHandler.sendEmptyMessage(0); // ????????????
    }

    /**
     * ??????????????????????????????
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
     * ?????????????????????????????????
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
//    // ????????????
//    private void checkNet() {
//
//        if (!NetStateUtils.isNetworkAvailable(this)) {
//            LogUtils.d(TAG, "checkNet  ??????");
//            mNoNetView.setVisibility(View.VISIBLE);
//            mBodyView.setVisibility(View.GONE);
//
//            // ???????????????????????????????????????
//            TextView refreshView = (TextView) mNoNetView.findViewById(R.id.net_fail_refresh);
//            refreshView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    loadData();
//                }
//            });
//
//        } else {
//            // ??????????????????
//            LogUtils.d(TAG, "checkNet  ??????????????????");
//            mNoNetView.setVisibility(View.GONE);
//            mBodyView.setVisibility(View.GONE);
//
//            gotoLogin();
//        }
//
//    }
//
//    // ?????????????????????
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
