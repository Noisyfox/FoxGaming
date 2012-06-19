/**
 * FileName:     DrawThread.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-19 下午8:05:01
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-19      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.GameCommon.Core;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * @ClassName: DrawThread
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午8:05:01
 * 
 */
public class DrawThread implements Runnable {

	private SurfaceHolder surfaceHolder;

	public DrawThread(SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void onDraw(Canvas canvas) {

	}

	public void setMaxFPS(int maxFPS) {

	}

	public final double FPS() {
		return 0;
	}

}
