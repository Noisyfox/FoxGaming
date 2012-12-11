package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.THEngine.Performers.HighScore;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.SectionStage;
import org.foxteam.noisyfox.THEngine.Section.Stages._01_Section_01_TestStage;
import org.foxteam.noisyfox.THEngine.Stages.*;

import android.os.Handler;
import android.os.Message;

public class THEngineMainActivity extends FGGameActivity {

	public final static int MESSAGE_SHOWHIGHSCOREDIALOG = 1;

	static Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SHOWHIGHSCOREDIALOG:
				HighScore.requireHighScoreRecorded(FGGamingThread.score);
				break;
			}
			super.handleMessage(msg);
		}
	};

	public THEngineMainActivity() {
	}

	@Override
	public void onEngineReady() {
		GlobalResources.loadResources();
		SectionStage.initSectionStage();

		new _00_MainMenu();
		new _01_Section_01_TestStage();
	}

	@Override
	public void onCreate() {
		this.forcePortrait();
		normalMode();
	}

	public static Handler getHandler() {
		return myHandler;
	}

}
