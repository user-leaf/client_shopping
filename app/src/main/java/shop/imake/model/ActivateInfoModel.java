package shop.imake.model;

import java.io.Serializable;

/**
 * 兑换券使用数据类型
 * Created by IUU
 * 2017/4/26
 */
public class ActivateInfoModel implements Serializable {

    /**
     * can_drawings_amount : 0.50
     * user_integration : 9399
     * money_quantity : 3597
     * push_money : 99896
     * user_lever : 2
     * drawings_number : 1
     * week_activate_limit : 300.00
     * week_activate_number :
     * today_surplus_activate_number : 1
     * today_surplus_drawings_number : 1
     * user_withdrawable_balance : 96934.11
     * today_drawings_number : 1
     * today_surplus_push_money_drawings_number : 0
     */

    private UserAboutCashInfoBean userAboutCashInfo;

    public UserAboutCashInfoBean getUserAboutCashInfo() {
        return userAboutCashInfo;
    }

    public void setUserAboutCashInfo(UserAboutCashInfoBean userAboutCashInfo) {
        this.userAboutCashInfo = userAboutCashInfo;
    }

    public static class UserAboutCashInfoBean implements Serializable{
        private String can_drawings_amount;
        private long user_integration;
        private long money_quantity;
        private String push_money;
        private int user_lever;
        private int drawings_number;
        private String week_activate_limit;
        private String week_activate_number;
        private int today_surplus_activate_number;
        private int today_surplus_drawings_number;
        private double user_withdrawable_balance;
        private int today_drawings_number;
        private int today_surplus_push_money_drawings_number;

        public String getCan_drawings_amount() {
            return can_drawings_amount;
        }

        public void setCan_drawings_amount(String can_drawings_amount) {
            this.can_drawings_amount = can_drawings_amount;
        }

        public long getUser_integration() {
            return user_integration;
        }

        public void setUser_integration(long user_integration) {
            this.user_integration = user_integration;
        }

        public long getMoney_quantity() {
            return money_quantity;
        }

        public void setMoney_quantity(long money_quantity) {
            this.money_quantity = money_quantity;
        }

        public String getPush_money() {
            return push_money;
        }

        public void setPush_money(String push_money) {
            this.push_money = push_money;
        }

        public int getUser_lever() {
            return user_lever;
        }

        public void setUser_lever(int user_lever) {
            this.user_lever = user_lever;
        }

        public int getDrawings_number() {
            return drawings_number;
        }

        public void setDrawings_number(int drawings_number) {
            this.drawings_number = drawings_number;
        }

        public String getWeek_activate_limit() {
            return week_activate_limit;
        }

        public void setWeek_activate_limit(String week_activate_limit) {
            this.week_activate_limit = week_activate_limit;
        }

        public String getWeek_activate_number() {
            return week_activate_number;
        }

        public void setWeek_activate_number(String week_activate_number) {
            this.week_activate_number = week_activate_number;
        }

        public int getToday_surplus_activate_number() {
            return today_surplus_activate_number;
        }

        public void setToday_surplus_activate_number(int today_surplus_activate_number) {
            this.today_surplus_activate_number = today_surplus_activate_number;
        }

        public int getToday_surplus_drawings_number() {
            return today_surplus_drawings_number;
        }

        public void setToday_surplus_drawings_number(int today_surplus_drawings_number) {
            this.today_surplus_drawings_number = today_surplus_drawings_number;
        }

        public double getUser_withdrawable_balance() {
            return user_withdrawable_balance;
        }

        public void setUser_withdrawable_balance(double user_withdrawable_balance) {
            this.user_withdrawable_balance = user_withdrawable_balance;
        }

        public int getToday_drawings_number() {
            return today_drawings_number;
        }

        public void setToday_drawings_number(int today_drawings_number) {
            this.today_drawings_number = today_drawings_number;
        }

        public int getToday_surplus_push_money_drawings_number() {
            return today_surplus_push_money_drawings_number;
        }

        public void setToday_surplus_push_money_drawings_number(int today_surplus_push_money_drawings_number) {
            this.today_surplus_push_money_drawings_number = today_surplus_push_money_drawings_number;
        }
    }
}
