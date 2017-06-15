package shop.imake.activity;
/**
 * 关于我么页面
 * @author Alice
 *Creare 2016/8/27 19:57
 *
 *
 */
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.utils.AppUtils;
import shop.imake.utils.DialUtils;
import shop.imake.widget.IUUTitleBar;

import static shop.imake.R.id.ll_service_the_phone;
import static shop.imake.R.id.ll_supply_the_phone;


public class AboutIUUActivity extends BaseActivity implements View.OnClickListener{

    private IUUTitleBar mTitleBar;
    private View mCompanyView;
    private TextView mTvVersions;
    private LinearLayout mLLServerPhone;
    private LinearLayout mLLSupplyPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_iuu);
        initView();
        setUpView();
        setData();
    }



    private void initView() {
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_about_iuu));
        mCompanyView = findViewById(R.id.tv_about_iuu_company);
        mTvVersions = ((TextView) findViewById(R.id.tv_about_iuu_versions));
        mLLServerPhone = ((LinearLayout) findViewById(ll_service_the_phone));
        mLLSupplyPhone = ((LinearLayout) findViewById(ll_supply_the_phone));

    }

    private void setUpView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mCompanyView.setOnClickListener(this);
        mLLServerPhone.setOnClickListener(this);
        mLLSupplyPhone.setOnClickListener(this);
    }
    private void setData() {
        mTvVersions.setText("v"+AppUtils.getVersionName(getApplicationContext())+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_layout:
                finish();
                break;

            case R.id.tv_about_iuu_company:
//                jump(MineRechargeSuccessActivity.class, false);
//                jump(ScanPayActivity.class, false);
//                WebShowActivity.actionStart(this, "http://192.168.8.140:8080?vt=" + System.currentTimeMillis(), WebShowActivity.PARAM_PAGE_HIDE);
                break;

            case R.id.ll_service_the_phone://客服电话
                DialUtils.callCentre(this,DialUtils.CENTER_NUM);
                break;
            case ll_supply_the_phone://客服电话
                DialUtils.callCentre(this,DialUtils.SUPPLY_PHONE);
                break;

        }
    }
}
