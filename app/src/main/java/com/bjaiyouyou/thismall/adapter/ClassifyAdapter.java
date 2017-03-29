package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.ClassifyProductModel;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bjaiyouyou.thismall.utils.StringUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/28.
 */
public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ClassifyProductModel.DataBean> mDatas = null;

    public ClassifyAdapter(Context context, ArrayList<ClassifyProductModel.DataBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_classify_recyclerview, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ClassifyProductModel.DataBean dataBean = mDatas.get(position);
        if (dataBean == null) {
            return;
        }
        String productName = dataBean.getName();
        viewHolder.mTvTitle.setText(String.valueOf(!TextUtils.isEmpty(productName) ? productName : ""));

        ClassifyProductModel.DataBean.SizeBean sizeBean = dataBean.getSize();
        if (sizeBean != null) {
            String sizeName = sizeBean.getName();
            viewHolder.mTvContent.setText(!TextUtils.isEmpty(sizeName) ? sizeName : "");
            String price = sizeBean.getPrice();
            viewHolder.mTvPrice.setText(!TextUtils.isEmpty(price) ? "¥" + price : "");
            int jifen = sizeBean.getIntegration_price();
            viewHolder.mTvJifen.setText(!TextUtils.isEmpty(String.valueOf(jifen)) ? "+" + jifen + "积分" : "");
        }

        ClassifyProductModel.DataBean.ImageBean imageBean = dataBean.getImage();
        if (imageBean != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(imageBean.getImage_path())
                    .append("/")
                    .append(imageBean.getImage_base_name());
            String imgUrl = sb.toString();
            Glide.with(mContext)
                    .load(ImageUtils.getThumb(imgUrl, ScreenUtils.getScreenWidth(mContext) / 3, 0))
//                    .load(imgUrl)
                    .placeholder(R.color.app_gray_dddddd)
                    .error(R.color.app_gray_dddddd)
                    .into(viewHolder.mIvImage);
        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvImage;
        private TextView mTvTitle;
        private TextView mTvContent;
        private TextView mTvPrice;
        private TextView mTvJifen;

        public ViewHolder(View view) {
            super(view);
            mIvImage = (ImageView) view.findViewById(R.id.classify_recyclerview_item_iv_image);
            mTvTitle = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_title);
            mTvContent = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_content);
            mTvPrice = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_price);
            mTvJifen = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_jifen);
        }
    }

}
