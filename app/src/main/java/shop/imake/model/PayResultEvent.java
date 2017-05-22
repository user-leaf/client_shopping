package shop.imake.model;

/**
 * Created by Administrator on 2017/4/25.
 */
public class PayResultEvent {
    private boolean paySuccess;

    public PayResultEvent(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }

    public boolean isPaySuccess() {
        return paySuccess;
    }

    public void setPaySuccess(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }
}
