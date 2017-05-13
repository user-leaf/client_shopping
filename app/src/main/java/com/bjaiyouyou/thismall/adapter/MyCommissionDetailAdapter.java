package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.MyCommissionModel;

import java.util.List;

/**
 * 众汇券详情adapter
 * Created by Alice
 * 2017/5/12
 */
public class MyCommissionDetailAdapter extends MyBaseAdapter<MyCommissionModel.DataBean>{
    private List<MyCommissionModel.DataBean> mList;
    private Context mContext;

    public MyCommissionDetailAdapter(List<MyCommissionModel.DataBean> datas, Context context) {
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

        MyCommissionModel.DataBean dataBean=mList.get(position);
        if (dataBean!=null){
            //填充数据
//            //判断月份的条目是否显示
//            holder.llType.setVisibility(View.VISIBLE);
//            holder.tvMonth.setText("");
//
//            holder.llType.setVisibility(View.GONE);
            holder.tvType.setText(dataBean.getWord());
            holder.tvTime.setText(dataBean.getCreated_at());
            String money=dataBean.getPush_money();
            //判断金额的颜色
            holder.tvMoney.setText(money);
            if (money!=null){
                if (money.contains("+")){
                    holder.tvMoney.setEnabled(true);
                }else if (money.contains("-")){
                    holder.tvMoney.setEnabled(false);
                }
            }
        }
        return convertView;
    }
    class ViewHolder{
        //月份显示
        private LinearLayout llType;
        //月份显示
        private TextView tvMonth;
        //消费类型
        private TextView tvType;
        //时间
        private TextView tvTime;
        //金额
        private TextView tvMoney;

    }
}
