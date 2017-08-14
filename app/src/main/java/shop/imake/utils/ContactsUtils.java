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
     * @param context
     * @param number
     * @return
     */
    public static String getDisplayNameByNumber(Context context, String number) {
//        String displayName = null;
//        Cursor cursor = null;
//
//        try {
//            ContentResolver resolver = context.getContentResolver();
//            Uri uri = ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon().appendPath(number).build();
//            String[] projection = new String[] { COLUMN_ID, COLUMN_DISPLAY_NAME };
//            cursor = resolver.query(uri, projection, null, null, null);
//
//            if (cursor != null && cursor.moveToFirst()) {
//                int columnIndexName = cursor.getColumnIndex(COLUMN_DISPLAY_NAME);
//                displayName = cursor.getString(columnIndexName);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//
//        return displayName;


        String contactName = "";
        ContentResolver cr = context.getContentResolver();
        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
                new String[]{number}, null);
        if (pCur.moveToFirst()) {
            contactName = pCur
                    .getString(pCur
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
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
}
