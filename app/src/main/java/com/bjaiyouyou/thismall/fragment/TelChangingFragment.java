package com.bjaiyouyou.thismall.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bjaiyouyou.thismall.R;

/**
 * 新手机号页（新）
 *
 * @author kanbin
 * @date 2016/6/20
 */
public class TelChangingFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = TelChangingFragment.class.getSimpleName();

    // 新手机号输入框
    private EditText mEtNewTel;
    // 填写验证码输入框
    private EditText mEtCode;
    // 获取验证码
    private Button mBtnGetCode;
    // 倒计时
    private CountDownTimer mCountDownTimer;
    // 下一步
    private Button mBtnNext;

    public TelChangingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_tel_changing, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setupView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
    }

    private void initView() {
        mEtNewTel = (EditText) layout.findViewById(R.id.tel_changing_et_new_tel);
        mEtCode = (EditText) layout.findViewById(R.id.tel_changing_et_code);
        mBtnGetCode = (Button) layout.findViewById(R.id.tel_changing_btn_get_code);
        mBtnNext = (Button) layout.findViewById(R.id.tel_changing_btn_next);

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
    }

    private void setupView() {
        mBtnGetCode.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tel_changing_btn_get_code: // 获取验证码
                // 开始倒计时
                mCountDownTimer.start();
                // 设置按钮不能点击
                mBtnGetCode.setEnabled(false);
                break;

            case R.id.tel_changing_btn_next: // 下一步
                // 修改成功
                View layout = LayoutInflater.from(getContext()).inflate(R.layout.tel_changing_dialog, (ViewGroup) getActivity().findViewById(R.id.tel_changing_dialog));
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder .setView(layout)
                        .setPositiveButton("返回个人中心", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("返回首页", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getActivity().finish();
//                                MainActivity.instance.onTabClicked(MainActivity.instance.findViewById(R.id.controller_zero));
                            }
                        });
                // 显示对话框
                builder.create().show();
                break;
        }
    }
}
