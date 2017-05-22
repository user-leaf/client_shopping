package shop.imake.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.model.ExpressDetailModel;
import shop.imake.utils.LogUtils;

import java.util.List;

/**
 * 查看物流的适配器
 * @author Alice
 *Creare 2016/10/25 15:24
 */
public class LogisticsAdapter extends MyBaseAdapter<ExpressDetailModel.DataBean.TracesBean.TraceBean> {
    private List<ExpressDetailModel.DataBean.TracesBean.TraceBean> datas;
    private Context context;
    public LogisticsAdapter(List<ExpressDetailModel.DataBean.TracesBean.TraceBean> datas, Context context) {
        super(datas, context);
        this.datas=datas;
        this.context=context;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=getInflater().inflate(R.layout.item_check_this_logistics_lv,null);
            holder=new ViewHolder();
            holder.iv= ((ImageView) convertView.findViewById(R.id.iv_check_the_logistics_circle));
            holder.tvArrived= ((TextView) convertView.findViewById(R.id.tv_check_this_logistic_address));
            holder.tvTime= ((TextView) convertView.findViewById(R.id.tv_check_this_logistic_time));
            holder.vFirst= ((View) convertView.findViewById(R.id.view_check_the_logistics_line));
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        ExpressDetailModel.DataBean.TracesBean.TraceBean traceBean=datas.get(position);
        if (traceBean!=null){
            //填充数据
            if (position==0){
                holder.iv.setEnabled(false);
                holder.tvArrived.setTextColor(context.getResources().getColor(R.color.app_red));
                holder.vFirst.setBackgroundColor(Color.rgb(255,255,255));
            }else {
                holder.iv.setEnabled(true);
            }
            LogUtils.e("Arrived:",""+traceBean.getStation());
            holder.tvArrived.setText("["+traceBean.getStation()+"]"+"   "+traceBean.getRemark());
            holder.tvTime.setText(traceBean.getTime());
            holder.vFirst.setVisibility(View.VISIBLE);

        }

        return convertView;
    }
    class ViewHolder{
        private ImageView iv;
        private TextView tvArrived;
        private TextView tvTime;
        private View vFirst;
    }
}
