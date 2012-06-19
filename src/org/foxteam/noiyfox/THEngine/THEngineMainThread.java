/**
 * FileName:     THEngineMainThread.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-16 下午8:42:36
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-16      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noiyfox.THEngine;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @ClassName: THEngineMainThread
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-16 下午8:42:36
 * 
 */
public class THEngineMainThread extends Thread implements
		SurfaceHolder.Callback, OnTouchListener {

	SurfaceHolder surfaceHolder;
	long countFrame = 0;
	double currentFPS = 0;
	long drawStartTime = 0;
	long countDrawingTime = 0;
	long countDrawingTime_ls = 0;
	long countFPSStartTime = 0;
	long countFPSFrame = 0;

	boolean run = false;
	boolean finish = false;
	Paint paint;

	final double MAX_FPS = 10.0;
	final long FPS_COUNT_INTERVAL_MILLIS = 100;

	public THEngineMainThread(SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
		paint = new Paint(Color.BLACK);
	}

	@Override
	public void run() {
		while (!finish) {
			while (run) {
				beforeDrawLogic();
				if (shouldDraw()) {
					onDrawLogic();
					draw1All();
				}
				if (drawStartTime == 0) {
					drawStartTime = System.currentTimeMillis();
					countDrawingTime_ls = countDrawingTime;
				} else {
					countDrawingTime = countDrawingTime_ls
							+ System.currentTimeMillis() - drawStartTime;
				}
				afterDrawLogic();
			}
			drawStartTime = 0;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		this.start();
		run = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		finish = true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

		}
		return false;
	}

	private boolean shouldDraw() {
		double k = MAX_FPS / countFPS();
		if (k >= 1.0)
			return true;
		Random rand = new Random();
		int i = rand.nextInt(); // int范围类的随机数
		i = rand.nextInt(100) + 1; // 生成0-100以内的随机数
		if (i <= 10 * k)
			return true;
		else
			return false;
	}

	public double countFPS() {
		try {
			if (countFPSStartTime == 0) {
				countFPSStartTime = System.currentTimeMillis();
				countFPSFrame = 0;
			} else if (System.currentTimeMillis() - countFPSStartTime > FPS_COUNT_INTERVAL_MILLIS) {
				currentFPS = countFPSFrame
						/ ((System.currentTimeMillis() - countFPSStartTime) / 1000);
				countFPSStartTime = System.currentTimeMillis();
				countFPSFrame = 0;
			}
		} catch (Exception ex) {

		}
		return currentFPS;
	}

	public void draw1All() {
		Canvas c = null;
		try {
			synchronized (surfaceHolder) {
				c = surfaceHolder.lockCanvas();
				c.drawARGB(255, 255, 255, 255);
				c.drawText(currentFPS + ";" + countFrame + ";"
						+ countDrawingTime, 50, 50, paint);
				countFrame++;
				countFPSFrame++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (c != null) {
				surfaceHolder.unlockCanvasAndPost(c);
			}
		}
	}

	public void onDrawLogic() {

	}

	public void beforeDrawLogic() {

	}

	public void afterDrawLogic() {

	}

}
