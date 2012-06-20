/**
 * FileName:     Unit.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-19 下午9:21:05
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-19      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.GameCommon.Core;

import org.foxteam.noisyfox.GameCommon.G2D.Sprite;

import android.graphics.Canvas;

/**
 * @ClassName: Unit
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午9:21:05
 * 
 */
public class Performer {
	private EventsListener eventsListener = new EventsListener() {

	};

	private boolean visible = true;
	private int x = 0, y = 0;
	private int deepth = 0;
	protected boolean frozen = false;
	private Sprite sprite = null;
	protected boolean employed = false;
	protected boolean performing = false;
	public String description = "";// 不产生实际作用，仅在调试、编辑时做参考用

	public Performer() {

	}

	public final int getDeepth() {
		return deepth;
	}

	public final void setEventsListener(EventsListener eventsListener) {
		this.eventsListener = eventsListener;
	}

	// 最特殊的event回调函数，地位与EventsListener相同
	// 没有重载前负责绘制默认精灵，重载后如果不手动调用绘图则会使该performer不绘制默认精灵
	protected void onDraw(Performer unit) {
		Canvas c = unit.getCanvas();
		sprite.paint(c, x, y);
	}

	public final void callEvent(int event, Object... args) {
		if (eventsListener == null || frozen)
			return;
		switch (event) {
		case EventsListener.EVENT_ONCREATE:
			eventsListener.onCreate(this);
			break;
		case EventsListener.EVENT_ONDESTORY:
			eventsListener.onDestory(this);
			break;
		case EventsListener.EVENT_ONTOUCH:
			eventsListener.onTouch(this, (Integer) args[0], (Integer) args[1],
					(Integer) args[2]);
			break;
		case EventsListener.EVENT_ONTOUCHPRESS:
			eventsListener.onTouchPress(this, (Integer) args[0],
					(Integer) args[1], (Integer) args[2]);
			break;
		case EventsListener.EVENT_ONTOUCHRELEASE:
			eventsListener.onTouchRelease(this, (Integer) args[0],
					(Integer) args[1], (Integer) args[2]);
			break;
		case EventsListener.EVENT_ONKEY:
			eventsListener.onKey(this, (Integer) args[0]);
			break;
		case EventsListener.EVENT_ONKEYPRESS:
			eventsListener.onKeyPress(this, (Integer) args[0]);
			break;
		case EventsListener.EVENT_ONKEYRELEASE:
			eventsListener.onKeyRelease(this, (Integer) args[0]);
			break;
		case EventsListener.EVENT_ONTIMER:
			eventsListener.onTimer(this, (Integer) args[0]);
			break;
		case EventsListener.EVENT_ONGAMESTART:
			eventsListener.onGameStart(this);
			break;
		case EventsListener.EVENT_ONGAMEPAUSE:
			eventsListener.onGamePause(this);
			break;
		case EventsListener.EVENT_ONGAMERESUME:
			eventsListener.onGameResume(this);
			break;
		case EventsListener.EVENT_ONGAMEEND:
			eventsListener.onGameEnd(this);
			break;
		case EventsListener.EVENT_ONSTAGECHANGE:
			eventsListener.onStageChange(this);
			break;
		case EventsListener.EVENT_ONSTAGESTART:
			eventsListener.onStageStart(this);
			break;
		case EventsListener.EVENT_ONSTAGEEND:
			eventsListener.onStageEnd(this);
			break;
		case EventsListener.EVENT_ONDRAW:
			if (isVisible())
				onDraw(this);
			break;
		case EventsListener.EVENT_ONSTEP:
			eventsListener.onStep(this);
			break;
		case EventsListener.EVENT_ONSTEPSTART:
			eventsListener.onStepStart(this);
			break;
		case EventsListener.EVENT_ONSTEPEND:
			eventsListener.onStepEnd(this);
			break;
		case EventsListener.EVENT_ONUSERDEFINEDEVENT:
			eventsListener.onUserDefinedEvent(this, (Integer) args[0]);
			break;
		}
	}

	public final void perform(int stage) {
		employed = true;
	}

	public final void dismiss() {
		employed = false;
		performing = false;
	}

	public final void freezeMe() {
		frozen = true;
	}

	public final void unfreezeMe() {
		frozen = false;
	}

	public final boolean isVisible() {
		return visible;
	}

	public final void setVisible(boolean visible) {
		this.visible = visible;
	}

	public final void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public final Canvas getCanvas() {
		return GamingThread.canvas;
	}
}
