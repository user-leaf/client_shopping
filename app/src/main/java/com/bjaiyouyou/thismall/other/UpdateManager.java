package com.bjaiyouyou.thismall.other;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;

import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.UpdateInfo2;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

/**
 * 升级管理类
 * <p/>
 * User: kanbin
 * Date: 2016/9/10
 */
public class UpdateManager {
    private static final java.lang.String TAG = UpdateManager.class.getSimpleName();

    private ProgressDialog mProgressDialog;
    private Handler mHandler;
    private Context mContext;
    //    private UpdateInfo updateInfo;
    private UpdateInfo2.PackageBean mUpdateInfo2;
    private final String APK_NAME = "aiyouyoumall.apk";

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    /**
     * 获取升级信息
     *
     * @return
     */
//    public UpdateInfo getUpdateInfo() {
//
////        UpdateInfo updateInfo = new UpdateInfo();
////        updateInfo.setVersion("2");
////        updateInfo.setQ(false);
////        updateInfo.setDescription("abcd\nefgh");
//////        updateInfo.setUrl("http://apk.hiapk.com/appdown/com.xunmeng.pinduoduo");
////        updateInfo.setUrl("http://www.apk3.com/uploads/soft/guiguangbao/qmqz.apk");
//////        updateInfo.setUrl("http://www.apk3.com/uploads/soft/201603/com.rovio.popcorn_1.7.5.apk");
////        this.updateInfo=updateInfo;
//        return updateInfo;
//    }
    public UpdateInfo2.PackageBean getUpdateInfo() {
        return mUpdateInfo2;
    }

    public void checkVersion(final CheckVersionCallback callback) {
        OkHttpUtils.get()
                .url(ClientAPI.API_POINT + "api/v1/package/lastVersion/0")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showException(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!TextUtils.isEmpty(response) && !"[]".equals(response)) {
                            Gson gson = new Gson();
                            try {
                                UpdateInfo2 updateInfo2 = gson.fromJson(response, UpdateInfo2.class);

                                if (updateInfo2 != null) {
                                    mUpdateInfo2 = updateInfo2.getPackageX();

                                    if (mUpdateInfo2 != null) {
                                        callback.run();
                                    }
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    /**
     * 是否需要更新
     *
     * @return
     */
    public boolean hasUpdate() {
        String new_version = mUpdateInfo2.getPackage_version(); // 最新版本的版本号
        //获取当前版本号
        String now_version = "";
        int now_version_code = 0;
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    mContext.getPackageName(), 0);
            now_version = packageInfo.versionName;
            now_version_code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        int new_version_code = Integer.parseInt(new_version);

        LogUtils.d(TAG, "now_version=" + now_version + ",new_version=" + new_version);
        LogUtils.d(TAG, "now_version_code=" + now_version_code + ",new_version_code=" + new_version_code);

        if (now_version_code < new_version_code) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 下载文件
     *
     * @param urlpath
     * @param pDialog
     * @param h
     */
    public void downloadFile(final String urlpath, final ProgressDialog pDialog, Handler h) {
        mProgressDialog = pDialog;
        mHandler = h;
        mProgressDialog.setMax(100);

        OkHttpUtils.get()
                .url(urlpath)
                .tag(mContext)
                .build()
                .execute(new FileCallBack(Environment
                        .getExternalStorageDirectory().getAbsolutePath(), APK_NAME) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showException(e);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        down(response);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        mProgressDialog.setProgress((int) (100 * progress));
                    }
                });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
////                ByteArrayOutputStream baos = null;
//                FileOutputStream fileOutputStream = null;
//                try {
////                    baos = new ByteArrayOutputStream();
//
//                    File file = new File(
//                            Environment.getExternalStorageDirectory(),
//                            APK_NAME);// 有两处名字mall.apk，本页底部还有一处
//                    fileOutputStream = new FileOutputStream(file);
//
//                    //1创建URL
//                    URL url = new URL(urlpath);
//                    //2调用openConnection()
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    //3设置请求参数
//                    connection.setConnectTimeout(10000);
//                    connection.setReadTimeout(10000);
//                    // 解决长度为-1问题，http://blog.csdn.net/buaaroid/article/details/50525234
//                    connection.setRequestProperty("Accept-Encoding", "identity");
//                    //4建立连接
//                    connection.connect();
//                    //5处理响应结果
//                    if (connection.getResponseCode() == 200) {
//                        InputStream is = connection.getInputStream();
//
//                        // 获取文件大小，并设置进度条总长度
//                        int fileLength = connection.getContentLength();
//                        LogUtils.d(TAG, "fileLength : " + fileLength);
//                        mProgressDialog.setMax(100);
//
//                        byte[] buf = new byte[1024 * 4];
//                        int len = 0;
//                        int process = 0;
//                        while ((len = is.read(buf)) != -1) {
////                            baos.write(buf, 0, len);
//                            fileOutputStream.write(buf, 0, len);
//                            process += len;
//                            mProgressDialog.setProgress(process * 100 / fileLength);       //这里就是关键的实时更新进度了！
//                        }
//                        is.close();
//                        down();
//                    }
////                    return baos.toByteArray();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
////                return null;
//            }
//        }).start();
    }

    private void down(final File file) {
        mHandler.post(new Runnable() {
            public void run() {
                mProgressDialog.cancel();
                install(file);
            }
        });
    }

    private void install(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        /**
         * http://bbs.csdn.net/topics/390837112/
         * http://blog.csdn.net/jjmm2009/article/details/47110475
         * 解决升级安装完成后没有“完成，打开”的提示问题
         */
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);

    }

    public interface CheckVersionCallback {
        public void run();
    }
}
