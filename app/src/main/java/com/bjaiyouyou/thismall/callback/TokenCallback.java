package com.bjaiyouyou.thismall.callback;

import android.content.Context;
import android.content.Intent;

import com.bjaiyouyou.thismall.activity.LoginActivity;

/**
 * Token失效等异常时的接口回调
 *
 * @author kanbin
 * @date 2016/7/18
 */
public class TokenCallback {

    public static void onDealTokenError(Context context){
        // 跳转到登录页
//        Intent intent = new Intent(context, LoginTempActivity.class);
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
