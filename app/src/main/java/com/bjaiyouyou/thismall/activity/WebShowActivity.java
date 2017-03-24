package com.bjaiyouyou.thismall.activity;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

/**
 * 网页展示页
 *
 * @author kanbin
 * @date 2016/7/12
 */
public class WebShowActivity extends BaseActivity implements View.OnClickListener {

    private static final java.lang.String TAG = WebShowActivity.class.getSimpleName();
    public static final String PARAM_URLPATH = "urlpath";

    private IUUTitleBar mTitleBar;
    private ProgressBar mProgressBar;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_show);

        initView();
        setupView();

        initWebView();
        loadUrl();

    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.web_show_title_bar);
        mProgressBar = (ProgressBar) findViewById(R.id.web_show_progress_bar);
        mWebView = (WebView) findViewById(R.id.web_show_webview);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mProgressBar.setMax(100);
    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        //启用js支持
        webSettings.setJavaScriptEnabled(true);
        //js可以有弹出窗体
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);

        // 开启DOM缓存，开启LocalStorage存储（HTML5的本地存储方式）
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath(WebShowActivity.this.getApplicationContext().getCacheDir().getAbsolutePath());

        // 设置Android5.0以上版本支持同时加载Https和Http混合模式
        if(Build.VERSION.SDK_INT >= 21){
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //设置网页在WebView中打开，而不是跳转到浏览器
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // 弹出窗体的设置
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitleBar.setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress != 100) {
                    mProgressBar.setProgress(newProgress);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            //弹出一个对话框
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            //带按钮的对话框
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            //带输入的对话框
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

        });

    }

    private void loadUrl() {
        String urlpath = getIntent().getStringExtra(PARAM_URLPATH);
        // 载入网页
        if (urlpath != null) {
            mWebView.loadUrl(urlpath);
        }
        LogUtils.d(TAG, "urlpath:" + urlpath);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                pauseVideo();
                finish();
                break;
        }
    }

    private void pauseVideo() {
        mWebView.loadUrl("javascript:pauseVideo()");
    }

    //监听手机键盘的按下事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断当前的网页是否为刚开始加载的网页
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        // 按返回键退出前暂停视频
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            pauseVideo();
            finish();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.destroy();
        }

        super.onDestroy();
    }

}
