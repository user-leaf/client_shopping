package shop.imake.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.xiaoneng.uiapi.Ntalker;
import shop.imake.R;
import shop.imake.fragment.TaskPage;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DataCleanManager;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UpdateUtils;
import com.bumptech.glide.Glide;

/**
 * 设置
 *
 * @author JackB
 * @date 2016/6/6
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    // 缓存大小
    private TextView mTvCache;
    // 清除缓存
    private View mCleanCacheView;
    // 返回
    private View mBackView;
    // 退出登录
    private Button mBtnLogout;
    // 检查升级
    private View mUpdateView;
    // 升级
    private UpdateUtils mUpdateUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mUpdateUtils = new UpdateUtils();

        initView();
        setupView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 计算缓存大小
        try {
            String totalCacheSize = DataCleanManager.getTotalCacheSize(this);
            mTvCache.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        mBackView = findViewById(R.id.left_layout);
        mCleanCacheView = findViewById(R.id.settings_rl_cache);
        mTvCache = (TextView) findViewById(R.id.settings_tv_cache);
        mBtnLogout = (Button) findViewById(R.id.settings_btn_logout);
        mUpdateView = findViewById(R.id.settings_update);
    }

    private void setupView() {
        mBackView.setOnClickListener(this);
        mCleanCacheView.setOnClickListener(this);
        mBtnLogout.setOnClickListener(this);
        mUpdateView.setOnClickListener(this);

        if (TextUtils.isEmpty(CurrentUserManager.getUserToken())){
            mBtnLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);

        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;

            case R.id.settings_rl_cache: // 清除缓存，并显示清扫后的缓存大小
                DataCleanManager.clearAllCache(this);

                // 执行清理Glide磁盘缓存之后，图片都加载不出来了
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Glide.get(SettingsActivity.this).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
//                    }
//                }).start();

                Glide.get(this).clearMemory();//清理内存缓存  可以在UI主线程中进行
                try {
                    String totalCacheSize = DataCleanManager.getTotalCacheSize(this);
                    mTvCache.setText(totalCacheSize);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.settings_update: // 检查升级
                if (!NetStateUtils.isNetworkAvailable(this)) {
                    ToastUtils.showShort("网络不可用");
                    return;
                }
                mUpdateView.setOnClickListener(null);

                mUpdateUtils.checkUpdate(this, mUpdateView);
                Toast.makeText(this, "正在检查版本更新..", Toast.LENGTH_SHORT).show();
                break;

            case R.id.settings_btn_logout: // 退出账号
                if (!TextUtils.isEmpty(CurrentUserManager.getUserToken())){
                    CurrentUserManager.clearUserToken();
                    // 任务页数据重置
                    Intent taskIntent = new Intent(TaskPage.INTENT_BROADCAST);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(taskIntent);
                    // 小能退出登录
                    Ntalker.getBaseInstance().logout();
                    CurrentUserManager.clearCurrentUser();
                    finish();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == UpdateUtils.REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mUpdateUtils.downFile(UpdateUtils.sDownloadUrl, this);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
