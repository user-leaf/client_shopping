package com.bjaiyouyou.thismall.client;

import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.fragment.ClassifyDetailFragment;
import com.bjaiyouyou.thismall.fragment.ClassifyPage;
import com.bjaiyouyou.thismall.model.ClassifyCateAdModel;
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
     * 根据一级分类获取二级分类项列表
     *
     * @param callback
     */
    public void getOneLevelCate(DataCallback<ClassifyOneCateModel> callback) {
        //一级分类地址
        // https://testapi2.bjaiyouyou.com/api/v1/category/getAll

        String url = ClientAPI.API_POINT + "api/v1/category/getAll" ;

        LogUtils.d(TAG, "getOneLevelCate: " + url);

        doGet(url, ClassifyPage.TAG, null, callback);
    }

}
