package com.bjaiyouyou.thismall.picker;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * GlideLoader
 * Created by Yancy on 2015/12/6.
 */
public class GlideLoader implements com.yancy.imageselector.ImageLoader {

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                .centerCrop()
                .into(imageView);
    }

}
