package com.bjaiyouyou.thismall.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * 上拉加载的,解决滑动冲突的GridView
 * @author QuXinhang
 *Creare 2016/7/18 14:58
 */
public class NoScrollPullToRefreshGridView extends PullToRefreshGridView{
    public NoScrollPullToRefreshGridView(Context context) {
        super(context);
    }
    public NoScrollPullToRefreshGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoScrollPullToRefreshGridView(Context context, AttributeSet attrs, int defStyle) {
            super(context,attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
