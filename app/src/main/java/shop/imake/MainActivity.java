package shop.imake;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import shop.imake.activity.BaseActivity;
import shop.imake.activity.PermissionsActivity;
import shop.imake.client.Api4Mine;
import shop.imake.client.ClientApiHelper;
import shop.imake.fragment.BaseFragment;
import shop.imake.fragment.CartPage;
import shop.imake.fragment.ClassifyPage;
import shop.imake.fragment.HomePage;
import shop.imake.fragment.MinePage;
import shop.imake.fragment.TaskPage;
import shop.imake.utils.ACache;
import shop.imake.utils.DialUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.NTalkerUtils;
import shop.imake.utils.UpdateUtils;

public class MainActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String STATE_CURRENT_TAB_INDEX = "STATE_CURRENT_TAB_INDEX";
    public static final String PARAM_ORDER = "order";
    public static final String ACTIVATE_EXCHANGE = "ACTIVATE_EXCHANGE";

    private long mExitTime;

    private RadioButton mController0;
    private RadioButton mController1;
    //    private RadioButton mController2;
    private RadioButton mController3;
    private RadioButton mController4;

    private BaseFragment cacheFragment;

    // 第0页：首页
    private HomePage mHomePage;
    // 第1页：分类页
    private ClassifyPage mClassifyPage;
    // 第2页：任务页
//    private TaskPage mTaskPage;
    // 第3页：购物车页
    private CartPage mCartPage;
    // 第4页：个人页
    private MinePage mMinePage;

    RadioButton[] mTabs;
    private int index;
    private int currentTabIndex;
    private android.support.v4.app.Fragment[] fragments;

    // 升级
    private UpdateUtils mUpdateUtils;

    //////////处理客服电话
    private Api4Mine mClientApi;//网络请求对象

    public static String serverPhone[];//客服电话
    public static String supplyPhone[];//供货电话


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


        if (savedInstanceState == null) {
            setupView();

        } else { // 内存重启
            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof HomePage) {
                    mHomePage = (HomePage) fragment;
                    fragments[0] = mHomePage;
                } else if (fragment instanceof ClassifyPage) {
                    mClassifyPage = (ClassifyPage) fragment;
                    fragments[1] = mClassifyPage;
                } else if (fragment instanceof TaskPage) {
//                    mTaskPage = (TaskPage) fragment;
//                    fragments[2] = mTaskPage;
                } else if (fragment instanceof CartPage) {
                    mCartPage = (CartPage) fragment;
                    fragments[3] = mCartPage;
                } else if (fragment instanceof MinePage) {
                    mMinePage = (MinePage) fragment;
                    fragments[4] = mMinePage;
                }
            }
            currentTabIndex = savedInstanceState.getInt(STATE_CURRENT_TAB_INDEX, 0);

            LogUtils.d("@@@", "MainActivity, onCreate - currentTabIndex: " + currentTabIndex);
        }

        mUpdateUtils = new UpdateUtils();
        // 检查升级
        mUpdateUtils.checkUpdate(this, null);
        //获取电话信息
        getTelephone();
        // 小能客服登录
        NTalkerUtils.getInstance().login();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_TAB_INDEX, currentTabIndex);
        LogUtils.d("@@@", "MainActivity, onSaveInstanceState - currentTabIndex: " + currentTabIndex);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        fragments = new Fragment[5];

        mController0 = (RadioButton) findViewById(R.id.controller_zero);
        mController1 = (RadioButton) findViewById(R.id.controller_one);
        mController3 = (RadioButton) findViewById(R.id.controller_three);
        mController4 = (RadioButton) findViewById(R.id.controller_four);

        mTabs = new RadioButton[5];
        mTabs[0] = mController0;
        mTabs[1] = mController1;
        mTabs[3] = mController3;
        mTabs[4] = mController4;
//        mTabs[0].setSelected(true);
    }

    /**
     * 设置属性、监听等
     */
    private void setupView() {

        // 将四个页面加入到栈中，并持有第一个页面的引用
        mHomePage = new HomePage();
        mClassifyPage = new ClassifyPage();
        mCartPage = new CartPage();
        mMinePage = new MinePage();

        fragments[0] = mHomePage;
        fragments[1] = mClassifyPage;
        fragments[3] = mCartPage;
        fragments[4] = mMinePage;

        getSupportFragmentManager().beginTransaction().add(R.id.container, mHomePage, mHomePage.TAG)
                .add(R.id.container, mClassifyPage, mClassifyPage.TAG).hide(mClassifyPage)
//                .add(R.id.container, mTaskPage, mTaskPage.TAG).hide(mTaskPage)
//                .add(R.id.container,mCartPage, mCartPage.TAG).hide(mCartPage)
//                .add(R.id.container, mMinePage, mMinePage.TAG).hide(mMinePage)
                .show(mHomePage)
                .commit();
    }

    /**
     * button点击事件
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.controller_zero:
                index = 0;
                if (fragments[index] == null) {
                    fragments[index] = new HomePage();
                }
                break;
            case R.id.controller_one:
                index = 1;
                if (fragments[index] == null) {
                    fragments[index] = new ClassifyPage();
                }
                break;

            case R.id.controller_three:
                index = 3;
                if (fragments[index] == null) {
                    fragments[index] = new CartPage();
                }
                break;
            case R.id.controller_four:
                index = 4;
                if (fragments[index] == null) {
                    fragments[index] = new MinePage();
                }
                break;
        }

        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        LogUtils.d("@@@", "MainActivity, onTabClicked - currentTabIndex: " + currentTabIndex);
        mTabs[currentTabIndex].setChecked(false);
        // 把当前tab设为选中状态
        mTabs[index].setChecked(true);
        currentTabIndex = index;

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            if ((System.currentTimeMillis()-mExitTime)>2000){
//                Object mHelperUtils;
//                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
//                mExitTime = System.currentTimeMillis();
//            }else {
//                finish();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 获取HomePage页的WebView，判断是否还能返回上一页，
        android.support.v4.app.Fragment homePage = getSupportFragmentManager().findFragmentByTag(HomePage.TAG);
        WebView webview = (WebView) homePage.getView().findViewById(R.id.home_webview);

        // 打开APP直接点击返回（不进行WebView操作）时
        if ((keyCode == KeyEvent.KEYCODE_BACK) && !webview.canGoBack() && ((System.currentTimeMillis() - mExitTime) > 2000)) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    ////////////////////////处理我的订单返回跳转+商品详情直接跳转到购物车
    public void goToBuyCar() {
        index = 3;
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.container, fragments[index]);
            }
//                trx.show(fragments[index]).commit();
            /**
             报错Can not perform this action after onSaveInstanceState
             大致意思是说我使用的 commit方法是在Activity的onSaveInstanceState()之后调用的，这样会出错，因为
             onSaveInstanceState方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存玩状态后
             再给它添加Fragment就会出错。解决办法就是把commit（）方法替换成 commitAllowingStateLoss()就行
             了，其效果是一样的。
             */
            trx.show(fragments[index]).commitAllowingStateLoss();
        }

        mTabs[currentTabIndex].setChecked(false);
        // 把当前tab设为选中状态
        mTabs[index].setChecked(true);
        currentTabIndex = index;
    }

//    public void goToTask() {
//        index = 2;
//        if (currentTabIndex != index) {
//            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
//            trx.hide(fragments[currentTabIndex]);
//            if (!fragments[index].isAdded()) {
//                trx.add(R.id.container, fragments[index]);
//            }
////                trx.show(fragments[index]).commit();
//            /**
//             报错Can not perform this action after onSaveInstanceState
//             大致意思是说我使用的 commit方法是在Activity的onSaveInstanceState()之后调用的，这样会出错，因为
//             onSaveInstanceState方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存玩状态后
//             再给它添加Fragment就会出错。解决办法就是把commit（）方法替换成 commitAllowingStateLoss()就行
//             了，其效果是一样的。
//             */
//            trx.show(fragments[index]).commitAllowingStateLoss();
//        }
//
//        mTabs[currentTabIndex].setChecked(false);
//        // 把当前tab设为选中状态
//        mTabs[index].setChecked(true);
//        currentTabIndex = index;
//    }

    //获得intent
    @Override
    public void onNewIntent(Intent intent) {
        String order = intent.getStringExtra(PARAM_ORDER);
        LogUtils.d(TAG, "order: " + order);
        //到购物车
        if ("order".equals(order)) {
            goToBuyCar();
        }
//        //到任务
//        String intentString=intent.getStringExtra(ACTIVATE_EXCHANGE);
//        if (ACTIVATE_EXCHANGE.equals(intentString)) {
//            goToTask();
//        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == UpdateUtils.REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mUpdateUtils.downFile(UpdateUtils.sDownloadUrl, this);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == DialUtils.REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            Toast.makeText(getApplicationContext(), "未授权，不能拨打电话", Toast.LENGTH_SHORT).show();
//            finish();
        } else if (requestCode == DialUtils.REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
            DialUtils.callCentre(this, ACache.get(getApplicationContext()).getAsString(DialUtils.PHONE_GET_SAFE_CODE_KEY));
        }
    }


    /**
     * 获取客服,供货电话
     */
    private void getTelephone() {
        mClientApi = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
        mClientApi.getTelephone(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                //请求失败再次请求
                getTelephone();
            }

            @Override
            public void onResponse(String response, int id) {
                if (TextUtils.isEmpty(response)){
                    return;
                }
                ACache.get(getApplicationContext()).put(DialUtils.PHONE_JSON_KEY, response.trim());
                //用于缓存默认的安全码找回的客电话
                DialUtils.getPhoneNum(getApplicationContext(),DialUtils.SERVER_PHONE_TYPE);
                LogUtils.e("telephone",response);

            }
        });


    }

}
