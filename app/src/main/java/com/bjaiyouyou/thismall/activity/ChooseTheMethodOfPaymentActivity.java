package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author QuXinhang
 *Creare 2016/6/14 14:09
 * 选择付款方式页面
 *
 */
public class ChooseTheMethodOfPaymentActivity extends BaseActivity implements AdapterView.OnItemClickListener,View.OnClickListener{
    //支付方式列表
    private ListView lv;
    //列表数据
    private List<Map<String ,Object>> data;
    //图文混排适配器
    private SimpleAdapter adapter;
    //标题
    private IUUTitleBar titleBar;
//  记录当前默认条目
    private int mSelectID=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_the_method_of_payment);

        lv = ((ListView) findViewById(R.id.lv_choose_payment_method));
        titleBar = ((IUUTitleBar) findViewById(R.id.title_choose_payment_method));
        titleBar.setLeftLayoutClickListener(this);
        //选择默认支付方式
//        titleBar.setRightLayoutClickListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        data=new ArrayList<>();
        initData();
        adapter=new SimpleAdapter(this,data,R.layout.item_choose_payment_method,new String[] {"name","state","img"},
                new int[]{R.id.tv_choose_payment_method,R.id.tv_choose_payment_method_state,R.id.iv_choose_payment_method});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    private void initData() {
        Map<String ,Object> map1=new HashMap<>();
        map1.put("name","微信支付");
        map1.put("img",R.mipmap.list_edit);
        Map<String ,Object> map2=new HashMap<>();
        map2.put("name","支付宝");
        map2.put("img",R.mipmap.list_edit);
        Map<String ,Object> map3=new HashMap<>();
        map3.put("name","银行卡");
        map3.put("img",R.mipmap.list_edit);

        map1.put("state","");
        map2.put("state","");
        map3.put("state","");
        if (mSelectID==0){
            map1.put("state","（默认支付）");
        }
        if (mSelectID==1){
            map2.put("state","（默认支付）");
        }
        if (mSelectID==2){
            map3.put("state","（默认支付）");
        }

        data.add(map1);
        data.add(map2);
        data.add(map3);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                Toast.makeText(this,"微信支付",Toast.LENGTH_SHORT).show();
                jump(WechatAddActivity.class,false);
                break;
            case 1:
                Toast.makeText(this,"支付宝",Toast.LENGTH_SHORT).show();
                jump(ZhiFuBaoAddActivity.class,false);
                break;
            case 2:
                jump(BankCardAddActivity.class,false);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_layout:
                finish();
                break;
            case R.id.right_layout://设置默认付款方式
                Intent intent=new Intent(this,SelectTheDefaultOfPaymentActivity.class);
                intent.putExtra("state",mSelectID);
                jump(101,intent,false);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101&&resultCode==102){
//            上传到网络然后
            mSelectID = data.getIntExtra("selectID",-1);
            Log.e("selectID",""+mSelectID);
        }
    }
}
