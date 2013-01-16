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
public abstract class FGGameActivity extends Activity {
	private static boolean activityCreated = false;// 只允许有一个GameActivity
	private static boolean forcePortrait = false;
	private static boolean forceLandscape = false;
	private static boolean forceFullscreen = false;

	protected static FGGameCore gameEngine = null;

	/* ******************************************************** */
	// Static function

	/**
	 * Flags debugMode as true, Debug.print will output to LogCat
	 */
	public static final void debugMode() {
		FGDebug.debugMode = true;
	}

	/**
	 * Flags debugMode as false (default state), Debug.print will not output to
	 * LogCat
	 */
	public static final void normalMode() {
		FGDebug.debugMode = false;
	}

	public static final FGGameCore getGameCore() {
		return gameEngine;
	}

	/* ******************************************************** */
	// Normal function
	/**
	 * Forces the engine to stick to a portrait screen.<br>
	 * This will be applied each time the main Activity be created( or
	 * recreated)
	 */
	public final void forcePortrait() {
		forcePortrait = true;
		forceLandscape = false;
	}

	/**
	 * Forces the engine to stick to a landscape screen<br>
	 * This will be applied each time the main Activity be created( or
	 * recreated)
	 */
	public final void forceLandscape() {
		forcePortrait = false;
		forceLandscape = true;
	}

	/**
	 * @return TRUE if the engine is being forced into portrait mode
	 */
	public final boolean isForcePortrait() {
		return forcePortrait;
	}

	/**
	 * @return TRUE if the engine is being forced into landscape mode
	 */
	public final boolean isForceLandscape() {
		return forceLandscape;
	}

	/**
	 * Forces the Activity to be shown fullscreen ie, no titlebar
	 */
	public final void forceFullscreen() {
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

	private final void prepareEngine() {
		FGDebug.print("prepareEngine()");
		gameEngine = new FGGameCore(this);
		setContentView(FGGameCore.gameView);

		FGNativeHelper.loadNativeLibrary();
		FGMathsHelper.generateGaussianDistribution();
	}

	/**
	 * Removes everyting from the memory, and resets statics. This is
	 * automatically called at onDestroy() when isFinishing() is TRUE You
	 * shouldn't need to call this yourself
	 */
	public void dispose() {
		FGDebug.print("dispose()");
		activityCreated = false;
		System.gc();
	}

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FGDebug.print("onCreate()");
		if (activityCreated) {
			FGDebug.print("GameActivity already been created");
			gameEngine.onActivityRecreated();
			// MyDebug.print("setContentView");
			setContentView(FGGameCore.gameView);
		} else {
			// Debug.startMethodTracing("fox.trace");

			FGDebug.debug(" ");
			FGDebug.debug("FoxGaming Engine started!");
			FGDebug.debug("Version " + FGEngineConfig.getVersion());
			FGDebug.debug(" ");

			onCreate();

			prepareEngine();

			// 创建引擎初始界面
			(FGStage.targetStage = new FGSplashStage()).setStageIndex(0);

			FGDebug.print("Game start!");
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
		FGDebug.print("onDestroy()");
		// FGGameCore.gameView.
		super.onDestroy();
		if (isFinishing()) {
			dispose();
			// Debug.stopMethodTracing();

			FGDebug.debug(" ");
			FGDebug.debug("FoxGaming Engine finished!");
			FGDebug.debug("Thanks for using!");
			FGDebug.debug(" ");

			FGDebug.forceExit();// 彻底结束程序
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
