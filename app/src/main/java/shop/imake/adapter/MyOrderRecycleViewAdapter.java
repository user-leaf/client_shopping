package shop.imake.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;
import okhttp3.Call;
import shop.imake.Constants;
import shop.imake.MainActivity;
import shop.imake.MainApplication;
import shop.imake.R;
import shop.imake.activity.MyOrderActivity;
import shop.imake.activity.OrderReturnActivity;
import shop.imake.activity.OrderReturnDealActivity;
import shop.imake.activity.PermissionsActivity;
import shop.imake.client.ClientAPI;
import shop.imake.fragment.BaseFragment;
import shop.imake.fragment.MyOrderPaymentFragment;
import shop.imake.fragment.PayDetailFragment;
import shop.imake.model.AddAllToCart;
import shop.imake.model.MyOrder;
import shop.imake.model.PermissionsChecker;
import shop.imake.task.PaymentTask;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.ACache;
import shop.imake.utils.DialUtils;
import shop.imake.utils.DialogUtils;
import shop.imake.utils.DoubleTextUtils;
import shop.imake.utils.ImageUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.PayUtils;
import shop.imake.utils.SpaceItemDecoration;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UNNetWorkUtils;

/**
 * @author Alice
 *         Creare 2016/6/12 18:25
 *         ????????????????????????
 */
public class MyOrderRecycleViewAdapter extends RecyclerView.Adapter<MyOrderRecycleViewAdapter.ViewHolder> implements View.OnClickListener, OnItemClickListener {
    private Context context;
    private List<MyOrder.DataBean> orders;
    private int days;
    //    ???????????????????????????
    private OptionPicker picker;
    //    ????????????????????????
    private AlertDialog.Builder mDialog;
    //    ????????????????????????
    private AlertDialog.Builder mConfirmDialog;
    //    ????????????????????????
    private AlertDialog.Builder mDeleteDialog;

    //??????????????????
    private int nowPosition;
    //???????????????
    private static String mOrderNumber;
    //????????????
    private ViewHolder holder = null;
    //????????????
    private String mChannel;

    //????????????Activity
    private Activity activity;

    //????????????Ui
    private Handler mHandler;

    //
    private BaseFragment mFragment;

    // ?????????????????????
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET
    };
    private PermissionsChecker mPermissionsChecker; // ???????????????
    //????????????
    private LayoutInflater mInflater;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;
    //????????????????????????
    private double mAmount;
    private Dialog mCancleOrderdialog;//???????????????????????????

    public MyOrderRecycleViewAdapter(Context context, BaseFragment fragment, List<MyOrder.DataBean> orders, Activity activity, Handler mHandler) {
        mInflater = LayoutInflater.from(context);
        mPermissionsChecker = new PermissionsChecker(context);
        //????????????????????????
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
        //????????????
        private TextView tvState;
        //?????????
        private TextView tvID;
        //??????
        private TextView tvName;
        //??????
        private TextView tvDimension;
        //??????
        private TextView tvSingleMoney;
        //????????????????????????
        private TextView tvNum;
        //????????????
        private TextView tvGoodsNum;
        //?????????
        private TextView tvAllMoney;
        //??????
        private TextView tvTravel;
        //??????
        private ImageView iv;
        //???????????????
        private LinearLayout llSendAndReceive;
        //????????????
        private TextView btApplyForRefund;
        //????????????
        private TextView btCheckTheLogistics;
        //????????????
        private TextView btconfirmReceipt;
        //????????????
        private LinearLayout llFinsh;
        //????????????
        private TextView btReBuy;
        //????????????
        private TextView btFinshCheckTheLogistics;
        //???????????????
        private LinearLayout llPay;
        //??????
        private TextView btCancle;
        //?????????
        private TextView btPay;

        private LinearLayout llButton;
        //????????????
        private LinearLayout llRefund;
        //????????????
        private TextView btContaCtcustomerService;
        //????????????
        private TextView btRefundProgress;
        //??????????????????
        private LinearLayout llREfundFinish;
        //??????
        private TextView btDeleteOrder;
        //?????????????????????????????????
        private LinearLayout llSingle;
        //??????????????????????????????
        private RecyclerView lvMore;
        //?????????????????????
        private TextView btRefundDetail;

        public ViewHolder(View rootView, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
            super(rootView);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            //????????????
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
         * ??????????????????????????????
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


    //    ???????????????--????????????
    private void initDialog() {
        if (mCancleOrderdialog!=null&&mCancleOrderdialog.isShowing()){
            mCancleOrderdialog.dismiss();
        }
        //????????????
         mCancleOrderdialog = DialogUtils.createConfirmDialog(context, null, "?????????????????????????????????????????????", "???", "???",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(mOrderNumber)) {
                            //???????????????????????????
                            cancleOrder();
                        } else {
                            Toast.makeText(context, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        //???????????????
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
        mCancleOrderdialog.show();

    }

    //    ???????????????--????????????
    private void initConfirmDialog() {

        Dialog dialog = DialogUtils.createConfirmDialog(context, null, "?????????????????????????????????????????????", "??????", "??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(mOrderNumber)) {
                            //???????????????????????????
                            confirmReceipt();
                        } else {
                            Toast.makeText(context, "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        //???????????????
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

    //    ???????????????--????????????
    private void initDeleteDialog() {

        Dialog dialog = DialogUtils.createConfirmDialog(context, null, "?????????????????????????????????????????????", "??????", "??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(mOrderNumber)) {
                            //??????????????????
                            deleteOrder();
                        } else {
                            Toast.makeText(context, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        //???????????????
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


    //?????????Picker-????????????
    private void initPicker() {
        picker = new OptionPicker((Activity) context, new String[]
                {"???????????????", "???????????????????????????", "????????????"});
        picker.setOffset(1);
        picker.setSelectedIndex(0);
        picker.setTextSize(20);
        picker.setTextColor(Color.BLACK);
//        ??????????????????
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

        //?????????????????????????????????
        nowPosition = (int) v.getTag();


        //????????????????????????????????????
        mOrderNumber = orders.get(nowPosition).getOrder_number();
        switch (v.getId()) {
            /**
             * ??????????????????
             * ????????????
             * ????????????
             *
             */
            case R.id.bt_myOrder_cancel:
                //???????????????????????????????????????????????????????????????????????????
                //???????????????????????????
                initDialog();
//        initConfirmDialog();
//        initDeleteDialog();

//                mDialog.create().show();
                this.notifyDataSetChanged();
                break;
            case R.id.bt_myOrder_pay:
                //?????????????????????
                //???????????????????????????

                //??????ping++??????
                // doPayByPingpp();

                //test
                doPayByPingpp();

                //test????????????
//                orderPayFail();

                break;
            /**
             ???????????????????????????????????????      			-- 1????????????
             ??????????????????????????????????????????????????????		-- 2????????????
             * ????????????
             * ????????????????????????
             * ????????????
             */
            case R.id.bt_myOrder_applyForRefund:
                //???????????????????????????
                //???????????????????????????
//                Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                //???pick????????????????????????
                initPicker();
                picker.show();
                break;
            case R.id.bt_myOrder_CheckTheLogistics:
                //???????????????????????????
                //???????????????????????????
                Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_myOrder_confirmReceipt:
                //???????????????????????????
//                initDialog();
                initConfirmDialog();
//        initDeleteDialog();
//                mConfirmDialog.create().show();
                this.notifyDataSetChanged();
                //???????????????????????????
//                confirmReceipt();
//                //????????????
//                Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                break;

            /**
             *??????????????????????????????????????????				-- 3: ????????????
             * ???????????? ????????????
             * ????????????
             */
            case R.id.bt_myOrder_contactcustomerservice:
                //????????????????????????
//                callCustomerServerPhone();
                DialUtils.callCentre(activity, ACache.get(context).getAsString(DialUtils.PHONE_GET_SAFE_CODE_KEY));
                break;
            case R.id.bt_myOrder_refundprogress:
                //???????????????????????????
//            Toast.makeText(context,"????????????",Toast.LENGTH_SHORT).show();
                Intent orderReturnIntent = new Intent(context, OrderReturnDealActivity.class);
                orderReturnIntent.putExtra(OrderReturnDealActivity.PARAM_ORDER_NUMBER, mOrderNumber);
                activity.startActivity(orderReturnIntent);

//                v.setEnabled(true);
                break;


            /**
             ???????????????????????????????????????				-- 5: ?????????
             * ????????????
             * ????????????
             */
            case R.id.bt_myOrderFinish_CheckTheLogistics:
                //???????????????????????????
                //???????????????????????????
                //??????????????????????????????
//                Log.e("pay",v.getTag()+"");
//                Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
//                initDialog();
//        initConfirmDialog();
                initDeleteDialog();

//                mDeleteDialog.create().show();
//                v.setEnabled(true);
                break;
            case R.id.bt_myOrder_repay:
                //???????????????????????????
//                Log.e("pay",v.getTag()+"");
//                Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
                buyGoodsAgain();
//                v.setEnabled(true);

                //test
//                mDeleteDialog.create().show();
                break;
            /**
             *??????????????????????????????????????????  ????????????	-- 4: ?????????
             * ????????????
             * ????????????
             *
             */
            case R.id.bt_myOrder_deleteorder:
                //???????????????????????????
                //???????????????????????????
//                Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
//                initDialog();
//        initConfirmDialog();
                initDeleteDialog();
//                mDeleteDialog.create().show();
//                orders.remove(nowPosition);
//                this.notifyDataSetChanged();
                break;
            case R.id.bt_myOrder_refund_detaile:
                //???????????????????????????
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

        //      ??????????????????
        //?????????????????????
        MyOrder.DataBean item = orders.get(position);
        //?????????????????????
        int intType = item.getShow_state();
        String type = "";

//        //test,???????????????????????????
//        if (position == 0) {
//            intType = 0;
//            //?????????
//            type = "1";
//        } else if (position == 1) {
//            intType = 1;
//            //?????????
//            type = "2";
//        } else if (position == 2) {
//            intType = 2;
//            //?????????
//            type = "3";
//        } else if (position == 3) {
//            intType = 3;
//            //?????????
//            type = "4";
//        } else if (position == 4) {
//            intType = 5;
//            //?????????
//            type = "5";
//        } else if (position == 5) {
//            intType = 4;
//            //????????????
//            type = "6";
//        }

        //??????????????????????????????????????????

        if (intType == 0) {
            //?????????
            type = "1";
            holder.llPay.setVisibility(View.VISIBLE);
            holder.llSendAndReceive.setVisibility(View.GONE);
            holder.llFinsh.setVisibility(View.GONE);
            holder.llRefund.setVisibility(View.GONE);
            holder.llREfundFinish.setVisibility(View.GONE);
        } else if (intType == 1) {
            //?????????
            type = "2";
            holder.btconfirmReceipt.setVisibility(View.GONE);
            holder.llPay.setVisibility(View.GONE);
            holder.llSendAndReceive.setVisibility(View.VISIBLE);
            holder.llFinsh.setVisibility(View.GONE);
            holder.llRefund.setVisibility(View.GONE);
            holder.llREfundFinish.setVisibility(View.GONE);
        } else if (intType == 2) {
            //?????????
            type = "3";
            holder.btconfirmReceipt.setVisibility(View.VISIBLE);
            holder.llPay.setVisibility(View.GONE);
            holder.llSendAndReceive.setVisibility(View.VISIBLE);
            holder.llFinsh.setVisibility(View.GONE);
            holder.llRefund.setVisibility(View.GONE);
            holder.llREfundFinish.setVisibility(View.GONE);
        } else if (intType == 5) {
            //?????????
            type = "4";
            holder.llPay.setVisibility(View.GONE);
            holder.llSendAndReceive.setVisibility(View.GONE);
            holder.llFinsh.setVisibility(View.VISIBLE);
            //??????????????????????????????
            holder.btFinshCheckTheLogistics.setVisibility(View.VISIBLE);
            holder.llRefund.setVisibility(View.GONE);
            holder.llREfundFinish.setVisibility(View.GONE);
        } else if (intType == 3) {
            //?????????
            type = "5";
            //            holder.tvState.setText("???????????????");
            holder.llPay.setVisibility(View.GONE);
            holder.llSendAndReceive.setVisibility(View.GONE);
            holder.llFinsh.setVisibility(View.GONE);
            holder.llRefund.setVisibility(View.VISIBLE);
            //???????????????????????????
            holder.btContaCtcustomerService.setVisibility(View.GONE);

            holder.llREfundFinish.setVisibility(View.GONE);

        } else if (intType == 4) {
            //????????????
            type = "6";
            holder.llPay.setVisibility(View.GONE);
            holder.llSendAndReceive.setVisibility(View.GONE);
            holder.llFinsh.setVisibility(View.GONE);
            holder.llRefund.setVisibility(View.GONE);
            holder.llREfundFinish.setVisibility(View.VISIBLE);

        } else if (intType == -1) {
            //????????????
            type = "7";
        }

        //??????????????????????????????????????????
        setUpView(holder, position);
        if (item != null) {
            // ????????????????????????

            if (item.getOrder_detail() != null && item.getOrder_detail().size() != 0) {
                final List<MyOrder.DataBean.OrderDetailBean> dataBeani = item.getOrder_detail();

                //????????????????????????????????????
                if (item.getOrder_detail().size() == 1) {
                    //????????????
                    holder.llSingle.setVisibility(View.VISIBLE);
                    holder.lvMore.setVisibility(View.GONE);
//                    MyOrder.DataBean.OrderDetailBean detailBeani = item.getOrder_detail().get(0);
                    final MyOrder.DataBean.OrderDetailBean detailBeani = dataBeani.get(0);
                    //??????
                    if (detailBeani != null) {
                        if (detailBeani.getProduct_image() != null && detailBeani.getProduct_image().getImage_path() != null && detailBeani.getProduct_image().getImage_base_name() != null) {
                            String imgUrl = detailBeani.getProduct_image().getImage_path() + "/" + detailBeani.getProduct_image().getImage_base_name();
                            //?????????????????????
                            //???????????????????????????px???
//                            imgUrl=ImageUtils.getThumb(imgUrl, holder.iv.getWidth(), 0);
                            Glide.with(context)
                                    .load(imgUrl)
                                    .load(ImageUtils.getThumb(imgUrl, (int) context.getResources().getDimension(R.dimen.width_top_barLarge), 0))
//                                    .placeholder(R.mipmap.list_image_loading)
                                    .error(R.mipmap.list_image_loading)
                                    .into(holder.iv);
                        }
//        Picasso.with(context).load(orders.get(position).getImgUrl()).into(holder.iv);
                        //????????????
                        if (detailBeani.getProduct() != null && detailBeani.getProduct().getName() != null) {
                            holder.tvName.setText(detailBeani.getProduct().getName());
                        }
                        //??????
                        if (detailBeani.getProduct_size() != null) {
//                            String singleMoney = "???" + detailBeani.getPrice() + "\n+" + detailBeani.getProduct_size().getIntegration_price() + "?????????";
                            String singleMoney = detailBeani.getPrice() ;
                            double singleMoneys=Double.valueOf(singleMoney);
                            singleMoney= DoubleTextUtils.setDoubleUtils(singleMoneys);

                            SpannableStringBuilder builderSingleMoney = new SpannableStringBuilder(singleMoney);
                            //ForegroundColorSpan ?????????????????????BackgroundColorSpan??????????????????
                            ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
//                            builderSingleMoney.setSpan(redSpan, 0, singleMoney.indexOf("+"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            //????????????
                            holder.tvSingleMoney.setText( "???" +builderSingleMoney);
                            //??????
                            holder.tvDimension.setText(detailBeani.getProduct_size().getName());
                        }
                        //?????????????????????
                        holder.tvNum.setText("X" + detailBeani.getNumber());
                    }

                    //????????????????????????,???????????????????????????
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

                    //???????????????????????????????????????
                    holder.llSingle.setVisibility(View.GONE);
                    holder.lvMore.setVisibility(View.VISIBLE);
                    initHorizontalListView(dataBeani, holder.lvMore);
                }

            }

            holder.tvState.setText(item.getShow_state_msg());

            //????????????
            if (item.getOrder_number() != null) {
                holder.tvID.setText(item.getOrder_number());
            }
            holder.tvGoodsNum.setText("???" + item.getNumber() + "?????????  ??????:");
            //?????????
            String allMoney = null;

            mAmount= item.getAll_amount();

            allMoney = "???" + DoubleTextUtils.setDoubleUtils(mAmount);
            holder.tvAllMoney.setText(allMoney);

            double travelMoney=item.getPostage();

            holder.tvTravel.setText("(????????????"+DoubleTextUtils.setDoubleUtils(travelMoney)+")");

//        SpannableStringBuilder builderAllMoney = new SpannableStringBuilder(allMoney);
            //ForegroundColorSpan ?????????????????????BackgroundColorSpan??????????????????
//        builderAllMoney.setSpan(redSpan,0,allMoney.indexOf("+"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        holder.tvAllMoney.setText(builderAllMoney);
//            holder.tvTravel.setText("+" + item.getDeduct_integration() + "?????????????????????"+item.getPostage()+")");
//            if (item.getAmount()<150){
//                holder.tvTravel.setText("+"+item.getDeduct_integration()+"?????????????????????20.00)");
//            }else {
//                holder.tvTravel.setText("+"+item.getDeduct_integration()+"???????????????)");
//            }
        }

    }


    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }


    /**
     * ??????????????????
     *
     * @param holder
     * @param position
     */
    private void setUpView(ViewHolder holder, int position) {
        //?????????
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


    ///////////////////////////////////////????????????//////////////////////////////

    /**
     * ????????????
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
                        CurrentUserManager.TokenDue(e);
                        UNNetWorkUtils.unNetWorkOnlyNotify(context, e);
                        LogUtils.e("cancleOrder--E:", e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("cancleOrder--", "cancleOrder--" + orderNum);
                        Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
                        //????????????
                        orders.remove(nowPosition);
                        notifyDataSetChanged();
                        if (getItemCount() == 0) {
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                });
    }

    /**
     * ????????????
     */
    private void confirmReceipt() {
        //????????????????????????????????????IndexOutOfBoundsException: Invalid index 0, size is 0
        if (orders.size()!=0&&orders.size()>nowPosition){
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
                            CurrentUserManager.TokenDue(e);
                            UNNetWorkUtils.unNetWorkOnlyNotify(context, e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.e("confirmReceipt", "confirmReceipt--" + orderNum);
                            //????????????
                            orders.remove(nowPosition);
                            notifyDataSetChanged();
                            if (getItemCount() == 0) {
                                mHandler.sendEmptyMessage(1);
                            }
                        }
                    });
        }
    }

    /**
     * ???????????????????????????
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
                        CurrentUserManager.TokenDue(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("deleteOrder", "deleteOrder--" + orderNum);
                        //????????????
                        orders.remove(nowPosition);
                        notifyDataSetChanged();
                        if (getItemCount() == 0) {
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                });
    }

    /**
     * ????????????
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
        //?????????Json?????????
        final String jsonString = gson.toJson(newProducts);

        //????????????
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
                        CurrentUserManager.TokenDue(e);
                        UNNetWorkUtils.unNetWorkOnlyNotify(context, e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("orderNum", "orderNum--" + orderNum);


                        //??????MainActivity
                        Intent mainIntent = new Intent(context, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        mainIntent.setFlags(100);
                        mainIntent.putExtra("order", "order");
                        activity.startActivity(mainIntent);

//                        activity.finish();
                    }
                });


    }


    ///////////////////////////////////ping++??????
    // ??????ping++?????????
    private void doPayByPingpp() {
        // https://github.com/saiwu-bigkoo/Android-AlertView

//        new AlertView("??????????????????", null, "??????", null, new String[]{context.getString(R.string.pay_alipay), context.getString(R.string.pay_balance), context.getString(R.string.pay_hx)
//        }, activity, AlertView.Style.ActionSheet, this).show();

        double amount=0;
        if (orders.get(nowPosition)!=null){
            amount=orders.get(nowPosition).getAll_amount();
        }
        LogUtils.e("amount","position**"+nowPosition+"amount**"+amount);

        PayUtils.pay(MyOrderActivity.mFragmentManager, MyOrderPaymentFragment.TAG, amount, new PayDetailFragment.PayCallback() {
            @Override
            public void onPayCallback(String channel) {
                int amount = 1; // ?????? ???????????????????????????????????????????????????????????????????????????
                new PaymentTask(
                        activity,
                        activity,
                        mOrderNumber,
                        channel,
                        holder.btPay,
                        MyOrderPaymentFragment.TAG
                        ,null
                ).execute(new PaymentTask.PaymentRequest(channel, amount));

            }
        });
    }

    //  https://github.com/saiwu-bigkoo/Android-AlertView
    // ??????
    @Override
    public void onItemClick(Object o, int position) {
        //?????????????????????????????????
//        super.onItemClick(o,position);


        int amount = 1; // ?????? ???????????????????????????????????????????????????????????????????????????
        switch (position) {
            case 0: // ???????????????
                mChannel = Constants.CHANNEL_ALIPAY;
                toPay();
                break;
            case 1: // ????????????
                ToastUtils.showShort("???????????????...");
                break;

            case 2: // ????????????
                ToastUtils.showShort("???????????????...");
                break;
            default:
                return;
        }

    }

    private void toPay() {
        new PaymentTask(context, mFragment, mOrderNumber, mChannel, holder.btPay, MyOrderPaymentFragment.TAG, null)
                .execute(new PaymentTask.PaymentRequest(mChannel, 1));
    }

    ////////////////////////////////////////////?????????????????????///////////////////////////////////////////////

    /**
     * ??????????????????
     */
    private void callCustomerServerPhone() {
        // ???????????????, ????????????????????????
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            LogUtils.e("????????????????????????", "");
            startPermissionsActivity();
        } else {
            //??????????????????????????????
            DialUtils.callCentre(activity, ACache.get(context).getAsString(DialUtils.PHONE_GET_SAFE_CODE_KEY));
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(activity, Constants.CALL_PERMISSIONS_REQUEST_CODE, PERMISSIONS);
    }

    //?????????????????????
    public static String getOrderNum() {
        return mOrderNumber;
    }


    /**
     * ???????????????????????????
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
//                ToastUtils.showShort("???"+position+"??????");
                //??????????????????
//                long productId=dataBeani.get(position).getProduct().getId();
//                Intent intent=new Intent(context, GoodsDetailsActivity.class);
//                intent.putExtra(GoodsDetailsActivity.PARAM_PRODUCT_ID,productId);
//                context.startActivity(intent);
            }
        });
        lv.setAdapter(adapterImg);

    }


    /**
     * ????????????
     *
     * @param dd
     */
    public void addAll(List<MyOrder.DataBean> dd) {
        orders.addAll(dd);
        notifyDataSetChanged();
    }

    /**
     * ???????????????
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
     * ??????Item????????????
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    /**
     * ????????????????????????
     */
    public void payFinshRefreshi() {
        LogUtils.e("????????????","payFinshRefreshi+nowPosition:"+nowPosition);
        //????????????
        orders.remove(nowPosition);
        notifyDataSetChanged();
        if (getItemCount() == 0) {
            mHandler.sendEmptyMessage(1);
        }
    }

}
