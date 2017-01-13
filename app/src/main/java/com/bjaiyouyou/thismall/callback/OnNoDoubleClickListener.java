package com.bjaiyouyou.thismall.callback;

import android.view.View;

import java.util.Calendar;

/**
 * 防止过快点击造成多次点击事件
 * 
 * User: kanbin
 * Date: 2016/9/29
 */
public abstract class OnNoDoubleClickListener implements View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View view);
}