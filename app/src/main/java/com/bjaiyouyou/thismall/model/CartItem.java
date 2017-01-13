package com.bjaiyouyou.thismall.model;

import java.io.Serializable;

/**
 * 购物车条目(旧)
 * @see CartItem2
 * @author kanbin
 * @date 2016/6/5
 */
public class CartItem implements Serializable{
    // 商品
    private ProductInfo mProductInfo;

    // 购买数量
    private int count;

    // 是否被选中
    private boolean checked;

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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "mProductInfo=" + mProductInfo +
                ", count=" + count +
                ", checked=" + checked +
                '}';
    }
}
