package shop.imake.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import shop.imake.Constants;
import shop.imake.MainApplication;
import shop.imake.R;
import shop.imake.adapter.PayWayAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4Cart;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.PayTypeModel;
import shop.imake.model.PayWayModel;
import shop.imake.utils.DialUtils;
import shop.imake.utils.DialogUtils;
import shop.imake.utils.DoubleTextUtils;
import shop.imake.utils.KeyBoardUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.PayUtils;
import shop.imake.utils.ScreenUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.Utility;
import shop.imake.widget.LoadingDialog;

/**
 * 支付底部弹窗Fragment
 * Created by JackB on 2017/4/25.
 */
public class PayDetailFragment extends DialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final String TAG = PayDetailFragment.class.getSimpleName();

    boolean payWayListGetSuccessFlag = false;   // 支付方式列表是否获取成功

    private double mMoney;          // 付款金额
    private String mStrOrderNum;    // 订单号

    // 支付详情
    private RelativeLayout mRlPayDetail;
    private TextView mTvPayWay;         // 付款方式

    // 支付方式
    private LinearLayout mLlPayWay;
    private ListView mLvPayWay;
    private PayWayAdapter mPayWayAdapter;
    private List<PayWayModel> mPayWayModels;

    private Context mContext;
    private Api4Cart mApi4Cart;

    private enum PayWayEnum {
        balance, alipay, wx
    }

    private PayWayEnum currentPayWay = PayWayEnum.balance;   // 当前的实付方式(默认余额)

    private PayCallback mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mMoney = arguments.getDouble(PayUtils.EXTRA_MONEY);
        mStrOrderNum = arguments.getString(PayUtils.EXTRA_ORDER_NUM);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();

        mApi4Cart = (Api4Cart) ClientApiHelper.getInstance().getClientApi(Api4Cart.class);

        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.fragment_pay_detail);
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.AnimBottom);
        }
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() / 3;
        window.setAttributes(lp);

        initView(dialog);

        return dialog;
    }

    private void initView(Dialog dialog) {
        // 支付详情
        mRlPayDetail = (RelativeLayout) dialog.findViewById(R.id.rl_pay_detail);
        ImageView ivCloseOne = (ImageView) dialog.findViewById(R.id.pay_detail_close);
        RelativeLayout rlPayWay = (RelativeLayout) dialog.findViewById(R.id.pay_detail_rl_pay_way);
        mTvPayWay = (TextView) dialog.findViewById(R.id.pay_detail_tv_pay_way);
        TextView tvMoney = (TextView) dialog.findViewById(R.id.pay_detail_tv_money);
        Button btnPay = (Button) dialog.findViewById(R.id.pay_detail_btn_confirm_pay);  // 立即支付

        ivCloseOne.setOnClickListener(this);
        rlPayWay.setOnClickListener(this);
        btnPay.setOnClickListener(this);

        // 支付方式
        mLlPayWay = (LinearLayout) dialog.findViewById(R.id.ll_pay_way);
        View ivPayWayBack = dialog.findViewById(R.id.pay_way_iv_back);      // 返回按钮

        ivPayWayBack.setOnClickListener(this);

        // 支付方式列表
        mLvPayWay = (ListView) dialog.findViewById(R.id.pay_way_listview);
        mPayWayModels = new ArrayList<>();
        LogUtils.d(TAG, PayWayEnum.balance.name());
        LogUtils.d(TAG, PayWayEnum.balance.toString());

        // 获取支付方式列表
        loadData4PayWayList();

//        PayWayModel payWayModel = new PayWayModel(PayWayEnum.balance.name(), R.mipmap.list_icon_coincertificate, "众汇券", true, true);
//        mPayWayModels.add(payWayModel);
//        PayWayModel payWayModel2 = new PayWayModel(PayWayEnum.alipay.name(), R.mipmap.list_icon_alipay, "支付宝", false, false);
//        mPayWayModels.add(payWayModel2);
//        PayWayModel payWayModel3 = new PayWayModel(PayWayEnum.wx.name(), R.mipmap.list_icon_wechatpay, "微信支付", false, false);
//        mPayWayModels.add(payWayModel3);

//        mPayWayAdapter = new PayWayAdapter(getContext(), mPayWayModels);
//        mLvPayWay.setAdapter(mPayWayAdapter);
//        mLvPayWay.setOnItemClickListener(this);

        // 设置数据
        tvMoney.setText(DoubleTextUtils.setDoubleUtils(mMoney));
    }

    /**
     * 获取支付方式列表
     */
    private void loadData4PayWayList() {
        Api4Cart api4Cart = (Api4Cart) ClientApiHelper.getInstance().getClientApi(Api4Cart.class);
        api4Cart.getPayWayList(true, new DataCallback<PayTypeModel>(mContext) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                payWayListGetSuccessFlag = false;
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response == null) {
                    return;
                }
                payWayListGetSuccessFlag = true;

                PayTypeModel payTypeModel = (PayTypeModel) response;
                List<PayTypeModel.PayTypesBean> pay_types = payTypeModel.getPay_types();
                if (pay_types == null) {
                    return;
                }

                mPayWayModels.clear();
                for (PayTypeModel.PayTypesBean item : pay_types) {
                    PayWayModel payWayModel = new PayWayModel();
                    payWayModel.setPayWay(item.getPay_param());
                    payWayModel.setIcon(item.getIcon());
                    payWayModel.setTitle(item.getName());
                    payWayModel.setRecommend(false);
                    payWayModel.setChoose(false);

                    // 图标
                    if (PayWayEnum.balance.name().equals(item.getPay_param())) {
                        payWayModel.setResId(R.mipmap.list_icon_coincertificate);
                    } else if (PayWayEnum.alipay.name().equals(item.getPay_param())) {
                        payWayModel.setResId(R.mipmap.list_icon_alipay);
                    }else if (PayWayEnum.wx.name().equals(item.getPay_param())){
                        payWayModel.setResId(R.mipmap.list_icon_wechatpay);
                    }
//                    else if (PayWayEnum.ips.name().equals(item.getPay_param())) {
//                        payWayModel.setResId(R.mipmap.list_icon_ring_payment);
//                    } else if (PayWayEnum.yee_pay.name().equals(item.getPay_param())){
//                        payWayModel.setResId(R.mipmap.list_logo_yibaozhifu);
//                    }

                    if (PayWayEnum.balance.name().equals(item.getPay_param())) {
                        payWayModel.setChoose(true);
                    }
                    mPayWayModels.add(payWayModel);
                }
                mPayWayModels.get(0).setRecommend(true);

                mPayWayAdapter = new PayWayAdapter(getContext(), mPayWayModels);
                mLvPayWay.setAdapter(mPayWayAdapter);
                mLvPayWay.setOnItemClickListener(PayDetailFragment.this);
            }
        });
    }

    @Override
    public void onClick(View v) {

        Animation slide_left_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_left_to_left);
        Animation slide_right_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_right_to_left);
        Animation slide_left_to_right = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_left_to_right);
        Animation slide_left_to_left_in = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_left_to_left_in);

        switch (v.getId()) {
            case R.id.pay_detail_close: // 关闭
                getDialog().dismiss();
                break;

            case R.id.pay_detail_rl_pay_way:    // 选择方式
                if (!payWayListGetSuccessFlag) {
                    ToastUtils.showShort("支付列表获取失败");
                    loadData4PayWayList();
                    return;
                }

                mRlPayDetail.startAnimation(slide_left_to_left);
                mRlPayDetail.setVisibility(View.GONE);
                mLlPayWay.startAnimation(slide_right_to_left);
                mLlPayWay.setVisibility(View.VISIBLE);
                break;

            case R.id.pay_detail_btn_confirm_pay:   // 确认付款
                getDialog().dismiss();

                LogUtils.d(TAG, "currentPayWay: " + currentPayWay);
                switch (currentPayWay) {
                    case balance:
                        final LoadingDialog loadingDialog = LoadingDialog.getInstance(mContext);
                        loadingDialog.show();
                        mApi4Cart.isSafeCodeEmpty(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                loadingDialog.dismiss();
                                ToastUtils.showException(e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                loadingDialog.dismiss();
                                if (TextUtils.isEmpty(response) || "[]".equals(response)) {
                                    return;
                                }

                                if ("1".equals(response)) { // 设置了安全码
                                    showPasswordDialog(Constants.CHANNEL_BALANCE);

                                } else { // 没有设置安全码
                                    showSafeCodeSettingDialog();
                                }
                            }
                        });
                        break;

                    case alipay:
                        // 调用支付
                        mCallback.onPayCallback(Constants.CHANNEL_ALIPAY);
                        break;

                    case wx:
                        mCallback.onPayCallback(Constants.CHANNEL_WECHAT);
                        break;

                    default:
                        break;
                }
                break;

            case R.id.pay_way_iv_back:  // 支付方式返回
                mRlPayDetail.startAnimation(slide_left_to_left_in);
                mRlPayDetail.setVisibility(View.VISIBLE);
                mLlPayWay.startAnimation(slide_left_to_right);
                mLlPayWay.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    /**
     * 安全码设置
     */
    private void showSafeCodeSettingDialog() {
        View inflateView = LayoutInflater.from(mContext).inflate(R.layout.dialog_pay_detail_safe_code_setting, null);
        final EditText etSafeCode = (EditText) inflateView.findViewById(R.id.pay_detail_dialog_et_safe_code);
        Dialog safeCodeSettingDialog = DialogUtils.createRandomDialog(mContext, "设置安全码", "确定", "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String strSafeCode = etSafeCode.getText().toString();
                        mApi4Cart.setSafeCode(strSafeCode, new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                dialog.dismiss();
                                ToastUtils.showException(e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                dialog.dismiss();
                                ToastUtils.showShort("安全码设置成功");
                            }
                        });
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                },
                inflateView
        );
        safeCodeSettingDialog.show();
    }

    /**
     * 安全码输入
     *
     * @param channel
     */
    private void showPasswordDialog(final String channel) {
        final Dialog pswDialog = new Dialog(mContext, R.style.BottomDialog);
        pswDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        pswDialog.setContentView(R.layout.fragment_pay_pwd);
        pswDialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = pswDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.AnimBottom);
        }
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
//        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() >> 2;
        lp.height = ScreenUtils.getScreenHeight(mContext) / 4;
        window.setAttributes(lp);

        View closeView = pswDialog.findViewById(R.id.pay_psw_close);
        final View commitView = pswDialog.findViewById(R.id.pay_psw_tv_commit);
        final EditText etPasswordView = (EditText) pswDialog.findViewById(R.id.pay_psw_et);
        View tvForget = pswDialog.findViewById(R.id.pay_psw_tv_forget);

        final View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Utility.isFastDoubleClick()) {
                    return;
                }

                switch (v.getId()) {
                    case R.id.pay_psw_close: // 关闭
                        KeyBoardUtils.closeKeybord(etPasswordView, mContext);
                        pswDialog.dismiss();
                        break;

                    case R.id.pay_psw_tv_commit: // 提交
                        validateSafeCode(pswDialog, etPasswordView, channel, commitView, this);
                        break;

                    case R.id.pay_psw_tv_forget: // 忘记安全码
                        KeyBoardUtils.closeKeybord(etPasswordView, mContext);
                        pswDialog.dismiss();
                        showSafeCodeForgetDialog();
                        break;
                }
            }
        };

        closeView.setOnClickListener(onClickListener);
        commitView.setOnClickListener(onClickListener);
        tvForget.setOnClickListener(onClickListener);

        etPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                validateSafeCode(pswDialog, etPasswordView, channel, commitView, onClickListener);
                return true;
            }
        });

        pswDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtils.openKeybord(etPasswordView, mContext);
            }
        }, 300);
    }

    /**
     * 验证安全码
     *  @param pswDialog
     * @param etPasswordView
     * @param channel
     * @param clickView
     * @param onClickListener
     */
    private void validateSafeCode(final Dialog pswDialog, final EditText etPasswordView, final String channel, final View clickView, final View.OnClickListener onClickListener) {
        String safeCode = etPasswordView.getText().toString();

        clickView.setOnClickListener(null);

        final LoadingDialog loadingDialog = LoadingDialog.getInstance(mContext);
        loadingDialog.show();

        mApi4Cart.validateSafeCode(safeCode, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                loadingDialog.dismiss();
                clickView.setOnClickListener(onClickListener);
                ToastUtils.showException(e);
                etPasswordView.getText().clear();
            }

            @Override
            public void onResponse(String response, int id) {
                loadingDialog.dismiss();
                clickView.setOnClickListener(onClickListener);
                KeyBoardUtils.closeKeybord(etPasswordView, MainApplication.getContext());
                pswDialog.dismiss();
                // 调用支付
                mCallback.onPayCallback(channel);
            }
        });

    }

    /**
     * 忘记安全码
     */
    private void showSafeCodeForgetDialog() {
        // 拨打客服电话
        DialUtils.callSafeCodeForget(mContext);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (Utility.isFastDoubleClick()) {
            return;
        }

        for (PayWayModel item : mPayWayModels) {
            item.setChoose(false);
        }
        mPayWayModels.get(position).setChoose(true);
        mPayWayAdapter.notifyDataSetChanged();

        currentPayWay = PayWayEnum.valueOf(mPayWayModels.get(position).getPayWay());
        String payWay = null;

        switch (currentPayWay) {
            case balance:
                payWay = "众汇券";
                break;

            case alipay:
                payWay = "支付宝";
                break;

            case wx:
                payWay = "微信支付";
                break;

            default:
                break;
        }

        mTvPayWay.setText(payWay);
        LogUtils.d(TAG, "onItemClick: " + payWay);

        Animation slide_left_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_left_to_left);
        Animation slide_right_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_right_to_left);
        Animation slide_left_to_right = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_left_to_right);
        Animation slide_left_to_left_in = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_slide_left_to_left_in);

        mRlPayDetail.startAnimation(slide_left_to_left_in);
        mRlPayDetail.setVisibility(View.VISIBLE);
        mLlPayWay.startAnimation(slide_left_to_right);
        mLlPayWay.setVisibility(View.GONE);
    }

    public void setOnPayCallback(PayCallback callback) {
        mCallback = callback;
    }

    public interface PayCallback {
        // 调用ping++支付
        void onPayCallback(String channel);
    }
}
