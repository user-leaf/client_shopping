package shop.imake.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import shop.imake.MainActivity;
import shop.imake.R;
import shop.imake.activity.MineMemberCenterActivity;
import shop.imake.activity.MyExchangeActivity;
import shop.imake.activity.RechargeActivity;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4ClientOther;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.ActivateInfoModel;
import shop.imake.model.ActivateResultMode;
import shop.imake.model.ExchangeResultModel;
import shop.imake.model.ResponseModel;
import shop.imake.model.UserBalance;
import shop.imake.model.Withdraw;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DialogUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.StringUtils;
import shop.imake.utils.ToastUtils;
import com.google.gson.Gson;

import okhttp3.Call;

/**
 * 我的兑换券页面
 * author Alice
 * created at 2017/4/20 18:25
 */
public class ExchangeCertificateFragment extends BaseFragment {
    private Api4ClientOther mClient;
    public static final String TAG = ExchangeCertificateFragment.class.getSimpleName();
    //布局
    private View layout;
    //激活按钮
    private TextView mTvActivate;
    //兑换按钮
    private TextView mTvExchange;
    //充值按钮
    private TextView mTvRecharge;

    //激活提交按钮
    private TextView tvActivateCommit;
    //激活按钮可以点击
    private boolean isActivateCommitEnable;
    //激活券额填写
    private boolean isActivateNumOk;

    //兑换提交按钮
    private TextView tvExchangeCommit;
    //兑换按钮可点击
    private boolean isExchangeCommitEnable;
    //兑换券额填写
    private boolean isExchangeNumOk;
    //兑换安全码填写
    private boolean isExchangeSafeCodeOk;
    //兑换姓名填写
    private boolean isExchangeNameOk;

    //未激活券额
    private TextView mTvNotActiveNum;
    //可以使用的兑换券额
    private TextView mTvCanUseExchangeNum;
    //激活弹出框
    private PopupWindow mPopWindow;
    //兑换额度
    private EditText etExchangeNum;
    //安全码
    private EditText etSafeCode;
    //姓名
    private EditText etName;
    //激活劵额数据类
    private Withdraw mGetWithdraw;

    //本周可激活上限
    private double mLimitActivateNum;


    //用户可使用劵额数据类
    private UserBalance mUserBalance;

    //////////////////////用户信息/////////////////////////

    //用户数据对象
    private ActivateInfoModel mDataModel;

    private ActivateInfoModel.UserAboutCashInfoBean mUserAboutCashInfo;
    //安全码
    private String mSafeCofe;
    //用用户剩余UU
    private Long mUUNum;
    //用户的积分
    private Long mIntegral;
    //用户本周已经激活的券额
    private double mWeekHaveActivateNum;
    //本周激活上限
    private double mWeekActivateLimitNum;
    //未激活劵额
    private double mNotActivateNum;
    //可使用劵额数量
    private double mUserBalanceNum;
    //今日可激活次数
    private double mTodayCanActivateTimes;
    //今日可以兑换次数
    private int mTodayCanExchangeTimes;
    //用户等级
    private int mUserLeve;
    //是否是Vip用户
    private boolean isVipUser;
    /////////////激活弹框///////////////////////////
    //激活券额输入
    private EditText etActivateNum;
    //金额可输入长度
    private int mEtLength = 15;
    //本周还可激活兑换券额
    private int mWeekCanActivateNum;
    //本周可兑换券额
    private int mUserCanBalanceNum;
    //点击了兑换全部
    private boolean isSetUserCanBalanceNum;
    //点击了剩余可激活
    private boolean isSetWeekCanActivateNum;
    //uu显示控件
    private TextView tvUUNum;
    //兑换弹框的可使用兑换券额
    private TextView tvCanUseExchangeNum;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_exchange_certificate, container, false);
        // Inflate the layout for this fragment
        initView();
        initVariate();
        setupView();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("getExchangeData","onResume");
        //获取传递数据
        getData();
    }

    /**
     *
     * 获取传递数据
     */
    private void getData() {
        mUserAboutCashInfo= (ActivateInfoModel.UserAboutCashInfoBean) getArguments().get(MyExchangeActivity.MYEXCHANGE_USER);
        setData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            LogUtils.d("getExchangeData","!hidden");
            initData();
        }
    }

    private void initVariate() {
        mClient = (Api4ClientOther) ClientApiHelper.getInstance().getClientApi(Api4ClientOther.class);
    }

    /**
     * 获得数据
     */
    private void initData() {
        showLoadingDialog();
        ///////////////////////////////获取数据//////////////////////////////
        mClient.getExchangeData(new DataCallback<ActivateInfoModel>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                LogUtils.e("getExchangeData", "失败"+e.getMessage());
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(Object response, int id) {
                dismissLoadingDialog();
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
        //uu
        mUUNum = mUserAboutCashInfo.getMoney_quantity();
        //当控件不为null时，填充UU数值
        if (tvUUNum != null) {
            tvUUNum.setText(mUUNum + "");
        }

        //积分
        mIntegral = mUserAboutCashInfo.getUser_integration();
        //本周已经激活券额
        String weekHaveActivateNum = mUserAboutCashInfo.getWeek_activate_number();
        if (!TextUtils.isEmpty(weekHaveActivateNum)) {
            mWeekHaveActivateNum = Double.valueOf(weekHaveActivateNum);
        }

        //周激激活上限
        String weekActvateLimitNum = mUserAboutCashInfo.getWeek_activate_limit();

        if (!TextUtils.isEmpty(weekActvateLimitNum)) {
            mWeekActivateLimitNum = Double.valueOf(weekActvateLimitNum);
        }

        //未激活券额
        String canDrawAmount = mUserAboutCashInfo.getCan_drawings_amount();
        if (!TextUtils.isEmpty(canDrawAmount)) {
            mNotActivateNum = Double.valueOf(canDrawAmount);
        }
        //可激活券额为0时不能进入激活页面
        if (mNotActivateNum<=0){
            mTvActivate.setEnabled(false);
        }else {
            mTvActivate.setEnabled(true);
        }

        //设置未激活兑换券
        mTvNotActiveNum.setText(mNotActivateNum + "");
        //可使用兑换券
        mUserBalanceNum = mUserAboutCashInfo.getUser_withdrawable_balance();
        //设置可使用兑换券
        mTvCanUseExchangeNum.setText(mUserBalanceNum + "");
        //今日可激活次数
        mTodayCanActivateTimes = mUserAboutCashInfo.getToday_surplus_activate_number();

        //今天可兑换次数
        mTodayCanExchangeTimes = mUserAboutCashInfo.getToday_surplus_drawings_number();


        //test
//        mTodayCanActivateTimes = 1;
//        mTodayCanExchangeTimes = 1;


        //用户等级
        mUserLeve = mUserAboutCashInfo.getUser_lever();
        if (mUserLeve == 2 || mUserLeve == 5) {
            isVipUser = true;
        } else {
            isVipUser = false;
        }


        //本周还可激活数额=周激活上限-本周已激活数额
        double canActivateNum = mWeekActivateLimitNum - mWeekHaveActivateNum;
        //设置为本周还可激活数额和总共未激活数额的最小值
        mWeekCanActivateNum = Math.min((int) mNotActivateNum, (int) canActivateNum);

        //兑换页面显示的可使用兑换券额
        if (tvCanUseExchangeNum != null) {
            tvCanUseExchangeNum.setText("可使用兑换券额" + mUserBalanceNum);
        }
    }


    private void initView() {
        mTvActivate = ((TextView) layout.findViewById(R.id.tv_exchange_certificate_activate));
        mTvExchange = ((TextView) layout.findViewById(R.id.tv_exchange_certificate_exchange));
        mTvRecharge = ((TextView) layout.findViewById(R.id.tv_exchange_certificate_recharge));
        mTvNotActiveNum = ((TextView) layout.findViewById(R.id.tv_not_active_num));
        mTvCanUseExchangeNum = ((TextView) layout.findViewById(R.id.tv_can_use_exchange_num));

    }

    private void setupView() {
        mTvActivate.setOnClickListener(this);
        mTvExchange.setOnClickListener(this);
        mTvRecharge.setOnClickListener(this);
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
            //处理激活券额
            case R.id.tv_exchange_certificate_activate:
                initActivateExchange();

//                startActivity(new Intent(getContext(),ActivateExchangeCertificateActivity.class));
//                //添加启动动画
//                getActivity().overridePendingTransition(R.anim.anim_activate_exchange_certificate_open,R.anim.anim_activate_exchange_certificate_start);
                break;
            //跳转到兑换页面
            case R.id.tv_exchange_certificate_exchange:
                initExchange();

//                startActivity(new Intent(getContext(),ExchangeActivity.class));
//                //添加启动动画
//                getActivity().overridePendingTransition(R.anim.anim_activate_exchange_certificate_open,R.anim.anim_activate_exchange_certificate_start);
                break;
            //跳转到充值页面
            case R.id.tv_exchange_certificate_recharge:
                startActivity(new Intent(getContext(), RechargeActivity.class));
                break;


            /////////////////////////////激活弹出框///////////////////////////////////
            //激活弹出框消失
            case R.id.tv_activate_exchange_certificate_back:
                mPopWindow.dismiss();
                break;
            case R.id.tv_activate_commit:
                //提交激活
                activateCommit();
                break;
            case R.id.tv_activate_uu_recharge:
                //充值uu
                Intent uuIntent = new Intent(getContext(), MineMemberCenterActivity.class);
                uuIntent.putExtra("coin", mUUNum);
                uuIntent.putExtra("integral", mIntegral);
                startActivity(uuIntent);
                break;
            case R.id.tv_activate_set_all_can:
                setActivateAllCan();

                break;

            /////////////////////////////兑换弹出框///////////////////////////////////
            //兑换弹出框消失
            case R.id.tv_exchange_back:
                mPopWindow.dismiss();
                break;
            //提交兑换
            case R.id.tv_exchange_commit:
                exchangeCommit();
                break;
            //填充全部的兑换额
            case R.id.tv_exchange_set_all_can_use:
                //将可以使用的兑换券全部填充大ET
                //填充全部兑换
                setExchangeAllCan();
                break;

            default:
                return;
        }
    }

    /**
     * 将全部可兑换金额写入兑换输入框
     */
    private void setExchangeAllCan() {
        isSetUserCanBalanceNum = true;
        mUserCanBalanceNum = (int) mUserBalanceNum;
        etExchangeNum.setText("" + mUserCanBalanceNum);
    }

    /**
     * 将全部可以提现的券额写入输入框
     */

    private void setActivateAllCan() {
        isSetWeekCanActivateNum = true;
        //填充激活剩余
//                int notActivateNum = (int) mNotActivateNum;

        if (mWeekCanActivateNum==0){
            etActivateNum.setText("");
        }else {
            etActivateNum.setText("" + mWeekCanActivateNum);
        }


    }


    /**
     * 显示激活弹出页面
     */
    private void showActivatePopupWindow() {
        if (mPopWindow != null) {
            mPopWindow.dismiss();
        }
        //设置contentView
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.item_activate_exchange_certificate_dialog, null);
        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        //添加显示动画
        mPopWindow.setAnimationStyle(R.style.PopWindow_Anim_Style_Up_Down);

        //设置各个控件的点击响应
        //关闭按钮
        TextView tvCancle = (TextView) contentView.findViewById(R.id.tv_activate_exchange_certificate_back);
        //激活按钮
        tvActivateCommit = (TextView) contentView.findViewById(R.id.tv_activate_commit);
        //uu充值
        TextView tvUURecharge = (TextView) contentView.findViewById(R.id.tv_activate_uu_recharge);
        //激活剩余部分
        TextView tvActivateOther = (TextView) contentView.findViewById(R.id.tv_activate_set_all_can);
        //激活卷额，已经激活、未激活
        TextView tvActivateExchangeNum = (TextView) contentView.findViewById(R.id.tv_not_activate_num);
        //周激活上限
        TextView tvActivateLimit = (TextView) contentView.findViewById(R.id.tv_activate_limit_num);
        //周激活上限
        tvUUNum = (TextView) contentView.findViewById(R.id.tv_activate_uu);
        //激活券额输入
        etActivateNum = (EditText) contentView.findViewById(R.id.et_recharge_num);


        //添加点击事件
        tvActivateCommit.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        tvUURecharge.setOnClickListener(this);
        tvActivateOther.setOnClickListener(this);
        etActivateNum.addTextChangedListener(etActivateNumTextWatcher);

        //填充必要数据
        tvActivateExchangeNum.setText("未激活劵额：" + mNotActivateNum + "，本周已激活的金额" + mWeekHaveActivateNum);
        tvActivateLimit.setText("周激活上限为" + mWeekActivateLimitNum);
        tvUUNum.setText(mUUNum + "");
        //显示PopupWindow
        View rootview = LayoutInflater.from(getActivity()).inflate(R.layout.activity_my_exchange, null);
        mPopWindow.showAtLocation(rootview, Gravity.TOP, 0, 0);
    }


    /**
     * 显示兑换弹出页面
     */
    private void showExchangePopupWindow() {
        if (mPopWindow != null) {
            mPopWindow.dismiss();
        }
        //设置contentView
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.item_exchange_exchange_certificate_dialog, null);
        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setAnimationStyle(R.style.PopWindow_Anim_Style_Up_Down);
        //设置各个控件的点击响应
        TextView tvCancle = (TextView) contentView.findViewById(R.id.tv_exchange_back);
        tvExchangeCommit = (TextView) contentView.findViewById(R.id.tv_exchange_commit);
        tvCanUseExchangeNum = (TextView) contentView.findViewById(R.id.tv_exchange_can_use);
        TextView tvSetAllCanExchange = (TextView) contentView.findViewById(R.id.tv_exchange_set_all_can_use);
        etExchangeNum = (EditText) contentView.findViewById(R.id.et_exchange_num);
        etSafeCode = (EditText) contentView.findViewById(R.id.et_exchange_safe_code);
        etName = (EditText) contentView.findViewById(R.id.et_exchange_name);

        //设置监听
        tvCancle.setOnClickListener(this);
        tvExchangeCommit.setOnClickListener(this);
        tvSetAllCanExchange.setOnClickListener(this);
        etExchangeNum.addTextChangedListener(etExchangeNumTextWatcher);
        etSafeCode.addTextChangedListener(etExchangeSafeCodeTextWatcher);
        etName.addTextChangedListener(etExchangeNameTextWatcher);
        //填充数据
        tvCanUseExchangeNum.setText("可使用兑换券额" + mUserBalanceNum);


        //显示PopupWindow
        View rootview = LayoutInflater.from(getActivity()).inflate(R.layout.activity_my_exchange, null);
        mPopWindow.showAtLocation(rootview, Gravity.TOP, 0, 0);
    }

    /////////////////////////////////兑换使用方法/////////////////////////////////////////
    private void initExchange() {

//        mExchangeTimes=0;
        //判断兑换次数
        //不可进行兑换，提示
        if (mTodayCanExchangeTimes < 1) {
            Dialog dialog = DialogUtils.createConfirmDialog(getContext(), null,
                    "每日限兑换1次，明日再来哦！", "知道了", "",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
            //点击其他地方消失取消
            dialog.setCancelable(false);
            dialog.show();

        } else {
            //test
//            showExchangePopupWindow();

            //判断可兑换额度是否过了最低限制
            if (mUserBalanceNum < 100) {
                Dialog dialog = DialogUtils.createConfirmDialog(getContext(), null,
                        "可使用券额大于100才可兑换哦", "知道了", "",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                //点击其他地方消失取消
                dialog.setCancelable(false);
                dialog.show();

            } else {
                showExchangePopupWindow();
            }

        }
    }

    //兑换金额
    private Double mInPutExchangeNum;
    //是否输入兑换金额
    private boolean isEtExchangeNumChange;
    //输入金额可以进行兑换
    private boolean isCanExChange;
    /**
     * 兑换券额
     */
    TextWatcher etExchangeNumTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //获得输入金额
            String string = s.toString().trim();

            if (isSetUserCanBalanceNum) {
                isSetUserCanBalanceNum = false;
            } else {
                //限制开头输入不能是“0”
                while (string.startsWith("0")) {
                    string = string.substring(1, string.length());
                    etExchangeNum.setText(string);
                }
            }

            //控制光标的位置
            etExchangeNum.setSelection(string.length());

            if (string.length() > mEtLength) {
                //不可编辑当长度超长的时候不能删除
//                    mEtWithDraw.setEnabled(false);
//                    mEtWithDraw.setFocusable(false);
                //超长不显示，还可编辑
                string = string.substring(0, mEtLength);
                etExchangeNum.setSelection(string.length());
                etExchangeNum.setText(string);
            }
            if (!TextUtils.isEmpty(string)) {
                mInPutExchangeNum = Double.valueOf(string);
                if (mInPutExchangeNum != 0L) {
                    //根据输入判断是否可提现
                    etExchangeNumCheck(mInPutExchangeNum);
                } else {
                    isEtExchangeNumChange = false;
                }

                isExchangeNumOk = true;
            } else {
                isExchangeNumOk = false;
            }
            checkExchangeBtState();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    /**
     * 兑换安全码弹出框
     */
    TextWatcher etExchangeSafeCodeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //获得输入金额
            String string = s.toString().trim();
            if (!TextUtils.isEmpty(string)) {
                isExchangeSafeCodeOk = true;
            } else {
                isExchangeSafeCodeOk = false;
            }
            checkExchangeBtState();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    /**
     * 兑换姓名弹出框
     */
    TextWatcher etExchangeNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //获得输入金额
            String string = s.toString().trim();
            if (!TextUtils.isEmpty(string)) {
                isExchangeNameOk = true;

            } else {
                isExchangeNameOk = false;

            }
            checkExchangeBtState();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 检查兑换提交按钮状态
     */
    private void checkExchangeBtState() {
        //按钮是否可编辑
        isExchangeCommitEnable = isExchangeNameOk && isExchangeNumOk && isExchangeSafeCodeOk;
        //设置按钮状态
        tvExchangeCommit.setEnabled(isExchangeCommitEnable);

        //修改颜色
        //可以操作
        //tv.setTextColor(0xffff00ff);
        //0xffff00ff是int类型的数据，
        // 分组一下0x|ff|ff00ff，
        // 0x是代表颜色整数的标记，
        // ff是表示透明度，00全透明，ff为不透明
        // ，ff00ff表示颜色，
        // 注意：这里ffff00ff必须是8个的颜色表示，不接受ff00ff这种6个的颜色表示。
        if (isExchangeCommitEnable) {
            tvExchangeCommit.setTextColor(Color.WHITE);
        } else {
            tvExchangeCommit.setTextColor(getResources().getColor(R.color.app_btn_text_light_red));
        }

    }

    /**
     * 输入兑换劵额满足需求
     *
     * @param mInPutExchangeNum
     */

    private void etExchangeNumCheck(Double mInPutExchangeNum) {
        //兑换额是输入值
        isEtExchangeNumChange = true;
        //判断能否兑换
        isCanExChange = ((mInPutExchangeNum <= mUserBalanceNum) && (mInPutExchangeNum > 100)) && mInPutExchangeNum != 0L ? true : false;

    }


    /**
     * 兑换提交方法
     */
    private void exchangeCommit() {
        //将Long转化为double在转化成int，不可直接转化
        double exchangeMoney = mInPutExchangeNum;
        String withdrawNum = etExchangeNum.getText().toString().trim();
        if (TextUtils.isEmpty(withdrawNum)) {
            ToastUtils.showShort("请输入兑换券额");
            return;
        }
        String safeCode = etSafeCode.getText().toString().trim();
        if (TextUtils.isEmpty(safeCode)) {
            ToastUtils.showShort("安全码不可为空");
            return;
        }
        String userName = etName.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.showShort("实名认证不可为空");
            return;
        }

        String token = CurrentUserManager.getUserToken();
        if (!TextUtils.isEmpty(token)) {
            //数据加载Loading
            showLoadingDialog();
//            ClientAPI.withdraw(token, exchangeMoney, userName, safeCode, new StringCallback() {
//                /**
//                 * @param call
//                 * @param e
//                 * @param id
//                 */
//                @Override
//                public void onError(Call call, Exception e, int id) {
////                    UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
//                    //取消数据加载Loading
//                    dismissLoadingDialog();
//
////                    ToastUtils.showException(e);
//                    if (e != null) {
//                        String exceptionString = StringUtils.getExceptionMessage(e.getMessage());
//                        LogUtils.e("getExceptionMessage", exceptionString);
//                        LogUtils.e("getExceptionMessage", e.toString());
//
//                        //输入金额大于可兑换金额
//
//                        if (mInPutExchangeNum > mUserCanBalanceNum) {
//                            ToastUtils.showShort("不得超出可使用兑换券额");
////                            Dialog dialog = DialogUtils.createConfirmDialog(getActivity(), null, "兑换券额不足，是否充值", "去充值", "取消",
////                                    new DialogInterface.OnClickListener() {
////                                        @Override
////                                        public void onClick(DialogInterface dialog, int which) {
////                                            //跳转到充值兑换券页面
////                                            startActivity(new Intent(getContext(), RechargeActivity.class));
////                                            dialog.dismiss();
////                                        }
////                                    },
////                                    new DialogInterface.OnClickListener() {
////                                        @Override
////                                        public void onClick(DialogInterface dialog, int which) {
////                                            dialog.dismiss();
////                                        }
////                                    }
////                            );
////                            dialog.show();
//
//                            return;
//                        }
//
//                        Dialog dialog = DialogUtils.createConfirmDialog(getActivity(), null, exceptionString, "确定", "",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                },
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }
//                        );
//                        dialog.show();
//
//                    }
//
//                }
//
//                @Override
//                public void onResponse(String response, int id) {
//                    //取消数据加载Loading
//                    dismissLoadingDialog();
//                    if (!TextUtils.isEmpty(response.trim())) {
//                        //兑换成功
//                        ToastUtils.showShort("兑换成功");
//                        mPopWindow.dismiss();
//                        //重新加载数据
//                        initData();
//                    }
//
//                }
//            });

            mClient.postExchangeData(token, exchangeMoney, userName, safeCode,new DataCallback<ExchangeResultModel>(getContext()){
                @Override
                public void onFail(Call call, Exception e, int id) {
//                    ToastUtils.showShort("失败Exception");
                    dismissLoadingDialog();

                }

                @Override
                public void onSuccess(Object response, int id) {
//                    ToastUtils.showShort("兑换成功");
                    //取消数据加载Loading
                    dismissLoadingDialog();
                    if (response != null) {
                        //兑换成功
                        ToastUtils.showShort("兑换成功");
                        mPopWindow.dismiss();
                        //重新加载数据
                        LogUtils.d("getExchangeData","reGet");
                        initData();
                    }

                }


                @Override
                public void onFail(Call call, String responseBody, int id) {
                    super.onFail(call, responseBody, id);
//                    ToastUtils.showShort("responseBody");
                    //                    UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
                    //取消数据加载Loading
                    dismissLoadingDialog();

//                    ToastUtils.showException(e);
                    if (responseBody != null) {
                        //输入金额大于可兑换金额
                        ResponseModel model = new Gson().fromJson(responseBody, ResponseModel.class);
                        if (model != null) {
                            int code=model.getCode();
                            String messageString="";
                            if (code == 100009) {
//                                ToastUtils.showShort("不得超出可使用兑换券额");
                                messageString="不得超出可使用兑换券额";
                            }else if (code==100006){
//                                ToastUtils.showShort("可使用券额需大于100才可兑换哦！");
                                messageString="可使用券额需大于100才可兑换哦";

                            } else if (code==110001){
//                                ToastUtils.showShort("安全码错误");
                                messageString="安全码错误";

                            }else {
                                messageString=model.getMessage();
                            }

                            Dialog dialog = DialogUtils.createConfirmDialog(getActivity(), null, messageString, "知道了", "",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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

                            return;
                        }
                    }
                }
            });
        }
    }

///////////////////////////////////////激活使用方法/////////////////////////////////////////////////


    /**
     * 处理激活券额点击
     */
    private void initActivateExchange() {
        //test
//        showActivatePopupWindow();

        //test
//        Dialog dialog = DialogUtils.createConfirmDialog(getContext(), null,
//                "超出本周激活上限，激活会员权益可提升上限，是否激活","是" , "否",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //去任务页面激活会员
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        intent.putExtra(MainActivity.ACTIVATE_EXCHANGE, MainActivity.ACTIVATE_EXCHANGE);
//                        startActivity(intent);
//                        dialog.dismiss();
//                    }
//                },
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }
//        );
//        //点击其他地方消失取消
//        dialog.setCancelable(false);
//        dialog.show();

        //可以激活次数小于一次,不可激活提示
        //判断每天激活次数
        if (mTodayCanActivateTimes < 1) {
            Dialog dialog = DialogUtils.createConfirmDialog(getContext(), null,
                    "每日限激活1次，明日再来哦！", "知道了", "",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
            //点击其他地方消失取消
            dialog.setCancelable(false);
            dialog.show();

            /////////////////////////判断本周已经激活券额，与周激活上限的大小///////////////////////////
        } else {
            //周激活上限不大于，已经激活的数量，
            // 判断会员等级，普通会员到任务页升级会员，会员用户提示不可激活
            if (mWeekHaveActivateNum >= mWeekActivateLimitNum) {
                //是VIP用户提示
                if (isVipUser) {
                    Dialog dialog = DialogUtils.createConfirmDialog(getContext(), null,
                            "超过本周激活上限，下周再来！", "知道了", "",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
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
                    //点击其他地方消失取消
                    dialog.setCancelable(false);
                    dialog.show();
                    //不是VIP去开通会员页面
                } else {
                    Dialog dialog = DialogUtils.createConfirmDialog(getContext(), null,
                            "超出本周激活上限，激活会员权益可提升上限，是否激活", "是", "否",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //去任务页面激活会员
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.putExtra(MainActivity.ACTIVATE_EXCHANGE, MainActivity.ACTIVATE_EXCHANGE);
                                    startActivity(intent);
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
                    //点击其他地方消失取消
                    dialog.setCancelable(false);
                    dialog.show();


                }

                //进入激活页面
            } else {
                //进入激活弹出框处理
                showActivatePopupWindow();
            }
        }
    }


    //输入激活的券额
    private Double mInPutActivateNum;
    TextWatcher etActivateNumTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //获得输入金额
            String string = s.toString().trim();

            if (isSetWeekCanActivateNum) {
                isSetWeekCanActivateNum = false;

            } else {
                //限制开头输入不能是“0”
                while (string.startsWith("0")) {
                    string = string.substring(1, string.length());
                    etActivateNum.setText(string);
                }
            }

            //控制光标的位置
            etActivateNum.setSelection(string.length());

            if (string.length() > mEtLength) {
                //不可编辑当长度超长的时候不能删除
//                    mEtWithDraw.setEnabled(false);
//                    mEtWithDraw.setFocusable(false);
                //超长不显示，还可编辑
                string = string.substring(0, mEtLength);
                etActivateNum.setSelection(string.length());
                etActivateNum.setText(string);
            }
            if (!TextUtils.isEmpty(string)) {
                mInPutActivateNum = Double.valueOf(string);
                //判断输入是否大于可以激活金额，超过给提醒

                if (mInPutActivateNum > mWeekCanActivateNum) {
                    isActivateNumOk = false;
                    ToastUtils.showShort("你输入的激活券额大于可激活券额");
                    setActivateAllCan();
                } else {
                    isActivateNumOk = true;
                }

            } else {
                isActivateNumOk = false;
            }
            //检查激活按钮状态
            checkActivateBtState();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 检查激活提交按钮啊状态，设置字体颜色
     */
    private void checkActivateBtState() {
        //判断按钮是否可点击
        isActivateCommitEnable = isActivateNumOk;
        //改变状态
        tvActivateCommit.setEnabled(isActivateCommitEnable);
        //修改颜色
        //可以操作
        if (isActivateCommitEnable) {

            tvActivateCommit.setTextColor(Color.WHITE);
        } else {
            tvActivateCommit.setTextColor(getResources().getColor(R.color.app_btn_text_light_red));
        }

    }


    /**
     * 提交激活
     */
    private void activateCommit() {
        if (!TextUtils.isEmpty(etActivateNum.getText())) {
            //输入的激活数额与UU数目相比较
            //test
//            startActivity(new Intent(getContext(), MineMemberCenterActivity.class));
            //uu不足引导兑换uu页面
            if (mInPutActivateNum > mUUNum) {
                Dialog dialog = DialogUtils.createConfirmDialog(getContext(), null,
                        "UU余额不足！", "兑换UU", "",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //进行UU充值
                                Intent uuIntent = new Intent(getContext(), MineMemberCenterActivity.class);
                                uuIntent.putExtra("coin", mUUNum);
                                uuIntent.putExtra("integral", mIntegral);
                                startActivity(uuIntent);
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
                //点击其他地方消失取消
                dialog.setCancelable(true);
                dialog.show();

            } else {
                showLoadingDialog();
                //吊起激活接口
                mClient.postActivateExchange(mInPutActivateNum, new DataCallback<ActivateResultMode>(getContext()) {
                    @Override
                    public void onFail(Call call, Exception e, int id) {
                        dismissLoadingDialog();
                        LogUtils.e("postActivateExchange", e.toString());
                        LogUtils.e("postActivateExchange", e.getMessage());
                        if (e != null) {
                            String exceptionString = StringUtils.getExceptionMessage(e.getMessage());

                            Dialog dialog = DialogUtils.createConfirmDialog(getActivity(), null, exceptionString, "确定", "",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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

                    @Override
                    public void onSuccess(Object response, int id) {
                        dismissLoadingDialog();
                        //成功后
                        ToastUtils.showShort("激活成功");
                        mPopWindow.dismiss();
                        //重新加载数据
                        LogUtils.d("getExchangeData","reGet");
                        initData();
                    }
                });


            }
        } else {
            ToastUtils.showShort("请输入激活券额");

        }

    }
}
