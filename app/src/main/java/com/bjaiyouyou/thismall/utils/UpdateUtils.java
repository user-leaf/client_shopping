package com.bjaiyouyou.thismall.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bjaiyouyou.thismall.ActivityCollector;
import com.bjaiyouyou.thismall.MainActivity;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.UpdateInfo2;
import com.bjaiyouyou.thismall.other.UpdateManager;

/**
 * 升级工具类
 * <p>
 * User: JackB
 * Date: 2016/9/10
 */
public class UpdateUtils {
    private static final java.lang.String TAG = UpdateUtils.class.getSimpleName();

    public static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    public static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    // 更新版本要用到的一些信息
//    private static UpdateInfo info;
    private UpdateInfo2.PackageBean info2;
    private ProgressDialog mProgressDialog;
    private UpdateManager mUpdateManager;
    private Context mContext;
    public static String sDownloadUrl = "";
    private View mClickView;

    public void checkUpdate(final Context context, View updateView) {
        mContext = context;
        mClickView = updateView;
//        Toast.makeText(context, "正在检查版本更新..", Toast.LENGTH_SHORT).show();
        // 自动检查有没有新版本 如果有新版本就提示更新
        new Thread() {
            public void run() {
                try {
                    mUpdateManager = new UpdateManager(context);
//                    info = mUpdateManager.getUpdateInfo();

                    mUpdateManager.checkVersion(new UpdateManager.CheckVersionCallback() {
                        @Override
                        public void run() {
                            info2 = mUpdateManager.getUpdateInfo();
                            if (info2 != null) {
                                sDownloadUrl = info2.getPackage_link();
                                mHandler.sendEmptyMessage(0);
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (mClickView != null) {
                mClickView.setOnClickListener((View.OnClickListener) mContext);
            }
            // MainActivity中检查更新
            if (mContext instanceof MainActivity) {  // 如果是MainActivity。加上了对是否忽略版本的判断。
                if (mUpdateManager.hasUpdate()) {

                    // 从本地取出要忽略的版本号
                    String ignore_version_no_str = (String) SPUtils.get(mContext, "UPDATE_IGNORE_VERSION_NO", "-1");
                    int ignore_version_no = Integer.parseInt(ignore_version_no_str);
                    // 是否忽略本次升级
                    boolean isIgnore = (ignore_version_no == Integer.parseInt(info2.getPackage_version()) ? true : false);

                    if (isIgnore) {  // 如果忽略
                        if ((info2.getIs_upgrade() == 1 ? true : false)) {
                            // 如果是强制升级，就算忽略也显示dialog，让用户去选择
                            showUpdateDialog2(mContext);
                        }
                        return;
                    } else { // 不忽略
                        showUpdateDialog2(mContext);
                    }

                }

            } else { // 非MainActivity

                // 如果有更新就提示
                if (mUpdateManager.hasUpdate()) {
                    if (!(info2.getIs_upgrade() == 1 ? true : false)) {
                    } else { // 强制升级
                    }
                    showUpdateDialog2(mContext);

                } else {
                    // 最新版本
                    // 如果是MainActivity中的检查升级则不提示
                    if (mContext instanceof MainActivity) {

                    } else {
                        ToastUtils.showShort("已经是最新版本");
                    }

                }
            }
        }

    };

    //显示是否要更新的对话框
    private void showUpdateDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_info);
//        builder.setTitle("请升级至版本" + info2.getPackage_version());
        builder.setTitle("发现新版本");
        builder.setMessage(info2.getPackage_describe());
        builder.setCancelable(false);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(info2.getPackage_link(), mContext);
                } else {
                    Toast.makeText(context, "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNeutralButton("忽略此版本", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 把版本号保存到本地
                SPUtils.put(mContext, "UPDATE_IGNORE_VERSION_NO", info2.getPackage_version());
                // 如果本次升级是强制升级，那么退出程序
                if (info2.getIs_upgrade() == 1 ? true : false) {
                    ActivityCollector.finishAll();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (info2.getIs_upgrade() == 1 ? true : false) {
//                    System.exit(0);
                    ActivityCollector.finishAll();
                }
            }
        });
        builder.create().show();
    }

    // 升级对话框
    private void showUpdateDialog2(final Context context) {
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.layout_dialog_update, false)
                .build();
        View updateView = dialog.getCustomView();
        dialog.setCancelable(false);
        ImageView ivBg = (ImageView) updateView.findViewById(R.id.dialog_update_iv_bg);
        TextView tvContent = (TextView) updateView.findViewById(R.id.tv_dialog_update_content);
        TextView tvIgnore = (TextView) updateView.findViewById(R.id.tv_dialog_update_ignore);
        TextView tvUpdate = (TextView) updateView.findViewById(R.id.tv_dialog_update_now);
        ImageView tvCancel = (ImageView) updateView.findViewById(R.id.tv_dialog_update_cancel);
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvContent.setText(info2.getPackage_describe());
        // 忽略
        tvIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 把版本号保存到本地
                SPUtils.put(mContext, "UPDATE_IGNORE_VERSION_NO", info2.getPackage_version());
                // 如果本次升级是强制升级，那么退出程序
                if (info2.getIs_upgrade() == 1 ? true : false) {
                    ActivityCollector.finishAll();
                }
                dialog.dismiss();
            }
        });
        // 更新
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                                context.getString(R.string.mis_permission_rationale),
                                REQUEST_STORAGE_READ_ACCESS_PERMISSION);
                    } else {
                        downFile(info2.getPackage_link(), mContext);
                    }
                } else {
                    Toast.makeText(context, "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        // 取消
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info2.getIs_upgrade() == 1 ? true : false) {
//                    System.exit(0);
                    ActivityCollector.finishAll();
                }
                dialog.dismiss();
            }
        });

        // 修改窗口大小
        // http://blog.csdn.net/misly_vinky/article/details/19109517
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        BitmapDrawable bd = (BitmapDrawable) ivBg.getBackground();
        Bitmap bitmap = bd.getBitmap();
        int width = bitmap.getWidth();
        lp.width = width;

        dialogWindow.setAttributes(lp);

        dialog.show();
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permission)) {
            new android.support.v7.app.AlertDialog.Builder(mContext)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton("好", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton("拒绝", null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, requestCode);
        }
    }

    // 放在了调用检查升级方法的类中MainActivity  SettingsActivity
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION){
//            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                pickImage();
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    public void downFile(final String url, Context context) {
        mProgressDialog = new ProgressDialog(context);    //进度条，在下载的时候实时更新进度，提高用户友好度
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage("请等待...");
        mProgressDialog.setProgress(0);
        mProgressDialog.show();
        mUpdateManager.downloadFile(url, mProgressDialog, mHandler);
    }


}
