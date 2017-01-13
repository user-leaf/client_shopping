package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 热门搜索类
 * @author QuXinhang
 *Creare 2016/8/10 11:06
 *
 *
 */
public class SearchHot {

    /**
     * id : 8
     * keyword : 5
     * number : 2
     */

    private List<SearchRecordsBean> search_records;

    public List<SearchRecordsBean> getSearch_records() {
        return search_records;
    }

    public void setSearch_records(List<SearchRecordsBean> search_records) {
        this.search_records = search_records;
    }

    public static class SearchRecordsBean {
        private int id;
        private String keyword;
        private int number;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
