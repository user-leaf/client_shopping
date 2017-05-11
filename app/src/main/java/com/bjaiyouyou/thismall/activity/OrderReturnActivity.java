package com.bjaiyouyou.thismall.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bjaiyouyou.thismall.MainApplication;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.OrderDetailAdapter;
import com.bjaiyouyou.thismall.adapter.OrderReturnImageUploadAdapter;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.OrderDetailModel;
import com.bjaiyouyou.thismall.picker.GlideLoader;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.bjaiyouyou.thismall.widget.NoScrollListView;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 申请退款页
 *
 * @author JackB
 * @date 2016/6/14
 * <p/>
 * 本页搜mOrderNumber
 * 不要忘了
 */
public class OrderReturnActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final String TAG = OrderReturnActivity.class.getSimpleName();
    public static final String PARAM_ORDER_NUMBER = "orderNumber";

    private IUUTitleBar mTitleBar;
    // 问题描述
    private EditText mEtProblem;
    // 计数
    private TextView mTvCount;
    // 服务类型
    private RadioGroup mRgServiceType;
    // 上传图片按钮
    private ImageView mIvUpload;
    // 上传图片列表
    private RecyclerView mRecyclerView;
    private ArrayList<String> mPath = new ArrayList<>();
    private OrderReturnImageUploadAdapter mAdapter;
    // 提交
    private Button mBtnCommit;

    // 退货商品列表
    private NoScrollListView mListView;
    private OrderDetailAdapter mOrderAdapter;
    private List<OrderDetailModel.OrderBean.OrderDetailBean> mData;

    // 退款类型
    private int mType;
    // 上一页传过来的订单号
    private String mOrderNumber;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_return);

        initView();
        setupView();
        initCtrl();
        loadData();
    }

    /**
     * 启动本页
     * @param context
     * @param thing
     * @param orderNumber
     */
    public static void actionStart(Context context, List<OrderDetailModel.OrderBean.OrderDetailBean> thing, String orderNumber){
        MainApplication.getInstance().setData(thing);
        Intent intent = new Intent(context, OrderReturnActivity.class);
        intent.putExtra(PARAM_ORDER_NUMBER, orderNumber);
        context.startActivity(intent);
    }

    private void initView() {
        mData = new ArrayList<>();
        mTitleBar = (IUUTitleBar) findViewById(R.id.order_return_title_bar);
        mEtProblem = (EditText) findViewById(R.id.order_return_et_problem);
        mTvCount = (TextView) findViewById(R.id.order_return_tv_count);
        mRgServiceType = (RadioGroup) findViewById(R.id.order_return_rg_service_type);
        mIvUpload = (ImageView) findViewById(R.id.order_return_iv_upload);
        mRecyclerView = (RecyclerView) findViewById(R.id.order_return_recycler);
        mBtnCommit = (Button) findViewById(R.id.order_return_btn_commit);
        mListView = (NoScrollListView) findViewById(R.id.order_return_listview);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        // 计数
        mEtProblem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvCount.setText("" + s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mRgServiceType.setOnCheckedChangeListener(this);
        mIvUpload.setOnClickListener(this);
        mBtnCommit.setOnClickListener(this);
    }

    private void initCtrl() {
        // RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new OrderReturnImageUploadAdapter(this, mPath);
        mRecyclerView.setAdapter(mAdapter);

        // 获取上一页传过来的商品数据集合
        List<OrderDetailModel.OrderBean.OrderDetailBean> thing = (List<OrderDetailModel.OrderBean.OrderDetailBean>) MainApplication.getInstance().getData();
        if (thing != null) {
            mData.addAll(thing);
        }
        mOrderAdapter = new OrderDetailAdapter(this, mData);
        mListView.setAdapter(mOrderAdapter);

    }

    private void loadData() {
        Intent intent = getIntent();
        mOrderNumber = intent.getStringExtra(PARAM_ORDER_NUMBER);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.order_return_rbtn_no_receive: // 未收到货
                mType = 0;
                break;
            case R.id.order_return_rbtn_refund: // 退货退款
                mType = 1;
                break;
        }
    }

    private static final int RC_CAMERA_PERM = 123;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;

            case R.id.order_return_iv_upload: // 选择并上传图片
                /**
                 * Android 6.0 权限检查
                 * https://github.com/googlesamples/easypermissions
                 */
                String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(this, perms)) {
                    // https://github.com/YancyYe/GalleryPick
                    pickPhoto();
                } else {
                    EasyPermissions.requestPermissions(this, "需要开启一些权限",
                            RC_CAMERA_PERM, perms);
                }
                break;

            case R.id.order_return_btn_commit: // 提交
                doCommit();
                break;
        }
    }

    private void pickPhoto() {

        ImageConfig imageConfig
                = new ImageConfig.Builder(
                // GlideLoader 可用自己用的缓存库
                new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
//                        .steepToolBarColor(getResources().getColor(R.color.blue))
                // 标题的背景颜色 （默认黑色）
//                        .titleBgColor(getResources().getColor(R.color.blue))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启多选   （默认为多选）  (单选 为 singleSelect)
//                        .singleSelect()
                .crop()
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(3)
                // 已选择的图片路径
                .pathList(mPath)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                // 开启拍照功能 （默认开启）
                .showCamera()
                .requestCode(REQUEST_CODE)
                .build();


        ImageSelector.open(this, imageConfig);   // 开启图片选择器

    }

    public static final int REQUEST_CODE = 1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            for (String path : pathList) {
                Log.d(TAG, "ImagePathList: " + path);
            }

            mPath.clear();
            mPath.addAll(pathList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void doCommit() {

        if (TextUtils.isEmpty(mEtProblem.getText().toString())) {
            ToastUtils.showShort("请描述问题");
            return;
        }

        if (mEtProblem.getText().toString().length() < 10) {
            ToastUtils.showShort("描述问题，不少于10个字");
            return;
        }

        pd = ProgressDialog.show(this, null, "上传中...");

        final String url = ClientAPI.API_POINT
                + "api/v1/order/applicationForDrawback"
                + "?token=" + CurrentUserManager.getUserToken();

//                mOrderNumber = "2016091310054529 ";

        if (mPath.size() != 0) { // 带上传图片
            // 压缩
            // 文件地址
            final List<File> fileList = new ArrayList<>();

            for (String path : mPath) {
                Luban.get(this)
                        .load(new File(path))
                        .putGear(Luban.THIRD_GEAR)
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                // 压缩开始前调用，可以在方法内启动 loading UI
                            }

                            @Override
                            public void onSuccess(File file) {
                                // 压缩成功后调用，返回压缩后的图片文件
                                // 添加压缩结果
                                fileList.add(file);
                                // 判断压缩是否全部结束
                                if (fileList.size() == mPath.size()) {
                                    uploadFiles(url, fileList);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                // 当压缩过去出现问题时调用

                            }
                        }).launch();
            }
        } else { // 无上传图片
            uploadFiles(url, null);
        }
    }

    /**
     * 进行上传
     *
     * @param url      服务器地址
     * @param fileList 要上传的图片列表
     */
    private void uploadFiles(String url, List<File> fileList) {

        PostFormBuilder postFormBuilder = OkHttpUtils.post()
                .url(url)
                .addParams("order_number", mOrderNumber)
                .addParams("refund_type", "" + mType)
                .addParams("refund_description", mEtProblem.getText().toString());

        LogUtils.d(TAG, "order_number: " + mOrderNumber + ", refund_type: " + mType + ", refund_description: " + mEtProblem.getText().toString());

        int imageLength = mPath.size(); // fileList为null时，imageLength也为0
        for (int i = 0; i < imageLength; i++) {
            LogUtils.d(TAG, "fileName: " + fileList.get(i).getName());
//            postFormBuilder = postFormBuilder.addFile("image_" + i, fileList.get(i).getName(), fileList.get(i));
            postFormBuilder = postFormBuilder.addFile("image_" + i, i + ".jpg", fileList.get(i));
        }
        postFormBuilder.build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }

                ToastUtils.showException(e);

                // 测试
//                        // 跳转到退款进度页
//                        Intent intent = new Intent(OrderReturnActivity.this, OrderReturnDealActivity.class);
//                        intent.putExtra("mOrderNumber", mOrderNumber);
//                        startActivity(intent);
//                        OrderReturnActivity.this.finish();
            }

            @Override
            public void onResponse(String response, int id) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }

                ToastUtils.showShort("上传成功");
                // 跳转到退款进度页
                Intent intent = new Intent(OrderReturnActivity.this, OrderReturnDealActivity.class);
                intent.putExtra("mOrderNumber", mOrderNumber);
                startActivity(intent);
                OrderReturnActivity.this.finish();
            }
        });
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

//        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
//        // This will display a dialog directing them to enable the permission in app settings.
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            new AppSettingsDialog.Builder(this, getString(R.string.rationale_ask_again))
//                    .setTitle(getString(R.string.title_settings_dialog))
//                    .setPositiveButton(getString(R.string.setting))
//                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
//                    .setRequestCode(RC_SETTINGS_SCREEN)
//                    .build()
//                    .show();
//        }
    }


    /**
     * 点击其它区域隐藏软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
