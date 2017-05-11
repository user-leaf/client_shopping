package com.bjaiyouyou.thismall.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
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

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.MineMemberCenterActivity;
import com.bjaiyouyou.thismall.activity.WithDrawSucceedActivity;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4ClientOther;
import com.bjaiyouyou.thismall.client.Api4Mine;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.ActivateInfoModel;
import com.bjaiyouyou.thismall.model.IncomeEtCheckModel;
import com.bjaiyouyou.thismall.utils.DialogUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.StringUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 收益页面
 * author Alice
 * created at 2017/4/20 18:29
 */
public class IncomeFragment extends BaseFragment {
    public static final String TAG = IncomeFragment.class.getSimpleName();

    private String mMoneyAvailable; // 可用兑换券额
    private long mUUNum;            // UU余额
    private String mStrOpenId;      // 微信open_id

    private View layout;

    // 收益页面
    private TextView mTvExchangeIncomeNum;  // 兑换券收益数额
    private TextView mTvExchangeCommit;     // 兑换按钮

    // 收益兑换页面
    private PopupWindow mPopWindow;
    private EditText mEtExchangeNum;    // 兑换券额输入框
    private TextView mTvNumAvailable;   // 可使用兑换券额
    private EditText mEtSafeCode;       // 安全码输入框
    private EditText mEtName;           // 姓名输入框
    private TextView mTvTip;            // 错误提示栏
    private TextView mTvExchange;       // 兑换按钮
    private TextView mTvUU;             // UU余额

    private Api4Mine mApi4Mine;
    private Api4ClientOther mClientApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_income, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mApi4Mine = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
        mClientApi = (Api4ClientOther) ClientApiHelper.getInstance().getClientApi(Api4ClientOther.class);
        initView();
        setupView();
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("getExchangeData","IncomeFragmentResume");
//        loadData4IncomeExchange();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            LogUtils.d("getExchangeData","IncomeFragment!hidden");
            loadData4IncomeExchange();
        }
    }

    private void initView() {
        mTvExchangeCommit = ((TextView) layout.findViewById(R.id.tv_income_exchange));
        mTvExchangeIncomeNum = ((TextView) layout.findViewById(R.id.tv_exchange_income_num));
    }

    private void setupView() {
        mTvExchangeCommit.setOnClickListener(this);
    }

    private void loadData4IncomeExchange() {

        mClientApi.getExchangeData(new DataCallback<ActivateInfoModel>(getContext()) {

            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response == null) {
                    return;
                }

                ActivateInfoModel userCashModel = (ActivateInfoModel) response;
                ActivateInfoModel.UserAboutCashInfoBean userAboutCashInfo = userCashModel.getUserAboutCashInfo();
                if (userAboutCashInfo != null) {
                    mMoneyAvailable = userAboutCashInfo.getPush_money();
                    mUUNum = userAboutCashInfo.getMoney_quantity();
                    mTvExchangeIncomeNum.setText(mMoneyAvailable);
                    LogUtils.d(IncomeFragment.TAG, "mMoneyAvailable: " + mMoneyAvailable + ", mUUNum: " + mUUNum);
                    if (mTvUU != null) {
                        mTvUU.setText(String.valueOf(mUUNum));
                    }
                }

            }
        });

    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            /*收益页面*/
            case R.id.tv_income_exchange:   // 兑换按钮
                showLoadingDialog();
                mClientApi.getExchangeData(new DataCallback<ActivateInfoModel>(getContext()) {
                    @Override
                    public void onFail(Call call, Exception e, int id) {
                        dismissLoadingDialog();
                        ToastUtils.showException(e);
                    }

                    @Override
                    public void onSuccess(Object response, int id) {
                        dismissLoadingDialog();
                        if (response == null) {
                            return;
                        }

                        ActivateInfoModel activateInfoModel = (ActivateInfoModel) response;
                        ActivateInfoModel.UserAboutCashInfoBean userAboutCashInfo = activateInfoModel.getUserAboutCashInfo();
                        if (userAboutCashInfo != null) {
                            int today_surplus_push_money_drawings_number = userAboutCashInfo.getToday_surplus_push_money_drawings_number();
                            if (today_surplus_push_money_drawings_number > 0) {
                                showExchangePopupWindow();
                            } else {
                                ToastUtils.showShort("每日限兑换1次，明日再来哦！");
                            }
                        }
                    }
                });
                break;

            /*收益兑换页面*/
            case R.id.tv_income_back:   // 取消
                IncomeEtCheckModel.checkNum = 0;
                mPopWindow.dismiss();
                break;

            case R.id.tv_exchange_set_all_can_use:  // 全部兑换
                String strAll = String.valueOf(mMoneyAvailable);
                mEtExchangeNum.setText(strAll);
                mEtExchangeNum.setSelection(strAll.length());
                break;

            case R.id.tv_exchange_commit:   // 提交
                exchangeCommit();
                break;

            case R.id.exchange_income_tv_uu_recharge:   // 充值UU
                startActivity(MineMemberCenterActivity.class);
                break;

            default:
                break;
        }
    }

    /**
     * 显示收益兑换页面
     */
    private void showExchangePopupWindow() {
        if (mPopWindow != null) {
            mPopWindow.dismiss();
        }

        // 设置contentView
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.item_exchange_income_dialog, null);
        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setAnimationStyle(R.style.PopWindow_Anim_Style_Up_Down);

        TextView tvCancel = (TextView) contentView.findViewById(R.id.tv_income_back);
        mTvExchange = (TextView) contentView.findViewById(R.id.tv_exchange_commit);
        TextView tvSetAllCanExchange = (TextView) contentView.findViewById(R.id.tv_exchange_set_all_can_use);

        mTvNumAvailable = (TextView) contentView.findViewById(R.id.tv_exchange_can_use);
        mEtExchangeNum = (EditText) contentView.findViewById(R.id.et_exchange_num);
        mEtSafeCode = (EditText) contentView.findViewById(R.id.et_exchange_safe_code);
        mEtName = (EditText) contentView.findViewById(R.id.et_exchange_name);
        mTvTip = (TextView) contentView.findViewById(R.id.tv_income_tip);

        mTvUU = (TextView) contentView.findViewById(R.id.exchange_income_tv_uu);
        TextView tvUURecharge = (TextView) contentView.findViewById(R.id.exchange_income_tv_uu_recharge);

        tvCancel.setOnClickListener(this);
        mTvExchange.setOnClickListener(this);
        tvSetAllCanExchange.setOnClickListener(this);
        tvUURecharge.setOnClickListener(this);

        mEtExchangeNum.addTextChangedListener(new NewWatcher());
        mEtSafeCode.addTextChangedListener(new NewWatcher());
        mEtName.addTextChangedListener(new NewWatcher());

        // 显示PopupWindow
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_my_exchange, null);
        mPopWindow.showAtLocation(rootView, Gravity.TOP, 0, 0);

        // init
        mTvNumAvailable.setText("可使用兑换券额" + (TextUtils.isEmpty(mMoneyAvailable) ? 0 : mMoneyAvailable));
        mTvUU.setText(String.valueOf(mUUNum));
//        loadData4IncomeExchange();

        // hint
        // http://www.jiangnane.com/post/16.html
//        // 新建一个可以添加属性的文本对象
//        SpannableString ssNum = new SpannableString("请输入兑换券额");
//        // 新建一个属性对象,设置文字的大小
//        AbsoluteSizeSpan assNum = new AbsoluteSizeSpan(14, true);
//        // 附加属性到文本
//        ssNum.setSpan(assNum, 0, ssNum.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mEtExchangeNum.setHint(new SpannedString(ssNum)); // 一定要进行转换,否则属性会消失

//        SpannableString ssSafeCode = new SpannableString("请输入安全码");
//        AbsoluteSizeSpan assSafeCode = new AbsoluteSizeSpan(14, true);
//        ssSafeCode.setSpan(assSafeCode, 0, ssSafeCode.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mEtSafeCode.setHint(new SpannedString(ssSafeCode)); // 一定要进行转换,否则属性会消失
//
//        SpannableString ssName = new SpannableString("请输入支付宝实名认证姓名");
//        AbsoluteSizeSpan assName = new AbsoluteSizeSpan(14, true);
//        ssName.setSpan(assName, 0, ssName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mEtName.setHint(new SpannedString(ssName)); // 一定要进行转换,否则属性会消失

    }

    /**
     * 提交（兑换收益）
     */
    private void exchangeCommit() {
        String strNum = mEtExchangeNum.getText().toString();
        String strSafecode = mEtSafeCode.getText().toString();
        String strName = mEtName.getText().toString();

        LogUtils.d(TAG, "strNum: " + strNum + ", strSafecode: " + strSafecode + ", strName: " + strName);

        if (TextUtils.isEmpty(strNum)) {
            ToastUtils.showShort("请输入换取数额");
            return;
        }

        int amount = Integer.parseInt(strNum);
//        long amount = Long.parseLong(strNum);
        LogUtils.d(TAG, "amount: " + amount);

        if (amount <= 0) {
            ToastUtils.showShort("没有可提收益");
            return;
        }

        if (TextUtils.isEmpty(strSafecode)) {
            ToastUtils.showShort("请输入安全码");
            return;
        }

        if (TextUtils.isEmpty(strName)) {
            ToastUtils.showShort("请输入姓名");
            return;
        }

        if (amount > Integer.valueOf(mMoneyAvailable)) {
            ToastUtils.showShort("超过可使用兑换券额！");
            return;
        }

        if (amount > mUUNum) {
            Dialog confirmDialog = DialogUtils.createConfirmDialog(getContext(), null, "UU余额不足！", "兑换UU", "取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(MineMemberCenterActivity.class);
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }
            );
            confirmDialog.show();
            return;
        }

        showLoadingDialog();

        mApi4Mine.withdrawIncome(TAG, amount, strName, strSafecode, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dismissLoadingDialog();

                Dialog errorDialog = DialogUtils.createMessageDialog(
                        getContext(),
                        "",
                        StringUtils.getExceptionMessage(e.getMessage()),
                        "确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                errorDialog.show();
            }

            @Override
            public void onResponse(String response, int id) {
                dismissLoadingDialog();
                resetData();
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                }
                ToastUtils.showShort("兑换成功");
                startActivity(WithDrawSucceedActivity.class);

            }
        });
    }

    private void resetData() {
        mEtExchangeNum.getText().clear();
        mEtSafeCode.getText().clear();
        mEtName.getText().clear();
        mEtExchangeNum.requestFocus();
        loadData4IncomeExchange();
    }

    private class NewWatcher implements TextWatcher {

        private boolean input_not_empty = false, count_can_plus = true;
        private boolean flag = false; // 防止重复执行

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() > 0) {
                input_not_empty = true;
            } else if (s.length() == 0) {
                input_not_empty = false;

                count_can_plus = true;  // 开门
            }

            if (input_not_empty) {
                if (count_can_plus) {
                    IncomeEtCheckModel.checkNum++;
                    count_can_plus = false; // 关门
                }

            } else {
                IncomeEtCheckModel.checkNum--;
            }

            // 设置按钮状态
            if (IncomeEtCheckModel.checkNum >= 3) {
                mTvExchange.setEnabled(true);
            } else {
                mTvExchange.setEnabled(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }
}
