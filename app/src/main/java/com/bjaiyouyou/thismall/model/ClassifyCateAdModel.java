package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 分类详情页广告数据格式
 *
 * Created by kanbin on 2017/3/29.
 */
public class ClassifyCateAdModel {

    /**
     * image_path : storage/app/public/banner/2017/03/06/app_23
     * link : https://testwxweb.bjaiyouyou.com/advert_youfenback.html
     * image_base_name : default_0_1488782932.png
     */

    private List<ProductCateAdsBean> ProductCateAds;

    public List<ProductCateAdsBean> getProductCateAds() {
        return ProductCateAds;
    }

    public void setProductCateAds(List<ProductCateAdsBean> ProductCateAds) {
        this.ProductCateAds = ProductCateAds;
    }

    public static class ProductCateAdsBean {
        private String image_path;
        private String link;
        private String image_base_name;

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImage_base_name() {
            return image_base_name;
        }

        public void setImage_base_name(String image_base_name) {
            this.image_base_name = image_base_name;
        }
    }
}
