package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.HomeNavigationItem;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 *
 * @author QuXinhang
 *Creare 2016/7/5 10:24
 * 主页面我抢购的适配器
 *
 */
public class HomeNavigationAdapter extends MyBaseAdapter<HomeNavigationItem.DataBean> {
    private List<HomeNavigationItem.DataBean> data;
    private Context context;
    public HomeNavigationAdapter(List<HomeNavigationItem.DataBean> datas, Context context) {
        super(datas, context);
        this.data=datas;
        this.context=context;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=getInflater().inflate(R.layout.item_home_navigatiom_gv,null);
        }
        if (holder==null){
            holder=new ViewHolder();
            holder.iv= (ImageView) convertView.findViewById(R.id.iv_home_navigation_item);
            holder.tv= (TextView) convertView.findViewById(R.id.tv_home_navigation_item);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder) convertView.getTag();
        }
//        填充数据
        HomeNavigationItem.DataBean item=data.get(position);
        holder.tv.setText(item.getName());
        Glide.with(context)
                .load(item.getImage().getImage_path()+"/"+item.getImage().getImage_base_name())
                .error(R.mipmap.list_image_loading)
                .placeholder(R.mipmap.list_image_loading)
                .into(holder.iv);
        return convertView;
    }
    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}
