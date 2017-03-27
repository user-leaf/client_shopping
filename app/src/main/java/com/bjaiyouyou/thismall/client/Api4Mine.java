package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.model.ContactMemberModel;
import com.bjaiyouyou.thismall.model.User;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;

import java.util.Map;

/**
 * 个人中心模块网络请求
 */
public class Api4Mine extends BaseClientApi {

    /**
     * 获取用户个人信息
     * @param strTag
     * @param callback
     */
    public void getUserMessage(String strTag, DataCallback<User> callback) {
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/auth/memberDetail")
                .append("?token=")
                .append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        LogUtils.d(TAG, "getUserMessage: " + url);
        LogUtils.e(TAG, "getUserMessage: " + url);

        doGet(url, strTag, null, callback);
    }

    /**
     * 邀请好友页数据请求
     *
     * @param strTag
     * @param callback
     */
    public void getContactsInfo(String strTag, Map<String, String> params, DataCallback<ContactMemberModel> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/auth/isMemberByContacts")
                .append("?token=")
                .append(CurrentUserManager.getUserToken());
        String url = sb.toString();

        doPost(url, strTag, params, callback);
    }

}
