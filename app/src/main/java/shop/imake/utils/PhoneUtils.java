package shop.imake.utils;

import android.content.Context;

import shop.imake.model.ContactModel;
import shop.imake.model.InviteMineModel;
import shop.imake.pinyin.CharacterParser;
import shop.imake.widget.SideBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请好友页-我邀请的 号码处理
 * <p/>
 * Created by JackB on 2016/11/30.
 */
public class PhoneUtils {
    private static final String TAG = PhoneUtils.class.getSimpleName();

    private List<ContactModel> mContactList = new ArrayList<>();
    private Context mContext = null;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    private PhoneUtils() {
    }

    public PhoneUtils(Context context) {
        mContext = context;

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
    }

    /**
     * 接口数据转换为可索引数据
     *
     * @param dataList
     * @return
     */
    public List<ContactModel> getPhoneListFromSource(List<InviteMineModel.MembersBean> dataList) {

        for (InviteMineModel.MembersBean membersBean : dataList) {
            ContactModel model = new ContactModel();
            String name = membersBean.getNick_name();  // 联系人姓名
            String phone = membersBean.getPhone();  // 联系人电话
            boolean isVip = membersBean.getIs_vip() == 2 ? true : false;

            model.setName(name);
            model.setTel(phone);
            model.setVip(isVip);

            if (isVip){
                model.setSortLetters(SideBar.markVip);
                mContactList.add(model);
            }else {
                //汉字转换成拼音
                String pinyin = characterParser.getSelling(name);
                // 防止下一句报错 java.lang.StringIndexOutOfBoundsException: length=0; regionStart=0; regionLength=1
                if (pinyin.length() < 1) {
                    model.setSortLetters("#");
                    mContactList.add(model);
                } else {
                    String sortString = pinyin.substring(0, 1).toUpperCase();

                    // 正则表达式，判断首字母是否是英文字母
                    if (sortString.matches("[A-Z]")) {
                        model.setSortLetters(sortString.toUpperCase());
                    } else {
                        model.setSortLetters("#");
                    }
                    mContactList.add(model);
                }
            }
        }
        return mContactList;
    }
}
