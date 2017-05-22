package shop.imake.animat;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 
 * @author Alice
 *Creare 2016/9/1 20:29
 * 
 * 
 */
public class IUUPullToRefreshListViewAnimation extends Animation {
    private  int startHeight;//起始高度
    private  int targetHeight;//目标高度
    private View view;//被改变的控价

    public IUUPullToRefreshListViewAnimation(int targetHeight, View view) {
        this.targetHeight = targetHeight;
        this.view = view;
        startHeight=view.getLayoutParams().height;//起始位置就是图片的高度
        setDuration(200);
    }

    /**
     *
     * @param interpolatedTime 动画完成度 一般在0~1之间（回弹动画除外）
     * @param t
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        //控价现在的高度=起始高度-移动高度
        //移动高度=起始高度-目标高度
//        int currentHeight= (int) (startHeight-(startHeight-targetHeight)*interpolatedTime);
        int currentHeight= (int) (startHeight-(startHeight*interpolatedTime));
        view.getLayoutParams().height=currentHeight;
        view.requestLayout();
        super.applyTransformation(interpolatedTime, t);
    }
}
