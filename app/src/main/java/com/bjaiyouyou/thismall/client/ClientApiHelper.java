package com.bjaiyouyou.thismall.client;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * ClientAPI实体管理类
 * <p>
 * Created by kanbin on 2016/12/8.
 */
public class ClientApiHelper {
    public static final String TAG = ClientApiHelper.class.getSimpleName();

    private static ClientApiHelper mInstance;

    private Map<Class<? extends BaseClientApi>, BaseClientApi> clientMap = new HashMap<>();

    private Context mApplicationContext;

    /**
     * 设置全局上下文
     *
     * @param context
     */
    public void setApplicationContext(Context context) {
        this.mApplicationContext = context;
    }

    private ClientApiHelper() {
    }

    /** 获取client的实体 **/
    public BaseClientApi getClientApi(Class<? extends BaseClientApi> clazz) {
        BaseClientApi clientAPI = clientMap.get(clazz);
        if (clientAPI == null) {
            synchronized (ClientApiHelper.class) {
                if (clientMap.get(clazz) == null) {
                    try {
                        Class itemClazz = Class.forName(clazz.getName());
                        BaseClientApi instance = (BaseClientApi) itemClazz.newInstance();
                        clientMap.put(clazz, instance);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();

                    } catch (InstantiationException e) {
                        e.printStackTrace();

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                return clientMap.get(clazz);
            }
        }
        return clientMap.get(clazz);
    }

    public static ClientApiHelper getInstance() {
        if (mInstance != null) {
            return mInstance;
        } else {
            synchronized (ClientApiHelper.class) {
                if (mInstance == null) {
                    mInstance = new ClientApiHelper();
                }
                return mInstance;
            }
        }
    }
}
