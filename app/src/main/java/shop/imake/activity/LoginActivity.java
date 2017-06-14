package shop.imake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.client.ClientAPI;
import shop.imake.model.TokenModel;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.CountDownButtonHelper;
import shop.imake.utils.LogUtils;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.StringUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.ValidatorsUtils;
import shop.imake.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 登录页
 *
 * @date 2016/6/18
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = LoginActivity.class.getSimpleName();
    private IUUTitleBar mTitleBar;

    private View mTipsView;             // 提示栏
    private TextView mTvTelTips;        // 提示
    private EditText mEtTel;            // 手机号
    private ImageView mIvDelete;        // 手机号输入框中的删除按钮
    private EditText mEtVeriCode;       // 验证码
    private Button mBtnGetVeriCode;     // 获取验证码
    private View mBtnLogin;             // 登录按钮
    private TextView mTvAgree2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        setupView();
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.login_title_bar);
        mTvAgree2 = (TextView) findViewById(R.id.login_tv_agree2);
        mBtnGetVeriCode = (Button) findViewById(R.id.login_btn_get_veri_code);
        mTipsView = findViewById(R.id.login_ll_tips);
        mTvTelTips = (TextView) findViewById(R.id.login_tv_tips);
        mEtTel = (EditText) findViewById(R.id.login_et_tel);
        mIvDelete = (ImageView) findViewById(R.id.login_iv_delete);
        mEtVeriCode = ((EditText) findViewById(R.id.login_et_veri_code));
        mBtnLogin = findViewById(R.id.login_btn_login);

    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mBtnGetVeriCode.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mIvDelete.setOnClickListener(this);
        mEtTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIvDelete.setVisibility(s.length() > 0 ? View.VISIBLE : View.INVISIBLE);

                checkInputComplete();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEtVeriCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputComplete();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setAgreeData();
    }

    /**
     * 设置 用户服务协议 字段
     */
    private void setAgreeData() {
        String str1 = "阅读并同意";
        String str2 = "《用户服务协议》";
        String text = str1 + str2;
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, WebShowActivity.class);
                intent.putExtra(WebShowActivity.PARAM_URLPATH, ClientAPI.URL_WX_H5 + "user-instructions.html");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.app_red));
                ds.setUnderlineText(false);
            }
        };

        avoidHintColor(mTvAgree2);
        spannableString.setSpan(clickableSpan, str1.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAgree2.setText(spannableString);
        mTvAgree2.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void avoidHintColor(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setHighlightColor(getResources().getColor(android.R.color.transparent));
        }
    }

    /**
     * 检查输入完整性，都输入了，则登录按钮变红
     */
    private void checkInputComplete() {
        if (!TextUtils.isEmpty(mEtTel.getText().toString())
                && !TextUtils.isEmpty(mEtVeriCode.getText().toString())) {
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;

            case R.id.login_btn_get_veri_code: // 获取验证码
                sendVeriCode();
                break;

            case R.id.login_btn_login: //短信验证登录
                doLogin();
                break;

            case R.id.login_iv_delete: // 手机号输入框中的删除按钮
                mEtTel.setText("");
                break;

        }
    }

    // 处理登录
    private void doLogin() {
        final String phone = mEtTel.getText().toString();
        final String password = mEtVeriCode.getText().toString();

        if (!ValidatorsUtils.validateUserPhone(phone)) {
            mTipsView.setVisibility(View.VISIBLE);
            mTvTelTips.setText("手机号码有误");
            return;
        }

        showLoadingDialog();

        ClientAPI.postLogin(phone, password, null, new

                StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dismissLoadingDialog();

                        if (NetStateUtils.isNetworkAvailable(LoginActivity.this)) {
                            mTipsView.setVisibility(View.VISIBLE);
                            mTvTelTips.setText(StringUtils.getExceptionMessage(e.getMessage()));
                        } else {
                            mTipsView.setVisibility(View.VISIBLE);
                            mTvTelTips.setText("网络未连接");
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dismissLoadingDialog();

                        if (response != null && !"[]".equals(response)) {
                            Gson gson = new Gson();
                            TokenModel tokenModel = gson.fromJson(response, TokenModel.class);
                            if (tokenModel == null) {
                                return;
                            }

                            String token = tokenModel.getToken();
                            if (token != null) {
                                LogUtils.d(TAG, "保存token：" + token);
                                CurrentUserManager.setUserToken(token);
                                setResult(RESULT_OK);
                                finish();

                            } else {
                                ToastUtils.showShort("token为空");
                            }
                        }
                    }
                }
        );
    }

    /**
     * 发送验证码
     */
    public void sendVeriCode() {
        String phone = mEtTel.getText().toString();

        if ("".equals(phone)) {
            mTipsView.setVisibility(View.VISIBLE);
            mTvTelTips.setText("请输入手机号码");
            return;
        }

        if (!ValidatorsUtils.validateUserPhone(phone)) {
            mTipsView.setVisibility(View.VISIBLE);
            mTvTelTips.setText("手机号码有误");
            return;
        }

        mBtnGetVeriCode.setEnabled(false);

        CountDownButtonHelper helper = new CountDownButtonHelper(mBtnGetVeriCode,
                "获取验证码", 60, 1);
        helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {

            @Override
            public void finish() {
                mBtnGetVeriCode.setEnabled(true);
                mBtnGetVeriCode.setText("再次获取");
            }
        });
        helper.start();


        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/auth/getRandPassword")
                .append("?phone=")
                .append(phone.trim());
        String url = sb.toString();

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (NetStateUtils.isNetworkAvailable(LoginActivity.this)) {
                            mTipsView.setVisibility(View.VISIBLE);
                            mTvTelTips.setText(StringUtils.getExceptionMessage(e.getMessage()));
                        } else {
                            mTipsView.setVisibility(View.VISIBLE);
                            mTvTelTips.setText("网络未连接");
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                    }
                });

    }
}
