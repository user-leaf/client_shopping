package shop.imake.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import shop.imake.R;
/**
 * 激活兑换券类
 *author Alice
 *created at 2017/4/21 14:54
 */
public class ActivateExchangeCertificateActivity extends BaseActivity {

    private TextView mTvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_exchange_certificate);
        initView();
        setupView();
    }

    private void initView() {
        mTvBack = ((TextView) findViewById(R.id.tv_activate_exchange_certificate_back));

    }

    private void setupView() {
        mTvBack.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0,R.anim.anim_activate_exchange_certificate_close);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            //跳转兑换券激活页面
            case R.id.tv_activate_exchange_certificate_back :
                finish();
                break;

            default:
                return;
        }
    }
}
