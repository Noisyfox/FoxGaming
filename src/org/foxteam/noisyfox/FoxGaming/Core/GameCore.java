/**
 * FileName:     GameCore.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-19 下午8:16:28
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-19      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;

/**
 * @ClassName: GameCore
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午8:16:28
 * 
 */
public final class GameCore {

	private static GameView gameView;
	protected static Activity mainActivity;

	private GamingThread thread_Gaming = null;

	private static boolean inited = false;

	//protected Display display;

	public GameCore(Activity mainActivity) {
		GameCore.mainActivity = mainActivity;
		//display = ((WindowManager) mainActivity
		//		.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		
		if (inited) {
			// onActivityRecreated();
		} else {
			initializeCore();

			inited = true;
		}
	}

	private void initializeCore() {
		gameView = new GameView(mainActivity);
		setupGameThread();
		gameView.getHolder().addCallback(thread_Gaming);
		gameView.setOnTouchListener(thread_Gaming);
		gameView.setOnKeyListener(thread_Gaming);

		mainActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// 屏蔽系统对 返回键 的响应
		GamingThread.blockKeyFromSystem(KeyEvent.KEYCODE_BACK, false);
	}

	protected void onActivityRecreated() {
		MyDebug.print("OnActivityRecreated");
		gameView.getHolder().removeCallback(thread_Gaming);
		gameView.setOnTouchListener(null);
		gameView.setOnKeyListener(null);

		gameView = new GameView(mainActivity);
		thread_Gaming.setSurfaceHolder(gameView.getHolder());

		gameView.getHolder().addCallback(thread_Gaming);
		gameView.setOnTouchListener(thread_Gaming);
		gameView.setOnKeyListener(thread_Gaming);

		mainActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	private void setupGameThread() {
		thread_Gaming = new GamingThread(gameView.getHolder());
		thread_Gaming.start();
	}

	public void gameStart() {
		thread_Gaming.gameStart();
	}

	public void gameEnd() {
		thread_Gaming.gameEnd();
		mainActivity.finish();
	}

	public void gamePause() {
		thread_Gaming.gamePause();
	}

	public void gameResume() {
		thread_Gaming.gameResume();
	}

	public static GameView getGameView() {
		return gameView;
	}

	public static Context getMainContext() {
		return mainActivity;
	}

	public static Activity getMainActivity() {
		return mainActivity;
	}

}
