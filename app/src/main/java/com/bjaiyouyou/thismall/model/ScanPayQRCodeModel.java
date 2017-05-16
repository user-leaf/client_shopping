package com.bjaiyouyou.thismall.model;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ScanPayQRCodeModel {

    /**
     * shopId : 100
     * money : 100
     */

    private long shopId;
    private double money;

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
