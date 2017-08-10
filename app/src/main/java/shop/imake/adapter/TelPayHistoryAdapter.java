package shop.imake.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import shop.imake.R;
import shop.imake.model.TelPayHistoryModel;

/**
 * 手机充值历史充值下拉列表
 */

public class TelPayHistoryAdapter extends BaseAdapter implements Filterable {
    private List<TelPayHistoryModel.Bean> modelList;
    private Context mContext;


    public TelPayHistoryAdapter(List<TelPayHistoryModel.Bean> modelList, Context mContext) {
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
            convertView = View.inflate(mContext, R.layout.item_auto_completion_history, null);

            holder.tvClear = (TextView) convertView.findViewById(R.id.tv_tel_pay_history_clear);
            holder.rl = (RelativeLayout) convertView.findViewById(R.id.rl_tel_pay_history);
            holder.tvTelNum = (TextView) convertView.findViewById(R.id.tv_tel_pay_history_num);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_tel_pay_history_name);
            holder.tvLocal = (TextView) convertView.findViewById(R.id.tv_tel_pay_history_local);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //填充数据
        TelPayHistoryModel.Bean model = modelList.get(position);

        //最后一个显示清空历史
        if (position == modelList.size() - 1) {
            holder.tvClear.setVisibility(View.VISIBLE);
            holder.rl.setVisibility(View.GONE);
        } else {
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


    class ViewHolder {
        TextView tvClear;
        RelativeLayout rl;
        TextView tvName;
        TextView tvTelNum;
        TextView tvLocal;
    }

    /////////////重写筛选部分//////////////////////
    @Override
    public Filter getFilter() {
        return modelList == null || modelList.size() <= 0 ? null : new MyFilter();
    }


    /**
     * 数据改变监听接口
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/3/30,9:57
     * <h3>UpdateTime</h3> 2016/3/30,9:57
     * <h3>CreateAuthor</h3> luzhenbang
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     */
    public interface OnDataChangedListener {
        void dataChangedListener(String keyWord);
    }

    /**
     * 设置数据改变监听
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/3/30,10:07
     * <h3>UpdateTime</h3> 2016/3/30,10:07
     * <h3>CreateAuthor</h3> luzhenbang
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param changedListener OnDataChangedListener对象
     */
    private OnDataChangedListener changedListener;

    public void setOnDataChangedListener(OnDataChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return new MyFilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }

        class MyFilterResults extends Filter.FilterResults {

            public MyFilterResults() {
                values = modelList;
                count = modelList.size();
            }
        }
    }
}

