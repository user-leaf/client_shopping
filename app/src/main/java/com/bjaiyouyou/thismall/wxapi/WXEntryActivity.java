/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.bjaiyouyou.thismall.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bjaiyouyou.thismall.Constants;
import com.bjaiyouyou.thismall.activity.WithdrawActivity;
import com.bjaiyouyou.thismall.utils.LogUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;
	private boolean isGetOpenId=false;
	private String openID;
	private Intent intent;

	/**
	 * 处理微信发出的向第三方应用请求app message
	 * <p>
	 * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
	 * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
	 * 做点其他的事情，包括根本不打开任何页面
	 */
	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
		startActivity(iLaunchMyself);
	}

	/**
	 * 处理微信向第三方应用发起的消息
	 * <p>
	 * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
	 * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
	 * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
	 * 回调。
	 * <p>
	 * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
	 */
	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null
				&& (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}

//	/////////////////////////获得OpenId///////////////////////////////
//	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getOpenId();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		getOpenId();
	}

	private void getOpenId() {
		//一、注册微信
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.handleIntent(getIntent(),this);
		// 将该app注册到微信
		//二、发送请求
		api.registerApp(Constants.APP_ID);
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
//		req.transaction=Constants.APP_ID;
		req.state = "wechat_sdk_demo_test";
		api.sendReq(req);
	}
	@Override
	public void onReq(BaseReq baseReq) {

	}

	/**
	 * 接受微信请求(获取code值)
	 * @param resp
     */
	@Override
	public void onResp(BaseResp resp) {
		String  result = "";
		SendAuth.Resp re = ((SendAuth.Resp) resp);
		String code = re.code;

		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result ="成功";
				getOpenID(code);
//				finish();
//				android.os.Process.killProcess(android.os.Process.myPid());
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result ="用户取消";
				Toast.makeText(getApplicationContext(), "取消微信验证，不能继续提现", Toast.LENGTH_SHORT).show();
				this.finish();
//				android.os.Process.killProcess(android.os.Process.myPid());
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "拒绝";
				Toast.makeText(getApplicationContext(), "微信拒绝验证，不能继续提现", Toast.LENGTH_SHORT).show();
				this.finish();
//				android.os.Process.killProcess(android.os.Process.myPid());
				break;
			default:
				result = "未知错误";
				Toast.makeText(getApplicationContext(), "微信未知错误，不能继续提现", Toast.LENGTH_SHORT).show();
				this.finish();
//				android.os.Process.killProcess(android.os.Process.myPid());
				break;
		}
		LogUtils.e("result:",""+result);
		LogUtils.e("code:",""+code);
//		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//		Toast.makeText(this, code, Toast.LENGTH_LONG).show();

	}

	/**
	 * 通过code获取access_token，code等数据
	 * @param code
     */
	private  void   getOpenID(String code) {
		// APP_ID和APP_Secret在微信开发平台添加应用的时候会生成，grant_type 用默认的"authorization_code"即可.
		String urlStr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ Constants.APP_ID+"&secret="+ Constants.APP_Secret+
				"&code="+code+"&grant_type=authorization_code";

		HttpUtils http = new HttpUtils();
		// 设置超时时间
		//        http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpRequest.HttpMethod.GET, urlStr, null,
				new RequestCallBack<String>() {
					// 接口回调
					@Override
					public void onSuccess(ResponseInfo<String> info) {
						System.out.println("返回的json字符串：" + info.result);
//						Toast.makeText(getApplicationContext(), info.result, Toast.LENGTH_SHORT).show();
						JSONObject obj = null;
						try {
							obj = new JSONObject(info.result);
							//toast  OpenID
//							Toast.makeText(getApplicationContext(), obj.getString("openid"), Toast.LENGTH_LONG).show();
							openID=obj.getString("openid");
							isGetOpenId=true;
							finshThis();
//							WXEntryActivity.this.finish();


						} catch (JSONException e) {
							LogUtils.e("异常",e.toString());
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {

					}
				});
	}

	private void finshThis(){
		if (isGetOpenId){
			LogUtils.e("openid:",""+openID);
			LogUtils.e("openid:","向回传值");
			Intent intentOpen=new Intent(this, WithdrawActivity.class);
//			intentOpen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intentOpen.putExtra("openid",openID);
			startActivity(intentOpen); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
			this.finish();//此处一定要调用finish()方法
//			android.os.Process.killProcess(android.os.Process.myPid());
			LogUtils.e("openid:","关闭完成");
			LogUtils.e("open","吊起支付");
		}
	}

}
