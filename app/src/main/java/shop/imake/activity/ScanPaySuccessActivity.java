package shop.imake.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import shop.imake.R;
import shop.imake.utils.ImageUtils;
import shop.imake.utils.ScreenUtils;
import com.bumptech.glide.Glide;

public class ScanPaySuccessActivity extends Activity implements View.OnClickListener {

    public static final String PARAM_MONEY = "money";
    public static final String PARAM_HEAD_IMG_PATH = "headImagePath";
    public static final String PARAM_SHOP_NAME = "shopName";

    /**
     * 启动本页面
     *
     * @param context
     * @param money
     * @param headImagePath
     * @param shopName
     */
    public static void actionStart(Context context, double money, String headImagePath, String shopName) {
        Intent intent = new Intent(context, ScanPaySuccessActivity.class);
        intent.putExtra(PARAM_MONEY, money);
        intent.putExtra(PARAM_HEAD_IMG_PATH, headImagePath);
        intent.putExtra(PARAM_SHOP_NAME, shopName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_pay_success);

        TextView tvMoney = (TextView) findViewById(R.id.scan_pay_success_tv_money);
        ImageView ivHead = (ImageView) findViewById(R.id.scan_pay_success_iv_head);
        TextView tvName = (TextView) findViewById(R.id.scan_pay_success_tv_name);
        Button btnComplete = (Button) findViewById(R.id.scan_pay_success_btn_complete);

        btnComplete.setOnClickListener(this);

        Intent intent = getIntent();
        double money = intent.getDoubleExtra(PARAM_MONEY, -1);
        String strHeadImagePath = intent.getStringExtra(PARAM_HEAD_IMG_PATH);
        String strShopName = intent.getStringExtra(PARAM_SHOP_NAME);

        tvMoney.setText(String.valueOf(money));
        Glide.with(this)
                .load(ImageUtils.getThumb(strHeadImagePath, ScreenUtils.getScreenWidth(this) / 4, 0))
                .into(ivHead);
        tvName.setText(strShopName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_pay_success_btn_complete:
                finish();
                break;
        }
    }
}
