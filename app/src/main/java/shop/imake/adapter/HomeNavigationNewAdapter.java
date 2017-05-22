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
import shop.imake.model.HomeNavigationItemNew;
import shop.imake.utils.ImageUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.ScreenUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 主页面我抢购的适配器
 * @author Alice
 *Creare 2016/7/28 11:07
 *
 *
 */
public class HomeNavigationNewAdapter extends MyBaseAdapter<HomeNavigationItemNew.RushToPurchaseTimeFrameBean.DetailBean> {
    private List<HomeNavigationItemNew.RushToPurchaseTimeFrameBean.DetailBean> data;
    private Context context;

    public HomeNavigationNewAdapter(List<HomeNavigationItemNew.RushToPurchaseTimeFrameBean.DetailBean> datas, Context context) {
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
        HomeNavigationItemNew.RushToPurchaseTimeFrameBean.DetailBean item=data.get(position);
        if(item!=null){
            if (item.getSize()!=null){
                String price="￥"+item.getSize().getRush_price()+"\n+"+item.getSize().getIntegration_price()+"兑换券";
                //改变部分文字的颜色
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                SpannableStringBuilder builder = new SpannableStringBuilder(price);
                builder.setSpan(redSpan, 0, price.indexOf("+")-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tv.setText(builder);
                LogUtils.e("price  HomeNavigationItemNew:",""+price);
            }
            if (item.getImage()!=null&& item.getImage().getImage_path()!=null&&item.getImage().getImage_base_name()!=null){

                String imgUrl = item.getImage().getImage_path() + "/" + item.getImage().getImage_base_name();

                Glide.with(context)
                        .load(ImageUtils.getThumb(imgUrl, ScreenUtils.getScreenWidth(context)/3,0))
                        .error(R.mipmap.list_image_loading)
                        .placeholder(R.mipmap.list_image_loading)
                        .into(holder.iv);
            }
        }
        return convertView;
    }
    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}
