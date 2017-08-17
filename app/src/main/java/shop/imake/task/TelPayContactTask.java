package shop.imake.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.text.TextUtils;

import shop.imake.utils.LogUtils;

import static shop.imake.utils.ContactsUtils.NOT_HAVE_TELNUM;
import static shop.imake.utils.ContactsUtils.getPayTelNumCompaer;

/**
 * 手机号码匹配线程
 */

public class TelPayContactTask extends AsyncTask<String, Void, String> {
    private TelNameDealCall call;
    private Context context;
    private String number;

    public TelPayContactTask(Context context, String number, TelNameDealCall call) {
        this.call = call;
        this.context = context;
        this.number = number;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        //////////////////////////全部匹配
        boolean isHave = false;//查询电话号码是否存在
        String contactName = "";
        //生成ContentResolver对象
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = null;


        try {
            // 获得所有的联系人
            cursor = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            //这段代码和上面代码是等价的，使用两种方式获得联系人的Uri
//        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/contacts"), null, null, null, null);

            // 循环遍历
            if (cursor.moveToFirst()) {

                int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int displayNameColumn = cursor
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                do {
                    // 获得联系人的ID
                    String contactId = cursor.getString(idColumn);

                    // 获得联系人姓名
                    String displayName = cursor.getString(displayNameColumn);

                    //使用Toast技术显示获得的联系人信息
//                Toast.makeText(context, "联系人姓名：" + displayName, Toast.LENGTH_LONG).show();
                    // 查看联系人有多少个号码，如果没有号码，返回0
                    int phoneCount = cursor
                            .getInt(cursor
                                    .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (phoneCount > 0) {
                        // 获得联系人的电话号码列表
                        Cursor phoneCursor = null;

                        try {
                            phoneCursor = context.getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + "=" + contactId, null, null);

                            if (phoneCursor.moveToFirst()) {
                                do {
                                    //遍历所有的联系人下面所有的电话号码
                                    String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    //使用Toast技术显示获得的号码
//                            Toast.makeText(context, "联系人电话：" + phoneNumber, Toast.LENGTH_LONG).show();
                                    //进行判断
                                    if (getPayTelNumCompaer(number).equals(getPayTelNumCompaer(phoneNumber))) {
                                        isHave = true;
                                        contactName = displayName;
                                        return displayName;
                                    }

                                    if (isHave) {
                                        break;
                                    }

                                } while (phoneCursor.moveToNext());
                            }
                        } finally {
                            if (phoneCursor != null) {
                                phoneCursor.close();
                            }
                        }

                    }


                    if (isHave) {
                        break;
                    }

                } while (cursor.moveToNext());
            }

            if (TextUtils.isEmpty(contactName)) {
                contactName = NOT_HAVE_TELNUM;
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return contactName;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //将获取姓名通过回调传回去
        LogUtils.e("contactName", s);
        call.setName(s);

    }


    public interface TelNameDealCall {
        void setName(String telName);
    }

}
