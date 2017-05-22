package shop.imake.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

/**
 * 直接拨打客服电话
 *
 * @author Alice
 *         Creare 2016/8/23 15:29
 */
public class DialUtils {

    /**
     * 调用系统拨号器拨打电话
     * 客服电话
     */
    public static String CENTER_NUM = "4001599586";

    /**
     * 供货电话
     */
    public static String SUPPLY_PHONE="01053654225";
    //test
//    public static String SUPPLY_PHONE = "18333618642";

    public static  int REQUEST_CODE=110;




    /**
     * 判断是否开启拨打电话权限
     *
     * @param context
     * @return
     */
    public static boolean checkPermission(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.CALL_PHONE", "com.aiyouyou.thismall"));
        return permission;
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param mCenterNum
     */
    public static void callCentre(Context context, String mCenterNum) {
        //方法二，直接拨打电话
        Intent intent = new Intent();
        // 设置要拨打的号码
        intent.setData(Uri.parse("tel:" + mCenterNum));
        // 设置动作,拨号 动作
        intent.setAction(intent.ACTION_CALL);
        // 跳转到拨号界面
        try {
            // 跳转到拨号界面
            context.startActivity(intent);
        } catch (SecurityException e) {
            Toast.makeText(context, "请到设置里检查应用权限设置或硬件完整性", Toast.LENGTH_SHORT).show();
        }
//        context.startActivity(intent);

//        //检查权限是否开启
//        if (checkPermission(context)){
//            //方法二，直接拨打电话
//            Intent intent = new Intent();
//            // 设置要拨打的号码
//            intent.setData(Uri.parse("tel:" + mCenterNum));
//            // 设置动作,拨号 动作
//            intent.setAction(intent.ACTION_CALL);
//            // 跳转到拨号界面
//            context.startActivity(intent);
//        }else {
//            //开启权限设置页面
////            Intent intent = new Intent("/");
////            ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.RadioInfo");
////            intent.setComponent(cm);
////            intent.setAction("android.intent.action.VIEW");
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            context.startActivity(intent);
//            Toast.makeText(context,"请到设置里检查应用权限设置或硬件完整性",Toast.LENGTH_SHORT).show();
//        }
    }
}
