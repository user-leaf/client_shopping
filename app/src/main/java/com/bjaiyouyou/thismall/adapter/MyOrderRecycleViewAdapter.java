package com.bjaiyouyou.thismall.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.MainActivity;
import com.bjaiyouyou.thismall.MainApplication;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.OrderReturnActivity;
import com.bjaiyouyou.thismall.activity.OrderReturnDealActivity;
import com.bjaiyouyou.thismall.activity.PermissionsActivity;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.fragment.BaseFragment;
import com.bjaiyouyou.thismall.fragment.MyOrderPaymentFragment;
import com.bjaiyouyou.thismall.model.AddAllToCart;
import com.bjaiyouyou.thismall.model.MyOrder;
import com.bjaiyouyou.thismall.model.PermissionsChecker;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.DialUtils;
import com.bjaiyouyou.thismall.utils.DialogUtils;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.SpaceItemDecoration;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;
import okhttp3.Call;

/**
 * @author QuXinhang
 *         Creare 2016/6/12 18:25
 *         待付款页面适配器
 */
public class MyOrderRecycleViewAdapter extends RecyclerView.Adapter<MyOrderRecycleViewAdapter.ViewHolder> implements View.OnClickListener, OnItemClickListener {
    private Context context;
    private List<MyOrder.DataBean> orders;
    private int days;
    //    未收到货的申请退款
    private OptionPicker picker;
    //    取消订单的对话框
    private AlertDialog.Builder mDialog;
    //    确认收货的对话框
    private AlertDialog.Builder mConfirmDialog;
    //    删除订单的对话框
    private AlertDialog.Builder mDeleteDialog;

    //当前点击角标
    private int nowPosition;
    //获得订单号
    private static String mOrderNumber;
    //获得布局
    private ViewHolder holder = null;
    //支付参数
    private String channel;

    //用于启动Activity
    private Activity activity;

    //用于刷新Ui
    private Handler mHandler;

    //
    private BaseFragment mFragment;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    //布局填充
    private LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public MyOrderRecycleViewAdapter(Context context, BaseFragment fragment, List<MyOrder.DataBean> orders, Activity activity, Handler mHandler) {
        mInflater = LayoutInflater.from(context);
        mPermissionsChecker = new PermissionsChecker(context);
        //初始化权限检查器
        this.context = context;
        this.orders = orders;
        this.activity = activity;
        this.mHandler = mHandler;
        mFragment = fragment;
//        initPicker();
//        initDialog();
//        initConfirmDialog();
//        initDeleteDialog();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private OnItemClickListener mListener;
        private OnItemLongClickListener mLongClickListener;
        //订单状态
        private TextView tvState;
        //订单号
        private TextView tvID;
        //名称
        private TextView tvName;
        //规格
        private TextView tvDimension;
        //单价
        private TextView tvSingleMoney;
        //默认显示商品数量
        private TextView tvNum;
        //商品数量
        private TextView tvGoodsNum;
        //总钱数
        private TextView tvAllMoney;
        //邮费
        private TextView tvTravel;
        //图片
        private ImageView iv;
        //未完成布局
        private LinearLayout llSendAndReceive;
        //申请退款
        private TextView btApplyForRefund;
        //查看物流
        private TextView btCheckTheLogistics;
        //确认收货
        private TextView btconfirmReceipt;
        //完成布局
        private LinearLayout llFinsh;
        //再次购买
        private TextView btReBuy;
        //申请售后
        private TextView btFinshCheckTheLogistics;
        //代付款布局
        private LinearLayout llPay;
        //取消
        private TextView btCancle;
        //去付款
        private TextView btPay;

        private LinearLayout llButton;
        //退款布局
        private LinearLayout llRefund;
        //联系客服
        private TextView btContaCtcustomerService;
        //退款进度
        private TextView btRefundProgress;
        //退款完后布局
        private LinearLayout llREfundFinish;
        //删除
        private TextView btDeleteOrder;
        //订单只有一种商品的布局
        private LinearLayout llSingle;
        //订单有多个商品的布局
        private RecyclerView lvMore;
        //退款详情的按钮
        private TextView btRefundDetail;

        public ViewHolder(View rootView, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
            super(rootView);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            //获得控价
            tvID = (TextView) rootView.findViewById(R.id.tv_myOrder_id);
            tvState = (TextView) rootView.findViewById(R.id.tv_myOrderState);
            tvName = (TextView) rootView.findViewById(R.id.tv_myOrder_name);
            tvDimension = (TextView) rootView.findViewById(R.id.tv_myOrder_dimension);
            tvSingleMoney = (TextView) rootView.findViewById(R.id.tv_myOrder_singleMoney);
            tvNum = (TextView) rootView.findViewById(R.id.tv_myOrder_num);
            tvGoodsNum = (TextView) rootView.findViewById(R.id.tv_myOrder_goodsNum);
            tvAllMoney = (TextView) rootView.findViewById(R.id.tv_myOrder_allMoney);
            tvTravel = (TextView) rootView.findViewById(R.id.tv_myOrder_travelMoney);
            iv = (ImageView) rootView.findViewById(R.id.iv_myOrder_img);

            btCancle = (TextView) rootView.findViewById(R.id.bt_myOrder_cancel);
            btPay = (TextView) rootView.findViewById(R.id.bt_myOrder_pay);

            btApplyForRefund = (TextView) rootView.findViewById(R.id.bt_myOrder_applyForRefund);
            btCheckTheLogistics = (TextView) rootView.findViewById(R.id.bt_myOrder_CheckTheLogistics);
            btconfirmReceipt = (TextView) rootView.findViewById(R.id.bt_myOrder_confirmReceipt);

            btFinshCheckTheLogistics = (TextView) rootView.findViewById(R.id.bt_myOrderFinish_CheckTheLogistics);
            btReBuy = (TextView) rootView.findViewById(R.id.bt_myOrder_repay);

            btContaCtcustomerService = (TextView) rootView.findViewById(R.id.bt_myOrder_contactcustomerservice);
            btRefundProgress = (TextView) rootView.findViewById(R.id.bt_myOrder_refundprogress);

            btDeleteOrder = (TextView) rootView.findViewById(R.id.bt_myOrder_deleteorder);
            btRefundDetail = ((TextView) rootView.findViewById(R.id.bt_myOrder_refund_detaile));


            llPay = (LinearLayout) rootView.findViewById(R.id.ll_myOrderPayment);
            llSendAndReceive = (LinearLayout) rootView.findViewById(R.id.ll_myOrderSendAndReceive);
            llFinsh = (LinearLayout) rootView.findViewById(R.id.ll_myOrderFinish);
//        llButton= (LinearLayout) rootView.findViewById(R.id.ll_myOrder_button);
            llRefund = (LinearLayout) rootView.findViewById(R.id.ll_myorder_refund);
            llREfundFinish = (LinearLayout) rootView.findViewById(R.id.ll_myOrderFinish_refund);

            llSingle = ((LinearLayout) rootView.findViewById(R.id.ll_myorder_single));
            lvMore = (RecyclerView) rootView.findViewById(R.id.lv_myorder_more);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        /**
         * 条目布局添加点击事假
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }
    }


    //    定义对话框--取消订单
    private void initDialog() {
        //处理充值
        Dialog dialog = DialogUtils.createConfirmDialog(context, null, "取消订单后不能恢复，是否取消？", "是", "否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(mOrderNumber)) {
                            //通知服务器取消订单
                            cancleOrder();
                        } else {
                            Toast.makeText(context, "订单取消处理中，不可重复取消", Toast.LENGTH_SHORT).show();
                        }
                        //取消对话框
                        dialog.dismiss();

                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        dialog.show();

    }

    //    定义对话框--确认收货
    private void initConfirmDialog() {

        Dialog dialog = DialogUtils.createConfirmDialog(context, null, "确认收货后不能恢复，是否继续？", "确认", "返回",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(mOrderNumber)) {
                            //通知服务器确认收货
                            confirmReceipt();
                        } else {
                            Toast.makeText(context, "订单确认收货处理中，不可重复确认", Toast.LENGTH_SHORT).show();
                        }
                        //取消对话框
                        dialog.dismiss();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        dialog.show();

    }

    //    定义对话框--删除订单
    private void initDeleteDialog() {

        Dialog dialog = DialogUtils.createConfirmDialog(context, null, "删除订单后不能恢复，是否删除？", "删除", "返回",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(mOrderNumber)) {
                            //通知服务删除
                            deleteOrder();
                        } else {
                            Toast.makeText(context, "订单删除处理中，不可重复删除", Toast.LENGTH_SHORT).show();
                        }
                        //取消对话框
                        dialog.dismiss();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        dialog.show();

    }


    //初始化Picker-申请退款
    private void initPicker() {
        picker = new OptionPicker((Activity) context, new String[]
                {"我不想买了", "信息填写错误重新拍", "其他原因"});
        picker.setOffset(2);
        picker.setSelectedIndex(1);
        picker.setTextSize(20);
        picker.setTextColor(Color.BLACK);
//        处理选择事件
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
//                Toast.makeText(context, option, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, OrderReturnActivity.class);
                intent.putExtra("orderNumber", mOrderNumber);
                MainApplication.getInstance().setData(null);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        //获得被点击按钮所在位置
        nowPosition = (int) v.getTag();
        //当前点击按钮对应的订单号
        mOrderNumber = orders.get(nowPosition).getOrder_number();
        switch (v.getId()) {
            /**
             * 未付款状态：
             * 取消订单
             * 立即付款
             *
             */
            case R.id.bt_myOrder_cancel:
                //将位置的序号设置成按钮的标记，根据标记，移除该数据
                //通知服务器更新数据
                initDialog();
//        initConfirmDialog();
//        initDeleteDialog();

//                mDialog.create().show();
                this.notifyDataSetChanged();
                break;
            case R.id.bt_myOrder_pay:
                //跳转到付款页面
                //通知服务器更新数据

                //调用ping++付款
                // doPayByPingpp();

                //test
                doPayByPingpp();

                //test支付失败
//                orderPayFail();

                break;
            /**
             未完成（未发货）：申请退款      			-- 1：未发货
             未完成（已发货）：申请退款，确认收货		-- 2：已发货
             * 申请退款
             * 查看物流，先去掉
             * 确认收货
             */
            case R.id.bt_myOrder_applyForRefund:
                //跳转到申请退款页面
                //通知服务器更新数据
//                Toast.makeText(context, "申请退款", Toast.LENGTH_SHORT).show();
                //在pick中跳转到退款页面
                initPicker();
                picker.show();
                break;
            case R.id.bt_myOrder_CheckTheLogistics:
                //跳转到查看物流页面
                //通知服务器更新数据
                Toast.makeText(context, "查看物流", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_myOrder_confirmReceipt:
                //跳转到确认收货页面
//                initDialog();
                initConfirmDialog();
//        initDeleteDialog();
//                mConfirmDialog.create().show();
                this.notifyDataSetChanged();
                //通知服务器更新数据
//                confirmReceipt();
//                //页面移除
//                Toast.makeText(context, "确认收货", Toast.LENGTH_SHORT).show();
                break;

            /**
             *未完成（申请退款）：退款进度				-- 3: 申请退款
             * 联系客服 ，先去掉
             * 退款进度
             */
            case R.id.bt_myOrder_contactcustomerservice:
                //直接拨打客服电话
//                callCustomerServerPhone();
                DialUtils.callCentre(activity, DialUtils.CENTER_NUM);
                break;
            case R.id.bt_myOrder_refundprogress:
                //跳转到商品详情页面
//            Toast.makeText(context,"退款进度",Toast.LENGTH_SHORT).show();
                Intent orderReturnIntent = new Intent(context, OrderReturnDealActivity.class);
                orderReturnIntent.putExtra(OrderReturnDealActivity.PARAM_ORDER_NUMBER, mOrderNumber);
                activity.startActivity(orderReturnIntent);

//                v.setEnabled(true);
                break;


            /**
             已完成：再次购买，删除订单				-- 5: 已收货
             * 删除订单
             * 再次购买
             */
            case R.id.bt_myOrderFinish_CheckTheLogistics:
                //跳转到查看物流页面
                //通知服务器更新数据
                //去掉已完成的申请售后
//                Log.e("pay",v.getTag()+"");
//                Toast.makeText(context, "申请售后", Toast.LENGTH_SHORT).show();
//                initDialog();
//        initConfirmDialog();
                initDeleteDialog();

//                mDeleteDialog.create().show();
//                v.setEnabled(true);
                break;
            case R.id.bt_myOrder_repay:
                //跳转到商品详情页面
//                Log.e("pay",v.getTag()+"");
//                Toast.makeText(context, "再次购买", Toast.LENGTH_SHORT).show();
                buyGoodsAgain();
//                v.setEnabled(true);

                //test
//                mDeleteDialog.create().show();
                break;
            /**
             *已完成（退款成功）：删除订单  退款详情	-- 4: 已退款
             * 删除订单
             * 退款详情
             *
             */
            case R.id.bt_myOrder_deleteorder:
                //跳转到确认收货页面
                //通知服务器更新数据
//                Toast.makeText(context, "删除订单", Toast.LENGTH_SHORT).show();
//                initDialog();
//        initConfirmDialog();
                initDeleteDialog();
//                mDeleteDialog.create().show();
//                orders.remove(nowPosition);
//                this.notifyDataSetChanged();
                break;
            case R.id.bt_myOrder_refund_detaile:
                //跳转到退款详情页面
                Intent orderReturnIntent2 = new Intent(context, OrderReturnDealActivity.class);
                orderReturnIntent2.putExtra(OrderReturnDealActivity.PARAM_ORDER_NUMBER, mOrderNumber);
                activity.startActivity(orderReturnIntent2);
                break;

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = mInflater.inflate(R.layout.item_myorder_lv, viewGroup, false);
        holder = new ViewHolder(view, mItemClickListener, mItemLongClickListener);
//        LogUtils.e("onCreateViewHolder",""+position);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //      获得数据对象
        //第一个订单数据
        MyOrder.DataBean item = orders.get(position);
        //判断订单的状态
        int intType = item.getShow_state();
        String type = "";

//        //test,模拟各种的订单类型
//        if (position == 0) {
//            intType = 0;
//            //未付款
//            type = "1";
//        } else if (position == 1) {
//            intType = 1;
//            //待发货
//            type = "2";
//        } else if (position == 2) {
//            intType = 2;
//            //待收货
//            type = "3";
//        } else if (position == 3) {
//            intType = 3;
//            //已收货
//            type = "4";
//        } else if (position == 4) {
//            intType = 5;
//            //退款中
//            type = "5";
//        } else if (position == 5) {
//            intType = 4;
//            //退款成功
//            type = "6";
//        }

        //根据订单的状态，显示操作按钮

        if (intType == 0) {
            //未付款
            type = "1";
            holder.llPay.setVisibility(View.VISIBLE);
            holder.llSendAndReceive.setVisibility(View.GONE);
            holder.llFinsh.setVisibility(View.GONE);
            holder.llRefund.setVisibility(View.GONE);
            holder.llREfundFinish.setVisibility(View.GONE);
        } else if (intType == 1) {
            //待发货
            type = "2";
            holder.btconfirmReceipt.setVisibility(View.GONE);
            holder.llPay.setVisibility(View.GONE);
            holder.llSendAndReceive.setVisibility(View.VISIBLE);
            holder.llFinsh.setVisibility(View.GONE);
            holder.llRefund.setVisibility(View.GONE);
            holder.llREfundFinish.setVisibility(View.GONE);
        } else if (intType == 2) {
            //待收货
            type = "3";
            holder.btconfirmReceipt.setVisibility(View.VISIBLE);
            holder.llPay.setVisibility(View.GONE);
            holder.llSendAndReceive.setVisibility(View.VISIBLE);
            holder.llFinsh.setVisibility(View.GONE);
            holder.llRefund.setVisibility(View.GONE);
            holder.llREfundFinish.setVisibility(View.GONE);
        } else if (intType == 5) {
            //已收货
            type = "4";
            holder.llPay.setVisibility(View.GONE);
            holder.llSendAndReceive.setVisibility(View.GONE);
            holder.llFinsh.setVisibility(View.VISIBLE);
            //去掉已完成的申请售后
            holder.btFinshCheckTheLogistics.setVisibility(View.VISIBLE);
            holder.llRefund.setVisibility(View.GONE);
            holder.llREfundFinish.setVisibility(View.GONE);
        } else if (intType == 3) {
            //退款中
            type = "5";
            //            holder.tvState.setText("退款处理中");
            holder.llPay.setVisibility(View.GONE);
            holder.llSendAndReceive.setVisibility(View.GONE);
            holder.llFinsh.setVisibility(View.GONE);
            holder.llRefund.setVisibility(View.VISIBLE);
            //去掉联系客服的功能
            holder.btContaCtcustomerService.setVisibility(View.GONE);

            holder.llREfundFinish.setVisibility(View.GONE);

        } else if (intType == 4) {
            //退款成功
            type = "6";
            holder.llPay.setVisibility(View.GONE);
            holder.llSendAndReceive.setVisibility(View.GONE);
            holder.llFinsh.setVisibility(View.GONE);
            holder.llRefund.setVisibility(View.GONE);
            holder.llREfundFinish.setVisibility(View.VISIBLE);

        } else if (intType == -1) {
            //数据错误
            type = "7";
        }

        //设置监听，为按钮添加位置标志
        setUpView(holder, position);
        if (item != null) {
            // 向控价内填充数据

            if (item.getOrder_detail() != null && item.getOrder_detail().size() != 0) {
                final List<MyOrder.DataBean.OrderDetailBean> dataBeani = item.getOrder_detail();

                //默认显示第一个商品的信息
                if (item.getOrder_detail().size() == 1) {
                    //显示布局
                    holder.llSingle.setVisibility(View.VISIBLE);
                    holder.lvMore.setVisibility(View.GONE);
//                    MyOrder.DataBean.OrderDetailBean detailBeani = item.getOrder_detail().get(0);
                    final MyOrder.DataBean.OrderDetailBean detailBeani = dataBeani.get(0);
                    //图片
                    if (detailBeani != null) {
                        if (detailBeani.getProduct_image() != null && detailBeani.getProduct_image().getImage_path() != null && detailBeani.getProduct_image().getImage_base_name() != null) {
                            String imgUrl = detailBeani.getProduct_image().getImage_path() + "/" + detailBeani.getProduct_image().getImage_base_name();
                            //获取缩略图地址
//                            imgUrl=ImageUtils.getThumb(imgUrl, holder.iv.getWidth(), 0);
                            Glide.with(context)
                                    .load(imgUrl)
                                    .load(ImageUtils.getThumb(imgUrl, (int) context.getResources().getDimension(R.dimen.width_top_barLarge), 0))
//                                    .placeholder(R.mipmap.list_image_loading)
                                    .error(R.mipmap.list_image_loading)
                                    .into(holder.iv);
                        }
//        Picasso.with(context).load(orders.get(position).getImgUrl()).into(holder.iv);
                        //商品名称
                        if (detailBeani.getProduct() != null && detailBeani.getProduct().getName() != null) {
                            holder.tvName.setText(detailBeani.getProduct().getName());
                        }
                        //单价
                        if (detailBeani.getProduct_size() != null) {
                            String singleMoney = "￥" + detailBeani.getPrice() + "\n+" + detailBeani.getProduct_size().getIntegration_price() + "积分";
                            SpannableStringBuilder builderSingleMoney = new SpannableStringBuilder(singleMoney);
                            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                            ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                            builderSingleMoney.setSpan(redSpan, 0, singleMoney.indexOf("+"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            //改变颜色
                            holder.tvSingleMoney.setText(builderSingleMoney);
                            //规格
                            holder.tvDimension.setText(detailBeani.getProduct_size().getName());
                        }
                        //单件商品的个数
                        holder.tvNum.setText("X" + detailBeani.getNumber());
                    }

                    //添加图片点击事件,跳转到商品详情页面
//                    holder.iv.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            long productId=detailBeani.getProduct().getId();
//                            Intent intent=new Intent(context, GoodsDetailsActivity.class);
//                            intent.putExtra(GoodsDetailsActivity.PARAM_PRODUCT_ID,productId);
//                            context.startActivity(intent);
//                        }
//                    });


                } else {

                    //订单中多个商品图片横线显示
                    holder.llSingle.setVisibility(View.GONE);
                    holder.lvMore.setVisibility(View.VISIBLE);
                    initHorizontalListView(dataBeani, holder.lvMore);
                }

            }

            holder.tvState.setText(item.getShow_state_msg());

            //订单编号
            if (item.getOrder_number() != null) {
                holder.tvID.setText(item.getOrder_number());
            }
            holder.tvGoodsNum.setText("共" + item.getNumber() + "件商品  合计:");
            //总钱数
            String allMoney = null;
            allMoney = "￥" + item.getAll_amount();
            holder.tvAllMoney.setText(allMoney);

//        SpannableStringBuilder builderAllMoney = new SpannableStringBuilder(allMoney);
            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
//        builderAllMoney.setSpan(redSpan,0,allMoney.indexOf("+"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        holder.tvAllMoney.setText(builderAllMoney);
//            holder.tvTravel.setText("+" + item.getDeduct_integration() + "积分（含运费￥"+item.getPostage()+")");
//            if (item.getAmount()<150){
//                holder.tvTravel.setText("+"+item.getDeduct_integration()+"积分（含运费￥20.00)");
//            }else {
//                holder.tvTravel.setText("+"+item.getDeduct_integration()+"积分（包邮)");
//            }
        }

    }


    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }


    /**
     * 设置监听处理
     *
     * @param holder
     * @param position
     */
    private void setUpView(ViewHolder holder, int position) {
        //待付款
//            holder.llPay.setVisibility(View.VISIBLE);
        holder.btPay.setOnClickListener(this);
        holder.btPay.setTag(position);
        holder.btCancle.setOnClickListener(this);
        holder.btCancle.setTag(position);


//            holder.llSendAndReceive.setVisibility(View.VISIBLE);
        holder.btApplyForRefund.setOnClickListener(this);
        holder.btApplyForRefund.setTag(position);
        holder.btCheckTheLogistics.setOnClickListener(this);
        holder.btCheckTheLogistics.setTag(position);
        holder.btconfirmReceipt.setOnClickListener(this);
        holder.btconfirmReceipt.setTag(position);
        holder.btContaCtcustomerService.setOnClickListener(this);
        holder.btContaCtcustomerService.setTag(position);
        holder.btRefundProgress.setOnClickListener(this);
        holder.btRefundProgress.setTag(position);

        holder.btFinshCheckTheLogistics.setOnClickListener(this);
        holder.btFinshCheckTheLogistics.setTag(position);
        holder.btReBuy.setOnClickListener(this);
        holder.btReBuy.setTag(position);
        holder.btDeleteOrder.setOnClickListener(this);
        holder.btDeleteOrder.setTag(position);
        holder.btRefundDetail.setOnClickListener(this);
        holder.btRefundDetail.setTag(position);

    }


    ///////////////////////////////////////网络处理//////////////////////////////

    /**
     * 取消订单
     */
    private void cancleOrder() {
        final String orderNum = orders.get(nowPosition).getOrder_number();
        final String url = ClientAPI.API_POINT + "api/v1/order/cancel/"
                + orderNum
                + "?token="
                + CurrentUserManager.getUserToken();

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        UNNetWorkUtils.unNetWorkOnlyNotify(context, e);
                        LogUtils.e("cancleOrder--E:", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("cancleOrder--", "cancleOrder--" + orderNum);
                        Toast.makeText(context, "订单取消成功", Toast.LENGTH_SHORT).show();
                        //页面移除
                        orders.remove(nowPosition);
                        notifyDataSetChanged();
                        if (getItemCount() == 0) {
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                });
    }

    /**
     * 确认收货
     */
    private void confirmReceipt() {
        final String orderNum = orders.get(nowPosition).getOrder_number();
        final String url = ClientAPI.API_POINT + "api/v1/order/confirm/"
                + orderNum
                + "?token="
                + CurrentUserManager.getUserToken();
        LogUtils.e("url", url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        UNNetWorkUtils.unNetWorkOnlyNotify(context, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("confirmReceipt", "confirmReceipt--" + orderNum);
                        //页面移除
                        orders.remove(nowPosition);
                        notifyDataSetChanged();
                        if (getItemCount() == 0) {
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                });
    }

    /**
     * 删除退款成功的订单
     */
    private void deleteOrder() {
        final String orderNum = orders.get(nowPosition).getOrder_number();
        final String url = ClientAPI.API_POINT + "api/v1/order/delete/"
                + orderNum
                + "?token="
                + CurrentUserManager.getUserToken();
        LogUtils.e("url", url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        UNNetWorkUtils.unNetWorkOnlyNotify(context, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("deleteOrder", "deleteOrder--" + orderNum);
                        //页面移除
                        orders.remove(nowPosition);
                        notifyDataSetChanged();
                        if (getItemCount() == 0) {
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                });
    }

    /**
     * 再次购买
     */
    private void buyGoodsAgain() {
        final String orderNum = orders.get(nowPosition).getOrder_number();

        List<MyOrder.DataBean.OrderDetailBean> products = orders.get(nowPosition).getOrder_detail();
        List<AddAllToCart> newProducts = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < products.size(); i++) {
            AddAllToCart product = new AddAllToCart();
            product.setProduct_id(products.get(i).getProduct_id());
            product.setNumber(products.get(i).getNumber());
            product.setProduct_size_id(products.get(i).getProduct_size_id());
            newProducts.add(product);
        }
        //提交的Json字符串
        final String jsonString = gson.toJson(newProducts);

        //网络添加
        final String url = ClientAPI.API_POINT + "api/v1/shoppingCart/addAll"
                + "?product_list="
                + jsonString
                + "&token="
                + CurrentUserManager.getUserToken();
        LogUtils.e("url", url);
        OkHttpUtils.post()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        UNNetWorkUtils.unNetWorkOnlyNotify(context, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("orderNum", "orderNum--" + orderNum);


                        //开始MainActivity
                        Intent mainIntent = new Intent(context, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        mainIntent.setFlags(100);
                        mainIntent.putExtra("order", "order");
                        activity.startActivity(mainIntent);

//                        activity.finish();
                    }
                });


    }


    ///////////////////////////////////ping++支付
    // 调用ping++去付款
    private void doPayByPingpp() {

        // https://github.com/saiwu-bigkoo/Android-AlertView
        new AlertView("选择支付方式", null, "取消", null, new String[]{"微信支付"}, activity, AlertView.Style.ActionSheet, this).show();

    }

    //  https://github.com/saiwu-bigkoo/Android-AlertView
    // 所需
    @Override
    public void onItemClick(Object o, int position) {
        int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
        switch (position) {
            case 0: // 微信支付
                        new com.bjaiyouyou.thismall.task.PaymentTask(context, mFragment, mOrderNumber, Constants.CHANNEL_WECHAT, holder.btPay, MyOrderPaymentFragment.TAG)
                                .execute(new com.bjaiyouyou.thismall.task.PaymentTask.PaymentRequest(Constants.CHANNEL_WECHAT, 1));
                break;
//            case 1: // 支付宝支付
//                new PaymentTask(mOrderNumber).execute(new PaymentRequest(Constants.CHANNEL_ALIPAY, amount));
//                channel = Constants.CHANNEL_ALIPAY;
//                break;
        }
    }


    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

        private String orderNo;

        public PaymentTask(String orderNo) {
            this.orderNo = orderNo;
        }

        @Override
        protected void onPreExecute() {
            //按键点击之后的禁用，防止重复点击
//            holder.btPay.setOnClickListener(null);
        }

        @Override
        protected String doInBackground(PaymentRequest... pr) {

            PaymentRequest paymentRequest = pr[0];
            String data = null;
            String json = new Gson().toJson(paymentRequest);
            try {
                //向Your Ping++ Server SDK请求数据
//                String URL = Constants.PingppURL+"?token="+CurrentUserManager.getUserToken() + "&orderNo=" + orderNo;

                StringBuilder sb = new StringBuilder(Constants.PingppURL);
                sb.append("?token=").append(CurrentUserManager.getUserToken());
                sb.append("&orderNo=").append(orderNo);
                sb.append("&channel=").append(channel);
                String URL = sb.toString();

                data = postJson(URL, json);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if (null == data) {
                showMsg("请求出错", "请检查URL", "URL无法获取charge");
                return;
            }
            Log.d("charge", data);
//            Pingpp.createPayment(ClientSDKActivity.this, data);
            //QQ钱包调起支付方式  “qwalletXXXXXXX”需与AndroidManifest.xml中的data值一致
            //建议填写规则:qwallet + APP_ID
            Pingpp.createPayment(activity, data, "qwalletXXXXXXX");
//            Pingpp.createPayment(OrderDetailActivity.this, data, "qwalletXXXXXXX");
        }

    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    private static String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(url).post(body).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
//        return "{\"id\":\"ch_ezPGWP0GaXbDDS0ybL8KWDO8\",\"object\":\"charge\",\"created\":1473146951,\"livemode\":true,\"paid\":false,\"refunded\":false,\"app\":\"app_SynjLKu1Si5Czrnz\",\"channel\":\"wx\",\"order_no\":\"2016090550535349\",\"client_ip\":\"106.39.193.120\",\"amount\":1,\"amount_settle\":1,\"currency\":\"cny\",\"subject\":\"orderNo : 2016090550535349\",\"body\":\"adsfadf\",\"extra\":[],\"time_paid\":null,\"time_expire\":1473154151,\"time_settle\":null,\"transaction_no\":null,\"refunds\":{\"object\":\"list\",\"url\":\"1arges_ezPGWP0GaXbDDS0ybL8KWDO8/refunds\",\"has_more\":false,\"data\":[]},\"amount_refunded\":0,\"failure_code\":null,\"failure_msg\":null,\"metadata\":[],\"credential\":{\"object\":\"credential\",\"wx\":{\"appId\":\"wxa4650166adbfdcc1\",\"partnerId\":\"1383715402\",\"prepayId\":\"wx20160906152912ba7e15ede20721592606\",\"nonceStr\":\"5be48dae595399ea42e479c679c26aa8\",\"timeStamp\":1473146952,\"packageValue\":\"Sign=WXPay\",\"sign\":\"5384B61FE9EA36D6D41CCC9A9D7B5816\"}},\"description\":null}";
        return response.body().string();
    }

    class PaymentRequest {
        String channel;
        int amount;

        public PaymentRequest(String channel, int amount) {
            this.channel = channel;
            this.amount = amount;
        }
    }


    ////////////////////////////////////////////处理拨打打电话///////////////////////////////////////////////

    /**
     * 拨打客服电话
     */
    private void callCustomerServerPhone() {
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            LogUtils.e("缺少拨打电话权限", "");
            startPermissionsActivity();
        } else {
            //已经授权直接拨打电话
            DialUtils.callCentre(activity, DialUtils.CENTER_NUM);
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(activity, Constants.CALL_PERMISSIONS_REQUEST_CODE, PERMISSIONS);
    }

    //获得当前订单号
    public static String getOrderNum() {
        return mOrderNumber;
    }


    /**
     * 显示所有订单的图片
     *
     * @param dataBeani
     */
    private void initHorizontalListView(final List<MyOrder.DataBean.OrderDetailBean> dataBeani, RecyclerView lv) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        lv.setLayoutManager(manager);
        lv.addItemDecoration(new SpaceItemDecoration(5, 5, 0, 0));
        MyOrderMoreImagesRecycleViewAdapter adapterImg = new MyOrderMoreImagesRecycleViewAdapter(context, dataBeani);
        adapterImg.setOnItemClickListener(new MyOrderMoreImagesRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtils.showShort("第"+position+"照片");
                //添加点击事件
//                long productId=dataBeani.get(position).getProduct().getId();
//                Intent intent=new Intent(context, GoodsDetailsActivity.class);
//                intent.putExtra(GoodsDetailsActivity.PARAM_PRODUCT_ID,productId);
//                context.startActivity(intent);
            }
        });
        lv.setAdapter(adapterImg);

    }


    /**
     * 追加数据
     *
     * @param dd
     */
    public void addAll(List<MyOrder.DataBean> dd) {
        orders.addAll(dd);
        notifyDataSetChanged();
    }

    /**
     * 清空数据源
     */

    public void clear() {
        orders.clear();
//        notifyDataSetChanged();
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }
}
