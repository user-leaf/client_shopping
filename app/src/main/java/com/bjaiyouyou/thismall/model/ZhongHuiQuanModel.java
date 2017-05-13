package com.bjaiyouyou.thismall.model;

/**
 * 众汇券数据类型
 * Created by IUU
 * 2017/5/12
 */
public class ZhongHuiQuanModel {

    /**
     * can_use_balance : 200
     * all_balance : 0.00
     * freeze_balance : -200
     */

    private double can_use_balance;
    private String all_balance;
    private double freeze_balance;

    public double getCan_use_balance() {
        return can_use_balance;
    }

    public void setCan_use_balance(double can_use_balance) {
        this.can_use_balance = can_use_balance;
    }

    public String getAll_balance() {
        return all_balance;
    }

    public void setAll_balance(String all_balance) {
        this.all_balance = all_balance;
    }

    public double getFreeze_balance() {
        return freeze_balance;
    }

    public void setFreeze_balance(double freeze_balance) {
        this.freeze_balance = freeze_balance;
    }
}
