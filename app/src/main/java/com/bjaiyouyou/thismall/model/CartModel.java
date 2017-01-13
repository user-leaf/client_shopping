package com.bjaiyouyou.thismall.model;

/**
 * 购物车接口数据中的CartModel类
 *
 * @author kanbin
 * @date 2016/7/17
 */
public class CartModel {
    private long product_id; //,
    private long product_size_id; //,
    private int number; //,
    private String updated_at; //2016-07-09 12:21:14",
    private ProductModel product; //bject{...},
    private ProductSizeModel product_size; //bject{...},
    private ProductImageModel product_images; //bject{...}

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public long getProduct_size_id() {
        return product_size_id;
    }

    public void setProduct_size_id(long product_size_id) {
        this.product_size_id = product_size_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public ProductSizeModel getProduct_size() {
        return product_size;
    }

    public void setProduct_size(ProductSizeModel product_size) {
        this.product_size = product_size;
    }

    public ProductImageModel getProduct_images() {
        return product_images;
    }

    public void setProduct_images(ProductImageModel product_images) {
        this.product_images = product_images;
    }

    @Override
    public String toString() {
        return "CartModel{" +
                "product_id=" + product_id +
                ", product=" + product;
    }
}
