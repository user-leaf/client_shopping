package shop.imake.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import shop.imake.model.TelephoneModel;

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
//    public static String CENTER_NUM = "4001599586";
//    public static String CENTER_NUM = "01053358674";
//
//    public static String CENTER_NUM1 = "01053358654";
//    public static String CENTER_NUM2 = "01053358674";


    /**
     * 供货电话
     */
//    public static String SUPPLY_PHONE = "01053654225";
    //test
//    public static String SUPPLY_PHONE = "18333618642";

    public static int REQUEST_CODE = 110;

    public static String PHONE_JSON_KEY = "telephone_jsonString_key";//用于标识存储拨打电话数据
    public static String PHONE_GET_SAFE_CODE_KEY = "get_safe_code_key";//用于标识找回安全码电话数据


    public static int SERVER_PHONE_TYPE = 1;//客服电话类型
    public static int SUPPLY_PHONE_TYPE = 2;//供货电话类型

    public static String SERVER_TITLE = "爱每刻客服为您服务";//电话列表标题


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

    public static String[] getPhoneNum(Context context, int phoneType) {
        //获取缓存json数据
        String jsonString = ACache.get(context).getAsString(DialUtils.PHONE_JSON_KEY);
        //存储符合要求的电话数据
        List<String> listData=new ArrayList<>();

        if (!TextUtils.isEmpty(jsonString)) {
            //获取数据对象
            TelephoneModel telephoneModel = new Gson().fromJson(jsonString,TelephoneModel.class);
            List<TelephoneModel.PhoneBean> list = telephoneModel.getPhone();
            //遍历数据获得满足要求的数据
            for (int i=0;i< list.size();i++) {
                TelephoneModel.PhoneBean phoneBean=list.get(i);
                if (phoneBean != null && phoneBean.getType() == phoneType) {
                    listData.add(phoneBean.getVal());
                }
            }
        }
        //将集合数据赋值到数组用于掉起统一的选择框
        String[] phones =new String[listData.size()];
        for (int i=0;i<listData.size();i++){
            phones[i] =listData.get(i);
            if (phoneType==SERVER_PHONE_TYPE&&i==0){
                //用于找回安全码
                ACache.get(context).put(PHONE_GET_SAFE_CODE_KEY,listData.get(i));
            }
        }
        return phones;
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param mCenterNum
     */
    public static void callCentre(Context context, String mCenterNum) {

        //调用系统拨号器拨打电话
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

    public static void callSafeCodeForget(Context context){
        String telephone=ACache.get(context).getAsString(DialUtils.PHONE_GET_SAFE_CODE_KEY);
        if (TextUtils.isEmpty(telephone)){
            ToastUtils.showShort("咨询电话加载中...");
            return;
        }

        callCentre(context,telephone);
    }
}
