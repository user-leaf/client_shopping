package com.bjaiyouyou.thismall;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.squareup.leakcanary.LeakCanary;

/**
 * MainApplication
 *
 * @author kanbin
 * @date 2016/5/31
 */
public class MainApplication extends MultiDexApplication {

    public static String TAG = MainApplication.class.getSimpleName();

    public static Context sContext;
    private static MainApplication instance;
    // 是否开启debug模式
    private static boolean isDebug;
    // 数据载体
    private Object data;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
        instance = this;

        isDebug = false;
//        LeakCanary.install(this);

//        ClientAPIHelper.getInstance().setApplicationContext(this);
    }

    public static Context getContext() {
        return sContext;
    }

    public static MainApplication getInstance() {
        return instance;
    }

    public static boolean getIsDebug() {
        return isDebug;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
