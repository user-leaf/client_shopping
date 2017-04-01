package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.SearchResultGoods;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 搜索结果适配器
 *
 * @author QuXinhang
 *         Creare 2016/8/9 16:12
 */
public class SearchResultGoodsAdapter extends RecyclerView.Adapter<SearchResultGoodsAdapter.ViewHolder> {
    private List<SearchResultGoods.DataBean> data;
    private Context context;
    private LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public SearchResultGoodsAdapter(List datas, Context context) {
        mInflater = LayoutInflater.from(context);
        this.data = datas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.item_purchase_search_gv, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mItemClickListener, mItemLongClickListener);
//        LogUtils.e("onCreateViewHolder",""+position);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //填充数据
        SearchResultGoods.DataBean goods = data.get(position);
        if (goods != null) {
            if (goods.getName() != null) {
                holder.tvName.setText(goods.getName());
            }
            if (goods.getSize() != null) {
                String price = "￥" + goods.getSize().getPrice();
//        String price="￥"+goods.getPrice();
                holder.tvPrice.setText(price);
                String integral = "+" + goods.getSize().getIntegration_price() + "兑换券";
                holder.tvIntegral.setText(integral);
            }

            if (goods.getImage() != null && goods.getImage().getImage_path() != null && goods.getImage().getImage_base_name() != null) {

                //方法一：获取原图
//                Glide.with(context)
//                        .load(goods.getImage().getImage_path() + "/" + goods.getImage().getImage_base_name())
                //方法二：获取缩略图
                String imgUrl = goods.getImage().getImage_path() + "/" + goods.getImage().getImage_base_name();
                Glide.with(context)
                        .load(ImageUtils.getThumb(imgUrl, ScreenUtils.getScreenWidth(context) / 2, 0))
                        .error(R.mipmap.list_image_loading)
                        .placeholder(R.mipmap.list_image_loading)
                        .into(holder.iv);
            }
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnItemClickListener mListener;
        private OnItemLongClickListener mLongClickListener;
        private ImageView iv;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvIntegral;

        public ViewHolder(View rootView, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
            super(rootView);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            iv = (ImageView) rootView.findViewById(R.id.iv_purchase_search);
            tvName = (TextView) rootView.findViewById(R.id.tv_purchase_search_name);
            tvPrice = (TextView) rootView.findViewById(R.id.tv_purchase_search_price);
            tvIntegral = (TextView) rootView.findViewById(R.id.tv_purchase_search_integral);
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


    /**
     * 追加数据
     *
     * @param dd
     */
    public void addAll(List<SearchResultGoods.DataBean> dd) {
        data.addAll(dd);
        notifyDataSetChanged();
    }

    /**
     * 清空数据源
     */

    public void clear() {
        data.clear();
//        notifyDataSetChanged();
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
