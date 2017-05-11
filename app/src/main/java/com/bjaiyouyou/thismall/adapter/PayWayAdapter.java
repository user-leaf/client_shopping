package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.PayWayModel;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 支付方式
 * <p>
 * Created by JackB on 2017/4/21.
 */
public class PayWayAdapter extends BaseAdapter {

    private Context mContext;
    private List<PayWayModel> mList;

    public PayWayAdapter(Context context, List<PayWayModel> list) {
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

        // 1. ConvertView复用
        if (convertView != null) {
            ret = convertView;
        } else {
            ret = LayoutInflater.from(mContext).inflate(R.layout.item_pay_way, parent, false);
        }

        // 2. 减少findViewById的次数
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();

            holder.ivIcon = (ImageView) ret.findViewById(R.id.pay_way_item_icon);
            holder.tvTitle = (TextView) ret.findViewById(R.id.pay_way_item_title);
            holder.tvRecommend = (TextView) ret.findViewById(R.id.pay_way_item_recommend);
            holder.tvChoose = (RadioButton) ret.findViewById(R.id.pay_way_item_choose);

            ret.setTag(holder);
        }

        // 3. 显示数据
        PayWayModel payWayModel = mList.get(position);
        Glide.with(mContext).load(payWayModel.getResId()).into(holder.ivIcon);
        holder.tvTitle.setText(payWayModel.getTitle());
        holder.tvRecommend.setVisibility(payWayModel.isRecommend() ? View.VISIBLE : View.GONE);
        holder.tvChoose.setChecked(payWayModel.isChoose());

        return ret;
    }

    private class ViewHolder {
        private ImageView ivIcon;
        private TextView tvTitle;
        private TextView tvRecommend;
        private RadioButton tvChoose;
    }
}
