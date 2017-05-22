package shop.imake.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Alice
 *Creare 2016/8/16 9:54
 *
 *
 */
public class NetStateUtils {
    /**
     *  判断网络连接状态
     * @param context
     * @return
     */

    public  static boolean isNetworkAvailable(Context context){
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager =null;
        if (context!= null){
             connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    //状态====0====UNKNOWN
                    //状态====1====CONNECTED
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    //类型===0===MOBILE
                    //类型===1===WIFI
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
