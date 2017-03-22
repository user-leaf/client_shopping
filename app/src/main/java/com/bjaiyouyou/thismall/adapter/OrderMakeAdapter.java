package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.CartItem2;
import com.bjaiyouyou.thismall.model.CartModel;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

/**
 * 确认订单页列表适配器
 *
 * @author kanbin
 * @date 2016/6/12
 */
public class OrderMakeAdapter extends BaseAdapter {
    private Context mContext;
    private List<CartItem2> mList;

    public OrderMakeAdapter(Context context, List<CartItem2> list) {
        mContext = context;
        mList = list;
    }

    public void setData(List<CartItem2> list) {
        if (mList != null && list != null) {
            mList.clear();
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
            ret = LayoutInflater.from(mContext).inflate(R.layout.item_order_make, parent, false);
        }

        // 2. 减少findViewById的次数
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.pic = (ImageView) ret.findViewById(R.id.order_make_item_iv_pic);
            holder.name = (TextView) ret.findViewById(R.id.order_make_item_tv_name);
            holder.desc = (TextView) ret.findViewById(R.id.order_make_item_tv_desc);
            holder.price = (TextView) ret.findViewById(R.id.order_make_item_tv_price);
            holder.points = (TextView) ret.findViewById(R.id.order_make_item_tv_points);
            holder.count = (TextView) ret.findViewById(R.id.order_make_item_tv_count);

            ret.setTag(holder);
        }

        // init
        holder.name.setText("");
        holder.desc.setText("");
        holder.count.setText("");
        holder.price.setText("");
        holder.points.setText("");
        Glide.with(mContext).load(R.mipmap.list_image_loading).into(holder.pic);

        // 3. 显示数据
        CartModel cartModel = mList.get(position).getCartModel();

        if (cartModel != null) {
            // 根据商品本身判断是否在抢购中，不用本地时间判断
            // 是否是抢购商品
            boolean isRushGood = false;

            CartModel.ProductBean product = cartModel.getProduct();
            String productName = "";
            if (product != null) {
                productName = product.getName();
                holder.name.setText(productName);
                if (product.getProduct_type() == 0) {
                    isRushGood = true;
                }
            }

            CartModel.ProductSizeBean product_size = cartModel.getProduct_size();
            if (product_size != null) {
                holder.desc.setText(product_size.getName());
                holder.price.setText("¥" + product_size.getPrice());

                // 如果是抢购中商品就设为抢购价+抢购标识
                if (isRushGood) { // 抢购商品
                    CartModel.ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();

                    if (product_time_frame != null) { // 判断抢购商品有没有过期，非抢购商品这个字段为null
                        boolean if_rush_to_purchasing = product_time_frame.isIf_rush_to_purchasing();

                        if (if_rush_to_purchasing) { // 是抢购商品&&没过抢购期&&不是下架商品
                            // 抢购价
                            holder.price.setText("¥" + product_size.getRush_price());
                            // 显示抢购标识
                            SpannableStringBuilder spanString = new SpannableStringBuilder("icon");
                            ImageSpan imgSpan = new ImageSpan(mContext, R.mipmap.list_icon_panicpurchase);
                            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spanString.append(productName);
                            holder.name.setText(spanString);

                        }
                    }

                }

                holder.points.setText("+" + product_size.getIntegration_price() + "积分");

            }

            holder.count.setText("X" + Integer.toString(cartModel.getNumber()));

            if (cartModel.getProduct_images() != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(cartModel.getProduct_images().getImage_path())
                        .append(File.separator)
                        .append(cartModel.getProduct_images().getImage_base_name());
                String imageurl = sb.toString();
                Glide.with(mContext).load(ImageUtils.getThumb(imageurl, ScreenUtils.getScreenWidth(mContext) / 4, 0)).placeholder(R.mipmap.list_image_loading).into(holder.pic);
            }
        }

            // 不用了吧，原因忘了
//            // 是否是抢购商品
//            boolean isRushGood = false;
//
//            if (cartModel.getProduct_images() != null) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(cartModel.getProduct_images().getImage_path());
//                sb.append(File.separator).append(cartModel.getProduct_images().getImage_base_name());
//                String imgUrl = sb.toString();
//                Glide.with(mContext).load(ImageUtils.getThumb(imgUrl, ScreenUtils.getScreenWidth(mContext) / 3, 0)).placeholder(R.mipmap.list_image_loading).into(holder.pic);
//            }
//
//            ProductBean product = cartModel.getProduct();
//            if (product != null) {
//                String productName = product.getName();
//                holder.name.setText(productName);
//                isRushGood = product.getProduct_type() == 0 ? true : false;
//
//                // 包含在if (product != null)中，还是并列..
//                ProductSizeBean product_size = cartModel.getProduct_size();
//                if (product_size != null) {
//                    holder.price.setText("¥" + product_size.getPrice());
//
//                    if (isRushGood) { // 抢购商品
//                        ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();
//
//                        if (product_time_frame != null) { // 判断抢购商品有没有过期，非抢购商品这个字段为null
//                            ProductSizeBean.ProductTimeFrameBean.TimeFrameBean time_frame = product_time_frame.getTime_frame();
//
//                            if (time_frame != null) {
//                                String time_frame1 = time_frame.getTime_frame();
//                                boolean isInRush = CartPage.CartHelper.isRushGoodInRush(time_frame1);
//
//                                if (isInRush) { // 是抢购商品并且在抢购中
//                                    // 抢购价
//                                    holder.price.setText("¥" + product_size.getRush_price());
//                                    // 显示抢购标识
//                                    SpannableStringBuilder spanString = new SpannableStringBuilder("icon");
//                                    ImageSpan imgSpan = new ImageSpan(mContext, R.mipmap.list_icon_panicpurchase);
//                                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                    spanString.append(productName);
//                                    holder.name.setText(spanString);
//                                }
//                            }
//
//                        }
//
//                    }
//
//                    holder.desc.setText("" + product_size.getName());
//                    int integration_price = product_size.getIntegration_price();
//                    holder.points.setText("+" + integration_price + "积分");
//                }
//
//                holder.count.setText("X" + cartModel.getNumber());
//            }

        return ret;
    }

    class ViewHolder {
        // 商品图片
        private ImageView pic;
        // 商品名称
        private TextView name;
        // 商品描述（暂 规格）
        private TextView desc;
        // 商品价格
        private TextView price;
        // 购买消费积分
        private TextView points;
        // 购买数量
        private TextView count;
    }
}
