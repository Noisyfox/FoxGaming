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

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.microedition.khronos.opengles.GL10;

import org.foxteam.noisyfox.FoxGaming.Core.FGStage.ManagedParticleSystem;
import org.foxteam.noisyfox.FoxGaming.G2D.FGDraw;

import android.graphics.Bitmap;
import android.opengl.GLU;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;

/**
 * @ClassName: GamingThread
 * @Description: 游戏主线程
 * @author: Noisyfox
 * @date: 2012-6-19 下午8:12:06
 * 
 */
public final class FGGamingThread extends Thread implements OnTouchListener,
		OnKeyListener, SurfaceHolder.Callback {

	public static long score = 0;// 内置变量-分数

	protected static int width = 0;
	protected static int height = 0;
	protected static int screenRotation = 0;
	protected static SurfaceView surfaceView;

	// 线程状态标识
	private static final int STATEFLAG_WAITING = 0;// 线程刚被创建，尚未开始运作
	private static final int STATEFLAG_STOPING = 1;
	private static final int STATEFLAG_STOPED = 2;
	private static final int STATEFLAG_RUNNING = 3;
	private static final int STATEFLAG_PAUSE = 4;
	private static final int STATEFLAG_PAUSED = 5;
	private static final int STATEFLAG_RESUME = 6;

	private static float SPS = 1f;// step per second,每秒循环的次数，也就是帧速
	private static long allStepCount = 0;
	private static long gameStartTime = 0;
	private static List<Finger> registedFingers = new ArrayList<Finger>();
	private static List<Integer> actionedFingers = new ArrayList<Integer>();
	private static List<Integer> registedKeys = new ArrayList<Integer>();
	private static List<Integer> blockedKeys = new ArrayList<Integer>();

	private final int SPS_COUNT_INTERVAL_MILLIS = 100;// SPS刷新的间隔,单位毫秒

	private boolean processing = false;
	private int lastScreenWidth = 0;
	private int lastScreenHeight = 0;
	private List<TouchEvent> listTouchEvent = new LinkedList<TouchEvent>();
	private Queue<KeyboardEvent> queueKeyEvent = new LinkedList<KeyboardEvent>();
	private long stepCount = 0;
	private long SPS_startTime;
	private long lessTime = 0;// 当无法维持帧速时向别的帧借用的处理时间
	private static long MAXLESSTIME = 1000;// 当无法维持帧速时向别的帧借用的处理时间上限
	private int currentState = STATEFLAG_STOPED;

	private Boolean surfaceChanged = false;

	protected FGGamingThread(SurfaceView surfaceView) {
		FGGamingThread.surfaceView = surfaceView;
		currentState = STATEFLAG_WAITING;
	}

	public static float getSPS() {
		return SPS;
	}

	/**
	 * @Title: getStepCount
	 * @Description: 游戏开始后一共运行了多少step
	 * @param: @return
	 * @return: long
	 */
	public static long getStepCount() {
		return allStepCount;
	}

	/**
	 * @Title: getStepCount
	 * @Description: 游戏开始后一共运行了多少时间
	 * @param: @return
	 * @return: long
	 */
	public static long getGameRunTime() {
		return System.currentTimeMillis() - gameStartTime;
	}

	/**
	 * @Title: getFingerCount
	 * @Description: 当前按下的手指总数
	 * @param: @return
	 * @return: int
	 */
	public static int getFingerCount() {
		return registedFingers.size();
	}

	public static int getScreenWidth() {
		return width;
	}

	public static int getScreenHeight() {
		return height;
	}

	public static Bitmap getScreenshots() {
		int b[] = new int[width * height];
		int bt[] = new int[width * height];
		IntBuffer ib = IntBuffer.wrap(b);
		ib.position(0);
		FGEGLHelper.getBindedGL().glReadPixels(0, 0, width, height,
				GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pix = b[i * width + j];
				int pb = (pix >> 16) & 0xff;
				int pr = (pix << 16) & 0x00ff0000;
				int pix1 = (pix & 0xff00ff00) | pr | pb;
				bt[(height - i - 1) * width + j] = pix1;
			}
		}
		Bitmap bmp = Bitmap.createBitmap(bt, width, height,
				Bitmap.Config.ARGB_8888);
		return bmp;

	}

	/**
	 * @Title: blockKeyFromSystem
	 * @Description: 屏蔽系统对指定按键的响应，比如返回键
	 * @param: @param keyCode 需要屏蔽的按键代码
	 * @param: @param unblock 是否为取消屏蔽keyCode所指定的按键
	 * @return: void
	 */
	public static void blockKeyFromSystem(int keyCode, boolean unblock) {
		if (!unblock) {
			if (!blockedKeys.contains(keyCode)) {
				blockedKeys.add(keyCode);
			}
		} else {
			blockedKeys.remove(Integer.valueOf(keyCode));
		}
	}

	@Override
	public void run() {

		while (currentState == STATEFLAG_WAITING) {// 等待游戏开始
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		while (!processing) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		synchronized (surfaceChanged) {
			FGEGLHelper.bindSurfaceView(surfaceView);
			surfaceChanged = false;
		}

		FGGameCore.getMainActivity().onEngineReady();

		SPS_startTime = System.currentTimeMillis();

		// 游戏主循环
		while (currentState != STATEFLAG_STOPED) {
			switch (currentState) {
			case STATEFLAG_STOPING:// 准备结束游戏
				if (FGStage.currentStage != null) {// 广播 ONGAMEEND 事件
					FGStage.currentStage
							.broadcastEvent(FGEventsListener.EVENT_ONGAMEEND);
				}
				currentState = STATEFLAG_STOPED;// 标记为退出，结束线程
				break;

			case STATEFLAG_RUNNING:
				if (processing) {
					gameLogic();
				}
				break;

			case STATEFLAG_PAUSE:
				if (FGStage.currentStage != null) {
					FGStage.currentStage
							.broadcastEvent(FGEventsListener.EVENT_ONGAMEPAUSE);
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
				if (FGStage.currentStage != null) {
					FGStage.currentStage
							.broadcastEvent(FGEventsListener.EVENT_ONGAMERESUME);
				}
				currentState = STATEFLAG_RUNNING;
				break;
			}
		}
		// 退出游戏主循环，即游戏结束
		// 清理
		FGSimpleSoundEffect.freeAll();
		FGSimpleBGM.freeAll();

		FGDebug.print("Gaming thread exit.");
	}

	/**
	 * @Title: screenRefresh
	 * @Description: 使当前stage图像刷新显示（而不是使用绘图事件，即输出缓冲画布上的图像至屏幕）
	 * @param:
	 * @return: void
	 */
	public final void screenRefresh() {
		// Canvas targetCanvas = surfaceHolder.lockCanvas();// 获取目标画布
		// if (targetCanvas != null) {
		// drawToCanvas(targetCanvas);
		// surfaceHolder.unlockCanvasAndPost(targetCanvas);
		// }
		drawStage();
	}

	private void applyView() {
		if (FGStage.currentStage.activatedView == null) {
			FGEGLHelper.getBindedGL().glViewport(0, 0, width, height);

			FGEGLHelper.getBindedGL().glMatrixMode(GL10.GL_PROJECTION);
			FGEGLHelper.getBindedGL().glLoadIdentity();

			GLU.gluOrtho2D(FGEGLHelper.getBindedGL(), 0, width, height, 0);

			FGEGLHelper.getBindedGL().glMatrixMode(GL10.GL_MODELVIEW);
			FGEGLHelper.getBindedGL().glLoadIdentity();

		} else {
			FGViews v = FGStage.currentStage.activatedView;

			FGEGLHelper.getBindedGL().glViewport((int) v.getXFromScreen(),
					(int) v.getYFromScreen(), (int) v.getWidthFromScreen(),
					(int) v.getHeightFromScreen());

			FGEGLHelper.getBindedGL().glMatrixMode(GL10.GL_PROJECTION);
			FGEGLHelper.getBindedGL().glLoadIdentity();

			GLU.gluOrtho2D(FGEGLHelper.getBindedGL(), v.getXFromStage(),
					v.getXFromStage() + v.getWidthFromStage(),
					v.getYFromStage() + v.getHeightFromStage(),
					v.getYFromStage());

			FGEGLHelper.getBindedGL().glMatrixMode(GL10.GL_MODELVIEW);
			FGEGLHelper.getBindedGL().glLoadIdentity();

		}

	}

	public void drawStage() {
		// GL10 gl = FGEGLHelper.getBindedGL();
		// FGEGLHelper.getBufferGL().glClearColor(255f, 255f, 255f, 1f);
		FGEGLHelper.renderOnScreen();
	}

	// private static final void drawToCanvas(Canvas targetCanvas) {
	// if (targetCanvas != null) {
	// targetCanvas.drawARGB(0, 0, 0, 255);
	// // 处理视角
	// if (FGStage.currentStage.activatedView == null) {
	// targetCanvas.drawBitmap(bufferBitmap, 0, 0, null);
	// } else {
	// FGViews v = FGStage.currentStage.activatedView;
	//
	// targetCanvas.save();
	// targetCanvas.clipRect(v.targetView);
	// viewMatrix.reset();
	// viewMatrix.postScale(
	// v.targetView.width() / v.sourceView.width(),
	// v.targetView.height() / v.sourceView.height(),
	// v.sourceView.centerX(), v.sourceView.centerY());
	// viewMatrix.postRotate(v.sourceAngle, v.sourceView.centerX(),
	// v.sourceView.centerY());
	// viewMatrix.postTranslate(
	// v.targetView.centerX() - v.sourceView.centerX(),
	// v.targetView.centerY() - v.sourceView.centerY());
	// targetCanvas.setDrawFilter(antiAlias ? paintFlagsDrawFilter
	// : null);
	// targetCanvas.drawBitmap(bufferBitmap, viewMatrix, null);
	// targetCanvas.restore();
	//
	// }
	// }
	// }

	/**
	 * @Title: gameLogic
	 * @Description: 主游戏逻辑,负责一个 step 的逻辑处理
	 * @param:
	 * @return: void
	 */
	private void gameLogic() {
		long frameStartTime = System.currentTimeMillis();
		if (gameStartTime == 0)
			gameStartTime = System.currentTimeMillis();

		synchronized (surfaceChanged) {
			if (surfaceChanged) {
				FGEGLHelper.bindSurfaceView(surfaceView);
				surfaceChanged = false;
			}

			if (FGStage.targetStage != null) {
				// 先处理 Stage
				if (FGStage.switchStage) {// Stage 发生变化
					FGStage.switchStage = false;
					boolean gameStart = FGStage.currentStage == null;
					if (FGStage.currentStage != null) {// 不是第一次进游戏
						FGStage.currentStage
								.broadcastEvent(FGEventsListener.EVENT_ONSTAGECHANGE);
						FGStage.currentStage
								.broadcastEvent(FGEventsListener.EVENT_ONSTAGEEND);
						FGStage.currentStage
								.broadcastEvent(FGEventsListener.EVENT_ONDESTORY);

						if (FGStage.currentStage.closed) {
							FGStage s = FGStage.currentStage;
							FGStage.currentStage = FGStage.targetStage;
							s.closeStage();
						} else {
							FGStage.currentStage.initStage();
						}
					}

					FGStage.currentStage = FGStage.targetStage;
					// 执行 Stage 的初始化
					FGDebug.print("Create new stage.");
					FGStage.currentStage.onCreate();

					FGStage.speed = FGStage.currentStage.stageSpeed;

					// 所有事件都必须在EVENT_ONCREATE之后
					FGStage.currentStage.employPerformer();

					if (gameStart) {
						// 第一次进游戏，广播EVENT_ONGAMESTART事件
						FGStage.currentStage
								.broadcastEvent(FGEventsListener.EVENT_ONGAMESTART);
					}

					FGStage.currentStage
							.broadcastEvent(FGEventsListener.EVENT_ONSTAGECHANGE);
					FGStage.currentStage
							.broadcastEvent(FGEventsListener.EVENT_ONSTAGESTART);

				} else {
					FGStage.speed = FGStage.currentStage.stageSpeed;
					FGStage.currentStage.employPerformer();
				}

				// 处理当前场景的performer
				// 最先广播EVENT_ONSTEPSTART事件
				FGStage.currentStage
						.broadcastEvent(FGEventsListener.EVENT_ONSTEPSTART);
				// 处理定时器事件
				FGStage.currentStage.operateAlarm();
				// 计算碰撞
				FGStage.currentStage.operateCollision();
				// 计算离开 Stage
				FGStage.currentStage.detectOutOfStage();
				// 处理Performer的 ScreenPlay
				FGStage.currentStage.playScreenPlay();
				// 处理Performer的运动
				FGStage.currentStage.updateMovement();

				// 检测屏幕尺寸变化
				if (width != lastScreenWidth || height != lastScreenHeight) {
					lastScreenHeight = height;
					lastScreenWidth = width;
					FGStage.currentStage.broadcastEvent(
							FGEventsListener.EVENT_ONSCREENSIZECHANGED, width,
							height);
				}

				// 处理触屏事件队列并广播EVENT_ONTOUCH*事件
				actionedFingers.clear();
				synchronized (listTouchEvent) {
					while (!listTouchEvent.isEmpty()) {
						TouchEvent e = listTouchEvent.get(0);
						listTouchEvent.remove(0);
						actionedFingers.add(e.whichFinger);
						switch (e.event) {
						case MotionEvent.ACTION_DOWN:
							FGStage.currentStage.broadcastEvent(
									FGEventsListener.EVENT_ONTOUCHPRESS,
									e.whichFinger, e.x, e.y);
							break;
						case MotionEvent.ACTION_UP:
							FGStage.currentStage.broadcastEvent(
									FGEventsListener.EVENT_ONTOUCHRELEASE,
									e.whichFinger, e.x, e.y);
							break;
						default:
						}
					}
				}
				synchronized (registedFingers) {
					for (Finger f : registedFingers) {
						if (!actionedFingers.contains(f.id)) {
							FGStage.currentStage.broadcastEvent(
									FGEventsListener.EVENT_ONTOUCH, f.id, f.x,
									f.y);
						}
					}
				}
				// 处理按键事件队列并广播EVENT_ONKEY*事件
				synchronized (queueKeyEvent) {
					while (!queueKeyEvent.isEmpty()) {
						KeyboardEvent e = queueKeyEvent.poll();
						switch (e.getEvent()) {
						case KeyboardEvent.KEY_PRESS:
							FGStage.currentStage.broadcastEvent(
									FGEventsListener.EVENT_ONKEYPRESS,
									e.getKey());
							FGStage.currentStage.broadcastEvent(
									FGEventsListener.EVENT_ONKEY, e.getKey());
							break;
						case KeyboardEvent.KEY_HOLD:
							FGStage.currentStage.broadcastEvent(
									FGEventsListener.EVENT_ONKEY, e.getKey());
							break;
						case KeyboardEvent.KEY_RELEASE:
							FGStage.currentStage.broadcastEvent(
									FGEventsListener.EVENT_ONKEYRELEASE,
									e.getKey());
							break;
						default:
						}
					}
				}

				// 在EVENT_ONDRAW事件之前广播EVENT_ONSTEP事件
				FGStage.currentStage
						.broadcastEvent(FGEventsListener.EVENT_ONSTEP);

				// 更新粒子特效
				FGStage.currentStage.updateParticleSystems();

				// 绘制stage的title等并且广播EVENT_ONDRAW事件,统一绘制图像
				applyView();
				FGEGLHelper.getBindedGL().glClear(
						GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
				FGDraw.setColor(FGStage.currentStage.backgroundColor);// 绘制stage背景色
				FGDraw.setAlpha(1);
				FGDraw.drawRectFill(FGEGLHelper.mBinded_GL, 0, 0,
						FGStage.currentStage.getWidth(),
						FGStage.currentStage.getHeight());
				if (FGStage.currentStage.background != null) {// 绘制背景
					FGStage.currentStage.background.doAndDraw(0, 0,
							FGStage.currentStage.height,
							FGStage.currentStage.width);
				}
				if (FGStage.currentStage.managedParticleSystemSize == 0) {
					FGStage.currentStage
							.broadcastEvent(FGEventsListener.EVENT_ONDRAW);
				} else {
					// 处理粒子和performer的绘制顺序
					int index = 0;
					ManagedParticleSystem m = FGStage.currentStage.managedParticleSystem
							.get(index);
					int nowDepth = m.depth;

					for (FGPerformer p : FGStage.currentStage.performers) {
						if (p.depth <= nowDepth) {
							while (p.depth <= nowDepth
									&& index < FGStage.currentStage.managedParticleSystemSize) {
								m.particleSystem.draw();
								index++;
								if (index < FGStage.currentStage.managedParticleSystemSize) {
									m = FGStage.currentStage.managedParticleSystem
											.get(index);
									nowDepth = m.depth;
								}
							}
						}
						p.callEvent(FGEventsListener.EVENT_ONDRAW);
					}

					for (int i = index; i < FGStage.currentStage.managedParticleSystemSize; i++) {
						FGStage.currentStage.managedParticleSystem.get(i).particleSystem
								.draw();
					}

				}
				// 系统绘制
				screenRefresh();

				// 处理 performer 的 dismiss 操作
				FGStage.currentStage.dismissPerformer();

				// 最后广播EVENT_ONSTEPEND事件
				FGStage.currentStage
						.broadcastEvent(FGEventsListener.EVENT_ONSTEPEND);
			}

			// 控制帧速
			stepCount++;
			allStepCount++;
			long frameFinishTime = System.currentTimeMillis();
			float speed = FGStage.speed;
			long sleepTime = (long) (1.0f / speed * 1000.0f)
					- (frameFinishTime - frameStartTime);
			try {
				if (sleepTime > 0) {
					if (sleepTime - lessTime > 0) {
						Thread.sleep(sleepTime - lessTime);
						lessTime = 0;
					} else {
						Thread.sleep(sleepTime / 2);
						lessTime -= sleepTime / 2;
					}
				} else {
					if (lessTime - sleepTime > MAXLESSTIME) {
						lessTime = MAXLESSTIME;
					} else {
						lessTime -= sleepTime;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			frameFinishTime = System.currentTimeMillis();
			if (frameFinishTime - SPS_startTime >= SPS_COUNT_INTERVAL_MILLIS) {
				SPS = (float) (stepCount / ((frameFinishTime - SPS_startTime) / 1000.0));
				stepCount = 0;
				SPS_startTime = frameFinishTime;
			}
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

		return blockedKeys.contains(keyCode);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (currentState != STATEFLAG_RUNNING)
			return true;

		synchronized (listTouchEvent) {
			synchronized (registedFingers) {

				final int fingerCount = event.getPointerCount();// 获取手指数量
				final int action = event.getActionMasked();// 事件类型
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
		}
		return true;
	}

	// ********************************************************************************
	// * 游戏运行状态控制函数
	// ********************************************************************************
	protected void gameStart() {
		if (currentState != STATEFLAG_STOPED
				&& currentState != STATEFLAG_WAITING) {
			return;
		}

		currentState = STATEFLAG_RUNNING;
	}

	protected void gameEnd() {
		if (currentState != STATEFLAG_RUNNING) {
			return;
		}

		processing = false;
		currentState = STATEFLAG_STOPING;
	}

	protected void gamePause() {
		if (currentState != STATEFLAG_RUNNING) {
			return;
		}

		FGSimpleBGM.onPause();
		FGSimpleSoundEffect.onPause();
		currentState = STATEFLAG_PAUSE;
	}

	protected void gameResume() {
		if (currentState != STATEFLAG_PAUSED && currentState != STATEFLAG_PAUSE) {
			return;
		}

		FGSimpleBGM.onResume();
		FGSimpleSoundEffect.onResume();
		currentState = STATEFLAG_RESUME;
	}

	// ********************************************************************************
	// * Surface 事件响应函数
	// ********************************************************************************
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		synchronized (surfaceChanged) {
			FGDebug.print("serface changed");
			if (width != 0 && height != 0) {
				if (FGGamingThread.width == 0 && FGGamingThread.height == 0) {
					lastScreenHeight = height;
					lastScreenWidth = width;
				}
				FGGamingThread.width = width;
				FGGamingThread.height = height;
				processing = true;
			}
			FGDebug.print("Current view size:" + height + "x" + width);
			surfaceChanged = true;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		processing = false;
		// running = false;
	}

	// ********************************************************************************
	// * 私有辅助类
	// ********************************************************************************
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

}
