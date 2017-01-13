package com.bjaiyouyou.thismall.model;

/**
 * 历史搜索条目类
 * @author QuXinhang
 *Creare 2016/9/14 18:04
 */
public class HistorySearchItem {
    private String name;

    public HistorySearchItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
