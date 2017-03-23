package com.bjaiyouyou.thismall.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author QuXinhang
 *Creare 2016/9/21 15:45
 * 判断是否存在某个包
 *
 */
public class AppPackageChecked {
    private  appPackCheckedHaveCallBack callBack;

    /**
     * 判断指定软件是否存在
     * @param context
     * @param packageName
     * @return
     */
    private static boolean isExist(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if(pinfo != null){
            for(int i = 0; i < pinfo.size(); i++){
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    /**
     * 检查软件是否存在，存在在回调中进行相应操作，不存在下载软甲
     * @param context
     * @param packageName
     * @param activity
     * @param callback
     */
    public static void AppPageChecked(Context context, String packageName, Activity activity, appPackCheckedHaveCallBack callback){
        /**
         * 1、根据线上反馈，判断方法在有的机型会出问题，微信已经安装但是检测不到
         * 2、如果微信未安装，Ping++的返回结果中pay_result会返回invalid，可据此处理，此处判断没有必要
         *
         * Created by kanbin on 2017/3/23.
         */
        throw new UnsupportedOperationException("请直接调用支付方法，无需判断，如果支付APP未安装，pay_result会返回invalid，可据此处理");

//        //判断后的逻辑：
//        //已安装，打开程序，需传入参数包名："com.skype.android.verizon"
//        if(isExist(context,packageName)){
//           //软件存在吊起支付
//            callback.isHave();
//        }
//        //未安装，跳转至market下载该程序
//        else {
//            Toast.makeText(context,"本机缺少支付操作必要的软件\n请下载安装后再试",Toast.LENGTH_SHORT).show();
//            //下载安装必要软件
//            Uri uri = Uri.parse("market://details?id="+packageName);//id为包名
//            Intent it = new Intent(Intent.ACTION_VIEW, uri);
//            activity.startActivity(it);
//        }
    }

    public interface appPackCheckedHaveCallBack{
          void isHave();
    }
}
