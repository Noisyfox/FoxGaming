/**
 * FileName:     GamingThread.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-19 下午8:12:06
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-19      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.GameCommon.Core;

import android.view.SurfaceHolder;

/**
 * @ClassName: GamingThread
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午8:12:06
 * 
 */
public class GamingThread implements Runnable {

	private SurfaceHolder surfaceHolder;
	private boolean running = false;

	public GamingThread(SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
	}

	@Override
	public void run() {
		// 游戏主循环
		while (running) {
			long frameStartTime = System.currentTimeMillis();
			
			
			
			//控制帧速
			long frameFinishTime = System.currentTimeMillis();
			double speed = Stage.getSpeed();
			long sleepTime = (long) (1.0 / speed * 1000.0)
					- (frameFinishTime - frameStartTime);
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
