package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.callback.DataCallback;
import com.bjaiyouyou.thismall.client.Api4Mine;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.client.ClientApiHelper;
import com.bjaiyouyou.thismall.model.ZhongHuiQuanModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

import okhttp3.Call;

/**
 * 众汇券页面
 *author Alice
 *created at 2017/5/12 18:26
 */
public class MyZhongHuiQuanActivity extends BaseActivity {
    public static String TAG=MyZhongHuiQuanActivity.class.getSimpleName();

    private IUUTitleBar mTitle;
    //众汇券明细入口
    private TextView mTvZHQDetail;
    //众汇券总数控价
    private TextView mTvZHQNum;
    //可使用众汇券控件
    private TextView mTvZHQCanUse;
    //冻结众汇券控价
    private TextView mTvZHQDongjieNum;

    private Api4Mine mApi4Mine;
    private ZhongHuiQuanModel mZhangHuiQuanModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhong_hui_quan);
        initView();
        initVariable();
        initData();
        setupView();

    }

    private void initView() {
        mTitle = ((IUUTitleBar) findViewById(R.id.title_zhonghuiquan));
        mTvZHQDetail = ((TextView) findViewById(R.id.tv_into_zhonghuiquan_detail));
        mTvZHQNum = ((TextView) findViewById(R.id.tv_zhonghuiquan_num));
        mTvZHQCanUse = ((TextView) findViewById(R.id.tv_zhonghuiquan_can_use_num));
        mTvZHQDongjieNum = ((TextView) findViewById(R.id.tv_zhonghuiquan_dongjie_num));

    }

    private void setupView() {
        mTitle.setLeftLayoutClickListener(this);
        mTvZHQDetail.setOnClickListener(this);

    }
    private void initVariable() {
        mApi4Mine= (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
    }

    private void initData() {
        showLoadingDialog();
        mApi4Mine.getZhongHuiQuanData(this,new DataCallback<ZhongHuiQuanModel>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                dismissLoadingDialog();
                LogUtils.d("getZhongHuiQuanData",e.getMessage());
            }

            @Override
            public void onSuccess(Object response, int id) {
                dismissLoadingDialog();
                if (response!=null){
                    mZhangHuiQuanModel=(ZhongHuiQuanModel) response;
                    setDate();
                }

            }
        });

    }

    private void setDate() {
        mTvZHQNum.setText(mZhangHuiQuanModel.getAll_balance()+"");
        mTvZHQCanUse.setText(mZhangHuiQuanModel.getCan_use_balance()+"");
        mTvZHQDongjieNum.setText(mZhangHuiQuanModel.getFreeze_balance()+"");
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            case R.id.left_layout://关闭
                finish();
                break;
            case R.id.tv_into_zhonghuiquan_detail://进入众汇券明细
//                DataHolder.getInstance().setData(MyZhongHuiQuanActivity.TAG);
//                jump(MyCommissionDetailActivity.class,false);
//                ToastUtils.showShort("吊起WebView");
                intoDetail();
                break;
            default:
                return;
        }
    }
    /**
     * 跳转众汇券详情
     */
    private void intoDetail() {
        StringBuffer sb=new StringBuffer(ClientAPI.URL_WX_H5);
        sb.append("myduihuanquan-role.html");
        sb.append("?type=");
        sb.append("zhonghui");
        sb.append("&token=");
        sb.append(CurrentUserManager.getUserToken());

        String webShowUrl=sb.toString().trim();
        WebShowActivity.actionStart(MyZhongHuiQuanActivity.this,webShowUrl, null);
    }
}
