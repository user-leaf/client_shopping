package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.MyOrder;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 订单有多个显示条目
 * @author QuXinhang
 *Creare 2016/10/21 16:14
 *
 *
 */
public class MyOrderMoreImagesRecycleViewAdapter extends  RecyclerView.Adapter<MyOrderMoreImagesRecycleViewAdapter.ViewHolder>{
    private List<MyOrder.DataBean.OrderDetailBean> datas;
    private Context context;
    private LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public MyOrderMoreImagesRecycleViewAdapter(Context context, List<MyOrder.DataBean.OrderDetailBean> datas){
        mInflater=LayoutInflater.from(context);
        this.datas= datas;
        this.context=context;
    }

    @Override
    public MyOrderMoreImagesRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_myorder_more_iv, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mItemClickListener, mItemLongClickListener);
//        LogUtils.e("onCreateViewHolder",""+position);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyOrderMoreImagesRecycleViewAdapter.ViewHolder holder, int position) {
        //填充数据
        MyOrder.DataBean.OrderDetailBean detailBeani =datas.get(position);
        LogUtils.e("照片长度：",datas.size()+"");
        //图片
        if (detailBeani != null) {
            if (detailBeani.getProduct_image() != null && detailBeani.getProduct_image().getImage_path() != null && detailBeani.getProduct_image().getImage_base_name() != null) {
                String imgUrl=detailBeani.getProduct_image().getImage_path() + "/" + detailBeani.getProduct_image().getImage_base_name();
                Glide.with(context)
//                        .load(ImageUtils.getThumb(imgUrl,holder.iv.getWidth()/2,0))
                        .load(imgUrl)
                        .placeholder(R.mipmap.list_image_loading)
                        .error(R.mipmap.list_image_loading)
                        .into(holder.iv);
            }
        }

    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnItemClickListener mListener;
        private OnItemLongClickListener mLongClickListener;
        private ImageView iv;

        public ViewHolder(View rootView, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
            super(rootView);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            iv = (ImageView) rootView.findViewById(R.id.iv_myorder_more);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        /**
         * 条目布局添加点击事假
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

}
