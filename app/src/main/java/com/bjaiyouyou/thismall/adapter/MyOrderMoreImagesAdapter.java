package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
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
public class MyOrderMoreImagesAdapter extends MyBaseAdapter<MyOrder.DataBean.OrderDetailBean> {
    private List<MyOrder.DataBean.OrderDetailBean>datas;
    private Context context;

    public MyOrderMoreImagesAdapter(List<MyOrder.DataBean.OrderDetailBean> datas, Context context) {
        super(datas,context);
        this.datas= datas;
        this.context=context;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=getInflater().inflate(R.layout.item_myorder_more_iv,null);
            holder=new ViewHolder();
            holder.iv= ((ImageView) convertView.findViewById(R.id.iv_myorder_more));
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
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
        return convertView;
    }
    public class ViewHolder{
        private ImageView iv;
    }
}
