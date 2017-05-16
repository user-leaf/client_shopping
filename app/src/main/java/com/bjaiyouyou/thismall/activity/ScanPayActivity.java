package com.bjaiyouyou.thismall.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.MainApplication;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4Cart;
import com.bjaiyouyou.thismall.client.Api4Home;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.ScanPayModel;
import com.bjaiyouyou.thismall.model.ShopModel;
import com.bjaiyouyou.thismall.utils.DialUtils;
import com.bjaiyouyou.thismall.utils.DialogUtils;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.KeyBoardUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.Utility;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.callback.StringCallback;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 扫码支付页面
 * Created by JackB on 2017/5/12.
 */
public class ScanPayActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = ScanPayActivity.class.getSimpleName();
    public static final String PARAM_SHOP_ID = "shop_id";
    public static final String PARAM_MONEY = "money";

    private long mShopId;                   // 商户id
    private String mShopName;               // 商户名称
    private double mMoney;                  // 收款金额
    private boolean hasMoney;               // 是否有收款金额

    private IUUTitleBar mTitleBar;
    private CircleImageView mIvHead;        // 头像
    private TextView mTvName;               // 姓名
    private TextView mTvBalance;            // 剩余券额

    // 固定金额支付
    private View mLlPayBanner;              // 支付栏
    private TextView mTvMoney;              // 固定金额
    private Button mBtnPay;                 // 支付按钮

    // 自定义金额支付
    private View mLlPayBannerCustomMoney;   // 支付栏
    private EditText mEtMoney;              // 金额输入框
    private Button mBtnPayCustomMoney;      // 支付按钮

    private Api4Cart mApi4Cart;
    private Api4Home mApi4Home;

    /**
     * 启动本页面
     *
     * @param context
     */
    public static void actionStart(Context context, long shopId, double money) {
        Intent intent = new Intent(context, ScanPayActivity.class);
        LogUtils.d(TAG, "actionStart shopId: " + shopId + ", money:" + money);
        intent.putExtra(PARAM_SHOP_ID, shopId);
        intent.putExtra(PARAM_MONEY, money);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_pay);

        mApi4Cart = (Api4Cart) ClientApiHelper.getInstance().getClientApi(Api4Cart.class);
        mApi4Home = (Api4Home) ClientApiHelper.getInstance().getClientApi(Api4Home.class);

        initVariable();
        initView();
        setupView();
        loadData();
    }

    private void initVariable() {
        // 解析扫码得到的json
        mShopId = getIntent().getLongExtra(PARAM_SHOP_ID, -1);
        mMoney = getIntent().getDoubleExtra(PARAM_MONEY, 0);

        LogUtils.d(TAG, "initVariable shopId: " + mShopId + ", money:" + mMoney);
//        if (!TextUtils.isEmpty(strJson)) {
//            Gson gson = new Gson();
//            ScanPayQRCodeModel scanPayQRCodeModel = gson.fromJson(strJson, ScanPayQRCodeModel.class);
//            mShopId = scanPayQRCodeModel.getShopId();
//            mMoney = scanPayQRCodeModel.getMoney();
//        } else {
//            ToastUtils.showShort("出错，请重新扫码[1]");
//        }
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.title_bar);

        mIvHead = (CircleImageView) findViewById(R.id.scan_pay_iv_head);
        mTvName = (TextView) findViewById(R.id.scan_pay_tv_name);
        mTvBalance = (TextView) findViewById(R.id.scan_pay_tv_balance);

        mLlPayBanner = findViewById(R.id.scan_pay_ll);
        mTvMoney = (TextView) findViewById(R.id.scan_pay_tv_money);
        mBtnPay = (Button) findViewById(R.id.scan_pay_btn_pay);

        mLlPayBannerCustomMoney = findViewById(R.id.scan_pay_ll_custom_money);
        mEtMoney = (EditText) findViewById(R.id.scan_pay_et_money);
        mBtnPayCustomMoney = (Button) findViewById(R.id.scan_pay_btn_pay_custom_money);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mEtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 限定只能输入两位小数
                // http://blog.csdn.net/yinzhijiezhan/article/details/46819261
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        mEtMoney.setText(s);
                        mEtMoney.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    mEtMoney.setText(s);
                    mEtMoney.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mEtMoney.setText(s.subSequence(0, 1));
                        mEtMoney.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBtnPay.setOnClickListener(this);
        mBtnPayCustomMoney.setOnClickListener(this);

    }

    private void loadData() {
        LogUtils.d(TAG, "" + mMoney);
        // 金额
        if (mMoney > 10E-6) {  // 有收款金额
            hasMoney = true;
            LogUtils.d(TAG, "hasMoney: " + hasMoney);
            mTvMoney.setText(String.valueOf(mMoney));
            showBanner(0);
        } else { // 未设定金额，自定义金额
            hasMoney = false;
            LogUtils.d(TAG, "hasMoney: " + hasMoney);
            showBanner(1);
        }

        // 商户头像、名称
        mApi4Home.getShopInfo(this, mShopId, new DataCallback<ShopModel>(this) {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response == null) {
                    return;
                }

                ShopModel shopModel = (ShopModel) response;

                String imgUrl = shopModel.getAvatar_path() + "/" + shopModel.getAvatar_name();
                LogUtils.d(ScanPayActivity.TAG, "imgUrl: " + imgUrl);
                Glide.with(ScanPayActivity.this)
                        .load(ImageUtils.getThumb(imgUrl, ScreenUtils.getScreenWidth(ScanPayActivity.this) / 4, 0))
                        .into(mIvHead);

                mShopName = shopModel.getNick_name();
                mTvName.setText("向商家用户（" + mShopName + "）支付");
                mTvBalance.setText("剩余券额" + shopModel.getUser_withdrawable_balance());

            }
        });
    }

    /**
     * 选择显示支付栏
     */
    private void showBanner(int i) {
        switch (i) {
            case 0:
                mLlPayBanner.setVisibility(View.VISIBLE);
                mLlPayBannerCustomMoney.setVisibility(View.GONE);
                break;

            case 1:
                mLlPayBanner.setVisibility(View.GONE);
                mLlPayBannerCustomMoney.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;

            case R.id.scan_pay_btn_pay: // 固定金额支付
                checkSafeCodeEmpty(v);
                break;

            case R.id.scan_pay_btn_pay_custom_money:    // 自定义金额支付
                checkSafeCodeEmpty(v);
                break;
        }
    }

    /**
     * 检查安全码是否为空
     *
     * @param v
     */
    private void checkSafeCodeEmpty(final View v) {

        showLoadingDialog();
        mApi4Cart.isSafeCodeEmpty(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dismissLoadingDialog();
                ToastUtils.showException(e);
            }

            @Override
            public void onResponse(String response, int id) {
                dismissLoadingDialog();
                if (TextUtils.isEmpty(response) || "[]".equals(response)) {
                    return;
                }

                if ("1".equals(response)) { // 设置了安全码
                    switch (v.getId()) {
                        case R.id.scan_pay_btn_pay: // 固定金额
                            if (hasMoney) {
                                showPayDialog(mMoney);
                            } else {
                                ToastUtils.showShort("出错，请重新扫码[2]");
                            }
                            break;

                        case R.id.scan_pay_btn_pay_custom_money:    // 自定义金额
                            String strMoney = mEtMoney.getText().toString();
                            if (TextUtils.isEmpty(strMoney) || "0".equals(strMoney)) {
                                ToastUtils.showShort("请输入支付金额");
                                return;
                            }
                            Double aDouble = Double.valueOf(strMoney);
                            showPayDialog(aDouble);
                            break;
                    }

                } else { // 没有设置安全码
                    showSafeCodeSettingDialog();
                }
            }
        });

    }

    /**
     * 支付弹窗
     *
     * @param money 金额
     */
    private void showPayDialog(final double money) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_scan_pay, null);
        final Dialog payDialog = DialogUtils.createRandomDialog(this, null, null, null, null, null,
                inflate
        );

        View ivClose = inflate.findViewById(R.id.scan_pay_dialog_iv_close);
        TextView tvShopName = (TextView) inflate.findViewById(R.id.scan_pay_dialog_tv_name);
        TextView tvMoney = (TextView) inflate.findViewById(R.id.scan_pay_dialog_tv_money);
        final EditText etSafecode = (EditText) inflate.findViewById(R.id.scan_pay_dialog_et_psw);
        TextView tvForget = (TextView) inflate.findViewById(R.id.scan_pay_dialog_tv_forget);
        Button btnCommit = (Button) inflate.findViewById(R.id.scan_pay_dialog_btn_commit);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isFastDoubleClick()) {
                    return;
                }

                switch (v.getId()) {
                    case R.id.scan_pay_dialog_iv_close:     // 关闭
                        if (payDialog != null && payDialog.isShowing()) {
                            payDialog.dismiss();
                        }
                        break;

                    case R.id.scan_pay_dialog_tv_forget:    // 忘记安全码
                        payDialog.dismiss();
                        showSafeCodeForgetDialog();
                        break;

                    case R.id.scan_pay_dialog_btn_commit:   // 确定
                        validateSafeCode(payDialog, etSafecode, money);

                        break;
                }
            }
        };

        tvShopName.setText("向商家用户（" + mShopName + "）支付");
        tvMoney.setText(String.valueOf(money));
        ivClose.setOnClickListener(onClickListener);
        tvForget.setOnClickListener(onClickListener);
        btnCommit.setOnClickListener(onClickListener);
        etSafecode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                validateSafeCode(payDialog, etSafecode, money);

                return true;
            }
        });

        payDialog.show();
    }

    /**
     * 安全码设置
     */
    private void showSafeCodeSettingDialog() {
        View inflateView = LayoutInflater.from(this).inflate(R.layout.dialog_pay_detail_safe_code_setting, null);
        final EditText etSafeCode = (EditText) inflateView.findViewById(R.id.pay_detail_dialog_et_safe_code);
        Dialog safeCodeSettingDialog = DialogUtils.createRandomDialog(this, "设置安全码", "确定", "取消",
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
     * 验证安全码
     * @param pswDialog
     * @param etPasswordView
     * @param money
     */
    private void validateSafeCode(final Dialog pswDialog, final EditText etPasswordView, final double money) {
        final String safeCode = etPasswordView.getText().toString();

        if (TextUtils.isEmpty(safeCode)) {
            ToastUtils.showShort("请输入安全码");
            return;
        }

        mApi4Cart.validateSafeCode(safeCode, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showException(e);
            }

            @Override
            public void onResponse(String response, int id) {
                KeyBoardUtils.closeKeybord(etPasswordView, MainApplication.getContext());
                pswDialog.dismiss();

                // 支付
                mApi4Home.payAfterScan(ScanPayActivity.this, money, safeCode,
                        mShopId, new DataCallback<ScanPayModel>(ScanPayActivity.this) {
                            @Override
                            public void onFail(Call call, Exception e, int id) {
                                ToastUtils.showException(e);
                            }

                            @Override
                            public void onSuccess(Object response, int id) {
                                LogUtils.d(TAG, "payAfterScan onResponse(): " + response);

                            }
                        });
            }
        });

    }

    /**
     * 忘记安全码
     */
    private void showSafeCodeForgetDialog() {
        // 拨打客服电话
        Dialog pswForgetDialog = DialogUtils.createConfirmDialog(
                ScanPayActivity.this,
                null, "拨打客服电话" + Constants.KEFU_TEL + "进行修改", "拨打", "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialUtils.callCentre(ScanPayActivity.this, DialUtils.CENTER_NUM);
                        dialog.dismiss();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        pswForgetDialog.show();

    }
}
