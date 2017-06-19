package shop.imake.user;

import android.text.TextUtils;
import android.util.Log;

import shop.imake.MainApplication;
import shop.imake.utils.LogUtils;
import shop.imake.utils.SPUtils;

/**
 * 当前登录用户信息管理
 *
 * @author JackB
 * @date 2016/7/9
 */
public class CurrentUserManager {
    public static final String SP_CURRENT_USER = "current_user";
    public static final String SP_USER_TOKEN = "user_token";


    /**
     * 清除当前登录用户（退出登录时调用）
     */
    public static void clearCurrentUser() {
        SPUtils.remove(MainApplication.getContext(), SP_CURRENT_USER);

    }

    /**
     * 设置当前登录用户（登录成功时调用）
     *
     * @param userInfo
     * @return
     */
    public static UserInfo setCurrentUser(UserInfo userInfo) {
        SPUtils.put(MainApplication.getContext(), SP_CURRENT_USER, userInfo);
        return userInfo;
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static UserInfo getCurrentUser() {
        return (UserInfo) SPUtils.get(MainApplication.getContext(), SP_CURRENT_USER, null);
    }

    /**
     * 设置登录用户Token
     *
     * @param token
     */
    public static void setUserToken(String token) {
        LogUtils.e("TOKEN",token);
        Log.e("TOKEN",token);
        if (token != null) {
            SPUtils.put(MainApplication.getContext(), SP_USER_TOKEN, token);
        }
    }

    /**
     * 获取登录用户Token
     *
     * @return
     */
    public static String getUserToken() {
        String  token=(String) SPUtils.get(MainApplication.getContext(), SP_USER_TOKEN,"");
//        LogUtils.e("TOKEN",token);
        return token;

        //test
//        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjY3NSwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0XC9jb3Vwb25hcGlcL0FkbWluXC9wdWJsaWNcL2FwaVwvdjFcL2F1dGhcL2xvZ2luIiwiaWF0IjoxNDk0NTY3NTE1LCJleHAiOjE0OTcxNTk1MTUsIm5iZiI6MTQ5NDU2NzUxNSwianRpIjoiUUx0M3NidGlLT0IxWXg0UiJ9.yn9iRJajuMCDET6193IvLrKWugouGdYDV-Yz6YvqN94";

    }

    /**
     * 清除登录用户Token
     */
    public static void clearUserToken() {
        SPUtils.remove(MainApplication.getContext(), SP_USER_TOKEN);
    }

    /**
     * 判断用户是否登录
     */
    public static boolean isLoginUser(){
        return TextUtils.isEmpty(getUserToken())?false:true;
    }


    /**
     * Token过期清除缓存
     * @param e
     */
    public static void  TokenDue(Exception e){
        if (e==null){
            return;
        }
        String message=e.toString();
        boolean tokenPastDue= !TextUtils.isEmpty(message)&&
                (message.contains("token_expired")
                        || message.contains("token_absent")
                        || message.contains("user_not_found")
                        || message.contains("token_invalid"));

        if (tokenPastDue){
            CurrentUserManager.clearUserToken();
        }
    }
}
