/*
Copyright 2015 shizhefei（LuckyJayce）
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
   http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.bjaiyouyou.thismall.utils;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bjaiyouyou.thismall.R;
import com.bjaiyouyou.thismall.utils.vary.IVaryViewHelper;
import com.bjaiyouyou.thismall.utils.vary.VaryViewHelper;

/**
 * 自定义要切换的布局，通过IVaryViewHelper实现真正的切换<br>
 * 使用者可以根据自己的需求，使用自己定义的布局样式
 * 
 * @author LuckyJayce
 *
 */
public class LoadViewHelper {

	private IVaryViewHelper helper;

	public LoadViewHelper(View view) {
		this(new VaryViewHelper(view));
	}

	public LoadViewHelper(IVaryViewHelper helper) {
		super();
		this.helper = helper;
	}

	public void showError(final OnClickListener onClickListener) {
		View layout = helper.inflate(R.layout.layout_view_helper_load_error);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LoadViewHelper.this.showLoading();
				onClickListener.onClick(v);
			}
		});
		helper.showLayout(layout);
	}

	public void showEmpty(String errorText, String buttonText, OnClickListener onClickListener) {
//		View layout = helper.inflate(R.layout.load_empty);
//		TextView textView = (TextView) layout.findViewById(R.id.textView1);
//		textView.setText(errorText);
//		Button button = (Button) layout.findViewById(R.id.button1);
//		button.setText(buttonText);
//		button.setOnClickListener(onClickListener);
//		helper.showLayout(layout);
	}

	/**
	 * 显示加载状态
	 */
	public void showLoading(String loadText) {
		View layout = helper.inflate(R.layout.layout_view_helper_loading);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setText(loadText);
		helper.showLayout(layout);
	}

	/**
	 * 显示加载状态--默认提示语
	 */
	public void showLoading() {
		showLoading("加载中...");
	}

	public void restore() {
		helper.restoreView();
	}
}