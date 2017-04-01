package com.bjaiyouyou.thismall.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bigkoo.alertview.OnItemClickListener;
import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.client.RequestManager;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.Utility;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.bjaiyouyou.thismall.widget.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;

public class BaseFragment extends Fragment implements View.OnClickListener, OnItemClickListener {
    protected IUUTitleBar titleBar;
    protected InputMethodManager inputMethodManager;
    protected View layout;

    public LoadingDialog loadingDialog;
    private int loadingCount = 0;
    private long lastClick = 0;

    // from https://github.com/afollestad/material-dialogs
    // 这个demo里用的
//    private Toast mToast;
//    public void showToast(String message) {
//        if (mToast != null) {
//            mToast.cancel();
//            mToast = null;
//        }
//        mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
//        mToast.show();
//    }

    //====[解决Toast重复显示]begin====================>>
    private Toast mToast;

    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        }
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public void onBackPressed() {
        cancelToast();
//        super.onBackPressed();
        getActivity().onBackPressed();
    }
    //====[解决Toast重复显示]end=========================<<

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        titleBar = (IUUTitleBar) getView().findViewById(R.id.title_bar);
        loadingDialog = LoadingDialog.getInstance(getContext());
//        initView();
//        setUpView();
    }

    /**
     * 显示标题栏
     */
    public void showTitleBar() {
        if (titleBar != null) {
            titleBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏标题栏
     */
    public void hideTitleBar() {
        if (titleBar != null) {
            titleBar.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏键盘
     */
    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    /**
//     * 初始化控件
//     */
//    protected abstract void initView();
//
//    /**
//     * 设置属性，监听等
//     */
//    protected abstract void setUpView();


    /**
     * View点击
     **/
    public void widgetClick(View v) {

    }

    @Override
    public void onClick(View v) {

        // 返回键会失效
        // 一些页面可能没法点进去
        // 如果联网但是网络不通...
//        if (!NetStateUtils.isNetworkAvailable(getContext())) {
//            ToastUtils.showShort("网络不可用");
//            return;
//        }

        if (!Utility.isFastDoubleClick()) {
            widgetClick(v);
        }

    }

//    /**
//     * [防止快速点击]
//     *
//     * @return
//     */
//    private boolean fastClick() {
//        long currentTime = System.currentTimeMillis();
//        if (currentTime - lastClick <= 1000) {
//            return true;
//        }
//        lastClick = currentTime;
//        return false;
//    }


    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getContext(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getContext(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void showLoadingDialog() {
        if (loadingCount <= 0) {
            if (loadingDialog != null) {
                loadingDialog.show();

            }
        }

        loadingCount++;
    }

    public void dismissLoadingDialog() {
        if (loadingCount <= 1) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();

            }
        }

        loadingCount--;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RequestManager.cancelAll(this);
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public void onItemClick(Object o, int position) {

        if (position < 0){ // 取消
            return;
        }

        switch (position) {
            case 0: // 支付宝支付
                break;

            case 1: // 余额支付
                ToastUtils.showShort("正在开通中");
                break;

            case 2: // 环迅支付
                ToastUtils.showShort("正在开通中");
                break;

            default:
                break;
        }
    }
}
