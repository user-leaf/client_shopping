package shop.imake.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import shop.imake.Constants;
import shop.imake.MainApplication;
import shop.imake.R;
import shop.imake.activity.AboutIUUActivity;
import shop.imake.activity.AddressManagerActivity;
import shop.imake.activity.HistoryBuyNewActivity;
import shop.imake.activity.LoginActivity;
import shop.imake.activity.MineCustomerServiceSuggestionActivity;
import shop.imake.activity.MyCommissionActivity;
import shop.imake.activity.MyIncomeActivity;
import shop.imake.activity.MyOrderActivity;
import shop.imake.activity.MyZhongHuiQuanActivity;
import shop.imake.activity.SettingsActivity;
import shop.imake.activity.UpdateMineUserMessageActivity;
import shop.imake.activity.WebShowActivity;
import shop.imake.adapter.MineAdapter;
import shop.imake.adapter.MineOtherAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4Mine;
import shop.imake.client.ClientAPI;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.MyMine;
import shop.imake.model.MyMineOther;
import shop.imake.model.User;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DialUtils;
import shop.imake.utils.DialogUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.NTalkerUtils;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.SPUtils;
import shop.imake.utils.ScreenUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.utils.ValidatorsUtils;
import shop.imake.widget.NoScrollGridView;

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

    //    功能列表
    private NoScrollGridView gv;

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
    //用户头像名称
    private String mImgName;
    //用户头像路径
    private String mUserImgUrl;
    //判断头像是否存在
    private boolean isHaveImg;
    //可提金额
    private TextView mTvWithdrawNum;

    //网络请求对象
    private Api4Mine mClientApi;
    //拨打客服电话
    private LinearLayout mLLServicePhone;
    //其他服务布局
    private NoScrollGridView mGvOther;
    //网络加载
    private Api4Mine mClient;
    //其他服务部分的适配器
    private MineOtherAdapter mMyMineOtherAdapter;
    //其他服务数据
    private List<MyMineOther.ThreeServicesBean> myMineList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_mine, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initVariate();
        initView();
        setupView();
        initCtl();
        initData();
        initOtherData();


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
//            initGridViewChange();
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



    /**
     * 加载其他服务数据
     */

    private void initOtherData() {
        if (!NetStateUtils.isNetworkAvailable(getContext())){
            ToastUtils.showShort("当前网络不可用，请检查网络设置");
            return;
        }

        //获取其他服务图标信息
        mClient.getMyMineOther(getActivity(), new DataCallback<MyMineOther>(getActivity()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                ToastUtils.showException(e);
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response != null) {
                    MyMineOther myMineOther = (MyMineOther) response;
                    myMineList = myMineOther.getThree_services();
                    mMyMineOtherAdapter.clear();
                    mMyMineOtherAdapter.addAll(myMineList);
                }
            }
        });
    }


    private void initVariate() {
        mClient = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
    }



    private void initView() {
        mHeight = ScreenUtils.getScreenHeight(getContext());
        //登录后的头部布局
        mLoginView = layout.findViewById(R.id.rl_mine_head_login);
        //没登录的头部布局
        mNotLoginView = layout.findViewById(R.id.ll_mine_head_notlogin);

        //断网提示
        mLLUnNetWork = ((LinearLayout) layout.findViewById(R.id.ll_mine_un_network));
        //功能列表
        gv = ((NoScrollGridView) layout.findViewById(R.id.gv_mine));

        mGvOther = ((NoScrollGridView) layout.findViewById(R.id.gv_mine_other));


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
        mLoginView.setOnClickListener(this);
        mBtLogin.setOnClickListener(this);
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mScrollView.isRefreshing()) {
                    mScrollView.onRefreshComplete();
                    initData();
                    initOtherData();
                }
            }
        });

        mLLSupplyPhone.setOnClickListener(this);
        mLLServicePhone.setOnClickListener(this);

        //其他服务点击事件
        mGvOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!CurrentUserManager.isLoginUser()) {
                    jump(LoginActivity.class, false);
                    return;
                }
                goToHtml(position);
            }
        });
    }

    /**
     * 跳转到其他相应的Ｈ５页面
     *
     * @param position
     */
    private void goToHtml(int position) {
        if (myMineList != null && position < myMineList.size()) {
            MyMineOther.ThreeServicesBean threeServicesBean = myMineList.get(position);
            if (threeServicesBean != null) {
                int type = threeServicesBean.getIs_open();
                if (type == 1) {
                    StringBuilder stringBuilder = new StringBuilder(threeServicesBean.getRequest_url());
                    stringBuilder.append("?token=").append(CurrentUserManager.getUserToken())
                            .append("&type=android").append("&vt=").append(System.currentTimeMillis());
                    String htmlUrl = stringBuilder.toString();

                    WebShowActivity.actionStart(getContext(), htmlUrl, WebShowActivity.PARAM_PAGE_HIDE);
                } else if (type == 0) {
                    Dialog dialog = DialogUtils.createMessageDialog(
                            getActivity(), null, "暂未开通相关服务，敬请期待～", "确定",
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
    }

    /**
     * 网络请求数据
     * 先请求退款接口
     * 再获取个人信息
     */

    private void initData() {
        if (!NetStateUtils.isNetworkAvailable(getContext())){
            ToastUtils.showShort("当前网络不可用，请检查网络设置");
            return;
        }
        //取消数据加载Loading
//        showLoadingDialog();
        //退款接口为二次请求相应接口，先请求退款接口，（成功与否对获取用户信息不影响）再获取用户信息

        if (TextUtils.isEmpty(CurrentUserManager.getUserToken())){
            return;
        }
        ClientAPI.getWithdraw(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
                initUserData();
            }

            @Override
            public void onResponse(String response, int id) {
                initUserData();
            }
        });

    }


    private void initCtl() {
        myMineList = new ArrayList<>();
        mMyMineOtherAdapter = new MineOtherAdapter(myMineList, getContext());
        mGvOther.setAdapter(mMyMineOtherAdapter);

    }

    /**
     * 获取用户信息
     */

    private void initUserData() {

        if (!NetStateUtils.isNetworkAvailable(getContext())){
            ToastUtils.showShort("当前网络不可用，请检查网络设置");
            return;
        }
        //初始化网路请求对象

        mClientApi = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);

        //模拟我的邀请好友假数据
        String token = CurrentUserManager.getUserToken();



        if (token != null) {
            mClientApi.getUserMessage(TAG, new DataCallback<User>(getContext()) {
                @Override
                public void onFail(Call call, Exception e, int id) {
                    CurrentUserManager.TokenDue(e);
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
//                    initGridViewChange();
                }

                @Override
                public void onSuccess(Object response, int id) {
                    isLogin = true;
                    //隐藏网络请求
                    mLLUnNetWork.setVisibility(View.GONE);
                    mLoginView.setVisibility(View.VISIBLE);
                    mNotLoginView.setVisibility(View.GONE);
                    if (response != null) {
                        mUser = (User) response;
                        setData();
                    } else {
                        ToastUtils.showShort("数据加载错误");
                    }

                }
            });
        } else {
            Toast.makeText(getActivity(), "请先登录后再回来哦", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        //重新设置头像为默认头像

        User.MemberBean memberBean = mUser.getMember();
//        initGridViewChange();
        CurrentUserManager.setCurrentUser(memberBean);

//        //用户名
//        mTvName.setText(memberBean.getName());
//        //昵称
        if (memberBean != null) {
            String name = memberBean.getNick_name();
            if (!TextUtils.isEmpty(name)) {
                mTvName.setText(name);
            } else {
                mTvName.setText("");
            }

            String tel = memberBean.getPhone().trim();
            if (!TextUtils.isEmpty(tel)) {
                tel = tel.substring(0, 3) + "****" + tel.substring(tel.length() - 4, tel.length());
                mTVTel.setText(tel);
            } else {
                mTVTel.setText("");
            }


            mEmail = memberBean.getEmail().trim();
            if (!TextUtils.isEmpty(mEmail)) {
                isHaveEmail = true;
                String email;
                if (mEmail.length() >= 3) {
                    email = mEmail.substring(0, 2) + "****" + mEmail.substring(mEmail.indexOf("@"), mEmail.length());
                } else {
                    email = mEmail;
                }
                mTVEmail.setText(email);
            } else {
                isHaveEmail = false;
                mTVEmail.setText("");
            }


            if (!TextUtils.isEmpty(mSafeCode)) {
                mIsHaveSafeCode = true;
            } else {
                mIsHaveSafeCode = false;
            }
            mSafeCode = memberBean.getSecurity_code_hint();
            mIsHaveSafeCode = memberBean.isSecurity_code_state();

            mCoin = memberBean.getMoney_quantity();
            mTVGoldCoinNum.setText(mCoin + "");
            mIntegral = memberBean.getIntegration();
            mTVIntegralNum.setText(mIntegral + "");


            String inCome = memberBean.getPush_money();

            int inComeNum = 0;

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


            //根据数据判断显示图标
            LogUtils.e("getMember_type", "" + memberBean.getMember_type());
            if (memberBean.getMember_type() == 5) {
                mIvMemberFive.setVisibility(View.GONE);
            } else {
                mIvMemberFive.setVisibility(View.GONE);
            }

            LogUtils.e("getIs_vip", "" + memberBean.getIs_vip());
            if (memberBean.getIs_vip() == 2) {
                mIvMember.setVisibility(View.GONE);
            } else {
                mIvMember.setVisibility(View.GONE);
            }

            int memberLevel=memberBean.getMember_level();
            if (memberLevel==1||memberLevel==2||memberLevel==3){
                mIvMember.setVisibility(View.VISIBLE);
            }else {
                mIvMember.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 布局功能控价列表
     */
    private void initGridView() {
        initLoginGv();
//        initNotLoginGv();
    }

    /**
     * 改变功能布局的显示
     */
    private void initGridViewChange() {
        if (isLogin) {
            initLoginGv();

        } else {
//            initNotLoginGv();
            initLoginGv();
        }
    }

    /**
     * 登录功能布局的初始化
     */
    private void initLoginGv() {
        //已经登录的布局初始化
        dataListLogin = new ArrayList<>();

        MyMine mine1 = new MyMine("我的订单", R.mipmap.list_order_nor);
        dataListLogin.add(mine1);
        MyMine mine2 = new MyMine("历史购物", R.mipmap.list_historyrecorde_nor);
        dataListLogin.add(mine2);
        MyMine mine3 = new MyMine("收货地址", R.mipmap.list_addressinformation_nor);
        dataListLogin.add(mine3);
        MyMine mine4 = new MyMine("我的众汇券", R.mipmap.personal_centeriocn_icon_my_assets);
        dataListLogin.add(mine4);
        MyMine mine5 = new MyMine("意见反馈", R.mipmap.list_icon_customerservice_nor);
        dataListLogin.add(mine5);
        MyMine mine6 = new MyMine("关于我们", R.mipmap.list_aiyouyou_nor);
        dataListLogin.add(mine6);

        MyMine mine7 = new MyMine("设置", R.mipmap.list_set_nor);
        dataListLogin.add(mine7);
        MyMine mine8 = new MyMine("在线客服", R.mipmap.list_online_service);
        dataListLogin.add(mine8);
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
        createSafeCodeDialog();
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
    private InputFilter etInputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符

            if (source.equals(" ")) return "";
            else return null;
        }
    };

    /**
     * 生成安全码对话框
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


        //判断存不存在安全码进行页面显示变化
        if (mIsHaveSafeCode) {
            //安全码验证将输入类型改为密码
            //方法一： 会引起EditText中android:digits失效问题
//            etSateCodeInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            etSateCodeInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

            etSateCodeInput.setText("");
            tvSateCodeMode.setText("输入安全码");
            tvSateCodeMode.setGravity(Gravity.CENTER);
            tvSateCodeForget.setVisibility(View.VISIBLE);
            tvSateCodeForget.setEnabled(true);
            tvSateCodeForget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            etSateCodeMakeSure.setVisibility(View.GONE);
            etSateCodeInput.setHint(mSafeCode + "********");
            mLLSetEmail.setVisibility(View.GONE);
        } else {
            if (isHaveEmail) {
                tvSateCodeMode.setText("设置安全码");
                tvSateCodeMode.setGravity(Gravity.CENTER);
                tvSateCodeForget.setVisibility(View.INVISIBLE);
                tvSateCodeForget.setEnabled(false);
                etSateCodeMakeSure.setVisibility(View.GONE);
                etSateCodeInput.setHint("8~16位，数字、字母组合");
                mLLSetEmail.setVisibility(View.GONE);
            } else {
                tvSateCodeMode.setText("设置安全码");
                tvSateCodeForget.setVisibility(View.INVISIBLE);
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
     * 验证邮箱提交
     */
    private void emailValidate() {
        String emailReset = etSateCodeReGetEmailInput.getText().toString().trim();
        LogUtils.e("emailReset----", mEmail);
        if (isHaveEmail) {
            if (mEmail.equals(emailReset)) {
                //调用邮件发送接口
                String token = CurrentUserManager.getUserToken();
                if (!TextUtils.isEmpty(token)) {
                    ClientAPI.sendEmailResetCode(token, mEmail, new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            CurrentUserManager.TokenDue(e);
                            UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                            dialogDismiss(mFindSafeCodeDialog);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            //提交成功后就跳转到邮件发送成功页面
                            //创建发送成功对话框
                            dialogDismiss(mFindSafeCodeDialog);
                            createEmailSendSucceedDialog();
                            return;
                        }
                    });
                }

            } else {
                Toast.makeText(getContext(), "输入邮箱与预设邮箱不相符", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(getContext(), "未设置预设邮箱，请直接拨打客服找回安全码", Toast.LENGTH_SHORT).show();
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
                    CurrentUserManager.TokenDue(e);
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
                if (!(!TextUtils.isEmpty(email) && email.length() > 1 && ValidatorsUtils.isEmail(email))) {
                    Toast.makeText(getContext(), "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
                    return;
                }
                ClientAPI.postSetSafeCode(token, safeCode, email, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        CurrentUserManager.TokenDue(e);
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
                        CurrentUserManager.TokenDue(e);
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
     * 创建邮箱验证的对话框
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
     * 创建邮箱验证成功的对话框
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

        mEmailSendSucceedDialog.show();

    }


    ////////////////////////////点击事件处理////////////////////////////

    /**
     * @param parent
     * @param view
     * @param position
     * @param id       处理条目点击事件
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                if (!CurrentUserManager.isLoginUser()) {
                    jump(LoginActivity.class, false);
                    return;
                }
                // 我的订单
                jump(MyOrderActivity.class, false);
                break;
            case 1://历史购买
                if (!CurrentUserManager.isLoginUser()) {
                    jump(LoginActivity.class, false);
                    return;
                }

                startActivity(new Intent(getActivity(), HistoryBuyNewActivity.class));
                break;
            case 2:
                if (!CurrentUserManager.isLoginUser()) {
                    jump(LoginActivity.class, false);
                    return;
                }
                //收货地址"

                jump(AddressManagerActivity.class, false);
                break;
            case 3:
                if (!CurrentUserManager.isLoginUser()) {
                    jump(LoginActivity.class, false);
                    return;
                }
                //我的众汇券，WebView
                StringBuilder stringBuilder = new StringBuilder(ClientAPI.URL_WX_H5);
                stringBuilder.append("myzhonghui.html?token=").append(CurrentUserManager.getUserToken())
                        .append("&type=android&vt=").append(System.currentTimeMillis());
                String zhonghuiquanUrl = stringBuilder.toString();
                WebShowActivity.actionStart(getContext(), zhonghuiquanUrl, WebShowActivity.PARAM_PAGE_HIDE);

                break;
            case 4:
                //意见反馈"

                jump(MineCustomerServiceSuggestionActivity.class, false);
                break;
            case 5:
                // 关于我们"
                jump(AboutIUUActivity.class, false);
                break;

            case 6:
                //设置
                jump(SettingsActivity.class, false);

                break;

            case 7:
                // 在线客服
                NTalkerUtils.getInstance().startChat(getActivity());
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
     * 点击事件处理
     *
     * @param v
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
                //存在邮箱，邮箱验证弹框
                if (isHaveEmail) {
                    createFindSafeCodeDialog();
                    //没有邮箱直接拨打电话
                } else {
                    //拨打客服电话
                    DialUtils.callSafeCodeForget(getContext());
                }
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
                DialUtils.callSafeCodeForget(getContext());

//                new AlertView(DialUtils.SERVER_TITLE, null, "取消", null, new String[]{getString(R.string.service_num1),getString(R.string.service_num2)
//                }, getActivity(), AlertView.Style.ActionSheet, this).show();

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
                mIntentSafeCode = new Intent(getActivity(), MyIncomeActivity.class);
                checKUserState();

                break;
            case R.id.rl_mine_commission: // 我的佣金
                mIntentSafeCode = new Intent(getActivity(), MyCommissionActivity.class);
                checKUserState();
                break;

            case R.id.rl_mine_zhonghui: //我的众汇券
                mIntentSafeCode = new Intent(getActivity(), MyZhongHuiQuanActivity.class);
                checKUserState();

                break;

            case R.id.rl_mine_head_login: //带值跳转到安全码页面，然后再修改信息页面
                updateMineUserMessage();
                break;
            case R.id.iv_mine_head: //带值跳转到安全码页面，然后再修改信息页面
                updateMineUserMessage();
                break;

            case R.id.ll_mine_supply_the_phone: //拨打供货电话
//                DialUtils.callCentre(getContext(), DialUtils.SUPPLY_PHONE);
                break;
            case R.id.ll_mine_service_the_phone: //拨打客服电话
//                DialUtils.callCentre(getContext(), DialUtils.CENTER_NUM);
                break;
        }
    }

    /**
     * 检查网络状态，用户是否登录
     */
    private void checKUserState() {
        /**
         * 网络检查
         */

        if (!NetStateUtils.isNetworkAvailable(getContext())) {
            ToastUtils.showShort("当前网络不可用，请检查网络设置");
        } else {
            if (!CurrentUserManager.isLoginUser()) {
                startActivity(LoginActivity.class);
            } else {
                startActivity(mIntentSafeCode);
            }
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        //调用父类的方法给出提示
        super.onItemClick(o,position);
        switch (position) {
            case 0: //
//                DialUtils.callCentre(getActivity(), DialUtils.CENTER_NUM1);

                break;
            case 1: //
//                DialUtils.callCentre(getActivity(), DialUtils.CENTER_NUM2);

                break;
            default:
                return;
        }

    }
}
