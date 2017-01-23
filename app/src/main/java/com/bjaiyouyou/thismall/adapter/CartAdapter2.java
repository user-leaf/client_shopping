package com.bjaiyouyou.thismall.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.GoodsDetailsActivity;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.fragment.CartPage;
import com.bjaiyouyou.thismall.model.CartItem2;
import com.bjaiyouyou.thismall.model.CartModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.List;

import okhttp3.Call;

/**
 * 购物车页 数据列表适配器(适应新的数据类型)
 *
 * @author kanbin
 * @date 2016/6/5
 */
public class CartAdapter2 extends BaseSwipeAdapter implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static final String TAG = CartAdapter2.class.getSimpleName();

    private List<CartItem2> mList;
    private Context mContext;
    private CartPage mCartPage;

    public CartAdapter2(List<CartItem2> list, Context context, CartPage cartPage) {
        mList = list;
        mContext = context;
        mCartPage = cartPage;
    }

    /**
     * 设置数据集
     *
     * @param list
     */
    public void setData(List<CartItem2> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    /**
     * 追加数据集
     *
     * @param list
     */
    public void addData(List<CartItem2> list) {
        if (mList != null) {
            mList.clear();
            mList.addAll(list);
        }
        this.notifyDataSetChanged();
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
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;// 商品id？
    }

    /**
     * BaseSwipeAdapter
     *
     * @param position
     * @return
     */
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sample1;
    }

    /**
     * BaseSwipeAdapter
     *
     * @param position
     * @param parent
     * @return
     */
    @Override
    public View generateView(int position, ViewGroup parent) {
        View ret = null;

        ret = LayoutInflater.from(mContext).inflate(R.layout.item_cart, null);
        SwipeLayout swipeLayout = (SwipeLayout) ret.findViewById(getSwipeLayoutResourceId(position));

        return ret;
    }

    /**
     * BaseSwipeAdapter
     *
     * @param position
     * @param convertView
     */
    @Override
    public void fillValues(int position, View convertView) {

        CheckBox chb = (CheckBox) convertView.findViewById(R.id.cart_item_chb);
        ImageView ivPic = (ImageView) convertView.findViewById(R.id.cart_item_iv_pic);
        TextView tvName = (TextView) convertView.findViewById(R.id.cart_item_tv_name);
        TextView tvType = (TextView) convertView.findViewById(R.id.cart_item_tv_type);
        TextView tvSales = (TextView) convertView.findViewById(R.id.cart_item_tv_sales);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.cart_item_tv_price);
        TextView tvPoints = (TextView) convertView.findViewById(R.id.cart_item_tv_points);
        ImageView ivInc = (ImageView) convertView.findViewById(R.id.cart_item_tv_inc);
        ImageView ivDec = (ImageView) convertView.findViewById(R.id.cart_item_tv_dec);
        TextView tvCount = (TextView) convertView.findViewById(R.id.cart_item_tv_count);
        Button btnClick = (Button) convertView.findViewById(R.id.cart_item_btn_click);
        TextView tvDelete = (TextView) convertView.findViewById(R.id.cart_item_delete);
        View offSaleView = convertView.findViewById(R.id.cart_item_rl_off_sale);
        SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.sample1);


        // 设置选中的事件监听
        chb.setOnCheckedChangeListener(this);
        // +1
        ivInc.setOnClickListener(this);
        // -1
        ivDec.setOnClickListener(this);
        // 点击跳转
        btnClick.setOnClickListener(this);
        // 侧滑删除
        tvDelete.setOnClickListener(this);

        // 3. 显示内容
        swipeLayout.close();

        tvName.setText("--");
        tvType.setText("--");
        tvSales.setText("--");
        tvPrice.setText("--");
        tvPoints.setText("--");
        tvCount.setText("-");
        offSaleView.setVisibility(View.GONE);

        CartItem2 cartItem = mList.get(position);
        if (cartItem != null) {
            CartModel cartModel = cartItem.getCartModel();

            if (cartModel != null) {
                // 是否是抢购商品
                boolean isRushGood = false;

                CartModel.ProductBean product = cartModel.getProduct();
                String productName = "";
                if (product != null) {
                    productName = product.getName();
                    tvName.setText(productName);
//                    isRushGood = product.getProduct_type() == 0 ? true : false;
                    if (product.getProduct_type() == 0) {
                        isRushGood = true;
                    }
                }

                CartModel.ProductSizeBean product_size = cartModel.getProduct_size();
                if (product_size != null) {
                    tvType.setText(product_size.getName());
//                    tvSales.setText("已售出" + cartModel.getProduct_size().getSales_volume() + "件");

                    tvPrice.setText("¥" + product_size.getPrice());

                    // 如果是抢购中商品就设为抢购价+抢购标识
                    if (isRushGood) { // 抢购商品
                        CartModel.ProductSizeBean.ProductTimeFrameBean product_time_frame = product_size.getProduct_time_frame();

                        if (product_time_frame != null) { // 判断抢购商品有没有过期，非抢购商品这个字段为null
                            boolean if_rush_to_purchasing = product_time_frame.isIf_rush_to_purchasing();

                            if (if_rush_to_purchasing && !CartPage.CartHelper.isOffShelf(cartItem)) { // 是抢购商品&&没过抢购期&&不是下架商品
                                // 抢购价
                                tvPrice.setText("¥" + product_size.getRush_price());
                                // 显示抢购标识
                                SpannableStringBuilder spanString = new SpannableStringBuilder("icon");
                                ImageSpan imgSpan = new ImageSpan(mContext, R.mipmap.list_icon_panicpurchase);
                                spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spanString.append(productName);
                                tvName.setText(spanString);
//                                tvName.append(productName); // 不能这样，否则字数过多之后不会显示省略号，要在spanString里设置完文字

                            }
                        }

                    }

                    tvPoints.setText("+" + product_size.getIntegration_price() + "积分");

                }

                // 对下架商品进行处理
                if (CartPage.CartHelper.isOffShelf(cartItem)) {
                    offSaleView.setVisibility(View.VISIBLE);
                } else {
                    offSaleView.setVisibility(View.GONE);
                }

                tvCount.setText(Integer.toString(cartModel.getNumber()));

//                ivPic.setImageResource(R.mipmap.list_image_loading); // 解决删除条目时图片错乱问题
                Glide.with(mContext).load(R.mipmap.list_image_loading).into(ivPic);
                if (cartModel.getProduct_images() != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(cartModel.getProduct_images().getImage_path())
                            .append(File.separator)
                            .append(cartModel.getProduct_images().getImage_base_name());
                    String imageurl = sb.toString();
                    Glide.with(mContext).load(ImageUtils.getThumb(imageurl, ScreenUtils.getScreenWidth(mContext) / 4, 0)).placeholder(R.mipmap.list_image_loading).into(ivPic);
                }
            }
        }
        // 4. 设置带有点击事件的按钮的Tag，用于告诉监听器，到底点击哪一个
        chb.setTag(position);
        ivInc.setTag(position);
        ivDec.setTag(position);
        btnClick.setTag(position);
        tvDelete.setTag(position);

        chb.setChecked(cartItem.isChecked());

    }

//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View ret = null;
//        // 1. convertView复用
//        if (convertView != null) {
//            ret = convertView;
//        } else {
//            ret = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false);
//        }
//
//        // 2. 减少findViewById()的次数
//        ViewHolder holder = (ViewHolder) ret.getTag();
//        if (holder == null) {
//            holder = new ViewHolder();
//            holder.chb = (CheckBox) ret.findViewById(R.id.cart_item_chb);
//            holder.ivPic = (ImageView) ret.findViewById(R.id.cart_item_iv_pic);
//            holder.tvName = (TextView) ret.findViewById(R.id.cart_item_tv_name);
//            holder.tvType = (TextView) ret.findViewById(R.id.cart_item_tv_type);
//            holder.tvSales = (TextView) ret.findViewById(R.id.cart_item_tv_sales);
//            holder.tvPrice = (TextView) ret.findViewById(R.id.cart_item_tv_price);
//            holder.tvPoints = (TextView) ret.findViewById(R.id.cart_item_tv_points);
//            holder.ivInc = (ImageView) ret.findViewById(R.id.cart_item_tv_inc);
//            holder.ivDec = (ImageView) ret.findViewById(R.id.cart_item_tv_dec);
//            holder.tvCount = (TextView) ret.findViewById(R.id.cart_item_tv_count);
//            holder.btnClick = (Button) ret.findViewById(R.id.cart_item_btn_click);
//            holder.tvDelete = (TextView) ret.findViewById(R.id.cart_item_delete);
//            holder.swipeLayout = (SwipeLayout) ret.findViewById(R.id.sample1);
//
//            // 设置选中的事件监听
//            holder.chb.setOnCheckedChangeListener(this);
//            // +1
//            holder.ivInc.setOnClickListener(this);
//            // -1
//            holder.ivDec.setOnClickListener(this);
//            // 点击跳转
//            holder.btnClick.setOnClickListener(this);
//            // 侧滑删除
//            holder.tvDelete.setOnClickListener(this);
//
//            ret.setTag(holder);
//        }
//
//        // 3. 显示内容
//        holder.swipeLayout.close();
//
//        holder.tvName.setText("-商品名称-");
//        holder.tvType.setText("-规格-");
//        holder.tvSales.setText("-销量-");
//        holder.tvPrice.setText("-价格-");
//        holder.tvPoints.setText("-积分-");
//        holder.tvCount.setText("00");
//
//        CartItem2 cartItem = mList.get(position);
//        if (cartItem != null) {
//            CartModel cartModel = cartItem.getCartModel();
//
//            if (cartModel != null) {
//
//                if (cartModel.getProduct() != null) {
//                    holder.tvName.setText(cartModel.getProduct().getName());
//                }
//                if (cartModel.getProduct_size() != null) {
//                    holder.tvType.setText(cartModel.getProduct_size().getName());
//                    holder.tvSales.setText("已售出" + cartModel.getProduct_size().getSales_volume() + "件");
//                    holder.tvPrice.setText("¥" + cartModel.getProduct_size().getPrice());
//                    holder.tvPoints.setText("+" + cartModel.getProduct_size().getIntegration_price() + "积分");
//                }
//                holder.tvCount.setText(Integer.toString(cartModel.getNumber()));
//
//                holder.ivPic.setImageResource(R.mipmap.list_image_loading); // 解决删除条目时图片错位问题
//                if (cartModel.getProduct_images() != null) {
//                    StringBuilder sb = new StringBuilder();
//                    sb.append(cartModel.getProduct_images().getImage_path())
//                            .append(File.separator)
//                            .append(cartModel.getProduct_images().getImage_base_name());
//                    String imageurl = sb.toString();
//                    Glide.with(mContext).load(imageurl).placeholder(R.mipmap.list_image_loading).into(holder.ivPic);
//                }
//            }
//        }
//        // 4. 设置带有点击事件的按钮的Tag，用于告诉监听器，到底点击哪一个
//        holder.chb.setTag(position);
//        holder.ivInc.setTag(position);
//        holder.ivDec.setTag(position);
//        holder.btnClick.setTag(position);
//        holder.tvDelete.setTag(position);
//
//        holder.chb.setChecked(cartItem.isChecked());
//        return ret;
//    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object tag = buttonView.getTag();
        if (tag != null && tag instanceof Integer) {
            Integer it = (Integer) tag;
            CartItem2 cartItem = mList.get(it);
            cartItem.setChecked(isChecked);
        }
        this.notifyDataSetChanged(); //这样可以触发观察者变动价格
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Object tag = v.getTag();

        final Integer position;

        switch (id) {
            case R.id.cart_item_tv_dec: // 减1
                if (tag != null && tag instanceof Integer) {
                    // 利用 Tag 附带 按钮所在的 position 位置
                    position = (Integer) tag;
                    CartItem2 cartItem = mList.get(position);
                    int count = cartItem.getCartModel().getNumber();
                    count--;
                    if (count >= 1) {
                        cartItem.getCartModel().setNumber(count);
                        // 网络请求
                        dealNumberChange(count, cartItem, 0);
                    } else {
                        // 0时删除
                        new AlertDialog.Builder(mContext)
                                .setMessage("是否删除此商品")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        doDelete(position);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();

                    }
                    // 刷新列表
                    this.notifyDataSetChanged();
                }
                break;

            case R.id.cart_item_tv_inc: // 加1
                if (tag != null && tag instanceof Integer) {
                    // 利用 Tag 附带 按钮所在的 position 位置
                    position = (Integer) tag;
                    CartItem2 cartItem = mList.get(position);

                    int count = cartItem.getCartModel().getNumber();
                    count++;
//                    if (count <= cartItem.getCartModel().getProduct_size().getStock()) { // 如果有库存
                    if (count <= 99) { // 如果有库存
                        cartItem.getCartModel().setNumber(count);
                        // 网络请求
                        dealNumberChange(count, cartItem, 1);
                    } else {
                        ToastUtils.showShort("不可再多");
                    }
                }
                // 刷新列表
                this.notifyDataSetChanged();
                break;

            case R.id.cart_item_btn_click:  // 点击跳转
                if (tag != null && tag instanceof Integer) {
                    position = (Integer) tag;
                    CartItem2 item = mList.get(position);
//                    ToastUtils.showShort("位置" + position + "点击跳转:" + item.toString());
                    // 跳转到商品详情页
                    long product_id = item.getCartModel().getProduct_id();
                    Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
                    intent.putExtra("productID", product_id);
                    mContext.startActivity(intent);
                }
                break;

            case R.id.cart_item_delete: // 侧滑删除
                if (tag != null && tag instanceof Integer) {
                    position = (Integer) tag;
                    // 执行删除
                    doDelete(position);
                }
                break;
        }
    }

    /**
     * 条目左滑删除
     *
     * @param position
     */
    private void doDelete(final Integer position) {

        String userToken = CurrentUserManager.getUserToken();

        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/shoppingCart/delete");
        sb.append("?token=").append(userToken);
        sb.append("&product_id=").append(mList.get(position).getCartModel().getProduct_id());
        sb.append("&product_size_id=").append(mList.get(position).getCartModel().getProduct_size_id());

        String url = sb.toString();

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort("删除失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (position <= mList.size() - 1) {
                            mList.remove(mList.get(position));
                        }
                        CartAdapter2.this.notifyDataSetChanged();
                        mCartPage.checkDataSize();
                    }
                });
    }

    /**
     * 数量变化的数据请求
     *
     * @param count    此时数量
     * @param cartItem 要变化的条目
     * @param type     0:-1; 1:+1
     */
    private void dealNumberChange(int count, CartItem2 cartItem, final int type) {
        String token = CurrentUserManager.getUserToken();
        // 数据请求
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/shoppingCart/editNumber");
        sb.append("?token=").append(token);
        sb.append("&number=").append(count);
        sb.append("&product_id=").append(cartItem.getCartModel().getProduct_id());
        sb.append("&product_size_id=").append(cartItem.getCartModel().getProduct_size_id());
        String url = sb.toString();

        LogUtils.d("CartAdapter2", "change number url:" + url);

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        switch (type) {
                            case 0: // -1
//                                ToastUtils.showShort("-1");
                                break;
                            case 1: // +1
//                                ToastUtils.showShort("+1");
                                break;
                        }
                    }
                });


    }

    private class ViewHolder {
        // 选择框
        private CheckBox chb;
        // 商品图片
        private ImageView ivPic;
        // 名称
        private TextView tvName;
        // 规格
        private TextView tvType;
        // 销量
        private TextView tvSales;
        // 价格
        private TextView tvPrice;
        // 积分
        private TextView tvPoints;
        // 数量+1
        private ImageView ivInc;
        // 数量-1
        private ImageView ivDec;
        // 数量显示
        private TextView tvCount;
        // 可点击跳转区域
        private Button btnClick;
        // 侧滑删除
        private TextView tvDelete;
        // 已下架图层
        private View offSaleView;

        // 侧滑布局
        private SwipeLayout swipeLayout;

    }
}
