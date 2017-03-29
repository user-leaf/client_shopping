package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/29.
 */
public class ClassifyListDropDownAdapter extends BaseAdapter{
    private Context mContext;
    private List<String> mList;
    private int checkItemPosition = 0;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }
    public ClassifyListDropDownAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
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
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_classify_default_drop_down, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }


    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.mText.setText(mList.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.mText.setTextColor(mContext.getResources().getColor(R.color.drop_down_selected));
                viewHolder.mText.setBackgroundResource(R.color.check_bg);
            } else {
                viewHolder.mText.setTextColor(mContext.getResources().getColor(R.color.drop_down_unselected));
                viewHolder.mText.setBackgroundResource(R.color.white);
            }
        }
    }

    static class ViewHolder {
        TextView mText;
        ViewHolder(View view) {
            mText = (TextView) view.findViewById(R.id.text);
        }
    }
}
