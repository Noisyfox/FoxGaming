package org.foxteam.noisyfox.THEngine;

import com.imax.vmall.sdk.android.oauthv2.OAuth2;
import com.imax.vmall.sdk.android.oauthv2.OAuth2Listener;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.app.Activity;
import android.content.Intent;

public class AuthActivity extends Activity {

	private static final String TAG = "AuthActivity";
	public WebView webView;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// APP在第三方平台注册应用时分配的参数
		String ep_id = getIntent().getStringExtra("ep_id");
		String app_key = getIntent().getStringExtra("app_key");
		String app_secret = getIntent().getStringExtra("app_secret");
		String redirect_url = getIntent().getStringExtra("redirect_url");
		final OAuth2 oauth = OAuth2.create(ep_id, app_key, app_secret,
				redirect_url);

		// APP控制WebView的布局和外观
		setContentView(R.layout.activity_auth);
		webView = (WebView) findViewById(R.id.webview);

		// OAuth 2.0 鉴权
		oauth.authorize(webView, new OAuth2Listener() {
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

				Log.v(TAG, "onCancel");
				// 关闭WebView控件
				webView.destroy();
				// 通知主线程用户取消了授权
				Intent it = new Intent();
				it.putExtra("auth_state", "cancel");
				setResult(RESULT_OK, it);
				// 返回MainActivity
				finish();
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				Log.v(TAG, "onComplete");
				String access_token = oauth.getToken().getAccessToken();
				long expires_in = oauth.getToken().getExpiresIn();
				String uid = oauth.getUid();
				Log.v(TAG, "access_token = " + access_token + ", expires_in = "
						+ expires_in + ", uid = " + uid);

				// 关闭WebView控件
				webView.destroy();
				// 通知主线程用户授权成功
				Intent it = new Intent();
				it.putExtra("auth_state", "ok");
				it.putExtra("access_token", access_token);
				it.putExtra("expires_in", expires_in);
				it.putExtra("uid", uid);
				setResult(RESULT_OK, it);
				// 返回MainActivity
				finish();
			}

			@Override
			public void onError() {
				// TODO Auto-generated method stub
				Log.v(TAG, "onError");

				String error_code = oauth.getLastError(); // 获取错误码
				String error_msg = oauth.getLastErrorMsg(); // 获取错误描述信息
				Log.v(TAG, "error_code = " + error_code + ", error_msg = "
						+ error_msg);

				// 关闭WebView控件
				webView.destroy();
				// 通知主线程用户授权出错
				Intent it = new Intent();
				it.putExtra("auth_state", "error");
				it.putExtra("error_code", error_code);
				it.putExtra("error_msg", error_msg);
				setResult(RESULT_OK, it);
				// 返回MainActivity
				finish();
			}
		});
	}

}
