package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 一级分类列表数据
 *author Qxh
 *created at 2017/3/29 13:14
 */
public class ClassifyOneCateModel {

    private List<OneCateListBean> OneCateList;

    public List<OneCateListBean> getOneCateList() {
        return OneCateList;
    }

    public void setOneCateList(List<OneCateListBean> OneCateList) {
        this.OneCateList = OneCateList;
    }

    public static class OneCateListBean {
        /**
         * cate_name : 酒水饮料
         * id : 1
         */

        private String cate_name;
        private int id;

        public String getCate_name() {
            return cate_name;
        }

        public void setCate_name(String cate_name) {
            this.cate_name = cate_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
