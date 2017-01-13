package com.bjaiyouyou.thismall.model;

import java.io.Serializable;

/**
 * 用户信息类
 * @author QuXinhang
 *Creare 2016/7/27 9:56
 */
public class User implements Serializable {


    /**
     * id : 672
     * invitation_member_id : 656
     * open_id : oyYs-xIRqYgBpxEO1YnKJo43SQvY
     * name : 阚斌
     * phone : 13161211424
     * email :
     * security_code_hint : ka
     * nick_name : 步履不停
     * integration : 9981
     * member_level : 0
     * money_quantity : 146
     * can_drawings_amount : 0.00
     * is_vip : 2
     * is_get_intergration : 0
     * avatar_path : http://wx.qlogo.cn/mmopen/b9tsALhBHda6Z7CiaeYKOvpuDhGvpCxoQ5kg9vcEicOzibomAZKjITr8RvGe9oDt5DjR0vbSk5YhFsy2OualA7xWO4gE60jA8dk/0
     * avatar_name :
     * my_invitation_code :
     * created_at : 2016-10-17 16:20:56
     * unionid : oO_hwwTdJWt8m2L3iq2AXUOFK_e8
     * sex : 0
     * member_type : 4
     * five_member_id : 0
     * push_money : 0
     * is_in_test_user : 1
     * cannot_push_money : 1
     * security_code_state : true
     */

    private MemberBean member;

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public static class MemberBean implements Serializable{
        private int id;
        private int invitation_member_id;
        private String open_id;
        private String name;
        private String phone;
        private String email;
        private String security_code_hint;
        private String nick_name;
        private int integration;
        private int member_level;
        private int money_quantity;
        private String can_drawings_amount;
        private int is_vip;
        private int is_get_intergration;
        private String avatar_path;
        private String avatar_name;
        private String my_invitation_code;
        private String created_at;
        private String unionid;
        private int sex;
        private int member_type;
        private int five_member_id;
        private String push_money;
        private int is_in_test_user;
        private String cannot_push_money;
        private boolean security_code_state;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInvitation_member_id() {
            return invitation_member_id;
        }

        public void setInvitation_member_id(int invitation_member_id) {
            this.invitation_member_id = invitation_member_id;
        }

        public String getOpen_id() {
            return open_id;
        }

        public void setOpen_id(String open_id) {
            this.open_id = open_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSecurity_code_hint() {
            return security_code_hint;
        }

        public void setSecurity_code_hint(String security_code_hint) {
            this.security_code_hint = security_code_hint;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public int getIntegration() {
            return integration;
        }

        public void setIntegration(int integration) {
            this.integration = integration;
        }

        public int getMember_level() {
            return member_level;
        }

        public void setMember_level(int member_level) {
            this.member_level = member_level;
        }

        public int getMoney_quantity() {
            return money_quantity;
        }

        public void setMoney_quantity(int money_quantity) {
            this.money_quantity = money_quantity;
        }

        public String getCan_drawings_amount() {
            return can_drawings_amount;
        }

        public void setCan_drawings_amount(String can_drawings_amount) {
            this.can_drawings_amount = can_drawings_amount;
        }

        public int getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(int is_vip) {
            this.is_vip = is_vip;
        }

        public int getIs_get_intergration() {
            return is_get_intergration;
        }

        public void setIs_get_intergration(int is_get_intergration) {
            this.is_get_intergration = is_get_intergration;
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

        public String getMy_invitation_code() {
            return my_invitation_code;
        }

        public void setMy_invitation_code(String my_invitation_code) {
            this.my_invitation_code = my_invitation_code;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getMember_type() {
            return member_type;
        }

        public void setMember_type(int member_type) {
            this.member_type = member_type;
        }

        public int getFive_member_id() {
            return five_member_id;
        }

        public void setFive_member_id(int five_member_id) {
            this.five_member_id = five_member_id;
        }

        public String getPush_money() {
            return push_money;
        }

        public void setPush_money(String push_money) {
            this.push_money = push_money;
        }

        public int getIs_in_test_user() {
            return is_in_test_user;
        }

        public void setIs_in_test_user(int is_in_test_user) {
            this.is_in_test_user = is_in_test_user;
        }

        public String getCannot_push_money() {
            return cannot_push_money;
        }

        public void setCannot_push_money(String cannot_push_money) {
            this.cannot_push_money = cannot_push_money;
        }

        public boolean isSecurity_code_state() {
            return security_code_state;
        }

        public void setSecurity_code_state(boolean security_code_state) {
            this.security_code_state = security_code_state;
        }
    }
}
