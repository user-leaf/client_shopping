package shop.imake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.utils.DoubleTextUtils;

/**
 * 话费充值成功页面
 */
public class TelPaySuccessActivity extends BaseActivity {

    private TextView mTvMoney;
    private Button mBtFinsh;
    public static String TEL_PAY_MONEY="telpaymoney";

    public static void startAction(Context context,String money) {
        Intent intent = new Intent(context, TelPaySuccessActivity.class);
        intent.putExtra(TEL_PAY_MONEY, money);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_pay_success);
        initView();
        setUpView();
    }

    private void initView() {
        Intent intent=getIntent();
        String money=intent.getStringExtra(TEL_PAY_MONEY);
        mTvMoney = ((TextView) findViewById(R.id.tel_pay_success_tv_money));
        if (!TextUtils.isEmpty(money)){
            mTvMoney.setText(""+ DoubleTextUtils.setDoubleUtils(Double.valueOf(money)));
        }
        mBtFinsh = ((Button) findViewById(R.id.tel_pay_success_btn_complete));
    }

    private void setUpView() {
        mBtFinsh.setOnClickListener(this);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            case R.id.tel_pay_success_btn_complete:
                finish();
                break;
        }
    }
}
