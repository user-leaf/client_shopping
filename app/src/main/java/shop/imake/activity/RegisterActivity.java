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

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4Mine;
import shop.imake.client.ClientAPI;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.TokenModel;
import shop.imake.utils.CountDownButtonHelper;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.StringUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.ValidatorsUtils;
import shop.imake.widget.IUUTitleBar;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private IUUTitleBar mTitleBar;

    private View mTipsView;             // 提示栏
    private TextView mTvTelTips;        // 提示
    private EditText mEtTel;            // 手机号
    private ImageView mIvDelete;        // 手机号输入框中的删除按钮
    private EditText mEtVeriCode;       // 验证码
    private Button mBtnGetVeriCode;     // 获取验证码
    private EditText mEtInviteCode;     // 邀请码
    private View mBtnLogin;             // 登录按钮
    private TextView mTvAgree2;
    private View mTvGotoLogin;
    private Api4Mine mApi4Mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mApi4Mine = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);

        initView();
        setupView();
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.register_title_bar);
        mTvAgree2 = (TextView) findViewById(R.id.register_tv_agree);
        mBtnGetVeriCode = (Button) findViewById(R.id.register_btn_get_veri_code);
        mTipsView = findViewById(R.id.register_ll_tips);
        mTvTelTips = (TextView) findViewById(R.id.register_tv_tips);
        mEtTel = (EditText) findViewById(R.id.register_et_tel);
        mIvDelete = (ImageView) findViewById(R.id.register_iv_delete);
        mEtVeriCode = ((EditText) findViewById(R.id.register_et_veri_code));
        mBtnLogin = findViewById(R.id.register_btn_login);
        mTvGotoLogin = findViewById(R.id.register_tv_login);
        mEtInviteCode = (EditText) findViewById(R.id.register_et_invite_code);

    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mBtnGetVeriCode.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mIvDelete.setOnClickListener(this);
        mTvGotoLogin.setOnClickListener(this);
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

        mEtInviteCode.addTextChangedListener(new TextWatcher() {
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
        String str1 = "注册即同意";
        String str2 = "《用户服务协议》";
        String text = str1 + str2;
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(RegisterActivity.this, WebShowActivity.class);
                intent.putExtra(WebShowActivity.PARAM_URLPATH, ClientAPI.URL_WX_H5 + "user-instructions.html");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue));
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
     * 检查输入完整性，都输入了，则登录按钮变亮
     */
    private void checkInputComplete() {
        if (!TextUtils.isEmpty(mEtTel.getText().toString())
                && !TextUtils.isEmpty(mEtVeriCode.getText().toString())
                && !TextUtils.isEmpty(mEtInviteCode.getText().toString())) {
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:          // 返回
                finish();
                break;

            case R.id.register_btn_get_veri_code:  // 获取验证码
                sendVeriCode();
                break;

            case R.id.register_btn_login:   //短信验证登录
                doRegister();
                break;

            case R.id.register_iv_delete:   // 手机号输入框中的删除按钮
                mEtTel.getText().clear();
                break;

            case R.id.register_tv_login:    // 去登录
                finish();
                break;

            default:
                break;
        }
    }

    // 处理登录
    private void doRegister() {
        final String phone = mEtTel.getText().toString();
        final String password = mEtVeriCode.getText().toString();
        final String invitationCode = mEtInviteCode.getText().toString();

        if (!ValidatorsUtils.validateUserPhone(phone)) {
            mTipsView.setVisibility(View.VISIBLE);
            mTvTelTips.setText("手机号码有误");
            return;
        }
        if (TextUtils.isEmpty(invitationCode)) {
            mTipsView.setVisibility(View.VISIBLE);
            mTvTelTips.setText("请输入邀请人手机号");
            return;
        }

        if (!TextUtils.isEmpty(invitationCode) && !ValidatorsUtils.validateUserPhone(invitationCode)) {
            mTipsView.setVisibility(View.VISIBLE);
            mTvTelTips.setText("邀请人号码有误");
            return;
        }

        showLoadingDialog();

        mApi4Mine.register(phone, password, invitationCode, new DataCallback<TokenModel>(this) {

            @Override
            public void onFail(Call call, Exception e, int id) {
                dismissLoadingDialog();

                if (NetStateUtils.isNetworkAvailable(RegisterActivity.this)) {
                    mTipsView.setVisibility(View.VISIBLE);
                    mTvTelTips.setText(StringUtils.getExceptionMessage(e.getMessage()));
                } else {
                    mTipsView.setVisibility(View.VISIBLE);
                    mTvTelTips.setText("网络未连接");
                }
            }

            @Override
            public void onSuccess(Object response, int id) {

                dismissLoadingDialog();
                ToastUtils.showShort("注册成功");
//                if (response == null) {
//                    return;
//                }
//
//                TokenModel tokenModel = (TokenModel) response;
//                String token = tokenModel.getToken();
//
//                if (TextUtils.isEmpty(token)) {
//                    ToastUtils.showShort("token为空");
//                    return;
//                }
//
//                LogUtils.d(TAG, "token：" + token);
//                CurrentUserManager.setUserToken(token);
//                setResult(RESULT_OK);
                finish();
            }

        });

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
                .append(phone.trim())
                .append("&type=register");
        String url = sb.toString();

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (NetStateUtils.isNetworkAvailable(RegisterActivity.this)) {
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
