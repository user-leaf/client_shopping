package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bigkoo.alertview.OnItemClickListener;
import com.bjaiyouyou.thismall.ActivityCollector;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.Utility;
import com.bjaiyouyou.thismall.widget.LoadingDialog;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * BaseActivity
 *
 * @author JackB
 * @date 2016/5/31
 */

/**
 *
 * @author Alice
 *Creare 2016/6/18 11:42
 * 添加startActivityForResult()的跳转方式
 *
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    public final String TAG = this.getClass().getSimpleName();

    // 是否禁止旋转屏幕
    private boolean isAllowScreenRotate = false;
    // 加载中dialog
    public LoadingDialog loadingDialog;
    private int loadingCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, getClass().getSimpleName());
        ActivityCollector.addActivity(this);
        if (!isAllowScreenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        loadingDialog = LoadingDialog.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
//        RequestManager.cancelAll(this);
        LogUtils.d(TAG, "-->" + this);
        OkHttpUtils.getInstance().cancelTag(this);
        ActivityCollector.removeActivity(this);

        // 销毁前dismissLoadingDialog
        if (loadingCount >= 1) {
            loadingCount = 1;
            dismissLoadingDialog();
        }

        super.onDestroy();
    }

    /**
     * 跳转页面
     * @param cls 要填转到的页面
     * @param isFinish 是否关闭本页
     */
    public void jump(Class<?> cls, boolean isFinish) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        if (isFinish) {
            this.finish();
        }
    }

    /**
     *
     * @param cls 要填转到的页面
     * @param requestCode 请求码
     * @param isFinish 是否关闭本页
     */
    public void jump(Class<?> cls, int requestCode, boolean isFinish) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, requestCode);
        if (isFinish) {
            this.finish();
        }
    }

    /**
     *
     * @author Alice
     *Creare 2016/6/18 14:19
     * @param intent 意图

     * @param requestCode 请求码
     * @param isFinish 是否关闭本页
     */
    public void jump(int requestCode, Intent intent, boolean isFinish) {
        startActivityForResult(intent, requestCode);
        if (isFinish) {
            this.finish();
        }
    }

    /**
     *  带值跳转
     * @author Alice
     *  Creare 2016/6/18 14:19
     * @param intent 意图
     * @param isFinish 是否关闭本页
     */
    public void jump(Intent intent, boolean isFinish) {
        startActivity(intent);
        if (isFinish) {
            this.finish();
        }
    }

    /**
     * [是否允许屏幕旋转]
     *
     * @param isAllowScreenRoate
     */
    public void setScreenRotate(boolean isAllowScreenRoate) {
        this.isAllowScreenRotate = isAllowScreenRoate;
    }


    /** View点击 **/
    public void widgetClick(View v) {

    }

    @Override
    public void onClick(View v) {
        if (!Utility.isFastDoubleClick()) {
            widgetClick(v);
        }
    }


    public void showLoadingDialog() {
        // 如果页面已经销毁就不再显示
        if (!ActivityCollector.sActivities.contains(this)){
            return;
        }

        if (loadingCount <= 0) {
            loadingDialog.show();
        }

        loadingCount++;
    }

    public void dismissLoadingDialog() {
        if (loadingCount <= 1) {
            loadingDialog.dismiss();
        }

        loadingCount--;
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
                ToastUtils.showShort("正在开通中...");
                break;

            case 2: // 环迅支付
                ToastUtils.showShort("正在开通中...");
                break;

            default:
                break;
        }
    }
}
