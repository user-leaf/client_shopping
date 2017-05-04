package com.bjaiyouyou.thismall.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.bjaiyouyou.thismall.fragment.PayDetailFragment;

/**
 * Created by Administrator on 2017/4/21.
 */
public class PayUtils {
    public static final String EXTRA_MONEY = "money";
    public static final String EXTRA_ORDER_NUM = "order_num";

    /**
     * 支付
     * @param fragmentManager
     * @param tag       Fragment的TAG，根据TAG，fragmentManager可查找到该fragment
     * @param money     支付金额
     */
    public static void pay(FragmentManager fragmentManager, String tag, double money, PayDetailFragment.PayCallback callback){
        PayDetailFragment payDetailFragment = new PayDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(EXTRA_MONEY, money);
        payDetailFragment.setArguments(bundle);
        payDetailFragment.setOnPayCallback(callback);
        payDetailFragment.show(fragmentManager, tag);
    }
}
