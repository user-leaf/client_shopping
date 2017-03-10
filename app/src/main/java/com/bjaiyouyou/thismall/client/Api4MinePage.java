package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.model.User;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;

/**
 *author Qxh
 *created at 2017/3/10 13:39
 * 个人中心网络请求
 */
public class Api4MinePage extends BaseClientApi {

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

        doGet(url, strTag, null, callback);
    }
}
