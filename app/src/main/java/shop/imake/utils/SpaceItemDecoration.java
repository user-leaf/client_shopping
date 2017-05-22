package shop.imake.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 设置RecycleView的间距
 * @author Alice
 *Creare 2016/11/10 17:18
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
    private int leftSpace;
    private int rightSpace;
    private int topSpace;
    private int bottomSpace;

    public SpaceItemDecoration(int leftSpace, int rightSpace, int topSpace, int bottomSpace) {
        this.leftSpace = leftSpace;
        this.rightSpace = rightSpace;
        this.topSpace = topSpace;
        this.bottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildPosition(view)!=0){
            outRect.left=leftSpace;
            outRect.right=rightSpace;
            outRect.top=topSpace;
            outRect.bottom=bottomSpace;
        }
    }
}
