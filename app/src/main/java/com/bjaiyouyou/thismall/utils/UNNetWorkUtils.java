package com.bjaiyouyou.thismall.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 断网处理
 * @author QuXinhang
 *Creare 2016/8/18 9:49
 */
public class UNNetWorkUtils {
    /**
     * 判断是否有网
     * @param context
     * @param mLLNotLogin
     * @param mTvDataEmpty
     * @param lv
     * @param mLLUnNetWork
     * @param ivEmpty
     */
    public static void isNetHaveConnect(Context context,LinearLayout mLLNotLogin, TextView mTvDataEmpty, View lv , LinearLayout mLLUnNetWork, View ivEmpty){
        if (NetStateUtils.isNetworkAvailable(context)){
            lvHide(mLLNotLogin,mTvDataEmpty,lv,mLLUnNetWork,ivEmpty);
        }else {
            Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show();
            unNetWorkView(mLLNotLogin,mTvDataEmpty,lv,mLLUnNetWork,ivEmpty);
        }
    }

    /**
     * 数据正常
     * @param mLLNotLogin 没登录布局
     * @param mTvDataEmpty 数据加载中
     * @param lv 数据列表
     * @param ivEmpty  成功加载数据为空
     * @param mLLUnNetWork 断网设置
     */
    public static void lvShow(LinearLayout mLLNotLogin, TextView mTvDataEmpty, View lv ,LinearLayout mLLUnNetWork,View ivEmpty){
        mLLNotLogin.setVisibility(View.GONE);

        mTvDataEmpty.setVisibility(View.VISIBLE);
        lv.setVisibility(View.VISIBLE);
        ivEmpty.setVisibility(View.GONE);

        mLLUnNetWork.setVisibility(View.GONE);
    }

    /**
     * 没登录
     * @param mLLNotLogin
     * @param mTvDataEmpty
     * @param lv
     * @param mLLUnNetWork
     * @param ivEmpty
     */
    public static void lvHide(LinearLayout mLLNotLogin, TextView mTvDataEmpty, View lv ,LinearLayout mLLUnNetWork,View ivEmpty){
        mLLNotLogin.setVisibility(View.VISIBLE);
        mTvDataEmpty.setVisibility(View.GONE);
        lv.setVisibility(View.GONE);
        mLLUnNetWork.setVisibility(View.GONE);
        ivEmpty.setVisibility(View.GONE);
    }

    /**
     * 网络未连接
     * @param mLLNotLogin
     * @param mTvDataEmpty
     * @param lv
     * @param mLLUnNetWork
     * @param ivEmpty
     */
    public static void unNetWorkView(LinearLayout mLLNotLogin, TextView mTvDataEmpty, View lv ,LinearLayout mLLUnNetWork,View ivEmpty){
        mLLNotLogin.setVisibility(View.GONE);
        mTvDataEmpty.setVisibility(View.GONE);
        lv.setVisibility(View.GONE);
        mLLUnNetWork.setVisibility(View.VISIBLE);
        ivEmpty.setVisibility(View.GONE);
    }

    /**
     * 数据为空
     * @param mLLNotLogin
     * @param mTvDataEmpty
     * @param lv
     * @param mLLUnNetWork
     * @param ivEmpty
     */

    public static void dataEmpty(LinearLayout mLLNotLogin, TextView mTvDataEmpty, View lv ,LinearLayout mLLUnNetWork,View ivEmpty){
        mLLNotLogin.setVisibility(View.GONE);
        mTvDataEmpty.setVisibility(View.GONE);
        lv.setVisibility(View.GONE);
        mLLUnNetWork.setVisibility(View.GONE);
        ivEmpty.setVisibility(View.VISIBLE);
    }


    /**
     * 不需要登录
     * 不需要当数据为空显示
     *
     * @param context
     * @param mTvDataEmpty
     * @param lv
     * @param mLLUnNetWork
     */
    public static void isNetHaveConnect(Context context,TextView mTvDataEmpty, View lv , LinearLayout mLLUnNetWork){
        Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show();
        unNetWorkView(mTvDataEmpty,lv,mLLUnNetWork);
    }
    /**
     * 数据正常
     * @param mTvDataEmpty 数据加载中
     * @param lv 数据列表
     * @param mLLUnNetWork 断网设置
     */
    public static void lvShow( View mTvDataEmpty, View lv ,LinearLayout mLLUnNetWork){

        mTvDataEmpty.setVisibility(View.VISIBLE);
        lv.setVisibility(View.VISIBLE);
        mLLUnNetWork.setVisibility(View.GONE);
    }

    /**
     * 网络未连接
     * @param mTvDataEmpty
     * @param lv
     * @param mLLUnNetWork
     */
    public static void unNetWorkView( TextView mTvDataEmpty, View lv ,LinearLayout mLLUnNetWork){
        mTvDataEmpty.setVisibility(View.GONE);
        lv.setVisibility(View.GONE);
        mLLUnNetWork.setVisibility(View.VISIBLE);
    }

    /**
     * 只需要提示的网络状态判断
     */
    public static void unNetWorkOnlyNotify(Context context,Exception e){
        String eString= e.toString();
        LogUtils.e("--eString:",eString);
//        eString=eString.substring(eString.length()-3,eString.length());
        if (NetStateUtils.isNetworkAvailable(context)){
            if (eString!=null){
                if (eString.contains("400")||eString.contains("401")){
                    Toast.makeText(context,"请登录后再次操作",Toast.LENGTH_SHORT).show();
                }else {
//                Toast.makeText(context,"提交失败"+e.toString(),Toast.LENGTH_SHORT).show();
                    ToastUtils.showException(e,context);
                }
            }
        }else {
            Toast.makeText(context,"网络连接失败，请检查网络设置后再次操作",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 需要登录
     *
     * @param context
     * @param notLoginView
     * @param LoginView
     * @param mLLUnNetWork
     */
    public static void isNetHaveConnect(Context context,View notLoginView, View LoginView , LinearLayout mLLUnNetWork){
        Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show();
        unNetWorkView(notLoginView,LoginView,mLLUnNetWork);
    }


    /**
     * 数据正常
     * @param notLoginView 数据加载中
     * @param LoginView 数据列表
     * @param mLLUnNetWork 断网设置
     */
    public static void lvShow(ImageView notLoginView, View LoginView  , LinearLayout mLLUnNetWork){

        notLoginView.setVisibility(View.GONE);
        LoginView.setVisibility(View.VISIBLE);
        mLLUnNetWork.setVisibility(View.GONE);
    }
    /**
     * 未登录
     * @param notLoginView 数据加载中
     * @param LoginView 数据列表
     * @param mLLUnNetWork 断网设置
     */
    public static void notLogin(View notLoginView, View LoginView  ,LinearLayout mLLUnNetWork){

        notLoginView.setVisibility(View.VISIBLE);
        LoginView.setVisibility(View.GONE);
        mLLUnNetWork.setVisibility(View.GONE);
    }

    /**
     * 网络未连接
     * @param notLoginView
     * @param LoginView
     * @param mLLUnNetWork
     */
    public static void unNetWorkView( View notLoginView, View LoginView  ,LinearLayout mLLUnNetWork){
        notLoginView.setVisibility(View.GONE);
        LoginView.setVisibility(View.GONE);
        mLLUnNetWork.setVisibility(View.VISIBLE);
    }

}
