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
import android.os.Bundle;

/**
 * @ClassName: GameActivity
 * @Description: The base Activity for the game engine to work from
 * @author: Noisyfox
 * @date: 2012-7-17 下午12:40:34
 * 
 */
public class GameActivity extends Activity {
	private static boolean activityCreated = false;// 只允许有一个GameActivity
	protected static GameCore gameEngine = null;

	/* ******************************************************** */
	// Static function

	/**
	 * Flags debugMode as true, Debug.print will output to LogCat
	 */
	public static final void debugMode() {
		Debug.debugMode = true;
	}

	/**
	 * Flags debugMode as false (default state), Debug.print will not output to
	 * LogCat
	 */
	public static final void normalMode() {
		Debug.debugMode = false;
	}

	public static final GameCore getGameCore() {
		return gameEngine;
	}

	/* ******************************************************** */
	// Normal function

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
		Debug.print("prepareEngine()");
		gameEngine = new GameCore(this);
		setContentView(GameCore.getGameView());
	}

	/**
	 * Removes everyting from the memory, and resets statics. This is
	 * automatically called at onDestroy() when isFinishing() is TRUE You
	 * shouldn't need to call this yourself
	 */
	public void dispose() {
		Debug.print("dispose()");
		activityCreated = false;
		gameEngine.gameEnd();
		System.gc();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Debug.print("onCreate()");
		if (activityCreated) {
			Debug.error("GameActivity already created");
			finish();
			return;
		}
		onCreate();

		prepareEngine();
		onEngineReady();

		Debug.print("Game start!");
		gameEngine.gameStart();

		activityCreated = true;
	}

	@Override
	protected void onDestroy() {
		Debug.print("onDestroy()");
		if (isFinishing()) {
			dispose();
		}
		super.onDestroy();
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
