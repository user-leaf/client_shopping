package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.ZhongHuiDetailModle;

import java.util.List;

/**
 * 众汇券详情adapter
 * Created by Alice
 * 2017/5/12
 */
public class ZhongHuiDetailAdapter extends MyBaseAdapter<ZhongHuiDetailModle>{
    private List<ZhongHuiDetailModle> mList;
    private Context mContext;

    public ZhongHuiDetailAdapter(List<ZhongHuiDetailModle> datas, Context context) {
        super(datas, context);
        this.mList=datas;
        this.mContext=context;
    }


    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=getInflater().inflate(R.layout.item_zhanghui_detail,null);
            holder=new ViewHolder();
            holder.llType=((LinearLayout) convertView.findViewById(R.id.LL_zh_detail_month));
            holder.tvMonth=((TextView) convertView.findViewById(R.id.tv_zh_detail_month));
            holder.tvType= ((TextView) convertView.findViewById(R.id.tv_zh_detail_type));
            holder.tvTime= ((TextView) convertView.findViewById(R.id.tv_zh_detail_time));
            holder.tvMoney= ((TextView) convertView.findViewById(R.id.tv_zh_detail_num));
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        //填充数据





        return convertView;
    }
    class ViewHolder{
        private LinearLayout llType;
        private TextView tvMonth;

        private TextView tvType;
        private TextView tvTime;
        private TextView tvMoney;

    }
}
