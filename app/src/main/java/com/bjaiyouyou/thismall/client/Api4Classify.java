package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.fragment.ClassifyDetailFragment;
import com.bjaiyouyou.thismall.fragment.ClassifyPage;
import com.bjaiyouyou.thismall.model.ClassifyCateAdModel;
import com.bjaiyouyou.thismall.model.ClassifyProductModel;
import com.bjaiyouyou.thismall.model.ClassifyOneCateModel;
import com.bjaiyouyou.thismall.model.ClassifyTwoCateModel;
import com.bjaiyouyou.thismall.utils.LogUtils;

/**
 * Created by Administrator on 2017/3/29.
 */
public class Api4Classify extends BaseClientApi {

    /**
     * 根据一级分类获取二级分类项列表
     *
     * @param oneLevelCateId 一级分类id
     * @param callback
     */
    public void getTwoLevelCate(int oneLevelCateId, DataCallback<ClassifyTwoCateModel> callback) {

        String url = ClientAPI.API_POINT + "api/v1/category/getTwoLevelCate/" + oneLevelCateId;

        LogUtils.d(TAG, "getTwoLevelCate: " + url);

        doGet(url, ClassifyDetailFragment.TAG, null, callback);
    }

    /**
     * 根据一级分类获取广告数据
     *
     * @param oneLevelCateId 一级分类id
     * @param callback
     */
    public void getCateAd(int oneLevelCateId, DataCallback<ClassifyCateAdModel> callback) {

        String url = ClientAPI.API_POINT + "api/v1/category/getProductCateAd/" + oneLevelCateId;

        LogUtils.d(TAG, "getCateAd: " + url);

        doGet(url, ClassifyDetailFragment.TAG, null, callback);
    }

    /**
     * 根据层级分类获取商品列表数据
     *
     * @param pid      分类id
     * @param callback
     */
    public void getProductsData(int level, int pid, int pageNo, DataCallback<ClassifyProductModel> callback) {
        StringBuilder stringBuilder = new StringBuilder(ClientAPI.API_POINT);
        if (level == -1) { // 推荐
            stringBuilder.append("api/v1/product/recommendProduct")
                    .append("?page=").append(pageNo);

        } else if (level == 1) { // 一级分类
            stringBuilder.append("api/v1/product/getOneCateProducts/")
                    .append(pid);
        } else { // 二级分类
            stringBuilder.append("api/v1/product/getTwoCateProducts/")
            .append(pid);
        }

        String url = stringBuilder.toString();

        LogUtils.d(TAG, "getProductsData: " + url);

        doGet(url, ClassifyDetailFragment.TAG, null, callback);
    }


    /**
     * 获取一级分类项列表
     *
     * @param callback
     */
    public void getOneLevelCate(DataCallback<ClassifyOneCateModel> callback) {
        //一级分类地址
        // https://testapi2.bjaiyouyou.com/api/v1/category/getAll

        String url = ClientAPI.API_POINT + "api/v1/category/getAll";

        LogUtils.d(TAG, "getOneLevelCate: " + url);

        doGet(url, ClassifyPage.TAG, null, callback);
    }

    /**
     * 获取可搜索商品数量
     *
     * @param callback
     */
    public void getClassifyGoodsNumber(DataCallback<String> callback) {
        //https://testapi2.bjaiyouyou.com/api/v1/product/getProductCount  获取商品数量  搜索栏

        String url = ClientAPI.API_POINT + "api/v1/product/getProductCount";

        LogUtils.d(TAG, "getClassifyGoodsNumber: " + url);

        doGet(url, ClassifyPage.TAG, null, callback);
    }


}
