package com.bjaiyouyou.thismall.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.bjaiyouyou.thismall.MainApplication;

/**
 * Toast工具类
 *
 * @author kanbin
 * @date 2016/6/1
 */

/**
 * @author QuXinhang
 *         Creare 2016/10/14 10:31
 *         添加网络连接错误Toast
 */
public class ToastUtils {
    private static final java.lang.String TAG = ToastUtils.class.getSimpleName();

//    public static boolean isDebug = MainApplication.getIsDebug();// 是否需要打印bug，可以在application的onCreate函数里面初始化

    private ToastUtils() {
    }

    private static void show(Context context, int resId, int duration) {
        Toast.makeText(context, resId, duration).show();
    }

    private static void show(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void showShort(int resId) {
        Toast.makeText(MainApplication.getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String message) {
        Toast.makeText(MainApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int resId) {
        Toast.makeText(MainApplication.getContext(), resId, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String message) {
        Toast.makeText(MainApplication.getContext(), message, Toast.LENGTH_LONG).show();
    }

//    private static void show(Context context, int resId, int duration) {
//        if (isDebug) {
//            Toast.makeText(context, resId, duration).show();
//        }
//    }
//
//    private static void show(Context context, String message, int duration) {
//        if (isDebug) {
//            Toast.makeText(context, message, duration).show();
//        }
//    }
//
//    public static void showShort(int resId) {
//        if (isDebug) {
//            Toast.makeText(MainApplication.getContext(), resId, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public static void showShort(String message) {
//        if (isDebug) {
//            Toast.makeText(MainApplication.getContext(), message, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public static void showLong(int resId) {
//        if (isDebug) {
//            Toast.makeText(MainApplication.getContext(), resId, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public static void showLong(String message) {
//        if (isDebug) {
//            Toast.makeText(MainApplication.getContext(), message, Toast.LENGTH_LONG).show();
//        }
//    }

    /**
     * 格式化e.getMessage()  001 : abc --> abc
     *
     * @param e
     * @param context
     */
    public static void exceptionToast(Exception e, Context context) {
//        if (e!=null) {
//            String eString=e.toString();
//            LogUtils.e("error",eString+"");
//            if (!TextUtils.isEmpty(eString)){
//                while (eString.contains(":")){
//                    eString=eString.substring(eString.indexOf(":")+1,eString.length());
//                }
//                Toast.makeText(context,eString,Toast.LENGTH_SHORT).show();
//            }
//        }

        if (e != null) {
            if (!TextUtils.isEmpty(e.getMessage())){
                Toast.makeText(context, StringUtils.getExceptionMessage(e.getMessage()), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static void showException(Exception e, Context context) {
        exceptionToast(e, context);
    }

    public static void showException(Exception e) {
        showException(e, MainApplication.getContext());
    }

}
