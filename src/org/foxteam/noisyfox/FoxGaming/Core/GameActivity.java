/**
 * FileName:     GameActivity.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-17 下午12:40:34
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-17      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * @ClassName: GameActivity
 * @Description: The base Activity for the game engine to work from
 * @author: Noisyfox
 * @date: 2012-7-17 下午12:40:34
 * 
 */
public class GameActivity extends Activity {
	private static boolean activityCreated = false;// 只允许有一个GameActivity
	private static boolean forcePortrait = false;
	private static boolean forceLandscape = false;
	private static boolean forceFullscreen = false;

	protected static GameCore gameEngine = null;

	/* ******************************************************** */
	// Static function

	/**
	 * Flags debugMode as true, Debug.print will output to LogCat
	 */
	public static final void debugMode() {
		MyDebug.debugMode = true;
	}

	/**
	 * Flags debugMode as false (default state), Debug.print will not output to
	 * LogCat
	 */
	public static final void normalMode() {
		MyDebug.debugMode = false;
	}

	public static final GameCore getGameCore() {
		return gameEngine;
	}

	/* ******************************************************** */
	// Normal function
	/**
	 * Forces the engine to stick to a portrait screen.<br>
	 * This will be applied each time the main Activity be created( or
	 * recreated)
	 */
	public void forcePortrait() {
		forcePortrait = true;
		forceLandscape = false;
	}

	/**
	 * Forces the engine to stick to a landscape screen<br>
	 * This will be applied each time the main Activity be created( or
	 * recreated)
	 */
	public void forceLandscape() {
		forcePortrait = false;
		forceLandscape = true;
	}

	/**
	 * @return TRUE if the engine is being forced into portrait mode
	 */
	public boolean isForcePortrait() {
		return forcePortrait;
	}

	/**
	 * @return TRUE if the engine is being forced into landscape mode
	 */
	public boolean isForceLandscape() {
		return forceLandscape;
	}

	/**
	 * Forces the Activity to be shown fullscreen ie, no titlebar
	 */
	public void forceFullscreen() {
		forceFullscreen = true;
	}

	/* ******************************************************** */
	// Override-able function

	/**
	 * Called when GameActivity is created, you can change engine settings here.
	 * After that, the application will create engine automatically.
	 */
	public void onCreate() {
	}

	/**
	 * Called when the engine is ready to work, usually after onCreate()
	 */
	public void onEngineReady() {
	}

	/* ******************************************************** */
	// System function

	private void prepareEngine() {
		MyDebug.print("prepareEngine()");
		gameEngine = new GameCore(this);
		setContentView(GameCore.getGameView());
	}

	/**
	 * Removes everyting from the memory, and resets statics. This is
	 * automatically called at onDestroy() when isFinishing() is TRUE You
	 * shouldn't need to call this yourself
	 */
	public void dispose() {
		MyDebug.print("dispose()");
		activityCreated = false;
		System.gc();
	}

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyDebug.print("onCreate()");
		if (activityCreated) {
			MyDebug.print("GameActivity already been created");
			gameEngine.onActivityRecreated();
			//MyDebug.print("setContentView");
			setContentView(GameCore.getGameView());
		} else {
			// Debug.startMethodTracing("fox.trace");
			onCreate();

			prepareEngine();
			onEngineReady();

			MyDebug.print("Game start!");
			gameEngine.gameStart();

			activityCreated = true;
		}

		// 应用参数
		if (forceFullscreen) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
		}
		if (forceLandscape) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if (forcePortrait) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

	}

	@Override
	protected void onDestroy() {
		MyDebug.print("onDestroy()");
		super.onDestroy();
		if (isFinishing()) {
			dispose();
			// Debug.stopMethodTracing();
			MyDebug.forceExit();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		gameEngine.gamePause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		gameEngine.gameResume();
	}

}
