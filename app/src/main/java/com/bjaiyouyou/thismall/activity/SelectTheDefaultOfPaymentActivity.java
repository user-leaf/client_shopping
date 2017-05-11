package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
/**
 *
 * @author Alice
 *Creare 2016/6/18 11:16
 * 选择默认支付方式
 *
 */

public class SelectTheDefaultOfPaymentActivity extends BaseActivity implements View.OnClickListener
{
    //银行卡
    private FrameLayout mRLYinHangKa;
//    微信
    private View mRlWeiXin;
//    支付宝
    private FrameLayout mRlZhiFuBao;
    private IUUTitleBar mTitleBar;
//    条目数组
    private View[] items;
//    默认选择的条目ID
    private int selectID=-1;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_the_default_of_payment);
        intent=getIntent();
        selectID=intent.getIntExtra("state",-1);
        initView();
        setBackGround(selectID);
        setUpView();
    }

    private void setUpView() {
        mRlWeiXin.setOnClickListener(this);
        mRlZhiFuBao.setOnClickListener(this);
        mRLYinHangKa.setOnClickListener(this);
        mTitleBar.setLeftLayoutClickListener(this);
    }

    private void initView() {
        mRlWeiXin = ((View) findViewById(R.id.fl_weixin));
        mRlZhiFuBao = ((FrameLayout) findViewById(R.id.fl_zhifubao));
        mRLYinHangKa = ((FrameLayout) findViewById(R.id.fl_yihangka));
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_select_default_Payment));
        items=new View[]{mRlWeiXin,mRlZhiFuBao,mRLYinHangKa};
        setBackGround(selectID);
    }

    @Override
    public void onClick(View v) {
        //选择某个默认付款方式后向后台提交
        switch (v.getId()){
            case R.id.fl_weixin:
                selectID=0;
                setBackGround(selectID);
            break;
            case R.id.fl_zhifubao:
                selectID=1;
                setBackGround(selectID);

                break;
            case R.id.fl_yihangka:
                selectID=2;
                setBackGround(selectID);
                break;

            case R.id.left_layout:
                setResult(102,intent);
                finish();
            break;

        }
    }

    public void setBackGround(int backGround) {
        intent.putExtra("selectID",backGround);
        for (int i=0;i<items.length;i++){
            if (i==backGround){
                items[i].setBackgroundColor(Color.RED);
            }
            else {
                items[i].setBackgroundColor(Color.WHITE);
            }
        }
    }
}
