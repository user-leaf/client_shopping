package com.bjaiyouyou.thismall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 为解决ScrollView与GridView嵌套问题
 * 重写了GridView，让其失去滑动性
 * <p/>
 * http://mobile.51cto.com/aprogram-394381.htm
 * http://blog.csdn.net/zhiying201039/article/details/8631418
 *
 * @author JackB
 * @date 2016/6/2
 */
public class NoScrollGridView extends GridView {
    public NoScrollGridView(Context context) {
        this(context, null);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
