package com.bjaiyouyou.thismall.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.picker.AssetsUtils;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import cn.qqtheme.framework.picker.AddressPicker;

/**
 * 获取地址数据并显示地址选择器
 *
 * @author 李玉江[QQ:1032694760]
 * @version 2015/12/15
 */
public class AddressInitTask extends AsyncTask<String, Void, ArrayList<AddressPicker.Province>> {
    private Activity activity;
    private ProgressDialog dialog;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private boolean hideCounty = false;
    // 要显示的控件
    // 省
    private TextView mTvProvince;
    // 市
    private TextView mTvCity;
    // 区县
    private TextView mTvCounty;

    /**
     * 初始化为不显示区县的模式
     *
     * @param activity
     * @param hideCounty is hide County
     */
    public AddressInitTask(Activity activity, boolean hideCounty) {
        this.activity = activity;
        this.hideCounty = hideCounty;
        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
    }

    public AddressInitTask(Activity activity) {
        this.activity = activity;
        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
    }


    public AddressInitTask(Activity activity, TextView province, TextView city, TextView county) {
        this.activity = activity;
        dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
        mTvProvince = province;
        mTvCity = city;
        mTvCounty = county;
    }

    @Override
    protected ArrayList<AddressPicker.Province> doInBackground(String... params) {
        if (params != null) {
            switch (params.length) {
                case 1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
                default:
                    break;
            }
        }
        ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();
        try {
            String json = AssetsUtils.readText(activity, "city.json");
            data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<AddressPicker.Province> result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (result.size() > 0) {
            AddressPicker picker = new AddressPicker(activity, result);
            picker.setTextSize(14);
            picker.setAnimationStyle(R.style.address_picker_anim_style);
            picker.setLineColor(activity.getResources().getColor(R.color.app_gray_dcdcdc));
            picker.setTextColor(activity.getResources().getColor(R.color.black));
            picker.setHideCounty(hideCounty);
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(String province, String city, String county) {
                    if (county == null) {
//                        Toast.makeText(activity, province + city, Toast.LENGTH_LONG).show();
                        mTvProvince.setText(province);
                        mTvCity.setText(city);
                        mTvCounty.setText(null);
                    } else {
//                        Toast.makeText(activity, province + city + county, Toast.LENGTH_LONG).show();
                        mTvProvince.setText(province);
                        mTvCity.setText(city);
                        mTvCounty.setText(county);
                    }
                }
            });
            picker.show();
        } else {
            Toast.makeText(activity, "数据初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

}
