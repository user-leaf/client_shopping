package com.bjaiyouyou.thismall.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 *
 * @author QuXinhang
 *Creare 2016/11/10 14:38
 *
 *
 */
public class NoScrollRecycleView extends XRecyclerView {
    public NoScrollRecycleView(Context context) {
        this(context, null);
    }

    public NoScrollRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoScrollRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
