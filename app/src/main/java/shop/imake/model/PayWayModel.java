package shop.imake.model;

/**
 * 支付方式
 *
 * Created by Administrator on 2017/4/21.
 */
public class PayWayModel {
    private String payWay;
    private int resId;
    private String icon;
    private String title;
    private boolean recommend;
    private boolean choose;

    public PayWayModel() {
    }

    public PayWayModel(String payWay, int resId, String title, boolean recommend, boolean choose) {
        this.payWay = payWay;
        this.resId = resId;
        this.title = title;
        this.recommend = recommend;
        this.choose = choose;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
