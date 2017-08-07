package shop.imake.zxing.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Vector;

import okhttp3.Call;
import shop.imake.Constants;
import shop.imake.R;
import shop.imake.activity.PermissionsActivity;
import shop.imake.activity.ScanGoodsDetailActivity;
import shop.imake.activity.ScanPayActivity;
import shop.imake.callback.DataCallback;
import shop.imake.client.Api4Home;
import shop.imake.client.ClientApiHelper;
import shop.imake.model.PermissionsChecker;
import shop.imake.model.ScanPayQRCodeModel;
import shop.imake.model.ShopModel;
import shop.imake.user.CurrentUserManager;
import shop.imake.utils.Base64;
import shop.imake.utils.LogUtils;
import shop.imake.utils.ToastUtils;
import shop.imake.zxing.camera.CameraManager;
import shop.imake.zxing.decoding.CaptureActivityHandler;
import shop.imake.zxing.decoding.InactivityTimer;
import shop.imake.zxing.utils.MyUtils;
import shop.imake.zxing.utils.RGBLuminanceSource;
import shop.imake.zxing.view.ViewfinderView;

import static shop.imake.utils.LogUtils.e;


/**
 * @author Alice
 *         Creare 2016/6/5 13:41
 */
public class CaptureActivity extends Activity implements Callback, View.OnClickListener {

    private static final int REQUEST_CODE = 500;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private String photo_path;
    private Bitmap scanBitmap;
    private ImageView btGetPhoto;
    private ImageView btScanCode;
    private ImageView btScanHistory;
    //private Button cancelScanButton;
    private Api4Home mApi4Home;

    //////////////////////授权变量// 所需的全部权限


    static final String[] PERMISSIONS = new String[]{
//			Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
//			Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//			Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
//			Manifest.permission.VIBRATE,
//			Manifest.permission.INTERNET
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        //初始化权限检查器
        mPermissionsChecker = new PermissionsChecker(this);
        mApi4Home = (Api4Home) ClientApiHelper.getInstance().getClientApi(Api4Home.class);
        infoView();
        setUpView();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //判断权限
        getScanPermission();
    }

    //初始化控价和变量
    private void infoView() {
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        //cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        btGetPhoto = ((ImageView) findViewById(R.id.iv_scanCodePhoto));
        btScanCode = ((ImageView) findViewById(R.id.iv_scanCodeBack));
        btScanHistory = ((ImageView) findViewById(R.id.iv_scanCodeHistory));
    }


    private void setUpView() {
        btGetPhoto.setOnClickListener(this);

        btScanCode.setOnClickListener(this);
        btScanHistory.setOnClickListener(this);

    }

    ///////////////////////////////////////////////////////////////////////////处理按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_scanCodeBack:
                finish();
                break;
            case R.id.iv_scanCodePhoto://扫描相册内的图片
                scanSelfPhone();
                break;
            case R.id.iv_scanCodeHistory://查看扫描历
                Toast.makeText(this, "查看扫描历史", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            /**
             * SURFACE_TYPE_PUSH_BUFFERS表明该Surface不包含原生数据，Surface用到的数据由其他对象提供，
             * 在Camera图像预览中就使用该类型的Surface，有Camera负责提供给预览Surface数据，这样图像预览会比较流畅。
             */
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }


    //////////////////////////////////////直接扫描处理最终的扫描结果////////////////////////////////////////

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */

    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        //FIXME
        if (resultString.equals("")) {
            Toast.makeText(CaptureActivity.this, "扫码失败!", Toast.LENGTH_SHORT).show();
        } else {
//			System.out.println("Result:"+resultString);
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", resultString);
            //成功拿到扫描结果
            e("scan", resultString);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
            //调用扫描结果处理方法
            dealWithScanResult(resultString);
        }
        CaptureActivity.this.finish();
    }

    /**
     * 处理二维码扫描结果
     *
     * @param resultString
     */
    private void dealWithScanResult(String resultString) {
        //对二维码解密
        String resultStringPay = new String(Base64.decode(resultString));
        //将扫描结果传递到其他处理页面


        LogUtils.e("recode", resultString);
        if (CurrentUserManager.isLoginUser()) {
//                                    {"shopId":100,"money":100}
            //扫描的商家付款码
            if (resultStringPay.contains("shopId")) {
                ScanPayQRCodeModel scanPayQRCodeModel = new Gson().fromJson(resultStringPay, ScanPayQRCodeModel.class);
                if (scanPayQRCodeModel != null) {
                    final long shopId = scanPayQRCodeModel.getShopId();
                    final double money = scanPayQRCodeModel.getMoney();

                    // 商户头像、名称
                    mApi4Home.getShopInfo(this, shopId, new DataCallback<ShopModel>(this) {

                        @Override
                        public void onFail(Call call, Exception e, int id) {
                            CurrentUserManager.TokenDue(e);
                            ToastUtils.showException(e);
                        }

                        @Override
                        public void onSuccess(Object response, int id) {
                            if (response == null) {
                                return;
                            }

                            ShopModel shopModel = (ShopModel) response;

                            ScanPayActivity.actionStart(CaptureActivity.this, shopId, money, shopModel);
                        }
                    });
                }
            }else {
                ScanGoodsDetailActivity.actionStart(CaptureActivity.this, resultString);
            }
        }else {
            //商品详情
            ScanGoodsDetailActivity.actionStart(CaptureActivity.this, resultString);
        }

    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            LogUtils.e("相机异常1", ioe.toString());
            return;
        } catch (RuntimeException e) {
            LogUtils.e("相机异常2", e.toString());
            Toast.makeText(getApplicationContext(), "请到设置里检查应用权限设置或硬件完整性", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }


    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }


    ///////////////////////////////////////////////////////////////////////////实现自身相册扫面
    private void scanSelfPhone() {
        //1、获取相册照片
        //在Activity中开启相册
        Intent innerIntent = new Intent(); // "android.intent.action.GET_CONTENT"
        if (Build.VERSION.SDK_INT < 19) {
            innerIntent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
//			innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);//这个方法有可能 报图片地址 空指针；有可能打开的是非相册图片路径；使用下面的方法
            innerIntent.setAction(Intent.ACTION_PICK);
        }

        innerIntent.setType("image/*");

        Intent wrapperIntent = Intent.createChooser(innerIntent, "选择扫码图片");

        CaptureActivity.this.startActivityForResult(wrapperIntent, REQUEST_CODE);
        //选中了照片后返回的数据在onActivityResult方法中获取
    }

    /**
     * 获取扫面结果进行处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */


    /////////////////////////////////////相册扫描结果处理/////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_CODE://打开相册

                    String[] proj = {MediaStore.Images.Media.DATA};
                    // 获取选中图片的路径
                    Cursor cursor = getContentResolver().query(data.getData(),
                            proj, null, null, null);

                    if (cursor != null && cursor.moveToFirst()) {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        photo_path = cursor.getString(column_index);
                        if (photo_path == null) {
                            photo_path = MyUtils.getPath(getApplicationContext(), data.getData());
                            Log.e("123path  Utils", photo_path);
                        }
                        Log.e("123path", photo_path);

                        cursor.close();
                    }

                    new Thread(new Runnable() {

                        /**
                         *
                         */
                        @Override
                        public void run() {

                            Result result = scanningImage(photo_path);
                            // String result = decode(photo_path);
                            if (result == null) {
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(), "请拍摄完整且清晰的扫码图片，此图片未能识别哦", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else {
                                LogUtils.e("scan", "result" + result.toString());

                                // 返回扫描结果
                                String recode = recode(result.getText().toString().trim());

                                if (!TextUtils.isEmpty(recode)) {
                                    Intent data = new Intent();
                                    data.putExtra("result", recode);
                                    setResult(300, data);
                                    //调用扫描结果处理方法
                                    dealWithScanResult(recode);

                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CaptureActivity.this, "扫码失败!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    LogUtils.e("扫码失败", "相册扫描结果为空字符串");

                                }
                                CaptureActivity.this.finish();

                            }
                        }
                    }).start();
                    break;
            }

        }

        ///////////////////////////////获得权限授予处理结果
        scanPermissionsResult(requestCode, resultCode);

    }

    /**
     * 压缩图片
     *
     * @param bitmap   源图片
     * @param width    想要的宽度
     * @param height   想要的高度
     * @param isAdjust 是否自动调整尺寸, true图片就不会拉伸，false严格按照你的尺寸压缩
     * @return Bitmap
     * @author wangyongzheng
     */
    public Bitmap reduce(Bitmap bitmap, int width, int height, boolean isAdjust) {
        // 如果想要的宽度和高度都比源图片小，就不压缩了，直接返回原图
        if (bitmap.getWidth() < width && bitmap.getHeight() < height) {
            return bitmap;
        }
        // 根据想要的尺寸精确计算压缩比例, 方法详解：public BigDecimal divide(BigDecimal divisor, int scale, int roundingMode);
        // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃
        float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();
        float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();
        if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸
            sx = (sx < sy ? sx : sy);
            sy = sx;// 哪个比例小一点，就用哪个比例
        }
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);// 调用api中的方法进行压缩，就大功告成了
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    // TODO: 解析部分图片
    protected Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            LogUtils.e("scan", "图片地址为空");
            return null;
        }
        LogUtils.e("scan", "通过图片地址获取图片--" + path);
        // DecodeHintType 和EncodeHintType
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();

        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 200);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);

        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
//			return reader.decode(bitmap1, hints);
            LogUtils.e("scan", "return not null");
            return reader.decode(bitmap1, hints);

        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.e("scan", "return  null");
        return null;
    }

    private static final long VIBRATE_DURATION = 200L;
    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 中文乱码
     * <p/>
     * 暂时解决大部分的中文乱码 但是还有部分的乱码无法解决 .
     *
     * @return
     */
    private String recode(String str) {
        String formart = "";
        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
                    .canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
                Log.i("1234      ISO8859-1", formart);
            } else {
                formart = str;
                Log.i("1234      stringExtra", str);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formart;
    }


    /////////////////////////////////////////////权限处理///////////////////////////////////////////

    /**
     * 权限判断
     */
    public void getScanPermission() {
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, Constants.CALL_PERMISSIONS_REQUEST_CODE, PERMISSIONS);
    }

    /**
     * 在OnActivityResult()方法中调用
     *
     * @param requestCode
     * @param resultCode
     */
    public void scanPermissionsResult(int requestCode, int resultCode) {
        LogUtils.e("CaptureActivity", "授权结果");
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == Constants.CALL_PERMISSIONS_REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            Toast.makeText(this, "未授权，不能进行扫描", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
