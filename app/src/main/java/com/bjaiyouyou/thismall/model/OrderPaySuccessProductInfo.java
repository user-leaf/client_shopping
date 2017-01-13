package com.bjaiyouyou.thismall.model;

/**
 * 订单支付成功页商品类
 *
 * @author kanbin
 * @date 2016/6/28
 */
public class OrderPaySuccessProductInfo {
    private String imageUrl;
    private String title;
    private String price;
    private String points;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
