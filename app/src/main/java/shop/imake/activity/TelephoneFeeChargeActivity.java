package shop.imake.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.ContactsUtils;
import shop.imake.utils.DialogUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.PayUtils;
import shop.imake.utils.TelPayHistoryUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.utils.ValidatorsUtils;
import shop.imake.widget.IUUTitleBar;
import shop.imake.widget.NoScrollGridView;

/**
 * 话费充值页面
 */
public class TelephoneFeeChargeActivity extends BaseActivity {


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
//    private String mTelNumself = "";//本机号码
    private String mTelNumself = CurrentUserManager.getCurrentUser().getPhone();//本机号码

    public static String USER_TELNUM = "usertelephonenum";

    private Api4Mine mApi4Mine;
    //金额选择数据
    private List<String> mTelPayMoneyList;
    private TelPayNumAdapter mTelPayNumAdapter;
    private double mPayMoney;

    public static final String TAG = TelephoneFeeChargeActivity.class.getSimpleName();

    private String mOrderNumber;//支付订单号

    private List<TelPayHistoryModel.Bean> mHistoryList;//历史充值数据
    private TelPayHistoryAdapter mHistoryAdapter;//历史充值适配器
    public static String AMOUNT = "amount";
    public static String TEL = "telephone";
    private boolean isGetContas;


    public static void startAction(Context context) {
        Intent intent = new Intent(context, TelephoneFeeChargeActivity.class);
//        intent.putExtra(USER_TELNUM, telNum);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephone_fee_charge);

        EventBus.getDefault().register(this);

        mApi4Mine = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
        mTelPayMoneyList = new ArrayList<>();

        //获取传递的电话号码
//        Intent intent = getIntent();
//        mTelNumself = intent.getStringExtra(USER_TELNUM);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //申请授权，第一个参数为要申请用户授权的权限；第二个参数为requestCode 必须大于等于0，主要用于回调的时候检测，匹配特定的onRequestPermissionsResult。
            //可以从方法名requestPermissions以及第二个参数看出，是支持一次性申请多个权限的，系统会通过对话框逐一询问用户是否授权。
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
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
        String jsonStrig = TelPayHistoryUtils.getHistoryPay(this);

        jsonStrig = jsonStrig != null ? jsonStrig : "";

        TelPayHistoryModel model = new Gson().fromJson(jsonStrig, TelPayHistoryModel.class);
        //历史充值
        mHistoryList = new ArrayList<>();
        mHistoryList = model != null ? model.getBeanList() : mHistoryList;


        mHistoryAdapter = new TelPayHistoryAdapter(mHistoryList, this);
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
        mEtTelNum.setThreshold(1);

        mTvName = ((TextView) findViewById(R.id.tv_tel_pay_name));
        mTvLocal = ((TextView) findViewById(R.id.tv_tel_pay_local));

        mIvDealTelNum = ((ImageView) findViewById(R.id.iv_tel_pay));

        mGv = ((NoScrollGridView) findViewById(R.id.gv_tel_pay));
    }

    private void setupView() {
        mTitle.setLeftLayoutClickListener(this);

        mEtTelNum.addTextChangedListener(mTelNumTextWatcher);
        //处理历史充值条目点击事件
        mEtTelNum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //清除历史
                if (position == adapterView.getCount() - 1) {
                    clearHistory();
                    mEtTelNum.setText("");

                } else {
                    //将选择的电话号码填充在输入框里面
                    mEtTelNum.setText(mHistoryList.get(position).getTelNum());
                }

            }
        });

        //初始化输入框
        mEtTelNum.setText(mTelNumself);

        mIvDealTelNum.setOnClickListener(this);

        mGv.setOnItemClickListener(mPayFeeOnItemOnClickListener);

    }

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
                LogUtils.e("ifExist", num.equals(bean.getTelNum()) + "");
                if (num.equals(bean.getTelNum())) {
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

            LogUtils.e("getIndxe", mHistoryList.indexOf(bean) + "");
            if (num.equals(bean.getTelNum())) {
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
//            startContacts();
            jumpContacts();
            return;
        }


        //否则mLevel==1，清空输入框
        mEtTelNum.setText("");

    }

    /**
     * 打开通讯录
     */
    private void startContacts() {
        //如果当前版本大于等于Android 6.0，且该权限未被授予，则申请授权
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //申请授权，第一个参数为要申请用户授权的权限；第二个参数为requestCode 必须大于等于0，主要用于回调的时候检测，匹配特定的onRequestPermissionsResult。
            //可以从方法名requestPermissions以及第二个参数看出，是支持一次性申请多个权限的，系统会通过对话框逐一询问用户是否授权。
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            if (isGetContas) {
                jumpContacts();
            }
        }

    }

    private void jumpContacts() {
        isGetContas = false;
        //如果该版本低于6.0，或者该权限已被授予，它则可以继续读取联系人。
//        Uri uri = Uri.parse("content://contacts/people");
        Uri uri =ContactsContract.Contacts.CONTENT_URI;
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
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }


    private String mLocalInput;//输入电话的归属地
    private String mNameInput;//输入姓名
    /**
     * 电话号码输入框监听
     */

    TextWatcher mTelNumTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String string = charSequence.toString();
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
            //控制光标的位置
            mEtTelNum.setSelection(string.length());

            //是合格的电话号码
            if (ValidatorsUtils.validateUserPhone(string)) {
                mTelNum = string;
                //可获取通讯录
                mLevel = 0;
                mIvDealTelNum.setImageLevel(mLevel);
                //下面的选择框可以选择
                initCtlPayMoneyNums(true);
                //获得姓名，归属地
                String name = ContactsUtils.getDisplayNameByNumber(getApplicationContext(), mTelNum);
                if (mTelNumself.equals(string)) {
                    name = "账号绑定号码";
                }
                mNameInput = name;
                mTvName.setText(name);

                //获取归属地

                getTelLocal();


                //输入不符合条件
            } else {
                //显示清除输入框按钮
                mLevel = 1;
                mIvDealTelNum.setImageLevel(mLevel);
                //下面的选择框bu可以选择
                initCtlPayMoneyNums(false);

                mTvLocal.setText("");

                if (string.length() == 11) {
                    mTvName.setText("手机号码有误");
                    return;
                }
                mTvName.setText("");
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

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
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response == null) {
                    return;
                }
                TelPayLocalModel model = (TelPayLocalModel) response;
                if (model != null) {
                    TelPayLocalModel.DataBean bean = model.getData();
                    if (bean != null) {
                        mLocalInput = "（" + bean.getArea() + bean.getOperator() + "）";

                        mTvLocal.setText(mLocalInput);
                    }

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
            mPayMoney = Double.valueOf(mTelPayMoneyList.get(position));
//            ToastUtils.showShort("充值" + mPayMoney);

            //通讯录中存在
            if (ContactsUtils.isHave(getApplicationContext(), mTelNum)) {
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

                new PaymentTask(
                        TelephoneFeeChargeActivity.this,
                        TelephoneFeeChargeActivity.this,
                        mOrderNumber,
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
        String[] contacts = getPhoneContacts(uri);
        mTvName.setText(contacts[0]);
        mEtTelNum.setText(contacts[1]);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // 用户成功授予权限
//                if (isGetContas){
//                    jumpContacts();
//                    isGetContas=false;
//                }
                initView();
                setupView();
                LoadPayNums();
                getHistoryPay();


            } else {
//                ToastUtils.showShort("您拒绝了此应用对读取联系人权限的申请");
//                startContacts();
                finish();
            }
        }
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
            case 888://获取通讯录读取权限，成功跳转到通讯录
                if (!isGetContas) {
                    return;
                }
                jumpContacts();
                break;

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
        TelPaySuccessActivity.startAction(this, "¥ " + mPayMoney);
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
//                    indxe=getIndxe(mTelNum);
//                    if (indxe<0){
//                        return;
//                    }
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
        bean.setTelNum(mTelNum);
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

        TelPayHistoryUtils.clearHistoryPay(this);
        TelPayHistoryUtils.putHistoryPay(this, jsonString);
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