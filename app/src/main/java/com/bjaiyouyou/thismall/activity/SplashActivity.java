package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import com.bjaiyouyou.thismall.MainActivity;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.utils.AppUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.SPUtils;

public class SplashActivity extends BaseActivity {

    public static final String PARAM_GUIDE = "app_guide";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 是否是首次加载
//        int isFirst = (int) SPUtils.get(this, PARAM_GUIDE, 0);

        // 判断versionName或者版本号，升级之后可以再次出现引导页
        String versionName = AppUtils.getVersionName(this);
        boolean isFirst = !versionName.equals(SPUtils.get(this, PARAM_GUIDE, ""));
        LogUtils.d("isFirst: " + isFirst);
        if (isFirst) { //首次加载
            SPUtils.put(this, PARAM_GUIDE, versionName);
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();

        } else {
            // 开屏页2秒
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }

    }
}
