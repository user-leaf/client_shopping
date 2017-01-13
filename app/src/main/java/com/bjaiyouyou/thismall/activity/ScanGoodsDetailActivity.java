package com.bjaiyouyou.thismall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bjaiyouyou.thismall.MainApplication;
import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.client.ClientAPI;
import com.bjaiyouyou.thismall.model.CartItem2;
import com.bjaiyouyou.thismall.model.CartModel;
import com.bjaiyouyou.thismall.model.HomeAdModel;
import com.bjaiyouyou.thismall.model.ProductDetail;
import com.bjaiyouyou.thismall.model.ProductImageModel;
import com.bjaiyouyou.thismall.model.ProductModel;
import com.bjaiyouyou.thismall.model.ProductSizeModel;
import com.bjaiyouyou.thismall.user.CurrentUserManager;
import com.bjaiyouyou.thismall.utils.ImageUtils;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.bjaiyouyou.thismall.utils.NetStateUtils;
import com.bjaiyouyou.thismall.utils.ScreenUtils;
import com.bjaiyouyou.thismall.utils.UNNetWorkUtils;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Call;


/**
 *
 * @author QuXinhang
 *Creare 2016/6/4 11:15
 * 扫码结果页面
 *
 */
public class ScanGoodsDetailActivity extends BaseActivity implements View.OnClickListener,OnItemClickListener {
    private IUUTitleBar mTitleBar;

    //图片轮播容器
    private RelativeLayout mRLImageViews;
    //商品名称
    private TextView mTVName;
    //价格
    private TextView mTVMoney;
    //积分
    private TextView mTVIntegral;
    //卖出去的数量
    private TextView mTVSoldNum;
    //原价
    private TextView mTVOldPrice;
    //规格一
    private TagFlowLayout mFLModelOne;
    //规格二
    private TagFlowLayout mFlModelTwo;
    //评分
    private TextView mTVScore;
    //可获得UU
    private TextView mTVGetIntegral;
    //减少
    private ImageView mIVReduce;
    //增加
    private ImageView mIVAdd;
    //选择数量
    private TextView mTVChooseNum;
    //添加到
    private TextView mTVAddToCar;
    //总钱数
    private TextView mTVMoneyAll;
    //付款按钮
    private LinearLayout mLLToPay;

    //上一页传过来的的产品ID
    private long mProductID;
    //规格ID，在规格选择监听的时候改变
    private int mSizeID;
    //商品数量
    private int mProductNum=1;

    //规格角标，在规格选择监听的时候改变
    private int mMOdleIndex=0;
    //商品单价
    private double mMoney;
    //商品单积分
    private int mIntegral;
    //商品单价
    private double mMoneyAll;
    //商品单UU
    private int mIntegralAll;
    //单个商品获得商品UU
    private int mGetIntegral;
    //商品获得总UU
    private int mGetIntegralAll;
    //图片无限；
    private ConvenientBanner mConvenientBanner;
    //图片数组
    private List<HomeAdModel> mNetImages;
    //网络加载的商品
    private ProductDetail.ProductBean mProduct;

    List<ProductDetail.ProductBean.SizesBean> mSizeBeans;
    //规格数据集合
    private ArrayList<String> sizeList;
    //规格数据适配器
    private TagAdapter<String> sizeModelAdapterOne;
    //获得布局填充器
    private  LayoutInflater mInflater;
    private View mLLOnNet;
    private LinearLayout mLLUnNetWork;
    private TextView mTVLoading;
    private TextView mTvGetDataAgain;
    private LinearLayout mLLScanHaven;
    //规格角标
    private int mSizePosition;
    private LinearLayout mHaveDone;
    private boolean isRush=false;
    private LinearLayout mLLISRushState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_goods_detail);
        mInflater = LayoutInflater.from(this);
        initData();
        initView();
        setUpView();
    }


    /**
     * 获得控件
     */

    private void initView() {
        //抢票状态
        mLLISRushState = ((LinearLayout) findViewById(R.id.ll_is_rush_scan_good_details));
        //操作控价所在布局
        mHaveDone = ((LinearLayout) findViewById(R.id.ll_scan_goods_have_done));
        //扫描商品不存在
        mLLScanHaven = ((LinearLayout) findViewById(R.id.ll_scan_goods_haven));
        //网络判断相关
        mLLOnNet = findViewById(R.id.ll_scan_goods_detail_on_net);
        mLLUnNetWork = ((LinearLayout) findViewById(R.id.ll_unnetwork));
        mTVLoading = ((TextView) findViewById(R.id.tv_data_loading));
        mTVLoading.setVisibility(View.VISIBLE);
        mTvGetDataAgain = ((TextView)findViewById(R.id.tv_get_data_again));

        //图片轮播控制器
        mRLImageViews = ((RelativeLayout) findViewById(R.id.rl_scan_goods_details_imgs));
        mRLImageViews.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight(this) / 2));

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this) / 2);
        mConvenientBanner = new ConvenientBanner(this);
        mConvenientBanner.setLayoutParams(params);
        //标题
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_scan_goods_details));
        mTVName = ((TextView) findViewById(R.id.tv_scan_goods_details_name));
        mTVMoney = ((TextView) findViewById(R.id.tv_scan_goods_details_money));
        mTVIntegral = (TextView) findViewById(R.id.tv_scan_goods_details_integral);
        mTVSoldNum = ((TextView) findViewById(R.id.tv_scan_goods_details_sold_num));
        mTVOldPrice = ((TextView) findViewById(R.id.tv_scan_goods_details_oldprice));
        //为原价添加中线
        mTVOldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线

        mFLModelOne = ((TagFlowLayout) findViewById(R.id.flowLayout_scan_goods_detail_model_one));
        mFlModelTwo = ((TagFlowLayout) findViewById(R.id.flowLayout_scan_goods_detail_model_two));

        mTVScore = ((TextView) findViewById(R.id.tv_scan_goods_details_score));
        mTVGetIntegral = ((TextView) findViewById(R.id.tv_scan_goods_details_getintegral));

        mIVReduce = ((ImageView) findViewById(R.id.iv_scan_goods_detail_reduce));
        mIVAdd = ((ImageView) findViewById(R.id.iv_scan_goods_detail_add));
        mTVChooseNum = ((TextView) findViewById(R.id.tv_scan_goods_details_choose_num));

        mTVAddToCar = ((TextView) findViewById(R.id.tv_scan_goods_details_addtocar));
        mTVMoneyAll = ((TextView) findViewById(R.id.tv_scan_goods_details_money_all));
        mLLToPay = ((LinearLayout) findViewById(R.id.ll_scan_goods_detail_pay));
    }

    /**
     * 设置监听
     */
    private void setUpView() {
        mTvGetDataAgain.setOnClickListener(this);
        mTitleBar.setLeftLayoutClickListener(this);
        mTitleBar.setRightLayoutClickListener(this);

        mIVReduce.setOnClickListener(this);
        mIVAdd.setOnClickListener(this);
        mTVAddToCar.setOnClickListener(this);
        mLLToPay.setOnClickListener(this);

        mFLModelOne.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                setSize(position);
                //获得规格角标
                mSizePosition=position;
                return true;
            }
        });
        mFLModelOne.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {

            }
        });

        mFlModelTwo.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                return false;
            }
        });
        mFlModelTwo.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
            }
        });
//
    }
    private void initData() {
        String productScanID=getIntent().getStringExtra("productScanID");
        ClientAPI.getScanProductDetailsData(productScanID, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
                LogUtils.e("ScanGood",e.getMessage()+"");
                if (NetStateUtils.isNetworkAvailable(getApplicationContext())){
                    mLLScanHaven.setVisibility(View.VISIBLE);
                    mLLOnNet.setVisibility(View.GONE);
                    mLLUnNetWork.setVisibility(View.GONE);
                    mTVLoading.setVisibility(View.GONE);
                    mHaveDone.setVisibility(View.GONE);
                }else {
                    mLLScanHaven.setVisibility(View.GONE);
                    mLLOnNet.setVisibility(View.GONE);
                    mLLUnNetWork.setVisibility(View.VISIBLE);
                    mTVLoading.setVisibility(View.GONE);
                    mHaveDone.setVisibility(View.GONE);
                }
            }
            @Override
            public void onResponse(String response, int id) {
                mTVLoading.setVisibility(View.VISIBLE);
                mLLOnNet.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(response.trim())){
                    UNNetWorkUtils.lvShow(mTVLoading,mLLOnNet,mLLUnNetWork);
                    mLLScanHaven.setVisibility(View.GONE);
                    mHaveDone.setVisibility(View.VISIBLE);
                    mProduct=new Gson().fromJson(response,ProductDetail.class).getProduct();
                    setData();
                }
                //数据为空
                else {
                    mLLScanHaven.setVisibility(View.VISIBLE);
                    mHaveDone.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setData() {
        //获取轮播图片地址集合
        mNetImages = new ArrayList<HomeAdModel>();
        List<ProductDetail.ProductBean.ImagesBean> imgList= mProduct.getImages();
        int imgSize=imgList.size();
        if (imgSize==0||imgList==null){
            mTVLoading.setText("暂无图片介绍");
            mTVLoading.setVisibility(View.VISIBLE);
        }else {
            mTVLoading.setVisibility(View.GONE);
            mTVLoading.setText("数据加载中。。。");
        }
        ProductDetail.ProductBean.ImagesBean imagesBean;
        HomeAdModel homeAdModel;
        for (int i=0;i<imgSize;i++){
            imagesBean=imgList.get(i);
            homeAdModel=new HomeAdModel(null,imagesBean.getImage_path(),imagesBean.getImage_base_name());
            mNetImages.add(homeAdModel);
        }
        //实现图片轮播
        initImgs(mNetImages);
        //填充数据
        mTitleBar.setTitle(mProduct.getName());
        mTVName.setText(mProduct.getName());
        mTVScore.setText(mProduct.getScore()+"分");
        //随着规格改变的，获得规格集合
        mSizeBeans=mProduct.getSizes();
        //初始与规格相关的页面
        setSize(0);
        //处理规格显示页面
        initSize();
        mTVChooseNum.setText(""+mProductNum);
    }

    /**
     * 规格布局
     */
    private void initSize() {
        //属性处理/////////////////////////////////////////////////////////////////////////
        sizeList = new ArrayList<String>();
        initSizeData(sizeList);
        //创建适配器
        sizeModelAdapterOne = new TagAdapter<String>(sizeList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_goods_detail_size_tv, mFLModelOne, false);
                tv.setText(s);
                return tv;
            }

            //某个字符串为初始化选中状态
            @Override
            public boolean setSelected(int position, String s) {
                //设置第一个为默认选中状态
                return s.equals(sizeList.get(0));
            }
        };
        //填充适配器
        mFLModelOne.setAdapter(sizeModelAdapterOne);

    }
    /**
     * 规格数据集合
     */
    private void initSizeData(List<String> data) {
        for (int i = 0; i < mSizeBeans.size(); i++) {
            data.add(mSizeBeans.get(i).getName());
        }
    }

    /**
     * 图片无限轮播
     * @param mNetImages
     */
    private void initImgs(List<HomeAdModel> mNetImages ) {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        mConvenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, mNetImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.list_indicate_nor, R.mipmap.list_indicate_sel})
                //设置指示器的位置
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

        // 为轮播设置条目点击监听
        mConvenientBanner.setOnItemClickListener(this);

        // 把控件加入到栏容器中
        mRLImageViews.addView(mConvenientBanner);
    }

    /**
     * 填充和获得size相关的数据
     * @param i
     */
    public void setSize(int i) {
        if (mSizeBeans.size()!=0){
            isRush=mSizeBeans.get(i).isIf_rush_to_purchasing();
            //控制抢购状态的显示和隐藏
            if (isRush){
                mLLISRushState.setVisibility(View.VISIBLE);
            }else {
                mLLISRushState.setVisibility(View.GONE);
            }
            mMoney=Double.valueOf(mSizeBeans.get(i).getPrice());
            mTVMoney.setText(mMoney+"");

            mIntegral=mSizeBeans.get(i).getIntegration_price();
            mTVIntegral.setText(mIntegral+"");

            mTVSoldNum.setText("已经出售"+mSizeBeans.get(i).getSales_volume()+"件");

            mGetIntegral=mSizeBeans.get(i).getPresented_gold();
            mTVGetIntegral.setText(mGetIntegral+"");

            mSizeID=mSizeBeans.get(i).getId();
            countMoney();
        }
    }

    /**
     * 栏控件 ConvenientBanner 所需
     */
    public class LocalImageHolderView implements Holder<HomeAdModel> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, HomeAdModel homeAdModel) {

            StringBuilder sb = new StringBuilder();
            sb.append(homeAdModel.getImage_path())
                    .append(File.separator)
                    .append(homeAdModel.getImage_base_name());
            String imagePath = sb.toString();

            Glide.with(getApplicationContext())
                    .load(ImageUtils.getThumb(imagePath, ScreenUtils.getScreenWidth(getApplicationContext())/2,0))
                    .error(R.mipmap.list_image_loading)
                    .placeholder(R.mipmap.list_image_loading)
                    .into(imageView);

        }
    }

    /**
     * 轮播条目点击监听
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
//        Toast.makeText(this,""+position,Toast.LENGTH_SHORT).show();
    }


    /**
     * 处理点击事件
     * @param v
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.left_layout:
                finish();
                break;
            //调用分享的方法进行分享
            case R.id.right_layout:
                showShare();
                break;

            //商品减少
            case R.id.iv_scan_goods_detail_reduce:
                reduceProduct();
                break;
            //商品增加
            case R.id.iv_scan_goods_detail_add:
                addProduct();
                break;
            //添加到购物车
            case R.id.tv_scan_goods_details_addtocar:
                addToCar();
                break;
            //去付款
            case R.id.ll_scan_goods_detail_pay:
                goTOPay();
                break;
            //再次获取数据
            case R.id.tv_get_data_again:
                initData();
                break;
        }
    }

    /**
     * 去支付
     */
    private void goTOPay() {
        mProductID=mProduct.getId();
        mSizeID=mProduct.getSizes().get(mSizePosition).getId();
//    Toast.makeText(this,"去付款",Toast.LENGTH_SHORT).show();
        StringBuffer sb=new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/shoppingCart/add");
        OkHttpUtils
                .get()
                .url(sb.toString().trim())
                .addParams("number",mProductNum+"")
                .addParams("product_id",mProductID+"")
                .addParams("product_size_id",mSizeID+"")
                .addParams("token", CurrentUserManager.getUserToken())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
//                Toast.makeText(getApplicationContext(),mProductNum+"请先登录再付款",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response, int id) {
                //提交订单
                makeOrder();
            }
        });

    }

    /**
     * 结账，下订单
     */

    private void makeOrder() {
        List<CartItem2> goodList = new ArrayList<>();
//        private long product_id; //,
//        private long product_size_id; //,
//        private int number; //,
//        private String updated_at; //2016-07-09 12:21:14",
//        private ProductModel product; //bject{...},
//        private ProductSizeModel product_size; //bject{...},
//        private ProductImageModel product_images; //bject{...}
        long product_id=mProduct.getId();
        long product_size_id=mProduct.getSizes().get(mSizePosition).getId();
        int number=mProductNum;
        String updated_at="";

        ProductModel product=new ProductModel();
        product.setName(mProduct.getName());

        ProductSizeModel product_size=new ProductSizeModel();
        product_size.setName(mProduct.getSizes().get(mSizePosition).getName());
        product_size.setPrice(mProduct.getSizes().get(mSizePosition).getPrice());
        product_size.setIntegration_price(mProduct.getSizes().get(mSizePosition).getIntegration_price());
        product_size.setPresented_gold(mProduct.getSizes().get(mSizePosition).getPresented_gold());

        ProductImageModel product_images=new ProductImageModel();
        if (mProduct.getImages().size()>0){
            product_images.setImage_base_name(mProduct.getImages().get(0).getImage_base_name());
            product_images.setImage_path(mProduct.getImages().get(0).getImage_path());
        }
        CartModel model=new CartModel();
        model.setNumber(number);
        model.setProduct(product);
        model.setProduct_id(product_id);
        model.setProduct_images(product_images);
        model.setProduct_size(product_size);
        model.setProduct_size_id(product_size_id);
//        model.setUpdated_at("");
        CartItem2 item=new CartItem2();
        item.setCartModel(model);
        item.setChecked(true);
        goodList.add(item);

        Intent intent = new Intent(this, OrderMakeActivity.class);
        // 通过MainApplication传值
        MainApplication ma = MainApplication.getInstance();
        ma.setData(goodList);
        startActivity(intent);
    }


    /**
     * 添加到购物车
     */

    /**
     * 添加到购物车
     */
    private void addToCar() {
        mProductID=mProduct.getId();
        mSizeID=mProduct.getSizes().get(mSizePosition).getId();
        StringBuffer sb=new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/shoppingCart/add");
        OkHttpUtils
                .get()
                .url(sb.toString().trim())
                .addParams("number",mProductNum+"")
                .addParams("product_id",mProductID+"")
                .addParams("product_size_id",mSizeID+"")
                .addParams("token", CurrentUserManager.getUserToken())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
               UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(),e);
            }
            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(getApplicationContext(),mProductNum+"件，您的宝贝已经加入购物车了哦",Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 添加商品
     */
    private void addProduct() {
        ++mProductNum;
        countMoney();
    }

    /**
     * 减少商品
     */
    private void reduceProduct() {
        if (mProductNum<=1){
            Toast.makeText(this,"亲不能再减少了哦",Toast.LENGTH_SHORT).show();
            return;
        }else {
            --mProductNum;
            countMoney();
        }

    }

    /**
     * 计算钱数
     */
    private void countMoney() {
        mGetIntegralAll=mGetIntegral*mProductNum;
        mTVGetIntegral.setText(mGetIntegralAll+"UU");
        mMoneyAll=mMoney*mProductNum;
        mIntegralAll=mIntegral*mProductNum;
        mTVMoneyAll.setText("总金额：￥"+mMoneyAll+"+"+mIntegralAll+"积分");
        mTVChooseNum.setText(""+mProductNum);
    }

    /**
     * 分享
     */
    private void showShare() {
//        ShareSDK.initSDK(this); // 添加到了 onCreate()
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("我是分享小达人");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 图片开始轮播
     */
    @Override
    public void onResume() {
        super.onResume();
        //广告开始自动翻页
        mConvenientBanner.startTurning(2000);
    }

    /**
     * 轮播停止
     */
    @Override
    public void onPause() {
        super.onPause();
        //广告停止翻页
        mConvenientBanner.stopTurning();
    }
}
