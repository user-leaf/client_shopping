package com.bjaiyouyou.thismall.model;

/**
 * d兑换成功数据类
 */
public class ExchangeResultModel {

    /**
     * id : 1701705032102025147
     * status : pending
     * object : withdrawal
     * app : app_aTKePC10Gub9908m
     * amount : 10100
     * asset_transaction : 310117050321020202959801
     * balance_transaction : 310317050321020202186501
     * channel : alipay
     * created : 1493816522
     * description : 换取兑换券
     * extra : {"account":"2088902384016188","name":"曲新航","account_type":"ALIPAY_USERID"}
     * failure_msg : null
     * fee : 0
     * livemode : false
     * metadata : {"order_no":"2017050321020216797505699"}
     * operation_url : null
     * order_no : 20170503210202673
     * source : tr_W1GqD8Ce5G4C5OOaDGfzbjvP
     * time_canceled : null
     * time_succeeded : null
     * user : ayouyou673
     * user_fee : 200
     */

    private String id;
    private String status;
    private String object;
    private String app;
    private int amount;
    private String asset_transaction;
    private String balance_transaction;
    private String channel;
    private int created;
    private String description;
    private ExtraBean extra;
    private Object failure_msg;
    private int fee;
    private boolean livemode;
    private MetadataBean metadata;
    private Object operation_url;
    private String order_no;
    private String source;
    private Object time_canceled;
    private Object time_succeeded;
    private String user;
    private int user_fee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAsset_transaction() {
        return asset_transaction;
    }

    public void setAsset_transaction(String asset_transaction) {
        this.asset_transaction = asset_transaction;
    }

    public String getBalance_transaction() {
        return balance_transaction;
    }

    public void setBalance_transaction(String balance_transaction) {
        this.balance_transaction = balance_transaction;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public Object getFailure_msg() {
        return failure_msg;
    }

    public void setFailure_msg(Object failure_msg) {
        this.failure_msg = failure_msg;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public Object getOperation_url() {
        return operation_url;
    }

    public void setOperation_url(Object operation_url) {
        this.operation_url = operation_url;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Object getTime_canceled() {
        return time_canceled;
    }

    public void setTime_canceled(Object time_canceled) {
        this.time_canceled = time_canceled;
    }

    public Object getTime_succeeded() {
        return time_succeeded;
    }

    public void setTime_succeeded(Object time_succeeded) {
        this.time_succeeded = time_succeeded;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getUser_fee() {
        return user_fee;
    }

    public void setUser_fee(int user_fee) {
        this.user_fee = user_fee;
    }

    public static class ExtraBean {
        /**
         * account : 2088902384016188
         * name : 曲新航
         * account_type : ALIPAY_USERID
         */

        private String account;
        private String name;
        private String account_type;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAccount_type() {
            return account_type;
        }

        public void setAccount_type(String account_type) {
            this.account_type = account_type;
        }
    }

    public static class MetadataBean {
        /**
         * order_no : 2017050321020216797505699
         */

        private String order_no;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }
    }
}
