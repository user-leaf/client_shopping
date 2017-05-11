package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.adapter.AddressAdapter;
import com.bjaiyouyou.thismall.model.AddressInfo;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理收货地址(旧)
 * @see AddressManagerNewActivity
 * 条目不可滑动
 *
 * @author JackB
 * @date 2016/6/6
 */
public class AddressManagerActivity extends BaseActivity implements View.OnClickListener {

    // 地址数据列表
    private ListView mListView;
    // 数据集
    private List<AddressInfo> mList;
    // 添加地址
    private TextView mTvAdd;
    // 地址列表适配器
    private AddressAdapter mAdapter;
    // 标题栏
    private IUUTitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);

        initView();
        setupView();
        initCtrl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: 2016/6/6 请求数据 更新列表
        setData();
    }

    private void initView() {
        mTitleBar = (IUUTitleBar) findViewById(R.id.address_title_bar);
        mList = new ArrayList<>();

        mListView = (ListView) findViewById(R.id.address_listview);
        mTvAdd = (TextView) findViewById(R.id.address_add);
    }

    private void setupView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mTvAdd.setOnClickListener(this);
    }

    private void initCtrl() {
        mAdapter = new AddressAdapter(this,mList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout: // 返回
                finish();
                break;
            case R.id.address_add: // 添加地址页
                jump(AddressAddActivity.class, false);
                break;
        }
    }

    /**
     * 请求并设置数据
     */
    public void setData() {
        /**
         * 测试数据
         */
        for (int i = 0; i < 15; i++) {
            AddressInfo addressInfo = new AddressInfo();
            addressInfo.setName("某某"+i);
            addressInfo.setTel("1316121142"+i);
            addressInfo.setAddress("东贸国际写字楼"+i);
            if (i == 3 ){
                addressInfo.setDefault(true);
            }else{
                addressInfo.setDefault(false);
            }
            mList.add(addressInfo);
        }

        mAdapter.setData(mList);
    }

}
