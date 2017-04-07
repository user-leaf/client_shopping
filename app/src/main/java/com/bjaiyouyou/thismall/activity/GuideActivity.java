package com.bjaiyouyou.thismall.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bjaiyouyou.thismall.MainActivity;
import com.bjaiyouyou.thismall.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {
    private List<ImageView> mList;
    private ViewPager mPager;
    private PagerAdapter mAdapter;
    private Button mButton;
    private LinearLayout mLinearLayoutView;

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

        Glide.with(this).load(R.drawable.guide1).dontAnimate().into(imageView1);
        Glide.with(this).load(R.drawable.guide2).dontAnimate().into(imageView2);
        Glide.with(this).load(R.drawable.guide3).dontAnimate().into(imageView3);

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
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


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
                // 对“大小点”进行设置
                for (int i = 0; i < mLinearLayoutView.getChildCount(); i++) {
                    mLinearLayoutView.getChildAt(i).setSelected(false);
                }
                mLinearLayoutView.getChildAt(position).setSelected(true);
                if (position < 2) {
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
