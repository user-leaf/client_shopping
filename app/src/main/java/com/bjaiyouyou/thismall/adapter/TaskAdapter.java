package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.MyTask;

import java.util.List;

/**
 * 
 * @author QuXinhang
 *Creare 2016/6/7 17:33
 * 
 * 任务适配器
 */
public class TaskAdapter extends MyBaseAdapter<MyTask>  {
    private List<MyTask> datas;
    public TaskAdapter(List<MyTask> datas, Context context) {
        super(datas, context);
        this.datas=datas;
    }
    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=getInflater().inflate(R.layout.item_task_lv,null);
            holder.tvName= (TextView) convertView.findViewById(R.id.tv_task_lv);
            holder.tvFinsh= (TextView) convertView.findViewById(R.id.tv_task_lv_finsh);
            holder.iv= (ImageView) convertView.findViewById(R.id.iv_task_lv);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        MyTask task=datas.get(position);
        holder.tvName.setText(task.getName());
        if (task.isFinsh()){
            holder.iv.setEnabled(true);
            holder.tvFinsh.setVisibility(View.VISIBLE);
        }else {
            holder.iv.setEnabled(false);
            holder.tvFinsh.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        ImageView iv;
        TextView tvName;
        TextView tvFinsh;
    }
}
