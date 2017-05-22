package shop.imake.model;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ScanPayModel {

    /**
     * id : 310317051613235902052502
     * object : balance_transaction
     * app : app_aTKePC10Gub9908m
     * amount : -10000
     * available_balance : 980581
     * created : 1494912239
     * description : 余额转账
     * livemode : false
     * source : testcoupon41487
     * type : transfer
     * user : testcoupon675
     */

    private String id;
    private String object;
    private String app;
    private int amount;
    private int available_balance;
    private int created;
    private String description;
    private boolean livemode;
    private String source;
    private String type;
    private String user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAvailable_balance() {
        return available_balance;
    }

    public void setAvailable_balance(int available_balance) {
        this.available_balance = available_balance;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
