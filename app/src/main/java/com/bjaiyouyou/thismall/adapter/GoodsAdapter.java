package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.Goods;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 *  1、抢购更多适配器
 *
 * @author QuXinhang
 *Creare 2016/7/11 12:09
 *
 */
public class GoodsAdapter extends MyBaseAdapter<Goods.DataBean>  {
    private List<Goods.DataBean> data=null;
    private  Context context;

    public GoodsAdapter(List datas, Context context) {
        super(datas, context);
        this.data=datas;
        this.context=context;
//        Log.e("size",""+datas.size());
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=getInflater().inflate(R.layout.item_purchase_search_gv,null);
        }
        if (holder==null){
            holder=new ViewHolder();
            holder.iv= ((ImageView) convertView.findViewById(R.id.iv_purchase_search));
            holder.tvName= ((TextView) convertView.findViewById(R.id.tv_purchase_search_name));
            holder.tvPrice= ((TextView) convertView.findViewById(R.id.tv_purchase_search_price));
            holder.tvIntegral=((TextView) convertView.findViewById(R.id.tv_purchase_search_integral));
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //填充数据
        Goods.DataBean goods=null;
        goods=data.get(position);
       if (data!=null){
          if(goods!=null){
              if (goods.getProduct()!=null){
                  if (!TextUtils.isEmpty(goods.getProduct().getName())){
                      holder.tvName.setText(goods.getProduct().getName());
                  }
              }
              if (goods.getSize()!=null){
                  if (!TextUtils.isEmpty(goods.getSize().getPrice()+"")){
//        Log.e("",goods.getName());
                      String price="￥"+goods.getSize().getPrice();
//        String price="￥"+goods.getPrice();
                      holder.tvPrice.setText(price);
                  }
                  if (!TextUtils.isEmpty(goods.getSize().getIntegration_price()+"")){
                      String integral="+"+goods.getSize().getIntegration_price()+"兑换券";
                      holder.tvIntegral.setText(integral);
                  }
              }

              if (goods.getImage()!=null && !TextUtils.isEmpty(goods.getImage().getImage_path())&&!TextUtils.isEmpty(goods.getImage().getImage_base_name())){
                  Glide.with(context)
                          .load(goods.getImage().getImage_path()+"/"+goods.getImage().getImage_base_name())
                          .error(R.mipmap.list_image_loading)
                          .placeholder(R.mipmap.list_image_loading)
                          .into(holder.iv);

              }
          }
       }
        return convertView;
    }
    class ViewHolder{
        ImageView iv;
        TextView tvName;
        TextView tvPrice;
        TextView tvIntegral;
    }
}
