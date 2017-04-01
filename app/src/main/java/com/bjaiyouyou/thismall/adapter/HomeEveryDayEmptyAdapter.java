package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bumptech.glide.Glide;

/**
 * 首页每日上新的RecycleView的适配器
 *
 * @author QuXinhang
 *         Creare 2016/11/10 13:3
 */
public class HomeEveryDayEmptyAdapter extends RecyclerView.Adapter<HomeEveryDayEmptyAdapter.ViewHolder> {
    //先定义两个ItemViewType，0代表头，1代表表格中间的部分
    //数据源
    private LayoutInflater mInflater;
    private Context mContext;


    //构造函数
    public HomeEveryDayEmptyAdapter(Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mTvName;

        public ViewHolder(View rootView) {
            super(rootView);
            mImageView = (ImageView) rootView.findViewById(R.id.iv_home_navigation_item_empty);
            mTvName = (TextView) rootView.findViewById(R.id.tv_home_navigation_item_empty);
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = mInflater.inflate(R.layout.item_home_navigatiom_gv_empty, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
//        LogUtils.e("onCreateViewHolder",""+position);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        LogUtils.e("onBindViewHolder", "" + position);
        //填充数据
        // 3. 展示数据
        Glide.with(mContext)
                .load(R.mipmap.list_image_loading)
                .error(R.mipmap.list_image_loading)
                .placeholder(R.mipmap.list_image_loading)
                .into(viewHolder.mImageView);
        viewHolder.mTvName.setText("￥0.00\n+0兑换券");
    }

}
