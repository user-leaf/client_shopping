package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bjaiyouyou.thismall.ActivityCollector;
import com.bjaiyouyou.thismall.client.RequestManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.Utility;
import com.bjaiyouyou.thismall.widget.LoadingDialog;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * BaseActivity
 * @author kanbin
 * @date 2016/5/31
 */
/**
 *
 * @author QuXinhang
 *Creare 2016/6/18 11:42
 * 添加startActivityForResult()的跳转方式
 *
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    /** 是否禁止旋转屏幕 **/
    private boolean isAllowScreenRotate = false;
    /** 日志输出标志 **/
    public final String TAG = this.getClass().getSimpleName();
    // 加载中dialog
    public LoadingDialog loadingDialog;
    private long lastClick = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d(TAG, getClass().getSimpleName());
        ActivityCollector.addActivity(this);
        if (!isAllowScreenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        loadingDialog = new LoadingDialog(this);
    }

    /**
     * 跳转页面
     * @param cls 要填转到的页面
     * @param isFinish 是否关闭本页
     */
    public void jump(Class<?> cls,boolean isFinish){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
        if (isFinish){
            this.finish();
        }
    }

    /**
     *
     * @param cls 要填转到的页面
     * @param requestCode 请求码
     * @param isFinish 是否关闭本页
     */
    public void jump(Class<?> cls,int requestCode,boolean isFinish){
        Intent intent = new Intent(this,cls);
        startActivityForResult(intent,requestCode);
        if (isFinish){
            this.finish();
        }
    }
    /**
     *
     * @author QuXinhang
     *Creare 2016/6/18 14:19
     * @param intent 意图

     * @param requestCode 请求码
     * @param isFinish 是否关闭本页
     */
    public void jump( int requestCode,Intent intent, boolean isFinish){
        startActivityForResult(intent,requestCode);
        if (isFinish){
            this.finish();
        }
    }
    /**
     *  带值跳转
     * @author QuXinhang
     *  Creare 2016/6/18 14:19
     * @param intent 意图
     * @param isFinish 是否关闭本页
     */
    public void jump( Intent intent, boolean isFinish){
        startActivity(intent);
        if (isFinish){
            this.finish();
        }
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
        RequestManager.cancelAll(this);
        LogUtils.d(TAG, "-->" + this);
        OkHttpUtils.getInstance().cancelTag(this);
        ActivityCollector.removeActivity(this);
        super.onDestroy();
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
    public void widgetClick(View v){

    }

    @Override
    public void onClick(View v) {

        // 返回键也失效了
        // 一些页面可能没法点进去
//        if (v.getId() != R.id.left_layout && !NetStateUtils.isNetworkAvailable(this)){
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
}
