package shop.imake.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import shop.imake.R;
import shop.imake.model.MyIncomeModel;

/**
 * 我的收益（众汇）
 */
public class MyIncomeAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyIncomeModel.PushMoneyDetailsBean> mList;

    public MyIncomeAdapter(Context context, List<MyIncomeModel.PushMoneyDetailsBean> list) {
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
        MyIncomeModel.PushMoneyDetailsBean pushMoneyDetailsBean = mList.get(position);
        if (pushMoneyDetailsBean != null) {
            // “直接联盟商家收益”
            String remarks = pushMoneyDetailsBean.getRemarks();
            holder.tvTitle.setText(remarks == null ? "" : remarks);

            // “共对接X家”
            String word1 = pushMoneyDetailsBean.getWord1();
            int number = pushMoneyDetailsBean.getNumber();
            String word2 = pushMoneyDetailsBean.getWord2();
            holder.tvNum.setText(word1 + number + word2);

            // "对接收益：xx(现金) + xx(众汇券)"
            String word3 = pushMoneyDetailsBean.getWord3();                 // “对接收益”
            String push_money = pushMoneyDetailsBean.getPush_money();       // 现金
            String zhonghuiquan = pushMoneyDetailsBean.getZhonghuiquan();   // 众汇券
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(word3).append(": ")
                    .append(push_money).append("(佣金) +")
                    .append(zhonghuiquan).append("(众汇券)");
            String desc = stringBuilder.toString();
            holder.tvDesc.setText(desc);
        }

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
