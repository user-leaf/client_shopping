package shop.imake.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import static android.content.ContentValues.TAG;


/**
 * 手机通讯处理工具
 */

public class ContactsUtils {
    /**
     * 根据电话号码，获取在通讯录中的姓名
     * @param context
     * @param number
     * @return
     */
    public static String getDisplayNameByNumber(Context context, String number) {

        String contactName = "";
        ContentResolver cr = context.getContentResolver();
        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
                new String[] { number },
                null
        );

        if (pCur.moveToFirst()) {
            contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            pCur.close();
        }

        if (TextUtils.isEmpty(contactName)){
            contactName="不在通讯录";
        }

        return contactName;
    }

    public static boolean isHave(Context context, String number) {

        return "不在通讯录".equals(getDisplayNameByNumber(context,number))?false:true;
    }




    /*
     * 根据电话号码取得联系人姓名
     */
    public static String getContactNameByPhoneNumber(Context context, String address) {
        String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };

        // 将自己添加到 msPeers 中
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, // Which columns to return.
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
                        + address + "'", // WHERE clause.
                null, // WHERE clause value substitution
                null); // Sort order.

        if (cursor == null) {
            Log.d(TAG, "getPeople null");
            return null;
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            // 取得联系人名字
            int nameFieldColumnIndex = cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cursor.getString(nameFieldColumnIndex);
            return name;
        }
        return null;
    }




    public static String getPeople(Context context,String mNumber) {
        String name = "";
        String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
                                /*ContactsContract.CommonDataKinds.Phone.NUMBER*/};

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + mNumber + "'",
                null,
                null);
        if( cursor == null ) {
            return "";
        }
        for( int i = 0; i < cursor.getCount(); i++ )
        {
            cursor.moveToPosition(i);

            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            name = cursor.getString(nameFieldColumnIndex);
            Log.i(TAG, "lanjianlong" + name + " .... " + nameFieldColumnIndex); // 这里提示 force close
            break;
        }
        if(cursor != null){
            cursor.close();
        }
        return name;
    }
}
