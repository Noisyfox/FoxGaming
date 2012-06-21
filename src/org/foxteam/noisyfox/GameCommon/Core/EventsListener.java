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
package org.foxteam.noisyfox.GameCommon.Core;

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
	public final static int EVENT_ONTIMER = 9;
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
	public final static int EVENT_ONUSERDEFINEDEVENT = 21;

	public void onCreate(Performer unit) {
	}// 当一个performer被创建时接收到的第一个事件，优先于其它所有事件

	public void onDestory(Performer unit) {
	}// 当一个performer被销毁时接收到的事件，是一个performer生命周期里接收到的最后一个事件

	public void onTouch(Performer unit, int whichfinger, int x, int y) {
	}// 只要触屏被按下就会触发

	public void onTouchPress(Performer unit, int whichfinger, int x, int y) {
	}// 当有新的手指按下的时刻触发

	public void onTouchRelease(Performer unit, int whichfinger, int x, int y) {
	}// 当有手指离开触屏时触发

	public void onKey(Performer unit, int keyCode) {
	}// 按键只要持续按下就持续触发

	public void onKeyPress(Performer unit, int keyCode) {
	}// 按键被按下的时刻触发

	public void onKeyRelease(Performer unit, int keyCode) {
	}// 按键松开的时刻触发

	public void onTimer(Performer unit, int timer) {
	}

	public void onGameStart(Performer unit) {
	}// 游戏开始时广播的事件，在向第一个stage广播完EVENT_ONCREATE事件后广播，整个游戏中第二个触发的事件

	public void onGamePause(Performer unit) {
	}

	public void onGameResume(Performer unit) {
	}

	public void onGameEnd(Performer unit) {
	}// 游戏结束时的stage里的performer都会接收到该事件，是整个游戏里最后发出的事件

	public void onStageChange(Performer unit) {
	}// 切换stage时前一个和目标stage里的performer都会接收到该事件，优先于其他所有STAGE事件

	public void onStageStart(Performer unit) {
	}// 切换stage时目标stage里所有performer都会接收到该事件，在EVENT_ONCREATE 和
		// EVENT_ONSTAGECHANGE事件之后

	public void onStageEnd(Performer unit) {
	}// 切换stage时前一个stage里所有performer都会接收到该事件，在EVENT_ONSTAGECHANGE事件之后

	public void onStep(Performer unit) {
	}// 每一此循环都会触发，在画布绘制(EVENT_ONDRAW事件)之前触发

	public void onStepStart(Performer unit) {
	}// 每一个游戏循环最先的一个事件（切换stage时触发的event除外)

	public void onStepEnd(Performer unit) {
	}// 每一个游戏循环最后的一个事件（EVENT_ONUSERDEFINEDEVENT除外)

	public void onUserDefinedEvent(Performer unit, int event) {
	}// 只会在程序中手动调用
}
