package com.bjaiyouyou.thismall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ListView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.animat.IUUPullToRefreshListViewAnimation;

/**
 * 自定义下拉刷新列表
 * @author Alice
 *Creare 2016/9/1 17:16
 *
 */
public class IUUPullToRefreshListView extends ListView {
    private View headImageView;
    private Context mContext;

    private int mMaxOverY;//最大可拉动值，根据屏幕的高度设置
    private int currentY;//当前总共互动了多少

    private OnLoadingListener mOnLoadingListener;//刷新的监听
    private  int canRefreshHeight=50;//允许刷新监听执行的最小距离，大于等于这个值的时候才视为可刷新

    private int initHeight;//初始的高度，用于回弹的时候使用


    private int initFootHeight;//初始的高度，用于回弹的时候使用


    private boolean isLodaing;//是不是正在加载数据，如果正在加载就不允许继续拖动，以及执行相应的回调

    private  long anDurationTime=0;//小幅度拖动不够刷新需求的回弹时间动画时间，时间短一点

    private View footImageView;//尾部控价
    private boolean isFoot;


    public IUUPullToRefreshListView(Context context) {
        this(context,null);
    }

    public IUUPullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    /**
     * 初始化方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public IUUPullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();//用于存储屏幕的信息的对象
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);//获取屏幕的分辨率信息
        mMaxOverY=displayMetrics.heightPixels/6>100?displayMetrics.heightPixels/6:100;
        init();
    }

    /**
     * 初始化头部状态
     */
    public void  init(){
        //////////////////////////////////////////头部////////////////////////////////////////////////
        final View view= LayoutInflater.from(mContext).inflate(R.layout.iuu_ptr_lv_item_head,null);//headVIew所在布局
        addHeaderView(view);
        headImageView= ((View) view.findViewById(R.id.tv_head_state_text));

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headImageView.getLayoutParams().height=0;
                headImageView.requestLayout();
                initHeight=headImageView.getMeasuredHeight();
                Log.e("initHeight",initHeight+"");
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        ////////////////////////////////////////底部///////////////////////////////////////////////
        final View viewFoot= LayoutInflater.from(mContext).inflate(R.layout.iuu_ptr_lv_item_foot,null);
        addFooterView(viewFoot);
        footImageView= ((View) viewFoot.findViewById(R.id.tv_foot_state_text));

        viewFoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                footImageView.getLayoutParams().height=0;
                footImageView.requestLayout();
                initFootHeight=footImageView.getMeasuredHeight();
                Log.e("initHeight",initFootHeight+"");
                viewFoot.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        });
    }



    /**
     * 超出控件范围的滑动监听
     * @param deltaX
     * @param deltaY  Y轴滚动的距离差 下拉的时候是负数
     * @param scrollX
     * @param scrollY 实际滚动了多少
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX X轴的最大距离
     * @param maxOverScrollY
     * @param isTouchEvent 是否是手指拖动 true ，如果是惯性滚动 false
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (headImageView!=null&&!isLodaing){

           if (deltaY<0&&currentY<mMaxOverY){
               isFoot=false;
               currentY-=deltaY/2;
               int height=headImageView.getLayoutParams().height;
               height-=deltaY/2;
               headImageView.getLayoutParams().height=height;
               headImageView.requestLayout();
           }
        }

        if (footImageView!=null&&!isLodaing){
            if (deltaY>0&&currentY<mMaxOverY){
                isFoot=true;
                currentY+=deltaY/2;
                int height=footImageView.getLayoutParams().height;
                height+=deltaY/2;
                footImageView.getLayoutParams().height=height;
                footImageView.requestLayout();
            }
        }
        Log.e("method---", "overScrollBy() called with: ----" + "deltaX = [" + deltaX + "], deltaY = [" + deltaY + "], scrollX = [" + scrollX + "], scrollY = [" + scrollY + "], scrollRangeX = [" + scrollRangeX + "], scrollRangeY = [" + scrollRangeY + "], maxOverScrollX = [" + maxOverScrollX + "], maxOverScrollY = [" + maxOverScrollY + "], isTouchEvent = [" + isTouchEvent + "]");
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    /**
     * 获取、设置刷新监听
     * @return
     */
    public OnLoadingListener getOnLoadingListener() {
        return mOnLoadingListener;
    }

    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        mOnLoadingListener = onLoadingListener;
    }

    /**
     * 刷新接口
     */
    public interface  OnLoadingListener{
        void onDownRefresh();
        void onUpRefresh();
    }


    /**
     * 判断手势状态进行刷新回弹
     * @param ev
     * @return
     */

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                if (mOnLoadingListener!=null&&currentY>=canRefreshHeight&&!isLodaing){
                    isLodaing=true;

                    if (!isFoot){
                        mOnLoadingListener.onDownRefresh();
                    }else {
                        mOnLoadingListener.onUpRefresh();
                    }
                }else if (currentY>0&&currentY<canRefreshHeight){
                    anDurationTime=100;
                    onRefreshDone();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }



    /**
     * 完成刷新回弹
     * 调用刷新完成方法，回滚到原来的距离，重置已经移动的距离
     */

    public void onRefreshDone(){
        if (!isFoot){
            IUUPullToRefreshListViewAnimation animation=new IUUPullToRefreshListViewAnimation(-initHeight,headImageView);
            if (anDurationTime!=0){
                animation.setDuration(anDurationTime);
                anDurationTime=0;
            }
            headImageView.startAnimation(animation);
            currentY=0;
            isLodaing=false;
            Log.e("刷新完成","刷新完成");
        }
        else {
            IUUPullToRefreshListViewAnimation animationFoot=new IUUPullToRefreshListViewAnimation(-initFootHeight,footImageView);
            if (anDurationTime!=0){
                animationFoot.setDuration(anDurationTime);
                anDurationTime=0;
            }
            footImageView.startAnimation(animationFoot);
            currentY=0;
            isLodaing=false;
            isFoot=false;
            Log.e("加载完成","加载完成");
        }
    }
}
