package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.OrderDetailModel;
import com.bjaiyouyou.thismall.utils.DoubleTextUtils;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

/**
 * 订单详情页列表适配器
 * 申请退款页列表适配器
 *
 * @author JackB
 * @date 2016/6/12
 */
public class OrderDetailAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private List<OrderDetailModel.OrderBean.OrderDetailBean> mList;

    public OrderDetailAdapter(Context context, List<OrderDetailModel.OrderBean.OrderDetailBean> list) {
        mContext = context;
        mList = list;
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<OrderDetailModel.OrderBean.OrderDetailBean> list) {
        if (mList != null) {
            mList = list;
            this.notifyDataSetChanged();
        }
    }

    /**
     * 追加数据
     *
     * @param list
     */
    public void addData(List<OrderDetailModel.OrderBean.OrderDetailBean> list) {
        if (mList != null) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (mList != null) {
            ret = mList.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;

        // 1. converView 复用
        if (convertView != null) {
            ret = convertView;
        } else {
            ret = LayoutInflater.from(mContext).inflate(R.layout.item_order_detail, parent, false);
        }

        // 2. 减少findViewById的次数
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.ivImage = (ImageView) ret.findViewById(R.id.order_detail_item_iv_pic);
            holder.tvName = (TextView) ret.findViewById(R.id.order_detail_item_tv_name);
            holder.tvDesc = (TextView) ret.findViewById(R.id.order_detail_item_tv_desc);
            holder.tvPrice = (TextView) ret.findViewById(R.id.order_detail_item_tv_price);
            holder.tvJifen = (TextView) ret.findViewById(R.id.order_detail_item_tv_points);
            holder.tvNum = (TextView) ret.findViewById(R.id.order_detail_item_tv_count);
            holder.tvAfterSale = (TextView) ret.findViewById(R.id.order_detail_item_tv_after_sale);

            holder.tvAfterSale.setOnClickListener(this);

            ret.setTag(holder);
        }

        // init
        holder.tvName.setText("");
        holder.tvDesc.setText("");
        holder.tvPrice.setText("");
        holder.tvJifen.setText("");
        holder.tvNum.setText("");

        // 3. 显示数据
//        ProductInfo productInfo = mList.get(position).getProductInfo();
//        if (productInfo != null) {
//            if (productInfo.getProductImageUrl() != null) {
//                Glide.with(mContext).load(productInfo.getProductImageUrl()).into(holder.ivImage);
//            }
//            holder.tvName.setText(productInfo.getName());
//            holder.tvPrice.setText("¥" + productInfo.getPrice());
//            holder.tvNum.setText("X" + Integer.toString(mList.get(position).getCount()));
//
//            // TODO: 2016/6/25 退款状态
////            int returnStatus = 1;
////            if (returnStatus == 0) {
////                holder.tvAfterSale.setText("退款中");
////            } else if (returnStatus == 1) {
////                holder.tvAfterSale.setText("退款成功");
////            }
//        }

        OrderDetailModel.OrderBean.OrderDetailBean orderDetailBean = mList.get(position);
        if (orderDetailBean != null) {

            if (orderDetailBean.getProduct() != null) {
                holder.tvName.setText(orderDetailBean.getProduct().getName());
            }

            if (orderDetailBean.getProduct_size() != null) {
                holder.tvDesc.setText(orderDetailBean.getProduct_size().getName());
                // 只显示价格20170513
//                int points = orderDetailBean.getProduct_size().getIntegration_price();
//                holder.tvJifen.setText("+" + points + "兑换券");
            }
            holder.tvPrice.setText("¥" + DoubleTextUtils.setDoubleUtils(Double.valueOf(orderDetailBean.getPrice())));
            // 数量
            holder.tvNum.setText("X" + orderDetailBean.getNumber());

            if (orderDetailBean.getProduct_image() != null) {
                String imagePath = orderDetailBean.getProduct_image().getImage_path() + File.separator + orderDetailBean.getProduct_image().getImage_base_name();
                Glide.with(mContext).load(ImageUtils.getThumb(imagePath, ScreenUtils.getScreenWidth(mContext)/4, 0)).placeholder(R.mipmap.list_image_loading).into(holder.ivImage);
            }
        }

        // 4. 设置带有点击事件的按钮的Tag，用于告诉监听器，到底点击哪一个
        holder.tvAfterSale.setTag(position);

        return ret;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_item_tv_after_sale: // 申请售后
                Object tag = v.getTag();
                if (tag != null && tag instanceof Integer) {
                    int position = (int) tag;
                    ToastUtils.showShort("申请售后" + position);
                }
                break;
        }
    }

    class ViewHolder {
        // 商品图片
        private ImageView ivImage;
        // 商品名称
        private TextView tvName;
        // 商品描述
        private TextView tvDesc;
        // 商品价格
        private TextView tvPrice;
        // 积分
        private TextView tvJifen;
        // 购买数量
        private TextView tvNum;
        // 申请售后按钮
        private TextView tvAfterSale;
    }
}
