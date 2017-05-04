package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 激活结果
 * Created by IUU
 * 2017/4/26
 */
public class ActivateResultMode {

    /**
     * object : list
     * url : /v1/apps/app_aTKePC10Gub9908m/balance_transactions
     * has_more : false
     * data : [{"id":"310317042618580601961201","object":"balance_transaction","app":"app_aTKePC10Gub9908m","amount":20000,"available_balance":222000,"created":1493204286,"description":"用户释放金额到余额","livemode":false,"source":null,"type":"receipts_earning","user":"ayouyou20683"}]
     */

    private String object;
    private String url;
    private boolean has_more;
    private List<DataBean> data;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 310317042618580601961201
         * object : balance_transaction
         * app : app_aTKePC10Gub9908m
         * amount : 20000
         * available_balance : 222000
         * created : 1493204286
         * description : 用户释放金额到余额
         * livemode : false
         * source : null
         * type : receipts_earning
         * user : ayouyou20683
         */

        private String id;
        private String object;
        private String app;
        private int amount;
        private int available_balance;
        private int created;
        private String description;
        private boolean livemode;
        private Object source;
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

        public Object getSource() {
            return source;
        }

        public void setSource(Object source) {
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
}
