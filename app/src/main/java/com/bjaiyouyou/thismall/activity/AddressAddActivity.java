package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.AddressInfo2;
import com.bjaiyouyou.thismall.task.AddressInitTask;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.ToastUtils;
import com.bjaiyouyou.thismall.utils.ValidateUserInputUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.kyleduo.switchbutton.SwitchButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * type==0 添加地址页
 * type==1 修改地址页
 * <p/>
 * 地址选择器控件使用的是 https://github.com/gzu-liyujiang/AndroidPicker
 *
 * @author kanbin
 * @date 2016/6/6
 */
public class AddressAddActivity extends BaseActivity implements View.OnClickListener {

    private static final java.lang.String TAG = AddressAddActivity.class.getSimpleName();

    private IUUTitleBar mTitleBar;

    // 地区
    private View mDistrict;
    // 省、市、县
    private TextView mTvProvince;
    private TextView mTvCity;
    private TextView mTvCounty;

    // 收货人
    private EditText mEtName;
    // 手机号码
    private EditText mEtTel;
    // 街道地址
    private EditText mEtAddress;
    // 是否默认地址(新)
    private SwitchButton mSbIsDefault;
    private TextView mTvSave;
    // 修改地址
    // 页面类型 0：添加新地址  1：修改地址
    private int mPageType;
    // 要修改的地址的id
    private long mAddressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);

        initView();
        setupView();

        // 获取页面类型（添加地址、修改地址）
        mPageType = getIntent().getIntExtra("pageType", Constants.ADDRESS_ADD);

        // 如果页面类型为“修改地址”，给页面内各项设置为修改前的数值
        if (mPageType == Constants.ADDRESS_EDIT) { // 页面类型为 修改地址
            mTitleBar.setTitle("修改收货地址");

            AddressInfo2.MemberAddressesBean mab = (AddressInfo2.MemberAddressesBean) getIntent().getSerializableExtra("address");
            mAddressId = mab.getId();
            // 设置上次地址
            mEtName.setText(mab.getContact_person());
            mEtTel.setText(mab.getContact_phone());
            mTvProvince.setText(mab.getProvince());
            mTvCity.setText(mab.getCity());
            mTvCounty.setText(mab.getDistrict());
            mEtAddress.setText(mab.getAddress_detail());
            mSbIsDefault.setChecked(mab.isIs_default());
        }
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.address_add_title_bar);
        mEtName = (EditText) findViewById(R.id.address_add_et_name);
        mEtTel = (EditText) findViewById(R.id.address_add_et_tel);
        mDistrict = findViewById(R.id.address_add_district);

        mTvProvince = (TextView) findViewById(R.id.address_add_tv_province);
        mTvCity = (TextView) findViewById(R.id.address_add_tv_city);
        mTvCounty = (TextView) findViewById(R.id.address_add_tv_county);

        mEtAddress = (EditText) findViewById(R.id.address_add_et_address);
        mSbIsDefault = (SwitchButton) findViewById(R.id.address_add_sb_isdefault);

        mTvSave = (TextView) findViewById(R.id.address_add_tv_save);

    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mDistrict.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: //返回
                finish();
                break;
            case R.id.address_add_district: //所在地区
                /**
                 * 选择器指向上次选择的地址，默认值为"北京市北京市东城区"
                 */
                String province = mTvProvince.getText().toString();
                String city = mTvCity.getText().toString();
                String county = mTvCounty.getText().toString();
                if (TextUtils.isEmpty(province) || TextUtils.isEmpty(city) || TextUtils.isEmpty(county)) {
                    province = "北京";
                    city = "北京";
                    county = "东城";
                }

                new AddressInitTask(this, mTvProvince, mTvCity, mTvCounty).execute(province, city, county);
                break;
            case R.id.address_add_tv_save: // 保存
                doSave();
                break;
        }
    }

    /**
     * 保存
     */
    private void doSave() {
        // 输入检查
        String name = mEtName.getText().toString().trim();
        String tel = mEtTel.getText().toString().trim();
        String province = mTvProvince.getText().toString().trim();
        String city = mTvCity.getText().toString().trim();
        String county = mTvCounty.getText().toString().trim();
        String street = mEtAddress.getText().toString().trim();

//        LogUtils.d("AddressAddActivity", "name=" + name + ",tel=" + tel + ",province" + province + ",city=" + city + ",county=" + county + ",street=" + street);

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请填写收货人姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.length()<2){
            ToastUtils.showShort("收货人姓名至少2个汉字");
            return;
        }

        if (name.length()>15){
            ToastUtils.showShort("收货人姓名至多15个汉字");
            return;
        }

        if (TextUtils.isEmpty(tel)) {
            Toast.makeText(this, "请填写收手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidateUserInputUtils.validateUserPhone(tel)){
            ToastUtils.showShort("手机号码有误");
            return;
        }

        if (TextUtils.isEmpty(province) || TextUtils.isEmpty(city) || TextUtils.isEmpty(county)) {
            Toast.makeText(this, "请选择所在地区", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(street)) {
            Toast.makeText(this, "请填写街道地址", Toast.LENGTH_SHORT).show();
            return;
        }

        if (street.length()<5){
            ToastUtils.showShort("街道地址至少5个汉字");
            return;
        }

        if (street.length()>60){
            ToastUtils.showShort("街道地址至多60个汉字");
            return;
        }

        String userToken = CurrentUserManager.getUserToken();
        StringBuilder sb = new StringBuilder(ClientAPI.API_POINT);

        if (mPageType == Constants.ADDRESS_EDIT) { //页面类型为修改地址页
            sb.append("api/v1/member/editAddress/")
                    .append(mAddressId)
                    .append("?token=")
                    .append(userToken);

        } else { // 添加新地址页
            sb.append("api/v1/member/addAddress")
                    .append("?token=").append(userToken);
        }

        String url = sb.toString();

        OkHttpUtils.post()
                .url(url)
                .addParams("is_default",""+(mSbIsDefault.isChecked()==true? 1:0))
                .addParams("contact_person", name)
                .addParams("contact_phone", tel)
                .addParams("province", province)
                .addParams("city", city)
                .addParams("district", county)
                .addParams("address_detail", street)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showException(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        finish();
                    }
                });
    }

}
