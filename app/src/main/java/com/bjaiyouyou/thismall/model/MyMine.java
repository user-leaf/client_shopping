package com.bjaiyouyou.thismall.model;

/**
 *
 * @author QuXinhang
 *Creare 2016/6/7 15:11
 * 我页面的数据类型类
 *
 */
public class MyMine {
    String name;
    int img;

    public MyMine(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
