package shop.imake.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;


/**
 * 手机通讯处理工具
 */

public class ContactsUtils {


    /**
     * 根据电话号码，获取在通讯录中的姓名
     *
     * @param context
     * @param number
     * @return
     */
    public static String getDisplayNameByNumber(Context context, String number) {
         boolean isHave = false;//查询电话号码是否存在

        if (TextUtils.isEmpty(number)) {
            return "";
        }

        String contactName = "";
//        ContentResolver cr = context.getContentResolver();
//        Cursor pCur = cr.query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                null,
//                ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
//                new String[] { number },
//                null
//        );
//
//        if (pCur.moveToFirst()) {
//            contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            pCur.close();
//        }
//
//        if (TextUtils.isEmpty(contactName)){
//            contactName="不在通讯录";
//        }


        //生成ContentResolver对象
        ContentResolver contentResolver = context.getContentResolver();

        // 获得所有的联系人
        Cursor cursor = contentResolver.query(
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
                    Cursor phoneCursor = context.getContentResolver().query(
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
                }


                if (isHave) {
                    break;
                }

            } while (cursor.moveToNext());
        }

        if (TextUtils.isEmpty(contactName)) {
            contactName = "不在通讯录";
        }

        return contactName;
    }

    public static boolean isHave(Context context, String number) {

        return "不在通讯录".equals(getDisplayNameByNumber(context, number)) ? false : true;
    }


    /**
     * 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
     *
     * @param phoneNum
     * @return
     */
    public static String getPayTelNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return "";
        }
        phoneNum = phoneNum.replaceAll("^(\\+86)", "");
        phoneNum = phoneNum.replaceAll("^(86)", "");
        phoneNum = phoneNum.replaceAll("-", "");
        phoneNum = phoneNum.replaceAll(" ", "");
        phoneNum = phoneNum.trim();
        if (phoneNum.length() > 11) {
            phoneNum = phoneNum.substring(0, 11);
        }

        LogUtils.e("getPayTelNum", phoneNum);
        return phoneNum;
    }

    public static String getPayTelNumCompaer(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return "";
        }
        phoneNum = phoneNum.replaceAll("^(\\+86)", "");
        phoneNum = phoneNum.replaceAll("^(86)", "");
        phoneNum = phoneNum.replaceAll("-", "");
        phoneNum = phoneNum.replaceAll(" ", "");
        phoneNum = phoneNum.trim();
        LogUtils.e("getPayTelNum", phoneNum);
        return phoneNum;
    }
}
