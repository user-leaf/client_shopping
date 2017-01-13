package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.TokenModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.SPUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 临时的登录页
 *
 * @author kanbin
 */
public class LoginTempActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEtPhone;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private Button mBtnPrintToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_temp);

        initView();
        setupView();
    }

    private void initView() {
        mEtPhone = (EditText) findViewById(R.id.login_new_et_phone);
        mEtPassword = (EditText) findViewById(R.id.login_new_et_password);
        mBtnLogin = (Button) findViewById(R.id.login_new_btn_login);
        mBtnPrintToken = (Button) findViewById(R.id.login_new_btn_print_token);
    }

    private void setupView() {
        mBtnLogin.setOnClickListener(this);
        mBtnPrintToken.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_new_btn_login:
                StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
                sb.append("api/v1/auth/login");
                String url = sb.toString();
                String phone = mEtPhone.getText().toString();
                String password = mEtPassword.getText().toString();
                OkHttpUtils.post()
                        .url(url)
                        .addParams("phone", phone)
                        .addParams("password", password)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showShort("登录失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Gson gson = new Gson();
                                TokenModel tokenModel = gson.fromJson(response, TokenModel.class);
                                String token = tokenModel.getToken();
                                if (token != null) {
                                    LogUtils.d("保存token："+token);
                                    CurrentUserManager.setUserToken(token);
                                    ToastUtils.showShort("登录成功");
                                } else {
                                    ToastUtils.showShort("token为空");
                                }
                            }
                        });
                break;

            case R.id.login_new_btn_print_token:
                String token = (String) SPUtils.get(this, "user_token", "");
                LogUtils.d(""+token);
                break;
        }
    }
}
