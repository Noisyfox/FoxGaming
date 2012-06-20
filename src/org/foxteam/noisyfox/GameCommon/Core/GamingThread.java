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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;

/**
 * @ClassName: GamingThread
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午8:12:06
 * 
 */
public class GamingThread implements Runnable, OnTouchListener, OnKeyListener {

	public static Canvas canvas = null;

	private SurfaceHolder surfaceHolder;
	private boolean running = false;
	private Stage lastStage = null;
	private Stage currentStage = null;
	private Queue<String> stackTouchEvent = new LinkedList<String>();
	private Queue<String> stackKeyEvent = new LinkedList<String>();
	private List<Finger> registedFingers = new ArrayList<Finger>();

	public GamingThread(SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
	}

	@Override
	public void run() {
		// 游戏主循环
		while (running) {
			long frameStartTime = System.currentTimeMillis();
			// 全局参数准备
			canvas = surfaceHolder.lockCanvas();// 获取画布
			currentStage = Stage.index2Stage(Stage.getCurrentStage());
			if (currentStage != null) {
				// 先准备stage
				if (currentStage != lastStage) {// stage发生变化
					if (lastStage != null) {// 不是第一次进游戏，处理上一个stage
						lastStage
								.broadcastEvent(EventsListener.EVENT_ONSTAGECHANGE);
						lastStage
								.broadcastEvent(EventsListener.EVENT_ONSTAGEEND);
						lastStage
								.broadcastEvent(EventsListener.EVENT_ONDESTORY);
					}
					currentStage.broadcastEvent(EventsListener.EVENT_ONCREATE);
					currentStage
							.broadcastEvent(EventsListener.EVENT_ONSTAGECHANGE);
					currentStage
							.broadcastEvent(EventsListener.EVENT_ONSTAGESTART);
					lastStage = currentStage;
				}
				// 处理当前场景的performer
				// 最先广播EVENT_ONSTEPSTART事件
				currentStage.broadcastEvent(EventsListener.EVENT_ONSTEPSTART);

				// 广播EVENT_ONDRAW事件,统一绘制图像
				currentStage.broadcastEvent(EventsListener.EVENT_ONDRAW);

				// 最后广播EVENT_ONSTEPEND事件
				currentStage.broadcastEvent(EventsListener.EVENT_ONSTEPEND);
				// 控制帧速
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
			surfaceHolder.unlockCanvasAndPost(canvas);
			canvas = null;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int eventIndex = event.getActionIndex();
		int fingerID = event.getPointerId(eventIndex);
		boolean registed = false;
		for (Finger f : registedFingers) {
			if(f.id == fingerID){
				registed = true;
				break;
			}
		}
		return true;
	}

	private class Finger {
		int id = 0;
		int index = 0;
		int x = 0;
		int y = 0;
	}

}
