package shop.imake.model;

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
    private double user_withdrawable_balance;
    /**
     * company_name : 阿斯顿撒旦撒
     */

    private String company_name;

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

    public double getUser_withdrawable_balance() {
        return user_withdrawable_balance;
    }

    public void setUser_withdrawable_balance(double user_withdrawable_balance) {
        this.user_withdrawable_balance = user_withdrawable_balance;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
