package com.bjaiyouyou.thismall.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/28.
 */
public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ViewHolder>{
    public ArrayList<String> datas = null;

    public ClassifyAdapter(ArrayList<String> datas) {
        this.datas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_classify_recyclerview,viewGroup,false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mTvTitle.setText(datas.get(position));
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvImage;
        private TextView mTvTitle;
        private TextView mTvContent;
        private TextView mTvPrice;
        private TextView mTvJifen;

        public ViewHolder(View view){
            super(view);
            mIvImage = (ImageView) view.findViewById(R.id.classify_recyclerview_item_iv_image);
            mTvTitle = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_title);
            mTvContent = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_content);
            mTvPrice = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_price);
            mTvJifen = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_jifen);
        }
    }

}
