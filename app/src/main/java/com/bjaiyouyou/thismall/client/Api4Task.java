package com.bjaiyouyou.thismall.client;

import android.text.TextUtils;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.fragment.TaskPage;
import com.bjaiyouyou.thismall.model.SignInInfo;
import com.bjaiyouyou.thismall.model.TaskModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * 任务模块网络请求
 *
 * Created by kanbin on 2017/2/14.
 */
public class Api4Task extends BaseClientApi {

    /**
     * 不能私有默认无参构造方法，否则不能实例化对象
     */

    /**
     * 获取任务页签到信息
     *
     * @param callback
     */
    public void getTaskSignInfo(DataCallback<SignInInfo> callback) {

        String url = ClientAPI.API_POINT + "api/v1/auth/getSignInInfo" + "?token=" + CurrentUserManager.getUserToken();

        LogUtils.d(TAG, "getTaskSignInfo: " + url);

        doGet(url, TaskPage.TAG, null, callback);
    }

    /**
     * 获取任务页广告
     *
     * @param callback
     */
    public void getTaskAd(int pageno, DataCallback<TaskModel> callback){

        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/task/index2?page=");
        sb.append(pageno);

        String token = CurrentUserManager.getUserToken();
        if (!TextUtils.isEmpty(token)) {
            sb.append("&token=").append(token);
        }

        String url = sb.toString();

        LogUtils.d(TAG, "getTaskAD.url: " + url);

        doGet(url, TaskPage.TAG, null, callback);
    }
}
