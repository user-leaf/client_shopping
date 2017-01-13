package com.bjaiyouyou.thismall.model;

/**
 * 购物车接口数据中的Product类
 *
 * @author kanbin
 * @date 2016/7/17
 */
public class ProductModel {

    /**
     * id : 1101
     * name : TCL液晶平板电视
     * score : 0
     * onsell : 0
     * product_type : 1
     * deleted_at :
     * updated_at : 2016-10-25 10:18:08
     */

    private long id;
    private String name;
    private int score;
    private int onsell;
    private int product_type;
    private String deleted_at;
    private String updated_at;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getOnsell() {
        return onsell;
    }

    public void setOnsell(int onsell) {
        this.onsell = onsell;
    }

    public int getProduct_type() {
        return product_type;
    }

    public void setProduct_type(int product_type) {
        this.product_type = product_type;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
