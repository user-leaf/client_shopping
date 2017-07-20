package shop.imake.callback;

import android.content.Context;

import shop.imake.utils.LogUtils;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 将服务器返回的数据转换成接口需要的参数类型
 * <p>
 * Created by JackB on 2016/12/8.
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
        String str = response.body().string();
        if (response != null && !"[]".equals(response)) {
            try {
                Gson gson = new Gson();
                T t = gson.fromJson(str, mType);

                return t;
            } catch (Exception e) {
                // 上传响应数据及错误信息到友盟统计
                MobclickAgent.reportError(mContext,
                        "[" + mContext.getClass() + "]"
                                + "\n[" + mType + "]"
                                + "\n[gson] " + e.getMessage()
                                + "\n[json] " + str
                );
                LogUtils.e(TAG, e.getMessage());
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
        if (e.getMessage().contains("bjaiyouyou.com")) {
            onFail(call, new IOException("网络请求失败，请检查网络后重试"), id);
            return;
        }
        onFail(call, e, id);
    }

    // 自定义code > 0 时走的回调
    @Override
    public void onError(Call call, String responseBody, int id) {
        onFail(call, responseBody, id);
    }

    public abstract void onFail(Call call, Exception e, int id);

    public void onFail(Call call, String responseBody, int id) {

    }

    public abstract void onSuccess(Object response, int id);

}
