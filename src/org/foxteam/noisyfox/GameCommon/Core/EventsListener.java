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
	}

	public void onDestory(Performer unit) {
	}

	public void onTouch(Performer unit, int whichfinger, int x, int y) {
	}

	public void onTouchPress(Performer unit, int whichfinger, int x, int y) {
	}

	public void onTouchRelease(Performer unit, int whichfinger, int x, int y) {
	}

	public void onKey(Performer unit) {
	}

	public void onKeyPress(Performer unit) {
	}

	public void onKeyRelease(Performer unit) {
	}

	public void onTimer(Performer unit, int timer) {
	}

	public void onGameStart(Performer unit) {
	}

	public void onGamePause(Performer unit) {
	}

	public void onGameResume(Performer unit) {
	}

	public void onGameEnd(Performer unit) {
	}

	public void onStageChange(Performer unit) {
	}

	public void onStageStart(Performer unit) {
	}

	public void onStageEnd(Performer unit) {
	}

	public void onStep(Performer unit) {
	}

	public void onStepStart(Performer unit) {
	}

	public void onStepEnd(Performer unit) {
	}

	public void onUserDefinedEvent(Performer unit, int event) {
	}
}
