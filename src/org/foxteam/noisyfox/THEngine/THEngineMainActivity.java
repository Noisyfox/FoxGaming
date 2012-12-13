package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.THEngine.Performers.HighScore;
import org.foxteam.noisyfox.THEngine.Performers.WeiboShareProcessor;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.SectionStage;
import org.foxteam.noisyfox.THEngine.Section.Stages._02_Section_01_TestStage;
import org.foxteam.noisyfox.THEngine.Stages.*;
import org.foxteam.noisyfox.common.huawei.SDKUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class THEngineMainActivity extends FGGameActivity {

	public final static int MESSAGE_SHOWHIGHSCOREDIALOG = 1;
	public final static int MESSAGE_SHOWTOAST = 2;
	public final static int MESSAGE_SHAREWEIBO = 3;

	public final static int RESULT_SHAREHIGHSCORE = 1;

	static Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SHOWHIGHSCOREDIALOG:
				HighScore.requireHighScoreRecorded(FGGamingThread.score);
				break;
			case MESSAGE_SHOWTOAST:
				Toast.makeText(FGGameCore.getMainContext(),
						msg.getData().getString("message"), Toast.LENGTH_SHORT)
						.show();
				break;
			case MESSAGE_SHAREWEIBO:
				WeiboShareProcessor.shareIt();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public static final void showToast(String text) {
		Message message = new Message();
		message.what = THEngineMainActivity.MESSAGE_SHOWTOAST;
		Bundle b = new Bundle();
		b.putString("message", text);
		message.setData(b);
		myHandler.sendMessage(message);
	}

	@Override
	public void onEngineReady() {
		SDKUtil.sdkInit(this, null);
		GlobalResources.loadResources();
		SectionStage.initSectionStage();

		SectionStage.setSpecialStage(new _00_MainMenu(), new _01_GameClear());

		new _02_Section_01_TestStage();
	}

	@Override
	public void onCreate() {
		this.forcePortrait();
		normalMode();
	}

	public static Handler getHandler() {
		return myHandler;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_SHAREHIGHSCORE) {
			if (data != null) {
				String state = data.getStringExtra("auth_state");
				if (state != null) {
					if (state.equals("ok")) {// 用户授权成功
						String access_token = data
								.getStringExtra("access_token");
						long expires_in = data.getLongExtra("expires_in", 0);
						String uid = data.getStringExtra("uid");
						FGDebug.print("access_token = " + access_token
								+ ", expires_in = " + expires_in + ", uid = "
								+ uid);
						// Toast.makeText(this, "用户授权成功!", Toast.LENGTH_SHORT)
						// .show();
					} else if (state.equals("error")) {// 用户授权失败
						String error_code = data.getStringExtra("error_code");
						String error_msg = data.getStringExtra("error_msg");
						FGDebug.print("error_code = " + error_code
								+ ", error_msg = " + error_msg);
						// Toast.makeText(this, "用户授权出错!", Toast.LENGTH_SHORT)
						// .show();
					} else if (state.equals("cancel")) {// 用户取消了授权
						// Toast.makeText(this, "用户取消授权!", Toast.LENGTH_SHORT)
						// .show();
					}
				}
			}
			WeiboShareProcessor.sendShare();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
