package shop.imake.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.model.UuDetailModel;

import java.util.List;

/**
 * UU详情适配器
 * @author Alice
 *Creare 2016/12/1 16:56
 */
public class UuDetailAdapter extends MyBaseAdapter<UuDetailModel.DataBean>{
    private Context context;
    private List<UuDetailModel.DataBean> datas;

    public UuDetailAdapter(List<UuDetailModel.DataBean> datas, Context context) {
        super(datas, context);
        this.datas=datas;
        this.context=context;
    }


    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=getInflater().inflate(R.layout.item_withdraw_record_lv,null);
            holder=new ViewHolder();
            holder.tvType= ((TextView) convertView.findViewById(R.id.tv_withdraw_record_type));
            holder.tvTime= ((TextView) convertView.findViewById(R.id.tv_withdraw_record_time));
            holder.tvMoney= ((TextView) convertView.findViewById(R.id.tv_withdraw_record_money));
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        UuDetailModel.DataBean dataBean=datas.get(position);
        if (dataBean!=null){
            //test
            holder.tvMoney.setText(dataBean.getUu()+"UU");

            holder.tvTime.setText(dataBean.getUpdated_at());
            holder.tvType.setText(dataBean.getType());
        }
        return convertView;
    }
    class ViewHolder{
        private TextView tvType;
        private TextView tvTime;
        private TextView tvMoney;

    }
}
