package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * Created by Alice
 * 2017/5/13
 */
public class MyCommissionModel {

    /**
     * total : 4
     * per_page : 15
     * current_page : 1
     * last_page : 1
     * next_page_url :
     * prev_page_url :
     * from : 1
     * to : 4
     * data : [{"member_id":41487,"push_money":"+5000.00","type":"1","created_at":"2017-05-12 18:55:46","word":"直接对接返佣收益"},{"member_id":41487,"push_money":"+5000.00","type":"1","created_at":"2017-05-12 14:44:40","word":"直接对接返佣收益"},{"member_id":41487,"push_money":"+1000.00","type":"1","created_at":"2017-05-02 15:24:48","word":"直接对接返佣收益"},{"member_id":41487,"push_money":"+1000.00","type":"2","created_at":"2017-05-02 15:24:48","word":"协助对接返佣金收益"}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private String next_page_url;
    private String prev_page_url;
    private int from;
    private int to;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * member_id : 41487
         * push_money : +5000.00
         * type : 1
         * created_at : 2017-05-12 18:55:46
         * word : 直接对接返佣收益
         */

        private int member_id;
        private String push_money;
        private String type;
        private String created_at;
        private String word;

        public int getMember_id() {
            return member_id;
        }

        public void setMember_id(int member_id) {
            this.member_id = member_id;
        }

        public String getPush_money() {
            return push_money;
        }

        public void setPush_money(String push_money) {
            this.push_money = push_money;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }
}
