package shop.imake.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 *
 * @author Alice
 *Creare 2016/6/22 15:25
 * 解决scrollView与ListView的滑动冲动
 *
 */
public class NoScrollListView extends ListView {
    public NoScrollListView(Context context) {
        this(context, null);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
