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
 * ??????????????????
 */
public class TelephoneFeeChargeActivity extends BaseActivity {
    public static final String TAG = TelephoneFeeChargeActivity.class.getSimpleName();
    private IUUTitleBar mTitle;
    private AutoCompleteTextView mEtTelNum;//?????????????????????
    private ImageView mIvDealTelNum;//??????????????????/?????????????????????
    private TextView mTvName;//??????????????????
    private TextView mTvLocal;//?????????????????????
    private NoScrollGridView mGv;//??????????????????
    private int mLevel = 0;//??????????????????????????????
    public static int CONTACT_REQUESTCODE = 666;//????????????????????????
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 888;//?????????????????????????????????
    private String mTelNum;//?????????????????????
    //test
//    private String mTelNumself = "18333618642";
    private String mTelNumself;//????????????
    public static String USER_TELNUM = "usertelephonenum";//??????????????????????????????????????????
    private Api4Mine mApi4Mine;
    private List<String> mTelPayMoneyList; //??????????????????
    private TelPayNumAdapter mTelPayNumAdapter;//???????????????????????????
    private double mPayMoney;//????????????=
    private List<TelPayHistoryModel.Bean> mHistoryList;//??????????????????
    private TelPayHistoryAdapter mHistoryAdapter;//?????????????????????
    public static String AMOUNT = "amount";//?????????????????????key
    public static String TEL = "telephone";//?????????????????????key
    private boolean isGetContas;//?????????????????????????????????????????????
    private String mTelNumShow;//?????????????????????
    private boolean isFromContact;//??????????????????????????????????????????
    private List<String> mTelNumsOfDilogList;//???????????????????????????????????????
    private String mLocalInput;//????????????????????????
    private String mNameInput;//????????????
    private String[] mContacts = new String[2];//?????????????????????????????????
    private boolean isHaveLocal = false;//?????????????????????????????????

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
        //???????????????????????????
        Intent intent = getIntent();
        mTelNumself = intent.getStringExtra(USER_TELNUM);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
        //????????????????????????????????????????????????????????????????????????????????????requestCode ??????????????????0??????????????????????????????????????????????????????onRequestPermissionsResult???
        //??????????????????requestPermissions?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//            ToastUtils.showShort("??????????????????????????????");
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
     * ????????????????????????
     */
    private void getHistoryPay() {
        //?????????????????????????????????
        String jsonStrig = TelPayHistoryUtils.getHistoryPay(this, mTelNumself);
        jsonStrig = jsonStrig != null ? jsonStrig : "";
        LogUtils.e("getHistoryPay", jsonStrig);
        //???????????????
        TelPayHistoryModel model = new Gson().fromJson(jsonStrig, TelPayHistoryModel.class);
        //????????????
        mHistoryList = new ArrayList<>();
        mHistoryList = model != null ? model.getBeanList() : mHistoryList;
        //?????????????????????,????????????????????????????????????
        mHistoryAdapter = new TelPayHistoryAdapter(mHistoryList, this, new TelPayHistoryAdapter.OnItemOnItemClick() {
            @Override
            public void setOnItemClick(int position, int count) {
                LogUtils.e("???????????????" + position);
                //????????????
                if (position == count - 1) {
                    clearHistory();
                    mEtTelNum.setText("");

                } else {
                    //????????????????????????????????????????????????
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
     * ????????????????????????
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
        //?????????????????????,????????????????????????
        mEtTelNum.setThreshold(1);
        mTvName = ((TextView) findViewById(R.id.tv_tel_pay_name));
        mTvLocal = ((TextView) findViewById(R.id.tv_tel_pay_local));
        mIvDealTelNum = ((ImageView) findViewById(R.id.iv_tel_pay));
        mGv = ((NoScrollGridView) findViewById(R.id.gv_tel_pay));
    }

    private void setupView() {
        mTitle.setLeftLayoutClickListener(this);
        //?????????????????????
//        mEtTelNum.addTextChangedListener(mTelNumTextWatcher);
        mEtTelNum.addTextChangedListener(mNewTextWatcher);
        //??????????????????
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
            //????????????,????????????11??????????????????
            string = getPayTelNum(string);
            //????????????
            if (TextUtils.isEmpty(string)) {
                //??????????????????
                mLevel = 0;
                mIvDealTelNum.setImageLevel(mLevel);
                //??????????????????bu????????????
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

            //????????????????????????
            if (ValidatorsUtils.validateUserPhone(string)) {
                mTelNum = string;
                mTelNumShow = mEtTelNum.getText().toString();
                //??????????????????
                mLevel = 0;
                mIvDealTelNum.setImageLevel(mLevel);
                //??????????????????????????????
                final String[] name = {""};

                //????????????????????????
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
                    name[0] = "??????????????????";
                    mNameInput = name[0];
                    mTvName.setText(name[0]);
                }

                //???????????????
                //test
                getTelLocal();

                initCtlPayMoneyNums(true);

                //?????????????????????
            } else {
                //???????????????????????????
                mLevel = 1;
                mIvDealTelNum.setImageLevel(mLevel);
                //??????????????????bu????????????
                initCtlPayMoneyNums(false);

                mTvLocal.setText("");

                if (string.length() == 11) {
                    mTvName.setText("????????????????????????");
                    mTvName.setEnabled(false);
                    //???????????????????????????
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
     * ???????????????????????????
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
//            //????????????,????????????11??????????????????
//            string = getPayTelNum(string);
//
//            //????????????
//            if (TextUtils.isEmpty(string)) {
//                //??????????????????
//                mLevel = 0;
//                mIvDealTelNum.setImageLevel(mLevel);
//                //??????????????????bu????????????
//                initCtlPayMoneyNums(false);
//                mTvName.setText("");
//                mTvLocal.setText("");
//
//                return;
//            }
//
//            //??????????????????
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
//            //??????????????????
//            int length = string.length();
//            StringBuffer sb = new StringBuffer(string);
//
//            if (length > 3 && length <= 7 && stringShow.indexOf(" ") == -1) {
//                sb.insert(3, " ");
//                mEtTelNum.setText(sb.toString());
//                //?????????????????????
//                mEtTelNum.setSelection(sb.toString().length());
//            } else if (length > 7 && stringShow.lastIndexOf(" ") != 8) {
//
//                sb.insert(3, " ").insert(8, " ");
//
//                mEtTelNum.setText(sb.toString());
//                //?????????????????????
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
     * ??????????????????
     */
    private void clearHistory() {

        //?????????????????????
        if (ifExist(mTelNumself)) {
            while (mHistoryList.size() > 2) {
                mHistoryList.remove(1);
            }
            //????????????????????????
        } else {
            mHistoryList.clear();
        }
        mHistoryAdapter.notifyDataSetChanged();
        updateHistoryData();

    }

    /**
     * ??????????????????????????????
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
        LogUtils.e("ifExist", "???????????????");
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
        LogUtils.e("ifExist", "???????????????");
        return -1;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_layout://??????
                finish();
                break;
            case R.id.iv_tel_pay://???????????????????????????/???????????????????????????
                dealTelNumInputView();
                break;

        }
    }

    /**
     * ???????????????????????????????????????
     */
    private void dealTelNumInputView() {
        isGetContas = true;
        //???????????????????????????
        if (mLevel == 0) {
            isFromContact = true;
//            startContacts();
            jumpContacts();
            return;
        }
        //??????mLevel==1??????????????????
        mEtTelNum.setText("");

    }


    private void jumpContacts() {
        isGetContas = false;
        //?????????????????????6.0?????????????????????????????????????????????????????????????????????
        //?????????????????????????????????????????????????????????????????????????????????
//        Uri uri = Uri.parse("content://contacts/people");

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(intent, CONTACT_REQUESTCODE);


    }


    /**
     * ??????????????????????????????
     *
     * @param uri
     * @return
     */
    private String[] getPhoneContacts(Uri uri) {
        final String[] contact = new String[2];
        //??????ContentResolver??????
        ContentResolver cr = getContentResolver();
        //???????????????????????????????????????
        Cursor cursor = null;
        try {
            cursor = cr.query(uri, null, null, null, null);
//            if (cursor != null) {
            if (cursor.moveToFirst()) {
                //?????????????????????
                int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                isFromContact = true;
                contact[0] = cursor.getString(nameFieldColumnIndex);
                //??????????????????
                String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                // ???????????????????????????????????????????????????????????????0
                int phoneCount = cursor
                        .getInt(cursor
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                LogUtils.e("phoneCount", "" + phoneCount);

                if (phoneCount > 0) {
                    // ????????????????????????????????????
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
                                //???????????????????????????????????????????????????
                                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                LogUtils.e("pzhoneNumber", "" + phoneNumber);
                                LogUtils.e("pzhoneNumber_name", "" + contact[0]);

                                mTelNumsOfDilogList.add(phoneNumber);

                            } while (phoneCursor.moveToNext());
                            //????????????
                            //??????????????????,????????????
                            if (mTelNumsOfDilogList.size() > 1) {
                                showPhoneChoiceDialog(mTelNumsOfDilogList, new GetTelNumCallBack() {
                                    @Override
                                    public void setChoiceNum(String choiceNum) {
                                        contact[1] = choiceNum;
                                        mContacts = contact;
                                        mTvName.setText(contact[0]);
                                        mEtTelNum.setText(contact[1]);

                                        if (!ValidatorsUtils.validateUserPhone(getPayTelNum(contact[1]))) {
                                            ToastUtils.showShort("????????????");
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
                                    ToastUtils.showShort("????????????");
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
                    ToastUtils.showShort("????????????");
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
     * ?????????????????????
     */
    private void getTelLocal() {
        //????????????????????????????????????????????????
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
                            mLocalInput = "???" + bean.getArea() + bean.getOperator() + "???";

                            break;
                        case "400":
                            isHaveLocal = false;
                            mTvLocal.setEnabled(false);
                            mLocalInput = "????????????";
                            mTvName.setText("");
                            break;
                    }

                    mTvLocal.setText(mLocalInput);
                }
            }
        });
    }

    /**
     * ????????????????????????????????????
     */

    AdapterView.OnItemClickListener mPayFeeOnItemOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, final View view, int position, long l) {

            if (Utility.isFastDoubleClick()) {
                return;
            }

            mPayMoney = Double.valueOf(mTelPayMoneyList.get(position));
//            ToastUtils.showShort("??????" + mPayMoney);

            //??????????????????
            if (ContactsUtils.isHave(getApplicationContext(), mTvName.getText().toString()) || mTelNumself.equals(mTelNum)) {
                pay(view);

            } else {
                //????????????
                Dialog dialog = DialogUtils.createConfirmDialog(
                        TelephoneFeeChargeActivity.this,
                        null,
                        "??????????????????\n" + mTelNum + "(???????????????)\n" + "????????????",
                        "??????",
                        "??????",
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
     * ????????????
     */


    private void pay(final View view) {
        //test
//        boolean test = true;
//        if (test) {
//            paySuccess();
//            return;
//        }


        //????????????????????????????????????????????????????????????????????????????????????????????????
        //????????????
        PayUtils.pay(getSupportFragmentManager(), TAG, mPayMoney, new PayDetailFragment.PayCallback() {
            @Override
            public void onPayCallback(String channel) {
                int amount = 1; // ?????? ???????????????????????????????????????????????????????????????????????????
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
     * ?????????????????????
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
     * ??????????????????
     *
     * @param phoneList
     */
    private void showPhoneChoiceDialog(final List<String> phoneList, final GetTelNumCallBack callBack) {
        final String[] choiceNum = {""};
        final View choiceView = LayoutInflater.from(this).inflate(R.layout.dialog_tel_fee_charge_choice, null);
        ListView listView = (ListView) choiceView.findViewById(R.id.tel_fee_charge_choice_dialog_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice, phoneList);
        listView.setAdapter(adapter);
        final Dialog choiceDialog = DialogUtils.createRandomDialog(this, "?????????????????????", null, "??????", null,
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
     * ??????????????????????????????
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //???????????????????????????
        PingppPayResult.setOnPayResultCallback(requestCode, resultCode, data, new PingppPayResult.OnPayResultCallback() {
            @Override
            public void onPaySuccess() {
                paySuccess();
            }

            @Override
            public void onPayFail() {
                ToastUtils.showShort("????????????");
            }
        });
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case 666://?????????????????????
                getContacts(data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //////////////////////????????????////////////////////////

    /**
     * ????????????????????????
     */
    private void paySuccess() {
        updateHistory();
        TelPaySuccessActivity.startAction(this, "" + mPayMoney);
        finish();
    }

    /**
     * ????????????????????????
     */
    private void updateHistory() {
        int indxe = -1;
        switch (mHistoryList.size()) {
            case 0:
                addNewHistory(0, 0);
                break;
            case 2:
                //???????????????
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
     * ????????????????????????????????????
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
     * ??????????????????????????????
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