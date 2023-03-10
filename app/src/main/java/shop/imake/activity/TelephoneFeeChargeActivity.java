package shop.imake.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import shop.imake.R;
import shop.imake.adapter.TelPayHistoryAdapter;
import shop.imake.adapter.TelPayNumAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.callback.PingppPayResult;
import shop.imake.client.Api4Mine;
import shop.imake.client.ClientApiHelper;
import shop.imake.fragment.PayDetailFragment;
import shop.imake.model.PayResultEvent;
import shop.imake.model.TelPayHistoryModel;
import shop.imake.model.TelPayLocalModel;
import shop.imake.model.TelephonePayNum;
import shop.imake.task.PaymentTask;
import shop.imake.task.TelPayContactTask;
import shop.imake.utils.ContactsUtils;
import shop.imake.utils.DialogUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.PayUtils;
import shop.imake.utils.TelPayHistoryUtils;
import shop.imake.utils.TelPhoneFormatUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.utils.Utility;
import shop.imake.utils.ValidatorsUtils;
import shop.imake.widget.IUUTitleBar;
import shop.imake.widget.NoScrollGridView;

import static shop.imake.utils.ContactsUtils.getPayTelNum;

/**
 * 话费充值页面
 */
public class TelephoneFeeChargeActivity extends BaseActivity {
    public static final String TAG = TelephoneFeeChargeActivity.class.getSimpleName();
    private IUUTitleBar mTitle;
    private AutoCompleteTextView mEtTelNum;//电话号码输入框
    private ImageView mIvDealTelNum;//清空电话号码/获取手机通讯录
    private TextView mTvName;//输入号码姓名
    private TextView mTvLocal;//输入号码归属地
    private NoScrollGridView mGv;//话费选择列表
    private int mLevel = 0;//输入框按钮的显示状态
    public static int CONTACT_REQUESTCODE = 666;//获取通讯录请求码
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 888;//获取通讯录权限的请求码
    private String mTelNum;//输入的电话号码
    //test
//    private String mTelNumself = "18333618642";
    private String mTelNumself;//本机号码
    public static String USER_TELNUM = "usertelephonenum";//用于获取上一页传递过来的数据
    private Api4Mine mApi4Mine;
    private List<String> mTelPayMoneyList; //金额选择数据
    private TelPayNumAdapter mTelPayNumAdapter;//支付金额选择适配器
    private double mPayMoney;//支付金额=
    private List<TelPayHistoryModel.Bean> mHistoryList;//历史充值数据
    private TelPayHistoryAdapter mHistoryAdapter;//历史充值适配器
    public static String AMOUNT = "amount";//吊起支付传参的key
    public static String TEL = "telephone";//吊起支付传参的key
    private boolean isGetContas;//判断是操作通讯录还是清除输入框
    private String mTelNumShow;//输入框显示数据
    private boolean isFromContact;//判读电话号码是不是来自通讯录
    private List<String> mTelNumsOfDilogList;//同一个人有多个电话号码列表
    private String mLocalInput;//输入电话的归属地
    private String mNameInput;//输入姓名
    private String[] mContacts = new String[2];//接收从通讯来的电话号码
    private boolean isHaveLocal = false;//标识是否存在归属地信息

    public static void startAction(Context context, String telNum) {
        Intent intent = new Intent(context, TelephoneFeeChargeActivity.class);
        intent.putExtra(USER_TELNUM, telNum);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_fee_charge);
        EventBus.getDefault().register(this);
        mApi4Mine = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
        mTelPayMoneyList = new ArrayList<>();
        //test
        //获取传递的电话号码
        Intent intent = getIntent();
        mTelNumself = intent.getStringExtra(USER_TELNUM);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
        //申请授权，第一个参数为要申请用户授权的权限；第二个参数为requestCode 必须大于等于0，主要用于回调的时候检测，匹配特定的onRequestPermissionsResult。
        //可以从方法名requestPermissions以及第二个参数看出，是支持一次性申请多个权限的，系统会通过对话框逐一询问用户是否授权。
//            ToastUtils.showShort("请授予通讯录操作权限");
//            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
//
//        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            initView();
            setupView();
            LoadPayNums();
            getHistoryPay();
        }

    }

    /**
     * 获取历史充值数据
     */
    private void getHistoryPay() {
        //获取本地存储的历史充值
        String jsonStrig = TelPayHistoryUtils.getHistoryPay(this, mTelNumself);
        jsonStrig = jsonStrig != null ? jsonStrig : "";
        LogUtils.e("getHistoryPay", jsonStrig);
        //转换成对象
        TelPayHistoryModel model = new Gson().fromJson(jsonStrig, TelPayHistoryModel.class);
        //历史充值
        mHistoryList = new ArrayList<>();
        mHistoryList = model != null ? model.getBeanList() : mHistoryList;
        //实例化购买历史,并处理点击事件的回调方法
        mHistoryAdapter = new TelPayHistoryAdapter(mHistoryList, this, new TelPayHistoryAdapter.OnItemOnItemClick() {
            @Override
            public void setOnItemClick(int position, int count) {
                LogUtils.e("点击了历史" + position);
                //清除历史
                if (position == count - 1) {
                    clearHistory();
                    mEtTelNum.setText("");

                } else {
                    //将选择的电话号码填充在输入框里面
                    String telNum = mHistoryList.get(position).getTelNum();
                    LogUtils.e("telNum", telNum + "");
                    mEtTelNum.setText(telNum);
                }
            }
        });
        mEtTelNum.setAdapter(mHistoryAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initCtlPayMoneyNums(boolean isEnable) {
        if (isEnable) {
            mGv.setOnItemClickListener(mPayFeeOnItemOnClickListener);
        } else {
            mGv.setOnItemClickListener(null);
        }
        mTelPayNumAdapter = new TelPayNumAdapter(mTelPayMoneyList, this, isEnable);
        mGv.setAdapter(mTelPayNumAdapter);
    }

    /**
     * 获取付款金额数据
     */
    private void LoadPayNums() {
        mApi4Mine.getTelePayNums(new DataCallback<TelephonePayNum>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                LoadPayNums();
                if (!NetStateUtils.isNetworkAvailable(getApplicationContext())) {
                    UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
                    return;
                }
                ToastUtils.showException(e);
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response == null) {
                    return;
                }
                TelephonePayNum telephonePayNum = (TelephonePayNum) response;
                mTelPayMoneyList = telephonePayNum.getTops();
                initCtlPayMoneyNums(true);
            }
        });

    }


    private void initView() {
        mTitle = ((IUUTitleBar) findViewById(R.id.title_telephone_pee_charge));
        mEtTelNum = ((AutoCompleteTextView) findViewById(R.id.et_tel_pay_num));
        //输入几个字符时,开始显示字符匹配
        mEtTelNum.setThreshold(1);
        mTvName = ((TextView) findViewById(R.id.tv_tel_pay_name));
        mTvLocal = ((TextView) findViewById(R.id.tv_tel_pay_local));
        mIvDealTelNum = ((ImageView) findViewById(R.id.iv_tel_pay));
        mGv = ((NoScrollGridView) findViewById(R.id.gv_tel_pay));
    }

    private void setupView() {
        mTitle.setLeftLayoutClickListener(this);
        //两个都是有用的
//        mEtTelNum.addTextChangedListener(mTelNumTextWatcher);
        mEtTelNum.addTextChangedListener(mNewTextWatcher);
        //初始化输入框
        mEtTelNum.setText(mTelNumself);
        mIvDealTelNum.setOnClickListener(this);
        mGv.setOnItemClickListener(mPayFeeOnItemOnClickListener);

    }

    TextWatcher mNewTextWatcher = new TextWatcher() {

        private boolean isDelete;

        @Override
        public void onTextChanged(CharSequence s, int cursorPosition, int before, int count) {
            LogUtils.e("position", "cursorPosition:" + cursorPosition + ",before:" + before + ",count:" + count);
            mTvLocal.setText("");
            mTvName.setText("");
            mTvName.setEnabled(true);

            String string = s.toString().trim();
            //去掉空格,获得完全11位的电话号码
            string = getPayTelNum(string);
            //输入为空
            if (TextUtils.isEmpty(string)) {
                //可获取通讯录
                mLevel = 0;
                mIvDealTelNum.setImageLevel(mLevel);
                //下面的选择框bu可以选择
                initCtlPayMoneyNums(false);
                mTvName.setText("");
                mTvLocal.setText("");
                return;
            }

            mEtTelNum.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        isDelete = true;
                    }
                    return false;
                }
            });

            TelPhoneFormatUtils.formatPhoneNumber(s, cursorPosition, before, count, mEtTelNum, this);

            //是合格的电话号码
            if (ValidatorsUtils.validateUserPhone(string)) {
                mTelNum = string;
                mTelNumShow = mEtTelNum.getText().toString();
                //可获取通讯录
                mLevel = 0;
                mIvDealTelNum.setImageLevel(mLevel);
                //下面的选择框可以选择
                final String[] name = {""};

                //获得姓名，归属地
                if (!mTelNumself.equals(mTelNum)) {
                    if (isFromContact && mContacts != null) {
                        name[0] = mContacts[0];
                        mNameInput = name[0];
                        mTvName.setText(name[0]);
                        isFromContact = false;
                    } else {
//                        name = ContactsUtils.getDisplayNameByNumber(getApplicationContext(), mTelNum);
                        new TelPayContactTask(TelephoneFeeChargeActivity.this, mTelNum, new TelPayContactTask.TelNameDealCall() {
                            @Override
                            public void setName(String telName) {
                                if (!isHaveLocal) {
                                    name[0] = "";
                                    mNameInput = name[0];
                                    mTvName.setText(name[0]);
                                } else {
                                    name[0] = telName;
                                    mNameInput = name[0];
                                    mTvName.setText(name[0]);
                                }

                            }
                        }).execute(mTelNum);

                    }
                } else {
                    name[0] = "账号绑定号码";
                    mNameInput = name[0];
                    mTvName.setText(name[0]);
                }

                //获取归属地
                //test
                getTelLocal();

                initCtlPayMoneyNums(true);

                //输入不符合条件
            } else {
                //显示清除输入框按钮
                mLevel = 1;
                mIvDealTelNum.setImageLevel(mLevel);
                //下面的选择框bu可以选择
                initCtlPayMoneyNums(false);

                mTvLocal.setText("");

                if (string.length() == 11) {
                    mTvName.setText("输入手机号码有误");
                    mTvName.setEnabled(false);
                    //显示获取通讯录数据
                    mLevel = 0;
                    mIvDealTelNum.setImageLevel(mLevel);
                    return;
                }
                mTvName.setText("");
            }
        }

        @Override
        public void afterTextChanged(Editable arg0) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };


    /**
     * 电话号码输入框监听
     */

//    TextWatcher mTelNumTextWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            mTvName.setEnabled(true);
//            String string = charSequence.toString().trim();
//            //去掉空格,获得完全11位的电话号码
//            string = getPayTelNum(string);
//
//            //输入为空
//            if (TextUtils.isEmpty(string)) {
//                //可获取通讯录
//                mLevel = 0;
//                mIvDealTelNum.setImageLevel(mLevel);
//                //下面的选择框bu可以选择
//                initCtlPayMoneyNums(false);
//                mTvName.setText("");
//                mTvLocal.setText("");
//
//                return;
//            }
//
//            //输入有误账号
//            String stringShow = charSequence.toString();
//
//            if (stringShow.contains("+86") || stringShow.startsWith("86")) {
//                mEtTelNum.setText(string);
//                return;
//            }
//            if (stringShow.length() > 13) {
//                mEtTelNum.setText(stringShow.substring(0, 13));
//                mEtTelNum.setSelection(13);
//                return;
//            }
//
//            //获得显示号码
//            int length = string.length();
//            StringBuffer sb = new StringBuffer(string);
//
//            if (length > 3 && length <= 7 && stringShow.indexOf(" ") == -1) {
//                sb.insert(3, " ");
//                mEtTelNum.setText(sb.toString());
//                //控制光标的位置
//                mEtTelNum.setSelection(sb.toString().length());
//            } else if (length > 7 && stringShow.lastIndexOf(" ") != 8) {
//
//                sb.insert(3, " ").insert(8, " ");
//
//                mEtTelNum.setText(sb.toString());
//                //控制光标的位置
//                mEtTelNum.setSelection(sb.toString().length());
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//        }
//    };


    /**
     * 清楚充值历史
     */
    private void clearHistory() {

        //自己的对象存在
        if (ifExist(mTelNumself)) {
            while (mHistoryList.size() > 2) {
                mHistoryList.remove(1);
            }
            //自己的对象不存在
        } else {
            mHistoryList.clear();
        }
        mHistoryAdapter.notifyDataSetChanged();
        updateHistoryData();

    }

    /**
     * 判断输入对象是否存在
     */
    private boolean ifExist(String num) {
        if (!TextUtils.isEmpty(num)) {
            for (TelPayHistoryModel.Bean bean : mHistoryList) {
                String getNum = getPayTelNum(bean.getTelNum());
                LogUtils.e("ifExist", num.equals(getNum) + "");
                if (num.equals(getNum)) {
                    return true;
                }
            }
        }
        LogUtils.e("ifExist", "没跳转出去");
        return false;
    }

    /**
     * @param
     */
    private int getIndxe(String num) {
        for (TelPayHistoryModel.Bean bean : mHistoryList) {
            String getNum = getPayTelNum(bean.getTelNum());
            LogUtils.e("getIndxe", mHistoryList.indexOf(bean) + "");
            if (num.equals(getNum)) {
                return mHistoryList.indexOf(bean);
            }
        }
        LogUtils.e("ifExist", "没跳转出去");
        return -1;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_layout://返回
                finish();
                break;
            case R.id.iv_tel_pay://清除电话号码输入框/获取通讯录电话号码
                dealTelNumInputView();
                break;

        }
    }

    /**
     * 根据编辑框的状态处理编辑框
     */
    private void dealTelNumInputView() {
        isGetContas = true;
        //获取通讯录电话号码
        if (mLevel == 0) {
            isFromContact = true;
//            startContacts();
            jumpContacts();
            return;
        }
        //否则mLevel==1，清空输入框
        mEtTelNum.setText("");

    }


    private void jumpContacts() {
        isGetContas = false;
        //如果该版本低于6.0，或者该权限已被授予，它则可以继续读取联系人。
        //存在通讯录排序错乱的问题，获取特殊字符联系人崩溃的问题
//        Uri uri = Uri.parse("content://contacts/people");

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(intent, CONTACT_REQUESTCODE);


    }


    /**
     * 处理通讯录返回的信息
     *
     * @param uri
     * @return
     */
    private String[] getPhoneContacts(Uri uri) {
        final String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = null;
        try {
            cursor = cr.query(uri, null, null, null, null);
//            if (cursor != null) {
            if (cursor.moveToFirst()) {
                //取得联系人姓名
                int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                isFromContact = true;
                contact[0] = cursor.getString(nameFieldColumnIndex);
                //取得电话号码
                String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                // 查看联系人有多少个号码，如果没有号码，返回0
                int phoneCount = cursor
                        .getInt(cursor
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                LogUtils.e("phoneCount", "" + phoneCount);

                if (phoneCount > 0) {
                    // 获得联系人的电话号码列表
                    Cursor phoneCursor = null;
                    try {
                        phoneCursor = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                        + "=" + ContactId, null, null);
                        if (phoneCursor.moveToFirst()) {


                            mTelNumsOfDilogList = new ArrayList<>();
                            String phoneNumber;


                            do {
                                //遍历所有的联系人下面所有的电话号码
                                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                LogUtils.e("pzhoneNumber", "" + phoneNumber);
                                LogUtils.e("pzhoneNumber_name", "" + contact[0]);

                                mTelNumsOfDilogList.add(phoneNumber);

                            } while (phoneCursor.moveToNext());
                            //吊起弹框
                            //多个电话号码,弹窗选择
                            if (mTelNumsOfDilogList.size() > 1) {
                                showPhoneChoiceDialog(mTelNumsOfDilogList, new GetTelNumCallBack() {
                                    @Override
                                    public void setChoiceNum(String choiceNum) {
                                        contact[1] = choiceNum;
                                        mContacts = contact;
                                        mTvName.setText(contact[0]);
                                        mEtTelNum.setText(contact[1]);

                                        if (!ValidatorsUtils.validateUserPhone(getPayTelNum(contact[1]))) {
                                            ToastUtils.showShort("号码错误");
                                        }
                                    }
                                });
                                return contact;

                            } else if (mTelNumsOfDilogList.size() == 1) {
                                contact[1] = phoneNumber;
                                mContacts = contact;
                                mTvName.setText(contact[0]);
                                mEtTelNum.setText(contact[1]);
                                if (!ValidatorsUtils.validateUserPhone(getPayTelNum(contact[1]))) {
                                    ToastUtils.showShort("号码错误");
                                }
                                return contact;
                            }
                        }
                    } finally {
                        if (phoneCursor != null) {
                            phoneCursor.close();
                        }
                    }
                } else {
                    ToastUtils.showShort("号码错误");
                }
            } else {
                ToastUtils.showShort(getString(R.string.mine_contact_permission));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contact;
    }


    /**
     * 获取电话归属地
     */
    private void getTelLocal() {
        //获取成功在成功回调中向控价中填充
        mApi4Mine.getTelLocal(mTelNum, new DataCallback<TelPayLocalModel>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
//                getTelLocal();
                if (!NetStateUtils.isNetworkAvailable(getApplicationContext())) {
                    UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
                    return;
                }
                ToastUtils.showException(e);
                LogUtils.e("getTelLocal_Exception", e.getMessage());
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response == null) {
                    return;
                }
                TelPayLocalModel model = (TelPayLocalModel) response;

                LogUtils.e("getTelLocal", model.getCode());

                if (model != null) {
                    TelPayLocalModel.DataBean bean = model.getData();
                    switch (model.getCode()) {
                        case "200":
                            isHaveLocal = true;
                            mTvLocal.setEnabled(true);
                            mLocalInput = "（" + bean.getArea() + bean.getOperator() + "）";

                            break;
                        case "400":
                            isHaveLocal = false;
                            mTvLocal.setEnabled(false);
                            mLocalInput = "查询失败";
                            mTvName.setText("");
                            break;
                    }

                    mTvLocal.setText(mLocalInput);
                }
            }
        });
    }

    /**
     * 处理话费选择列表点击事件
     */

    AdapterView.OnItemClickListener mPayFeeOnItemOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, final View view, int position, long l) {

            if (Utility.isFastDoubleClick()) {
                return;
            }

            mPayMoney = Double.valueOf(mTelPayMoneyList.get(position));
//            ToastUtils.showShort("充值" + mPayMoney);

            //通讯录中存在
            if (ContactsUtils.isHave(getApplicationContext(), mTvName.getText().toString()) || mTelNumself.equals(mTelNum)) {
                pay(view);

            } else {
                //弹框提示
                Dialog dialog = DialogUtils.createConfirmDialog(
                        TelephoneFeeChargeActivity.this,
                        null,
                        "确认给手机号\n" + mTelNum + "(不在通讯录)\n" + "充值吗？",
                        "充值",
                        "取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                pay(view);

                            }
                        }, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });
                dialog.show();

            }
        }
    };

    /**
     * 生成订单
     */


    private void pay(final View view) {
        //test
//        boolean test = true;
//        if (test) {
//            paySuccess();
//            return;
//        }


        //判断手机号码是否在通讯录中，存在吊起支付，不在弹框提示，吊起支付
        //吊起支付
        PayUtils.pay(getSupportFragmentManager(), TAG, mPayMoney, new PayDetailFragment.PayCallback() {
            @Override
            public void onPayCallback(String channel) {
                int amount = 1; // 金额 接口已修改，不从此处判断订单金额，此处设置实际无效
                Map<String, Object> map = new HashMap<>();
                map.put(AMOUNT, mPayMoney);
                map.put(TEL, mTelNum);
                LogUtils.e("pay", "AMOUNT" + mPayMoney + "----TEL" + mTelNum);
                new PaymentTask(
                        TelephoneFeeChargeActivity.this,
                        TelephoneFeeChargeActivity.this,
                        null,
                        channel,
                        view,
                        TAG,
                        map

                ).execute(new PaymentTask.PaymentRequest(channel, amount));
            }
        });
    }


    /**
     * 获取通讯录数据
     *
     * @param data
     */
    public void getContacts(Intent data) {
        Uri uri = data.getData();
        getPhoneContacts(uri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initView();
                setupView();
                LoadPayNums();
                getHistoryPay();

            } else {
                ToastUtils.showLong(getString(R.string.mine_contact_permission));
                finish();
            }
        }
    }

    /**
     * 多号码选择框
     *
     * @param phoneList
     */
    private void showPhoneChoiceDialog(final List<String> phoneList, final GetTelNumCallBack callBack) {
        final String[] choiceNum = {""};
        final View choiceView = LayoutInflater.from(this).inflate(R.layout.dialog_tel_fee_charge_choice, null);
        ListView listView = (ListView) choiceView.findViewById(R.id.tel_fee_charge_choice_dialog_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice, phoneList);
        listView.setAdapter(adapter);
        final Dialog choiceDialog = DialogUtils.createRandomDialog(this, "请选择一个号码", null, "取消", null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                },
                choiceView
        );
        choiceDialog.setCancelable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                callBack.setChoiceNum(phoneList.get(position));

                if (choiceDialog != null && choiceDialog.isShowing()) {
                    choiceDialog.dismiss();
                }
            }
        });
        if (choiceDialog != null && !choiceDialog.isShowing()) {
            choiceDialog.show();
        }
    }

    public interface GetTelNumCallBack {
        void setChoiceNum(String choiceNum);
    }

    /**
     * 重写方法获取返回信息
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //处理第三方支付处理
        PingppPayResult.setOnPayResultCallback(requestCode, resultCode, data, new PingppPayResult.OnPayResultCallback() {
            @Override
            public void onPaySuccess() {
                paySuccess();
            }

            @Override
            public void onPayFail() {
                ToastUtils.showShort("支付失败");
            }
        });
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case 666://获取通讯录信息
                getContacts(data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //////////////////////支付处理////////////////////////

    /**
     * 支付成功后的操作
     */
    private void paySuccess() {
        updateHistory();
        TelPaySuccessActivity.startAction(this, "" + mPayMoney);
        finish();
    }

    /**
     * 更新本地历史存储
     */
    private void updateHistory() {
        int indxe = -1;
        switch (mHistoryList.size()) {
            case 0:
                addNewHistory(0, 0);
                break;
            case 2:
                //电话不存在
                if (!ifExist(mTelNum)) {
                    if (!ifExist(mTelNumself)) {
                        addNewHistory(2, 0);
                    } else {
                        addNewHistory(2, 1);
                    }
                }
                break;
            case 3:
                if (!ifExist(mTelNum)) {
                    if (!ifExist(mTelNumself)) {
                        addNewHistory(3, 0);
                    } else {
                        addNewHistory(3, 1);
                    }
                } else {
                    mHistoryList.remove(getIndxe(mTelNum));
                    if (!ifExist(mTelNumself)) {
                        addNewHistory(3, 0);
                    } else {
                        addNewHistory(3, 1);
                    }

                }

                break;
            case 4:
                if (!ifExist(mTelNum)) {
                    mHistoryList.remove(2);
                    if (!ifExist(mTelNumself)) {
                        addNewHistory(4, 0);
                    } else {
                        addNewHistory(4, 1);
                    }
                } else {
                    mHistoryList.remove(getIndxe(mTelNum));
                    if (!ifExist(mTelNumself)) {
                        addNewHistory(4, 0);
                    } else {
                        addNewHistory(4, 1);
                    }
                }
                break;
        }
    }

    private void addNewHistory(int count, int index) {
        TelPayHistoryModel.Bean bean;
        bean = new TelPayHistoryModel.Bean();
        bean.setTelNum(mTelNumShow);
        bean.setName(mNameInput);
        bean.setLocal(mLocalInput);
        mHistoryList.add(index, bean);
        if (count == 0) {
            bean = new TelPayHistoryModel.Bean();
            mHistoryList.add(bean);
        }
        updateHistoryData();
    }

    /**
     * 处理本地存储历史充值数据
     */

    private void updateHistoryData() {
        TelPayHistoryModel model = new TelPayHistoryModel();
        String jsonString;
        model.setBeanList(mHistoryList);
        jsonString = new Gson().toJson(model);
        LogUtils.e("jsonString", jsonString);
        TelPayHistoryUtils.clearHistoryPay(this, mTelNumself);
        TelPayHistoryUtils.putHistoryPay(this, jsonString, mTelNumself);
    }

    /**
     * 余额支付后的“回调”
     *
     * @param event
     */
    @Subscribe
    public void onBalancePayEvent(PayResultEvent event) {
        if (event.isPaySuccess()) {
            paySuccess();
        }
    }
}