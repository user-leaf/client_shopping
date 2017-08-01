package shop.imake.utils;

import android.content.Context;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import shop.imake.MainApplication;
import shop.imake.client.ClientAPI;

/**
 * 极光推送相关
 * Created by SONY on 2017/8/1.
 */
public class JPushUtils {

    // 设置别名
    public static void setAlias(String alias) {
        if (ClientAPI.isFlag_test()){
            alias = "test" + alias;
        }

        final String finalAlias = alias;
        JPushInterface.setAlias(MainApplication.getContext(), alias, new TagAliasCallback() {
            @Override
            public void gotResult(int code, String s, Set<String> set) {
                switch (code) {
                    case 0:
                        break;

                    case 6002:
                        setAlias(finalAlias);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    public static void deleteAlias(){
        JPushInterface.setAlias(MainApplication.getContext(), "", new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {

            }
        });
    }
}
