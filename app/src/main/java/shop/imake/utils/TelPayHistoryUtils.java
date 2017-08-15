package shop.imake.utils;

import android.content.Context;
import android.text.TextUtils;

/**
 * 手机充值历史充值处理工具类
 */

public class TelPayHistoryUtils {
//    public static String TEL_PAY_HISTORY_LIST_KEY = "tel_pay_history_list_key";

    //test
//    public static String TEL_PAY_HISTORY_LIST_KEY = "18333618642";
//    public static String TEL_PAY_HISTORY_LIST_KEY = CurrentUserManager.getCurrentUser().getPhone();


    public static void putHistoryPay(Context context, String JsonString, String telNumSelf) {

        ACache.get(context).put(telNumSelf, JsonString);

    }

    public static boolean isHaveHistory(Context context, String telNumSelf) {

        return TextUtils.isEmpty(getHistoryPay(context, telNumSelf)) ? false : true;

    }

    public static String getHistoryPay(Context context, String telNumSelf) {

        return ACache.get(context).getAsString(telNumSelf);

    }

    public static void clearHistoryPay(Context context, String telNumSelf) {

        ACache.get(context).remove(telNumSelf);

    }

}
