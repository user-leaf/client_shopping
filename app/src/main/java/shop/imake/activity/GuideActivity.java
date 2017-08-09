package shop.imake.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import shop.imake.MainActivity;
import shop.imake.R;
import shop.imake.utils.ScreenUtils;

public class GuideActivity extends BaseActivity {
    private List<ImageView> mList;
    private ViewPager mPager;
    private PagerAdapter mAdapter;
    private Button mButton;
    private LinearLayout mLinearLayoutView;
    //当前选中页
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
        setupView();
        initCtrl();

    }

    private void initView() {
        mList = new ArrayList<>();

        ImageView imageView1 = new ImageView(this);
        ImageView imageView2 = new ImageView(this);
        ImageView imageView3 = new ImageView(this);

        imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView3.setScaleType(ImageView.ScaleType.FIT_XY);

        // OOM 图片也不太大...
//        imageView1.setImageResource(R.drawable.guide1);
//        imageView2.setImageResource(R.drawable.guide2);
//        imageView3.setImageResource(R.drawable.guide3);

        imageView1.setImageBitmap(readBitmap(this, R.drawable.guide01));
        imageView2.setImageBitmap(readBitmap(this, R.drawable.guide02));
        imageView3.setImageBitmap(readBitmap(this, R.drawable.guide03));

//        Glide.with(this).load(R.drawable.guide1).dontAnimate().into(imageView1);
//        Glide.with(this).load(R.drawable.guide2).dontAnimate().into(imageView2);
//        Glide.with(this).load(R.drawable.guide3).dontAnimate().into(imageView3);

        mList.add(imageView1);
        mList.add(imageView2);
        mList.add(imageView3);

        mPager = (ViewPager) findViewById(R.id.viewpager);

        mButton = (Button) findViewById(R.id.button);
        mButton.setClickable(false);

        mLinearLayoutView = (LinearLayout) findViewById(R.id.ll_indicator);
        mLinearLayoutView.getChildAt(0).setSelected(true);

    }

    private void setupView() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startApp();//切换界面

            }
        });

        //设置ViewPager的滑动监听,为了滑动到最后一页,继续滑动实现页面的跳转
        mPager.setOnTouchListener(new View.OnTouchListener() {
            float startX;

            float endX;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();

                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        //获取屏幕的宽度
                        int width = ScreenUtils.getScreenWidth(getApplicationContext());
                        //根据滑动的距离来切换界面
                        if (currentPage == 2 && startX - endX >= (width / 5)) {

                            startApp();//切换界面
                        }

                        break;
                }
                return false;
            }
        });

    }

    private Bitmap readBitmap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    /**
     * 进入主页
     */
    private void startApp() {
        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void initCtrl() {
        mAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mList.get(position));
                return mList.get(position);

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
                container.removeView(mList.get(position));
            }
        };

        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                // 对“大小点”进行设置
//                for (int i = 0; i < mLinearLayoutView.getChildCount(); i++) {
//                    mLinearLayoutView.getChildAt(i).setSelected(false);
//                }
//                mLinearLayoutView.getChildAt(position).setSelected(true);
                currentPage = position;

                if (position < mList.size() - 1) {
                    mButton.setClickable(false);
                } else {
                    mButton.setClickable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
