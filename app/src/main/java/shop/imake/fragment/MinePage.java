package shop.imake.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import shop.imake.Constants;
import shop.imake.MainApplication;
import shop.imake.R;
import shop.imake.activity.AboutIUUActivity;
import shop.imake.activity.AddressManagerActivity;
import shop.imake.activity.HistoryBuyNewActivity;
import shop.imake.activity.LoginActivity;
import shop.imake.activity.MineCustomerServiceSuggestionActivity;
import shop.imake.activity.MyCommissionActivity;
import shop.imake.activity.MyIncomeActivity;
import shop.imake.activity.MyOrderActivity;
import shop.imake.activity.MyZhongHuiQuanActivity;
import shop.imake.activity.SettingsActivity;
import shop.imake.activity.TelephoneFeeChargeActivity;
import shop.imake.activity.UpdateMineUserMessageActivity;
import shop.imake.activity.WebShowActivity;
import shop.imake.adapter.MineAdapter;
import shop.imake.adapter.MineOtherAdapter;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4Mine;
import shop.imake.client.ClientAPI;
import shop.imake.client.ClientApiHelper;
import shop.imake.client.HttpUrls;
import shop.imake.model.MyMine;
import shop.imake.model.MyMineOther;
import shop.imake.model.User;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.DialUtils;
import shop.imake.utils.DialogUtils;
import shop.imake.utils.LogUtils;
import shop.imake.utils.NTalkerUtils;
import shop.imake.utils.NetStateUtils;
import shop.imake.utils.SPUtils;
import shop.imake.utils.ScreenUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.widget.NoScrollGridView;

/**
 * ?????????
 *
 * @author Alice
 * @author JackB
 * @date 2016/5/31
 * @date 2016/6/20
 */
public class MinePage extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String TAG = MinePage.class.getSimpleName();

    //    ????????????
    private NoScrollGridView gv;

    //  ??????????????????
    private List<MyMine> dataListLogin;
    private List<MyMine> dataListNotLogin;
    //?????????????????????
    private MineAdapter adapter;

    //    ??????????????????
    private View mLoginView;

    //    ????????????
    private TextView mBtLogin;

    //    ??????????????????
    private View mNotLoginView;

    //??????????????????
    private String mImgUrl;


    private TextView mTVGoldCoinNum;
    private TextView mTVIntegralNum;

    //???????????????
    private TextView mTVTel;
    //??????
    private TextView mTvName;

    //??????
    private User mUser;
    private PullToRefreshScrollView mScrollView;
    //??????????????????
    private LinearLayout mRLIncome;
    //??????????????????
    private LinearLayout mRLCommission;
    //????????????
    private LinearLayout mRLZhonghui;

    private LinearLayout mLLUnNetWork;
    //UU??????
    private long mCoin;
    //????????????
    private long mIntegral;
    //????????????????????????
    private boolean isLogin = false;

    //????????????
    private CircleImageView mIVUserIcon;


    private Intent mIntentSafeCode;
    //????????????????????????????????????
    private boolean mIsHaveSafeCode = false;

    //???????????????
    private boolean isHaveEmail = false;

    //????????????
    private String mEmail;
    //???????????????
    private String mSafeCode;
    //?????????????????????
    private int mSafeCodeMinLimit = 8;
    private int mSafeCodeMaxLimit = 16;
    //????????????
    private int mHeight;
    //??????????????????
    private ImageView mIvMember;
    //???????????????
    private ImageView mIvMemberFive;


    //????????????
    //??????
    private TextView mTVEmail;
    //????????????
    private LinearLayout mLLSupplyPhone;

    ////////////????????????????????????///////////////////////
    //????????????????????????
    private MaterialDialog mSafeCodeDialog;
    //????????????????????????
    private MaterialDialog mFindSafeCodeDialog;
    //???????????????????????????
    private MaterialDialog mEmailSendSucceedDialog;
    //??????????????????
    private String mImgName;
    //??????????????????
    private String mUserImgUrl;
    //????????????????????????
    private boolean isHaveImg;
    //????????????
    private TextView mTvWithdrawNum;

    //??????????????????
    private Api4Mine mClientApi;
    //??????????????????
    private LinearLayout mLLServicePhone;
    //??????????????????
    private NoScrollGridView mGvOther;
    //????????????
    private Api4Mine mClient;
    //??????????????????????????????
    private MineOtherAdapter mMyMineLifeAdapter;
    //??????????????????????????????
    private MineOtherAdapter mMyMineOtherAdapter;
    //??????????????????
    private List<MyMineOther.ThreeServicesBean> myMineList;

    //????????????????????????
    private List<MyMineOther.ThreeServicesBean> mLifeList;
    //??????????????????
    private NoScrollGridView mGvTravel;
    //????????????
    private String mTelNum;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_mine, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initVariate();
        initView();
        setupView();
        initCtl();
        initData();

        initOtherData();//????????????????????????


    }

    @Override
    public void onResume() {
        super.onResume();

        mIVUserIcon.setImageResource(R.mipmap.list_profile_photo);

        //?????????????????????????????????????????????????????????
        String token = CurrentUserManager.getUserToken();
        if (TextUtils.isEmpty(token)) {
            isLogin = false;
            mLoginView.setVisibility(View.GONE);
            mNotLoginView.setVisibility(View.VISIBLE);
//            initGridViewChange();
            mTVIntegralNum.setText("" + 0);
            mTVGoldCoinNum.setText("" + 0);
            mTvWithdrawNum.setText("" + 0);
        } else {
            mLoginView.setVisibility(View.VISIBLE);
            mNotLoginView.setVisibility(View.GONE);
            //??????????????????
            SPUtils.put(getContext(), Constants.USER, "");
            initData();
        }
        dialogDismiss(mFindSafeCodeDialog);
        dialogDismiss(mEmailSendSucceedDialog);
        dialogDismiss(mSafeCodeDialog);

    }


    /**
     * ????????????????????????
     */

    private void initOtherData() {
        if (!NetStateUtils.isNetworkAvailable(getContext())) {
            ToastUtils.showShort(getString(R.string.xn_toast_nointernet));
            return;
        }

        //??????????????????????????????
        mClient.getMyMineOther(getActivity(), new DataCallback<MyMineOther>(getActivity()) {
            @Override
            public void onFail(Call call, Exception e, int id) {
                ToastUtils.showException(e);
            }

            @Override
            public void onSuccess(Object response, int id) {
                if (response != null) {
                    MyMineOther myMineOther = (MyMineOther) response;
                    List<MyMineOther.ThreeServicesBean> list = new ArrayList<>();

                    list = myMineOther.getThree_services();
                    mLifeList.clear();
                    myMineList.clear();

                    for (MyMineOther.ThreeServicesBean item : list) {
                        int type = item.getType();
                        //????????????
                        if (type == 1) {
                            mLifeList.add(item);

                            //????????????
                        } else if (type == 2) {
                            myMineList.add(item);

                        }
                    }

                    mMyMineOtherAdapter = new MineOtherAdapter(myMineList, getContext());
                    mGvOther.setAdapter(mMyMineOtherAdapter);
                    mMyMineLifeAdapter = new MineOtherAdapter(mLifeList, getContext());
                    mGvTravel.setAdapter(mMyMineLifeAdapter);
                }
            }
        });
    }


    private void initVariate() {
        mClient = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);
    }


    private void initView() {
        mHeight = ScreenUtils.getScreenHeight(getContext());
        //????????????????????????
        mLoginView = layout.findViewById(R.id.rl_mine_head_login);
        //????????????????????????
        mNotLoginView = layout.findViewById(R.id.ll_mine_head_notlogin);

        //????????????
        mLLUnNetWork = ((LinearLayout) layout.findViewById(R.id.ll_mine_un_network));
        //????????????????????????
        gv = ((NoScrollGridView) layout.findViewById(R.id.gv_mine));
        //????????????????????????
        mGvOther = ((NoScrollGridView) layout.findViewById(R.id.gv_mine_other));

        mGvTravel = ((NoScrollGridView) layout.findViewById(R.id.gv_mine_travel));

        initGridView();
        //?????????????????????????????????
        mBtLogin = ((TextView) layout.findViewById(R.id.bt_mine_login));


        //???????????????????????????
        //????????????
        mIVUserIcon = ((CircleImageView) layout.findViewById(R.id.iv_mine_head));
        //????????????
        mTvName = ((TextView) layout.findViewById(R.id.tv_mine_ll_login_name));
        //??????
        mTVTel = ((TextView) layout.findViewById(R.id.tv_mine_tel));

        //????????????
        mTvWithdrawNum = ((TextView) layout.findViewById(R.id.tv_mine_withdraw_num));

        //????????????
        mRLIncome = ((LinearLayout) layout.findViewById(R.id.rl_mine_income));
        //????????????
        mRLCommission = ((LinearLayout) layout.findViewById(R.id.rl_mine_commission));
        //??????????????????
        mRLZhonghui = ((LinearLayout) layout.findViewById(R.id.rl_mine_zhonghui));


        mTVIntegralNum = ((TextView) layout.findViewById(R.id.tv_mine_integral_num));
        //UU
        mTVGoldCoinNum = ((TextView) layout.findViewById(R.id.tv_mine_goldcoin_num));
        //??????????????????
        mScrollView = ((PullToRefreshScrollView) layout.findViewById(R.id.ptsv_mine));

        //????????????
        mIvMember = ((ImageView) layout.findViewById(R.id.iv_mine_member));
        mIvMemberFive = ((ImageView) layout.findViewById(R.id.iv_mine_member_five));

        //????????????
        mLLSupplyPhone = ((LinearLayout) layout.findViewById(R.id.ll_mine_supply_the_phone));
        //????????????
        mLLServicePhone = ((LinearLayout) layout.findViewById(R.id.ll_mine_service_the_phone));
        //??????
        mTVEmail = ((TextView) layout.findViewById(R.id.tv_mine_email));

    }

    private void setupView() {
        gv.setOnItemClickListener(this);
        mLoginView.setOnClickListener(this);
        // ??????????????????????????????
        gv.setFocusable(false);
        mIVUserIcon.setOnClickListener(this);

        //????????????
        mRLIncome.setOnClickListener(this);
        //????????????
        mRLCommission.setOnClickListener(this);
        //????????????
        mRLZhonghui.setOnClickListener(this);
        mLoginView.setOnClickListener(this);
        mBtLogin.setOnClickListener(this);
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mScrollView.isRefreshing()) {
                    mScrollView.onRefreshComplete();
                    initData();
                    initOtherData();
                }
            }
        });

        mLLSupplyPhone.setOnClickListener(this);
        mLLServicePhone.setOnClickListener(this);

        //????????????????????????
        mGvOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!CurrentUserManager.isLoginUser()) {
                    jump(LoginActivity.class, false);
                    return;
                }
                goToHtml(myMineList,position);
            }
        });

        //????????????????????????
        mGvTravel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!CurrentUserManager.isLoginUser()) {
                    jump(LoginActivity.class, false);
                    return;
                }

                goToHtml(mLifeList,i);

                String param = mLifeList.get(i).getParam();
                if (TextUtils.isEmpty(param)) {
                    return;
                }

                switch (param) {
                    case "plane":
                        //?????????
                        showTicketReadmeDialog(
                                ClientAPI.URL_WX_H5 + HttpUrls.MINE_README_PLANE,
                                new TicketReadmeCallback() {

                                    @Override
                                    public void agree() {
                                        NTalkerUtils.getInstance().startChat(getActivity(), NTalkerUtils.entryType.kefu_plane);
                                    }
                                });
                        break;
                    case "train":
                        //?????????
                        showTicketReadmeDialog(
                                ClientAPI.URL_WX_H5 + HttpUrls.MINE_README_TRAIN,
                                new TicketReadmeCallback() {

                                    @Override
                                    public void agree() {
                                        NTalkerUtils.getInstance().startChat(getActivity(), NTalkerUtils.entryType.kefu_train);
                                    }
                                });
                        break;

                    case "topup"://????????????
                        if (TextUtils.isEmpty(mTelNum)) {
                            initData();
                            return;
                        }
                        TelephoneFeeChargeActivity.startAction(getActivity(), mTelNum);
                        break;
                    default:
                        break;

                }
            }
        });
    }

    private void showTicketReadmeDialog(String url, final TicketReadmeCallback callback) {

        View readMeView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_ticket_readme, null);
        View closeView = readMeView.findViewById(R.id.ticket_dialog_close);
        WebView webview = (WebView) readMeView.findViewById(R.id.ticket_dialog_webview);
        View agreeView = readMeView.findViewById(R.id.ticket_dialog_agree);

        ViewGroup.LayoutParams layoutParams = webview.getLayoutParams();
        layoutParams.height = ScreenUtils.getScreenHeight(getContext());

        final Dialog readmeDialog = DialogUtils.createRandomDialog(getContext(), null, null, null, null, null, readMeView);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ticket_dialog_close:
                        if (readmeDialog != null && readmeDialog.isShowing()) {
                            readmeDialog.dismiss();
                        }
                        break;

                    case R.id.ticket_dialog_agree:
                        if (readmeDialog != null && readmeDialog.isShowing()) {
                            readmeDialog.dismiss();
                            callback.agree();
                        }
                        break;
                }
            }
        };

        closeView.setOnClickListener(onClickListener);
        agreeView.setOnClickListener(onClickListener);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        webview.loadUrl(url);

        readmeDialog.show();
    }

    /**
     * ????????????????????????????????????
     *
     * @param position
     */
    private void goToHtml(List<MyMineOther.ThreeServicesBean> list, int position) {
        if (list != null && position < list.size()) {
            MyMineOther.ThreeServicesBean threeServicesBean = list.get(position);
            int type = threeServicesBean.getIs_open();
            String param=threeServicesBean.getParam();

            if (!TextUtils.isEmpty(param)){
                return;
            }

            if (type == 1) {
                StringBuilder stringBuilder = new StringBuilder(threeServicesBean.getRequest_url());
                stringBuilder.append("?token=").append(CurrentUserManager.getUserToken())
                        .append("&type=android").append("&vt=").append(System.currentTimeMillis());
                String htmlUrl = stringBuilder.toString();

                WebShowActivity.actionStart(getContext(), htmlUrl, WebShowActivity.PARAM_PAGE_HIDE);
            } else if (type == 0) {
                Dialog dialog = DialogUtils.createMessageDialog(
                        getActivity(), null, "??????????????????????????????????????????", "??????",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
                dialog.show();
            }
        }
    }

    /**
     * ??????????????????
     * ?????????????????????
     * ?????????????????????
     */

    private void initData() {
        if (!NetStateUtils.isNetworkAvailable(getContext())) {
            ToastUtils.showShort(getString(R.string.xn_toast_nointernet));
            return;
        }
        //??????????????????Loading
//        showLoadingDialog();
        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????

        if (TextUtils.isEmpty(CurrentUserManager.getUserToken())) {
            return;
        }
        ClientAPI.getWithdraw(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                CurrentUserManager.TokenDue(e);
                initUserData();
            }

            @Override
            public void onResponse(String response, int id) {
                initUserData();
            }
        });

    }


    private void initCtl() {
        myMineList = new ArrayList<>();

        mLifeList = new ArrayList<>();
    }

    /**
     * ??????????????????
     */

    private void initUserData() {

        if (!NetStateUtils.isNetworkAvailable(getContext())) {
            ToastUtils.showShort(getString(R.string.xn_toast_nointernet));
            return;
        }
        //???????????????????????????

        mClientApi = (Api4Mine) ClientApiHelper.getInstance().getClientApi(Api4Mine.class);

        //?????????????????????????????????
        String token = CurrentUserManager.getUserToken();


        if (token != null) {
            mClientApi.getUserMessage(TAG, new DataCallback<User>(getContext()) {
                @Override
                public void onFail(Call call, Exception e, int id) {
                    CurrentUserManager.TokenDue(e);
                    //????????????
                    if (!NetStateUtils.isNetworkAvailable(getContext())) {
                        mLLUnNetWork.setVisibility(View.VISIBLE);
                    } else {
                        mLLUnNetWork.setVisibility(View.GONE);
                        String eString = e.toString();
                        LogUtils.e("--eString:", eString);
                        if (eString != null) {
                            if (eString.contains("400") || eString.contains("401")) {
                            } else {
                                ToastUtils.showException(e, getContext());
                            }
                        }
                    }
                    //token????????????????????????????????????????????????????????????
                    isLogin = false;
                    mLoginView.setVisibility(View.GONE);
                    mNotLoginView.setVisibility(View.VISIBLE);
                    mTVIntegralNum.setText("" + 0);
                    mTVGoldCoinNum.setText("" + 0);
                    mTvWithdrawNum.setText("" + 0);
//                    initGridViewChange();
                }

                @Override
                public void onSuccess(Object response, int id) {
                    isLogin = true;
                    //??????????????????
                    mLLUnNetWork.setVisibility(View.GONE);
                    mLoginView.setVisibility(View.VISIBLE);
                    mNotLoginView.setVisibility(View.GONE);
                    if (response != null) {
                        mUser = (User) response;
                        setData();
                    } else {
                        ToastUtils.showShort("??????????????????");
                    }

                }
            });
        } else {
            Toast.makeText(getActivity(), "???????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        //?????????????????????????????????

        User.MemberBean memberBean = mUser.getMember();
//        initGridViewChange();
        CurrentUserManager.setCurrentUser(memberBean);

//        //?????????
//        mTvName.setText(memberBean.getName());
//        //??????
        if (memberBean != null) {
            String name = memberBean.getNick_name();
            if (!TextUtils.isEmpty(name)) {
                mTvName.setText(name);
            } else {
                mTvName.setText("");
            }

            //??????????????????????????????
            String tel = memberBean.getPhone().trim();
            if (!TextUtils.isEmpty(tel)) {
                mTelNum = tel;
            }


            if (!TextUtils.isEmpty(tel)) {
                tel = tel.substring(0, 3) + "****" + tel.substring(tel.length() - 4, tel.length());
                mTVTel.setText(tel);
            } else {
                mTVTel.setText("");
            }


            mEmail = memberBean.getEmail().trim();
            if (!TextUtils.isEmpty(mEmail)) {
                isHaveEmail = true;
                String email;
                if (mEmail.length() >= 3) {
                    email = mEmail.substring(0, 2) + "****" + mEmail.substring(mEmail.indexOf("@"), mEmail.length());
                } else {
                    email = mEmail;
                }
                mTVEmail.setText(email);
            } else {
                isHaveEmail = false;
                mTVEmail.setText("");
            }


            if (!TextUtils.isEmpty(mSafeCode)) {
                mIsHaveSafeCode = true;
            } else {
                mIsHaveSafeCode = false;
            }
            mSafeCode = memberBean.getSecurity_code_hint();
            mIsHaveSafeCode = memberBean.isSecurity_code_state();

            mCoin = memberBean.getMoney_quantity();
            mTVGoldCoinNum.setText(mCoin + "");
            mIntegral = memberBean.getIntegration();
            mTVIntegralNum.setText(mIntegral + "");


            String inCome = memberBean.getPush_money();

            int inComeNum = 0;

            String canDraw = memberBean.getCan_drawings_amount();

            double canDrawNum = 0;
            if (!TextUtils.isEmpty(canDraw)) {
                canDrawNum = Double.valueOf(canDraw);
            }

            LogUtils.e("inCome", "" + inCome);
            LogUtils.e("canDraw", "" + canDraw);

            //????????????
            int money = (int) (inComeNum + canDrawNum);
            mTvWithdrawNum.setText("" + money);

            mImgUrl = memberBean.getAvatar_path();
            mImgName = memberBean.getAvatar_name();


            //????????????
            if (!TextUtils.isEmpty(mImgUrl) && !TextUtils.isEmpty(mImgName)) {
                isHaveImg = true;
                mUserImgUrl = mImgUrl + "/" + mImgName;
                LogUtils.e("UserImgUrl", mUserImgUrl);
                Glide.with(MainApplication.getContext())
                        .load(mUserImgUrl)
//                        .signature(new StringSignature(UUID.randomUUID().toString()))
                        //??????????????????????????????
//                        .placeholder(R.mipmap.list_profile_photo)
//                        .error(R.mipmap.list_profile_photo)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//??????????????????
                        .skipMemoryCache(true)//??????????????????
                        .into(mIVUserIcon);
            } else {
                isHaveImg = false;
            }


            //??????????????????????????????
            LogUtils.e("getMember_type", "" + memberBean.getMember_type());
            if (memberBean.getMember_type() == 5) {
                mIvMemberFive.setVisibility(View.GONE);
            } else {
                mIvMemberFive.setVisibility(View.GONE);
            }

            LogUtils.e("getIs_vip", "" + memberBean.getIs_vip());
            if (memberBean.getIs_vip() == 2) {
                mIvMember.setVisibility(View.GONE);
            } else {
                mIvMember.setVisibility(View.GONE);
            }

            int memberLevel = memberBean.getMember_level();
            if (memberLevel == 1 || memberLevel == 2 || memberLevel == 3) {
                mIvMember.setVisibility(View.VISIBLE);
            } else {
                mIvMember.setVisibility(View.GONE);
            }
        }
    }

    /**
     * ????????????????????????
     */
    private void initGridView() {
        initLoginGv();
//        initNotLoginGv();
    }

    /**
     * ???????????????????????????
     */
    private void initGridViewChange() {
        if (isLogin) {
            initLoginGv();

        } else {
//            initNotLoginGv();
            initLoginGv();
        }
    }

    /**
     * ??????????????????????????????
     */
    private void initLoginGv() {
        //??????????????????????????????
        dataListLogin = new ArrayList<>();

        MyMine mine1 = new MyMine("????????????", R.mipmap.list_order_nor);
        dataListLogin.add(mine1);
        MyMine mine2 = new MyMine("????????????", R.mipmap.list_historyrecorde_nor);
        dataListLogin.add(mine2);
        MyMine mine3 = new MyMine("????????????", R.mipmap.list_addressinformation_nor);
        dataListLogin.add(mine3);
        MyMine mine4 = new MyMine("???????????????", R.mipmap.personal_centeriocn_icon_my_assets);
        dataListLogin.add(mine4);
        MyMine mine6 = new MyMine("???????????????", R.mipmap.list_imake_nor);
        dataListLogin.add(mine6);
        MyMine mine8 = new MyMine("????????????", R.mipmap.list_online_service);
        dataListLogin.add(mine8);

        MyMine mine5 = new MyMine("????????????", R.mipmap.list_icon_customerservice_nor);
        dataListLogin.add(mine5);

        MyMine mine7 = new MyMine("??????", R.mipmap.list_set_nor);
        dataListLogin.add(mine7);

        //test
//        MyMine mine9 = new MyMine("????????????", R.mipmap.personal_centeriocn_icon_prepaid_refill);
//        dataListLogin.add(mine9);

        adapter = new MineAdapter(getActivity(), gv, dataListLogin);
        gv.setAdapter(adapter);


////////////////////????????????/////////////////////////
//        List<MyMine> dataListTravel = new ArrayList<>();
//        MyMine mine01 = new MyMine("?????????", R.mipmap.personal_centeriocn_icon_plane);
//
//        MyMine mine02 = new MyMine("?????????", R.mipmap.personal_centeriocn_icon_train);
//
//        MyMine mine03 = new MyMine("????????????", R.mipmap.personal_centeriocn_icon_prepaid_refill);
//
//        dataListTravel.add(mine03);
//        dataListTravel.add(mine01);
//        dataListTravel.add(mine02);
//
//        MineAdapter adapter = new MineAdapter(getActivity(), mGvTravel, dataListTravel);
//        mGvTravel.setAdapter(adapter);

    }

    /**
     * ?????????????????????????????????
     */

    private void initNotLoginGv() {
        //???????????????????????????
        dataListNotLogin = new ArrayList<>();
        MyMine mine11 = new MyMine("????????????", R.mipmap.list_order_nor);
        dataListNotLogin.add(mine11);
        MyMine mine22 = new MyMine("????????????", R.mipmap.list_historyrecorde_nor);
        dataListNotLogin.add(mine22);
        MyMine mine33 = new MyMine("????????????", R.mipmap.list_addressinformation_nor);
        dataListNotLogin.add(mine33);
        MyMine mine44 = new MyMine("????????????", R.mipmap.list_addfriend_nor);
        dataListNotLogin.add(mine44);
        MyMine mine55 = new MyMine("???????????????", R.mipmap.list_imake_nor);
        dataListNotLogin.add(mine55);
        MyMine mine66 = new MyMine("????????????", R.mipmap.list_icon_customerservice_nor);
        dataListNotLogin.add(mine66);
        MyMine mine77 = new MyMine("????????????", R.mipmap.list_set_nor);
        dataListNotLogin.add(mine77);

        //test
//        MyMine mine8= new MyMine("???????????????", R.mipmap.list_signinwechat);
//        dataListNotLogin.add(mine8);
//        MyMine mine9= new MyMine("??????QQ", R.mipmap.list_signinqq);
//        dataList.add(mine9);

        adapter = new MineAdapter(getActivity(), gv, dataListNotLogin);
        gv.setAdapter(adapter);
    }


    /**
     * ???????????????????????????
     */
    private void updateMineUserMessage() {
        mIntentSafeCode = new Intent(getActivity(), UpdateMineUserMessageActivity.class);
        mIntentSafeCode.putExtra("mUser", mUser);
        if (isHaveImg) {
            mIntentSafeCode.putExtra("mUserImgUrl", mUserImgUrl);
        } else {
            mIntentSafeCode.putExtra("mUserImgUrl", "");
        }
        createSafeCodeDialog();
    }

    /**
     * ????????????
     *
     * @param cls      ?????????????????????
     * @param isFinish ??????????????????
     */
    public void jump(Class<?> cls, boolean isFinish) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
        if (isFinish) {
            getActivity().finish();
        }
    }


    ////////////////////////////////////////////////?????????????????????/////////////////////////////////////////////

    private EditText mEtEmail;
    //??????????????????
    private EditText etSateCodeInput;
    //????????????????????????
    private EditText etSateCodeMakeSure;
    //?????????????????????
    private TextView tvSateCodeMode;
    //????????????
    private TextView tvSateCodeSubmit;
    //???????????????
    private TextView tvSateCodeForget;
    //????????????
    private ImageView ivSateCodeClose;
    //?????????????????????????????????
    private LinearLayout mLLSetEmail;


    //?????????????????????
    private InputFilter etInputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            //??????null???????????????????????????,????????????????????????????????????????????????

            if (source.equals(" ")) return "";
            else return null;
        }
    };

    /**
     * ????????????????????????
     */
    private void createSafeCodeDialog() {
        if (mSafeCodeDialog != null) {
            mSafeCodeDialog.dismiss();
        }
        mSafeCodeDialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.mine_safe_code_layout, false)
                .build();

        View view = mSafeCodeDialog.getCustomView();
        mSafeCodeDialog.setCancelable(false);
        //??????????????????
//        mSafeCodeDialog.getWindow().setWindowAnimations(R.style.Dialog_Anim_Style_Up_Down);

        etSateCodeInput = (EditText) view.findViewById(R.id.et_safe_code_input);
        etSateCodeMakeSure = (EditText) view.findViewById(R.id.et_safe_code_input_makesure);
        tvSateCodeMode = (TextView) view.findViewById(R.id.tv_safe_code_mode);
        tvSateCodeSubmit = (TextView) view.findViewById(R.id.tv_safe_code_submit);
        tvSateCodeForget = (TextView) view.findViewById(R.id.tv_safe_code_forget);
        tvSateCodeForget = (TextView) view.findViewById(R.id.tv_safe_code_forget);
        ivSateCodeClose = (ImageView) view.findViewById(R.id.iv_safe_code_close);
        mLLSetEmail = ((LinearLayout) view.findViewById(R.id.ll_safe_code_email));
        mEtEmail = ((EditText) view.findViewById(R.id.et_safe_code_input_email));

        //???????????????????????????
        etSateCodeInput.setFilters(new InputFilter[]{etInputFilter});


        //??????????????????????????????
        etSateCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String safeCode = s.toString();
                int inPutLength = safeCode.length();
                if (inPutLength > mSafeCodeMaxLimit) {
                    Toast.makeText(getContext(), "??????????????????" + mSafeCodeMaxLimit + "???", Toast.LENGTH_SHORT).show();
                    etSateCodeInput.setText(safeCode.substring(0, mSafeCodeMaxLimit));
                    etSateCodeInput.setSelection(mSafeCodeMaxLimit);
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // ??????????????????
        ivSateCodeClose.setOnClickListener(this);
        tvSateCodeSubmit.setOnClickListener(this);
        tvSateCodeForget.setOnClickListener(this);


        //???????????????????????????????????????????????????
        if (mIsHaveSafeCode) {
            //??????????????????????????????????????????
            //???????????? ?????????EditText???android:digits????????????
//            etSateCodeInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            etSateCodeInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

            etSateCodeInput.setText("");
            tvSateCodeMode.setText("???????????????");
            tvSateCodeMode.setGravity(Gravity.CENTER);
            tvSateCodeForget.setVisibility(View.VISIBLE);
            tvSateCodeForget.setEnabled(true);
            tvSateCodeForget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            etSateCodeMakeSure.setVisibility(View.GONE);
            etSateCodeInput.setHint(mSafeCode + "********");
            mLLSetEmail.setVisibility(View.GONE);
        } else {
            if (isHaveEmail) {
                tvSateCodeMode.setText("???????????????");
                tvSateCodeMode.setGravity(Gravity.CENTER);
                tvSateCodeForget.setVisibility(View.INVISIBLE);
                tvSateCodeForget.setEnabled(false);
                etSateCodeMakeSure.setVisibility(View.GONE);
                etSateCodeInput.setHint("8~16???????????????????????????");
                mLLSetEmail.setVisibility(View.GONE);
            } else {
                tvSateCodeMode.setText("???????????????");
                tvSateCodeForget.setVisibility(View.INVISIBLE);
                tvSateCodeForget.setEnabled(false);
                etSateCodeMakeSure.setVisibility(View.GONE);
                etSateCodeInput.setHint("8~16???????????????????????????");
                mLLSetEmail.setVisibility(View.GONE);
            }
        }

        //???????????????
        mSafeCodeDialog.show();
    }


    /**
     * ??????????????????
     */
    private void emailValidate() {
        String emailReset = etSateCodeReGetEmailInput.getText().toString().trim();
        LogUtils.e("emailReset----", mEmail);
        if (isHaveEmail) {
            if (mEmail.equals(emailReset)) {
                //????????????????????????
                String token = CurrentUserManager.getUserToken();
                if (!TextUtils.isEmpty(token)) {
                    ClientAPI.sendEmailResetCode(token, mEmail, new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            CurrentUserManager.TokenDue(e);
                            UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                            dialogDismiss(mFindSafeCodeDialog);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            //???????????????????????????????????????????????????
                            //???????????????????????????
                            dialogDismiss(mFindSafeCodeDialog);
                            createEmailSendSucceedDialog();
                            return;
                        }
                    });
                }

            } else {
                Toast.makeText(getContext(), "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(getContext(), "????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * ???????????????????????????
     */
    private void submitSafeCode() {
        loadingDialog.show();

        //?????????????????????   mClassJump  ???
        //???????????????
        String safeCode = etSateCodeInput.getText().toString();
        int minLeght = safeCode.length();
        if (minLeght < mSafeCodeMinLimit) {
            Toast.makeText(getContext(), "????????????????????????8??????", Toast.LENGTH_SHORT).show();
            return;
        }
        String token = CurrentUserManager.getUserToken().toString().trim();
        //???????????????
        if (mIsHaveSafeCode) {
            ClientAPI.postValidateSafeCode(token, safeCode, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    loadingDialog.dismiss();
                    CurrentUserManager.TokenDue(e);
                    LogUtils.e("getMessage-----", e.getMessage());
                    UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                    dialogDismiss(mSafeCodeDialog);
                }

                @Override
                public void onResponse(String response, int id) {
                    loadingDialog.dismiss();
                    startActivity(mIntentSafeCode);
                    dialogDismiss(mSafeCodeDialog);
                }
            });
            return;
        } else {
            //??????????????? mIsHaveSafeCode=true

            //??????????????????????????????
            ClientAPI.postSetSafeCode(token, safeCode, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    loadingDialog.dismiss();
                    CurrentUserManager.TokenDue(e);
                    UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
                    dialogDismiss(mSafeCodeDialog);
                }

                @Override
                public void onResponse(String response, int id) {
                    loadingDialog.dismiss();
                    //????????????????????????
                    initData();
                    dialogDismiss(mSafeCodeDialog);
                }
            });

//            //???????????????????????????
//            if (!isHaveEmail) {
//                String email = mEtEmail.getText().toString().trim();
//                if (!(!TextUtils.isEmpty(email) && email.length() > 1 && ValidatorsUtils.isEmail(email))) {
//                    Toast.makeText(getContext(), "????????????????????????", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                ClientAPI.postSetSafeCode(token, safeCode, email, new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        CurrentUserManager.TokenDue(e);
//                        UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
//                        dialogDismiss(mSafeCodeDialog);
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        //????????????????????????
//                        initData();
//                        dialogDismiss(mSafeCodeDialog);
//                    }
//                });
//            } else {
//                //??????????????????????????????
//                ClientAPI.postSetSafeCode(token, safeCode, new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        CurrentUserManager.TokenDue(e);
//                        UNNetWorkUtils.unNetWorkOnlyNotify(getContext(), e);
//                        dialogDismiss(mSafeCodeDialog);
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        //????????????????????????
//                        initData();
//                        dialogDismiss(mSafeCodeDialog);
//                    }
//                });
//
//            }
        }

        dialogDismiss(mSafeCodeDialog);
    }

    private void dialogDismiss(MaterialDialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }

    }

    ///////////////////////////??????????????????????????????/////////////////////////////////////////////////


    /**
     * ??????????????????????????????
     */

    //????????????
    private EditText etSateCodeReGetEmailInput;
    //??????
    private TextView tvSateCodeEmail;
    //????????????
    private TextView tvSateCodeFindSubmit;
    //??????????????????
    private TextView tvSateCodeCallCentre;
    //??????????????????
    private ImageView ivSateCodeFindClose;

    private void createFindSafeCodeDialog() {
        if (mFindSafeCodeDialog != null) {
            mFindSafeCodeDialog.dismiss();
        }

        mFindSafeCodeDialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.mine_safe_code_forget_layout, false)
                .build();
        View view = mFindSafeCodeDialog.getCustomView();
//        mFindSafeCodeDialog.setCancelable(false);

        etSateCodeReGetEmailInput = (EditText) view.findViewById(R.id.tv_input_safe_code_forget);
        tvSateCodeEmail = (TextView) view.findViewById(R.id.tv_email_safe_code_forget);
        tvSateCodeFindSubmit = (TextView) view.findViewById(R.id.tv_safe_code_forget_submit);
        tvSateCodeCallCentre = (TextView) view.findViewById(R.id.tv_safe_code_forget_callcentre);
        ivSateCodeFindClose = (ImageView) view.findViewById(R.id.iv_safe_code_forget_close);

        tvSateCodeCallCentre.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        etSateCodeReGetEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                tvSateCodeFindSubmit.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        // ????????????????????????
        tvSateCodeFindSubmit.setOnClickListener(this);
        //????????????
        tvSateCodeCallCentre.setOnClickListener(this);
        //????????????
        ivSateCodeFindClose.setOnClickListener(this);

        if (isHaveEmail) {
            String email = mEmail.substring(0, 2) + "****" + mEmail.substring(mEmail.indexOf("@"), mEmail.length());

            etSateCodeReGetEmailInput.setHint(email);
            etSateCodeReGetEmailInput.setEnabled(true);
        } else {
            etSateCodeReGetEmailInput.setHint("?????????????????????????????????????????????");
            etSateCodeReGetEmailInput.setEnabled(false);
        }
        mFindSafeCodeDialog.show();
    }


    ///////////////////////////??????????????????????????????//////////////////////////////////////////////////

    /**
     * ????????????????????????????????????
     */

    private ImageView ivEmailSendSucceedClose;

    private void createEmailSendSucceedDialog() {
        if (mEmailSendSucceedDialog != null) {
            mEmailSendSucceedDialog.dismiss();
        }
        mEmailSendSucceedDialog = new MaterialDialog.Builder(getContext())
                .customView(R.layout.mine_email_send_succeed_layout, false)
                .build();
        View view = mEmailSendSucceedDialog.getCustomView();
        mEmailSendSucceedDialog.setCancelable(false);

        ivEmailSendSucceedClose = ((ImageView) view.findViewById(R.id.iv_email_send_succeed));
        ivEmailSendSucceedClose.setOnClickListener(this);

        mEmailSendSucceedDialog.show();

    }


    ////////////////////////////??????????????????////////////////////////////

    /**
     * @param parent
     * @param view
     * @param position
     * @param id       ????????????????????????
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                if (!CurrentUserManager.isLoginUser()) {
                    jump(LoginActivity.class, false);
                    return;
                }
                // ????????????
                jump(MyOrderActivity.class, false);
                break;
            case 1://????????????
                if (!CurrentUserManager.isLoginUser()) {
                    jump(LoginActivity.class, false);
                    return;
                }

                startActivity(new Intent(getActivity(), HistoryBuyNewActivity.class));
                break;
            case 2:
                if (!CurrentUserManager.isLoginUser()) {
                    jump(LoginActivity.class, false);
                    return;
                }
                //????????????"

                jump(AddressManagerActivity.class, false);
                break;
            case 3:
                if (!CurrentUserManager.isLoginUser()) {
                    jump(LoginActivity.class, false);
                    return;
                }
                //??????????????????WebView
                StringBuilder stringBuilder = new StringBuilder(ClientAPI.URL_WX_H5);
                stringBuilder.append("myzhonghui.html?token=").append(CurrentUserManager.getUserToken())
                        .append("&type=android&vt=").append(System.currentTimeMillis());
                String zhonghuiquanUrl = stringBuilder.toString();
                WebShowActivity.actionStart(getContext(), zhonghuiquanUrl, WebShowActivity.PARAM_PAGE_HIDE);

                break;
            case 6:
                //????????????"

                jump(MineCustomerServiceSuggestionActivity.class, false);
                break;
            case 4:
                // ???????????????"
                jump(AboutIUUActivity.class, false);
                break;

            case 7:
                //??????
                jump(SettingsActivity.class, false);

                break;

            case 5:
                // ????????????
                NTalkerUtils.getInstance().startChat(getActivity(), NTalkerUtils.entryType.kefu);
                break;

            //test
            case 8:
                //test????????????????????????
                TelephoneFeeChargeActivity.startAction(getActivity(), mTelNum);
                break;

            default:
                break;

//            case 8:
//                if (isLogin){
//                    //QQ?????????????????????????????????????????????
//
//
//                }else {
//                    bindPhone();
//                }
//                // Toast.makeText(getActivity(), "??????QQ", Toast.LENGTH_SHORT).show();
//                break;
        }
    }


    /**
     * ??????????????????
     *
     * @param v
     */
    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            //????????????????????????????????????????????????????????????????????????
            //????????????
            case R.id.tv_safe_code_submit:
                //???????????????
                submitSafeCode();
                break;
            //???????????????????????????, popWin
            case R.id.tv_safe_code_forget:
                dialogDismiss(mSafeCodeDialog);
                //??????????????????
                DialUtils.callSafeCodeForget(getContext());
//                //?????????????????????????????????
//                if (isHaveEmail) {
//                    createFindSafeCodeDialog();
//                    //??????????????????????????????
//                } else {
//                    //??????????????????
//                    DialUtils.callSafeCodeForget(getContext());
//                }
                break;
            //??????????????????????????????
            case R.id.iv_safe_code_close:
                dialogDismiss(mSafeCodeDialog);
                break;

            //???????????????????????????????????????
            case R.id.tv_safe_code_forget_submit:
                emailValidate();
                break;

            //?????????????????????????????????
            case R.id.tv_safe_code_forget_callcentre:
                //??????????????????
                DialUtils.callSafeCodeForget(getContext());

//                new AlertView(DialUtils.SERVER_TITLE, null, "??????", null, new String[]{getString(R.string.service_num1),getString(R.string.service_num2)
//                }, getActivity(), AlertView.Style.ActionSheet, this).show();

                dialogDismiss(mFindSafeCodeDialog);
                break;
            //???????????????????????????
            case R.id.iv_safe_code_forget_close:
                dialogDismiss(mFindSafeCodeDialog);
                break;
            //?????????????????????????????????
            case R.id.iv_email_send_succeed:
                dialogDismiss(mEmailSendSucceedDialog);
                break;


            ///////////////////////////????????????????????????// ////////////////////////////////////////////////////////

            case R.id.bt_mine_login: // ??????
                jump(LoginActivity.class, false);

                break;
            case R.id.rl_mine_income: // ????????????
                mIntentSafeCode = new Intent(getActivity(), MyIncomeActivity.class);
                checKUserState();

                break;
            case R.id.rl_mine_commission: // ????????????
                mIntentSafeCode = new Intent(getActivity(), MyCommissionActivity.class);
                checKUserState();
                break;

            case R.id.rl_mine_zhonghui: //???????????????
                mIntentSafeCode = new Intent(getActivity(), MyZhongHuiQuanActivity.class);
                checKUserState();

                break;

            case R.id.rl_mine_head_login: //????????????????????????????????????????????????????????????
                updateMineUserMessage();
                break;
            case R.id.iv_mine_head: //????????????????????????????????????????????????????????????
                updateMineUserMessage();
                break;

            case R.id.ll_mine_supply_the_phone: //??????????????????
//                DialUtils.callCentre(getContext(), DialUtils.SUPPLY_PHONE);
                break;
            case R.id.ll_mine_service_the_phone: //??????????????????
//                DialUtils.callCentre(getContext(), DialUtils.CENTER_NUM);
                break;
        }
    }

    /**
     * ???????????????????????????????????????
     */
    private void checKUserState() {
        /**
         * ????????????
         */

        if (!NetStateUtils.isNetworkAvailable(getContext())) {
            ToastUtils.showShort(getString(R.string.xn_toast_nointernet));
        } else {
            if (!CurrentUserManager.isLoginUser()) {
                startActivity(LoginActivity.class);
            } else {
                startActivity(mIntentSafeCode);
            }
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        //?????????????????????????????????
        super.onItemClick(o, position);
        switch (position) {
            case 0: //
//                DialUtils.callCentre(getActivity(), DialUtils.CENTER_NUM1);

                break;
            case 1: //
//                DialUtils.callCentre(getActivity(), DialUtils.CENTER_NUM2);

                break;
            default:
                return;
        }

    }

    interface TicketReadmeCallback {
        void agree();
    }
}
