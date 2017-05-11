package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.CartItem;
import com.bjaiyouyou.thismall.utils.ToastUtils;

import java.util.List;

/**
 * 购物车页 数据列表适配器
 * @see CartAdapter2
 *
 * @author JackB
 * @date 2016/6/5
 */
public class CartAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private List<CartItem> mList;
    private Context mContext;

    public CartAdapter(List<CartItem> list, Context context) {
        mList = list;
        mContext = context;
    }

    /**
     * 设置数据集
     *
     * @param list
     */
    public void setData(List<CartItem> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    /**
     * 追加数据集
     *
     * @param list
     */
    public void addData(List<CartItem> list) {
        if (mList != null) {
            mList.clear();
            mList.addAll(list);
        }
        this.notifyDataSetChanged();
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
        return position;// 商品id？
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        // 1. convertView复用
        if (convertView != null) {
            ret = convertView;
        } else {
            ret = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false);
        }

        // 2. 减少findViewById()的次数
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.chb = (CheckBox) ret.findViewById(R.id.cart_item_chb);
            holder.ivPic = (ImageView) ret.findViewById(R.id.cart_item_iv_pic);
            holder.tvName = (TextView) ret.findViewById(R.id.cart_item_tv_name);
            holder.tvSales = (TextView) ret.findViewById(R.id.cart_item_tv_sales);
            holder.tvPrice = (TextView) ret.findViewById(R.id.cart_item_tv_price);
            holder.ivInc = (ImageView) ret.findViewById(R.id.cart_item_tv_inc);
            holder.ivDec = (ImageView) ret.findViewById(R.id.cart_item_tv_dec);
            holder.tvCount = (TextView) ret.findViewById(R.id.cart_item_tv_count);
            holder.btnClick = (Button) ret.findViewById(R.id.cart_item_btn_click);

            // 设置选中的事件监听
            holder.chb.setOnCheckedChangeListener(this);
            // +1
            holder.ivInc.setOnClickListener(this);
            // -1
            holder.ivDec.setOnClickListener(this);
            // 点击跳转
            holder.btnClick.setOnClickListener(this);

            ret.setTag(holder);
        }

        // 3. 显示内容
        CartItem cartItem = mList.get(position);
        holder.tvName.setText(cartItem.getProductInfo().getName());
        holder.tvPrice.setText(""+cartItem.getProductInfo().getPrice());
        holder.tvCount.setText(Integer.toString(cartItem.getCount()));

        // 4. 设置带有点击事件的按钮的Tag，用于告诉监听器，到底点击哪一个
        holder.chb.setTag(position);
        holder.ivInc.setTag(position);
        holder.ivDec.setTag(position);
        holder.btnClick.setTag(position);

        holder.chb.setChecked(cartItem.isChecked());
        return ret;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object tag = buttonView.getTag();
        if (tag != null && tag instanceof Integer) {
            Integer it = (Integer) tag;
            CartItem cartItem = mList.get(it);
            cartItem.setChecked(isChecked);
        }
        this.notifyDataSetChanged(); //这样可以触发观察者变动价格
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Object tag = v.getTag();

        Integer position;

        switch (id) {
            case R.id.cart_item_tv_dec: // 减1
                if (tag != null && tag instanceof Integer) {
                    // 利用 Tag 附带 按钮所在的 position 位置
                    position = (Integer) tag;
                    CartItem cartItem = mList.get(position);
                    int count = cartItem.getCount();
                    count--;
                    if (count >= 0) {
                        cartItem.setCount(count);
                    } else {
                        // TODO: 2016/6/5 考虑实现0时删除
                    }
                    // 刷新列表
                    this.notifyDataSetChanged();
                }
                break;

            case R.id.cart_item_tv_inc: // 加1
                if (tag != null && tag instanceof Integer) {
                    // 利用 Tag 附带 按钮所在的 position 位置
                    position = (Integer) tag;
                    CartItem cartItem = mList.get(position);

                    int count = cartItem.getCount();
                    count++;
                    cartItem.setCount(count);
                }
                // 刷新列表
                this.notifyDataSetChanged();
                break;

            case R.id.cart_item_btn_click:  // 点击跳转
                if (tag != null && tag instanceof Integer) {
                    position = (Integer) tag;
                    CartItem item = mList.get(position);
                    ToastUtils.showShort("位置"+position+"点击跳转:"+item.toString());
                }
                break;
        }
    }

    private class ViewHolder {
        // 选择框
        private CheckBox chb;
        // 商品图片
        private ImageView ivPic;
        // 名称
        private TextView tvName;
        // 销量
        private TextView tvSales;
        // 价格
        private TextView tvPrice;
        // 数量+1
        private ImageView ivInc;
        // 数量-1
        private ImageView ivDec;
        // 数量显示
        private TextView tvCount;
        // 可点击跳转区域
        private Button btnClick;

    }
}
