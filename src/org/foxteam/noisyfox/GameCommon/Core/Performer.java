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
	private Canvas canvas = null;

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
			break;
		case EventsListener.EVENT_ONKEYPRESS:
			break;
		case EventsListener.EVENT_ONKEYRELEASE:
			break;
		case EventsListener.EVENT_ONTIMER:
			break;
		case EventsListener.EVENT_ONGAMESTART:
			break;
		case EventsListener.EVENT_ONGAMEPAUSE:
			break;
		case EventsListener.EVENT_ONGAMERESUME:
			break;
		case EventsListener.EVENT_ONGAMEEND:
			break;
		case EventsListener.EVENT_ONSTAGECHANGE:
			break;
		case EventsListener.EVENT_ONSTAGESTART:
			break;
		case EventsListener.EVENT_ONSTAGEEND:
			break;
		case EventsListener.EVENT_ONDRAW:
			if (isVisible())
				onDraw(this);
			break;
		case EventsListener.EVENT_ONSTEP:
			break;
		case EventsListener.EVENT_ONSTEPSTART:
			break;
		case EventsListener.EVENT_ONSTEPEND:
			break;
		case EventsListener.EVENT_ONUSERDEFINEDEVENT:
			break;
		}
	}

	public final void perform(int stage) {

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
		return canvas;
	}
}
