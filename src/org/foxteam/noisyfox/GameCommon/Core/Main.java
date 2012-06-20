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
package org.foxteam.noisyfox.GameCommon.Core;

import android.content.Context;
import android.graphics.Canvas;

/**
 * @ClassName:     Main
 * @Description:   TODO
 * @author:        Noisyfox
 * @date:          2012-6-19 下午8:16:28
 *
 */
public class Main {
	
	private GameView gameView;
	public static Context mContext;
	
	private GamingThread thread_Gaming = null;
	
	public Main(Context context){
		mContext = context;
		initializeCore();
	}
	
	private void initializeCore(){
		gameView = new GameView(mContext);
		setupGameThread();
		gameView.setOnTouchListener(thread_Gaming);
		gameView.setOnKeyListener(thread_Gaming);
	}
	
	private void setupGameThread(){
		thread_Gaming = new GamingThread(gameView.getHolder());
	}
	
	GameView getGameView(){
		return gameView;
	}

}
