package com.bjaiyouyou.thismall.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.bjaiyouyou.thismall.R;

/**
 * 加载中Dialog
 *
 * User: JackB
 * Date: 2016/11/3
 */
public class LoadingDialog extends Dialog {

    private Context context;

    private LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        this.context = context;
    }

    /**这里只是为了防止私自new对象，如果可以，请直接使用base类中的show/dismiss方法*/
    public static LoadingDialog getInstance(Context context){
        return new LoadingDialog(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction()==MotionEvent.ACTION_MOVE) {
//            dismiss();
//            return true;
//        }
//        return super.onTouchEvent(event);
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
//        return true;
    }

}