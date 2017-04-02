package com.bjaiyouyou.thismall.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.LoginActivity;
import com.bjaiyouyou.thismall.activity.WebShowActivity;
import com.bjaiyouyou.thismall.adapter.TaskGridViewAdapter;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.callback.PingppPayResult;
import com.bjaiyouyou.thismall.client.Api4Mine;
import com.bjaiyouyou.thismall.client.Api4Task;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.SignInInfo;
import com.bjaiyouyou.thismall.model.TaskModel;
import com.bjaiyouyou.thismall.model.User;
import com.bjaiyouyou.thismall.task.PaymentTask;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.widget.NoScrollGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 任务页
 *
 * @author kanbin
 * @date 2016/6/16
 */
public class TaskPage extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, OnItemClickListener {
    public static final String TAG = TaskPage.class.getSimpleName();
    public static final int REQUEST_CODE = 100;
    public static final String INTENT_BROADCAST = "com.bjaiyouyou.thismall.fragment.TaskPage.action.INIT";

    private TaskInitReceiver mTaskInitReceiver;

    private int currentPageNum; // 当前页码
    private boolean currentSignState; // 是否已签到

    private TextView mTvNoNet;  // 无网提示

    // 签到栏
    private TextView mBtnSignIn;        // 签到按钮
    private TextView mTvSignInTotalNum; // 累计签到天数
    private TextView mTvGetGoldToday;   // 签到可领UU
    private TextView mTvSignInContCount;    // 连续签到天数
    private View mSigninNologinView;    // 签到栏未登录
    private View mSigninHasLoginView;   // 签到页已登录
    private View mTvLogin;              // 去登录按钮

    // 会员展示栏
    private TextView mTvVipTip;         // 标题
    private View mVipSyncView;          // 同步积分
    private ImageView mIvVipSync;       // 同步积分按钮图标
    private TextView mTvVipSyncShow;    // 同步积分文字
    private TextView mTvVipRecharge;    // 去激活按钮

    // 视频广告列表
    private PullToRefreshScrollView mScrollView;
    private NoScrollGridView mGridView;
    private TaskGridViewAdapter mAdapter;
    private List<TaskModel.DataBean> mList;

    private Api4Task mApi4Task;
    private Api4Mine mApi4Mine;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_task, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mApi4Task = (Api4Task) ClientApiHelper.getInstance().getClientApi(Api4Task.class);
        mApi4Mine = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);

        initVariable();
        initView();
        setupView();
        initCtrl();
        loadData4Ad(currentPageNum);
        loadData4Page();

        IntentFilter filter = new IntentFilter(INTENT_BROADCAST);
        mTaskInitReceiver = new TaskInitReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mTaskInitReceiver, filter);

    }

    private void initVariable() {
        currentPageNum = 1;
        currentSignState = false;

    }

    @Override
    public void onResume() {
        super.onResume();
//        loadData4Ad(); // 从详情页返回会再次加载列表数据 currentPageNum == 2
//        loadData4Page(); // 从广告详情页返回会有会员开通按钮隐藏的过程，效果不太好。从登录页返回刷新改为startactivityforresult的onactivityresult

        currentPageNum = 1;
        loadData4Ad(currentPageNum);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mTaskInitReceiver);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            loadData4Page();
        }
    }

    private void initView() {
        mList = new ArrayList<>();
        mScrollView = (PullToRefreshScrollView) layout.findViewById(R.id.task_scrollview);
        mGridView = (NoScrollGridView) layout.findViewById(R.id.task_gridview);
        mTvLogin = layout.findViewById(R.id.task_tv_login);
        mBtnSignIn = (TextView) layout.findViewById(R.id.task_rbtn_sign_in);
        mTvSignInTotalNum = (TextView) layout.findViewById(R.id.task_sign_in_all);
        mTvGetGoldToday = (TextView) layout.findViewById(R.id.task_get_gold_today);
        mTvSignInContCount = (TextView) layout.findViewById(R.id.task_tv_continue);
        mTvVipTip = (TextView) layout.findViewById(R.id.task_tv_vip_tip);
        mVipSyncView = layout.findViewById(R.id.task_ll_sync);
        mIvVipSync = (ImageView) layout.findViewById(R.id.task_iv_sync);
        mTvVipSyncShow = (TextView) layout.findViewById(R.id.task_tv_sync_show);
        mTvVipRecharge = (TextView) layout.findViewById(R.id.task_tv_recharge);

        mTvNoNet = (TextView) layout.findViewById(R.id.net_fail);
        mSigninNologinView = layout.findViewById(R.id.task_rl_no_login);
        mSigninHasLoginView = layout.findViewById(R.id.task_fl_has_login);

    }

    private void setupView() {
        // 设置GridView不获取焦点，使ScrollView起始位置从顶部开始
        mGridView.setFocusable(false);
        mGridView.setOnItemClickListener(this);

        mScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // 下拉刷新
                currentPageNum = 1;
                loadData4Ad(currentPageNum);
                loadData4Page();
                checkNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // 上拉加载
                loadData4Ad(currentPageNum);
                LogUtils.d(TAG, "currentPageNum: " + currentPageNum);
            }
        });

        mTvLogin.setOnClickListener(this);
        mBtnSignIn.setOnClickListener(this);
        mVipSyncView.setOnClickListener(this);
        mTvVipRecharge.setOnClickListener(this);

        // init
        mVipSyncView.setClickable(false);
        mVipSyncView.setSelected(false);
        mIvVipSync.setSelected(false);
    }

    private void initCtrl() {
        mAdapter = new TaskGridViewAdapter(getActivity(), mList);
        mGridView.setAdapter(mAdapter);
    }

    /**
     * 加载视频广告列表数据
     *
     * @param pageNum   页码
     */
    private void loadData4Ad(final int pageNum) {

        mApi4Task.getTaskAd(pageNum, new DataCallback<TaskModel>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                closeRefresh();
            }

            @Override
            public void onSuccess(Object response, int id) {

                TaskModel taskModel = (TaskModel) response;
                if (taskModel != null) {

                    if (pageNum == 1) {
                        mList.clear();
                    }
                    mList.addAll(taskModel.getData());
                    mAdapter.notifyDataSetChanged();

                }
                closeRefresh();

                currentPageNum++;
            }
        });

    }


    private void closeRefresh() {
        // 加载完成，关闭刷新
        if (mScrollView.isRefreshing()) {
            mScrollView.onRefreshComplete();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TaskModel.DataBean dataBean = mList.get(position);
        if (dataBean != null) {
            Intent intent = new Intent(getActivity(), WebShowActivity.class);
            StringBuilder stringBuilder = new StringBuilder(ClientAPI.URL_WX_H5);
            stringBuilder.append("task-detail.html?id=")
                    .append(dataBean.getId())
                    .append("&token=")
                    .append(CurrentUserManager.getUserToken())
                    .append("&type=android");
            String strVideoH5Url = stringBuilder.toString();

            intent.putExtra(
                    WebShowActivity.PARAM_URLPATH,
                    strVideoH5Url);
            getActivity().startActivity(intent);
        }

    }

    /**
     * 加载签到信息和会员开通信息
     */
    private void loadData4Page() {
        checkLogin();

        // 获取签到数据
        mApi4Task.getTaskSignInfo(new DataCallback<SignInInfo>(getContext()) {
            @Override
            public void onSuccess(Object response, int id) {
                SignInInfo signInInfo = (SignInInfo) response;
                if (signInInfo != null) {
                    mTvSignInTotalNum.setText("" + signInInfo.getSign_in_number());
                    mTvGetGoldToday.setText("" + signInInfo.getToday_get_gold() + "UU");
                    mTvSignInContCount.setText("" + signInInfo.getSign_in_continuous_number());

                    currentSignState = signInInfo.isIs_sign_in();
                    mBtnSignIn.setSelected(currentSignState);

                }
            }

            @Override
            public void onFail(Call call, Exception e, int id) {
                // 保持原有状态
                mBtnSignIn.setSelected(currentSignState);
                checkNet();
            }
        });

        // 获取用户信息
        mApi4Mine.getUserMessage(TAG, new DataCallback<User>(getContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {

            }

            @Override
            public void onSuccess(Object response, int id) {

                if (response != null) {
                    User user = (User) response;
                    User.MemberBean member = user.getMember();

                    if (member == null) {
                        return;
                    }

                    boolean isVip = (member.getIs_vip() == 2);
                    if (isVip) { // 如果是会员
                        mTvVipTip.setText(R.string.task_vip_yes);
                        mVipSyncView.setClickable(true);
                        mVipSyncView.setSelected(true);
                        mIvVipSync.setSelected(true);
                        mTvVipRecharge.setVisibility(View.GONE);
                    } else { // 非会员
                        mTvVipTip.setText(R.string.task_vip_no);
                        mVipSyncView.setClickable(false);
                        mVipSyncView.setSelected(false);
                        mIvVipSync.setSelected(false);
                        mTvVipRecharge.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    // 断网提示
    private void checkNet() {

        if (!NetStateUtils.isNetworkAvailable(getContext())) { // 无网
            mTvNoNet.setVisibility(View.VISIBLE);
        } else { // 有网
            mTvNoNet.setVisibility(View.GONE);
        }

    }

    // 检查是否登录
    private void checkLogin() {
        if (TextUtils.isEmpty(CurrentUserManager.getUserToken())) {
            // 未登录
            mSigninNologinView.setVisibility(View.VISIBLE);
            mSigninHasLoginView.setVisibility(View.GONE);
        } else {
            // 已登录
            mSigninNologinView.setVisibility(View.GONE);
            mSigninHasLoginView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);

        // 这页没有返回键
        if (!NetStateUtils.isNetworkAvailable(getContext())) {
            ToastUtils.showShort("网络不可用");
            return;
        }

        switch (v.getId()) {

            case R.id.task_tv_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                TaskPage.this.startActivityForResult(intent, REQUEST_CODE);
                break;

            case R.id.task_rbtn_sign_in:

                // 当服务器返回已签到状态为true，或实际已签到，不再继续执行
                if (currentSignState || mBtnSignIn.isSelected()) { // 第2个条件是防止点签到之后再次点击又执行一遍（走onError）
                    return;
                }

                showLoadingDialog();

                String userToken = CurrentUserManager.getUserToken();
                String url = ClientAPI.API_POINT + "api/v1/auth/signInRecord" + "?token=" + userToken;

                // 点击签到
                OkHttpUtils.get()
                        .url(url)
                        .tag(this)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                dismissLoadingDialog();
                                mBtnSignIn.setSelected(currentSignState); // 保持原有状态
                                checkNet();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                mTvNoNet.setVisibility(View.GONE);

                                currentSignState = true;
                                mBtnSignIn.setSelected(true);
//                                mBtnSignIn.setClickable(false); // 改为：如果已签到，则在签到操作之前return掉

                                loadData4Page();

                                dismissLoadingDialog();
                            }
                        });
                break;

            // 同步积分
            case R.id.task_ll_sync:

                if (mTvVipSyncShow.getVisibility() == View.GONE) {
                    // 旋转动画，网络请求结束停止
                    Animation rotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_task_sync_rotate);
                    rotateAnim.setInterpolator(new LinearInterpolator());
                    mIvVipSync.startAnimation(rotateAnim);

                    ClientAPI.taskSync(TAG, new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            String str = "+0积分";
                            showSyncAnim(str);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            String str = "+5万积分";
                            showSyncAnim(str);
                        }
                    });
                }

                break;

            // 去激活
            case R.id.task_tv_recharge:
                new AlertDialog.Builder(getContext())
                        .setMessage("200元开通会员特权")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(CurrentUserManager.getUserToken())) {
                                    startActivityForResult(LoginActivity.class, null, REQUEST_CODE);
                                    return;
                                }

                                new AlertView("选择支付方式", null, "取消", null, new String[]{getString(R.string.pay_alipay), getString(R.string.pay_balance), getString(R.string.pay_hx)}, getContext(), AlertView.Style.ActionSheet, TaskPage.this).show();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;
        }
    }

    /**
     * 显示同步动画
     *
     * @param str 要显示的文字
     */
    private void showSyncAnim(String str) {
        mTvVipSyncShow.setText(str);

        mTvVipSyncShow.setVisibility(View.VISIBLE);

        Animation asyncAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_task_sync);
        asyncAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mTvVipSyncShow.setVisibility(View.GONE);
                mIvVipSync.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mTvVipSyncShow.startAnimation(asyncAnim);
    }

    @Override
    public void onItemClick(Object o, int position) {
        super.onItemClick(o, position);

        if (position < 0){
            return;
        }

        switch (position) {
            case 0: // 支付宝支付
                int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
                String channel = Constants.CHANNEL_ALIPAY;
                new PaymentTask(
                        getActivity(),
                        TaskPage.this,
                        null,
                        channel,
                        mTvVipRecharge,
                        TAG
                ).execute(new PaymentTask.PaymentRequest(channel, amount));

                break;

            case 1: // 余额支付
                break;

            case 2: // 环迅支付
                break;

            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mTvVipRecharge.setOnClickListener(TaskPage.this);
        // 支付页面返回处理
        PingppPayResult.setOnPayResultCallback(requestCode, resultCode, data, new PingppPayResult.OnPayResultCallback() {
            @Override
            public void onPaySuccess() {
                // 刷新页面
                loadData4Page();
            }

            @Override
            public void onPayFail() {
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                ToastUtils.showShort(errorMsg);
            }
        });

        // 从登录页返回
        if (requestCode == REQUEST_CODE) {
            if (resultCode == LoginActivity.RESULT_OK) {
                currentPageNum = 1;
                loadData4Ad(currentPageNum);
                loadData4Page();

            }
        }
    }

    class TaskInitReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 要不要直接initData()
            mTvSignInTotalNum.setText("0");
            mTvGetGoldToday.setText("0UU");
            mTvSignInContCount.setText("0");
            currentSignState = false;
            mBtnSignIn.setSelected(false);
            mTvVipTip.setText(R.string.task_vip_no);
            mVipSyncView.setClickable(false);
            mVipSyncView.setSelected(false);
            mIvVipSync.setSelected(false);
            mTvVipRecharge.setVisibility(View.VISIBLE);

            currentPageNum = 1;
            loadData4Ad(currentPageNum);
        }
    }
}
