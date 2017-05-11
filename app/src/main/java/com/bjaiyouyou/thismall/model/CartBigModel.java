package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 购物车接口数据中的CartBigModel类
 *
 * @author JackB
 * @date 2016/7/17
 */
public class CartBigModel {
    private List<CartModel> shopping_carts;

    public List<CartModel> getShopping_carts() {
        return shopping_carts;
    }

    public void setShopping_carts(List<CartModel> shopping_carts) {
        this.shopping_carts = shopping_carts;
    }
}
