package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.TaskModel;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

/**
 * （新）任务页 GridView的适配器
 *
 * @author JackB
 * @date 2016/6/16
 */
public class TaskGridViewAdapter extends BaseAdapter {

    private static final java.lang.String TAG = TaskGridViewAdapter.class.getSimpleName();

    private Context mContext;
    private List<TaskModel.DataBean> mList;

    public TaskGridViewAdapter(Context context, List<TaskModel.DataBean> list) {
        mContext = context;
        mList = list;
    }

    /**
     * 设置数据集
     * @param list
     */
    public void setData(List<TaskModel.DataBean> list){
        mList = list;
        notifyDataSetChanged();
    }

    public void addData(List<TaskModel.DataBean> list){
        if (mList != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
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
        return  mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;

        // 1. convertView复用
        if (convertView != null) {
            ret = convertView;
        }else {
            ret = LayoutInflater.from(mContext).inflate(R.layout.item_task_ad,parent,false);
        }

        // 2. 减少findViewById的次数
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.ivPic = (ImageView) ret.findViewById(R.id.task_ad_item_pic);
            holder.tvTitle = (TextView) ret.findViewById(R.id.task_ad_item_title);
            holder.tvLast = (TextView) ret.findViewById(R.id.task_ad_item_last);
            holder.ivComplete = (ImageView) ret.findViewById(R.id.task_ad_item_complete);

            ret.setTag(holder);
        }

        // 3. 展示数据
        TaskModel.DataBean taskAdInfo = mList.get(position);
        String imagepath = taskAdInfo.getImage_path()+ File.separator+taskAdInfo.getImage_base_name();

        if (imagepath != null) {
            Glide.with(mContext).load(imagepath).placeholder(R.mipmap.list_image_loading).into(holder.ivPic);
        }

        holder.tvTitle.setText(taskAdInfo.getName());
        holder.tvLast.setText(taskAdInfo.getTime());
        boolean isComplete = taskAdInfo.isIf_is_complete();

        LogUtils.d(TAG, "isComplete: " + isComplete);

        if (isComplete){ // 已观看
            holder.ivComplete.setVisibility(View.VISIBLE);
        }else {
            holder.ivComplete.setVisibility(View.GONE);
        }

        return ret;
    }

    class ViewHolder{
        // 首图
        private ImageView ivPic;
        // 标题
        private TextView tvTitle;
        // 时长
        private TextView tvLast;
        // 已完成标识
        private ImageView ivComplete;
    }
}
