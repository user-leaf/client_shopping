package com.bjaiyouyou.thismall.model;

/**
 * 购物车接口数据中的ProductImageModel类
 *
 * @author kanbin
 * @date 2016/7/17
 */
public class ProductImageModel {
    private String deleted_at;
    private String image_path; //http://139.129.167.60/storage/app/private/2016/06/03/product_100",
    private String image_base_name; //iamge_1.jpg",
    private String updated_at; //2016-07-09 09:57:25"

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
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
}
