package shop.imake.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.model.OrderPaySuccessProductInfo;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 订单支付成功页GridView适配器
 *
 * @author JackB
 * @date 2016/6/28
 */
public class OrderPaySuccessAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderPaySuccessProductInfo> mList;

    public OrderPaySuccessAdapter(Context context, List<OrderPaySuccessProductInfo> list) {
        mContext = context;
        mList = list;
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
        } else {
            ret = LayoutInflater.from(mContext).inflate(R.layout.item_order_pay_success, parent, false);
        }

        // 2. 减少findViewById的次数
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.mImageView = (ImageView) ret.findViewById(R.id.order_pay_success_item_iv_image);
            holder.mTvTitle = (TextView) ret.findViewById(R.id.order_pay_success_item_tv_title);
            holder.mTvPrice = (TextView) ret.findViewById(R.id.order_pay_success_item_tv_price);
            holder.mTvPoints = (TextView) ret.findViewById(R.id.order_pay_success_item_tv_points);

            ret.setTag(holder);
        }

        // 3. 展示数据
        OrderPaySuccessProductInfo productInfo = mList.get(position);
        Glide.with(mContext).load(productInfo.getImageUrl()).into(holder.mImageView);
        holder.mTvTitle.setText(productInfo.getTitle());
        holder.mTvPrice.setText("¥" + productInfo.getPrice());
        if (productInfo.getPoints() != null) {
            holder.mTvPoints.setText("+" + productInfo.getPoints() + "兑换券");
        } else {
            holder.mTvPoints.setText("" + productInfo.getPoints());
        }

        return ret;
    }

    class ViewHolder {
        private ImageView mImageView;
        private TextView mTvTitle;
        private TextView mTvPrice;
        private TextView mTvPoints;
    }
}
