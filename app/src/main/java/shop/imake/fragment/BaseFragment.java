package shop.imake.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.bigkoo.alertview.OnItemClickListener;
import shop.imake.R;
import shop.imake.utils.LogUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.Utility;
import shop.imake.widget.IUUTitleBar;
import shop.imake.widget.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;

public class BaseFragment extends Fragment implements View.OnClickListener, OnItemClickListener {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    protected IUUTitleBar titleBar;
    protected InputMethodManager inputMethodManager;
    protected View layout;

    public LoadingDialog loadingDialog;
    private int loadingCount = 0;
    private long lastClick = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("@@@", "onCreate");
        if (savedInstanceState != null) {
            LogUtils.d("@@@", "onCreate, savedInstanceState != null");
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            LogUtils.d("@@@", "onCreate()--isSupportHidden: " + isSupportHidden);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        titleBar = (IUUTitleBar) getView().findViewById(R.id.title_bar);
        loadingDialog = LoadingDialog.getInstance(getContext());
//        initView();
//        setUpView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RequestManager.cancelAll(this);
        OkHttpUtils.getInstance().cancelTag(this);
    }

    public void onBackPressed() {
//        super.onBackPressed();
        getActivity().onBackPressed();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
        LogUtils.d("@@@", "onSaveInstanceState");
        LogUtils.d("@@@", "onSaveInstanceState()--isSupportHidden: " + isHidden());
    }

    /**
     * ???????????????
     */
    public void showTitleBar() {
        if (titleBar != null) {
            titleBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ???????????????
     */
    public void hideTitleBar() {
        if (titleBar != null) {
            titleBar.setVisibility(View.GONE);
        }
    }

    /**
     * ????????????
     */
    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    /**
//     * ???????????????
//     */
//    protected abstract void initView();
//
//    /**
//     * ????????????????????????
//     */
//    protected abstract void setUpView();


    /**
     * View??????
     **/
    public void widgetClick(View v) {

    }

    @Override
    public void onClick(View v) {

        if (!Utility.isFastDoubleClick()) {
            widgetClick(v);
        }
    }

//    /**
//     * [??????????????????]
//     *
//     * @return
//     */
//    private boolean fastClick() {
//        long currentTime = System.currentTimeMillis();
//        if (currentTime - lastClick <= 1000) {
//            return true;
//        }
//        lastClick = currentTime;
//        return false;
//    }

    /**
     * [????????????]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * [???????????????????????????]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getContext(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * [??????Bundle??????Class??????????????????]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getContext(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void showLoadingDialog() {
        if (loadingCount <= 0) {
            if (loadingDialog != null) {
                loadingDialog.show();

            }
        }

        loadingCount++;
    }

    public void dismissLoadingDialog() {
        if (loadingCount <= 1) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();

            }
        }

        loadingCount--;
    }

    @Override
    public void onItemClick(Object o, int position) {

        if (position < 0) { // ??????
            return;
        }

        switch (position) {
            case 0: // ???????????????
                break;

            case 1: // ????????????
                ToastUtils.showShort("???????????????");
                break;

            case 2: // ????????????
                ToastUtils.showShort("???????????????");
                break;

            default:
                break;
        }
    }
}
