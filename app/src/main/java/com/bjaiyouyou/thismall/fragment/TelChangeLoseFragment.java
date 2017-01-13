package com.bjaiyouyou.thismall.fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.widget.IUUTitleBar;

/**
 * 手机号码丢失页（新）
 *
 * @author kanbin
 * @date 2016/6/20
 */
public class TelChangeLoseFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = TelChangeLoseFragment.class.getSimpleName();

    // 下一步
    private Button mBtnNext;

    public TelChangeLoseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_tel_change_lose, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setupView();
    }

    private void initView() {
        mBtnNext = (Button) layout.findViewById(R.id.tel_changing_lose_btn_next);
    }

    private void setupView() {
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tel_changing_lose_btn_next: // 下一步
                TelChangingFragment telChangingFragment = new TelChangingFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.tel_change_new_container,telChangingFragment,TelChangingFragment.TAG);
                transaction.commit();
                // 更改标题
                IUUTitleBar titleBar = (IUUTitleBar) getActivity().findViewById(R.id.tel_change_new_title_bar);
                titleBar.setTitle("新手机号");
                break;
        }
    }
}
