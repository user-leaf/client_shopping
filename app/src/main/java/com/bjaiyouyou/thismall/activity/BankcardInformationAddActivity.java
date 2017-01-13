package com.bjaiyouyou.thismall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
/**
 *
 * @author QuXinhang
 *Creare 2016/6/14 18:31
 * 添加银行卡信息页面
 *
 */
public class BankcardInformationAddActivity extends BaseActivity implements View.OnClickListener {

    //标题栏
    private IUUTitleBar mTitleBar;
    //储存卡类型借记卡还是信用卡
    private View mCardTypeName;
    //电话号码编辑框
    private EditText mEtTeleponeNumber;
    //下页按钮
    private Button mBtNext;
    //服务协议入口
    private TextView mTvServerAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcard_information_add);
        infoView();
        setUpView();
    }

    private void infoView() {
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_bank_card_information));
        mCardTypeName = ((View) findViewById(R.id.ll_card_information_name));
        mEtTeleponeNumber = ((EditText) findViewById(R.id.et_card_information_telephonenum));
        mBtNext = ((Button) findViewById(R.id.bt_bankcard_information_next));
        mTvServerAgreement = ((TextView) findViewById(R.id.tv_bank_information_service_agreement));
    }

    private void setUpView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mCardTypeName.setOnClickListener(this);
        mEtTeleponeNumber.setOnClickListener(this);
        mBtNext.setOnClickListener(this);
        mTvServerAgreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_layout:
                finish();
                break;
            case R.id.ll_card_information_name:

                Toast.makeText(this,"选择银行卡类型",Toast.LENGTH_SHORT).show();
//                jump();
                break;
            case R.id.bt_bankcard_information_next:
                Toast.makeText(this,"下个页面",Toast.LENGTH_SHORT).show();
//                jump();

                break;
            case R.id.tv_bank_information_service_agreement:
                Toast.makeText(this,"服务协议",Toast.LENGTH_SHORT).show();
//                jump();
                break;
        }
    }
}
