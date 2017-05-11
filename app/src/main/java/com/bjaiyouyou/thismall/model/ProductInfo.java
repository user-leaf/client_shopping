package com.bjaiyouyou.thismall.model;

/**
 * 商品对象(首页物品列表、购物车、订单详情、下订单)
 * @author JackB
 * @date 2016/6/5
 */
public class ProductInfo {
    // 商品id
    private long id;
    // 商品名称
    private String name;
    // 商品价格
//    private float price;
    private double price;
    // 积分
    private int points;
    // 图片网址
    private String productImageUrl;
    // 商品描述
    private String productDescription;

    public ProductInfo(){}

    public ProductInfo(long id, String name, float price, String productImageUrl, String productDescription) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productImageUrl = productImageUrl;
        this.productDescription = productDescription;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public float getPrice() {
//        return price;
//    }

//    public void setPrice(float price) {
//        this.price = price;
//    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", productImageUrl='" + productImageUrl + '\'' +
                ", productDescription='" + productDescription + '\'' +
                '}';
    }
}
