package com.bjaiyouyou.thismall.model;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ShopModel {

    /**
     * avatar_path : https://testapi3.bjaiyouyou.com/storage/app/public/avatar/2017/01/06/41487
     * avatar_name : default_wx_1483674120_71.jpg
     * nick_name : 、Summer
     */

    private String avatar_path;
    private String avatar_name;
    private String nick_name;

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public String getAvatar_name() {
        return avatar_name;
    }

    public void setAvatar_name(String avatar_name) {
        this.avatar_name = avatar_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }
}
