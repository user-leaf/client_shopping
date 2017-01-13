package com.bjaiyouyou.thismall.model;

/**
 * 首页抢购商品为空类
 *
 * @author QuXinhang
 *Creare 2016/7/28 10:41
 */
public class HomeNavigationItemNewEmpty {
    private int img;
    private  String price;
    private String integral;

    public HomeNavigationItemNewEmpty(int img, String integral, String price) {
        this.img = img;
        this.integral = integral;
        this.price = price;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
}
