package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 邀请页-我邀请的
 *
 * User: kanbin
 * Date: 2016/8/24
 */
public class InviteMineModel {

    /**
     * id : 13
     * phone : 18210219797
     * nick_name :
     * avatar_path : http://localhost/laravel-5.2.31/admin/public/
     * avatar_name :
     */

    private List<MembersBean> members;

    public List<MembersBean> getMembers() {
        return members;
    }

    public void setMembers(List<MembersBean> members) {
        this.members = members;
    }

    public static class MembersBean {

        /**
         * id : 669
         * phone : 13426110787
         * nick_name : 宋婷
         * avatar_path : http://wx.qlogo.cn/mmopen/b9tsALhBHda6Z7CiaeYKOvurNtuWwqSxVT7aJE86Pa58ShVVic2l9F0cH4mjYctZT7omeCic28OialG0uFPaewcE52bzfrnwXY6L/0
         * avatar_name :
         * is_vip : 0
         */

        private long id;
        private String phone;
        private String nick_name;
        private String avatar_path;
        private String avatar_name;
        private int is_vip;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

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

        public int getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(int is_vip) {
            this.is_vip = is_vip;
        }
    }
}
