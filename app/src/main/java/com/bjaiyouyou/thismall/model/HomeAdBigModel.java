package com.bjaiyouyou.thismall.model;

import java.util.List;

/**
 * 首页广告轮播
 *
 * @author kanbin
 * @date 2016/7/12
 */
public class HomeAdBigModel {
    private List<HomeAdModel> banners;

    public List<HomeAdModel> getBanners() {
        return banners;
    }

    public void setBanners(List<HomeAdModel> banners) {
        this.banners = banners;
    }
}
