package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.TokenModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * 个人中心，绑定手机号页面
 * @author Alice
 *Creare 2016/8/30 15:49
 *
 *
 */
public class MineBingPhoneNumActivity extends BaseActivity implements View.OnClickListener {
    //电话
    private EditText mEtPhone;
    //验证码
    private EditText mEtVerification;
    //邀请码
    private EditText mEtInvitationCode;
    //登录
    private Button mBtLogin;
    //没收到验证码
    private TextView mTvNotHaveVerification;
    //获取验证码
    private TextView mTvGetVerification;
    //计时器
    private Timer mPhoneTimer;
    //手机号
    private String mVerification;
    //验证码
    private String mPnone="";
    //验证码
    private String mInvitationCode;
    //手机号位数
    private int mMaxPhone=11;
    //计时器异步线程
    private TimerTask mPhoneTimeTask;
    //倒数描述
    private int mTimePhone=60;
    private int mMaxTimePhone=60;
    //实时更新UI
    private Handler mHandler=new Handler();
    //获得意图
    private Intent mIntent;
    //获得意图
    private boolean isLogin=true;
    private LinearLayout mLLLogin;
    private RelativeLayout mRlNotLogin;
    private TextView mTVGoTOLogin;
    private TextView mNotLoginTitle;
    private IUUTitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_bing_phone_num);
        mIntent=getIntent();
        isLogin=mIntent.getBooleanExtra("isLogin",true);
        initView();
        setUpView();
    }

    private void setUpView() {
        mTvNotHaveVerification.setOnClickListener(this);
        mBtLogin.setOnClickListener(this);
        mTvGetVerification.setOnClickListener(this);
        mTVGoTOLogin.setOnClickListener(this);
        mTitleBar.setLeftLayoutClickListener(this);
    }

    private void initView() {
        mLLLogin = ((LinearLayout) findViewById(R.id.ll_mine_bind_login));
        mRlNotLogin = ((RelativeLayout) findViewById(R.id.ll_not_login));
        mTVGoTOLogin = ((TextView) findViewById(R.id.tv_goto_login));
        mNotLoginTitle = ((TextView) findViewById(R.id.tv_not_login_title));
        mNotLoginTitle.setText("未登录，请登录后再次绑定");
        if (isLogin){
            mLLLogin.setVisibility(View.VISIBLE);
            mRlNotLogin.setVisibility(View.GONE);
        }else {
            mLLLogin.setVisibility(View.GONE);
            mRlNotLogin.setVisibility(View.VISIBLE);

        }
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_mine_bind_phone));
        mEtPhone = ((EditText) findViewById(R.id.et_bing_phone_num));
        mEtVerification= ((EditText) findViewById(R.id.et_bing_verification));
        mEtInvitationCode = ((EditText) findViewById(R.id.et_bing_invitation_code));
        mBtLogin = ((Button) findViewById(R.id.btn_bing_submit));
        mTvNotHaveVerification = ((TextView) findViewById(R.id.tv_bing_not_have_verification));
        mTvGetVerification = ((TextView) findViewById(R.id.tv_bing_get_verification));
        mTvNotHaveVerification.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            //返回
            case R.id.left_layout:
                //上传数据
                finish();
                break;
            //登录
            case R.id.btn_bing_submit:
                //上传数据
                submit();
                break;

            case R.id.tv_bing_not_have_verification:
                //没收到验证码
                Toast.makeText(getApplicationContext(),"未收到验证码",Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_bing_get_verification:
                //获取验证码
                getVerification();
                break;
            case R.id.tv_goto_login:
                jump(LoginActivity.class,false);
                finish();
                break;

        }
    }

    /**
     * 提交数据
     */
    private void submit() {
        mVerification=mEtVerification.getText().toString().trim();
        mInvitationCode=mEtInvitationCode.getText().toString().trim();
        if (mPnone.length()==11){
            if (mVerification.length()==4){
                //参数合格调用登录接口
                ClientAPI.postLogin(mPnone,mVerification,mInvitationCode ,new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("error--",e.toString());
//                        ToastUtils.showShort("登录失败");
                        UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response != null) {
                            Gson gson = new Gson();
                            TokenModel tokenModel = gson.fromJson(response, TokenModel.class);
                            String token = tokenModel.getToken();
                            if (token != null) {
                                LogUtils.d("保存token：" + token);
                                CurrentUserManager.setUserToken(token);
                                ToastUtils.showShort("登录成功");
                                finish();
                            } else {
                                ToastUtils.showShort("token为空");
                            }
                        }
                    }
                });


            }else {

                Toast.makeText(getApplicationContext(),"请输入正确的4位验证码",Toast.LENGTH_SHORT).show();
                return;
            }
        }else {
            Toast.makeText(getApplicationContext(),"请输入正确的11位手机号",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     *获取验证码
     */
    public void getVerification() {
        mPnone=mEtPhone.getText().toString().trim();
        if (mPnone.length()==mMaxPhone){
            //网络获取验证码
            ClientAPI.getLoginVerification(mPnone, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    LogUtils.e("getUpdateUserVerification","--------------"+e.toString());
//                    Toast.makeText(getApplicationContext(),"验证码发送失败",Toast.LENGTH_SHORT).show();
                    UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
                }
                @Override
                public void onResponse(String response, int id) {
                    Toast.makeText(getApplicationContext(),"验证码已经发送到您的手机上请注意查收",Toast.LENGTH_SHORT).show();
                    initTimer();
                    mPhoneTimer.schedule(mPhoneTimeTask,0L,1000); //延时1000ms后执行，1000ms执行一次
                }
            });

        }else {
            Toast.makeText(getApplicationContext(),"请输入正确的11位手机号",Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * 创建计时器，实现验证码获取倒计时
     */
    private void initTimer() {
        mPhoneTimer = new Timer(true);
        mPhoneTimeTask = new TimerTask(){
            public void run() {
                LogUtils.e("time","mTimeDifference--"+mTimePhone);
                mHandler.post(new Runnable() {
                    //处理倒计时更新页面
                    @Override
                    public void run() {
                        if (mTimePhone>=0){
                            mTvGetVerification.setEnabled(false);
                            mTvGetVerification.setText(mTimePhone+"秒");
                            --mTimePhone;
                        }else {
                            mTvGetVerification.setEnabled(true);
                            mTvGetVerification.setText("获取验证码");
                            mTimePhone=mMaxTimePhone;
                            mPhoneTimer.cancel();
                        }
                    }
                });
            }
        };
    }

}
