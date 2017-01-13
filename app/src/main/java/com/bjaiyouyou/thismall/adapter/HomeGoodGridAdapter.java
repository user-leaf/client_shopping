package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.model.HomeProductModel;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 首页物品列表适配器
 *
 * @author kanbin
 * @date 2016/7/3
 */
public class HomeGoodGridAdapter extends MyBaseAdapter<HomeProductModel.DataBean> {

    private Context mContext;
    private List<HomeProductModel.DataBean> mList;


    public HomeGoodGridAdapter(List<HomeProductModel.DataBean> datas, Context context) {
        super(datas, context);
        this.mList = datas;
        this.mContext = context;
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
           View ret = null;

           // 1. convertView的复用
           if (convertView != null) {
               ret = convertView;
           } else {
               ret =getInflater().inflate(R.layout.item_home_good_grid, parent, false);
           }

           // 2. 减少findViewById的次数
           ViewHolder holder = (ViewHolder) ret.getTag();
           if (holder == null) {
               holder = new ViewHolder();
               holder.mImageView = (ImageView) ret.findViewById(R.id.home_good_grid_item_iv_pic);
               holder.mTvTitle = (TextView) ret.findViewById(R.id.home_good_grid_item_tv_title);
               holder.mTvPrice = (TextView) ret.findViewById(R.id.home_good_grid_item_tv_price);
               holder.mTvPoints = (TextView) ret.findViewById(R.id.home_good_grid_item_tv_points);

               ret.setTag(holder);
           }

           // 3. 展示数据
           HomeProductModel.DataBean productInfo = mList.get(position);

           if (productInfo!=null){
               if (productInfo.getImage()!=null && productInfo.getImage().getImage_path()!=null&&productInfo.getImage().getImage_base_name()!=null){
                   String imgUrl=productInfo.getImage().getImage_path()+productInfo.getImage().getImage_base_name();
                   if (!TextUtils.isEmpty(imgUrl)){
                       imgUrl=productInfo.getImage().getImage_path()+"/"+productInfo.getImage().getImage_base_name();

                       Glide.with(mContext)
                               .load(ImageUtils.getThumb(imgUrl, ScreenUtils.getScreenWidth(mContext)/2,0))
                               .error(R.mipmap.list_image_loading)
                               .placeholder(R.mipmap.list_image_loading)
                               .into(holder.mImageView);
                   }
               }
               if (productInfo.getName()!=null){
                   holder.mTvTitle.setText(productInfo.getName());
               }
               if (productInfo.getSize()!=null){
                   String price="￥"+productInfo.getSize().getPrice();
                   holder.mTvPrice.setText(price);
                   String integral="+"+productInfo.getSize().getIntegration_price()+"积分";
                   holder.mTvPoints.setText(integral);
               }

//        Log.e("price",""+productInfo.getSize().getPrice());

           }
           return ret;

    }


    class ViewHolder {
        private ImageView mImageView;
        private TextView mTvTitle;
        private TextView mTvPrice;
        private TextView mTvPoints;
    }
}
