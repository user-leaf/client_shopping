package com.bjaiyouyou.thismall.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

/**
 * 常用对话框Dialog封装
 * 
 * Created by JackB on 2017/1/9.
 */
public class DialogUtils {

    private DialogUtils(){}

    /**
     * 创建消息对话框
     *
     * @param context 上下文 必填
     * @param title 标题 必填
     * @param message 显示内容 必填
     * @param btnName 按钮名称 必填
     * @param listener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createMessageDialog(Context context, String title, String message,
                                             String btnName, DialogInterface.OnClickListener listener)
    {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(title)) {
            //设置对话框标题
            builder.setTitle(title);
        }
        //设置对话框消息
        builder.setMessage(message);
        //设置按钮
        builder.setPositiveButton(btnName, listener);
        //创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    /**
     * 创建警示（确认、取消）对话框
     *
     * @param context 上下文 必填
     * @param title 标题 必填
     * @param message 显示内容 必填
     * @param positiveBtnName 确定按钮名称 必填
     * @param negativeBtnName 取消按钮名称 必填
     * @param positiveBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createConfirmDialog(Context context, String title, String message,
                                             String positiveBtnName, String negativeBtnName, DialogInterface.OnClickListener positiveBtnListener,
                                             DialogInterface.OnClickListener negativeBtnListener)
    {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(title)) {
            //设置对话框标题
            builder.setTitle(title);
        }
        //设置对话框消息
        builder.setMessage(message);
        //设置确定按钮
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        //设置取消按钮
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        //创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }


    /**
     * 创建自定义（含确认、取消）对话框
     *
     * @param context 上下文 必填
     * @param title 标题 必填
     * @param positiveBtnName 确定按钮名称 必填
     * @param negativeBtnName 取消按钮名称 必填
     * @param positiveBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param view 对话框中自定义视图 必填
     * @return
     */
    public static Dialog createRandomDialog(Context context, String title, String positiveBtnName,
                                            String negativeBtnName, DialogInterface.OnClickListener positiveBtnListener,
                                            DialogInterface.OnClickListener negativeBtnListener, View view)
    {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (!TextUtils.isEmpty(title)) {
            //设置对话框标题
            builder.setTitle(title);
        }
        builder.setView(view);
        //设置确定按钮
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        //设置确定按钮
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        //创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

}
