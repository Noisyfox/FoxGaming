/**
 * FileName:     Main.java
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
 * @ClassName: Main
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午8:16:28
 * 
 */
public final class GameCore {

	private GameView gameView;
	private static Activity mainActivity;

	private GamingThread thread_Gaming = null;

	private static boolean inited = false;

	public GameCore(Activity mainActivity) {
		if (inited) {
			throw new RuntimeException();
		}
		
		GameCore.mainActivity = mainActivity;
		initializeCore();
		
		inited = true;
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

	private void setupGameThread() {
		thread_Gaming = new GamingThread(gameView.getHolder());
	}

	public void gameStart() {
		thread_Gaming.start();
	}

	public void gameEnd() {
		thread_Gaming.gameEnd();
		while (thread_Gaming.isAlive()) {// 等待游戏线程结束
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public GameView getGameView() {
		return gameView;
	}

	public static Context getMainContext() {
		return mainActivity;
	}

	public static Activity getMainActivity() {
		return mainActivity;
	}

}
