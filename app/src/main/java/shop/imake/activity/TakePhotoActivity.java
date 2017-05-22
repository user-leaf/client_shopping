package shop.imake.activity;


import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import shop.imake.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * user:Alice
 * 2016/5/29
 * 拍照页面
 */
public class TakePhotoActivity extends BaseActivity implements View.OnClickListener,SurfaceHolder.Callback,Camera.PictureCallback{
    private static final int REQUEST_CODE_PICK_IMAGE = 110;
    @ViewInject(R.id.surfaceView_takePhoto)
    private SurfaceView mSurfaceView;
//    @ViewInject(R.id.bt_takePhoto)
//    private ImageView ivPhoto ;
    //拍照按钮
    @ViewInject(R.id.bt_takePhoto)
    private Button btTackPhoto;
    //相册入口
    @ViewInject(R.id.bt_getPhoto)
    private Button btGetPhoto;
    //返回
    @ViewInject(R.id.bt_takePhotoBack)
    private Button btBack;
    //拍照历史入口
    @ViewInject(R.id.bt_takePhotoHistory)
    private Button btPhotoHistory;
    //重新拍照
    @ViewInject(R.id.bt_Retake)
    private TextView tvRetake;
    //保存图片
    @ViewInject(R.id.bt_takePhotoSure)
    private TextView tvSure;



    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Bitmap saveBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tack_photo);
        initView();
    }
    private void initView(){
        ViewUtils.inject(this);
        //拍照按钮设置监听
        btTackPhoto.setOnClickListener(this);
        btGetPhoto.setOnClickListener(this);
        mSurfaceView.setFocusable(true);
        mSurfaceView.setFocusableInTouchMode(true);
        mSurfaceView.setClickable(true);
//        mSurfaceView.setOnClickListener(this);
        mSurfaceHolder=mSurfaceView.getHolder();
        //设置该SurfaceView是一个"推送"类型的SurfaceView
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
        btBack.setOnClickListener(this);
        btPhotoHistory.setOnClickListener(this);
        tvRetake.setOnClickListener(this);
        tvSure.setOnClickListener(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mCamera=Camera.open();
        mCamera.setDisplayOrientation(90);
        try {
            //将视图内容  展示到  surface上
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //启动预览
        mCamera.startPreview();
    }
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        mCamera.startPreview();
        //获取照相机的参数
        Camera.Parameters  parameters =  mCamera.getParameters();
        //设置图片质量
        parameters.setJpegQuality(100);
        //设置预览图片的大小
        parameters.setPreviewSize(width,height);

    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    //处理SurfaceView的点击事件
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_takePhoto://拍照
                mCamera.takePicture(null,null,this);
                break;
            case R.id.bt_getPhoto://获取相册照片
               getPicture();
                break;
            case R.id.bt_takePhotoBack:
               finish();
                break;
            case R.id.bt_takePhotoHistory:
                Toast.makeText(getApplicationContext(),"拍照历史",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_Retake://重拍
                mCamera.startPreview();

                break;
            case R.id.bt_takePhotoSure://确定存储
               if (saveBitmap!=null){
                   saveBitmap(saveBitmap);
               }

                break;
            case R.id.surfaceView_takePhoto:
                mCamera.takePicture(null,null,this);//surfaceView拍照
                break;

        }
    }

    /**
     *
     * @param bitmap
     * 将图片保存到相册
     */

    private void saveBitmap(Bitmap bitmap) {
        //图片保存在相册返回图片的路径
        //将图片保存至相册
        ContentResolver resolver = getContentResolver();
        String imgURl=MediaStore.Images.Media.insertImage(resolver, bitmap, "t", "des");
        /**
         * Retrieves an image for the given url as a {@link Bitmap}.
         *
         * //获得系统相册照片的方法
         *MediaStore.Images.Media.getBitmap(resolver, Uri.parse(imgURl));
         *
         * @param cr The content resolver to use
         * @param url The url of the image
         * @throws FileNotFoundException
         * @throws IOException
         */

        Log.e("img","--imgURl----"+imgURl);
        //重新打开预览图
        mCamera.startPreview();
    }


    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        //将图片旋转正过来
        Matrix matrix=new Matrix();
        // 缩放原图
        matrix.postScale(1f, 1f);
        // 向左旋转45度，参数为正则向右旋转
        matrix.postRotate(90);
        saveBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(), bitmap.getHeight(),matrix,true);

        /**
         * Insert an image and create a thumbnail for it.
         *
         * @param cr The content resolver to use
         * @param source The stream to use for the image
         * @param title The name of the image
         * @param description The description of the image
         * @return The URL to the newly created image, or <code>null</code> if the image failed to be stored
         *              for any reason.
         */

//        //拍照后重新开始预览
//        camera.startPreview();
    }




    //获得相册照片
    private void getPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");  // 开启Pictures画面Type设定为image
        intent.setAction(Intent.ACTION_GET_CONTENT); //使用Intent.ACTION_GET_CONTENT这个Action
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        处理本地相册返回的图片
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            ContentResolver resolver = getContentResolver();
            //照片的原始资源地址
            Uri originalUri = data.getData();
//            ivPhoto.setImageURI(originalUri);   //在界面上显示图片
            try {
                //使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
//                ivPhoto.setImageBitmap(photo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
