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
    private int money;

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
