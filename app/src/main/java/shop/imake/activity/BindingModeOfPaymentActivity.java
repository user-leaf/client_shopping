package shop.imake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import shop.imake.R;
import shop.imake.adapter.BankCardAdapter;
import shop.imake.model.BankCard;
import shop.imake.widget.IUUTitleBar;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

/**
   *
   * @author Alice
   *Creare 2016/6/21 10:24
   *支付方式选择页面
   *
   *
   */
    public class BindingModeOfPaymentActivity extends BaseActivity implements AdapterView.OnItemClickListener ,View.OnClickListener{
        //银行卡列表
        private ListView lv;
        //银行卡数据集合
        private List<BankCard> data;
        //银行卡数据适配器
        private  BankCardAdapter adapter;
        //标题栏
        private IUUTitleBar tilteBar;
//      微信侧滑
      private SwipeLayout samplelWechat;
      private LinearLayout llWechat;
//      微信默认支付
      private TextView mTvWeChatSeting;
//      微信删除
      private TextView mTvWechatDelete;
//      支付宝侧滑
      private LinearLayout llZhiFuBao;
      private SwipeLayout samplelZhiFuBao;
//      支付宝默认支付
      private TextView mTvZhiFuBaoSeting;
//      支付宝删除
      private TextView mTvZhiFuBaoDelete;
      private LinearLayout llYinHangKa;

      private Intent mIntent;
      private boolean isLogin=false;
      private RelativeLayout mLLNotLogin;
      private TextView mTvGoToLogin;
      private TextView mTVNotLoginTitle;

      @Override
        protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_bank_card);
          mIntent=getIntent();
          isLogin=mIntent.getBooleanExtra("isLogin",false);
          initView();
          setUpView();
          initData();
          initCtrl();
        }

      private void initCtrl() {
          adapter=new BankCardAdapter(this,data,llYinHangKa);
          lv.setAdapter(adapter);
      }

      private void setUpView() {
          lv.setOnItemClickListener(this);
          mTvGoToLogin.setOnClickListener(this);
      }

      private void initData() {
            data=new ArrayList<>();
            for (int i=0;i<9;i++){
                BankCard card;
                if (i<3){
                    card = new BankCard("中国银行", "储蓄卡",""+i+i+i+i+i+i+i+i+i+i+i+i+i+i+i,true);
                }else {

                    card=new  BankCard("建设银行", "储蓄卡",""+i+i+i+i+i+i+i+i+i+i+i+i+i+i+i,false);
                }
                data.add(card);
            }
        }


        private void initView() {
            lv = ((ListView) findViewById(R.id.lv_band_card));
            tilteBar = ((IUUTitleBar) findViewById(R.id.title_bank_card));
            tilteBar.setLeftLayoutClickListener(this);
//            尾部添加银行卡
            initFood();
//            添加头部
            initHead();
//            条目监听
            mLLNotLogin = ((RelativeLayout) findViewById(R.id.ll_not_login));
            mTVNotLoginTitle = ((TextView) findViewById(R.id.tv_not_login_title));
            mTVNotLoginTitle.setText("无法获得支付信息");
            mTvGoToLogin = ((TextView) findViewById(R.id.tv_goto_login));
            if (isLogin){
                mLLNotLogin.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
            }else {
                mLLNotLogin.setVisibility(View.VISIBLE);
                lv.setVisibility(View.GONE);
            }
        }

      private void initHead() {
          View headView=LayoutInflater.from(this).inflate(R.layout.head_bangding_modeofpayment,null);
          lv.addHeaderView(headView);
//          微信
          llWechat = ((LinearLayout) headView.findViewById(R.id.ll_bangding_modeofpayment_wechat));
          llWechat.setOnClickListener(this);
//          微信侧滑
          samplelWechat = ((SwipeLayout) headView.findViewById(R.id.sample1_wechat));
          samplelWechat.setShowMode(SwipeLayout.ShowMode.PullOut);
          samplelWechat.addDrag(SwipeLayout.DragEdge.Right, samplelWechat.findViewById(R.id.bottom_wrapper_wechat));
          mTvWeChatSeting = ((TextView) headView.findViewById(R.id.tv_bangdingPayment_wechat_setingdefault));
          mTvWechatDelete = ((TextView) headView.findViewById(R.id.tv_bangdingPayment_wechat_delete));
          mTvWeChatSeting.setOnClickListener(this);
          mTvWechatDelete.setOnClickListener(this);
//          支付宝
          llZhiFuBao = ((LinearLayout) headView.findViewById(R.id.ll_bangding_modeofpayment_zhifubao));
          llZhiFuBao.setOnClickListener(this);
//          支付宝侧滑
          samplelZhiFuBao = ((SwipeLayout) headView.findViewById(R.id.sample1_zhifubao));
          samplelZhiFuBao.setShowMode(SwipeLayout.ShowMode.PullOut);
          samplelZhiFuBao.addDrag(SwipeLayout.DragEdge.Right, samplelZhiFuBao.findViewById(R.id.bottom_wrapper_zhifubao));
          mTvZhiFuBaoSeting = ((TextView) headView.findViewById(R.id.tv_bangdingPayment_zhifubao_setingdefault));
          mTvZhiFuBaoDelete = ((TextView) headView.findViewById(R.id.tv_bangdingPayment_zhifubao_delete));
          mTvZhiFuBaoSeting.setOnClickListener(this);
          mTvZhiFuBaoDelete.setOnClickListener(this);
//
          llYinHangKa = ((LinearLayout) headView.findViewById(R.id.ll_bangding_modeofpayment_yinhangka));


      }

      private void initFood() {
          View  foodView=LayoutInflater.from(this).inflate(R.layout.food_bank_card,null);
          foodView.setOnClickListener(this);
          foodView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //跳转到绑定类型选择
                  jump(ChooseTheMethodOfPaymentActivity.class,false);
              }
          });
          lv.addFooterView(foodView);
      }

      @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(this,position+"",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.left_layout:
                    finish();
                    break;
//                微信
                case R.id.tv_bangdingPayment_wechat_setingdefault:
                    Toast.makeText(this,"微信设置为默认支付",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_bangdingPayment_wechat_delete:
                    llWechat.setVisibility(View.GONE);
                    break;
//                支付宝
                case R.id.tv_bangdingPayment_zhifubao_setingdefault:
                    Toast.makeText(this,"支付宝设置为默认支付",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_bangdingPayment_zhifubao_delete:
                    llZhiFuBao.setVisibility(View.GONE);
                    break;
                case R.id.tv_goto_login:
                    jump(new Intent(getApplicationContext(),LoginActivity.class),false);
                    finish();
                    break;
            }
        }
    }
