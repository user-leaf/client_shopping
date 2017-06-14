package shop.imake.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import shop.imake.R;
import shop.imake.model.MyMine;

/**
 *
 * @author Alice
 *Creare 2016/6/7 14:10
 * 个人页面的适配器
 *
 */
public class MineAdapter extends BaseAdapter {
    private Context context;
    private GridView mGv;
    private List<MyMine> data;

    public static int ROW_NUMBER = 4;

    public MineAdapter(Context context, GridView mGv, List<MyMine> data) {
        this.context = context;
        this.mGv = mGv;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mine_gridview, parent, false);

            //高度计算,使得每个Item都是正方形
            AbsListView.LayoutParams param = new AbsListView.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(param);

            holder.tv= (TextView) convertView.findViewById(R.id.tv_mineGridView);
            holder.iv= (ImageView) convertView.findViewById(R.id.iv_mineGridView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        MyMine myMine=data.get(position);

        if (myMine!=null){
            holder.iv.setImageResource(myMine.getImg());
            holder.tv.setText(myMine.getName());
        }

       return convertView;
    }
    class ViewHolder{
        ImageView iv;
        TextView  tv;
    }

    public void clear(){
        data.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(List<MyMine> newData){
        data.addAll(newData);
        this.notifyDataSetChanged();
    }
}
