package shop.imake.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import shop.imake.MainActivity;
import shop.imake.R;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4ClientOther;
import shop.imake.client.ClientAPI;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.CartItem2;
import shop.imake.model.CartModel;
import shop.imake.model.HomeAdModel;
import shop.imake.model.ProductDetail;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DoubleTextUtils;
import shop.imake.utils.ImageUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.ScreenUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.widget.IUUTitleBar;


/**
 * @author Alice
 *         Creare 2016/6/4 11:15
 *         商品详情页面
 */
public class GoodsDetailsActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {
    public static final String PARAM_PRODUCT_ID = "productID";

    public static final String TAG = GoodsDetailsActivity.class.getSimpleName();
    private Api4ClientOther mClient;

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
    //可获得积分
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
    private int mProductNum = 1;

    //规格角标，在规格选择监听的时候改变
    private int mMOdleIndex = 0;
    //商品单价
    private double mMoney;
    //商品单积分
    private int mIntegral;
    //商品单价
    //商品单积分
    private int mIntegralAll;
    //单个商品获得商品积分
    private int mGetIntegral;
    //商品获得总UU-UU
    private int mGetIntegralAll;
    //图片无限；
    private ConvenientBanner mConvenientBanner;
    //图片数组
    private List<HomeAdModel> mNetImages;
    //网络加载的商品
    private ProductDetail.ProductBean mProduct;
    //数据集合
    List<ProductDetail.ProductBean.SizesBean> mSizeBeans;
    //规格数据集合
    private ArrayList<String> sizeList;
    //规格数据适配器
    private TagAdapter<String> sizeModelAdapterOne;
    //获得布局填充器
    private LayoutInflater mInflater;
    //网络正常布局
    private View mLLOnNet;
    //断网布局
    private LinearLayout mLLUnNetWork;
    //数据加载提示
    private TextView mTVLoading;
    //再次加载点击按钮
    private TextView mTvGetDataAgain;
    //选中的规格编号
    private int mSizePosition;
    //最下方操作按钮布局
    private LinearLayout mLLDone;

    //商品数据增加上限
    private int mNumLimit = 99;
    //单次购买数量限制哦
    private int mBuyLimit = 10;


    //是否在抢购中
    private LinearLayout mLLISRushState;
    //是否为抢购商品提示
    private TextView mTVIsRushState;
    //规格
    private LinearLayout mLLSize;
    //判断规格是否存在
    private boolean isSizeHave = false;
    //标记是否是正在抢购商品
    private boolean isRush = false;

    //标记是否是正在抢购商品
    private boolean isRushing = false;
    //商品的重量
    private TextView mTvWeight;
    //添加到购物车按钮
    private ImageView mIvAddToCar;
    //商品数量过多
    private LinearLayout mLLOverNum;
    //通知提示框
    private TextView mTvNotify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        //获得商品ID
        mProductID = getIntent().getLongExtra(PARAM_PRODUCT_ID, -1);
        mInflater = LayoutInflater.from(this);
        initData();
        initView();
        setUpView();
    }

    public static void actionStart(Context context, long productID) {
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra(GoodsDetailsActivity.PARAM_PRODUCT_ID, productID);
        context.startActivity(intent);
    }

    private void initView() {
        mIvAddToCar = ((ImageView) findViewById(R.id.iv_goods_details_addtocar));
        //规格
        mLLSize = ((LinearLayout) findViewById(R.id.ll_goods_detail_size));
        //抢票状态
        mLLISRushState = ((LinearLayout) findViewById(R.id.ll_is_rush_good_details));
        mTVIsRushState = ((TextView) findViewById(R.id.tv_is_rush_good_details));
        //最下方操作按钮布局
        mLLDone = ((LinearLayout) findViewById(R.id.ll_goods_detail_done));
        //网络判断相关
        mLLOnNet = findViewById(R.id.ll_goods_detail_on_net);
        mLLUnNetWork = ((LinearLayout) findViewById(R.id.ll_unnetwork));
        mTVLoading = ((TextView) findViewById(R.id.tv_data_loading));
        mTvGetDataAgain = ((TextView) findViewById(R.id.tv_get_data_again));

        //图片轮播控制器
        mRLImageViews = ((RelativeLayout) findViewById(R.id.rl_goods_details_imgs));
        mRLImageViews.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight(this) / 2));

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this) / 2);
        mConvenientBanner = new ConvenientBanner(this);
        mConvenientBanner.setLayoutParams(params);
        //标题
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_goods_details));
        mTVName = ((TextView) findViewById(R.id.tv_goods_details_name));
        mTVMoney = ((TextView) findViewById(R.id.tv_goods_details_money));
        mTVIntegral = (TextView) findViewById(R.id.tv_goods_details_integral);
        mTVSoldNum = ((TextView) findViewById(R.id.tv_goods_details_sold_num));
        mTVOldPrice = ((TextView) findViewById(R.id.tv_goods_details_oldprice));
        //为原价添加中线
        mTVOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

        mFLModelOne = ((TagFlowLayout) findViewById(R.id.flowLayout_goods_detail_model_one));
        mFlModelTwo = ((TagFlowLayout) findViewById(R.id.flowLayout_goods_detail_model_two));

        mTVScore = ((TextView) findViewById(R.id.tv_goods_details_score));
        mTVGetIntegral = ((TextView) findViewById(R.id.tv_goods_details_getintegral));

        mIVReduce = ((ImageView) findViewById(R.id.iv_goods_detail_reduce));
        mIVAdd = ((ImageView) findViewById(R.id.iv_goods_detail_add));
        mTVChooseNum = ((TextView) findViewById(R.id.tv_goods_details_choose_num));

        mTVAddToCar = ((TextView) findViewById(R.id.tv_goods_details_addtocar));
        mLLToPay = ((LinearLayout) findViewById(R.id.ll_goods_detail_pay));

        mTvWeight = ((TextView) findViewById(R.id.tv_goods_details_weight));
        mLLOverNum = ((LinearLayout) findViewById(R.id.ll_goods_details_over_num));
        mTvNotify = ((TextView) findViewById(R.id.tv_goods_details_over_num));
    }

    /**
     * 设置监听
     */
    private void setUpView() {
        mTvGetDataAgain.setOnClickListener(this);
        mTitleBar.setLeftLayoutClickListener(this);
//        mTitleBar.setRightLayoutClickListener(this);

        mIVReduce.setOnClickListener(this);
        mIVAdd.setOnClickListener(this);
        mTVAddToCar.setOnClickListener(this);
        mLLToPay.setOnClickListener(this);
        mIvAddToCar.setOnClickListener(this);

        mFLModelOne.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                setSize(position);
                mSizePosition = position;

                //添加条件保证必须有一个条目被选
                int count = parent.getChildCount();
                LogUtils.e("count", "" + count);
                for (int i = 0; i < count; i++) {
                    if (i != position) {
                        parent.getChildAt(i).setClickable(false);
                    } else {
                        parent.getChildAt(i).setClickable(true);
                    }
                }
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
        long productID = getIntent().getLongExtra(PARAM_PRODUCT_ID, -1);

        mClient = (Api4ClientOther) ClientApiHelper.getInstance().getClientApi(Api4ClientOther.class);

        mClient.getGoodDetailData(TAG, productID, new DataCallback<ProductDetail>(getApplicationContext()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                LogUtils.e("goodsDetails--e", e.toString());
                String eString = e.toString();
                //网络不存在
                if (!NetStateUtils.isNetworkAvailable(getApplicationContext())) {
                    UNNetWorkUtils.isNetHaveConnect(getApplicationContext(), mTVLoading, mLLOnNet, mLLUnNetWork);
                } else {
                    ToastUtils.showException(e, getApplicationContext());

                    //商品不存在
                    // 判断"商品不存在"在报错中是否存在，当为-1的时候报错中不存在，说明不是商品不存在报错
                    if ("商品不存在".indexOf(eString) != -1)
                        Toast.makeText(getApplicationContext(), "商品详情信息暂时不存在", Toast.LENGTH_SHORT).show();
                    mTVLoading.setVisibility(View.VISIBLE);
                    mLLOnNet.setVisibility(View.GONE);
                    mLLDone.setVisibility(View.GONE);
                    mTVLoading.setText("商品详情信息暂时不存在");
                }

            }

            @Override
            public void onSuccess(Object response, int id) {
                mTVLoading.setVisibility(View.VISIBLE);
                mLLOnNet.setVisibility(View.GONE);
                if (response != null) {
                    mTVLoading.setVisibility(View.GONE);
                    UNNetWorkUtils.lvShow(mTVLoading, mLLOnNet, mLLUnNetWork);
                    ProductDetail productDetail = (ProductDetail) response;
                    mProduct = productDetail.getProduct();
                    mLLDone.setVisibility(View.VISIBLE);
                    setData();
                }
                //数据为空
                else {
                    Toast.makeText(getApplicationContext(), "商品详情信息暂时不存在", Toast.LENGTH_SHORT).show();
                    mLLDone.setVisibility(View.GONE);
                    mTVLoading.setText("商品详情信息暂时不存在");
                }

            }
        });

    }

    private void setData() {
        //商品存在
        if (mProduct != null) {
            mNetImages = new ArrayList<HomeAdModel>();
            //获取轮播图片地址集合
            List<ProductDetail.ProductBean.ImagesBean> imgList = mProduct.getImages();
            //轮播图片不为null
            if (imgList != null) {
                int imgSize = imgList.size();
                //轮播图片不存在
                if (imgSize == 0 || imgList == null) {
                    mTVLoading.setText("暂无图片介绍");
                } else {
                    //轮播图片存在
                    mTVLoading.setVisibility(View.GONE);
                    ProductDetail.ProductBean.ImagesBean imagesBean;
                    HomeAdModel homeAdModel;
                    for (int i = 0; i < imgSize; i++) {
                        imagesBean = imgList.get(i);
                        homeAdModel = new HomeAdModel(null, imagesBean.getImage_path(), imagesBean.getImage_base_name());
                        mNetImages.add(homeAdModel);
                    }
                    //实现图片轮播
                    initImgs(mNetImages);
                }
            } else {
                mTVLoading.setText("暂无图片介绍");
            }
            //填充数据
            mTitleBar.setTitle(mProduct.getName());
            mTVName.setText(mProduct.getName());
            mTVScore.setText(mProduct.getScore() + "分");
            //随着规格改变的，获得规格集合
            mSizeBeans = mProduct.getSizes();
            if (mSizeBeans != null && mSizeBeans.size() != 0) {
                isSizeHave = true;
                //规格部分显示
                mLLSize.setVisibility(View.VISIBLE);
                //初始与规格相关的页面
                setSize(0);
                //处理规格显示页面
                initSize();
            } else {
                isSizeHave = false;
            }
            mTVChooseNum.setText("" + mProductNum);
        }
        //数据为空，商品不存在
        else {
            Toast.makeText(getApplicationContext(), "商品详情信息暂时不存在", Toast.LENGTH_SHORT).show();
            mLLDone.setVisibility(View.GONE);
            mTVLoading.setText("商品详情信息暂时不存在");
        }
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
        mFLModelOne.getChildAt(0).setClickable(true);

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
     *
     * @param mNetImages
     */
    private void initImgs(List<HomeAdModel> mNetImages) {
        if (mNetImages.size() == 1) {
            mConvenientBanner.setCanLoop(false);
        } else {
            mConvenientBanner.setCanLoop(true);
        }

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
     *
     * @param i
     */
    public void setSize(int i) {
        if (mSizeBeans.size() != 0) {
            isRushing = mSizeBeans.get(i).isIf_rush_to_purchasing();
            isRush = mSizeBeans.get(i).isIs_frame_product();
            //控制抢购状态的显示和隐藏

            if (isRush) {
                mLLISRushState.setVisibility(View.VISIBLE);
                if (isRushing) {
                    mTVIsRushState.setText("火爆抢购中");
                    mMoney = Double.valueOf(mSizeBeans.get(i).getRush_price());
                } else {
                    mTVIsRushState.setText("不在抢购时段");
                    mMoney = Double.valueOf(mSizeBeans.get(i).getPrice());
                }
            } else {
                mMoney = Double.valueOf(mSizeBeans.get(i).getPrice());
                mLLISRushState.setVisibility(View.GONE);
            }


            mTVMoney.setText(DoubleTextUtils.setDoubleUtils(mMoney) + "");

            mIntegral = mSizeBeans.get(i).getIntegration_price();
            mTVIntegral.setText(mIntegral + "");

            mTVSoldNum.setText("已经出售" + mSizeBeans.get(i).getSales_volume() + "件");

            mGetIntegral = mSizeBeans.get(i).getPresented_gold();
            mTVGetIntegral.setText(mGetIntegral + "");

            mTvWeight.setText(mSizeBeans.get(i).getWeight() + "kg");

            mSizeID = mSizeBeans.get(i).getId();
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
                    .load(ImageUtils.getThumb(imagePath, ScreenUtils.getScreenWidth(GoodsDetailsActivity.this) / 2, 0))
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
        ArrayList<String> urls = new ArrayList<>();
        for (HomeAdModel ham : mNetImages) {
            String str = ham.getImage_path() + File.separator + ham.getImage_base_name();
            urls.add(str);
        }

        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }


    /**
     * 处理点击事件
     *
     * @param v
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.left_layout:
                finish();
                break;
            //调用分享的方法进行分享
            case R.id.right_layout:
                break;

            //商品减少
            case R.id.iv_goods_detail_reduce:
                reduceProduct();
                break;
            //商品增加
            case R.id.iv_goods_detail_add:
                addProduct();
                break;
            //添加到购物车
            case R.id.tv_goods_details_addtocar:
                if (isSizeHave) {
                    addToCar();
                } else {
                    Toast.makeText(getApplicationContext(), "该商品暂不支持购买，sorry！", Toast.LENGTH_SHORT).show();
                }
                break;
            //跳转到购物车页面
            case R.id.iv_goods_details_addtocar:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(MainActivity.PARAM_ORDER, "order");
                jump(intent, false);
                break;
            //去付款
            case R.id.ll_goods_detail_pay:
//                goTOPay();
                if (isSizeHave) {
                    goToPay();
                } else {
                    Toast.makeText(getApplicationContext(), "该商品暂不支持购买，sorry！", Toast.LENGTH_SHORT).show();
                }

                break;
            //再次获取数据
            case R.id.tv_get_data_again:
                initData();
                break;

        }
    }

    /**
     * 去付款
     */
    private void goToPay() {
        String token = CurrentUserManager.getUserToken();
        //存在token说明已经登录，去付款
        if (!TextUtils.isEmpty(token)) {
            makeOrder();
        } else {
            //未登录跳转到登录页面
            Toast.makeText(this, "请先登录，再进行操作", Toast.LENGTH_SHORT).show();
            jump(LoginActivity.class, false);
        }
    }


    /**
     * 结账，下订单
     */

    private void makeOrder() {
        List<CartItem2> goodList = new ArrayList<>();
        long product_id = mProduct.getId();
        long product_size_id = mProduct.getSizes().get(mSizePosition).getId();
        int number = mProductNum;

        CartModel.ProductBean product = new CartModel.ProductBean();
        product.setName(mProduct.getName());
        product.setProduct_type(mProduct.getProduct_type());
        product.setDeleted_at((String) mProduct.getDeleted_at());

        CartModel.ProductSizeBean product_size = new CartModel.ProductSizeBean();
        product_size.setName(mProduct.getSizes().get(mSizePosition).getName());
        product_size.setPrice(mProduct.getSizes().get(mSizePosition).getPrice());
        product_size.setIntegration_price(mProduct.getSizes().get(mSizePosition).getIntegration_price());
        product_size.setPresented_gold(mProduct.getSizes().get(mSizePosition).getPresented_gold());
        product_size.setWeight(mProduct.getSizes().get(mSizePosition).getWeight());
        product_size.setDeleted_at((String) mProduct.getSizes().get(mSizePosition).getDeleted_at());


        CartModel.ProductImagesBean product_images = new CartModel.ProductImagesBean();
        if (mProduct.getImages().size() > 0) {
            product_images.setImage_base_name(mProduct.getImages().get(0).getImage_base_name());
            product_images.setImage_path(mProduct.getImages().get(0).getImage_path());
            product_images.setDeleted_at((String) mProduct.getImages().get(0).getDeleted_at());
        }
        CartModel model = new CartModel();
        model.setNumber(number);
        model.setProduct(product);
        model.setProduct_id(product_id);
        model.setProduct_images(product_images);
        model.setProduct_size(product_size);
        model.setProduct_size_id(product_size_id);
        CartItem2 item = new CartItem2();
        item.setCartModel(model);
        item.setChecked(true);
        goodList.add(item);

        OrderMakeActivity.actionStart(this, goodList, 1);
    }

    /**
     * 添加到购物车
     */
    private void addToCar() {
        StringBuffer sb = new StringBuffer(ClientAPI.API_POINT);
        sb.append("api/v1/shoppingCart/add");
        OkHttpUtils
                .get()
                .url(sb.toString().trim())
                .addParams("number", mProductNum + "")
                .addParams("product_id", mProductID + "")
                .addParams("product_size_id", mSizeID + "")
                .addParams("token", CurrentUserManager.getUserToken())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e("makeOrder---e:", e.toString());
                CurrentUserManager.TokenDue(e);
                checkError(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(getApplicationContext(), mProductNum + "件，您的宝贝已经加入购物车了哦", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //对网络异常进行判断
    private void checkError(Exception e) {
        String eString = e.toString().trim();
        LogUtils.e("eString:", eString + "");
        if (eString != null) {
            if (eString.contains("400") || eString.contains("401")) {
                jump(LoginActivity.class, false);
            } else {
                UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
            }
        }
    }

    /**
     * 添加商品
     */
    private void addProduct() {
        //是否达到最大限购数量
        if (mProductNum < mNumLimit) {
            ++mProductNum;
            countMoney();
        } else {
            Toast.makeText(getApplicationContext(), "已达到单品购物上限，\n数量不可增加了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 减少商品
     */
    private void reduceProduct() {

        if (mProductNum <= 1) {
            Toast.makeText(this, "亲不能再减少了哦", Toast.LENGTH_SHORT).show();
            return;
        } else {
            --mProductNum;
            countMoney();
        }

    }

    private void limitNotify() {
        //商品没下架
        if (mProduct != null && mProduct.getOnsell() != 0 && (mProduct.getDeleted_at() == null || mProduct.getDeleted_at() == "")) {
            //超过提醒限购数量
            if (mProductNum >= mBuyLimit) {
                mLLOverNum.setVisibility(View.GONE);
                mTvNotify.setText("数量大于10个，将10日后发货。");
                mTvNotify.setGravity(Gravity.LEFT);
            } else {
                //未超过
                mLLOverNum.setVisibility(View.GONE);
//                mTvNotify.setText("");
                mTvNotify.setGravity(Gravity.LEFT);
            }
        } else {
            //商品下架
            mLLOverNum.setVisibility(View.VISIBLE);
            mTvNotify.setText("商品已下架");
            mTvNotify.setGravity(Gravity.CENTER);
        }
    }


    /**
     * 计算钱数
     */
    private void countMoney() {
        limitNotify();
        mGetIntegralAll = mGetIntegral * mProductNum;
        mTVGetIntegral.setText(mGetIntegralAll + "UU");
        mIntegralAll = mIntegral * mProductNum;
        mTVChooseNum.setText("" + mProductNum);
    }


    /**
     * 图片开始轮播
     */
    @Override
    public void onResume() {
        super.onResume();
        //广告开始自动翻页
        mConvenientBanner.startTurning(5000);
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
