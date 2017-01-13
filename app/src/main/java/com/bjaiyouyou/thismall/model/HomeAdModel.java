package com.bjaiyouyou.thismall.model;

/**
 * 首页广告轮播
 *
 * @author kanbin
 * @date 2016/7/12
 */
public class HomeAdModel {
    private String link; //http://www.baidu.com",
    private String image_path; //storage/app/private/2016/06/03/product_100",
    private String image_base_name; //iamge_1.jpg"

    public HomeAdModel() {
    }

    public HomeAdModel(String link, String image_path, String image_base_name) {
        this.link = link;
        this.image_path = image_path;
        this.image_base_name = image_base_name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
    }}
