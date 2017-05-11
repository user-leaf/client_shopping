package com.bjaiyouyou.thismall.model;

/**
 * 下订单页列表项数据(旧 @see CartItem2.java)
 *
 * @author JackB
 * @date 2016/6/12
 */
public class OrderMakeItem {
    // 商品描述
    private ProductInfo mProductInfo;
    // 购买的数量
    private int count;

    public ProductInfo getProductInfo() {
        return mProductInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        mProductInfo = productInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
