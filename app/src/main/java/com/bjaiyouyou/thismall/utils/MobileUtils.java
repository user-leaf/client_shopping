package com.bjaiyouyou.thismall.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import com.bjaiyouyou.thismall.model.ContactModel;
import com.bjaiyouyou.thismall.pinyin.CharacterParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取手机通讯录联系人
 * User: kanbin
 * Date: 2016/5/16
 */
public class MobileUtils {
    private static final String TAG = MobileUtils.class.getSimpleName();

    private List<ContactModel> mMembersEntityList = new ArrayList<>();

    private Context mContext = null;

    /**获取库Phon表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };

    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**头像ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    private MobileUtils(){}

    public MobileUtils(Context context) {
        mContext = context;

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
    }

    /**得到手机通讯录联系人信息**/
    private void getPhoneContacts() {
        ContentResolver resolver = mContext.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                //得到联系人头像Bitamp
                Bitmap contactPhoto = null;

//                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
//                if(photoid > 0 ) {
//                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);
//                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
//                    contactPhoto = BitmapFactory.decodeStream(input);
//                }else {
//                    contactPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
//                }

                phoneNumber = dealPhoneNumber(phoneNumber);

                ContactModel model = new ContactModel();
                model.setName(contactName);
                model.setTel(phoneNumber);

                String pinyin = "";
                //汉字转换成拼音
                if (!TextUtils.isEmpty(contactName)) {
                    pinyin = characterParser.getSelling(contactName);
                }
                // 防止下一句报错 java.lang.StringIndexOutOfBoundsException: length=0; regionStart=0; regionLength=1
                if (pinyin.length()<1){
                    return;
                }
                String sortString = pinyin.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if(sortString.matches("[A-Z]")){
                    model.setSortLetters(sortString.toUpperCase());
                }else{
                    model.setSortLetters("#");
                }

                mMembersEntityList.add(model);
            }

            phoneCursor.close();
        }
    }

    /**
     * 处理手机号码格式，去掉+86、减号等字符
     * @param str
     * @return
     */
    private String dealPhoneNumber(String str) {
        String ret = null;
        if (str.startsWith("+86")){
            str = str.substring(3);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c>='0'&&c<='9'){
                sb.append(c);
            }
        }
        ret = sb.toString();
        return ret;
    }

    public List<ContactModel> getAllContacts() {
        getPhoneContacts();
        Log.d(TAG, "getAllContacts: "+mMembersEntityList.toString());
        return mMembersEntityList;
    }
}
