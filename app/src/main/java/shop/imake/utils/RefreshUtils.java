package shop.imake.utils;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * Created by Administrator on 2017/4/2.
 */
public class RefreshUtils {

    /**
     * 统一xRecyclerView加载更多的样式
     * @param xRecyclerView
     */
    public static void setLoadMoreProgressStyleStyle(XRecyclerView xRecyclerView){
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.CubeTransition);
    }

}
