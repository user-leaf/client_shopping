package shop.imake.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import shop.imake.R;
import shop.imake.model.MyMineOther;
import shop.imake.utils.ImageUtils;
import shop.imake.utils.LogUtils;

/**
 * @author Alice
 *         Creare 2016/6/7 14:10
 *         个人中心其他的适配器
 */
public class MineOtherAdapter extends MyBaseAdapter<MyMineOther.ThreeServicesBean> {
    private Context context;
    private List<MyMineOther.ThreeServicesBean> data;

    public static int ROW_NUMBER = 4;

    public MineOtherAdapter(List<MyMineOther.ThreeServicesBean> datas, Context context) {
        super(datas, context);
        this.context = context;
        this.data = datas;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.item_mine_gridview, parent, false);

            //高度计算,使得每个Item都是正方形
            AbsListView.LayoutParams param = new AbsListView.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(param);

            holder.tv = (TextView) convertView.findViewById(R.id.tv_mineGridView);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_mineGridView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //填充数据
        MyMineOther.ThreeServicesBean myMine = data.get(position);

        if (myMine != null) {
            holder.tv.setText(myMine.getService_name());

            String imgUrl=myMine.getIcon();

            LogUtils.d("img","holder.iv.getWidth()"+holder.iv.getHeight()+"holder.iv.getHeight()"+holder.iv.getHeight());

            //加载网络图片
            Glide.with(context)
                    .load(ImageUtils.getThumb(imgUrl,140,140))
//                    .load(imgUrl)
                    .placeholder(R.mipmap.list_image_loading)
                    .error(R.mipmap.list_image_loading)
                    .into(holder.iv);


        }

        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }

}
