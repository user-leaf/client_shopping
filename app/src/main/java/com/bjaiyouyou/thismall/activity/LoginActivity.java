package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjaiyouyou.thismall.MainActivity;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.TokenModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.StringUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.ValidateUserInputUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;

/**
 * 登录页
 *
 * @author kanbin
 * @date 2016/6/18
 * @author QuXinhang
 * UpDate 2016/6/25 10:12
 * <p>
 * 添加三方登录的入口
 * 添加第三方登录的方法
 * @author QuXinhang
 * UpDate 2016/6/25 10:12
 * <p>
 * 添加三方登录的入口
 * 添加第三方登录的方法
 */

/**
 * @author QuXinhang
 *         UpDate 2016/6/25 10:12
 *         <p/>
 *         添加三方登录的入口
 *         添加第三方登录的方法
 */

/**
 *  修改登录方法
 * @author QuXinhang
 *Creare 2016/8/30 20:17
 *
 *
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    // 标题栏
    private IUUTitleBar mTitleBar;
    // 获取验证码
    private TextView mBtnGetVeriCode;
    // 倒计时
    private CountDownTimer mCountDownTimer;
    //三方登录的入口
    private ImageView mIVQQ;
    private ImageView mIVWetch;
    private ImageView mIvSina;
    //数据获取电话
//    private EditText mEtTel;
    private EditText mEtTel;
    //数据获取验证码
    private EditText mEtVeriCode;
    //登录按钮
    private View mBtnLogin;
    // 提示栏
    private View mTipsView;
    // 手机号输入框上方的提示
    private TextView mTvTelTips;
    // 手机号输入框中的删除按钮
    private ImageView mIvDelete;
    // 邀请码
    private EditText mEtInviteCode;
    // 加载中
//    private AVLoadingIndicatorView mLoadingView;
    private TextView mTvAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ShareSDK.initSDK(this);

        initView();
        setupView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.login_title_bar);
        mTvAgree = (TextView) findViewById(R.id.login_tv_agree);
        mBtnGetVeriCode = (TextView) findViewById(R.id.login_btn_get_veri_code);
        mTipsView = findViewById(R.id.login_ll_tips);
        mTvTelTips = (TextView) findViewById(R.id.login_tv_tips);
        mEtTel = (EditText) findViewById(R.id.login_et_tel);
        mIvDelete = (ImageView) findViewById(R.id.login_iv_delete);
        mEtVeriCode = ((EditText) findViewById(R.id.login_et_veri_code));
        mEtInviteCode = (EditText) findViewById(R.id.login_et_invite_code);
        mBtnLogin = (TextView) findViewById(R.id.login_btn_login);

        // 60秒
        mCountDownTimer = new CountDownTimer(60 * 1000, 1 * 1000 - 10) {

            @Override
            public void onTick(long millisUntilFinished) {
                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
                mBtnGetVeriCode.setText("再次获取(" + ((millisUntilFinished + 15) / 1000) + "秒)");
            }

            @Override
            public void onFinish() {
                mBtnGetVeriCode.setEnabled(true);
                mBtnGetVeriCode.setText("再次获取");
            }
        };
//        三方登录入口控件
        mIVQQ = ((ImageView) findViewById(R.id.iv_login_QQ));
        mIVWetch = ((ImageView) findViewById(R.id.iv_login_wechat));
        mIvSina = ((ImageView) findViewById(R.id.iv_login_sina));

    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mBtnGetVeriCode.setOnClickListener(this);

//       添加注册监听
        mIVQQ.setOnClickListener(this);
        mIVWetch.setOnClickListener(this);
        mIvSina.setOnClickListener(this);

        mBtnLogin.setOnClickListener(this);
        // 在xml中把Button、TextView设置为android:clickable="false"不起作用
        mBtnLogin.setClickable(false);

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
        String str1 = "温馨提示：未注册的手机号，登录时将自动注册，且代表您已经同意";
        String str2 = "《用户服务协议》";
        String text = str1 + str2;
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, WebShowActivity.class);
                intent.putExtra(WebShowActivity.PARAM_URLPATH, "http://wxweb.bjaiyouyou.com/user-instructions.html");
                startActivity(intent);
                avoidHintColor(widget);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableString.setSpan(clickableSpan,str1.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvAgree.setText(spannableString);
        mTvAgree.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void avoidHintColor(View view) {
        if(view instanceof TextView)
            ((TextView)view).setHighlightColor(getResources().getColor(android.R.color.transparent));
    }

    /**
     * 检查输入完整性，都输入了，则登录按钮变红
     */
    private void checkInputComplete() {
        if (!TextUtils.isEmpty(mEtTel.getText().toString()) && !TextUtils.isEmpty(mEtVeriCode.getText().toString())) {
            mBtnLogin.setAlpha(1);
            mBtnLogin.setClickable(true);
        } else {
            mBtnLogin.setAlpha(0.5f);
            mBtnLogin.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;

            case R.id.login_btn_get_veri_code: // 获取验证码
                String phone = mEtTel.getText().toString().trim();

                if ("".equals(phone)) {
                    mTipsView.setVisibility(View.VISIBLE);
                    mTvTelTips.setText("请输入手机号码");
                    return;
                }

                if (!ValidateUserInputUtils.validateUserPhone(phone)){
                    mTipsView.setVisibility(View.VISIBLE);
                    mTvTelTips.setText("手机号码有误");
                    return;
                }

                mBtnGetVeriCode.setEnabled(false);
                mCountDownTimer.start();

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
                                }else {
                                    mTipsView.setVisibility(View.VISIBLE);
                                    mTvTelTips.setText("网络未连接");
                                }
                            }

                            @Override
                            public void onResponse(String response, int id) {
//                                Toast.makeText(LoginActivity.this, "已发送", Toast.LENGTH_SHORT).show();
                            }
                        });

                break;

//            点击跳转到指定平台

            case R.id.login_btn_login: //短信验证登录
                doLogin();
                break;

            case R.id.iv_login_QQ:
                login(new QZone(this));
                break;

            case R.id.iv_login_wechat:
//                login(ShareSDK.getPlatform(this, Wechat.NAME));
                Platform wechat = ShareSDK.getPlatform(this, Wechat.NAME);
                wechat.setPlatformActionListener(this);
                wechat.authorize();
                break;

            case R.id.iv_login_sina:
                login(new SinaWeibo(this));
                break;

            case R.id.login_iv_delete: // 手机号输入框中的删除按钮
                mEtTel.setText("");
                break;

        }
    }

    //处理登录
    private void doLogin() {
        String phone = mEtTel.getText().toString();
        String password = mEtVeriCode.getText().toString();
        String invitationCode = mEtInviteCode.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            mTipsView.setVisibility(View.VISIBLE);
            mTvTelTips.setText("请输入手机号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mTipsView.setVisibility(View.VISIBLE);
            mTvTelTips.setText("请输入密码");
            return;
        }

        // 检查手机号
        if (phone.length() < 11) {
            mTipsView.setVisibility(View.VISIBLE);
            mTvTelTips.setText("输入位数不足，不是有效的手机号");
            return;
        } else if (phone.length() > 11) {
            mTipsView.setVisibility(View.VISIBLE);
            mTvTelTips.setText("输入位数过多，不是有效的手机号");
            return;
        }else if (!ValidateUserInputUtils.validateUserPhone(phone)){
            mTipsView.setVisibility(View.VISIBLE);
            mTvTelTips.setText("手机号码有误");
            return;
        }

        if (!TextUtils.isEmpty(invitationCode)){
            if (!ValidateUserInputUtils.validateUserPhone(invitationCode)) {
                mTipsView.setVisibility(View.VISIBLE);
                mTvTelTips.setText("邀请人号码有误");
                return;
            }
        }

        loadingDialog.show();

        ClientAPI.postLogin(phone, password, invitationCode, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                loadingDialog.dismiss();
                if (NetStateUtils.isNetworkAvailable(LoginActivity.this)) {
                    mTipsView.setVisibility(View.VISIBLE);
                    mTvTelTips.setText(StringUtils.getExceptionMessage(e.getMessage()));
                }else {
                    mTipsView.setVisibility(View.VISIBLE);
                    mTvTelTips.setText("网络未连接");
                }
            }

            @Override
            public void onResponse(String response, int id) {
                loadingDialog.dismiss();
                if (response != null && !"[]".equals(response)) {
                    Gson gson = new Gson();
                    TokenModel tokenModel = gson.fromJson(response, TokenModel.class);
                    if (tokenModel == null){
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
        });

    }

    //根据那个平台进行登录
    private void login(Platform platform) {

        String userID = platform.getDb().getUserId();

        if (TextUtils.isEmpty(userID)) {
            //对平台进行验证
            platform.SSOSetting(true);

            //对平台监听
            platform.setPlatformActionListener(this);

            //要功能不要数据   没有用户管理平台
            platform.authorize();

            //要数据  不要功能  有用户管理平台
            //  platform.showUser(null);
        } else {
            //不为空  说明  可以登录   直接跳转到主界面

            //对平台进行验证
            platform.SSOSetting(true);

            //对平台监听
            platform.setPlatformActionListener(this);

            //要功能不要数据   没有用户管理平台
            platform.authorize();

            //要数据  不要功能  有用户管理平台
            //  platform.showUser(null);

//            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
//            startActivity(intent);
        }


    }

    /**
     * 登录成功后回调
     *
     * @param platform 平台
     * @param i        i表示  是那种方式进行登录    1,要数据不要功能  2,要功能不要数据
     * @param hashMap  保存数据
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (i == Platform.ACTION_AUTHORIZING) {//要功能不要数据
            Log.e("AAA", "登录成功");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (i == Platform.ACTION_USER_INFOR) {//要数据不要功能
            Iterator<Map.Entry<String, Object>> iterator = hashMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, Object> map = iterator.next();
                String key = map.getKey();
                Object values = map.getValue();

                //发送到后台....
                Log.e("AAA", "==key==" + key + "==values=" + values);

            }
        }
    }

    /*
     * 登录失败  回调此方法
     * @param platform
     * @param i
     * @param throwable
     */
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.e("AAA", "用户名或者密码错误");
    }

    /**
     * 取消
     *
     * @param platform
     * @param i
     */
    @Override
    public void onCancel(Platform platform, int i) {
        Log.e("AAA", "取消");
    }


}
