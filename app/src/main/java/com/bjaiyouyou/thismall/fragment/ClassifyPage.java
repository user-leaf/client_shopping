package com.bjaiyouyou.thismall.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjaiyouyou.thismall.R;

/**
 * 分类页
 *
 * Created by kanbin on 2017/3/28.
 */
public class ClassifyPage extends BaseFragment {
    public static final String TAG = ClassifyPage.class.getSimpleName();

    protected View layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_classify, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setupView();
        initCtrl();
        loadData();
    }

    private void initView() {

    }

    private void setupView() {

    }

    private void initCtrl() {

    }

    private void loadData() {

    }
}
