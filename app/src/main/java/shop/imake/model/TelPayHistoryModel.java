package shop.imake.model;

/**
 * 手机充值历史
 */

public class TelPayHistoryModel {
    private String telNum;
    private String name;
    private String local;


    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
