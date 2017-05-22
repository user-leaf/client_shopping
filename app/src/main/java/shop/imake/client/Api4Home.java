package shop.imake.client;

import android.text.TextUtils;

import shop.imake.callback.DataCallback;
import shop.imake.fragment.HomePage;
import shop.imake.model.HomeAdBigModel;
import shop.imake.model.HomeNavigationItemNew;
import shop.imake.model.HomeProductModel;
import shop.imake.model.IsHaveMessageNotRead;
import shop.imake.model.ScanPayModel;
import shop.imake.model.ShopModel;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by Administrator on 2017/2/16.
 */
public class Api4Home extends BaseClientApi {

    /**
     * Created byJackB
     * on 2017/2/16.
     */
    /**
     * 首页广告数据请求
     *
     * @param callback
     */
    public void getHomeAdData(String strTag, DataCallback<HomeAdBigModel> callback) {

        String url = ClientAPI.API_POINT + "api/v1/banner/index";

        LogUtils.d(TAG, "getHomeAdData: " + url);

        doGet(url, strTag, null, callback);
    }
    /**
     *
     *author Alice
     *created at 2017/3/10 15:48
     * 获取首页为您推荐部分的信息
     *
     * @param strTag
     * @param page
     * @param callback

     */

    public void getRecommendData(String strTag, int page,DataCallback<HomeProductModel> callback) {

        String url = ClientAPI.API_POINT + HttpUrls.EVERYDAY_NEW +page;

        LogUtils.d(TAG, "getRecommendData: " + url);

        doGet(url, strTag, null, callback);
    }
    /**
     *
     *author Alice
     *created at 2017/3/24 10:47
     *
     * 获取抢购信息
     *
     * @param strTag
     * @param callback
     */
    public void getNavigation(String strTag,DataCallback<HomeNavigationItemNew> callback){
        String url =ClientAPI.API_POINT + HttpUrls.PANICBUY_NEW;
        LogUtils.d(TAG, "getNavigation: " + url);
        doGet(url, strTag, null, callback);
    }

    /**
     * 是否存在未读取信息
     *author Alice
     *created at 2017/4/14 17:03
     */
    public void isHaveMessageNotRead(DataCallback<IsHaveMessageNotRead> callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT );
        sb.append("api/v1/message/isHasNoRead");
        if(!TextUtils.isEmpty(CurrentUserManager.getUserToken())){
            sb.append("?token=" + CurrentUserManager.getUserToken());
        }
//        sb.append("&device_type=" + "android");
        String url = sb.toString();

        LogUtils.d(TAG, "isHaveMessageNotRead: " + url);
        doGet(url, HomePage.TAG, null, callback);
    }
    /**
     *author Alice
     *created at 2017/3/24 10:47
     *
     * 获取抢购信息
     * @param callback
     */
    public void getPushMessage(int page,StringCallback callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/message/getList");
        sb.append("?page=");
        sb.append(page);
        if(!TextUtils.isEmpty(CurrentUserManager.getUserToken())){
            sb.append("&token=" + CurrentUserManager.getUserToken());
        }
//        sb.append("&device_type=" + "android");
        String url = sb.toString();
        LogUtils.d("getPushMessage",url);

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(callback);

    }
    /**
     * 删除系统消息
     *author Alice
     *created at 2017/4/19 14:48
     */

    public void deletePushMessage(String id,StringCallback callback){
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);
        sb.append("api/v1/message/delete/");
        sb.append(id);
        sb.append("?token=" + CurrentUserManager.getUserToken());
//        sb.append("&device_type=" + "android");
        String url = sb.toString();
        LogUtils.e("deletePushMessage",url);

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(callback);

    }

    /**
     * 扫码支付 - 获取商户信息
     * @param tag
     * @param shopId
     * @param callback
     */
    public void getShopInfo(Object tag, long shopId, DataCallback<ShopModel> callback){
        StringBuilder stringBuilder = new StringBuilder(ClientAPI.API_POINT);
        stringBuilder.append(HttpUrls.URL_SCAN_PAY_SHOP_INFO).append(shopId)
                .append("?token=").append(CurrentUserManager.getUserToken());
        String url = stringBuilder.toString();
        LogUtils.d(TAG, "getShopInfo url: " + url);

        doGet(url, tag, null, callback);
    }

    /**
     * 扫码支付 - 支付
     * @param tag
     * @param safecode
     * @param callback
     */
    public void payAfterScan(Object tag, double amount, String safecode, long shopId, DataCallback<ScanPayModel> callback){
        StringBuilder stringBuilder = new StringBuilder(ClientAPI.API_POINT);
        stringBuilder.append(HttpUrls.URL_SCAN_PAY_QRCODEPAY)
                .append("?amount=").append(amount)
                .append("&security_code=").append(safecode)
                .append("&seller_user_id=").append(shopId)
                .append("&token=").append(CurrentUserManager.getUserToken());
        String url = stringBuilder.toString();
        LogUtils.d(TAG, "payAfterScan url: " + url);

        doPost(url, tag, null, callback);
    }
}
