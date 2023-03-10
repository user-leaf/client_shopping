package shop.imake.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.callback.DataCallback;
import shop.imake.callback.KeyboardChangeListener;
import shop.imake.client.Api4Mine;
import shop.imake.client.ClientAPI;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.CommissionModel;
import shop.imake.model.ResponseModel;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.CashierInputFilter;
import shop.imake.utils.DialogUtils;
import shop.imake.utils.DoubleTextUtils;
import shop.imake.utils.LogUtils;
import shop.imake.widget.IUUTitleBar;

/**
 * 我的佣金
 * author Alice
 * created at 2017/5/12 14:28
 */
public class MyCommissionActivity extends BaseActivity {
    public static String TAG = MyCommissionActivity.class.getSimpleName();
    //标题
    private IUUTitleBar mTitle;
    //佣金详情入口
    private TextView mTvIntoCommissionDetail;
    //申请提取（提取页面入口）
    private TextView mTvApplyWithdraw;
    //剩余佣金数量控价
    private TextView mTvHavaCommissionNum;
    //已经提取的佣金数量控价
    private TextView mTvHaveWithdrawNum;
    //能够提取的佣金数量
    private double mCommissionCanWithdrawNum;
    ///////////////////////提取弹框相关变量/////////////////////////
    //提取弹框
    private PopupWindow mPopWindow;
    //提交提取佣金按钮
    private TextView tvCommissionWithdrawCommit;
    //可提取佣金数量控件
    private TextView tvCanUseCommissionNum;
    //提取佣金输入框
    private EditText etCommissionNum;
    //金额可输入长度
    private int mEtLength = 15;
    //是否是点击设置全部提取
    private boolean isSetCommissionNum;
    //输入的提取佣金的数量
    private Double mInPutCommissionNum;
    //输入合格佣金
    private boolean isCommissionNumOk;
    //输入佣金数值大于可提取金额显示控件
    private TextView tvCommissionOver;
    //输入金额不大于可提取金额显示控件
    private LinearLayout llCommissionInputNotOver;

    private Api4Mine mApi4Mine;
    private CommissionModel mCommissionModel;
    private CommissionModel.PushMoneyBean mPushMoneyBean;
    private ScrollView mScrlViewPop;
    private boolean isCancel = false;
    //弹框的布局
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission);
        initView();
        initVariable();
        initData();
        setupView();
    }

    private void initVariable() {
        mApi4Mine = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
    }

    private void initView() {
        //标题
        mTitle = ((IUUTitleBar) findViewById(R.id.title_commission));
        //佣金明细入口
        mTvIntoCommissionDetail = ((TextView) findViewById(R.id.tv_into_commission_detail));
        //申请提取按钮
        mTvApplyWithdraw = ((TextView) findViewById(R.id.tv_commission_apply_withdraw));
        //剩余佣金数量
        mTvHavaCommissionNum = ((TextView) findViewById(R.id.tv_commission_have_commission));
        //已经返回佣金总数
        mTvHaveWithdrawNum = ((TextView) findViewById(R.id.tv_commission_withdraw_amount));

        //设置contentView
        contentView = LayoutInflater.from(MyCommissionActivity.this).inflate(R.layout.item_commission_withdraw_dialog, null);
        mScrlViewPop = ((ScrollView) contentView.findViewById(R.id.srcl_commission_pop));


    }

    private void setupView() {
        mTitle.setLeftLayoutClickListener(this);
        mTvIntoCommissionDetail.setOnClickListener(this);
        mTvApplyWithdraw.setOnClickListener(this);

        //键盘监听，当键盘弹出的时候将页面滑动到最上边
        new KeyboardChangeListener(MyCommissionActivity.this).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    mScrlViewPop.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }
        });

    }

    private void initData() {
        showLoadingDialog();
        mApi4Mine.getCommissionData(this, new DataCallback<CommissionModel>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
                dismissLoadingDialog();
                LogUtils.d("getCommissionData", e.getMessage());
            }

            @Override
            public void onSuccess(Object response, int id) {
                dismissLoadingDialog();
                if (response != null) {
                    mCommissionModel = (CommissionModel) response;
                    setData();
                }
            }
        });
    }

    /**
     * 根据接口数据进行页面操作
     */
    private void setData() {
        mPushMoneyBean = mCommissionModel.getPush_money();
        if (mPushMoneyBean != null) {
            mTvHavaCommissionNum.setText("" + DoubleTextUtils.setDoubleUtils(Double.valueOf(mPushMoneyBean.getPush_money())));
            mTvHaveWithdrawNum.setText("" + DoubleTextUtils.setDoubleUtils(Double.valueOf(mPushMoneyBean.getAll_push_money())));

            mCommissionCanWithdrawNum = Double.valueOf(DoubleTextUtils.setDoubleUtils(Double.valueOf(mPushMoneyBean.getPush_money())));

        }
        //test
//        mCommissionCanWithdrawNum=Double.valueOf("10.00");
        LogUtils.e("mCommissionCanWithdrawNum", mCommissionCanWithdrawNum + "");
        LogUtils.e("mCommissionHaveWithdrawNum", mPushMoneyBean.getAll_push_money() + "");

        if (mCommissionCanWithdrawNum > 0) {
            //可以进入提取页面
            mTvApplyWithdraw.setEnabled(true);
        } else {
            //不可以进入提取页面
            mTvApplyWithdraw.setEnabled(false);

        }


    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.left_layout://退出我的佣金页面
                finish();
                break;
            case R.id.tv_into_commission_detail://进入佣金详情页面
                intoDetail();
                break;
            case R.id.tv_commission_apply_withdraw://申请提取（提取页面入口）
                initCommissionWithdraw();
                break;

            //////////处理提取弹框页面///////////////////////////////////////

            case R.id.tv_commission_set_all_can_use://全部提取
                setExchangeAllCan();
                break;
            case R.id.tv_commission_apply_withdraw_commit://申请提取提交
                applyWithdrawCommit();
                break;
            case R.id.tv_commission_withdraw_back://销毁弹框
                closeKeyboard(getApplicationContext(), etCommissionNum);
                mPopWindow.dismiss();
                break;

            default:
                return;

        }
    }

    /**
     * 提取申请提交
     */
    private void applyWithdrawCommit() {
//        ToastUtils.showShort("申请提取提交");
        showLoadingDialog();
        String amountString = etCommissionNum.getText().toString().trim();
        Double amount = Double.valueOf(amountString);
        mApi4Mine.getCommissiongCommit(this, amount, new DataCallback<String>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
                dismissLoadingDialog();
                LogUtils.e("getCommissiongCommit", e.getMessage());

            }

            @Override
            public void onSuccess(Object response, int id) {
                dismissLoadingDialog();
                Dialog dialog = DialogUtils.createConfirmDialog(MyCommissionActivity.this, "申请提交成功", "我们会审核后转账至您绑定的提取账号", "确定", "",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mPopWindow.dismiss();
                                initData();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mPopWindow.dismiss();
                                initData();
                            }
                        }
                );
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


            }

            @Override
            public void onFail(Call call, String responseBody, int id) {
                super.onFail(call, responseBody, id);
                //取消数据加载Loading
                dismissLoadingDialog();

                if (responseBody != null) {
                    //输入金额大于可兑换金额
                    ResponseModel model = new Gson().fromJson(responseBody, ResponseModel.class);
                    if (model != null) {
                        int code = model.getCode();
                        String messageString = "";
                        if (code == 100012) {
                            messageString = "额度不足";
                        } else if (code == 100060) {
                            messageString = "添加记录失败";

                        } else if (code == 100061) {
//                                ToastUtils.showShort("安全码错误");
                            messageString = "更新用户账户失败";

                        } else if (code == 100013) {
                            messageString = "操作频繁，稍后再试";
                        } else {
                            messageString = model.getMessage();
                        }

                        Dialog dialog = DialogUtils.createConfirmDialog(MyCommissionActivity.this, null, messageString, "知道了", "",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
//                                        mPopWindow.dismiss();
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
//                                        mPopWindow.dismiss();
                                    }
                                }
                        );
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        return;
                    }
                }

            }
        });

    }

    /**
     * 将全部可兑换金额写入兑换输入框
     */
    private void setExchangeAllCan() {
        isSetCommissionNum = true;
        etCommissionNum.setText("" + DoubleTextUtils.setDoubleUtils(mCommissionCanWithdrawNum));
    }

    /**
     * 申请页面入口
     */
    private void initCommissionWithdraw() {
        //进入提取页面
        if (mCommissionCanWithdrawNum > 0) {
            showCommissionWithdrawWindow();
        }
    }

    /**
     * 显示兑换弹出页面
     */
    private void showCommissionWithdrawWindow() {
        if (mPopWindow != null) {
            mPopWindow.dismiss();
        }


        mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setAnimationStyle(R.style.PopWindow_Anim_Style_Up_Down);
        //设置各个控件的点击响应
        //取消
        TextView tvCancle = (TextView) contentView.findViewById(R.id.tv_commission_withdraw_back);
        //全部提取
        TextView tvSetAllCanExchange = (TextView) contentView.findViewById(R.id.tv_commission_set_all_can_use);

        tvCommissionWithdrawCommit = (TextView) contentView.findViewById(R.id.tv_commission_apply_withdraw_commit);
        tvCanUseCommissionNum = (TextView) contentView.findViewById(R.id.tv_commission_withdraw_can_use);
        etCommissionNum = (EditText) contentView.findViewById(R.id.et_commission_withdraw_input_num);
        etCommissionNum.setText("");


        //默认数字键盘
//        etCommissionNum.setInputType(EditorInfo.TYPE_CLASS_PHONE);


        tvCommissionOver = (TextView) contentView.findViewById(R.id.tv_commission_input_over);
        llCommissionInputNotOver = (LinearLayout) contentView.findViewById(R.id.LL_commission_input_not_over);

        //设置监听
        tvCancle.setOnClickListener(this);
        tvSetAllCanExchange.setOnClickListener(this);

        tvCommissionWithdrawCommit.setOnClickListener(this);

        //填充数据
        tvCanUseCommissionNum.setText("" + DoubleTextUtils.setDoubleUtils(mCommissionCanWithdrawNum));


        etCommissionNum.addTextChangedListener(etCommissionInputNumTextWatcher);
        //添加过滤器，只能输入两位小数，不能连续以0开始
        InputFilter[] filters = {new CashierInputFilter()};
        etCommissionNum.setFilters(filters);

        //弹出页面直接显示键盘
        etCommissionNum.setFocusable(true);
        etCommissionNum.setFocusableInTouchMode(true);
        etCommissionNum.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(etCommissionNum, 0);
                           }
                       },
                200);

        //显示PopupWindow
        View rootview = LayoutInflater.from(MyCommissionActivity.this).inflate(R.layout.activity_commission, null);
        mPopWindow.showAtLocation(rootview, Gravity.TOP, 0, 0);
    }

    /**
     * 关闭软件盘
     */

    public static void closeKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 提取佣金输入框监听
     */
    TextWatcher etCommissionInputNumTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            //获得输入金额
            String string = s.toString().trim();
            //控制光标的位置
            etCommissionNum.setSelection(string.length());

            //根据需求改变按钮的状态
            if (!TextUtils.isEmpty(string)) {
                mInPutCommissionNum = Double.valueOf(string);
                if (mInPutCommissionNum != 0L && mInPutCommissionNum <= mCommissionCanWithdrawNum) {
                    //根据输入判断是否可提现
                    tvCommissionWithdrawCommit.setEnabled(true);
                } else {
                    tvCommissionWithdrawCommit.setEnabled(false);
                }
                //输入大于可提取
                if (mInPutCommissionNum > mCommissionCanWithdrawNum) {
                    tvCommissionOver.setVisibility(View.VISIBLE);
                    llCommissionInputNotOver.setVisibility(View.GONE);
                } else {
                    tvCommissionOver.setVisibility(View.GONE);
                    llCommissionInputNotOver.setVisibility(View.VISIBLE);

                }
            } else {
                tvCommissionWithdrawCommit.setEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 跳转佣金详情
     */
    private void intoDetail() {
        StringBuffer sb = new StringBuffer(ClientAPI.URL_WX_H5);
        sb.append("myshouyi-detail.html");
        sb.append("?pageType=");
        sb.append("yongjin");
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());
        sb.append("&type=android");
        sb.append("&vt=").append(System.currentTimeMillis());

        String webShowUrl = sb.toString().trim();
        LogUtils.e("webShowUrl", webShowUrl);

        WebShowActivity.actionStart(MyCommissionActivity.this, webShowUrl, null);
    }
}
