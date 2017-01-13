package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class TaskModel {
    /**
     * total : 12
     * per_page : 6
     * current_page : 1
     * last_page : 2
     * next_page_url : http://api.bjaiyouyou.com/api/v1/task/index2?page=2
     * prev_page_url :
     * from : 1
     * to : 6
     * data : [{"id":78,"gold":2,"name":"东方神参","path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/12/20/app_task_78/default_0.mp4","time":"00:00:45.62","image_path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/12/20/app_task_78","image_base_name":"default_0.mp4.jpg","updated_at":"2016-12-20 17:42:56","if_is_complete":false},{"id":76,"gold":2,"name":"产品宣传片","path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/11/11/app_task_76/default_0.mp4","time":"00:04:44.47","image_path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/11/11/app_task_76","image_base_name":"default_0.mp4.jpg","updated_at":"2016-11-11 18:19:33","if_is_complete":false},{"id":75,"gold":2,"name":"产品宣传片","path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/11/11/app_task_75/default_0.mp4","time":"00:05:04.27","image_path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/11/11/app_task_75","image_base_name":"default_0.mp4.jpg","updated_at":"2016-11-11 18:18:49","if_is_complete":false},{"id":74,"gold":2,"name":"砭石神器DC插头使用须知","path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/11/11/app_task_74/default_0.mp4","time":"00:00:25.94","image_path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/11/11/app_task_74","image_base_name":"default_0.mp4.jpg","updated_at":"2016-11-11 18:18:13","if_is_complete":false},{"id":72,"gold":2,"name":"砭石神器操作","path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/11/11/app_task_72/default_0.mp4","time":"00:02:36.97","image_path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/11/11/app_task_72","image_base_name":"default_0.mp4.jpg","updated_at":"2016-11-11 18:17:21","if_is_complete":false},{"id":71,"gold":2,"name":"艾益生坐灸神器","path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/11/11/app_task_71/default_0.mp4","time":"00:03:15.16","image_path":"http://api.bjaiyouyou.com/storage/app/public/video/2016/11/11/app_task_71","image_base_name":"default_0.mp4.jpg","updated_at":"2016-11-11 18:16:56","if_is_complete":false}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private String next_page_url;
    private String prev_page_url;
    private int from;
    private int to;
    /**
     * id : 78
     * gold : 2
     * name : 东方神参
     * path : http://api.bjaiyouyou.com/storage/app/public/video/2016/12/20/app_task_78/default_0.mp4
     * time : 00:00:45.62
     * image_path : http://api.bjaiyouyou.com/storage/app/public/video/2016/12/20/app_task_78
     * image_base_name : default_0.mp4.jpg
     * updated_at : 2016-12-20 17:42:56
     * if_is_complete : false
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
        private long id;
        private int gold;
        private String name;
        private String path;
        private String time;
        private String image_path;
        private String image_base_name;
        private String updated_at;
        private boolean if_is_complete;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public String getImage_base_name() {
            return image_base_name;
        }

        public void setImage_base_name(String image_base_name) {
            this.image_base_name = image_base_name;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public boolean isIf_is_complete() {
            return if_is_complete;
        }

        public void setIf_is_complete(boolean if_is_complete) {
            this.if_is_complete = if_is_complete;
        }
    }

}
