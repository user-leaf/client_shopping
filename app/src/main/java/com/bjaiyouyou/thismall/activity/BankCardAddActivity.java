package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
/**
 *
 * @author Alice
 *Creare 2016/6/14 18:31
 *
 * 添加银行卡页面
 */

public class BankCardAddActivity extends BaseActivity implements View.OnClickListener{
    //下一页按钮
    private Button mBtNext;
    //标题栏
    private IUUTitleBar mTitleBar;
    //持卡人编辑框
    private EditText mEtCardHolder;
    //卡号编辑框
    private EditText mEtCardNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_card_add);
        infoView();
        setUpView();
    }

    private void infoView() {
        mBtNext = ((Button) findViewById(R.id.bt_bankcard_add_next));
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_bankcard_add));
        mEtCardHolder = ((EditText) findViewById(R.id.et_cardholder_name));
        mEtCardNum = ((EditText) findViewById(R.id.et_card_num));
    }

    private void setUpView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mBtNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case  R.id.left_layout:
               finish();
               break;
            case  R.id.bt_bankcard_add_next:
                jump(BankcardInformationAddActivity.class,false);
                break;


        }
    }
}
