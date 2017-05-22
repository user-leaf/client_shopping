package shop.imake.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import shop.imake.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 申请退款页 展示选择的图片的RecyclerView的适配器
 *
 * @author JackB
 * @date 2016/6/15
 */
public class OrderReturnImageUploadAdapter extends RecyclerView.Adapter<OrderReturnImageUploadAdapter.ViewHolder> {


    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<String> result;

    public OrderReturnImageUploadAdapter(Context context, List<String> result) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.result = result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_order_return, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context)
                .load(result.get(position))
                .centerCrop()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.order_return_item_image);
        }

    }

}
