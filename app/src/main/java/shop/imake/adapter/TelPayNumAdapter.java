package shop.imake.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import shop.imake.R;

/**
 * 手机充值
 */
public class TelPayNumAdapter extends MyBaseAdapter<String> {
    private Context context;
    private List<String> data;
    private boolean isEnabel;

    public static int ROW_NUMBER = 4;

    public TelPayNumAdapter(List<String> datas, Context context,boolean isEnabel) {
        super(datas, context);
        this.context = context;
        this.data = datas;
        this.isEnabel=isEnabel;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(R.layout.item_telephone_fee_charge_pay_num_item, parent, false);

            holder.ll = (LinearLayout) convertView.findViewById(R.id.ll_tel_pay_num);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_tel_pay_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        //字体和背景的显示
        holder.ll.setEnabled(isEnabel);
        holder.tv.setEnabled(isEnabel);
        //填充数据
        String telephonePayNum = data.get(position);

        if (telephonePayNum != null) {
            holder.tv.setText("¥"+telephonePayNum);
        }

        return convertView;
    }

    class ViewHolder {
        LinearLayout ll;
        TextView tv;
    }

}
