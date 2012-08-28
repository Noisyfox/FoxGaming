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
public final class FGGameCore {

	protected static FGGameView gameView;
	protected static Activity mainActivity;

	protected FGGamingThread thread_Gaming = null;

	private static boolean inited = false;

	public FGGameCore(Activity mainActivity) {
		FGGameCore.mainActivity = mainActivity;
		if (inited) {
		} else {
			initializeCore();

			inited = true;
		}
	}

	/**
	 * @Title: initializeCore
	 * @Description: 初始化核心
	 * @param:
	 * @return: void
	 */
	private void initializeCore() {
		gameView = new FGGameView(mainActivity);// 创建 View
		setupGameThread();// 启动主线程
		gameView.getHolder().addCallback(thread_Gaming);
		gameView.setOnTouchListener(thread_Gaming);
		gameView.setOnKeyListener(thread_Gaming);

		// 应用系统音量控制
		mainActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// 屏蔽系统对 返回键 的响应
		FGGamingThread.blockKeyFromSystem(KeyEvent.KEYCODE_BACK, false);
	}

	/**
	 * @Title: onActivityRecreated
	 * @Description: Activity 被重新创建时再次选择性的初始化
	 * @param:
	 * @return: void
	 */
	protected void onActivityRecreated() {
		FGDebug.print("OnActivityRecreated");
		gameView.getHolder().removeCallback(thread_Gaming);
		gameView.setOnTouchListener(null);
		gameView.setOnKeyListener(null);

		gameView = new FGGameView(mainActivity);
		FGGamingThread.surfaceHolder = gameView.getHolder();

		gameView.getHolder().addCallback(thread_Gaming);
		gameView.setOnTouchListener(thread_Gaming);
		gameView.setOnKeyListener(thread_Gaming);

		mainActivity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	/**
	 * @Title: setupGameThread
	 * @Description: 启动主线程并进入等待游戏开始的状态
	 * @param:
	 * @return: void
	 */
	private void setupGameThread() {
		thread_Gaming = new FGGamingThread(gameView.getHolder());
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

	/**
	 * @Title: getGameView
	 * @Description: 获取运行时常量 gameView
	 * @param: @return
	 * @return: FGGameView
	 */
	public static FGGameView getGameView() {
		return gameView;
	}

	public static Context getMainContext() {
		return mainActivity;
	}

	public static Activity getMainActivity() {
		return mainActivity;
	}

}
