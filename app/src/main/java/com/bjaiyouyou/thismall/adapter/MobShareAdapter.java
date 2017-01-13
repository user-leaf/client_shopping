package com.bjaiyouyou.thismall.adapter;

import cn.sharesdk.framework.authorize.AuthorizeAdapter;

/**
 * ShareSDK分享 去掉 powered by ShareSDK标识所需的
 *
 * @author kanbin
 * @date 2016/6/29
 */
public class MobShareAdapter extends AuthorizeAdapter {
    public void onCreate() {
        // 隐藏标题栏右部的ShareSDK Logo
        hideShareSDKLogo();
    }
}
