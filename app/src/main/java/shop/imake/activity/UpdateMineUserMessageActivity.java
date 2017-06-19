package shop.imake.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;
import shop.imake.R;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4ClientOther;
import shop.imake.client.ClientAPI;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.User;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.LogUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.utils.UNNetWorkUtils;
import shop.imake.utils.ValidatorsUtils;
import shop.imake.widget.IUUTitleBar;

/**
 * 修改个人用户信息页面
 *
 * @author Alice
 *         Creare 2016/8/24 18:11
 */
public class UpdateMineUserMessageActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, OnItemClickListener {

    //获取相册照片的请求码
    private static final int PHOTO_REQUEST_GALLERY = 111;
    //    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private static final int PHOTO_REQUEST_CAMERA = 112;
    private static final int PHOTO_REQUEST_CUT = 113;

    private File tempFile;
    private Bitmap bitmap;
    //上传进度
    private ProgressDialog pd;


    private IUUTitleBar mTitleBar;
    //用户对象，由上一页传过来
    private User mUser;
    //头像
    private ImageView mIvUserImg;
    //昵称
    private EditText mEtName;
    //验证码
    private EditText mEtVaildateNum;
    //电话
    private EditText mEtPhone;
    //获得验证码的按钮
    private TextView mTvGetVaildateNum;
    //邮箱
    private EditText mEtEmail;
    //安全码
    private EditText mEtSafeCode;
    //信息提交
    private TextView mTvSubmit;
    //计时器，用于倒数发送验证
    private Timer mPhoneTimer;
    //倒计时线程
    private TimerTask mPhoneTimeTask;

    //处理倒计时结束事件
    private Handler mHandler = new Handler();
    private String mName;
    private String mPnone;
    private String mVerification;
    private String mEmail;
    private String mSafeCode;
    private String mImgUrl;

    //安全码提交条件限制
    private int mMinSafeleght = 8;
    private int mMaxSafeleght = 16;
    private boolean isSafeChange = false;
    private boolean isNameChange = false;
    private boolean isPhoneChange = false;
    private boolean isVerificationChange = false;
    private boolean isEmailChange = false;
    //电话号码提交条件限制
    private int mMaxPhone = 11;
    private long mMaxTimePhone = 60;
    //短息发送倒计时
    private long mTimePhone = 60;

    private int mVerificationLimit = 4;
    private HashMap<String, String> parameterMap;
    //获得头像的对话框
    private AlertView mGetUserImgAlertView;
    //用户头像
    private CircleImageView mUserImg;

    //头像是否更改
    private boolean isUserImgChange = false;

    private Uri uri;
    //头像名称
    private String mImgName;
    //会员信息
    private User.MemberBean mBean;
    //用户头像全路径
    private String mUserImgUrl;

    public static final String TAG = UpdateMineUserMessageActivity.class.getSimpleName();

    private Api4ClientOther mclient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mine_user_message);
        mUser = (User) getIntent().getSerializableExtra("mUser");
        initVariables();
        initView();
        setView();
        initCtrl();

    }

    /**
     * 初始化变量
     */
    private void initVariables() {
        mclient = (Api4ClientOther) ClientApiHelper.getInstance().getClientApi(Api4ClientOther.class);
    }

    private void initView() {
        mTitleBar = ((IUUTitleBar) findViewById(R.id.title_update_mine_user_message));
        mIvUserImg = ((ImageView) findViewById(R.id.iv_update_user_img));
        mEtName = ((EditText) findViewById(R.id.et_update_user_name));
        mEtPhone = ((EditText) findViewById(R.id.et_update_user_phone));
        mEtVaildateNum = ((EditText) findViewById(R.id.et_update_user_validate_num));
        mTvGetVaildateNum = ((TextView) findViewById(R.id.tv_update_user_get_validate_num));
        mEtEmail = ((EditText) findViewById(R.id.et_update_user_email));
        mEtSafeCode = ((EditText) findViewById(R.id.et_update_user_safe_code));
        mTvSubmit = ((TextView) findViewById(R.id.tv_update_user_submit));
        mUserImg = ((CircleImageView) findViewById(R.id.iv_update_user_img));


        mGetUserImgAlertView = new AlertView(null, null, "取消", null, new String[]{"拍照", "我的相册"}, this, AlertView.Style.ActionSheet, this);

    }

    private void setView() {
        mTitleBar.setLeftLayoutClickListener(this);
        mTvGetVaildateNum.setOnClickListener(this);
        mTvSubmit.setOnClickListener(this);
        mIvUserImg.setOnClickListener(this);

        //昵称
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString().trim();
                //判断并标识,昵称修改是否符合标准 不为空，与原来不一样
//                if (!TextUtils.isEmpty(name)){
//                    mName=name;
//                    isNameChange=true;
//                }else {
//                    isNameChange=false;
//                }
                if (TextUtils.isEmpty(mName)) {
                    if (!TextUtils.isEmpty(name)) {
                        mName = name;
                        isNameChange = true;
                    } else {
                        isNameChange = false;
                    }
                } else {
                    if (mName.equals(name) || TextUtils.isEmpty(name)) {
                        isNameChange = false;
                    } else {
                        mName = name;
                        isNameChange = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //账号，手机号
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = s.toString().trim();
                //判断并标识,手机修改是否符合标准，11位与原来不同
                if (phone.length() == mMaxPhone && !mPnone.equals(phone)) {
                    isPhoneChange = true;
                    mPnone = phone;
                } else {
                    isPhoneChange = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //验证码
        mEtVaildateNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String verification = s.toString().trim();
                //电话号码没修改，验证码不可输入
                if (!isPhoneChange) {
                    //引起死循环判断，注释掉解决
//                    mEtVaildateNum.setText("");
                    isVerificationChange = false;
                    mEtVaildateNum.setEnabled(false);
                } else {
                    mEtVaildateNum.setEnabled(true);
                    //判断并标识验证码修改是否符合标准
                    if (verification.length() == mVerificationLimit) {
                        isVerificationChange = true;
                        mVerification = verification;
                    } else {
                        isVerificationChange = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //邮箱
        mEtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString().trim();
                //判断并标识,邮箱修改是否符合标准 包含@符号
                mEmail = email;
                if (email.contains("@")) {
                    isEmailChange = true;
                } else {
                    isEmailChange = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //安全码
        mEtSafeCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String safeCode = s.toString().trim();
                //判断并标识,安全码修改是否符合标准 8-16位
                if (mMinSafeleght <= safeCode.length() && safeCode.length() <= mMaxSafeleght) {
                    isSafeChange = true;
                    mSafeCode = safeCode;
                } else {
                    isSafeChange = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initCtrl() {
        //重新设置头像为默认头像
//        mUserImg.setImageResource(R.mipmap.list_profile_photo);
        if (mUser != null) {
            mBean = mUser.getMember();
            if (mBean != null) {
                mImgUrl = mBean.getAvatar_path();
                mImgName = mBean.getAvatar_name();
                if (!TextUtils.isEmpty(mImgUrl) && !TextUtils.isEmpty(mImgName)) {
                    mUserImgUrl = mImgUrl + "/" + mImgName;
                    LogUtils.e("UserImgUrl", mUserImgUrl);
                    Glide.with(getApplicationContext())
                            .load(mUserImgUrl)
                            // 重点在这行,加上后不再加载图片
//                        .signature(new StringSignature(UUID.randomUUID().toString()))
                            //不注释掉清除缓存失效
//                            .placeholder(R.mipmap.list_profile_photo)
//                            .error(R.mipmap.list_profile_photo)
                            //去掉缓存功能
                            .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                            .skipMemoryCache(true)//跳过内存缓存
                            .into(mIvUserImg);
                } else {
                    mUserImg.setImageResource(R.mipmap.list_profile_photo);
                }

                //昵称
                mName = mBean.getNick_name();
//        mEtName.setText(mName);
                mEtName.setHint(mName);
//        mEtName.setSelection(mName.length());

                mPnone = mBean.getPhone();
//        mEtPhone.setText(mPnone);
                mPnone = mPnone.substring(0, 3) + "****" + mPnone.substring(7, mPnone.length());
                mEtPhone.setHint(mPnone);
//        mEtPhone.setSelection(mPnone.length());

                mEmail = mBean.getEmail();
                if (!TextUtils.isEmpty(mEmail)) {
//            mEtEmail.setText(mEmail);
                    mEtEmail.setHint(mEmail);
//            mEtEmail.setSelection(mEmail.length());

//                    mEtSafeCode.setEnabled(true);
                } else {
                    //未设置邮箱不能修改安全码
//                    mEtSafeCode.setEnabled(false);
                }
                mSafeCode = mBean.getSecurity_code_hint();
                if (!TextUtils.isEmpty(mSafeCode)) {
                    mSafeCode = mSafeCode + "********";
//            mEtSafeCode.setText(mSafeCode);
                    mEtSafeCode.setHint(mSafeCode);
//            mEtSafeCode.setSelection(mSafeCode.length());
                }
            }

        }

    }


    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_STORAGE_PERM = 124;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_layout:
                finish();
                break;

            case R.id.tv_update_user_get_validate_num:
                //定时获取验证码
                getVerification();
                break;

            case R.id.tv_update_user_submit:
                //向服务器提交信息
                //头像上传
                upload();
//                submitToServer();
//                finish();
                break;

            case R.id.iv_update_user_img://修改头像
                mGetUserImgAlertView.show();
                break;

        }
    }


    /**
     * 获取验证码
     */
    public void getVerification() {
        mPnone = mEtPhone.getText().toString().trim();
        if (mPnone.length() == mMaxPhone) {
            //网络获取验证码
            mclient.getUpdateUserVerification(TAG, mPnone, new DataCallback<String>(getApplicationContext()) {
                @Override
                public void onFail(Call call, Exception e, int id) {
                    LogUtils.e("getUpdateUserVerification", "--------------" + e.toString());
                    UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
                }

                @Override
                public void onSuccess(Object response, int id) {
                    Toast.makeText(getApplicationContext(), "验证码已经发送到您的手机上请注意查收", Toast.LENGTH_SHORT).show();
                    initTimer();
                    mPhoneTimer.schedule(mPhoneTimeTask, 0L, 1000); //延时1000ms后执行，1000ms执行一次

                }
            });


//            ClientAPI.getUpdateUserVerification(mPnone, new StringCallback() {
//                @Override
//                public void onError(Call call, Exception e, int id) {
//                    LogUtils.e("getUpdateUserVerification", "--------------" + e.toString());
//                    UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
//                }
//
//                @Override
//                public void onResponse(String response, int id) {
//                    Toast.makeText(getApplicationContext(), "验证码已经发送到您的手机上请注意查收", Toast.LENGTH_SHORT).show();
//                    initTimer();
//                    mPhoneTimer.schedule(mPhoneTimeTask, 0L, 1000); //延时1000ms后执行，1000ms执行一次
//                }
//            });

        } else {
            Toast.makeText(getApplicationContext(), "请输入正确的11位手机号", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 创建计时器，实现验证码获取倒计时
     */
    private void initTimer() {
        mPhoneTimer = new Timer(true);
        mPhoneTimeTask = new TimerTask() {
            public void run() {
                LogUtils.e("time", "mTimeDifference--" + mTimePhone);
                mHandler.post(new Runnable() {
                    //处理倒计时更新页面
                    @Override
                    public void run() {
                        if (mTimePhone >= 0) {
                            mTvGetVaildateNum.setEnabled(false);
                            mTvGetVaildateNum.setText(mTimePhone + "秒");
                            --mTimePhone;
                        } else {
                            mTvGetVaildateNum.setEnabled(true);
                            mTvGetVaildateNum.setText("获取验证码");
                            mTimePhone = mMaxTimePhone;
                            mPhoneTimer.cancel();
                        }
                    }
                });
            }
        };
    }


    /**
     * 向服务器提交数据
     */
    private void submitToServer() {
        String token = CurrentUserManager.getUserToken();
        if (!TextUtils.isEmpty(token)) {
            parameterMap = new HashMap<>();
            LogUtils.e("mName", mName);
            if (isNameChange) {
                parameterMap.put("nick_name", mName);
            }
            if (isPhoneChange && isVerificationChange) {
                parameterMap.put("phone", mPnone);
                parameterMap.put("verification", mVerification);
            }
            if (isEmailChange) {
                parameterMap.put("email", mEmail);
            } else {
                if (!mEmail.isEmpty()) {
                    if (!ValidatorsUtils.isEmail(mEmail)) {
                        Toast.makeText(getApplicationContext(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            if (isSafeChange) {
                parameterMap.put("security_code", mSafeCode);
            }
            //参数有变再进行提交，进行网络请求
            if (parameterMap.size() != 0) {
                LogUtils.e("parameterMap-lenght--", parameterMap.size() + "");
                LogUtils.e("parameterMap---", parameterMap.keySet().toString() + "");
                ClientAPI.updateUserMessage(parameterMap, token, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        CurrentUserManager.TokenDue(e);
                        LogUtils.e("net error", e.toString());
                        UNNetWorkUtils.unNetWorkOnlyNotify(getApplicationContext(), e);
                        return;
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


            } else {
                if (!isUserImgChange) {
                    Toast.makeText(getApplicationContext(), "没能为您进行任何修改", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    finish();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "用户未登录不能进行修改", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
        if (requestCode == RC_CAMERA_PERM) {
            //获得相机权限可以拍照
            camera();
        } else if (requestCode == RC_STORAGE_PERM) {
            //获得相册的访问权限可获取
            gallery();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
    }


    /**
     * 头像图片获取方式的选择
     *
     * @param o
     * @param position
     */
    @Override
    public void onItemClick(Object o, int position) {
        switch (position) {
            case 0://拍照
                /**
                 * Android 6.0 权限检查
                 * https://github.com/googlesamples/easypermissions
                 */
                String[] perms1 = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(this, perms1)) {
//                    ToastUtils.showShort("拍照");
                    camera();

                } else {
                    EasyPermissions.requestPermissions(this, "需要开启一些权限",
                            RC_CAMERA_PERM, perms1);
                }
                break;

            case 1://我的相册
                String[] perms2 = {Manifest.permission.READ_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(this, perms2)) {
//                    ToastUtils.showShort("我的相册");
                    gallery();
                } else {
                    EasyPermissions.requestPermissions(this, "需要开启一些权限",
                            RC_STORAGE_PERM, perms2);
                }
                break;

        }
    }

    /**
     * 获取相册照片
     */
    public void gallery() {
        //获取相册照片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /**
     * 拍照获取图片
     */
    public void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), PHOTO_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    /**
     * 判断内存卡是否存在
     *
     * @return
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    private void crop(Uri uri) {
        //
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);// 输出是X方向的比例
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高，切忌不要再改动下列数字，会卡死
        intent.putExtra("outputX", 250);// 输出X方向的像素
        intent.putExtra("outputY", 250);
        //
        intent.putExtra("outputFormat", "JPEG");
        //
        intent.putExtra("noFaceDetection", true);
        // 设置为不返回数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 上传头像
     */
    private final static String USERIMG_PATH = Environment.getExternalStorageDirectory() + "/userImg/";

    public void upload() {
        if (bitmap != null) {
            pd = ProgressDialog.show(this, null, "头像上传中...");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            try {
                out.flush();
                out.close();
                byte[] buffer = out.toByteArray();
                byte[] encode = Base64.encode(buffer, Base64.DEFAULT);

                //将byte[]转换成File
                BufferedOutputStream stream = null;
                File dirFile = new File(USERIMG_PATH);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }
                File file = null;
                try {
                    file = new File(USERIMG_PATH + "userImg.jpg");
                    FileOutputStream fstream = new FileOutputStream(file);
                    stream = new BufferedOutputStream(fstream);
                    stream.write(buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                String photo = new String(encode);
                //调接口上传头像
                isUserImgChange = false;
                ClientAPI.postUserImg(file, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        CurrentUserManager.TokenDue(e);
                        ToastUtils.exceptionToast(e, getApplicationContext());
                        LogUtils.e("postUserImg", e.getMessage() + "");
                        //提交全部数据
                        submitToServer();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        isUserImgChange = true;
                        ToastUtils.showShort("图片上传成功");
                        //提交全部数据
                        submitToServer();
                    }
                });
                pd.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //提交全部数据
            submitToServer();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA && resultCode == RESULT_OK) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                uri = Uri.fromFile(tempFile);
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                bitmap = data.getParcelableExtra("data");
                this.mIvUserImg.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
