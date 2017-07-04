package shop.imake;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import cn.xiaoneng.activity.ChatActivity;
import cn.xiaoneng.uiapi.EPlusFunctionType;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.xpush.XPush;
import shop.imake.BuildConfig;

import shop.imake.client.ClientApiHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.squareup.leakcanary.LeakCanary;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.InputStream;
import java.security.GeneralSecurityException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * MainApplication
 *
 * @author JackB
 * @date 2016/5/31
 */
public class MainApplication extends MultiDexApplication {

    public static String TAG = MainApplication.class.getSimpleName();

    public static Context sContext;
    private static MainApplication instance;
    // 是否开启debug模式
    public static final boolean DEBUG = BuildConfig.DEBUG;
    // 数据载体
    private Object data;

    // 小能客服
    public static String siteid = "kf_9003_1499140862639";// 示例kf_9979,kf_8002,kf_3004,zf_1000,yy_1000,kf_9979
    public static String sdkkey = "AC7B45BC-AF77-4821-9375-6F5890A69358";// 示例FB7677EF-00AC-169D-1CAD-DEDA35F9C07B,w2222

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
        instance = this;

        LeakCanary.install(this);

        ClientApiHelper.getInstance().setApplicationContext(this);

        initHttps();

        JPushInterface.setDebugMode(DEBUG);
        JPushInterface.init(this);

        // 小能在线客服
        Ntalker.getBaseInstance().enableDebug(DEBUG);
        Ntalker.getBaseInstance().initSDK(getApplicationContext(), siteid, sdkkey);

        Ntalker.getExtendInstance().extensionArea().removeAll();//首先先调用删除所有功能的接口，然后依次添加功能
        Ntalker.getExtendInstance().extensionArea().addPlusFunction(EPlusFunctionType.DEFAULT_PICTRUE);
        Ntalker.getExtendInstance().extensionArea().addPlusFunction(EPlusFunctionType.DEFAULT_CAMERA);
        Ntalker.getExtendInstance().extensionArea().addPlusFunction(EPlusFunctionType.DEFAULT_VIDEO);
        Ntalker.getExtendInstance().extensionArea().addPlusFunction(EPlusFunctionType.DEFAULT_EVALUATE);
        setXPush();
    }

    public static Context getContext() {
        return sContext;
    }

    public static MainApplication getInstance() {
        return instance;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private void initHttps() {
        // 设置可访问所有的https网站
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

        //让Glide能用HTTPS
        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(getOkHttpClient()));

    }

    private OkHttpClient getOkHttpClient() {
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory = null;

        trustManager = new X509TrustManager() {

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        };

        try {
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new X509TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
        return client;
    }

    /**
     * 小能推送
     */
    private void setXPush() {
        XPush.setNotificationClickToActivity(getApplicationContext(), ChatActivity.class);
        XPush.setNotificationShowIconId(getApplicationContext(), 0);
        XPush.setNotificationShowTitleHead(getApplicationContext(), null);//getResources().getString(R.string.app_name)

//		XPush.enableHuaweiPush(getApplicationContext(), true);
//		XPush.setHuaweiPushParams(getApplicationContext(), "10556196");
//		XPush.enableXiaomiPush(getApplicationContext(), true);
//		XPush.setXiaomiPushParams(getApplicationContext(), getPackageName(), "2882303761517480753", "5641748066753");

    }
}
