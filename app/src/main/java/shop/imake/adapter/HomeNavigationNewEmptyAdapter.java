package shop.imake.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.model.HomeNavigationItemNewEmpty;

import java.util.List;

/**
 * 主页面我抢购的适配器
 * @author Alice
 *Creare 2016/7/28 11:07
 *
 *
 */
public class HomeNavigationNewEmptyAdapter extends MyBaseAdapter<HomeNavigationItemNewEmpty> {
    private List<HomeNavigationItemNewEmpty> data;
    private Context context;

    public HomeNavigationNewEmptyAdapter(List<HomeNavigationItemNewEmpty> datas, Context context) {
        super(datas, context);
        this.data=datas;
        this.context=context;
    }

    @Override
    public int getCount() {
        return 4;
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
        //填充数据
        String price="￥0.0\n0兑换券+";
        //改变部分文字的颜色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder builder = new SpannableStringBuilder(holder.tv.getText().toString());
        builder.setSpan(redSpan,0, price.indexOf("+")-3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv.setText(price);
        holder.tv.setText(builder);
        holder.iv.setImageResource(R.mipmap.list_image_loading);
        return convertView;
    }
    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}
