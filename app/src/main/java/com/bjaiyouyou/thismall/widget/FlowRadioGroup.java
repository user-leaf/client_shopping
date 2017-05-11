package com.bjaiyouyou.thismall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

/**
 * 实现radiogroup中 的radiobutton自动换行
 * @author Alice
 *Creare 2016/10/13 16:35
 */
public class FlowRadioGroup extends RadioGroup {

    private int mSpace=20;

    public FlowRadioGroup(Context context) {
        super(context);
    }

    public FlowRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        int x = 0;
        int y = 0;
        int row = 0;

        for (int index = 0; index < childCount; index++) {
            final View child = getChildAt(index);
            if (child.getVisibility() != View.GONE) {
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                // 此处增加onlayout中的换行判断，用于计算所需的高度
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += width+mSpace;
                y = row * (height+mSpace) + height;
                if (x > maxWidth) {
                    x = width+mSpace;
                    row++;
                    y = row * (height+mSpace) + height;
                }
            }
        }
        // 设置容器所需的宽度和高度

        setMeasuredDimension(maxWidth, y+mSpace);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        int maxWidth = r - l;
        int x = 0;
        int y = 0;
        int row = 0;
        for (int i = 0; i < childCount; i++) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += width+mSpace;
                y = row * (height+mSpace) + height;
                if (x > maxWidth) {
                    x = width+mSpace;
                    row++;
                    y = row * (height+mSpace) + height;
                }
                child.layout(x - width, y - height+mSpace, x, y+mSpace);
//                layout(int l, int t, int r, int b)
            }
        }
    }
}
