package shop.imake.activity;
/**
 * 关于我么页面
 *
 * @author Alice
 * Creare 2016/8/27 19:57
 */

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;

import shop.imake.R;
import shop.imake.utils.AppUtils;
import shop.imake.utils.DialUtils;
import shop.imake.widget.IUUTitleBar;

import static shop.imake.R.id.ll_service_the_phone;
import static shop.imake.R.id.ll_supply_the_phone;


public class AboutIUUActivity extends BaseActivity implements View.OnClickListener {

    private IUUTitleBar mTitleBar;
    private TextView mTvVersions;//版本
    private LinearLayout mLLServerPhone;//客服电话
    private LinearLayout mLLSupplyPhone;//供货电话
    private String[] mPhones ;//电话数组

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
        mTvVersions = ((TextView) findViewById(R.id.tv_about_iuu_versions));
        mLLServerPhone = ((LinearLayout) findViewById(ll_service_the_phone));
        mLLSupplyPhone = ((LinearLayout) findViewById(ll_supply_the_phone));

    }

    private void setUpView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mLLServerPhone.setOnClickListener(this);
        mLLSupplyPhone.setOnClickListener(this);
    }

    private void setData() {
        mTvVersions.setText("v" + AppUtils.getVersionName(getApplicationContext()) + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;

            case R.id.ll_service_the_phone://客服电话
                mPhones=DialUtils.getPhoneNum(getApplicationContext(),DialUtils.SERVER_PHONE_TYPE);
                new AlertView("哎呦呦客服为您服务", null, "取消", null, mPhones,
                        this, AlertView.Style.ActionSheet, this).show();
                break;
            case ll_supply_the_phone://供货电话
                mPhones=DialUtils.getPhoneNum(getApplicationContext(),DialUtils.SUPPLY_PHONE_TYPE);
                new AlertView("哎呦呦客服为您服务", null, "取消", null, mPhones,
                        this, AlertView.Style.ActionSheet, this).show();

                break;

        }
    }


    /**
     * 电话弹出框条目点击
     * @param o
     * @param position
     */
    @Override
    public void onItemClick(Object o, int position) {
        //调用父类的方法给出提示
        super.onItemClick(o, position);
        if (position<0){
            return;
        }
        //拨打相应的电话
        DialUtils.callCentre(this, mPhones[position]);
    }
}
