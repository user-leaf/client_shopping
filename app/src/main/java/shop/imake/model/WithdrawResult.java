package shop.imake.model;

/**
 * 提现结果信息
 * @author Alice
 *Creare 2016/9/18 20:13
 *
 *
 */
public class WithdrawResult {

    /**
     * id : tr_HqbzHCvLOaL4La1ezHfDWTqH
     * object : transfer
     * type : b2c
     * created : 1432724825
     * time_transferred : null
     * livemode : true
     * status : pending
     * app : app_1Gqj58ynP0mHeX1q
     * channel : wx_pub
     * order_no : 123456789
     * amount : 100
     * amount_settle : 100
     * currency : cny
     * recipient : o7zpMs5MW2-52GAy5hRrjdYVCktU
     * description : Your Description
     * transaction_no : 1000018301201505200184147302
     * failure_msg : null
     * extra : {"user_name":"User Name","force_check":true}
     */

    private String id;
    private String object;
    private String type;
    private int created;
    private Object time_transferred;
    private boolean livemode;
    private String status;
    private String app;
    private String channel;
    private String order_no;
    private int amount;
    private int amount_settle;
    private String currency;
    private String recipient;
    private String description;
    private String transaction_no;
    private Object failure_msg;
    /**
     * user_name : User Name
     * force_check : true
     */

    private ExtraBean extra;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public Object getTime_transferred() {
        return time_transferred;
    }

    public void setTime_transferred(Object time_transferred) {
        this.time_transferred = time_transferred;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount_settle() {
        return amount_settle;
    }

    public void setAmount_settle(int amount_settle) {
        this.amount_settle = amount_settle;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
    }

    public Object getFailure_msg() {
        return failure_msg;
    }

    public void setFailure_msg(Object failure_msg) {
        this.failure_msg = failure_msg;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public static class ExtraBean {
        private String user_name;
        private boolean force_check;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public boolean isForce_check() {
            return force_check;
        }

        public void setForce_check(boolean force_check) {
            this.force_check = force_check;
        }
    }
}
