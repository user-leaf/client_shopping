package com.bjaiyouyou.thismall.callback;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 将服务器返回的数据转换成接口需要的参数类型
 * <p/>
 * Created by kanbin on 2016/12/8.
 */
public abstract class DataCallback<T> extends Callback {
    public static final String TAG = DataCallback.class.getSimpleName();

    private Context mContext;
    private Type mType;

    public DataCallback(Context context) {
        this.mContext = context;
        mType = getSuperclassTypeParameter(getClass());
    }

    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        if (response != null && !"[]".equals(response)) {
            try {
                Gson gson = new Gson();
                String str = response.body().string();
                T t = gson.fromJson(str, mType);

//            BaseBean baseBean = (BaseBean) t;
//            baseBean.code = response.code();
//            baseBean.desc = str;
                return t;
            }catch (Exception e){
                // TODO: 2017/2/14 解析错误，上传响应数据及错误信息到友盟统计

                return null;
            }
        }
        return null;
    }

    @Override
    public void onResponse(Object response, int id) {
        onSuccess(response, id);
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onFail(call, e, id);
    }

    public abstract void onFail(Call call, Exception e, int id);

    public abstract void onSuccess(Object response, int id);

}