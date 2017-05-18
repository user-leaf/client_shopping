package com.bjaiyouyou.thismall.model;

/**
 *
 *author Alice
 *created at 2017/5/18 15:27
 */
public class PayResultMyOrderRefreshEvent {
    private boolean paySuccess;

    public PayResultMyOrderRefreshEvent(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }

    public boolean isPaySuccess() {
        return paySuccess;
    }

    public void setPaySuccess(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }
}
