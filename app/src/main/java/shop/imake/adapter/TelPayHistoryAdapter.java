package shop.imake.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import shop.imake.R;
import shop.imake.model.TelPayHistoryModel;

/**
 * 手机充值历史充值下拉列表
 */

public class TelPayHistoryAdapter extends BaseAdapter  {
    private List<TelPayHistoryModel> modelList;
    private Context mContext;


    public TelPayHistoryAdapter(List<TelPayHistoryModel> modelList, Context mContext) {
        this.modelList = modelList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,R.layout.item_auto_completion_history, null);

            holder.tvClear = (TextView) convertView.findViewById(R.id.tv_tel_pay_history_clear);
            holder.rl=(RelativeLayout) convertView.findViewById(R.id.rl_tel_pay_history);
            holder.tvTelNum = (TextView) convertView.findViewById(R.id.tv_tel_pay_history_num);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_tel_pay_history_name);
            holder.tvLocal = (TextView) convertView.findViewById(R.id.tv_tel_pay_history_local);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //填充数据
        TelPayHistoryModel model = modelList.get(position);

        //最后一个显示清空历史
        if (position==modelList.size()-1){
            holder.tvClear.setVisibility(View.VISIBLE);
            holder.rl.setVisibility(View.GONE);
        }else {
            holder.tvClear.setVisibility(View.GONE);
            holder.rl.setVisibility(View.VISIBLE);
        }

        if (model != null) {
            holder.tvTelNum.setText(model.getTelNum());
            holder.tvClear.setText(model.getName());
            holder.tvLocal.setText(model.getLocal());
        }

        return convertView;
    }



    class ViewHolder{
        TextView tvClear;
        RelativeLayout rl;
        TextView tvName;
        TextView tvTelNum;
        TextView tvLocal;
    }

}
