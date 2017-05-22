package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.OrderPayFail;
import com.bjaiyouyou.thismall.utils.DoubleTextUtils;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 *
 * @author Alice
 *Creare 2016/6/28 11:31
 *
 * 支付失败适配器
 */
public class OrderPayFailAdapter extends MyBaseAdapter<OrderPayFail.OrderBean.OrderDetailBean>{
    private  List<OrderPayFail.OrderBean.OrderDetailBean> datas;
    private Context context;
    public OrderPayFailAdapter(List<OrderPayFail.OrderBean.OrderDetailBean> datas, Context context) {
        super(datas, context);
        this.datas=datas;
        this.context=context;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=getInflater().inflate(R.layout.item_order_pay_lv,null);
        }
        if (holder==null){
            holder=new ViewHolder();
            holder.iv= (ImageView) convertView.findViewById(R.id.iv_orderpay_img);
            holder.tvName= (TextView) convertView.findViewById(R.id.tv_orderpay_name);
            holder.tvDimension= (TextView) convertView.findViewById(R.id.tv_orderpay_dimension);
            holder.tvIntegral= (TextView) convertView.findViewById(R.id.tv_orderpay_singleMoney);
            holder.tvNumber= (TextView) convertView.findViewById(R.id.tv_orderpay_num);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
         //        填充数据
        OrderPayFail.OrderBean.OrderDetailBean order=datas.get(position);
        if (order!=null){
           if (order.getProduct()!=null&&order.getProduct().getName()!=null){
               holder.tvName.setText(order.getProduct().getName());
           }
            holder.tvNumber.setText("X"+order.getNumber());
            if (order.getProduct_size()!=null&&order.getProduct_size().getName()!=null){
                holder.tvDimension.setText(order.getProduct_size().getName());
            }

            holder.tvIntegral.setText("￥"+ DoubleTextUtils.setDoubleUtils(Double.valueOf(order.getPrice())));
            //        网络加载图片
            if (order.getProduct_image()!=null&& order.getProduct_image().getImage_path()!=null&&order.getProduct_image().getImage_base_name()!=null){

                String imgUrl=order.getProduct_image().getImage_path()+"/"+order.getProduct_image().getImage_base_name();
                LogUtils.e("imgUrl",imgUrl);
                Glide.with(context)
//                        .load(imgUrl)
                        .load(ImageUtils.getThumb(imgUrl, holder.iv.getWidth(), 0))
                        .error(R.mipmap.list_image_loading)
                        .placeholder(R.mipmap.list_image_loading)
                        .into(holder.iv);
            }else {
                LogUtils.d("imgUrl","图片路径有为空项");
            }

        }
        return convertView;
    }
    class ViewHolder{
        ImageView iv;
        TextView tvName;
        TextView tvDimension;
        TextView tvIntegral;
        TextView tvNumber;
    }
}
