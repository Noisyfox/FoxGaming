/**
 * FileName:     EventsListener.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-19 下午8:14:09
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-19      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.Core;

/**
 * @ClassName: EventsListener
 * @Description: 负责获取所有的事件监听,重写相应方法以处理对应事件
 * @author: Noisyfox
 * @date: 2012-6-19 下午8:14:09
 * 
 */
public class EventsListener {
	public final static int EVENT_ONCREATE = 1;
	public final static int EVENT_ONDESTORY = 2;
	public final static int EVENT_ONTOUCH = 3;
	public final static int EVENT_ONTOUCHPRESS = 4;
	public final static int EVENT_ONTOUCHRELEASE = 5;
	public final static int EVENT_ONKEY = 6;
	public final static int EVENT_ONKEYPRESS = 7;
	public final static int EVENT_ONKEYRELEASE = 8;
	public final static int EVENT_ONALARM = 9;
	public final static int EVENT_ONGAMESTART = 10;
	public final static int EVENT_ONGAMEPAUSE = 11;
	public final static int EVENT_ONGAMERESUME = 12;
	public final static int EVENT_ONGAMEEND = 13;
	public final static int EVENT_ONSTAGECHANGE = 14;
	public final static int EVENT_ONSTAGESTART = 15;
	public final static int EVENT_ONSTAGEEND = 16;
	public final static int EVENT_ONDRAW = 17;
	public final static int EVENT_ONSTEP = 18;
	public final static int EVENT_ONSTEPSTART = 19;
	public final static int EVENT_ONSTEPEND = 20;
	public final static int EVENT_ONCOLLISIONWITH = 21;
	public final static int EVENT_ONUSERDEFINEDEVENT = 22;
	public final static int EVENT_ONSCREENSIZECHANGED = 23;

	protected void onCreate(Performer performer) {
	}// 当一个performer被创建时接收到的第一个事件，优先于其它所有事件

	protected void onDestory(Performer performer) {
	}// 当一个performer被销毁时接收到的事件，是一个performer生命周期里接收到的最后一个事件

	protected void onTouch(Performer performer, int whichfinger, int x, int y) {
	}// 只要触屏被按下就会触发

	protected void onTouchPress(Performer performer, int whichfinger, int x,
			int y) {
	}// 当有新的手指按下的时刻触发

	protected void onTouchRelease(Performer performer, int whichfinger) {
	}// 当有手指离开触屏时触发

	protected void onKey(Performer performer, int keyCode) {
	}// 按键只要持续按下就持续触发

	protected void onKeyPress(Performer performer, int keyCode) {
	}// 按键被按下的时刻触发

	protected void onKeyRelease(Performer performer, int keyCode) {
	}// 按键松开的时刻触发

	protected void onAlarm(Performer performer, int whichAlarm) {
	}// 定时器事件，在 EVENT_ONSTEPSTART 事件之后触发

	protected void onGameStart(Performer performer) {
	}// 游戏开始时广播的事件，在向第一个stage广播完 EVENT_ONCREATE 事件后广播，整个游戏中第二个触发的事件

	protected void onGamePause(Performer performer) {
	}// 游戏暂停时广播的事件

	protected void onGameResume(Performer performer) {
	}// 游戏恢复时广播的事件

	protected void onGameEnd(Performer performer) {
	}// 游戏结束时的stage里的performer都会接收到该事件，是整个游戏里最后发出的事件

	protected void onStageChange(Performer performer) {
	}// 切换stage时前一个和目标stage里的performer都会接收到该事件，优先于其他所有STAGE事件

	protected void onStageStart(Performer performer) {
	}// 切换stage时目标stage里所有performer都会接收到该事件，在EVENT_ONCREATE 和
		// EVENT_ONSTAGECHANGE 事件之后

	protected void onStageEnd(Performer performer) {
	}// 切换stage时前一个stage里所有performer都会接收到该事件，在 EVENT_ONSTAGECHANGE 事件之后

	protected void onStep(Performer performer) {
	}// 每一此循环都会触发，在画布绘制(EVENT_ONDRAW 事件)之前触发

	protected void onStepStart(Performer performer) {
	}// 每一个游戏循环最先的一个事件（切换stage时触发的event除外)

	protected void onStepEnd(Performer performer) {
	}// 每一个游戏循环最后的一个事件（EVENT_ONUSERDEFINEDEVENT 除外)

	protected void onCollisionWith(Performer performer, Performer target) {
	}// 当与指定类型的 Performer 发生碰撞的时候触发，只有主动请求碰撞检测的 Performer 会接收到该事件

	protected void onScreenSizeChanged(Performer performer, int width,
			int height) {
	}// 当屏幕尺寸发生变化时（如改变屏幕方向）触发

	protected void onDraw(Performer performer) {
	}// 绘制事件

	protected void onUserDefinedEvent(Performer performer, int event) {
	}// 只会在程序中手动调用
}
