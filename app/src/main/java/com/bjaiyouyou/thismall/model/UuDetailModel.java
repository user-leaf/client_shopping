package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * UU详情类
 * @author Alice
 *Creare 2016/12/1 15:22
 */
public class UuDetailModel {


    /**
     * total : 13
     * per_page : 15
     * current_page : 1
     * last_page : 1
     * next_page_url : null
     * prev_page_url : null
     * from : 1
     * to : 13
     * data : [{"member_id":2111,"uu":"-50.00","type":"提现扣除","updated_at":"2016-11-22 14:43:04"},{"member_id":2111,"uu":"+65","type":"购物获得","updated_at":"2016-11-18 18:28:18"},{"member_id":2111,"uu":"+65","type":"购物获得","updated_at":"2016-11-18 18:28:01"},{"member_id":2111,"uu":"+65","type":"购物获得","updated_at":"2016-11-18 18:27:10"},{"member_id":2111,"uu":"+65","type":"购物获得","updated_at":"2016-11-18 18:26:52"},{"member_id":2111,"uu":"+65","type":"购物获得","updated_at":"2016-11-18 18:26:37"},{"member_id":2111,"uu":"+65","type":"购物获得","updated_at":"2016-11-18 18:25:39"},{"member_id":2111,"uu":"+150","type":"积分兑换","updated_at":"2016-11-09 21:33:58"},{"member_id":2111,"uu":"+100","type":"积分兑换","updated_at":"2016-11-09 21:33:21"},{"member_id":2111,"uu":"+1070","type":"充值UU","updated_at":"2016-11-09 20:24:26"},{"member_id":2111,"uu":"+1070","type":"充值UU","updated_at":"2016-11-09 20:21:31"},{"member_id":2111,"uu":"+18","type":"购物获得","updated_at":"2016-11-07 11:59:15"},{"member_id":2111,"uu":"-10.00","type":"提现扣除","updated_at":null}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private Object next_page_url;
    private Object prev_page_url;
    private int from;
    private int to;
    /**
     * member_id : 2111
     * uu : -50.00
     * type : 提现扣除
     * updated_at : 2016-11-22 14:43:04
     */

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

    public Object getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(Object next_page_url) {
        this.next_page_url = next_page_url;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
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
        private int member_id;
        private String uu;
        private String type;
        private String updated_at;

        public int getMember_id() {
            return member_id;
        }

        public void setMember_id(int member_id) {
            this.member_id = member_id;
        }

        public String getUu() {
            return uu;
        }

        public void setUu(String uu) {
            this.uu = uu;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
