package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 系统消息列表类
 *author Qxh
 *created at 2017/4/14 11:36
 */
public class PushMessage {

    /**
     * total : 25
     * per_page : 6
     * current_page : 1
     * last_page : 5
     * next_page_url : https://testapi2.bjaiyouyou.com/api/v1/message/getList?device_type=android&page=2
     * prev_page_url : null
     * from : 1
     * to : 6
     * data : [{"id":"33","title":"ddd","desc":"dd","content":"<p>dd<\/p>","updated_at":"2017-04-13 11:52:04","member_id":"0","is_read":1},{"id":"32","title":"dd'd","desc":"dd","content":"<p>dd<\/p>","updated_at":"2017-04-13 11:43:15","member_id":"0","is_read":1},{"id":"31","title":"3fdsfds","desc":"fdsfds","content":"<p>fdsfds<\/p>","updated_at":"2017-04-13 11:17:10","member_id":"0","is_read":1},{"id":"30","title":"dd'd","desc":"ddd","content":"<p>dd<\/p>","updated_at":"2017-04-13 11:15:01","member_id":"0","is_read":1},{"id":"28","title":"f'f'f","desc":"f'f'f","content":"<p>f&#39;f&#39;f&#39;f&#39;f&#39;f<\/p>","updated_at":"2017-04-11 18:46:06","member_id":"0","is_read":1},{"id":"27","title":"ddd","desc":"dd","content":"<p>dd<\/p>","updated_at":"2017-04-11 18:38:59","member_id":"0","is_read":1}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private String next_page_url;
    private Object prev_page_url;
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
        /**
         * id : 33
         * title : ddd
         * desc : dd
         * content : <p>dd</p>
         * updated_at : 2017-04-13 11:52:04
         * member_id : 0
         * is_read : 1
         */

        private String id;
        private String title;
        private String desc;
        private String content;
        private String updated_at;
        private String member_id;
        private int is_read;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public int getIs_read() {
            return is_read;
        }

        public void setIs_read(int is_read) {
            this.is_read = is_read;
        }
    }
}
