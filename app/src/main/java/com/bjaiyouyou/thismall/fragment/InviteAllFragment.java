package com.bjaiyouyou.thismall.fragment;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.activity.LoginActivity;
import com.bjaiyouyou.thismall.adapter.SortAdapter;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4Mine;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.ContactMemberModel;
import com.bjaiyouyou.thismall.model.ContactModel;
import com.bjaiyouyou.thismall.pinyin.CharacterParser;
import com.bjaiyouyou.thismall.pinyin.PinyinComparator;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.MobileUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.widget.ClearEditText;
import com.bjaiyouyou.thismall.widget.SideBar;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 邀请成员页--所有联系人
 * <p/>
 * User: JackB
 * Date: 2016/8/24
 */
public class InviteAllFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {

    public static String TAG = InviteAllFragment.class.getSimpleName();

    private ListView sortListView;
    private SideBar sideBar; // 侧边栏索引
    private TextView dialog;  // 目前索引字母
    private SortAdapter adapter;  // 列表适配器
    private ClearEditText mClearEditText;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<ContactModel> mSourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    //    private ProgressDialog pd; // 进度提示框
    private View mBack; // 返回

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 更新列表数据
            adapter.updateDate(mSourceDateList);

            // 进度提示框消失
//            if (pd != null && pd.isShowing()) {
//                pd.dismiss();
//            }
            dismissLoadingDialog();
        }
    };

    // 断网页
    private View mNoNetView;
    // 未登录页
    private View mNoLoginView;
    // 主页面
    private View mBodyView;
    private View mEmptyView;
    private Api4Mine mApi4Mine;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_invite_all, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSourceDateList = new ArrayList<>();

        initView();
        setupView();

        mApi4Mine = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();

    }

    private void initView() {
        sideBar = (SideBar) layout.findViewById(R.id.sidebar);
        dialog = (TextView) layout.findViewById(R.id.dialog);
        sortListView = (ListView) layout.findViewById(R.id.mobile_contact_list);
        mEmptyView = layout.findViewById(R.id.invite_all_empty);
        mClearEditText = (ClearEditText) layout.findViewById(R.id.filter_edit);
        mBack = layout.findViewById(R.id.left_layout);

        mNoNetView = layout.findViewById(R.id.net_fail);
        mNoLoginView = layout.findViewById(R.id.no_login);
        mBodyView = layout.findViewById(R.id.body);
    }

    private void setupView() {
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        adapter = new SortAdapter(getActivity(), mSourceDateList);
        sortListView.setAdapter(adapter);
        sortListView.setEmptyView(mEmptyView);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<ContactModel> filterDateList = new ArrayList<ContactModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mSourceDateList;
        } else {
            filterDateList.clear();
            for (ContactModel contactModel : mSourceDateList) {
                String name = contactModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(contactModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateDate(filterDateList);
    }

    // 检查网络
    private void checkNet() {

        if (!NetStateUtils.isNetworkAvailable(getActivity())) {
            LogUtils.d(TAG, "checkNet  无网");
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
            LogUtils.d(TAG, "checkNet  有网，但未登录");
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
     * 获取通讯录数据
     */
    public void getContact() {

        // 显示进度提示框
//        pd = ProgressDialog.show(getActivity(), null, "加载中...");
        showLoadingDialog();

        // 获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                MobileUtils mobileUtils = new MobileUtils(getActivity());
                mSourceDateList = mobileUtils.getAllContacts();
                LogUtils.d(TAG, "mSourceDateList:" + mSourceDateList.toString());

                if (mSourceDateList.size() != 0) { // 手机没有联系人时会产生异常

                    // 根据a-z进行排序源数据
                    Collections.sort(mSourceDateList, pinyinComparator);

                    // 上传联系人列表 查看是否注册 获取联系人是否已注册列表
                    List<String> list = new ArrayList<String>();
                    for (int i = 0; i < mSourceDateList.size(); i++) {
                        ContactModel contactModel = mSourceDateList.get(i);
                        list.add(contactModel.getTel());
                    }

                    Gson gson = new Gson();
                    String jsonList = gson.toJson(list);

                    if (!"[]".equals(jsonList)) {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("contacts", jsonList);

                        mApi4Mine.getContactsInfo(TAG, params, new DataCallback<ContactMemberModel>(getContext()) {

                            @Override
                            public void onFail(Call call, Exception e, int id) {
                                checkNet();
                                mHandler.sendMessage(Message.obtain());

                            }

                            @Override
                            public void onSuccess(Object response, int id) {
                                LogUtils.d(TAG, "onResponse: " + response);

                                mNoNetView.setVisibility(View.GONE);
                                mNoLoginView.setVisibility(View.GONE);
                                mBodyView.setVisibility(View.VISIBLE);

                                ContactMemberModel contactMemberModel = (ContactMemberModel) response;

                                if (contactMemberModel != null) {
                                    List<ContactMemberModel.DataBean> data = contactMemberModel.getData();

                                    LogUtils.d(TAG, "data.size() = " + data.size());

                                    // 给数据集赋值，赋是否是会员这个值
                                    for (int i = 0; i < data.size(); i++) {
                                        boolean isRegister = data.get(i).isIsRegister();
                                        mSourceDateList.get(i).setRegister(isRegister);
                                    }

                                    LogUtils.d(TAG, "mSourceDateList = " + mSourceDateList.toString());

                                }
                                // 放在这儿，防止异步请求导致的顺序颠倒
                                mHandler.sendMessage(Message.obtain());

                            }
                        });

                    } else {
                        mHandler.sendMessage(Message.obtain());
                    }
                } else {
                    mHandler.sendMessage(Message.obtain());
                }
            }
        }).start();
    }

    // ====新权限判断begin====>>
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
    // ====新权限判断end====<<

}
