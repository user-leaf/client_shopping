package shop.imake.browserengine;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import shop.imake.utils.LogUtils;

/**
 * Created by Administrator on 2016/6/4.
 */

/**
 * WebView 加载网页及资源时，会自动调用这个WebViewClient当中的回调方法
 * @author JackB
 * @date 2016/6/4
 */
public class AppWebViewClient extends WebViewClient {

    private static final String TAG = AppWebViewClient.class.getSimpleName();

    /**
     * ！！！当WebView内部超链接点击或者网页跳转的时候，会先调这个方法
     * 给应用程序一个拦截网址请求的机会。
     * 当调用 loadUrl(String url) 反而不会调用这个方法
     * @param view WebView
     * @param url 需要加载的网址
     * @return 如果返回true，代表应用程序自己进行网页的加载WebView，就不继续加载
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtils.d(TAG, "shouldOverrideUrlLoading: "+url);

        return super.shouldOverrideUrlLoading(view, url);
    }

    /**
     * 网页开始加载的时候，自动进行回调
     * @param view WebView
     * @param url String网址
     * @param favicon 网站上面标题部分的小图标
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        LogUtils.d(TAG, "onPageStarted: "+url);
        // TODO: 2016/6/4 显示加载中

    }

    /**
     * 网页加载完成
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        LogUtils.d(TAG, "onPageFinished: "+url);
        // TODO: 2016/6/4 隐藏加载中

    }
}
