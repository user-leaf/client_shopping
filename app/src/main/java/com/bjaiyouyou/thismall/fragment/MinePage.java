package com.bjaiyouyou.thismall.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alipay.sdk.app.AuthTask;
import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.MainApplication;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.AboutIUUActivity;
import com.bjaiyouyou.thismall.activity.AddressManagerNewActivity;
import com.bjaiyouyou.thismall.activity.HistoryBuyNewActivity;
import com.bjaiyouyou.thismall.activity.InviteActivity;
import com.bjaiyouyou.thismall.activity.LoginActivity;
import com.bjaiyouyou.thismall.activity.MineBingPhoneNumActivity;
import com.bjaiyouyou.thismall.activity.MineCustomerServiceSuggestionActivity;
import com.bjaiyouyou.thismall.activity.MyOrderActivity;
import com.bjaiyouyou.thismall.activity.PermissionsActivity;
import com.bjaiyouyou.thismall.activity.SettingsActivity;
import com.bjaiyouyou.thismall.activity.UpdateMineUserMessageActivity;
import com.bjaiyouyou.thismall.adapter.MineAdapter;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4Mine;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.BindingAlipayModel;
import com.bjaiyouyou.thismall.model.CheckIfBindingAlipayModel;
import com.bjaiyouyou.thismall.model.MyMine;
import com.bjaiyouyou.thismall.model.PermissionsChecker;
import com.bjaiyouyou.thismall.model.User;
import com.bjaiyouyou.thismall.pay.AuthResult;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.DialUtils;
import com.bjaiyouyou.thismall.utils.DialogUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.SPUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.utils.ValidatorsUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.bjaiyouyou.thismall.widget.NoScrollGridView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 个人页
 *
 * @author Alice
 * @author JackB
 * @date 2016/5/31
 * @date 2016/6/20
 */
public class MinePage extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String TAG = MinePage.class.getSimpleName();

    private IUUTitleBar mTitle;
    //    功能列表
    private NoScrollGridView gv;

    //记录用户积分，网络请求
    private int integral;
    //  功能按钮数据
    private List<MyMine> dataListLogin;
    private List<MyMine> dataListNotLogin;
    //功能按钮适配器
    private MineAdapter adapter;

    //    登陆后的头部
    private View mLoginView;

    //    登录入口
    private TextView mBtLogin;

    //    未登录的头部
    private View mNotLoginView;

    //最高级会员需要的UU数
    private int mGoldCoinAll = 3000;
    //UU的数量
    private int mGoldCoinNum = 0;
    //会员等级  网络获取
    private int mLevel;
    //会员头像地址
    private String mImgUrl;


    private TextView mTVGoldCoinNum;
    private TextView mTVIntegralNum;

    //用户的电话
    private TextView mTVTel;
    //名字
    private TextView mTvName;

    //用户
    private User mUser;
    private PullToRefreshScrollView mScrollView;
    //我的收益布局
    private LinearLayout mRLIncome;
    //我的佣金布局
    private LinearLayout mRLCommission;
    //众汇布局
    private LinearLayout mRLZhonghui;

    private LinearLayout mLLUnNetWork;
    //UU数量
    private long mCoin;
    //积分数量
    private long mIntegral;
    //记录用户是否登录
    private boolean isLogin = false;

    //用户头像
    private CircleImageView mIVUserIcon;


    private Intent mIntentSafeCode;
    //记录用户是否设置过安全码
    private boolean mIsHaveSafeCode = false;

    //是否有邮箱
    private boolean isHaveEmail = false;

    //用户邮箱
    private String mEmail;
    //用户安全码
    private String mSafeCode;
    //安全密码下限制
    private int mSafeCodeMinLimit = 8;
    private int mSafeCodeMaxLimit = 16;
    //屏幕高度
    private int mHeight;
    //用户会员图标
    private ImageView mIvMember;
    //第五季图标
    private ImageView mIvMemberFive;



    //提现布局
    //邮箱
    private TextView mTVEmail;
    //供货电话
    private LinearLayout mLLSupplyPhone;

    ////////////安全码对话框相关///////////////////////
    //安全码验证对话框
    private MaterialDialog mSafeCodeDialog;
    //找回安全码对话框
    private MaterialDialog mFindSafeCodeDialog;
    //邮件发送成功对话框
    private MaterialDialog mEmailSendSucceedDialog;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    //用户头像名称
    private String mImgName;
    //用户头像路径
    private String mUserImgUrl;
    //判断头像是否存在
    private boolean isHaveImg;
    //可提金额
    private TextView mTvWithdrawNum;

    //2为已升级为vip
    private int isVip;


    //网络请求对象
    private Api4Mine mClientApi;
    //拨打客服电话
    private LinearLayout mLLServicePhone;


    //是否绑定支付宝
    private boolean isBindingAlipay = false;


    private static final int SDK_AUTH_FLAG = 2;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        String userId = authResult.getUserId();
                        bindingAlipay(userId);
                        Log.e("authResult", "" + authResult.getResult().toString());
                        Log.e("userId", "" + authResult.getUserId());
                    } else {
                        // 其他状态值则为授权失败
                        ToastUtils.showShort("授权失败重新授权");
//                        dismissLoadingDialog();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_mine, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化权限检查器
        mPermissionsChecker = new PermissionsChecker(getContext());

        initView();
        setupView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();

        mIVUserIcon.setImageResource(R.mipmap.list_profile_photo);

        //判断登录与否隐藏显示头部和添加好友按钮
        String token = CurrentUserManager.getUserToken();
        if (TextUtils.isEmpty(token)) {
            isLogin = false;
            mLoginView.setVisibility(View.GONE);
            mNotLoginView.setVisibility(View.VISIBLE);
            initGridViewChange();
//            initGridView();
//            gv.setAlpha(0.6f);
//            mLLNeedSafe.setAlpha(0.6f);
            mTVIntegralNum.setText("" + 0);
            mTVGoldCoinNum.setText("" + 0);
            mTvWithdrawNum.setText("" + 0);
        } else {
            mLoginView.setVisibility(View.VISIBLE);
            mNotLoginView.setVisibility(View.GONE);
            //清空本地存储
            SPUtils.put(getContext(), Constants.USER, "");
            initData();
        }
        dialogDismiss(mFindSafeCodeDialog);
        dialogDismiss(mEmailSendSucceedDialog);
        dialogDismiss(mSafeCodeDialog);

    }


    private void initView() {
        mHeight = ScreenUtils.getScreenHeight(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight / 5);
        //登录后的头部布局
        mLoginView = layout.findViewById(R.id.rl_mine_head_login);
        //没登录的头部布局
        mNotLoginView = layout.findViewById(R.id.ll_mine_head_notlogin);
        mLoginView.setLayoutParams(params);
        mNotLoginView.setLayoutParams(params);


        //断网提示
        mLLUnNetWork = ((LinearLayout) layout.findViewById(R.id.ll_mine_un_network));
//        //个人设置图标
//        titleBar.setRightImageResource(R.mipmap.nav_set);
        //功能列表
        gv = ((NoScrollGridView) layout.findViewById(R.id.gv_mine));
        initGridView();
        //未登录布局中的登录按钮
        mBtLogin = ((TextView) layout.findViewById(R.id.bt_mine_login));


        //用户登录的基本信息
        //用户头像
        mIVUserIcon = ((CircleImageView) layout.findViewById(R.id.iv_mine_head));
        //用户名称
        mTvName = ((TextView) layout.findViewById(R.id.tv_mine_ll_login_name));
        //电话
        mTVTel = ((TextView) layout.findViewById(R.id.tv_mine_tel));

        //提现金额
        mTvWithdrawNum = ((TextView) layout.findViewById(R.id.tv_mine_withdraw_num));

        //我的收益
        mRLIncome = ((LinearLayout) layout.findViewById(R.id.rl_mine_income));
        //我的佣金
        mRLCommission = ((LinearLayout) layout.findViewById(R.id.rl_mine_commission));
        //支付方式布局
        mRLZhonghui = ((LinearLayout) layout.findViewById(R.id.rl_mine_zhonghui));


        mTVIntegralNum = ((TextView) layout.findViewById(R.id.tv_mine_integral_num));
        //UU
        mTVGoldCoinNum = ((TextView) layout.findViewById(R.id.tv_mine_goldcoin_num));
        //最外层的刷新
        mScrollView = ((PullToRefreshScrollView) layout.findViewById(R.id.ptsv_mine));

        //会员图标
        mIvMember = ((ImageView) layout.findViewById(R.id.iv_mine_member));
        mIvMemberFive = ((ImageView) layout.findViewById(R.id.iv_mine_member_five));

        //供货电话
        mLLSupplyPhone = ((LinearLayout) layout.findViewById(R.id.ll_mine_supply_the_phone));
        //供货电话
        mLLServicePhone = ((LinearLayout) layout.findViewById(R.id.ll_mine_service_the_phone));
        //邮箱
        mTVEmail = ((TextView) layout.findViewById(R.id.tv_mine_email));

    }

    private void setupView() {
        gv.setOnItemClickListener(this);
        mLoginView.setOnClickListener(this);
        // 解决页面不从顶部开始
        gv.setFocusable(false);
        mIVUserIcon.setOnClickListener(this);

        //我的收益
        mRLIncome.setOnClickListener(this);
        //我的佣金
        mRLCommission.setOnClickListener(this);
        //我的众汇
        mRLZhonghui.setOnClickListener(this);
//        titleBar.setRightLayoutClickListener(this);
        mLoginView.setOnClickListener(this);
        mBtLogin.setOnClickListener(this);
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mScrollView.isRefreshing()) {
                    mScrollView.onRefreshComplete();
                    initData();
                }
            }
        });

        mLLSupplyPhone.setOnClickListener(this);
        mLLServicePhone.setOnClickListener(this);
    }

    /**
     * 网络请求数据
     * 先请求退款接口
     * 再获取个人信息
     */

    private void initData() {
        //取消数据加载Loading
//        showLoadingDialog();
        //退款接口为二次请求相应接口，先请求退款接口，（成功与否对获取用户信息不影响）再获取用户信息
        ClientAPI.getWithdraw(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                initUserData();
            }

            @Override
            public void onResponse(String response, int id) {
                initUserData();
            }
        });

    }

    /**
     * 获取用户信息
     */

    private void initUserData() {
        //初始化网路请求对象

        mClientApi = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);

        //模拟我的邀请好友假数据
//        friendList=new ArrayList<>();
        String token = CurrentUserManager.getUserToken();
        if (token != null) {
            mClientApi.getUserMessage(TAG, new DataCallback<User>(getContext()) {
                @Override
                public void onFail(Call call, Exception e, int id) {
                    //断网提示
                    if (!NetStateUtils.isNetworkAvailable(getContext())) {
                        mLLUnNetWork.setVisibility(View.VISIBLE);
                    } else {
                        mLLUnNetWork.setVisibility(View.GONE);
                        String eString = e.toString();
                        LogUtils.e("--eString:", eString);
                        if (eString != null) {
                            if (eString.contains("400") || eString.contains("401")) {
                            } else {
//                Toast.makeText(context,"提交失败"+e.toString(),Toast.LENGTH_SHORT).show();
                                ToastUtils.showException(e, getContext());
                            }
                        }
                    }
                    //token是否过期，过期后对头部的显示隐藏进行处理
                    isLogin = false;
                    mLoginView.setVisibility(View.GONE);
                    mNotLoginView.setVisibility(View.VISIBLE);
                    mTVIntegralNum.setText("" + 0);
                    mTVGoldCoinNum.setText("" + 0);
                    mTvWithdrawNum.setText("" + 0);
                    initGridViewChange();
//                    gv.setAlpha(0.6f);
//                    mLLNeedSafe.setAlpha(0.6f);
                    //取消数据加载Loading
//                    dismissLoadingDialog();
                }

                @Override
                public void onSuccess(Object response, int id) {
                    isLogin = true;
                    //隐藏网络请求
                    mLLUnNetWork.setVisibility(View.GONE);
                    mLoginView.setVisibility(View.VISIBLE);
                    mNotLoginView.setVisibility(View.GONE);
                    if (response != null) {
//                        mUser = new Gson().fromJson((JsonElement) response, User.class);
                        mUser = (User) response;
                        setData();
                        //本地存储个人信息
                        /**
                         * 频繁报错
                         */
//                        SPUtils.put(getContext(), Constants.USER, response+"");
//                        ACache aCache=ACache.get(getActivity());
//                        aCache.put(Constants.USER,mUser);
//                        SPUtils.put(MainApplication.getContext(), Constants.USER, mUser + "");
                    } else {
                        ToastUtils.showShort("数据加载错误");
                    }
//                    dismissLoadingDialog();

                }
            });
        } else {
            Toast.makeText(getActivity(), "请先登录后再回来哦", Toast.LENGTH_SHORT).show();
//            dismissLoadingDialog();
        }

        //设置背景透明度
//        initGridView();
    }

    private void setData() {
//        dismissLoadingDialog();
        //重新设置头像为默认头像
//        mIVUserIcon.setImageResource(R.mipmap.list_profile_photo);
        User.MemberBean memberBean = mUser.getMember();
        initGridViewChange();
//        gv.setAlpha(1);
//        mLLNeedSafe.setAlpha(1);

//        //用户名
//        mTvName.setText(memberBean.getName());
//        //昵称
        if (memberBean != null) {
            String name=memberBean.getNick_name();
            if (!TextUtils.isEmpty(name)){
                mTvName.setText(name);
            }else {
                mTvName.setText("");
            }

            String tel = memberBean.getPhone().trim();
            if (!TextUtils.isEmpty(tel)) {
                tel = tel.substring(0, 3) + "****" + tel.substring(tel.length() - 3, tel.length());
                mTVTel.setText(tel);
            }else {
                mTVTel.setText("");
            }

            //test
//        mEmail="";
//        mEmail="aiyouyou_bj@163.com";

            mEmail = memberBean.getEmail().trim();
            if (!TextUtils.isEmpty(mEmail)) {
                isHaveEmail = true;
                String email = mEmail.substring(0, 2) + "****" + mEmail.substring(mEmail.indexOf("@"), mEmail.length());
                mTVEmail.setText(email);
            } else {
                isHaveEmail = false;
                mTVEmail.setText("");
            }

            //test
//            mSafeCode = "11";

            if (!TextUtils.isEmpty(mSafeCode)) {
                mIsHaveSafeCode = true;
            } else {
                mIsHaveSafeCode = false;
            }
            mSafeCode = memberBean.getSecurity_code_hint();
            mIsHaveSafeCode = memberBean.isSecurity_code_state();

            mCoin = memberBean.getMoney_quantity();
            isVip = memberBean.getIs_vip();
            mTVGoldCoinNum.setText(mCoin + "");
            mIntegral = memberBean.getIntegration();
            mTVIntegralNum.setText(mIntegral + "");


            String inCome = memberBean.getPush_money();

            int inComeNum = 0;
            if (!TextUtils.isEmpty(inCome)) {
                inComeNum = Integer.valueOf(inCome);
            }

            String canDraw = memberBean.getCan_drawings_amount();

            double canDrawNum = 0;
            if (!TextUtils.isEmpty(canDraw)) {
                canDrawNum = Double.valueOf(canDraw);
            }

            LogUtils.e("inCome", "" + inCome);
            LogUtils.e("canDraw", "" + canDraw);

            //可提现数
            int money = (int) (inComeNum + canDrawNum);
            mTvWithdrawNum.setText("" + money);

            mImgUrl = memberBean.getAvatar_path();
            mImgName = memberBean.getAvatar_name();


            //加载头像
            if (!TextUtils.isEmpty(mImgUrl) && !TextUtils.isEmpty(mImgName)) {
                isHaveImg = true;
                mUserImgUrl = mImgUrl + "/" + mImgName;
                LogUtils.e("UserImgUrl", mUserImgUrl);
                Glide.with(MainApplication.getContext())
                        .load(mUserImgUrl)
//                        .signature(new StringSignature(UUID.randomUUID().toString()))
                        //不注释掉清除缓存失效
//                        .placeholder(R.mipmap.list_profile_photo)
//                        .error(R.mipmap.list_profile_photo)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                        .skipMemoryCache(true)//跳过内存缓存
                        .into(mIVUserIcon);
            } else {
                isHaveImg = false;
            }

            mLevel = memberBean.getMember_level();
            //test
//            mLevel = 2;
            //修改会员等级的进度
            if (mLevel != 0) {
                if (mLevel == 1) {
                    mGoldCoinNum = 1500;
                } else if (mLevel == 2) {
                    mGoldCoinNum = 2500;
                } else if (mLevel == 3) {
                    mGoldCoinNum = 3000;
                }
            } else {
                mGoldCoinNum = 0;
            }


            //根据数据判断显示图标
            LogUtils.e("getMember_type", "" + memberBean.getMember_type());
            //test
//           if (memberBean.getMember_type()==4){
            if (memberBean.getMember_type() == 5) {
                mIvMemberFive.setVisibility(View.VISIBLE);
            } else {
                mIvMemberFive.setVisibility(View.GONE);
            }

            LogUtils.e("getIs_vip", "" + memberBean.getIs_vip());
            //test
//           if (memberBean.getIs_vip()==0){
            if (memberBean.getIs_vip() == 2) {
                mIvMember.setVisibility(View.VISIBLE);
            } else {
                mIvMember.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 布局功能控价列表
     */
    private void initGridView() {
        initNotLoginGv();
    }

    /**
     * 改变功能布局的显示
     */
    private void initGridViewChange() {
        if (isLogin) {
            initLoginGv();

        } else {
            initNotLoginGv();
        }
    }

    /**
     * 登录功能布局的初始化
     */
    private void initLoginGv() {
        //已经登录的布局初始化
        dataListLogin = new ArrayList<>();

        MyMine mine1 = new MyMine("我的订单", R.mipmap.list_order);
        dataListLogin.add(mine1);
        MyMine mine2 = new MyMine("历史购物", R.mipmap.list_historyrecorde);
        dataListLogin.add(mine2);
        MyMine mine3 = new MyMine("收货地址", R.mipmap.list_icon_address);
        dataListLogin.add(mine3);
        MyMine mine4 = new MyMine("邀请好友", R.mipmap.list_addfriend);
        dataListLogin.add(mine4);
        MyMine mine5 = new MyMine("关于我们", R.mipmap.list_aiyouyou);
        dataListLogin.add(mine5);
        MyMine mine6 = new MyMine("意见反馈", R.mipmap.list_icon_customerservice);
        dataListLogin.add(mine6);
        MyMine mine7 = new MyMine("通用设置", R.mipmap.list_set);
        dataListLogin.add(mine7);
        //test
//        MyMine mine8 = new MyMine("我的兑换券", R.mipmap.list_set);
//        dataListLogin.add(mine8);

        adapter = new MineAdapter(getActivity(), gv, dataListLogin);
        gv.setAdapter(adapter);
    }

    /**
     * 未登录功能布局的初始化
     */

    private void initNotLoginGv() {
        //未登录的布局初始化
        dataListNotLogin = new ArrayList<>();
        MyMine mine11 = new MyMine("我的订单", R.mipmap.list_order_nor);
        dataListNotLogin.add(mine11);
        MyMine mine22 = new MyMine("历史购物", R.mipmap.list_historyrecorde_nor);
        dataListNotLogin.add(mine22);
        MyMine mine33 = new MyMine("收货地址", R.mipmap.list_addressinformation_nor);
        dataListNotLogin.add(mine33);
        MyMine mine44 = new MyMine("邀请好友", R.mipmap.list_addfriend_nor);
        dataListNotLogin.add(mine44);
        MyMine mine55 = new MyMine("关于我们", R.mipmap.list_aiyouyou_nor);
        dataListNotLogin.add(mine55);
        MyMine mine66 = new MyMine("意见反馈", R.mipmap.list_icon_customerservice_nor);
        dataListNotLogin.add(mine66);
        MyMine mine77 = new MyMine("通用设置", R.mipmap.list_set_nor);
        dataListNotLogin.add(mine77);

        //test
//        MyMine mine8= new MyMine("我的兑换券", R.mipmap.list_signinwechat);
//        dataListNotLogin.add(mine8);
//        MyMine mine9= new MyMine("绑定QQ", R.mipmap.list_signinqq);
//        dataList.add(mine9);

        adapter = new MineAdapter(getActivity(), gv, dataListNotLogin);
        gv.setAdapter(adapter);
    }


    ////////////////////////////////////////提现处理支付宝/////////////////////////////////////////

    /**
     * 判断是否绑定支付宝
     */
    private void getIfBindingAlipay() {
        //数据加载Loading
//        showLoadingDialog();
        mClientApi.getIfBindingAlipay(new DataCallback<CheckIfBindingAlipayModel>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                ToastUtils.exceptionToast(e, getContext());
//                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response != null) {
                    CheckIfBindingAlipayModel model = (CheckIfBindingAlipayModel) response;
                    isBindingAlipay = model.isIs_bound_alipay();

                    //绑定支付宝
                    if (isBindingAlipay) {
                        //登录
                        if (isLogin) {
                            //安全码验证
                            createSafeCodeDialog();
                            //没登录
                        } else {
                            //绑定直接跳转到提现页面
                            startActivity(mIntentSafeCode);
                        }

//                        startActivity(mIntentSafeCode);

                    } else {
                        Dialog dialog = DialogUtils.createConfirmDialog(getContext(), null, "绑定支付宝账号，一经绑定不能修改，是否继续？", "绑定", "取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getAuthorizationParameters();
                                        dialog.dismiss();
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }
                        );
                        dialog.show();
                    }

                }
            }
        });
    }


    ///////////////////////////////支付宝授权////////////////////////////////////////////

    /**
     * 获得支付宝的授权参数
     * 成功授权，AlipayAuthorization()
     * 不成功提示
     */
    private void getAuthorizationParameters() {
        //DataCallback<String>(getContext())不能识别接口返回的数据

//        mClientApi.getAuthorizationParameters(new DataCallback<String>(getContext()) {
//            @Override
//            public void onFail(Call call, Exception e, int id) {
//                ToastUtils.exceptionToast(e,getContext());
//                //test
////                String authorizationParameters= "";
////                authV2(authorizationParameters);
//            }
//
//            @Override
//            public void onSuccess(Object response, int id) {
//                    String authorizationParameters= (String) response.toString().trim();
//                    authV2(authorizationParameters);
//
//            }
//        });
        mClientApi.getAuthorizationParameters(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.exceptionToast(e, getContext());
                //test
//                String authorizationParameters= "";
//                authV2(authorizationParameters);
//                dismissLoadingDialog();

            }

            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response)) {
                    String authorizationParameters = response.toString().trim();
                    authV2(authorizationParameters);
                } else {
                    ToastUtils.showShort("服务器处理中");
                }


            }
        });

    }

    /**
     * 绑定支付宝
     * 成功提现
     * 不成功提示
     */
    private void bindingAlipay(String userId) {
        mClientApi.bindingAlipay(userId, new DataCallback<BindingAlipayModel>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
//                ToastUtils.exceptionToast(e, getContext());
                ToastUtils.showShort("绑定支付宝失败");
                //取消数据加载Loading
//                dismissLoadingDialog();
                return;
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response!=null){
                    //绑定直接跳转到提现页面
//                ToastUtils.showShort("绑定支付宝成功，跳转提现页面");
                    //直接跳转我的兑换券页面
//                    startActivity(mIntentSafeCode);
                    //去掉安全码验证
                    createSafeCodeDialog();
                    //取消对话框
//                mBindingAlipayDialog.dismiss();
                    //取消数据加载Loading
//                    dismissLoadingDialog();

                }
            }
        });
    }


    /**
     * 支付宝登录授期权,获得UserId，
     * 绑定支付宝
     * 成功绑定
     * 不成功提示
     * <p/>
     * 获得了授权参数的时候调用
     * 支付宝账户授权业务
     *
     * @param authInfo
     */
    public void authV2(String authInfo) {
//    public void alipayAuthorization(String authInfo) {
        //test
//        authInfo  = "apiname=com.alipay.account.auth&app_id=2017032106317944&app_name=mc&auth_type=AUTHACCOUNT&biz_type=openservice&pid=2088421738853841&product_id=APP_FAST_LOGIN&scope=kuaijie&sign_type=RSA2&target_id=2088421738853841&sign=ehOEhQjCUOLCNdJVtzQWioGS%2Btt1u9yuS4ShuqHoLZdtEbqZAkt3uTi9ohV2IXCfesVHKbAwQMkHrNlHZje5bozT0JLpU0rpHIi1wTgsGjbAuq45XTI7GoefqmGpfn1T5Z1y4s0H2JRQ3mKEFSJjrfhCtMq3%2B07S5HlKXdJX7fXq1pS2EP%2F%2FUB%2FFv5IEkmvZalaHkUtWLqjB2pVd0vucvReH6Y9531xe8Hn4lYgZ3OuPtrxlIpOhLSs5Rt6fuox9gisPhaHiLWBjxCKUZy%2B4E65B05KZg2Jbgt8V%2BaxNZTIRvcBK7DxEeKPPBqBPyDRQoDitSR2SULDSKlQUJxB1uA%3D%3D";

        LogUtils.e("authInfo", authInfo);
        final String finalAuthInfo = authInfo;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(getActivity());
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(finalAuthInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }


    /**
     * 跳转到修改个人信息
     */
    private void updateMineUserMessage() {
        mIntentSafeCode = new Intent(getActivity(), UpdateMineUserMessageActivity.class);
        mIntentSafeCode.putExtra("mUser", mUser);
        if (isHaveImg) {
            mIntentSafeCode.putExtra("mUserImgUrl", mUserImgUrl);
        } else {
            mIntentSafeCode.putExtra("mUserImgUrl", "");
        }
//        showSafeCodePopWin();
        createSafeCodeDialog();
//                startActivity(mIntentSafeCode);
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

    /**
     * 绑定微信
     * 绑定qq
     * （绑定手机号）
     */
    private void bindPhone() {
        Intent bindIntent = new Intent(getActivity(), MineBingPhoneNumActivity.class);
        bindIntent.putExtra("isLogin", false);
        startActivity(bindIntent);
    }


    ////////////////////////////////////////////////安全码弹框处理/////////////////////////////////////////////

    private EditText mEtEmail;
    //安全码输入框
    private EditText etSateCodeInput;
    //确认安全码输入框
    private EditText etSateCodeMakeSure;
    //安全码输入标题
    private TextView tvSateCodeMode;
    //提交按钮
    private TextView tvSateCodeSubmit;
    //忘记安全码
    private TextView tvSateCodeForget;
    //关闭图标
    private ImageView ivSateCodeClose;
    //设置安全码找回邮箱布局
    private LinearLayout mLLSetEmail;


    //不允许输入空格
    private InputFilter etInputFilter=new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符

            if(source.equals(" "))return "";      else return null;   }
    };

    /**
     *
     * 生成安全码对话框
     *
     */
    private void createSafeCodeDialog() {
        if (mSafeCodeDialog != null) {
            mSafeCodeDialog.dismiss();
        }
        mSafeCodeDialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.mine_safe_code_layout, false)
                .build();

        View view = mSafeCodeDialog.getCustomView();
        mSafeCodeDialog.setCancelable(false);
        //设置显示动画
//        mSafeCodeDialog.getWindow().setWindowAnimations(R.style.Dialog_Anim_Style_Up_Down);

        etSateCodeInput = (EditText) view.findViewById(R.id.et_safe_code_input);
        etSateCodeMakeSure = (EditText) view.findViewById(R.id.et_safe_code_input_makesure);
        tvSateCodeMode = (TextView) view.findViewById(R.id.tv_safe_code_mode);
        tvSateCodeSubmit = (TextView) view.findViewById(R.id.tv_safe_code_submit);
        tvSateCodeForget = (TextView) view.findViewById(R.id.tv_safe_code_forget);
        tvSateCodeForget = (TextView) view.findViewById(R.id.tv_safe_code_forget);
        ivSateCodeClose = (ImageView) view.findViewById(R.id.iv_safe_code_close);
        mLLSetEmail = ((LinearLayout) view.findViewById(R.id.ll_safe_code_email));
        mEtEmail = ((EditText) view.findViewById(R.id.et_safe_code_input_email));

        //设置不允许输入空格
        etSateCodeInput.setFilters(new InputFilter[]{etInputFilter});



        //添加输入字符上限判断
        etSateCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String safeCode = s.toString();
                int inPutLength = safeCode.length();
                if (inPutLength > mSafeCodeMaxLimit) {
                    Toast.makeText(getContext(), "安全码不超过" + mSafeCodeMaxLimit + "位", Toast.LENGTH_SHORT).show();
                    etSateCodeInput.setText(safeCode.substring(0, mSafeCodeMaxLimit));
                    etSateCodeInput.setSelection(mSafeCodeMaxLimit);
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // 设置按钮监听
        ivSateCodeClose.setOnClickListener(this);
        tvSateCodeSubmit.setOnClickListener(this);
        tvSateCodeForget.setOnClickListener(this);
//        ivSateCodeClose.setOnClickListener(this);
//        tvSateCodeSubmit.setOnClickListener(this);
//        tvSateCodeForget.setOnClickListener(this);


        //判断存不存在安全码进行页面显示变化
        if (mIsHaveSafeCode) {
            //安全码验证将输入类型改为密码
            //方法一： 会引起EditText中android:digits失效问题
//            etSateCodeInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            etSateCodeInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

            etSateCodeInput.setText("");
            tvSateCodeMode.setText("输入安全码");
            tvSateCodeMode.setGravity(Gravity.CENTER);
            tvSateCodeForget.setEnabled(true);
            tvSateCodeForget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            etSateCodeMakeSure.setVisibility(View.GONE);
            etSateCodeInput.setHint(mSafeCode + "********");
            mLLSetEmail.setVisibility(View.GONE);
        } else {
            if (isHaveEmail) {
                tvSateCodeMode.setText("设置安全码");
                tvSateCodeMode.setGravity(Gravity.CENTER);
                tvSateCodeForget.setEnabled(false);
                etSateCodeMakeSure.setVisibility(View.GONE);
                etSateCodeInput.setHint("8~16位，数字、字母组合");
                mLLSetEmail.setVisibility(View.GONE);
            } else {
                tvSateCodeMode.setText("设置安全码");
                tvSateCodeForget.setEnabled(false);
                etSateCodeMakeSure.setVisibility(View.GONE);
                etSateCodeInput.setHint("8~16位，数字、字母组合");
                mLLSetEmail.setVisibility(View.VISIBLE);
            }
        }

        //对话框显示
        mSafeCodeDialog.show();
    }




    /**
     *
     *
     * 验证安全码处理，对话框的点击事件
     *
     *
     *
     *
     */

//    private OnNoDoubleClickListener onClickListener = new OnNoDoubleClickListener() {
//        @Override
//        public void onNoDoubleClick(View view) {
//            switch (view.getId()) {
//                //提交安全码，判断是否已经有安全码分别调用两个接口
//                //有：验证
//                case R.id.tv_safe_code_submit:
//                    //处理安全码
//                    submitSafeCode();
//                    break;
//                //跳转到忘记密码处理, popWin
//                case R.id.tv_safe_code_forget:
//                    dialogDismiss(mSafeCodeDialog);
//                    createFindSafeCodeDialog();
//                    break;
//                //关闭安全码验证的窗口
//                case R.id.iv_safe_code_close:
//                    dialogDismiss(mSafeCodeDialog);
//                    break;
//
//                //忘记密码页面，验证邮箱提交
//                case R.id.tv_safe_code_forget_submit:
//                    emailValidate();
//                    break;
//
//                //忘记你密码页面联系客服
//                case R.id.tv_safe_code_forget_callcentre:
//                    //拨打客服电话
//                    DialUtils.callCentre(getContext(), DialUtils.CENTER_NUM);
//                    //Test
//                    dialogDismiss(mFindSafeCodeDialog);
//                    break;
//                //关闭邮箱验证的窗口
//                case R.id.iv_safe_code_forget_close:
//                    dialogDismiss(mFindSafeCodeDialog);
//                    break;
//                //关闭邮箱验证成功的窗口
//                case R.id.iv_email_send_succeed:
//                    dialogDismiss(mEmailSendSucceedDialog);
//                    break;
//            }
//
//        }
//    };


    /**
     *
     *
     * 验证邮箱提交
     *
     *
     *
     */
    private void emailValidate() {
        String emailReset = etSateCodeReGetEmailInput.getText().toString().trim();
//        tvSateCodeFindSubmit.setEnabled(false);
        LogUtils.e("emailReset----", mEmail);
        if (isHaveEmail) {
            if (mEmail.equals(emailReset)) {
                //调用邮件发送接口
                String token = CurrentUserManager.getUserToken();
                if (!TextUtils.isEmpty(token)) {
                    ClientAPI.sendEmailResetCode(token, mEmail, new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
//                            createSafeCodeForgetPopWin.tvSateCodeSubmit.setEnabled(true);
                            UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                            dialogDismiss(mFindSafeCodeDialog);
//                            tvSateCodeFindSubmit.setEnabled(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            //提交成功后就跳转到邮件发送成功页面
                            //创建发送成功对话框
                            dialogDismiss(mFindSafeCodeDialog);
                            createEmailSendSucceedDialog();
//                            tvSateCodeFindSubmit.setEnabled(true);
                            return;
                        }
                    });
                }

            } else {
                Toast.makeText(getContext(), "输入邮箱与预设邮箱不相符", Toast.LENGTH_SHORT).show();
//                tvSateCodeFindSubmit.setEnabled(true);
                return;
            }
        } else {
            Toast.makeText(getContext(), "未设置预设邮箱，请直接拨打客服找回安全码", Toast.LENGTH_SHORT).show();
//            tvSateCodeFindSubmit.setEnabled(true);
            return;
        }
    }

    /**
     * 提交弹出框的安全码
     */
    private void submitSafeCode() {

        //提交成功条状到   mClassJump  类
        //没有：添加
        String safeCode = etSateCodeInput.getText().toString();
        int minLeght = safeCode.length();
        if (minLeght < mSafeCodeMinLimit) {
            Toast.makeText(getContext(), "安全密码不能少于8位数", Toast.LENGTH_SHORT).show();
            return;
        }
        String token = CurrentUserManager.getUserToken().toString().trim();
        //验证安全码
        if (mIsHaveSafeCode) {
            ClientAPI.postValidateSafeCode(token, safeCode, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    LogUtils.e("getMessage-----", e.getMessage());
                    UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                    dialogDismiss(mSafeCodeDialog);
                }

                @Override
                public void onResponse(String response, int id) {
                    startActivity(mIntentSafeCode);
                    dialogDismiss(mSafeCodeDialog);
                }
            });
            return;
        } else {
            //设置安全码 mIsHaveSafeCode=true
            //情况一：邮箱未设置
            if (!isHaveEmail) {
                String email = mEtEmail.getText().toString().trim();
                if (!(!TextUtils.isEmpty(email)  && email.length() > 1&& ValidatorsUtils.isEmail(email))) {
                    Toast.makeText(getContext(), "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }
                ClientAPI.postSetSafeCode(token, safeCode, email, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                        dialogDismiss(mSafeCodeDialog);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //设置成功刷新数据
                        initData();
                        dialogDismiss(mSafeCodeDialog);
                    }
                });
            } else {
                //情况一：邮箱已经设置
                ClientAPI.postSetSafeCode(token, safeCode, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                        dialogDismiss(mSafeCodeDialog);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //设置成功刷新数据
                        initData();
                        dialogDismiss(mSafeCodeDialog);
                    }
                });

            }
        }

        dialogDismiss(mSafeCodeDialog);
    }

    private void dialogDismiss(MaterialDialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }

    }

    ///////////////////////////处理忘记密码的对话框/////////////////////////////////////////////////


    /**
     *
     *
     * 创建邮箱验证的对话框
     *
     *
     */

    //邮箱输入
    private EditText etSateCodeReGetEmailInput;
    //标题
    private TextView tvSateCodeEmail;
    //提交按钮
    private TextView tvSateCodeFindSubmit;
    //呼叫客服中心
    private TextView tvSateCodeCallCentre;
    //窗口关闭按钮
    private ImageView ivSateCodeFindClose;

    private void createFindSafeCodeDialog() {
        if (mFindSafeCodeDialog != null) {
            mFindSafeCodeDialog.dismiss();
        }

        mFindSafeCodeDialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.mine_safe_code_forget_layout, false)
                .build();
        View view = mFindSafeCodeDialog.getCustomView();
//        mFindSafeCodeDialog.setCancelable(false);

        etSateCodeReGetEmailInput = (EditText) view.findViewById(R.id.tv_input_safe_code_forget);
        tvSateCodeEmail = (TextView) view.findViewById(R.id.tv_email_safe_code_forget);
        tvSateCodeFindSubmit = (TextView) view.findViewById(R.id.tv_safe_code_forget_submit);
        tvSateCodeCallCentre = (TextView) view.findViewById(R.id.tv_safe_code_forget_callcentre);
        ivSateCodeFindClose = (ImageView) view.findViewById(R.id.iv_safe_code_forget_close);

        tvSateCodeCallCentre.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        etSateCodeReGetEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                tvSateCodeFindSubmit.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        // 设置按钮监听确定
        tvSateCodeFindSubmit.setOnClickListener(this);
        //联系客服
        tvSateCodeCallCentre.setOnClickListener(this);
        //关闭窗体
        ivSateCodeFindClose.setOnClickListener(this);
        // 设置按钮监听确定
//        tvSateCodeFindSubmit.setOnClickListener(this);
//        //联系客服
//        tvSateCodeCallCentre.setOnClickListener(this);
//        //关闭窗体
//        ivSateCodeFindClose.setOnClickListener(this);
        //根据状态设置页面显示
        //登录、添加过

//        //test
//        mEmail="111111999@dhh.com";
//        isHaveEmail=true;
        if (isHaveEmail) {
            String email = mEmail.substring(0, 2) + "****" + mEmail.substring(mEmail.indexOf("@"), mEmail.length());

            etSateCodeReGetEmailInput.setHint(email);
            etSateCodeReGetEmailInput.setEnabled(true);
        } else {
            etSateCodeReGetEmailInput.setHint("未绑定邮箱，请直接联系客服找回");
            etSateCodeReGetEmailInput.setEnabled(false);
        }
        mFindSafeCodeDialog.show();
    }


    ///////////////////////////邮箱验证成功的对话框//////////////////////////////////////////////////

    /**
     *
     *
     * 创建邮箱验证成功的对话框
     *
     *
     *
     */

    private ImageView ivEmailSendSucceedClose;

    private void createEmailSendSucceedDialog() {
        if (mEmailSendSucceedDialog != null) {
            mEmailSendSucceedDialog.dismiss();
        }
        mEmailSendSucceedDialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.mine_email_send_succeed_layout, false)
                .build();
        View view = mEmailSendSucceedDialog.getCustomView();
        mEmailSendSucceedDialog.setCancelable(false);

        ivEmailSendSucceedClose = ((ImageView) view.findViewById(R.id.iv_email_send_succeed));
        ivEmailSendSucceedClose.setOnClickListener(this);
//        ivEmailSendSucceedClose.setOnClickListener(this);

        mEmailSendSucceedDialog.show();

    }


    ////////////////////////////////////////////处理拨打打电话////////////////////////////////////////////////////

    /**
     * 拨打客服电话
     */
    private void callCustomerServerPhone() {
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            LogUtils.e("缺少拨打电话权限", "");
            startPermissionsActivity();
        } else {
            //已经授权直接拨打电话
            DialUtils.callCentre(getContext(), DialUtils.SUPPLY_PHONE);
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(getActivity(), DialUtils.REQUEST_CODE, PERMISSIONS);
    }

    // 提示去公众号绑定微信的
    private void showBindingWeChatDialog() {
        final MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.notify_binding_wechat_dialog_item, false)
                .build();
        View view = dialog.getCustomView();
        dialog.setCancelable(false);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_mine_member_dialog_close);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 修改窗口大小
        // http://blog.csdn.net/misly_vinky/article/details/19109517
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        dialogWindow.setGravity(Gravity.CENTER);

        dialog.show();
    }



    ////////////////////////////点击事件处理////////////////////////////
    /**
     *
     *
     *
     *
     * @param parent
     * @param view
     * @param position
     * @param id       处理条目点击事件
     *
     *
     *
     *
     *
     *
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                // 我的订单
                jump(MyOrderActivity.class, false);
                break;
            case 1:
                Intent phIntent = new Intent(getActivity(), HistoryBuyNewActivity.class);
                //携带数据接口
                phIntent.putExtra("title", "历史购买");
                startActivity(phIntent);
                break;
            case 2:
                //Toast.makeText(getActivity(), "收货地址", Toast.LENGTH_SHORT).show();

                jump(AddressManagerNewActivity.class, false);
                break;
            case 3:
                //Toast.makeText(getActivity(), "邀请好友", Toast.LENGTH_SHORT).show();
//                jump(MobileContactActivity.class,false);
                jump(InviteActivity.class, false);
                break;
            case 4:
                // Toast.makeText(getActivity(), "关于我们", Toast.LENGTH_SHORT).show();
                jump(AboutIUUActivity.class, false);
                break;
            case 5:
                //Toast.makeText(getActivity(), "意见反馈", Toast.LENGTH_SHORT).show();
//                jump(MineCustomerServiceActivity.class, false);
                jump(MineCustomerServiceSuggestionActivity.class, false);
                break;
            case 6:
                //Toast.makeText(getActivity(), "设置", Toast.LENGTH_SHORT).show();
                jump(SettingsActivity.class, false);

                break;
            //test
//            case 7:
//                //test，跳转我的兑换券
//                jump(MyExchangeActivity.class,false);
//                break;

//            case 8:
//                if (isLogin){
//                    //QQ第三方登录，登录成功绑定手机号
//
//
//                }else {
//                    bindPhone();
//                }
//                // Toast.makeText(getActivity(), "绑定QQ", Toast.LENGTH_SHORT).show();
//                break;
        }
    }



    /**
     *
     *
     *
     *
     * 点击事件处理
     * @param v
     *
     *
     *
     *
     *
     */
    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            //提交安全码，判断是否已经有安全码分别调用两个接口
            //有：验证
            case R.id.tv_safe_code_submit:
                //处理安全码
                submitSafeCode();
                break;
            //跳转到忘记密码处理, popWin
            case R.id.tv_safe_code_forget:
                dialogDismiss(mSafeCodeDialog);
                createFindSafeCodeDialog();
                break;
            //关闭安全码验证的窗口
            case R.id.iv_safe_code_close:
                dialogDismiss(mSafeCodeDialog);
                break;

            //忘记密码页面，验证邮箱提交
            case R.id.tv_safe_code_forget_submit:
                emailValidate();
                break;

            //忘记你密码页面联系客服
            case R.id.tv_safe_code_forget_callcentre:
                //拨打客服电话
                DialUtils.callCentre(getContext(), DialUtils.CENTER_NUM);
                //Test
                dialogDismiss(mFindSafeCodeDialog);
                break;
            //关闭邮箱验证的窗口
            case R.id.iv_safe_code_forget_close:
                dialogDismiss(mFindSafeCodeDialog);
                break;
            //关闭邮箱验证成功的窗口
            case R.id.iv_email_send_succeed:
                dialogDismiss(mEmailSendSucceedDialog);
                break;


            ///////////////////////////个人中心重复点击// ////////////////////////////////////////////////////////

            case R.id.bt_mine_login: // 登录
                jump(LoginActivity.class, false);

                break;
            case R.id.rl_mine_income: // 我的收益

                Toast.makeText(getActivity(), "我的收益", Toast.LENGTH_SHORT).show();
//                mIntentSafeCode = new Intent(getActivity(), MineMemberCenterIntegralPayActivity.class);
//                mIntentSafeCode.putExtra("mIntegral", mIntegral);
//                mIntentSafeCode.putExtra("isLogin", isLogin);
//
//                if (isLogin) {
//                    //进入不验证安全码
////                    createSafeCodeDialog();
//                } else {
////                    startActivity(mIntentSafeCode);
//                }
//
//                startActivity(mIntentSafeCode);
                break;
            case R.id.rl_mine_commission: // 我的佣金
                Toast.makeText(getActivity(), "我的佣金", Toast.LENGTH_SHORT).show();
//                mIntentSafeCode = new Intent(getActivity(), MineMemberCenterActivity.class);
//
//
//                if (isLogin) {
//                    //进入的时候不验证安全码
//                    createSafeCodeDialog();
//                } else {
//                    startActivity(mIntentSafeCode);
//                }
//                //不用验证安全码直接跳转
////               startActivity(mIntentSafeCode);
//
                break;

            case R.id.rl_mine_zhonghui: //我的众汇券
                ToastUtils.showShort("我的众汇券");
                //没登录去登录页
//                if (!isLogin){
//                    mIntentSafeCode=new Intent(getActivity(),LoginActivity.class);
//                    startActivity(mIntentSafeCode);
//                    return;
//                }
//
//                //已经登录做登录处理
//                mIntentSafeCode = new Intent(getActivity(), MyExchangeActivity.class);
//
//                mClassJump = MyExchangeActivity.class;
//                /**
//                 * 根据mOpenId判断是否绑定微信
//                 * 没绑定弹框提示绑定
//                 * 绑定直接跳转
//                 */
//                //已经绑定的
//                //test
////                mOpenId=null;
////                isLogin=false;
//                //没有安全码设置安全码
//                if (TextUtils.isEmpty(mSafeCode)) {
//                    createSafeCodeDialog();
//                } else {
//                //绑定支付宝
//                    getIfBindingAlipay();
//                }
////                getIfBindingAlipay();

                break;

            case R.id.rl_mine_head_login: //带值跳转到安全码页面，然后再修改信息页面
                updateMineUserMessage();
                break;
            case R.id.iv_mine_head: //带值跳转到安全码页面，然后再修改信息页面
                updateMineUserMessage();
                break;

            case R.id.ll_mine_supply_the_phone: //拨打供货电话
//                ToastUtils.showShort("拨打供货电话");
//                callCustomerServerPhone();
                DialUtils.callCentre(getContext(), DialUtils.SUPPLY_PHONE);
                break;
            case R.id.ll_mine_service_the_phone: //拨打供货电话
//                ToastUtils.showShort("拨打供货电话");
//                callCustomerServerPhone();
                DialUtils.callCentre(getContext(), DialUtils.CENTER_NUM);
                break;


        }

    }
}
