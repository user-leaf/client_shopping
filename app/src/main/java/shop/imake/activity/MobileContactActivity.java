package shop.imake.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.adapter.SortAdapter;
import shop.imake.client.ClientAPI;
import shop.imake.model.ContactMemberModel;
import shop.imake.model.ContactModel;
import shop.imake.pinyin.CharacterParser;
import shop.imake.pinyin.PinyinComparator;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.LogUtils;
import shop.imake.utils.MobileUtils;
import shop.imake.utils.NetStateUtils;
import shop.imake.widget.ClearEditText;
import shop.imake.widget.SideBar;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

/**
 * 手机通讯录朋友页面(旧)
 * @see InviteActivity
 *
 * User: JackB
 * Date: 2016/5/18
 */
public class MobileContactActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MobileContactActivity.class.getSimpleName();

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

    private ProgressDialog pd; // 进度提示框
    private View mBack; // 返回

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 更新列表数据
            adapter.updateDate(mSourceDateList);

            // 进度提示框消失
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        }
    };

    // 断网页
    private View mNoNetView;
    // 未登录页
    private View mNoLoginView;
    // 主页面
    private View mBodyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_contact);

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSourceDateList = new ArrayList<>();

        initView();
        setupView();
        initDate();

        // 显示进度提示框
        pd = ProgressDialog.show(this, null, "加载中...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDate();
    }

    private void initView() {

        sideBar = (SideBar) findViewById(R.id.sidebar);
        dialog = (TextView) findViewById(R.id.dialog);
        sortListView = (ListView) findViewById(R.id.mobile_contact_list);
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        mBack = findViewById(R.id.left_layout);

        mNoNetView = findViewById(R.id.net_fail);
        mNoLoginView = findViewById(R.id.no_login);
        mBodyView = findViewById(R.id.body);
    }

    private void setupView() {
        mBack.setOnClickListener(this);
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

        adapter = new SortAdapter(this, mSourceDateList);
        sortListView.setAdapter(adapter);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initDate() {
        // 获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                MobileUtils mobileUtils = new MobileUtils(MobileContactActivity.this);
                mSourceDateList = mobileUtils.getAllContacts();
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

                LogUtils.d(TAG, "jsonList= " + jsonList);

                String userToken = CurrentUserManager.getUserToken();
                String url = ClientAPI.API_POINT + "api/v1/auth/isMemberByContacts"
                        + "?token=" + userToken;

                if (!"[]".equals(jsonList)) {
                    OkHttpUtils.post()
                            .url(url)
                            .addParams("contacts", jsonList)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
//                                    ToastUtils.showShort("请先登录");
                                    checkNet();
                                    mHandler.sendMessage(Message.obtain());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    mNoNetView.setVisibility(View.GONE);
                                    mNoLoginView.setVisibility(View.GONE);
                                    mBodyView.setVisibility(View.VISIBLE);

                                    LogUtils.d(TAG, response);
                                    Gson gson = new Gson();
                                    ContactMemberModel contactMemberModel = gson.fromJson(response, ContactMemberModel.class);
                                    List<ContactMemberModel.DataBean> data = contactMemberModel.getData();

                                    LogUtils.d(TAG, "data.size() = " + data.size());

                                    // 给数据集赋值，赋是否是会员这个值
                                    for (int i = 0; i < data.size(); i++) {
                                        boolean isRegister = data.get(i).isIsRegister();
                                        mSourceDateList.get(i).setRegister(isRegister);
                                    }

                                    LogUtils.d(TAG, "mSourceDateList = " + mSourceDateList.toString());
                                    // 放在这儿，防止异步请求导致的顺序颠倒
                                    mHandler.sendMessage(Message.obtain());
                                }
                            });
                }
            }
        }).start();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;
        }
    }

    // 检查网络
    private void checkNet() {

        if (!NetStateUtils.isNetworkAvailable(this)) {
            LogUtils.d(TAG, "checkNet  无网");
            mNoNetView.setVisibility(View.VISIBLE);
            mBodyView.setVisibility(View.GONE);

            // 无网络页面中的重新加载按钮
            TextView refreshView = (TextView) mNoNetView.findViewById(R.id.net_fail_refresh);
            refreshView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initDate();
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

        TextView tvGotoLogin = (TextView) findViewById(R.id.no_login_goto);
        tvGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(LoginActivity.class, false);
            }
        });
    }
}
