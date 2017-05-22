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

import shop.imake.R;
import shop.imake.fragment.TaskPage;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DataCleanManager;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UpdateUtils;
import com.bumptech.glide.Glide;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 设置
 *
 * @author JackB
 * @date 2016/6/6
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener {

//    public static SettingsActivity instance;

    // 缓存大小
    private TextView mTvCache;
    // 清除缓存
    private View mCleanCacheView;
    // 分享
    private View mShareView;
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

        ShareSDK.initSDK(this);

//        instance = this;
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
        mShareView = findViewById(R.id.settings_share);
        mBtnLogout = (Button) findViewById(R.id.settings_btn_logout);
        mUpdateView = findViewById(R.id.settings_update);
    }

    private void setupView() {
        mBackView.setOnClickListener(this);
        mCleanCacheView.setOnClickListener(this);
        mShareView.setOnClickListener(this);
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

            case R.id.settings_share: // 分享
                showShare();
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

    /**
     * 分享
     */
    private void showShare() {
//        ShareSDK.initSDK(this); // 添加到了 onCreate()
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("我是分享小达人");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl("https://www.baidu.com/img/bd_logo1.png");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

}
