package com.bjaiyouyou.thismall.fragment;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.User;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 手机号更换页（新）
 *
 * @author kanbin
 * @date 2016/6/20
 */
public class TelChangeFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = TelChangeFragment.class.getSimpleName();

    // 短信验证码输入框
    private EditText mEtVeriCode;
    // 获取验证码
    private Button mBtnGetCode;
    // 下一步
    private Button mBtnNext;
    // 倒计时
    private CountDownTimer mCountDownTimer;
    // “手机号丢失？”
    private View mTelLoseView;
    // 当前的手机号
    private TextView mTvTelNow;

    public TelChangeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_tel_change, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        setupView();
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
    }

    private void initView() {
        mEtVeriCode = (EditText) layout.findViewById(R.id.tel_change_et_verification_code);
        mBtnGetCode = (Button) layout.findViewById(R.id.tel_change_btn_get_code);
        mBtnNext = (Button) layout.findViewById(R.id.tel_change_btn_next);
        mTelLoseView = layout.findViewById(R.id.tel_change_tv_lose_tel);

        mCountDownTimer = new CountDownTimer(60 * 1000, 1 * 1000 - 10) {

            @Override
            public void onTick(long millisUntilFinished) {
                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
                mBtnGetCode.setText("再次获取(" + ((millisUntilFinished + 15) / 1000) + "秒)");
            }

            @Override
            public void onFinish() {
                mBtnGetCode.setEnabled(true);
                mBtnGetCode.setText("再次获取验证码");
            }
        };

        mTvTelNow = (TextView) layout.findViewById(R.id.tel_change_tel_now);
    }

    private void setupView() {
        // 为输入框设置监听，当输入内容时，下一步按钮可点击
        mEtVeriCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mBtnNext.setEnabled(true);
                } else {
                    mBtnNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBtnGetCode.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mTelLoseView.setOnClickListener(this);
    }

    private void loadData() {
        String userToken = CurrentUserManager.getUserToken();

        String url = ClientAPI.API_POINT + "api/v1/auth/memberDetail"
                +"?token="+userToken;

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        User user = gson.fromJson(response, User.class);
                        mTvTelNow.setText(user.getMember().getPhone());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        IUUTitleBar titleBar = (IUUTitleBar) getActivity().findViewById(R.id.tel_change_new_title_bar);
        switch (v.getId()) {
            case R.id.tel_change_btn_get_code: // 获取验证码
                mBtnGetCode.setEnabled(false);
                mCountDownTimer.start();

                String phone = mTvTelNow.getText().toString();
                String url = ClientAPI.API_POINT + "api/v1/auth/sendVerification"
                        + "?phone="+phone;

                OkHttpUtils.get()
                        .url(url)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.showShort("发送失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                ToastUtils.showShort("发送成功");
                            }
                        });

                break;

            case R.id.tel_change_tv_lose_tel: // 丢失手机号？
                TelChangeLoseFragment telChangeLoseFragment = new TelChangeLoseFragment();
                transaction.replace(R.id.tel_change_new_container, telChangeLoseFragment, telChangeLoseFragment.TAG);
                transaction.commit();
                // 更改标题
                titleBar.setTitle("手机号码丢失");
                break;

            case R.id.tel_change_btn_next: // 下一步
                TelChangingFragment telChangingFragment = new TelChangingFragment();
                transaction.replace(R.id.tel_change_new_container, telChangingFragment, TelChangingFragment.TAG);
                transaction.commit();
                // 更改标题
                titleBar.setTitle("新手机号");
                break;
        }
    }

}
