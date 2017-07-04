package shop.imake.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;
import shop.imake.Constants;
import shop.imake.R;
import shop.imake.client.ClientAPI;
import shop.imake.model.OrderReturnDealModel;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DialUtils;
import shop.imake.utils.DoubleTextUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.widget.IUUTitleBar;

/**
 * 处理退款申请页、退款进度
 *
 * @author JackB
 * @date 2016/6/20
 */
public class OrderReturnDealActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final java.lang.String TAG = OrderReturnDealActivity.class.getSimpleName();
    public static final String PARAM_ORDER_NUMBER = "mOrderNumber";
    private IUUTitleBar mTitleBar;
    // 退款信息标题栏
    private TextView mTvSheetTitle;
    // 订单编号
    private TextView mTvOrderNum;
    // 申请时间
    private TextView mTvApplyTime;
    // 退款原因
    private TextView mTvCause;
    // 处理方式
    private TextView mTvWay;
    // 退款金额
    private TextView mTvMoney;
    // 客服
    private View mKefuView;
    /**
     * 退款进度
     * 0：退款申请成功
     * 1：退款受理中
     * 2：预计退款成功
     * 3: 失败
     */
    private int mStageFlag = 0;
    // 进度图标
    private ImageView mIv2;
    // 进度文字
    private TextView mTvStage0;
    private TextView mTvStage1;
    private TextView mTvStage2;
    // 时间
    private TextView mTvTime0;
    private TextView mTvTime1;
    private TextView mTvTime2;
    // 进度条
    private ImageView mIvProgress1;
    private static final int RC_CALL_PERM = 123;
    //客服电话
    private String[] mPhones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_return_deal);

        initView();
        setupView();
        loadData();
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.order_return_deal_title_bar);
        mTvSheetTitle = (TextView) findViewById(R.id.order_return_deal_sheet_title);
        mTvOrderNum = (TextView) findViewById(R.id.order_return_deal_tv_order_num);
        mTvApplyTime = (TextView) findViewById(R.id.order_return_deal_tv_apply_time);
        mTvCause = (TextView) findViewById(R.id.order_return_deal_tv_cause);
        mTvWay = (TextView) findViewById(R.id.order_return_deal_tv_way);
        mTvMoney = (TextView) findViewById(R.id.order_return_deal_tv_money);
        mKefuView = findViewById(R.id.order_return_deal_rl_kefu);

        mIv2 = (ImageView) findViewById(R.id.order_return_deal_iv2);
        mIvProgress1 = (ImageView) findViewById(R.id.order_return_deal_progress1);

        mTvStage0 = (TextView) findViewById(R.id.order_return_deal_stage0);
        mTvStage1 = (TextView) findViewById(R.id.order_return_deal_stage1);
        mTvStage2 = (TextView) findViewById(R.id.order_return_deal_stage2);

        mTvTime0 = (TextView) findViewById(R.id.order_return_deal_time0);
        mTvTime1 = (TextView) findViewById(R.id.order_return_deal_time1);
        mTvTime2 = (TextView) findViewById(R.id.order_return_deal_time2);

    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mKefuView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;
            case R.id.order_return_deal_rl_kefu: // 联系客服
                mPhones=DialUtils.getPhoneNum(this,DialUtils.SERVER_PHONE_TYPE);
                if (mPhones.length==0){
                    ToastUtils.showShort("咨询电话加载中...");
                    return;
                }
                new AlertView(DialUtils.SERVER_TITLE, null, "取消", null, mPhones, this, AlertView.Style.ActionSheet, this).show();
                break;
        }
    }

    /**
     * 拨打电话
     */
    private void makeCall() {

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constants.KEFU_TEL));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
    }

    private void loadData() {
        Intent intent = getIntent();
        final String orderNumber = intent.getStringExtra(PARAM_ORDER_NUMBER);

        ClientAPI.getOrderReturn(orderNumber, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
                ToastUtils.showException(e);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "response:" + response);

                if (!TextUtils.isEmpty(response) && !"[]".equals(response)) {
                    Gson gson = new Gson();
                    OrderReturnDealModel orderReturnDealModel = gson.fromJson(response, OrderReturnDealModel.class);

                    // 显示数据
                    if (orderReturnDealModel != null) {
                        //订单编号
                        mTvOrderNum.setText(orderReturnDealModel.getCharge_order_no());

                        // 申请退款时间
                        long createdTime = orderReturnDealModel.getCreated();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                        String time = sdf.format(createdTime * 1000);
                        mTvTime0.setText(time);
                        mTvApplyTime.setText(time);

                        // 退款原因
                        mTvCause.setText(orderReturnDealModel.getDescription());

                        // 处理方式
                        String way = null;
                        switch (orderReturnDealModel.getRefund_type()) {
                            case 0:
                                way = "未收到货";
                                break;

                            case 1:
                                way = "退货退款";
                                break;
                        }
                        mTvWay.setText(way);

                        // 退款金额
                        mTvMoney.setText("¥" + DoubleTextUtils.setDoubleUtils((double) orderReturnDealModel.getAmount() / 100));

                        // 退款状态
                        String status = orderReturnDealModel.getStatus();
                        if ("pending".equals(status)) { // 处理中
                            mStageFlag = 1;
                        } else if ("succeeded".equals(status)) { // 成功
                            mStageFlag = 2;
                        } else if ("failed".equals(status)) { // 失败
                            mStageFlag = 3;
                        }

                        // 更新退款处理状态
                        updateStatus(mStageFlag);
                    }
                }
            }
        });
    }

    /**
     * 更新退款申请状态
     *
     * @param stage 阶段
     */
    private void updateStatus(int stage) {
        switch (stage) {
            case 0: // 退款申请成功
            case 1: // 退款受理中
                mTvStage0.setTextColor(Color.GRAY);
                mTvStage1.setTextColor(getResources().getColor(R.color.app_orange));
                mTvStage2.setTextColor(Color.GRAY);
                break;

            case 2: // 预计退款成功
                mIv2.setImageResource(R.mipmap.icon_refund_sel);
                mIvProgress1.setImageResource(R.mipmap.icon_progressbar_sel);
                mTvStage0.setTextColor(Color.GRAY);
                mTvStage1.setTextColor(Color.GRAY);
                mTvStage2.setTextColor(getResources().getColor(R.color.app_orange));
                // 退款信息列表
                mTvSheetTitle.setText("退款成功");
                break;

            case 3: // 退款失败
                mIv2.setImageResource(R.mipmap.icon_refund_failed);
                mIvProgress1.setImageResource(R.mipmap.icon_progressbar_sel);
                mTvStage0.setTextColor(Color.GRAY);
                mTvStage1.setTextColor(Color.GRAY);
                mTvStage2.setTextColor(getResources().getColor(R.color.app_orange));
                mTvStage2.setText("退款驳回");
                // 退款信息列表
                mTvSheetTitle.setText("退款失败");
                break;

        }
    }


    /**
     * 电话弹出框条目点击
     *
     * @param o
     * @param position
     */
    @Override
    public void onItemClick(Object o, int position) {
        //调用父类的方法给出提示
        super.onItemClick(o, position);
        if (position<0){
            return;
        }

        DialUtils.callCentre(this, mPhones[position]);


    }

}
