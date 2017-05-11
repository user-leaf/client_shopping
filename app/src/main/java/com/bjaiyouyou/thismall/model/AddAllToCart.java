package com.bjaiyouyou.thismall.model;

/**
 * 批量加入购物车类
 * @author Alice
 *Creare 2016/8/23 16:35
 */
public class AddAllToCart {

    /**
     * product_id : 1
     * product_size_id : 1
     * number : 1
     */

    private int product_id;
    private int product_size_id;
    private int number;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_size_id() {
        return product_size_id;
    }

    public void setProduct_size_id(int product_size_id) {
        this.product_size_id = product_size_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
