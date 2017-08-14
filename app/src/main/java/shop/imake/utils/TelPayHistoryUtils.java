package shop.imake.utils;

import android.content.Context;
import android.text.TextUtils;

import shop.imake.user.CurrentUserManager;

/**
 * 手机充值历史充值处理工具类
 */

public class TelPayHistoryUtils {
//    public static String TEL_PAY_HISTORY_LIST_KEY = "tel_pay_history_list_key";
    //test

//    public static String TEL_PAY_HISTORY_LIST_KEY = "";
    public static String TEL_PAY_HISTORY_LIST_KEY = CurrentUserManager.getCurrentUser().getPhone();


    public static void putHistoryPay(Context context, String JsonString) {

        ACache.get(context).put(TEL_PAY_HISTORY_LIST_KEY, JsonString);

    }

    public static boolean isHaveHistory(Context context) {

        return TextUtils.isEmpty(getHistoryPay(context)) ? false : true;

    }

    public static String getHistoryPay(Context context) {

        return ACache.get(context).getAsString(TEL_PAY_HISTORY_LIST_KEY);

    }

    public static void clearHistoryPay(Context context) {

        ACache.get(context).remove(TEL_PAY_HISTORY_LIST_KEY);

    }

}
