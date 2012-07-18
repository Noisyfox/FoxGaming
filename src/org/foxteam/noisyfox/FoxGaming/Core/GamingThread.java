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
package org.foxteam.noisyfox.FoxGaming.Core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
public class GamingThread extends Thread implements OnTouchListener,
		OnKeyListener, SurfaceHolder.Callback {

	protected static Canvas canvas = null;
	protected static int width = 0;
	protected static int height = 0;

	private static final int STATEFLAG_WAITING = 0;// 线程刚被创建，尚未开始运作
	private static final int STATEFLAG_STOPING = 1;
	private static final int STATEFLAG_STOPED = 2;
	private static final int STATEFLAG_RUNNING = 3;
	private static final int STATEFLAG_PAUSE = 4;
	private static final int STATEFLAG_PAUSED = 5;
	private static final int STATEFLAG_RESUME = 6;

	private static double SPS = 1;// step per second,每秒循环的次数，也就是帧速
	private static long allStepCount = 0;
	private static long gameStartTime = 0;
	private static List<Finger> registedFingers = new ArrayList<Finger>();
	private static List<Integer> registedKeys = new ArrayList<Integer>();
	private static List<Integer> blockedKeys = new ArrayList<Integer>();

	private final int SPS_COUNT_INTERVAL_MILLIS = 100;// SPS刷新的间隔,单位毫秒

	private SurfaceHolder surfaceHolder;
	private boolean processing = false;
	private Stage lastStage = null;
	private Stage currentStage = null;
	private List<TouchEvent> listTouchEvent = new ArrayList<TouchEvent>();
	private Queue<KeyboardEvent> queueKeyEvent = new LinkedList<KeyboardEvent>();
	private long stepCount = 0;
	private long SPS_startTime;
	private int currentState = STATEFLAG_STOPED;

	public static double getSPS() {
		return SPS;
	}

	public static long getStepCount() {
		return allStepCount;
	}

	public static long getGameRunTime() {
		return System.currentTimeMillis() - gameStartTime;
	}

	public static int getFingerCount() {
		return registedFingers.size();
	}

	// 屏蔽系统对指定按键的响应，比如返回键
	public static void blockKeyFromSystem(int keyCode, boolean unblock) {
		if (!unblock) {
			blockedKeys.add(keyCode);
		} else {
			for (int i = 0; i < blockedKeys.size();) {
				if (blockedKeys.get(i) == keyCode) {
					blockedKeys.remove(i);
				} else {
					i++;
				}
			}
		}
	}

	protected GamingThread(SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
		currentState = STATEFLAG_WAITING;
	}

	protected void setSurfaceHolder(SurfaceHolder surfaceHolder) {
		this.surfaceHolder = surfaceHolder;
	}

	@Override
	public void run() {
		while (currentState == STATEFLAG_WAITING) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		SPS_startTime = System.currentTimeMillis();

		// 游戏主循环
		while (currentState != STATEFLAG_STOPED) {
			switch (currentState) {
			case STATEFLAG_STOPING:
				if (currentStage != null) {
					currentStage.broadcastEvent(EventsListener.EVENT_ONGAMEEND);
				}
				currentState = STATEFLAG_STOPED;
				break;

			case STATEFLAG_RUNNING:
				if (processing) {
					gameLogic();
				}
				break;

			case STATEFLAG_PAUSE:
				if (currentStage != null) {
					currentStage
							.broadcastEvent(EventsListener.EVENT_ONGAMEPAUSE);
				}
				currentState = STATEFLAG_PAUSED;
				break;

			case STATEFLAG_PAUSED:
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case STATEFLAG_RESUME:
				if (currentStage != null) {
					currentStage
							.broadcastEvent(EventsListener.EVENT_ONGAMERESUME);
				}
				currentState = STATEFLAG_RUNNING;
				break;
			}
		}
		// 退出游戏主循环，即游戏结束
		// 清理
		SimpleSoundEffect.freeAll();
		SimpleBGM.freeAll();
	}

	// 主游戏逻辑,负责一个 step 的逻辑处理
	private void gameLogic() {
		long frameStartTime = System.currentTimeMillis();
		if (gameStartTime == 0)
			gameStartTime = System.currentTimeMillis();

		// 全局参数准备
		canvas = surfaceHolder.lockCanvas();// 获取画布
		currentStage = Stage.index2Stage(Stage.getCurrentStage());
		if (currentStage != null && canvas != null) {
			// 先准备stage
			if (currentStage != lastStage) {// stage发生变化
				// 全局变量应用
				Stage.speed = currentStage.getStageSpeed();

				if (lastStage != null) {// 不是第一次进游戏，处理上一个stage
					lastStage
							.broadcastEvent(EventsListener.EVENT_ONSTAGECHANGE);
					lastStage.broadcastEvent(EventsListener.EVENT_ONSTAGEEND);
					lastStage.broadcastEvent(EventsListener.EVENT_ONDESTORY);
				}
				// 所有事件都必须在EVENT_ONCREATE之后
				currentStage.broadcastEvent(EventsListener.EVENT_ONCREATE);
				// 第一次进游戏，广播EVENT_ONGAMESTART事件
				if (lastStage == null) {
					currentStage
							.broadcastEvent(EventsListener.EVENT_ONGAMESTART);
				}
				currentStage.broadcastEvent(EventsListener.EVENT_ONSTAGECHANGE);
				currentStage.broadcastEvent(EventsListener.EVENT_ONSTAGESTART);
				lastStage = currentStage;
			}
			// 处理当前场景的performer
			// 最先广播EVENT_ONSTEPSTART事件
			currentStage.broadcastEvent(EventsListener.EVENT_ONSTEPSTART);
			// 处理定时器事件
			currentStage.processAlarm();

			// 处理触屏事件队列并广播EVENT_ONTOUCH*事件
			synchronized (listTouchEvent) {
				while (!listTouchEvent.isEmpty()) {
					TouchEvent e = listTouchEvent.get(0);
					listTouchEvent.remove(0);
					switch (e.event) {
					case MotionEvent.ACTION_DOWN:
						currentStage.broadcastEvent(
								EventsListener.EVENT_ONTOUCHPRESS,
								e.whichFinger, e.x, e.y);
						break;
					case MotionEvent.ACTION_UP:
						currentStage.broadcastEvent(
								EventsListener.EVENT_ONTOUCHRELEASE,
								e.whichFinger, e.x, e.y);
						break;
					default:
					}
				}
			}
			for (Finger f : registedFingers) {
				currentStage.broadcastEvent(EventsListener.EVENT_ONTOUCH, f.id,
						f.x, f.y);
			}
			// 处理按键事件队列并广播EVENT_ONKEY*事件
			synchronized (queueKeyEvent) {
				while (!queueKeyEvent.isEmpty()) {
					KeyboardEvent e = queueKeyEvent.poll();
					switch (e.getEvent()) {
					case KeyboardEvent.KEY_PRESS:
						currentStage.broadcastEvent(
								EventsListener.EVENT_ONKEYPRESS, e.getKey());
						currentStage.broadcastEvent(EventsListener.EVENT_ONKEY,
								e.getKey());
						break;
					case KeyboardEvent.KEY_HOLD:
						currentStage.broadcastEvent(EventsListener.EVENT_ONKEY,
								e.getKey());
						break;
					case KeyboardEvent.KEY_RELEASE:
						currentStage.broadcastEvent(
								EventsListener.EVENT_ONKEYRELEASE, e.getKey());
						break;
					default:
					}
				}
			}

			// 在EVENT_ONDRAW事件之前广播EVENT_ONSTEP事件
			currentStage.broadcastEvent(EventsListener.EVENT_ONSTEP);
			// 绘制stage的title等并且广播EVENT_ONDRAW事件,统一绘制图像
			canvas.drawColor(currentStage.getBackgroundColor());// 绘制stage背景色
			if (currentStage.getBackground() != null)
				currentStage.getBackground().doAndDraw(canvas, height, width);// 绘制背景
			currentStage.broadcastEvent(EventsListener.EVENT_ONDRAW);

			// 最后广播EVENT_ONSTEPEND事件
			currentStage.broadcastEvent(EventsListener.EVENT_ONSTEPEND);
		}
		// 绘制
		if (canvas != null)
			surfaceHolder.unlockCanvasAndPost(canvas);
		canvas = null;

		// 控制帧速
		stepCount++;
		allStepCount++;
		long frameFinishTime = System.currentTimeMillis();
		if (frameFinishTime - SPS_startTime >= SPS_COUNT_INTERVAL_MILLIS) {
			SPS = stepCount / ((frameFinishTime - SPS_startTime) / 1000.0);
			stepCount = 0;
			SPS_startTime = frameFinishTime;
		}
		double speed = Stage.getSpeed();
		long sleepTime = (long) (1.0 / speed * 1000.0)
				- (frameFinishTime - frameStartTime);
		try {
			if (sleepTime > 0)
				Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (currentState != STATEFLAG_RUNNING)
			return true;

		int action = event.getAction();
		KeyboardEvent e = null;

		int keyIndex = -1;
		for (Integer i : registedKeys) {
			if (i.intValue() == keyCode) {
				keyIndex = registedKeys.indexOf(i);
				break;
			}
		}

		switch (action) {
		case KeyEvent.ACTION_DOWN:
			if (keyIndex >= 0) {
				e = new KeyboardEvent(keyCode, KeyboardEvent.KEY_HOLD);
			} else {
				e = new KeyboardEvent(keyCode, KeyboardEvent.KEY_PRESS);
				registedKeys.add(keyCode);
			}
			break;
		case KeyEvent.ACTION_UP:
			if (keyIndex >= 0) {
				registedKeys.remove(keyIndex);
				e = new KeyboardEvent(keyCode, KeyboardEvent.KEY_RELEASE);
			}
			break;
		default:
		}

		synchronized (queueKeyEvent) {
			if (e != null) {
				queueKeyEvent.offer(e);
			}
		}

		for (int i : blockedKeys) {
			if (keyCode == i) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (currentState != STATEFLAG_RUNNING)
			return true;

		// 重写的触屏逻辑
		synchronized (listTouchEvent) {

			final int fingerCount = event.getPointerCount();
			final int action = event.getActionMasked();
			final int actionID = event.getPointerId(event.getActionIndex());// 仅在非ACTION_MOVE事件中做手指的唯一标记
			int fingerIndex = -1;
			for (int i = 0; i < fingerCount; i++) {
				if (event.getPointerId(i) == actionID) {
					fingerIndex = i;
					break;
				}
			}

			boolean registed = false;
			Finger registedF = null;

			for (Finger f : registedFingers) {
				if (f.id == actionID) {
					registed = true;
					registedF = f;
					break;
				}
			}

			boolean eventExist = false;

			switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				if (!registed) {
					Finger f = new Finger();
					f.id = actionID;
					f.x = (int) event.getX(fingerIndex);
					f.y = (int) event.getY(fingerIndex);
					registedFingers.add(f);

					for (int i = 0; i < listTouchEvent.size();) {
						TouchEvent e = listTouchEvent.get(i);
						if (e.whichFinger == actionID) {
							eventExist = true;
							if (e.event == MotionEvent.ACTION_DOWN) {
								e.x = (int) event.getX(fingerIndex);
								e.y = (int) event.getY(fingerIndex);
							} else if (e.event == MotionEvent.ACTION_UP) {
								listTouchEvent.remove(i);
							}
							break;
						} else {
							i++;
						}
					}

				}
				if (!eventExist) {
					TouchEvent ne = new TouchEvent(actionID,
							MotionEvent.ACTION_DOWN,
							(int) event.getX(fingerIndex),
							(int) event.getY(fingerIndex));
					listTouchEvent.add(ne);
				}
				break;

			case MotionEvent.ACTION_MOVE:
				for (Finger f : registedFingers) {
					for (int i = 0; i < fingerCount; i++) {
						if (event.getPointerId(i) == f.id) {
							fingerIndex = i;
							f.x = (int) event.getX(fingerIndex);
							f.y = (int) event.getY(fingerIndex);
						}
					}
				}
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				if (registed) {
					for (int i = 0; i < listTouchEvent.size();) {
						TouchEvent e = listTouchEvent.get(i);
						if (e.whichFinger == actionID) {
							eventExist = true;
							if (e.event == MotionEvent.ACTION_DOWN) {
								listTouchEvent.remove(i);
							} else if (e.event == MotionEvent.ACTION_UP) {
								e.x = (int) event.getX(fingerIndex);
								e.y = (int) event.getY(fingerIndex);
							}
							break;
						} else {
							i++;
						}
					}
					registedFingers.remove(registedF);
				}
				if (!eventExist) {
					TouchEvent ne = new TouchEvent(actionID,
							MotionEvent.ACTION_UP,
							(int) event.getX(fingerIndex),
							(int) event.getY(fingerIndex));
					listTouchEvent.add(ne);
				}
				break;
			}
		}

		return true;
	}

	private class Finger {
		int id = 0;
		int x = 0;
		int y = 0;
	}

	private class TouchEvent {
		public int whichFinger = -1;
		public int event = 0;
		public int x = 0;
		public int y = 0;

		public TouchEvent(int whichFinger, int event, int x, int y) {
			if (whichFinger < 0) {
				throw new IllegalArgumentException();
			}
			this.whichFinger = whichFinger;
			this.event = event;
			this.x = x;
			this.y = y;
		}

	}

	private class KeyboardEvent {
		public static final int KEY_PRESS = 1;
		public static final int KEY_RELEASE = 2;
		public static final int KEY_HOLD = 3;

		private int whichKey = -1;
		private int event = 0;

		public KeyboardEvent(int whichKey, int event) {
			if (whichKey < 0) {
				throw new IllegalArgumentException();
			}
			this.whichKey = whichKey;
			this.event = event;
		}

		public int getKey() {
			return whichKey;
		}

		public int getEvent() {
			return event;
		}
	}

	protected final void gameStart() {
		if (currentState != STATEFLAG_STOPED
				&& currentState != STATEFLAG_WAITING) {
			return;
		}

		currentState = STATEFLAG_RUNNING;
	}

	protected final void gameEnd() {
		if (currentState != STATEFLAG_RUNNING) {
			return;
		}

		processing = false;
		currentState = STATEFLAG_STOPING;
		gameResume();
	}

	protected final void gamePause() {
		if (currentState != STATEFLAG_RUNNING) {
			return;
		}

		SimpleBGM.onPause();
		SimpleSoundEffect.onPause();
		currentState = STATEFLAG_PAUSE;
	}

	protected final void gameResume() {
		if (currentState != STATEFLAG_PAUSED) {
			return;
		}

		SimpleBGM.onResume();
		SimpleSoundEffect.onResume();
		currentState = STATEFLAG_RESUME;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (width != 0)
			GamingThread.width = width;
		if (height != 0)
			GamingThread.height = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		processing = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		processing = false;
		// running = false;
	}

}
