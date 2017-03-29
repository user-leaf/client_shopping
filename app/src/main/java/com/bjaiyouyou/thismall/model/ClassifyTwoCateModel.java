package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 二级分类列表数据
 *
 * Created by Administrator on 2017/3/29.
 */
public class ClassifyTwoCateModel {

    /**
     * cate_name : 饭桌
     * id : 5
     */

    private List<TwoCateListBean> TwoCateList;

    public List<TwoCateListBean> getTwoCateList() {
        return TwoCateList;
    }

    public void setTwoCateList(List<TwoCateListBean> TwoCateList) {
        this.TwoCateList = TwoCateList;
    }

    public static class TwoCateListBean {
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
