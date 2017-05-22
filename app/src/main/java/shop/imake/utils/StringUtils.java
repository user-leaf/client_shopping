package shop.imake.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/11/8.
 */
public class StringUtils {

    private StringUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 得到处理后的异常信息
     *
     * @param message
     * @return
     */
    public static String getExceptionMessage(String message) {
        String ret = "";
        if (!TextUtils.isEmpty(message)) {
            int index = message.indexOf(":");
            String left = "";
            if (index > 0) {
                left = message.substring(0, index).trim(); // 分号左边的字符串
            }
            if (!TextUtils.isEmpty(left) && isNum(left)) {
                // 409 : 密码错误
                if (!TextUtils.isEmpty(message)) {
                    String right = message.substring(index + 1).trim();
                    ret = right;
                }

            } else {
                // request failed , reponse's code is : 500
                ret = message;
            }
        }
        return ret;
    }

    // 判断是否是数字
    public static boolean isNum(String str) {
        return str.matches("[0-9]*");
    }
}
