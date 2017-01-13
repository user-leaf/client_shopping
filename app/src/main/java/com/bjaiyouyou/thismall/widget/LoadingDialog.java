package com.bjaiyouyou.thismall.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bjaiyouyou.thismall.R;

/**
 * 加载中Dialog
 *
 * User: kanbin
 * Date: 2016/11/3
 */
public class LoadingDialog extends Dialog {

    private Context context;
    private ImageView imageLoading;
    private RotateAnimation operatingAnim;

    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        imageLoading = (ImageView)findViewById(R.id.loading);
        operatingAnim = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.anim_loading);
//        LinearInterpolator lin = new LinearInterpolator();
//        operatingAnim.setInterpolator(lin);
        operatingAnim.setRepeatCount(Animation.INFINITE);
        imageLoading.startAnimation(operatingAnim);

    }

    @Override
    public void show() {
        if (imageLoading != null && !this.isShowing()) {
            imageLoading.startAnimation(operatingAnim);
        }
        super.show();
    }

    @Override
    public void dismiss() {
        if (imageLoading != null && this.isShowing()) {
            imageLoading.clearAnimation();
        }
        super.dismiss();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction()==MotionEvent.ACTION_MOVE) {
//            dismiss();
//            return true;
//        }
//        return super.onTouchEvent(event);
        return true;
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