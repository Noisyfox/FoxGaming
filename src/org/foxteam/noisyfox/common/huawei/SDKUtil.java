package org.foxteam.noisyfox.common.huawei;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.imax.vmall.sdk.android.common.adapter.ServiceCallback;
import com.imax.vmall.sdk.android.entry.CapabilityService;
import com.imax.vmall.sdk.android.entry.CommonService;
import com.imax.vmall.sdk.android.huawei.share.ShareStatus;
import com.imax.vmall.sdk.android.huawei.weather.WeatherService;

public final class SDKUtil {
	private static String APPID = "";
	private static String APPKEY = "";

	private static boolean inited = false;

	private static final String TAG = "SDKUtil";
	private static Context context = null;
	public static SDKUtil instance = new SDKUtil();

	// SDK的公共服务类
	private static CommonService mCommon;
	// SDK分享类
	private static ShareStatus mShare;
	// SDK天气类
	private static WeatherService mWeather;

	private SDKUtil() {
	}

	public static final void setSDK(String app_id, String app_key) {
		APPID = app_id;
		APPKEY = app_key;
	}

	// 第一步：初始化SDK
	public static void sdkInit(Context context, final ServiceCallback callBack) {
		SDKUtil.context = context;
		// 获取SDK公共服务类实例
		mCommon = CommonService.getInstance();

		// 初始化SDK,初始化成功后再调用其他接口
		// appID:APP在IMAX注册应用时分配的APP ID
		// appKey:APP在IMAX注册应用时分配的APP KEY
		mCommon.init(SDKUtil.context, APPID, APPKEY, new ServiceCallback() {
			@Override
			public void onComplete(String result) {
				Log.v(TAG, "-----sdk init success-----");
				// 初始化SDK成功，result是IMAX返回的token
				String token = result; // 或者使用//String token =
										// CommonService.getImaxToken();
				Log.v(TAG, "imax_token = " + token);
				// 通知主线程初始化成功，不要在回调方法中执行会阻塞线程的操作
				inited = true;
				if (callBack != null)
					callBack.onComplete(result);
			}

			@Override
			public void onError(String message) {
				// 发生网络错误或者获取IMAX TOKEN失败，message包含错误码
				// 提示SDK初始化失败
				Log.v(TAG, "-----sdk init faild-----");
				Log.v(TAG, "error message = " + message);
				// 通知主线程初始化失败，不要在回调方法中执行会阻塞线程的操作
				inited = false;
				if (callBack != null)
					callBack.onError(message);
			}
		});
	}

	// 第二步：OAuth授权
	// 以下参数需要APP自己到第三方平台注册自己的应用获取
	public static void oauth(String ep_id, int requestCode) {
		if (!inited)
			return;

		// 用WebView控件打开OAuth鉴权页面
		Intent it = new Intent(context, AuthActivity.class);
		it.putExtra("ep_id", ep_id);
		// APP在新浪微博开放平台注册应用时分配的参数
		if (ep_id.equals("sina")) {
			it.putExtra("app_key", "3179803434");
			it.putExtra("app_secret", "3e98c5b102978db17d4e0b8e83a4dd1d");
			it.putExtra("redirect_url", "http://nuaamstc.sinaapp.com");
		}
		// APP在腾讯微博开放平台注册应用时分配的参数
		else if (ep_id.equals("tencent")) {
			it.putExtra("app_key", "801268812");
			it.putExtra("app_secret", "889af130d9db770cb372e91d0c590625");
			it.putExtra("redirect_url", "http://www.baidu.com");
		}
		// APP在人人网开放平台注册应用时分配的参数
		else if (ep_id.equals("renren")) {
			it.putExtra("app_key", "763f986c6d1f41b0b7ec8cba28f8c530");
			it.putExtra("app_secret", "436e0399ac684ff09a0b4dd6aa014cf7");
			it.putExtra("redirect_url",
					"http://graph.renren.com/oauth/login_success.html");
		}
		// APP在豆瓣开放平台注册应用时分配的参数
		else if (ep_id.equals("douban")) {
			it.putExtra("app_key", "06da0ba739b966ae148e22f5ad2c340a");
			it.putExtra("app_secret", "83a7d621037944fa");
			it.putExtra("redirect_url",
					"http://192.168.8.12:8080/user/getBeanCode");
		}
		// APP在开心网开放平台注册应用时分配的参数
		else if (ep_id.equals("kaixin")) {
			it.putExtra("app_key", "689920286369b6fcdd10fd4e56ef55c9");
			it.putExtra("app_secret", "a02ac05cb710b3daea1a09fd3585ae12");
			it.putExtra("redirect_url",
					"http://218.2.129.7:8080/user/getHappyCode");
		}

		((Activity) context).startActivityForResult(it, requestCode);
	}

	// 第三步：分享文字或图片到第三方平台
	public static void statusShare(String epid, String text, String pic,
			final ServiceCallback callBack) {
		if (!inited)
			return;

		// 获取分享类的实例
		mShare = ShareStatus.getInstance();

		/**
		 * 分享的内容不能为空，否则会分享失败；相同的内容重复分享也会失败 一次分享到多家平台，之间用逗号隔开，至少需要填写一家
		 * 分享的内容不能为空，否则会分享失败，分享内容的最大长度请参考第三方平台的说明
		 * 分享的图片最大不能超过3M，APP可在上传前对图片进行裁剪；如果只分享文字微博则此参数传入null
		 * 请确保在调用SDK初始化接口成功后再调用其他接口！！！
		 */
		mShare.share(epid, text, pic, new ServiceCallback() {
			@Override
			public void onComplete(String result) {
				/**
				 * 请求正常响应，服务器返回JSON格式的响应 APP需要解析result，判断分享结果 如果请求的参数错误则会返回形式为
				 * ：{"ret":"2","msg":"缺失必选参数(content),请参考API文档"}
				 * 如果分享成功或分享失败则返回形式为 ：{"renren":{"ret":"1","msg":"失败"},"sina"
				 * :{"ret":"5","msg" :"接口调用频率超过上限"},"kaixin":{"ret":"17","msg"
				 * :"开心账号绑定已经过期，请重新绑定"
				 * },"douban":{"ret":"0","msg":"成功"},"tencent"
				 * :{"ret":"17","msg":"腾讯微博账号绑定已经过期，请重新绑定"}}
				 */

				Log.v(TAG, "-----statusShare success-----");
				Log.v(TAG, "response = " + result);
				// 提示分享结果
				// 通知主线程分享成功，不要在回调方法中执行会阻塞线程的操作
				if (callBack != null)
					callBack.onComplete(result);
			}

			@Override
			public void onError(String message) {
				// HTTP请求发生异常错误
				// 通知主线程分享失败，不要在回调方法中执行会阻塞线程的操作

				Log.v(TAG, "-----statusShare failed-----");
				Log.v(TAG, "response = " + message);

				if (callBack != null)
					callBack.onError(message);
			}
		});
	}

	public static void getWeather(String country, String province, String city,
			String address, final ServiceCallback callBack) {
		if (!inited)
			return;

		mWeather = CapabilityService.getWeatherServiceInstance();

		// 国家名
		// country = "china";
		// 省份名
		// province = "jiangsu";
		// 城市名
		// city = "nanjing";
		// 地点名
		// address = "jiangning";
		// 调用getWeather根据地名获取天气信息
		mWeather.getWeather(country, province, city, address,
				new ServiceCallback() {

					public void onError(String arg0) {
						// api接口调用错误响应
						Log.i(TAG, "getWeather error:" + arg0);

						if (callBack != null)
							callBack.onError(arg0);
					}

					public void onComplete(String arg0) {
						// api接口调用成功响应
						Log.i(TAG, "getWeather complete:" + arg0);

						if (callBack != null)
							callBack.onComplete(arg0);
					}
				});

	}

	public static WeatherDescription analyseWeatherJson(String jsonText) {
		WeatherDescription w = instance.new WeatherDescription();
		try {
			JSONTokener jsonParser = new JSONTokener(jsonText);
			JSONObject weatherB = (JSONObject) jsonParser.nextValue();
			if (weatherB.getString("ret").equals("0")) {// 确保成功
				// 获取weather code
				JSONObject weatherDescription = weatherB
						.getJSONObject("weather");
				String weatherCode = weatherDescription.getString("code");
				w.description = weatherDescription.getString("description");
				w.code = Integer.parseInt(weatherCode);
				w.codeDescription = decodeWeather(w.code);

				// 获取温度和湿度
				w.temperature = weatherB.getString("temperature");
				w.humidity = weatherB.getString("humidity");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return w;
	}

	private static final String[] weatherCodeMap = { "龙卷风", "热带风暴", "飓风",
			"强雷暴", "雷暴", "雨转雪", "雨转雨夹雪", "雪转冻雨", "冻毛毛雨", "细雨", "冻雨", "骤雨",
			"骤雨", "阵雪", "轻型阵雪", "吹雪", "雪", "冰雹", "雨夹雪", "灰尘", "有雾", "烟霾", "黑烟",
			"狂风", "刮风", "冷", "多云", "晴转多云", "晴转多云", "晴转多云", "晴转多云", "晴朗", "晴",
			"晴朗", "晴朗", "雨夹冰雹", "炎热", "局部雷暴", "局部雷暴", "局部雷暴", "局部阵雨", "大雪",
			"局部阵雪", "大雪", "晴转多云", "雷阵雨", "阵雪", "局部雷阵雨" };

	public static final String decodeWeather(int weatherCode) {
		if (weatherCode < 0 || weatherCode > weatherCodeMap.length - 1) {
			return "";
		}

		return weatherCodeMap[weatherCode];
	}

	public class WeatherDescription {

		public String description = "";
		public int code = -1;
		public String codeDescription = "";
		public String temperature = "0C";
		public String humidity = "0%";

	}

	public static boolean isInited() {
		return inited;
	}

}
