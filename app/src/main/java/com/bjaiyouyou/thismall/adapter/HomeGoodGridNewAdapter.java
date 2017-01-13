package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.HomeProductModel;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 首页每日上新的RecycleView的适配器
 *
 * @author QuXinhang
 *         Creare 2016/11/10 13:3
 */
public class HomeGoodGridNewAdapter extends RecyclerView.Adapter<HomeGoodGridNewAdapter.ViewHolder>  {
    //先定义两个ItemViewType，0代表头，1代表表格中间的部分
    //数据源
    private LayoutInflater mInflater;
    private Context mContext;
    private List<HomeProductModel.DataBean> mList;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;


    //构造函数
    public HomeGoodGridNewAdapter(Context mContext, List<HomeProductModel.DataBean> mList) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mList = mList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private OnItemClickListener mListener;
        private OnItemLongClickListener mLongClickListener;
        private ImageView mImageView;
        private TextView mTvTitle;
        private TextView mTvPrice;
        private TextView mTvPoints;
        public ViewHolder(View rootView,OnItemClickListener listener,OnItemLongClickListener longClickListener) {
            super(rootView);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            mImageView = (ImageView) rootView.findViewById(R.id.home_good_grid_item_iv_pic);
            mTvTitle = (TextView) rootView.findViewById(R.id.home_good_grid_item_tv_title);
            mTvPrice = (TextView) rootView.findViewById(R.id.home_good_grid_item_tv_price);
            mTvPoints = (TextView) rootView.findViewById(R.id.home_good_grid_item_tv_points);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        /**
         * 条目布局添加点击事假
         * @param v
         */
        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mLongClickListener != null){
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = mInflater.inflate(R.layout.item_home_good_grid, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view,mItemClickListener,mItemLongClickListener);
//        LogUtils.e("onCreateViewHolder",""+position);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        LogUtils.e("onBindViewHolder",""+position);
        //填充数据
        // 3. 展示数据
        HomeProductModel.DataBean productInfo = mList.get(position);

        if (productInfo != null) {
            if (productInfo.getImage() != null && productInfo.getImage().getImage_path() != null && productInfo.getImage().getImage_base_name() != null) {
                String imgUrl = productInfo.getImage().getImage_path() + productInfo.getImage().getImage_base_name();
                if (!TextUtils.isEmpty(imgUrl)) {
                    imgUrl = productInfo.getImage().getImage_path() + "/" + productInfo.getImage().getImage_base_name();

                    Glide.with(mContext)
                            .load(ImageUtils.getThumb(imgUrl, ScreenUtils.getScreenWidth(mContext) / 2, 0))
                            .error(R.mipmap.list_image_loading)
                            .placeholder(R.mipmap.list_image_loading)
                            .into(viewHolder.mImageView);
                }
            }
            if (productInfo.getName() != null) {
                viewHolder.mTvTitle.setText(productInfo.getName());
            }
            if (productInfo.getSize() != null) {
                String price = "￥" + productInfo.getSize().getPrice();
                viewHolder.mTvPrice.setText(price);
                String integral = "+" + productInfo.getSize().getIntegration_price() + "积分";
                viewHolder.mTvPoints.setText(integral);
            }
        }
    }

    /**
     * 追加数据
     * @param dd
     */
    public void addAll(List<HomeProductModel.DataBean> dd)
    {
        mList.addAll(dd);
        notifyDataSetChanged();
    }

    /**
     * 清空数据源
     */

    public void clear()
    {
        mList.clear();
//        notifyDataSetChanged();
    }

    public interface OnItemClickListener {

         void onItemClick(View view,int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }
    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }

}
