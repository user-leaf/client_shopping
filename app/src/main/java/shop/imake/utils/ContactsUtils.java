package shop.imake.utils;

import android.content.Context;
import android.text.TextUtils;


/**
 * 手机通讯处理工具
 */

public class ContactsUtils {


    public static String NOT_HAVE_TELNUM = "不在通讯录";

    public static boolean isHave(Context context, String name) {

//        return "不在通讯录".equals(getDisplayNameByNumber(context, number)) ? false : true;
        return NOT_HAVE_TELNUM.equals(name) ? false : true;
    }


    /**
     * 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
     *
     * @param phoneNum
     * @return
     */
    public static String getPayTelNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return "";
        }
        phoneNum = phoneNum.replaceAll("^(\\+86)", "");
        phoneNum = phoneNum.replaceAll("^(86)", "");
        phoneNum = phoneNum.replaceAll("-", "");
        phoneNum = phoneNum.replaceAll(" ", "");
        phoneNum = phoneNum.trim();
        if (phoneNum.length() > 11) {
            phoneNum = phoneNum.substring(0, 11);
        }

        LogUtils.e("getPayTelNum", phoneNum);
        return phoneNum;
    }

    public static String getPayTelNumCompaer(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return "";
        }
        phoneNum = phoneNum.replaceAll("^(\\+86)", "");
        phoneNum = phoneNum.replaceAll("^(86)", "");
        phoneNum = phoneNum.replaceAll("-", "");
        phoneNum = phoneNum.replaceAll(" ", "");
        phoneNum = phoneNum.trim();
        LogUtils.e("getPayTelNum", phoneNum);
        return phoneNum;
    }
}
