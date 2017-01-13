package com.bjaiyouyou.thismall.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证用户输入类
 *
 * User: kanbin
 * Date: 2016/9/22
 */
public class ValidateUserInputUtils {

    /**
     * 验证手机号
     *
     * @param phone
     * @return
     */
    public static boolean validateUserPhone(String phone){
        Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }
}
