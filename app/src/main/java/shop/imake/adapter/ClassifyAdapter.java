package shop.imake.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.model.ClassifyProductModel;
import shop.imake.utils.ImageUtils;
import shop.imake.utils.ScreenUtils;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/28.
 */
public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<ClassifyProductModel.DataBean> mDatas = null;
    private OnItemClickListener mOnItemClickListener = null;

    public ClassifyAdapter(Context context, ArrayList<ClassifyProductModel.DataBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_classify_recyclerview, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(position);

        ClassifyProductModel.DataBean dataBean = mDatas.get(position);
        if (dataBean == null) {
            return;
        }

        ClassifyProductModel.DataBean.ProductBean productBean = dataBean.getProduct();
        if (productBean != null) {
            String productName = productBean.getName();
            viewHolder.mTvTitle.setText(String.valueOf(!TextUtils.isEmpty(productName) ? productName : ""));
            viewHolder.mTvTag.setVisibility(productBean.getRecommend() == 1 ? View.VISIBLE : View.INVISIBLE);
        }

        ClassifyProductModel.DataBean.SizeBean sizeBean = dataBean.getSize();
        if (sizeBean != null) {
            String sizeName = sizeBean.getName();
            viewHolder.mTvContent.setText(!TextUtils.isEmpty(sizeName) ? sizeName : "");
            String price = sizeBean.getPrice();
            viewHolder.mTvPrice.setText(!TextUtils.isEmpty(price) ? "¥" + price : "");
            // 只显示价格20170513
//            int jifen = sizeBean.getIntegration_price();
//            viewHolder.mTvJifen.setText(!TextUtils.isEmpty(String.valueOf(jifen)) ? "+" + jifen + "兑换券" : "");
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

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvImage;
        private TextView mTvTitle;
        private TextView mTvTag;
        private TextView mTvContent;
        private TextView mTvPrice;
        private TextView mTvJifen;

        public ViewHolder(View view) {
            super(view);
            mIvImage = (ImageView) view.findViewById(R.id.classify_recyclerview_item_iv_image);
            mTvTitle = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_title);
            mTvTag = (TextView) view.findViewById(+R.id.classify_recyclerview_item_tv_tag);
            mTvContent = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_content);
            mTvPrice = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_price);
            mTvJifen = (TextView) view.findViewById(R.id.classify_recyclerview_item_tv_jifen);
        }
    }

}
