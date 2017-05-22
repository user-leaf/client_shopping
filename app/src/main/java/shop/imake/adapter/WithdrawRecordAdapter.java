package shop.imake.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.model.WithdrawReCordModel;

import java.util.List;

/**
 * 提现记录适配器
 * @author Alice
 *Creare 2016/11/1 16:56
 */
public class WithdrawRecordAdapter extends MyBaseAdapter<WithdrawReCordModel.DataBean>{
    private Context context;
    private List<WithdrawReCordModel.DataBean> datas;

    public WithdrawRecordAdapter(List<WithdrawReCordModel.DataBean> datas, Context context) {
        super(datas, context);
        this.context=context;
        this.datas=datas;
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
        WithdrawReCordModel.DataBean dataBean=datas.get(position);
        if (dataBean!=null){
            holder.tvType.setText(dataBean.getOrder_number());
            holder.tvMoney.setText(dataBean.getDrawings());
            holder.tvTime.setText(dataBean.getCreated_at());
        }
        return convertView;
    }
    class ViewHolder{
        private TextView tvType;
        private TextView tvTime;
        private TextView tvMoney;

    }
}
