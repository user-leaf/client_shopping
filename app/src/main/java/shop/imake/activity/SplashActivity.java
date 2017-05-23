package shop.imake.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import shop.imake.MainActivity;
import shop.imake.R;
import shop.imake.utils.AppUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.SPUtils;

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

        ImageView ivSplash = (ImageView) findViewById(R.id.splash);

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
            Glide.with(this).load(R.drawable.splash).into(ivSplash);
            // 开屏页2秒
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }

    }
}
