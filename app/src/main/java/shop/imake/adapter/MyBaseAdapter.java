package shop.imake.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    private List<T> datas;
    private LayoutInflater inflater;

    public MyBaseAdapter(List<T> datas, Context context) {
        this.datas = datas;
        this.inflater = LayoutInflater.from(context);
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    @Override
    public int getCount() {
        if (datas == null) {
            return 0;
        } else {

            return datas.size();
        }
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(position, convertView, parent);
    }

    public abstract View getItemView(int position, View convertView, ViewGroup parent);

    /**
     * 添加一个集合
     *
     * @param dd
     */
    public void addAll(List<T> dd) {
        if (datas!=null){
            datas.addAll(dd);
            notifyDataSetChanged();
        }
    }

    /**
     * 清空数据源
     */

    public void clear() {
        if (datas != null) {
            datas.clear();
        }

//        notifyDataSetChanged();
    }


}
