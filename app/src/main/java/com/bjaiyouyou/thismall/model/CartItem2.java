package com.bjaiyouyou.thismall.model;

/**
 * 购物车条目（新）
 *
 * @author kanbin
 * @date 2016/7/17
 */
public class CartItem2 {
    // 商品
    private CartModel mCartModel;

    // 是否被选中
    private boolean checked;

    public CartModel getCartModel() {
        return mCartModel;
    }

    public void setCartModel(CartModel cartModel) {
        mCartModel = cartModel;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "CartItem2{" +
                "mCartModel=" + mCartModel;
    }
}
