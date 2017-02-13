package com.bjaiyouyou.thismall.fragment;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.LoginActivity;
import com.bjaiyouyou.thismall.adapter.InviteMineAdapter;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.ContactModel;
import com.bjaiyouyou.thismall.model.InviteMineModel;
import com.bjaiyouyou.thismall.pinyin.CharacterParser;
import com.bjaiyouyou.thismall.pinyin.PinyinComparator;
import com.bjaiyouyou.thismall.utils.DensityUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.PhoneUtils;
import com.bjaiyouyou.thismall.widget.SideBar;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 邀请成员页--我邀请的
 * <p>
 * User: kanbin
 * Date: 2016/8/24
 */
public class InviteMineFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {

    private static final java.lang.String TAG = InviteMineFragment.class.getSimpleName();

    private ListView mListView;
    private InviteMineAdapter mAdapter;

    private SideBar sideBar; // 侧边栏索引
    private TextView dialog;  // 目前索引字母
//    private SortAdapter mAdapter;  // 列表适配器

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<ContactModel> mDataList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    // 断网页
    private View mNoNetView;
    // 未登录页
    private View mNoLoginView;
    // 主页面
    private View mBodyView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 更新列表数据
            mAdapter.updateDate(mDataList);

        }
    };
    private View mEmptyView;
    private TextView mTvVip;

    public InviteMineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_invite_mine, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initVariable();
        initView();
        setupView();
        initCtrl();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void initVariable() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mDataList = new ArrayList<>();
    }

    private void initView() {
        mListView = (ListView) layout.findViewById(R.id.invite_mine_listview);
        mEmptyView = layout.findViewById(R.id.invite_mine_empty);

        sideBar = (SideBar) layout.findViewById(R.id.sidebar);
        dialog = (TextView) layout.findViewById(R.id.dialog);

        mNoNetView = layout.findViewById(R.id.net_fail);
        mNoLoginView = layout.findViewById(R.id.no_login);
        mBodyView = layout.findViewById(R.id.body);

        mTvVip = new TextView(getContext());
        mTvVip.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(getContext(), 40)));
        mTvVip.setPadding(DensityUtils.dp2px(getContext(), 10), 0, 0, 0);
        mTvVip.setGravity(Gravity.CENTER_VERTICAL);
        mTvVip.setTextColor(getResources().getColor(R.color.app_red));
        mListView.addHeaderView(mTvVip);
    }

    private void setupView() {

        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

                                                       @Override
                                                       public void onTouchingLetterChanged(String s) {
                                                           //该字母首次出现的位置
                                                           int position = mAdapter.getPositionForSection(s.charAt(0));
                                                           LogUtils.d(TAG, "position: " + position + ", headerViewCount: " + mListView.getHeaderViewsCount());
                                                           LogUtils.d(TAG, "s.charAt(0): " + s.charAt(0));

                                                           if (position != -1) {
                                                               mListView.setSelection(position + mListView.getHeaderViewsCount());

                                                           }

                                                           if (SideBar.markVip.equals("" + s.charAt(0))) { // 当为"*"时直接滑到顶部
                                                               mListView.setSelection(0);
                                                           }

                                                       }

                                                   }

        );

        mListView.setEmptyView(mEmptyView);
    }

    private void initCtrl() {
        mAdapter = new InviteMineAdapter(getActivity(), mDataList);
//        mAdapter = new SortAdapter(getActivity(), mDataList);
        mListView.setAdapter(mAdapter);
    }

    private static final int RC_CONTACTS_PERM = 123;

    private void loadData() {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_CONTACTS)) {
            getContact();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要开启一些权限",
                    RC_CONTACTS_PERM, Manifest.permission.READ_CONTACTS);
        }

    }

    // 检查网络
    private void checkNet() {

        if (!NetStateUtils.isNetworkAvailable(getActivity())) {
            mNoNetView.setVisibility(View.VISIBLE);
            mBodyView.setVisibility(View.GONE);

            // 无网络页面中的重新加载按钮
            TextView refreshView = (TextView) mNoNetView.findViewById(R.id.net_fail_refresh);
            refreshView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadData();
                }
            });

        } else {
            // 有网但未登录
            mNoNetView.setVisibility(View.GONE);
            mBodyView.setVisibility(View.GONE);

            gotoLogin();
        }

    }

    // 显示去登录页面
    private void gotoLogin() {
        mNoLoginView.setVisibility(View.VISIBLE);

        TextView tvGotoLogin = (TextView) layout.findViewById(R.id.no_login_goto);
        tvGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取数据
     */
    public void getContact() {
        loadingDialog.show();

        ClientAPI.getInviteByMe(TAG, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                checkNet();
                dismissDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                dismissDialog();
                mNoNetView.setVisibility(View.GONE);
                mNoLoginView.setVisibility(View.GONE);
                mBodyView.setVisibility(View.VISIBLE);

                LogUtils.d(TAG, "response:" + response);
                if (!TextUtils.isEmpty(response) && !"[]".equals(response)) {
                    Gson gson = new Gson();
                    InviteMineModel inviteMineModel = gson.fromJson(response, InviteMineModel.class);
                    if (inviteMineModel != null) {
                        PhoneUtils phoneUtils = new PhoneUtils(getContext());
                        List<ContactModel> phoneList = phoneUtils.getPhoneListFromSource(inviteMineModel.getMembers());
                        mDataList.clear();
                        if (phoneList != null) {
                            mDataList.addAll(phoneList);
                            // 根据a-z进行排序源数据
                            Collections.sort(mDataList, pinyinComparator);
                            LogUtils.d(TAG, "phoneList: " + phoneList.toString());
                            mAdapter.notifyDataSetChanged();

                            // 统计会员人数
                            int vipCount = 0;
                            for (ContactModel model : mDataList) {
                                if (model.isVip()) {
                                    vipCount++;
                                }
                            }
                            mTvVip.setText("已有" + vipCount + "人成为会员");
                        }
                    }
                }
            }
        });

    }

    private void dismissDialog() {
        loadingDialog.dismiss();
    }

    // 权限判断begin>>
    // https://github.com/googlesamples/easypermissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        getActivity().finish();
    }
    // 权限判断end<<

}
