package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.HistoryBuy;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 历史购买recycleview适配器
 *
 * @author QuXinhang
 *Creare 2016/12/9 15:33
 */
public class HistoryBuyRecycleViewAdapter extends RecyclerView.Adapter<HistoryBuyRecycleViewAdapter.ViewHolder> {
    private Context mContext;
    //数据源
    private List<HistoryBuy.DataBean> mList;
    //先定义两个ItemViewType，0代表头，1代表表格中间的部分
    private LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;
    private HistoryBuy.DataBean goods;


    //构造函数
    public HistoryBuyRecycleViewAdapter(Context mContext, List<HistoryBuy.DataBean> mList) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.mList = mList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnItemClickListener mListener;
        private OnItemLongClickListener mLongClickListener;
        private ImageView iv;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvIntegral;
        private TextView tvSoldOut;
        private LinearLayout llBack;

        public ViewHolder(View rootView, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
            super(rootView);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            iv = (ImageView) rootView.findViewById(R.id.iv_history_buy);
            tvName = (TextView) rootView.findViewById(R.id.tv_history_buy_name);
            tvPrice = (TextView) rootView.findViewById(R.id.tv_history_buy_price);
            tvIntegral = (TextView) rootView.findViewById(R.id.tv_history_buy_integral);
            tvSoldOut = (TextView) rootView.findViewById(R.id.tv_history_buy_sold_out);
            llBack= ((LinearLayout) rootView.findViewById(R.id.ll_history_buy));
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
            LogUtils.e("ViewHolder","");
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

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = mInflater.inflate(R.layout.item_history_buy_gv, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mItemClickListener, mItemLongClickListener);
        LogUtils.e("onCreateViewHolder",""+position);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        LogUtils.e("onBindViewHolder", "" + position);
        //填充数据
        // 3. 展示数据
        goods = mList.get(position);

        if (goods != null) {

            //判断是否下架
            //下架
            //test
//            if (goods.getProduct().getDeleted_at()==null||goods.getProduct().getOnsell()!=0){

            if (goods.getProduct()==null||goods.getProduct().getDeleted_at()!=null||goods.getProduct().getOnsell()==0){
//                LogUtils.e("Deleted_at",goods.getProduct().getDeleted_at()+"");
//                LogUtils.e("Onsell",goods.getProduct().getOnsell()+"");
                //下架提醒
                holder.tvSoldOut.setVisibility(View.VISIBLE);
                //透明度
                holder.llBack.setAlpha(0.4f);
                holder.tvSoldOut.setAlpha(0.5f);
                //设置不可点击
                holder.mListener=null;
            }else {
                holder.tvSoldOut.setVisibility(View.GONE);
                holder.llBack.setAlpha(1f);
                holder.mListener=mItemClickListener;
            }


            if (goods.getProduct() != null && goods.getProduct().getName() != null) {
                if (!TextUtils.isEmpty(goods.getProduct().getName())) {
                    holder.tvName.setText(goods.getProduct().getName());
                }
            }
            if (goods.getProduct_size() != null) {
                if (!TextUtils.isEmpty(goods.getProduct_size().getPrice() + "")) {
//        Log.e("",goods.getName());
                    String price = "￥" + goods.getProduct_size().getPrice();
//        String price="￥"+goods.getPrice();
                    holder.tvPrice.setText(price);
                }
                if (!TextUtils.isEmpty(goods.getProduct_size().getIntegration_price() + "")) {
                    String integral = "+" + goods.getProduct_size().getIntegration_price() + "积分";
                    holder.tvIntegral.setText(integral);
                }
            }

            if (goods.getProduct_image() != null && goods.getProduct_image().getImage_path() != null && goods.getProduct_image().getImage_base_name() != null) {
                //方法一：获取原图
//                Glide.with(context)
//                        .load(goods.getImage().getImage_path() + "/" + goods.getImage().getImage_base_name())
                //方法二：获取缩略图
                String imgUrl = goods.getProduct_image().getImage_path() + "/" + goods.getProduct_image().getImage_base_name();
                Glide.with(mContext)
                        .load(ImageUtils.getThumb(imgUrl, ScreenUtils.getScreenWidth(mContext) / 2, 0))
                        .error(R.mipmap.list_image_loading)
                        .placeholder(R.mipmap.list_image_loading)
                        .into(holder.iv);

            }
        }
    }

    /**
     * 追加数据
     *
     * @param dd
     */
    public void addAll(List<HistoryBuy.DataBean> dd) {
        mList.addAll(dd);
        notifyDataSetChanged();
    }

    /**
     * 清空数据源
     */

    public void clear() {
        mList.clear();
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
