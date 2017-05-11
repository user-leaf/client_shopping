package com.bjaiyouyou.thismall.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;

/**
 * 标题栏
 * User: JackB
 * Date: 2016/5/5
 */
public class IUUTitleBar extends RelativeLayout{


    protected RelativeLayout leftLayout;
    protected ImageView leftImage;
    protected RelativeLayout rightLayout;
    protected ImageView rightImage;
    protected TextView titleView;
    protected RelativeLayout titleLayout;


    public IUUTitleBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public IUUTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IUUTitleBar(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.iuu_widget_title_bar, this);
        leftLayout = (RelativeLayout) findViewById(R.id.left_layout);
        leftImage = (ImageView) findViewById(R.id.left_image);
        rightLayout = (RelativeLayout) findViewById(R.id.right_layout);
        rightImage = (ImageView) findViewById(R.id.right_image);
        titleView = (TextView) findViewById(R.id.title);
        titleLayout = (RelativeLayout) findViewById(R.id.root);

        parseStyle(context, attrs);
    }

    private void parseStyle(Context context, AttributeSet attrs){
        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IUUTitleBar);
            String title = ta.getString(R.styleable.IUUTitleBar_iuuTitleBarTitle);
            titleView.setText(title);

            int color = ta.getColor(R.styleable.IUUTitleBar_iuuTitleBarTitleColor, getResources().getColor(R.color.white));
            titleView.setTextColor(color);

            Drawable leftDrawable = ta.getDrawable(R.styleable.IUUTitleBar_iuuTitleBarLeftImage);
            if (null != leftDrawable) {
                leftImage.setImageDrawable(leftDrawable);
            }
            Drawable rightDrawable = ta.getDrawable(R.styleable.IUUTitleBar_iuuTitleBarRightImage);
            if (null != rightDrawable) {
                rightImage.setImageDrawable(rightDrawable);
            }

            Drawable background = ta.getDrawable(R.styleable.IUUTitleBar_iuuTitleBarBackground);
            if(null != background) {
                titleLayout.setBackgroundDrawable(background);
            }

            ta.recycle();
        }
    }

    public void setLeftImageResource(int resId) {
        leftImage.setImageResource(resId);
    }

    public void setRightImageResource(int resId) {
        rightImage.setImageResource(resId);
    }

    public void setLeftLayoutClickListener(OnClickListener listener){
        leftLayout.setOnClickListener(listener);
    }

    public void setRightLayoutClickListener(OnClickListener listener){
        rightLayout.setOnClickListener(listener);
    }

    public void setLeftLayoutVisibility(int visibility){
        leftLayout.setVisibility(visibility);
    }

    public void setRightLayoutVisibility(int visibility){
        rightLayout.setVisibility(visibility);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void setBackgroundColor(int color){
        titleLayout.setBackgroundColor(color);
    }

    public RelativeLayout getLeftLayout(){
        return leftLayout;
    }

    public RelativeLayout getRightLayout(){
        return rightLayout;
    }

}
