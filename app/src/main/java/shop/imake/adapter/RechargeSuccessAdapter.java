package shop.imake.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.model.RechargeSuccessModel;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * UU充值、积分充值 充值成功页
 *
 * User: JackB
 * Date: 2016/8/15
 */
public class RechargeSuccessAdapter extends BaseAdapter{

    private Context mContext;
    private List<RechargeSuccessModel> mList;

    public RechargeSuccessAdapter(Context context, List<RechargeSuccessModel> list) {
        mContext = context;
        mList = list;
    }

    public void addData(List<RechargeSuccessModel> list){
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

        // 1. convertView的复用
        if (convertView != null) {
            ret = convertView;
        }else {
            ret = LayoutInflater.from(mContext).inflate(R.layout.item_recharge_success,parent, false);
        }

        // 2. 减少findViewById()的次数
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.pic = (ImageView) ret.findViewById(R.id.recharge_success_item_iv);
            holder.name = (TextView) ret.findViewById(R.id.recharge_success_item_tv_name);
            holder.price = (TextView) ret.findViewById(R.id.recharge_success_item_tv_price);
            holder.points = (TextView) ret.findViewById(R.id.recharge_success_item_tv_points);

            ret.setTag(holder);
        }

        // 3. 显示数据
        RechargeSuccessModel rechargeSuccessModel = mList.get(position);
        Glide.with(mContext)
                .load(rechargeSuccessModel
                .getImageurl())
                .placeholder(R.mipmap.list_image_loading)
                .into(holder.pic);
        holder.name.setText(rechargeSuccessModel.getName());
        holder.price.setText("¥"+rechargeSuccessModel.getPrice());
        holder.points.setText("+"+rechargeSuccessModel.getPoints()+"积分");

        return ret;
    }

    class ViewHolder{
        // 商品图片
        private ImageView pic;
        // 商品名称
        private TextView name;
        // 商品价格
        private TextView price;
        // 积分
        private TextView points;
    }
}
