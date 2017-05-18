package com.bjaiyouyou.thismall.fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.MainActivity;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.LoginActivity;
import com.bjaiyouyou.thismall.activity.OrderMakeActivity;
import com.bjaiyouyou.thismall.adapter.CartAdapter2;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.CartBigModel;
import com.bjaiyouyou.thismall.model.CartItem2;
import com.bjaiyouyou.thismall.model.CartModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.MathUtil;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

/**
 * 购物车页面
 *
 * @author JackB
 * @date 2016/6/5
 */
public class CartPage extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    public static final String TAG = CartPage.class.getSimpleName();

    private TextView mTvNoNet;          // 断网提示
    private LinearLayout mBodyView;     // 主布局
    private View mNoLoginView;          // 未登录
    private View mNoLoginGoto;
    private TextView mTvTipSendDelay;   // 延迟发货提示

    private PullToRefreshListView mRefreshListView;

    // 列表
//    private List<CartItem> mRawList;
    private List<CartItem2> mRawList;   // 接口返回的初始的数据集(删除、加减都是在这个数据集上操作的，不只用于显示..)
    private List<CartItem2> mOkList;    // 过滤得到的合法数据集（目前仅去掉下架商品，用于全选判断、头部提示是否显示判断..）
    //    private CartAdapter mAdapter;
    private CartAdapter2 mAdapter;
    private View mEmptyView;
    private View mTvGoShopping;         // EmptyView中的“赶紧去逛逛”

    // 底部栏（全选、总价、总积分、结算按钮）
    private View mBottomView;
    private CheckBox mChbAll;
    private TextView mTvTotalPrice;
//    private TextView mTvTotalPoint;
    private Button mBtnOrder;

    // flag
    private boolean isChooseAll = false;    // 全选标识
    private int selectedCount = 0;
    private int tipFlag = -1;               // 防止重复执行
    private boolean isPerform = true;       // 是否在onResume()中执行initData()

    private MainActivity mMainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_cart, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initVariable();
        initView();
        setupView();
        initCtrl();
//        initData();

        // 加上，解决首次进入APP的时候底部栏不消失的问题
        // 首次进入底部栏不消失，因为未登录所以不走onResume()中的initData()中的checkDataSize()，已解决。
        checkDataSetEmpty();
    }

    @Override
    public void onResume() {
        super.onResume();

        /**
         * 不加，从购物车选择商品付款返回后，购物车没有刷新，已购买物品没有消失
         * 加了，未登录的时候，总弹出登录页，返回又弹出
         */

        /**
         * 如果无网或者未登录，return 不再走了
         * （==> token过期，自动获取  自动刷新token？见网络请求）
         *
         * 从登录页返回执行onResume()方法。如果已断网，则不执行其中的initData()方法了
         */
        LogUtils.d(TAG, "onResume token :" + CurrentUserManager.getUserToken());
//        if (!NetStateUtils.isNetworkAvailable(getContext()) || TextUtils.isEmpty(CurrentUserManager.getUserToken())){
//            return;
//        }

        if (TextUtils.isEmpty(CurrentUserManager.getUserToken())) {
//            isPerform = false;
            isPerform = true;  // 未登录可以执行了16.11.4
        }

        if (!isPerform) {
            return;
        }

        if (!isHidden()) {
            initData();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initData();
        }
    }

    private void initVariable() {
        mRawList = new LinkedList<>();
        mOkList = new LinkedList<>();
    }

    private void initView() {
        mBodyView = (LinearLayout) layout.findViewById(R.id.body);
        mRefreshListView = (PullToRefreshListView) layout.findViewById(R.id.cart_listview);
        mTvTotalPrice = (TextView) layout.findViewById(R.id.cart_tv_total_fee);
//        mTvTotalPoint = (TextView) layout.findViewById(R.id.cart_tv_total_points);
        mChbAll = (CheckBox) layout.findViewById(R.id.cart_chb_choose_all);
        mBtnOrder = (Button) layout.findViewById(R.id.cart_btn_order);
        mEmptyView = layout.findViewById(R.id.cart_empty);
        mTvGoShopping = layout.findViewById(R.id.cart_tv_goshopping);
        mBottomView = layout.findViewById(R.id.cart_rl_bottom);

        mTvNoNet = (TextView) layout.findViewById(R.id.net_fail);
        mNoLoginView = layout.findViewById(R.id.no_login);
        mNoLoginGoto = layout.findViewById(R.id.no_login_goto);

        // 头部提示
        View tipView = LayoutInflater.from(getContext()).inflate(R.layout.layout_tip, null);
        mTvTipSendDelay = (TextView) tipView.findViewById(R.id.tip_tv_content);
        if (Constants.showTip) {
            ListView listView = mRefreshListView.getRefreshableView();
            listView.addHeaderView(tipView);
        }
    }

    private void setupView() {
        mChbAll.setOnCheckedChangeListener(this);
        mBtnOrder.setOnClickListener(this);
        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();

            }
        });
        mRefreshListView.setEmptyView(mEmptyView);
        mTvGoShopping.setOnClickListener(this);
        mNoLoginGoto.setOnClickListener(this);

    }

    private void initCtrl() {
        mAdapter = new CartAdapter2(mRawList, getActivity(), this);
//        mAdapter.setMode(Attributes.Mode.Multiple);
        mRefreshListView.setAdapter(mAdapter);

        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

                // 重新计算mOkList(当删除了某个条目，如果不重新计算，在全选时会把这个删除掉的也加进去了)
                buildOkList(mRawList);

                // 检查是否显示发货延迟提示
                showTopTip(mOkList);

                // 合计金额
                double sum = countTotalPrice();
                mTvTotalPrice.setText("¥" + ((sum < 10E-6) ? 0 : MathUtil.round(sum, 2)));

                // 只显示价格20170513
//                // 总积分
//                int sumPoint = countTotalPoints();
//                mTvTotalPoint.setText(String.format(Locale.CHINA, "+%d兑换券", sumPoint));

                /**
                 * 条目按钮-->全选按钮
                 * 只有所有条目选择，“全选”按钮才选中
                 * 所有条目有一个没有选中，则“全选”按钮不选中
                 */
                selectedCount = 0;
                if (!mOkList.isEmpty()) {
                    isChooseAll = true;
                    for (CartItem2 item : mOkList) {
                        if (!item.isChecked()) {
                            isChooseAll = false;
                        } else {
                            selectedCount++; // 统计选中个数
                        }
                    }

                    mChbAll.setChecked(isChooseAll);
                }
                mBtnOrder.setText(String.format(Locale.CHINA, "结算(%d)", selectedCount));
            }
        });
    }

    private void initData() {

        // 未登录
        if (TextUtils.isEmpty(CurrentUserManager.getUserToken())) {
            mNoLoginView.setVisibility(View.VISIBLE);
            mBodyView.setVisibility(View.GONE);
            return;

        } else {
            mNoLoginView.setVisibility(View.GONE);
            mBodyView.setVisibility(View.VISIBLE);

        }

        ClientAPI.getCartData(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                /**
                 * 错误类型：断网、token过期、服务器错误
                 */

                // 检查网络
                checkNet();

                /**
                 * token过期或者未登录
                 * token过期是401错误，见接口文档
                 * 未登录400
                 */
                LogUtils.d(TAG, "onError:" + e.getMessage());
                if (!TextUtils.isEmpty(e.getMessage()) && (e.getMessage().contains("401") || e.getMessage().contains("400"))) {
                    // TODO: 2016/9/17 隐式登录 自动刷新token

                    // 清空token(临时先这样，token过期，清空token)
                    CurrentUserManager.clearUserToken();
                } else if (!TextUtils.isEmpty(e.getMessage())) {
                    isPerform = false;
                }

//                if (e.getMessage().contains("502")){
//                    isPerform = false;
//                }

                if (mRefreshListView != null && mRefreshListView.isRefreshing()) {
                    mRefreshListView.onRefreshComplete();
                }

                // 底部“总金额”栏显示与隐藏，当数据为空时隐藏
                checkDataSetEmpty();

            }

            @Override
            public void onResponse(String response, int id) {
                // 无网提示栏
                mTvNoNet.setVisibility(View.GONE);

                LogUtils.d("CartPage", "onResponse: " + response);
                if (!TextUtils.isEmpty(response) && !"[]".equals(response)) {
                    Gson gson = new Gson();
                    CartBigModel cartBigModel = gson.fromJson(response, CartBigModel.class);
                    List<CartModel> shopping_carts = cartBigModel.getShopping_carts();

                    LogUtils.d(TAG, "shopping_carts = " + shopping_carts.toString());

                    /**
                     * 转换为本地数据类型
                     * （由于数据接口拿到较晚，之前已经做了假数据，本地数据类型与接口返回的数据类型不一致）
                     * 在此，用转换数据类型的方式吧，不修改adapter了，就用之前的数据类型
                     * 算了，全部改了吧
                     */
                    mRawList.clear();
                    for (CartModel item : shopping_carts) {
                        CartItem2 cartItem2 = new CartItem2();
                        cartItem2.setCartModel(item);
                        mRawList.add(cartItem2);
                    }

                    // 在给适配器设置数据前得到dealList // dataSetObserver()中加了，这里貌似没必要了
//                    buildOkList(mRawList);

                    mAdapter.setData(mRawList);

                } else {
                    mRawList.clear();
                    // 在给适配器设置数据前得到dealList // dataSetObserver()中加了，这里貌似没必要了
//                    buildOkList(mRawList);
                    mAdapter.notifyDataSetChanged();
                }


                // 结束刷新
                if (mRefreshListView != null && mRefreshListView.isRefreshing()) {
                    mRefreshListView.onRefreshComplete();
                }

                // 底部“总金额”栏显示与隐藏，当数据为空时隐藏
                checkDataSetEmpty();

            }
        });
        // token过期未提示
//        } else { // token为空
//            ToastUtils.showShort("Token为空，请先登录");
//            // 底部“总金额”栏显示与隐藏，当数据为空时隐藏
//            checkDataSetEmpty();
//            // 结束刷新
//            if (mRefreshListView != null && mRefreshListView.isRefreshing()) {
//                mRefreshListView.onRefreshComplete();
//            }
//        }

    }

    /**
     * 检查是否显示发货延迟提示
     * 当存在单个数量>=10，延迟10天发货
     *
     * @param list
     */
    private void showTopTip(List<CartItem2> list) {
        if (!Constants.showTip){
            return;
        }

        // 是否复合显示条件
        boolean isReach = false;
        for (CartItem2 item : list) {
            CartModel cartModel = item.getCartModel();

            if (cartModel != null) {
                if (cartModel.getNumber() >= 10) {
                    isReach = true;
                    break;
                }

            }
        }

        if (isReach) {
            if (tipFlag != 0) {
                if (isAdded()) {
                    mTvTipSendDelay.setText(getString(R.string.tip_send_2));
                    tipFlag = 0;
                }
            }
        } else {
            if (tipFlag != 1) {
                if (isAdded()) {
                    mTvTipSendDelay.setText(getString(R.string.tip_send));
                    tipFlag = 1;
                }
            }
        }

        // 需求修改16.12.29 --> 提示一直显示
//        ViewWrapper wrapper = new ViewWrapper(mTvTipSendDelay);
//
//        if (isReach){  // 显示
//            if (mTvTipSendDelay.getVisibility() == View.GONE) {
//                mTvTipSendDelay.setVisibility(View.VISIBLE);
//                ValueAnimator showAnim = ObjectAnimator.ofInt(wrapper, "height", 0, DensityUtils.dp2px(getContext(), 50));
//                showAnim.setInterpolator(new AccelerateInterpolator());
//                showAnim.setDuration(200);
//                showAnim.start();
//
//            }
//
//        }else {  // 消失
//            if (mTvTipSendDelay.getVisibility() == View.VISIBLE) {
//                ValueAnimator hideAnim = ObjectAnimator.ofInt(wrapper, "height", DensityUtils.dp2px(getContext(), 50), 0);
//                hideAnim.setInterpolator(new LinearInterpolator());
//                hideAnim.setDuration(200);
//                hideAnim.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        mTvTipSendDelay.setVisibility(View.GONE);
//
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                });
//                hideAnim.start();
//            }
//        }
    }

    public double countTotalPrice() {
        double sum = 0;

        for (CartItem2 item : mRawList) {
            if (item.isChecked()) { // 只计算选中的商品的总价格
                if (item != null) {
                    CartModel cartModel = item.getCartModel();
                    CartModel.ProductSizeBean product_size = cartModel.getProduct_size();

                    if (cartModel != null && product_size != null) {
                        int count = cartModel.getNumber();
                        double price = Double.valueOf(product_size.getPrice());

                        // 抢购中商品按抢购价
                        CartModel.ProductBean product = cartModel.getProduct();
                        if (product != null) {
                            boolean isRushGood = product.getProduct_type() == 0 ? true : false;

                            if (isRushGood) {
                                CartModel.ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();

                                if (product_time_frame != null) {
                                    boolean if_rush_to_purchasing = product_time_frame.isIf_rush_to_purchasing();

                                    if (if_rush_to_purchasing) {
                                        price = Double.valueOf(product_size.getRush_price());
                                    }
                                }
                            }
                        }

                        sum += (price * count);
                    }
                }
            }
        }

        return sum;
    }

    /**
     * 获取总积分
     *
     * @return
     */
    public int countTotalPoints() {
        int sum = 0;
        for (CartItem2 item : mRawList) {
            if (item.isChecked()) {
                if (item.getCartModel() != null && item.getCartModel().getProduct_size() != null) {
                    int count = item.getCartModel().getNumber();
                    int points = item.getCartModel().getProduct_size().getIntegration_price();
                    sum += (points * count);
                }
            }
        }
        return sum;
    }

    /**
     * 对rawList进行过滤（1、去掉下架商品），得到mOkList
     *
     * @param rawList
     */
    private void buildOkList(List<CartItem2> rawList) {
        mOkList.clear();
        if (rawList != null && !rawList.isEmpty()) {
            for (CartItem2 item : rawList) {
                if (CartHelper.isOffShelf(item)) {
                    continue;
                }
                mOkList.add(item);
            }

        }
    }

    public void checkDataSetEmpty() {
        // 底部“总金额”栏显示与隐藏，当数据为空时隐藏
        if (mRawList.size() == 0) {
            mBottomView.setVisibility(View.GONE);
        } else {
            mBottomView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 全选/全不选
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            for (CartItem2 item : mOkList) {
                if (!item.isChecked()) { // 减少触发DataSetChangeObserver的次数
                    item.setChecked(true);
                }
            }
        }
        // 全选按钮-->条目选中
        // 只有当所有条目已经选择时，点击非全选，则所有条目取消选择
        // 即只有当此时全部选中时，点击全不选，所有条目取消选择
        else {
            if (isChooseAll) {
                for (CartItem2 item : mOkList) {
                    if (item.isChecked()) {
                        item.setChecked(false);
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart_btn_order: // 结账，下订单
                makeOrder();
                break;

            case R.id.cart_tv_goshopping: // 赶紧去逛逛
                mMainActivity.onTabClicked(mMainActivity.findViewById(R.id.controller_zero));
                break;

            case R.id.no_login_goto:// 未登录布局中的去登录按钮
                jump(LoginActivity.class, false);
                break;
        }
    }

    /**
     * 结账，下订单
     */
    private void makeOrder() {

        final List<CartItem2> selectedList = new ArrayList<>();
//        for (CartItem2 item : mRawList) {
//            if (item.isChecked() && !CartHelper.isOffShelf(item)) { // 只打印选中的商品 && 去掉下架商品
//                selectedList.add(item);
//            }
//        }

        for (CartItem2 item : mOkList) {
            if (item.isChecked()) { // 只打印选中的商品 && 去掉下架商品
                selectedList.add(item);
            }
        }

        if (selectedList.isEmpty()) {
            ToastUtils.showShort("请选择商品");
            return;
        }

        final List<CartItem2> expiredList = listRushGoodExpired(selectedList);

        // 存在抢购中商品过抢购期
        if (!expiredList.isEmpty()) {
            final MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                    .customView(R.layout.layout_dialog_rush_expired, false)
                    .build();
            View customView = dialog.getCustomView();
            View passView = customView.findViewById(R.id.rush_expired_dialog_tv_pass);  // 不买它了
            View goonView = customView.findViewById(R.id.rush_expired_dialog_tv_goon);  // 继续购买

            // 不买它了
            passView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    LogUtils.d(TAG, "selectedList before: " + selectedList.toString());
                    selectedList.removeAll(expiredList);
//                    LogUtils.d(TAG, "selectedList after: " + selectedList.toString());
                    toMakeOrder(selectedList);
                    dialog.dismiss();
                }
            });

            // 继续购买
            goonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toMakeOrder(selectedList);
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
        // 没有商品过抢购期
        else {
            toMakeOrder(selectedList);
        }

    }

    @NonNull
    private List<CartItem2> listRushGoodExpired(List<CartItem2> selectedList) {
        // 对本次接口返回的商品中，选中的那些在抢购中的商品进行处理，判断此时有无过期
        // 记录过期商品
        List<CartItem2> expiredList = new ArrayList<>();

        for (int i = 0; i < selectedList.size(); i++) {
            CartItem2 cartItem = selectedList.get(i);
            CartModel cartModel = cartItem.getCartModel();
            if (cartModel != null) {
                CartModel.ProductBean product = cartModel.getProduct();

                if (product != null) {
                    int product_type = product.getProduct_type();

                    // product_type 0: 抢购商品，1：每日新上
                    if (product_type == 0) {
                        CartModel.ProductSizeBean product_size = cartModel.getProduct_size();

                        if (product_size != null) {
                            CartModel.ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();

                            if (product_time_frame != null) {
                                boolean if_rush_to_purchasing = product_time_frame.isIf_rush_to_purchasing();
                                // 本次请求时在抢购中
                                if (if_rush_to_purchasing) {
                                    // 此刻商品是否已过期
                                    CartModel.ProductSizeBean.ProductTimeFrameBean.TimeFrameBean time_frame = product_time_frame.getTime_frame();
                                    if (time_frame != null) {
                                        String time_frame1 = time_frame.getTime_frame();
                                        if (CartHelper.isRushGoodExpired(time_frame1)) {
                                            expiredList.add(cartItem);
                                            continue;
                                        }
                                    }
                                }
                                // 不到抢购时间的商品这个字段也为false，就出错了，不应被过滤掉
                                // 刚才请求回来的商品是否已过期
//                                if (!if_rush_to_purchasing) {
//                                    goodExpired = true;
//                                    expiredIndexes.add(i);
//                                    continue;
//                                }

//                                // 此刻商品是否已过期
//                                ProductSizeBean.ProductTimeFrameBean.TimeFrameBean time_frame = product_time_frame.getTime_frame();
//                                if (time_frame != null) {
//                                    String time_frame1 = time_frame.getTime_frame();
//                                    if (CartHelper.isRushGoodExpired(time_frame1)) {
//                                        goodExpired = true;
//                                        expiredIndexes.add(i);
//                                        continue;
//                                    }
//                                }
                            }
                        }
                    }
                }

            }
        }
        return expiredList;
    }

    /**
     * 跳转到确认订单页
     *
     * @param selectedList 选择的商品
     */
    private void toMakeOrder(List<CartItem2> selectedList) {
        if (selectedList != null && !selectedList.isEmpty()) {
            OrderMakeActivity.actionStart(getContext(), selectedList, 0);
        }
    }

    private void printAllSelected() {
        List<CartItem2> list = new ArrayList<>();
        for (CartItem2 item : mRawList) {
            if (item.isChecked()) { // 只打印选中的商品
                list.add(item);
            }
        }
        LogUtils.d(TAG, "已选商品：" + list);
    }

    /**
     * 跳转页面
     *
     * @param cls      要填转到的页面
     * @param isFinish 是否关闭本页
     */
    public void jump(Class<?> cls, boolean isFinish) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
    }

    // 检查断网
    private void checkNet() {
        if (!NetStateUtils.isNetworkAvailable(getContext())) {
            LogUtils.d(TAG, "checkNet  无网");
            mTvNoNet.setVisibility(View.VISIBLE);
            isPerform = false;
        } else { // 有网  未登录
            LogUtils.d(TAG, "checkNet  有网");
            mTvNoNet.setVisibility(View.GONE);

            // 未登录时清空购物车
            mRawList.clear();
            mAdapter.notifyDataSetChanged();
            checkDataSetEmpty();

        }

    }

    /**
     * 购物车辅助类
     * <p>
     * Created by JackB on 2016/12/5.
     */
    public static class CartHelper {

        // 抢购商品的类型
        public enum RushType {
            rush_before, // 未到抢购期
            rush_in,  // 抢购中
            rush_after, // 已过抢购期
            rush_no  // 非抢购商品
        }

        private CartHelper() {
        }

        /**
         * 检查某商品是否下架
         * 下架情况：下架、或者商品、某规格被删除、抢购商品但product_time_frame为null（16.12.22）
         *
         * @param item
         * @return
         */
        public static boolean isOffShelf(CartItem2 item) {
            boolean ret = false;
            CartModel cartModel = item.getCartModel();
            if (cartModel != null) {
                CartModel.ProductBean product = cartModel.getProduct();
                boolean isRush = false;
                if (product != null) {
                    isRush = product.getProduct_type() == 0 ? true : false;
                    if (product.getOnsell() == 0) {
                        ret = true;
                    }

                    if (!TextUtils.isEmpty(product.getDeleted_at())) {
                        ret = true;
                    }
                }

                CartModel.ProductSizeBean product_size = cartModel.getProduct_size();
                if (product_size != null) {
                    if (!TextUtils.isEmpty(product_size.getDeleted_at())) {
                        ret = true;
                    }

                    if (isRush && product_size.getProduct_time_frame() == null) {
                        ret = true;
                    }
                }


            }
            return ret;
        }

        /**
         * 当前时间，抢购商品的状态（相应抢购时段前、中、后）
         * 前提是该商品为抢购商品
         *
         * @param timeIntervalStart
         * @return
         */
        public static RushType getRushGoodTypeInTime(String timeIntervalStart) {
            RushType ret = RushType.rush_no;

//            CartModel cartModel = cartItem.getCartModel();
//            if (cartModel != null) {
//                ProductBean product = cartModel.getProduct();
//
//                if (product != null) {
//                    // 非抢购商品
//                    if (product.getProduct_type() != 0){
//                        return RushType.rush_no;
//
//                    }
//                    // 抢购商品
//                    else {
//                        ProductSizeBean product_size = cartModel.getProduct_size();
//
//                        if (product_size != null) {
//                            ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();
//                            // 利用当前时间判断
//
//                            if (product_time_frame != null) {
//                                ProductSizeBean.ProductTimeFrameBean.TimeFrameBean time_frame = product_time_frame.getTime_frame();
//                                String time_frame1 = time_frame.getTime_frame();
//
//
//                            }
//
//                        }
//                    }
//                }
//
//            }

            if (!TextUtils.isEmpty(timeIntervalStart)) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    long timeStart = sdf.parse(timeIntervalStart).getTime();
                    long timeEnd = timeStart + 3 * 60 * 60 * 1000; // 过期时间

                    long currentTimeMillis = System.currentTimeMillis();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(currentTimeMillis);
                    String currentHHmm = sdf.format(calendar.getTime());

                    long currentHHmmMillis = sdf.parse(currentHHmm).getTime();
                    LogUtils.d(TAG, "timeIntervalStart: " + timeIntervalStart + ", currentHHmm: " + currentHHmm
                            + ",\ntimeStart: " + timeStart + ", currentHHmmMillis: " + currentHHmmMillis
                            + ",\ntimeEnd: " + timeEnd + ", currentHHmmMillis: " + currentHHmmMillis);

                    if (currentHHmmMillis < timeStart) {
                        ret = RushType.rush_before;
                    } else if (currentHHmmMillis > timeEnd) {
                        ret = RushType.rush_after;
                    } else {
                        ret = RushType.rush_in;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            return ret;
        }

        /**
         * 判断当前时间，抢购商品是否在相应抢购时段中
         *
         * @param timeIntervalStart
         * @return
         */
        public static boolean isRushGoodInRush(String timeIntervalStart) {
            return getRushGoodTypeInTime(timeIntervalStart) == RushType.rush_in ? true : false;
        }

        /**
         * 判断当前时间，该商品是否是抢购商品并且在相应抢购时段中
         *
         * @param cartItem 商品对象
         * @return
         */
        public static boolean isRushGoodInRush(CartItem2 cartItem) {
            boolean ret = false;

            if (cartItem != null) {
                CartModel cartModel = cartItem.getCartModel();

                if (cartModel != null) {
                    CartModel.ProductBean product = cartModel.getProduct();

                    if (product != null) {
                        boolean isRushGood = product.getProduct_type() == 0 ? true : false;

                        if (isRushGood) { // 如果是抢购商品
                            CartModel.ProductSizeBean product_size = cartModel.getProduct_size();

                            if (product_size != null) {
                                CartModel.ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();

                                if (product_time_frame != null) {
                                    CartModel.ProductSizeBean.ProductTimeFrameBean.TimeFrameBean time_frame = product_time_frame.getTime_frame();

                                    if (time_frame != null) {
                                        String time_frame1 = time_frame.getTime_frame();

                                        if (isRushGoodInRush(time_frame1)) {
                                            ret = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return ret;
        }

        /**
         * 判断当前时间，抢购商品是否已过相应抢购时段
         *
         * @param timeIntervalStart
         * @return
         */
        public static boolean isRushGoodExpired(String timeIntervalStart) {
//            return getRushGoodTypeInTime(timeIntervalStart) == RushType.rush_after ? true : false;
            // 修改抢购商品过期规则，当前时间不在抢购中时段即为过期
            return getRushGoodTypeInTime(timeIntervalStart) == RushType.rush_in ? false : true;
        }

        /**
         * 判断当前时间，该商品是否是抢购商品并且已过相应抢购时段
         *
         * @param cartItem
         * @return
         */
        public static boolean isRushGoodExpired(CartItem2 cartItem) {
            boolean ret = false;

            if (cartItem != null) {
                CartModel cartModel = cartItem.getCartModel();

                if (cartModel != null) {
                    CartModel.ProductBean product = cartModel.getProduct();

                    if (product != null) {
                        boolean isRushGood = product.getProduct_type() == 0 ? true : false;

                        if (isRushGood) { // 如果是抢购商品
                            CartModel.ProductSizeBean product_size = cartModel.getProduct_size();

                            if (product_size != null) {
                                CartModel.ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();

                                if (product_time_frame != null) {
                                    CartModel.ProductSizeBean.ProductTimeFrameBean.TimeFrameBean time_frame = product_time_frame.getTime_frame();

                                    if (time_frame != null) {
                                        String time_frame1 = time_frame.getTime_frame();

                                        if (isRushGoodExpired(time_frame1)) {
                                            ret = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return ret;
        }

        /**
         * 判断商品本身是否是抢购中商品（不用本地时间判断）
         *
         * @param cartModel
         * @return
         */
        public static boolean isProductSelfInRush(CartModel cartModel) {
            boolean ret = false;

            if (cartModel != null) {
                CartModel.ProductBean product = cartModel.getProduct();
                boolean isRushGood = false;

                if (product != null) {

                    if (product.getProduct_type() == 0) {
                        isRushGood = true;
                    }
                }

                if (isRushGood) {
                    CartModel.ProductSizeBean product_size = cartModel.getProduct_size();

                    if (product_size != null) {
                        CartModel.ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();

                        if (product_time_frame != null) {
                            if (product_time_frame.isIf_rush_to_purchasing()) {
                                ret = true;
                            }
                        }
                    }
                }
            }

            return ret;
        }

        // 有了新状态，抢购时间段前加入购物车。 // 需求理解错了，这样写是对的。
//        /**
//         * 判断此刻商品有没有过期
//         * 只保留时分，转换成毫秒去比较的
//         *
//         * @param timeIntervalStart 抢购时间段开始时间 如"13:00"
//         * @return
//         */
//        public static boolean isGoodExpired(String timeIntervalStart) {
//            boolean ret = false;
//
//            if (!TextUtils.isEmpty(timeIntervalStart)) {
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//                try {
//                    long timeStart = sdf.parse(timeIntervalStart).getTime();
//                    long timeEnd = timeStart + 3 * 60 * 60 * 1000; // 过期时间
//
//                    long currentTimeMillis = System.currentTimeMillis();
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(currentTimeMillis);
//                    String currentHHmm = sdf.format(calendar.getTime());
//
//                    long currentHHmmMillis = sdf.parse(currentHHmm).getTime();
//                    LogUtils.d(TAG, "timeIntervalStart: " + timeIntervalStart + ", currentHHmm: " + currentHHmm
//                            + ",\ntimeStart: " + timeStart + ", currentHHmmMillis: " + currentHHmmMillis
//                            + ",\ntimeEnd: " + timeEnd + ", currentHHmmMillis: " + currentHHmmMillis);
//
//                    if (currentHHmmMillis >= timeStart && currentHHmmMillis <= timeEnd) {
//                        ret = false;
//                    } else {
//                        ret = true;
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            return ret;
//        }
    }


    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getHeight() {
            return mTarget.getLayoutParams().height;
        }

        public void setHeight(int height) {
            mTarget.getLayoutParams().height = height;
            mTarget.requestLayout();
        }

    }
}
