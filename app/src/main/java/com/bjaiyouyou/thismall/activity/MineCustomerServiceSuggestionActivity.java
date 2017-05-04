package com.bjaiyouyou.thismall.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.PermissionsChecker;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.DialUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.widget.FlowRadioGroup;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Call;

/**
 * 客服-意见反馈
 *
 * @author kanbin
 * @date 2016/6/16
 */

/**
 * 修改
 *
 * @author QuXinhang
 *         Creare 2016/8/8 9:33
 */
public class MineCustomerServiceSuggestionActivity extends BaseActivity implements TagFlowLayout.OnTagClickListener, View.OnClickListener, TagFlowLayout.OnSelectListener {

    private IUUTitleBar mTitleBar;

    private TagFlowLayout mFlowLayout;
    private String[] mVals;
    private TagAdapter<String> mTagAdapter;
    // 提交
    private Button mBtnCommit;
    private TextView mTvCount;
    private EditText mEtDesc;

    private RelativeLayout mRLCallCentre;   // 呼叫客服中心
    private RelativeLayout mRLToScore;      // 去评分
    private TextView mTvCallCenter;         // 客服电话
    private String mCenterNum;              // 客服电话号码
    //选中的意见类型
    private int mType = 0;
    private RelativeLayout mRLNotLogin;
    private LinearLayout mLLUnNetWork;
    private TextView mTVGoToLogin;
    private LinearLayout mLLLogin;

    ////////////拨打电话权限变量初始化
    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    //意见反馈的容器
    private FlowRadioGroup mRadioGroup;
    private List<RadioButton> mRadioButtons;
    //重新获取数据
    private TextView mTvGetAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_customer_service_suggestion);
        //初始化权限检查器
        mPermissionsChecker = new PermissionsChecker(this);
        initView();
        setupView();
        loadData();
        initCtrl();
        checkNet();
    }

    private void initView() {
        //意见反馈类型
        mRadioGroup = ((FlowRadioGroup) findViewById(R.id.service_suggestion_flow_rg));

        mTitleBar = (IUUTitleBar) findViewById(R.id.service_suggestion_title_bar);
        mFlowLayout = (TagFlowLayout) findViewById(R.id.service_suggestion_flowlayout);
        mBtnCommit = (Button) findViewById(R.id.service_suggestion_btn_commit);
        mEtDesc = (EditText) findViewById(R.id.service_suggestion_et_desc);
        mTvCount = (TextView) findViewById(R.id.service_suggestion_tv_count);
        mRLCallCentre = ((RelativeLayout) findViewById(R.id.rl_call_centre));
        mTvCallCenter = ((TextView) findViewById(R.id.tv_call_number));
        //网络状态处理
        mRLNotLogin = ((RelativeLayout) findViewById(R.id.ll_not_login));
        mTVGoToLogin = ((TextView) findViewById(R.id.tv_goto_login));
        mLLUnNetWork = ((LinearLayout) findViewById(R.id.ll_unnetwork));
        mTvGetAgain = ((TextView) findViewById(R.id.tv_get_data_again));
        mLLLogin = ((LinearLayout) findViewById(R.id.ll_mine_customer_service_login));

    }

    private void setupView() {
        mTVGoToLogin.setOnClickListener(this);

        mTvGetAgain.setOnClickListener(this);

        mTitleBar.setLeftLayoutClickListener(this);
        mFlowLayout.setOnTagClickListener(this);
        mFlowLayout.setOnSelectListener(this);
        mBtnCommit.setOnClickListener(this);
        mEtDesc.addTextChangedListener(new TextWatcher() {
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
        mRLCallCentre.setOnClickListener(this);

        //意见反馈类型修改
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position = checkedId - 1;
                mType = position;
                LogUtils.e("mType", "" + mType);
            }
        });
    }

    private void loadData() {
        mVals = new String[]{"产品种类", "商品品质", "软件功能", "促销活动", "配送服务", "其它问题"};
//        initSize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initSize() {

        mRadioButtons = new ArrayList<>();
        for (int i = 0; i < mVals.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            //代码去掉选项圈
            Bitmap a = null;
            radioButton.setButtonDrawable(new BitmapDrawable(a));
            radioButton.setBackground(getDrawable(R.drawable.selector_goodsdetail_size_item_bg));
            radioButton.setText(mVals[i]);
            //设置radioButton的内外间距
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 20, 20);
            radioButton.setPadding(20, 20, 20, 20);

            mRadioButtons.add(radioButton);
            mRadioGroup.addView(radioButton, params);
        }
        //初始化默认选中状态
        if (mRadioGroup.getChildCount() != 0) {
            mRadioButtons.get(0).setChecked(true);
        }
    }

    private void initCtrl() {
        mTagAdapter = new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater
                        .from(MineCustomerServiceSuggestionActivity.this)
                        .inflate(R.layout.service_suggestion_tv, mFlowLayout, false);
                textView.setText(s);
                return textView;
            }
        };

        mFlowLayout.setAdapter(mTagAdapter);
        // 预设选中
        mTagAdapter.setSelectedList(0);
        mFlowLayout.getChildAt(0).setClickable(true);

    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
//        ToastUtils.showShort(mVals[position]);
        mType = position;
        //添加条件保证必须有一个条目被选
//                view.setEnabled(false);
//                view.setClickable(true);
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (i != position) {
//                        parent.getChildAt(i).setEnabled(true);
                parent.getChildAt(i).setClickable(false);
            } else {
                parent.getChildAt(i).setClickable(true);
            }
        }
//        ToastUtils.showShort(mType+"");
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;
            case R.id.service_suggestion_btn_commit: // 提交
                String content = mEtDesc.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    submitOpinion(content);
                } else {
                    ToastUtils.showShort("小主，意见描述不可为空哦");
                }
                break;

            case R.id.rl_call_centre: // 拨打客服电话
//                callCustomerServerPhone();
                DialUtils.callCentre(this, DialUtils.CENTER_NUM);
                break;

            case R.id.tv_goto_login: // 去登陆
                jump(LoginActivity.class, false);
                break;
            case R.id.tv_get_data_again: // 重新进行网络加载
                checkNet();

                break;
            default:
                return;

        }
    }

    /**
     * 提交反馈意见
     */
    private void submitOpinion(String content) {
        //意见内容

        String token = CurrentUserManager.getUserToken();
        LogUtils.e("submitOpinion", "content:" + content + "type:" + mType);
        ClientAPI.postSubmitOpinion(mType, content, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                checkNet();
//                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
            }

            @Override
            public void onResponse(String response, int id) {
//                Toast.makeText(getApplicationContext(), "意见已经成功提交", Toast.LENGTH_SHORT).show();
                ToastUtils.showShort("提交成功");
                mEtDesc.setText("");
                finish();
            }

        });
    }

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

    /**
     * TagFlowLayout
     *
     * @param selectPosSet
     */
    @Override
    public void onSelected(Set<Integer> selectPosSet) {
    }

    ////////////////////////////////////////////处理拨打打电话

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
            DialUtils.callCentre(this, DialUtils.CENTER_NUM);
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            Toast.makeText(getApplicationContext(), "未授权，不能拨打电话", Toast.LENGTH_SHORT).show();
//            finish();
        } else if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
            DialUtils.callCentre(this, DialUtils.CENTER_NUM);
        }
    }

    /**
     * 网络检查
     */
    public void checkNet(){
        if (!NetStateUtils.isNetworkAvailable(getApplicationContext())) {
            UNNetWorkUtils.isNetHaveConnect(getApplicationContext(), mRLNotLogin, mLLLogin, mLLUnNetWork);
            ToastUtils.showShort("当前网络不可用，请检查网络设置");
        }else {
            mRLNotLogin.setVisibility(View.GONE);
            mLLUnNetWork.setVisibility(View.GONE);
            mLLLogin.setVisibility(View.VISIBLE);
        }
    }
}
