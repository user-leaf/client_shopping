package shop.imake.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4Mine;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.TokenModel;
import shop.imake.model.User;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.CountDownButtonHelper;
import shop.imake.utils.LogUtils;
import shop.imake.utils.NTalkerUtils;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.StringUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.ValidatorsUtils;
import shop.imake.widget.IUUTitleBar;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 登录页
 *
 * @date 2016/6/18
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private IUUTitleBar mTitleBar;

    private View mTipsView;             // 提示栏
    private TextView mTvTelTips;        // 提示
    private EditText mEtTel;            // 手机号
    private ImageView mIvDelete;        // 手机号输入框中的删除按钮
    private EditText mEtVeriCode;       // 验证码
    private Button mBtnGetVeriCode;     // 获取验证码
    private View mBtnLogin;             // 登录按钮
    private View mTvGotoRegister;       // 去注册
    private Api4Mine mApi4Mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mApi4Mine = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);

        initView();
        setupView();
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.login_title_bar);
        mBtnGetVeriCode = (Button) findViewById(R.id.login_btn_get_veri_code);
        mTipsView = findViewById(R.id.login_ll_tips);
        mTvTelTips = (TextView) findViewById(R.id.login_tv_tips);
        mEtTel = (EditText) findViewById(R.id.login_et_tel);
        mIvDelete = (ImageView) findViewById(R.id.login_iv_delete);
        mEtVeriCode = ((EditText) findViewById(R.id.login_et_veri_code));
        mBtnLogin = findViewById(R.id.login_btn_login);
        mTvGotoRegister = findViewById(R.id.login_tv_register);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mBtnGetVeriCode.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mIvDelete.setOnClickListener(this);
        mTvGotoRegister.setOnClickListener(this);
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

    }

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
            case R.id.left_layout:          // 返回
                finish();
                break;

            case R.id.login_btn_get_veri_code: // 获取验证码
                sendVeriCode();
                break;

            case R.id.login_btn_login:      // 短信验证登录
                doLogin();
                break;

            case R.id.login_iv_delete:      // 手机号输入框中的删除按钮
                mEtTel.getText().clear();
                break;

            case R.id.login_tv_register:    // 去注册
                jump(RegisterActivity.class, false);
                break;

            default:
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

        mApi4Mine.login(phone, password, new DataCallback<TokenModel>(this) {

            @Override
            public void onFail(Call call, Exception e, int id) {

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
            public void onSuccess(Object response, int id) {

                dismissLoadingDialog();

                if (response == null) {
                    return;
                }

                TokenModel tokenModel = (TokenModel) response;

                String token = tokenModel.getToken();

                if (token == null) {
                    ToastUtils.showShort("token为空");
                    return;
                }

                LogUtils.d(TAG, "token：" + token);
                CurrentUserManager.setUserToken(token);

                loginXiaoneng();

                setResult(RESULT_OK);
                finish();

            }
        });
    }

    private void loginXiaoneng() {
        mApi4Mine.getUserMessage(this, new DataCallback<User>(this) {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onSuccess(Object response, int id) {
                User user = (User) response;
                LogUtils.d(TAG, "loginXiaoneng:" + user.toString());
                if (user == null) {
                    return;
                }
                CurrentUserManager.setCurrentUser(user.getMember());
                NTalkerUtils.getInstance().login();
            }
        });
    }

    /**
     * 发送验证码
     */
    public void sendVeriCode() {
        String phone = mEtTel.getText().toString();

        if (TextUtils.isEmpty(phone)) {
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

        CountDownButtonHelper helper = new CountDownButtonHelper(
                mBtnGetVeriCode, "获取验证码", 60, 1);

        helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {

            @Override
            public void finish() {
                mBtnGetVeriCode.setEnabled(true);
                mBtnGetVeriCode.setText("再次获取");
            }
        });
        helper.start();

        mApi4Mine.getVeriCode(phone, new StringCallback() {
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
