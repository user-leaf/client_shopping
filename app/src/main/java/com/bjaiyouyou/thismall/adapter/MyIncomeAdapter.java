package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.MyIncomeModel;

import java.util.List;

/**
 * 我的收益（众汇）
 */
public class MyIncomeAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyIncomeModel> mList;

    public MyIncomeAdapter(Context context, List<MyIncomeModel> list) {
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
            ret = LayoutInflater.from(mContext).inflate(R.layout.item_my_income, parent, false);
        }

        // 2. 减少findViewById的次数
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.tvTitle = (TextView) ret.findViewById(R.id.my_income_item_title);
            holder.tvNum = (TextView) ret.findViewById(R.id.my_income_item_num);
            holder.tvDesc = (TextView) ret.findViewById(R.id.my_income_item_desc);

            ret.setTag(holder);
        }

        // 3. 显示数据

        return ret;
    }

    private class ViewHolder {
        // 标题
        private TextView tvTitle;
        // 数目
        private TextView tvNum;
        // 描述
        private TextView tvDesc;
    }
}
