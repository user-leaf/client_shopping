package shop.imake.activity;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import shop.imake.R;
import shop.imake.utils.LogUtils;
import shop.imake.widget.IUUTitleBar;

/**
 * 网页展示页
 *
 * @author JackB
 * @date 2016/7/12
 */
public class WebShowActivity extends BaseActivity implements View.OnClickListener {

    private static final java.lang.String TAG = WebShowActivity.class.getSimpleName();
    public static final String PARAM_URLPATH = "urlpath";
    public static final String PARAM_PAGE_HIDE = "隐藏标题栏";
    public static final String PARAM_TITLE = "title";

    private int isCustomTitle = -1;

    private IUUTitleBar mTitleBar;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private View mRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_show);

        initView();
        setupView();

        // 如果传过来标题，则用自定义标题，否则用默认标题
        String title = getIntent().getStringExtra(PARAM_TITLE);
        if (PARAM_PAGE_HIDE.equals(title)) {
            mTitleBar.setVisibility(View.GONE);
        } else if (title != null) {
            mTitleBar.setTitle(title);
            isCustomTitle = 1;
        }

        initWebView();
        loadUrl();

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

    /**
     * 启动本页面
     *
     * @param context
     * @param url
     * @param title   传null，则用网页默认标题
     */
    public static void actionStart(Context context, String url, String title) {
        Intent intent = new Intent(context, WebShowActivity.class);
        intent.putExtra(PARAM_URLPATH, url);
        intent.putExtra(PARAM_TITLE, title);
        context.startActivity(intent);
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.web_show_title_bar);
        mProgressBar = (ProgressBar) findViewById(R.id.web_show_progress_bar);
        mWebView = (WebView) findViewById(R.id.web_show_webview);
        mRefreshView = findViewById(R.id.web_show_refresh);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mProgressBar.setMax(100);
        mRefreshView.setOnClickListener(this);
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
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //设置网页在WebView中打开，而不是跳转到浏览器
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mWebView.setVisibility(View.GONE);
                mRefreshView.setVisibility(View.VISIBLE);
                //网络不畅通加载本地html
                mWebView.loadUrl("file:///android_asset/webviewreload.html");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }
        });

        // 弹出窗体的设置
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (isCustomTitle != 1) {
                    mTitleBar.setTitle(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress != 100) {
                    if (mProgressBar.getVisibility() == View.GONE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
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

        // JS调用Native方法
        mWebView.addJavascriptInterface(new JsInterface(), "android");
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

            case R.id.web_show_refresh:
                loadUrl();
                mWebView.setVisibility(View.VISIBLE);
                mRefreshView.setVisibility(View.GONE);
                break;
        }
    }

    private void pauseVideo() {
        mWebView.loadUrl("javascript:pauseVideo()");
    }

    private void goBack() {
        mWebView.loadUrl("javascript:goback()");
    }

    //监听手机键盘的按下事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断当前的网页是否为刚开始加载的网页
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            goBack();
            return true;
        }

        // 按返回键退出前暂停视频
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            pauseVideo();
            finish();
        }

        return false;
    }

    public class JsInterface {
        @JavascriptInterface
        public void backpage() {
            finish();
        }
    }
}
