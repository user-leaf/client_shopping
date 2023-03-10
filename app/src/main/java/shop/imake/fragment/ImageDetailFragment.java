package shop.imake.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import shop.imake.R;
import shop.imake.utils.ImageUtils;
import shop.imake.utils.ScreenUtils;
import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 单张图片显示Fragment
 *
 * User: JackB
 * Date: 2016/10/25
 */
public class ImageDetailFragment extends BaseFragment {

    private String mImageUrl;
    private PhotoView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_image_detail, container, false);
        mImageView = (PhotoView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }

            @Override
            public void onOutsidePhotoTap() {
                getActivity().finish();
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Glide.with(getContext()).load(ImageUtils.getThumb(mImageUrl, ScreenUtils.getScreenWidth(getContext()),0)).into(mImageView);
        // ??
//        mAttacher.update();

//        ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                String message = null;
//                switch (failReason.getType()) {
//                    case IO_ERROR:
//                        message = "下载错误";
//                        break;
//                    case DECODING_ERROR:
//                        message = "图片无法显示";
//                        break;
//                    case NETWORK_DENIED:
//                        message = "网络有问题，无法下载";
//                        break;
//                    case OUT_OF_MEMORY:
//                        message = "图片太大无法显示";
//                        break;
//                    case UNKNOWN:
//                        message = "未知的错误";
//                        break;
//                }
//                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                progressBar.setVisibility(View.GONE);
//                mAttacher.update();
//            }
//        });
    }
}
