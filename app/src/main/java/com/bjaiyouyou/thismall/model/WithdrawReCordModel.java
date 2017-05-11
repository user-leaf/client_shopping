package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 提现记录类
 * @author Alice
 *Creare 2016/11/1 16:55
 *
 *
 */
public class WithdrawReCordModel {

    /**
     * total : 2
     * per_page : 10
     * current_page : 1
     * last_page : 1
     * next_page_url : null
     * prev_page_url : null
     * from : 1
     * to : 2
     * data : [{"id":5,"member_id":25,"order_number":"","drawings":"10.00","created_at":"2016-10-27 17:34:54"},{"id":4,"member_id":25,"order_number":"2016112012346485","drawings":"12.00","created_at":"2016-10-04 17:30:10"}]
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
     * id : 5
     * member_id : 25
     * order_number :
     * drawings : 10.00
     * created_at : 2016-10-27 17:34:54
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
        private int id;
        private int member_id;
        private String order_number;
        private String drawings;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMember_id() {
            return member_id;
        }

        public void setMember_id(int member_id) {
            this.member_id = member_id;
        }

        public String getOrder_number() {
            return order_number;
        }

        public void setOrder_number(String order_number) {
            this.order_number = order_number;
        }

        public String getDrawings() {
            return drawings;
        }

        public void setDrawings(String drawings) {
            this.drawings = drawings;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
