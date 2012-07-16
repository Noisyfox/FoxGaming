/**
 * FileName:     performer.java
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
package org.foxteam.noisyfox.FoxGaming.Core;

import java.util.ArrayList;
import java.util.List;

import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;

import android.graphics.Canvas;

/**
 * @ClassName: performer
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午9:21:05
 * 
 */
public class Performer {
	private EventsListener eventsListener = new EventsListener() {
	};

	private List<Alarm> alarms;
	private boolean visible = true;
	private int x = 0, y = 0;
	private int depth = 0;
	protected boolean frozen = false;
	private Sprite sprite = null;
	protected boolean employed = false;
	protected boolean performing = false;
	public String description = "";// 不产生实际作用，仅在调试、编辑时做参考用

	public Performer() {
		alarms = new ArrayList<Alarm>();
		for (int i = 0; i < 10; i++) {
			Alarm a = new Alarm();
			alarms.add(a);
		}
	}

	public final int getDepth() {
		return depth;
	}

	public final void setEventsListener(EventsListener eventsListener) {
		this.eventsListener = eventsListener;
	}

	// 最特殊的event回调函数，地位与EventsListener相同
	// 没有重载前负责绘制默认精灵，重载后如果不手动调用绘图则会使该performer不绘制默认精灵
	protected void onDraw(Performer performer) {
		if (sprite != null) {
			Canvas c = performer.getCanvas();
			sprite.draw(c, x, y);
		}
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
			eventsListener.onTouchRelease(this, (Integer) args[0]);
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
		case EventsListener.EVENT_ONALARM:
			eventsListener.onAlarm(this, (Integer) args[0]);
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
		callEvent(EventsListener.EVENT_ONDESTORY);
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

	public final void setAlarm(int alarm, int step, boolean repeat) {
		Alarm a = alarms.get(alarm);
		a.setSteps = step;
		a.repeat = repeat;
	}

	public final void startAlarm(int alarm) {
		Alarm a = alarms.get(alarm);
		a.remainSteps = a.setSteps;
		a.start = true;
	}

	public final void stopAlarm(int alarm) {
		Alarm a = alarms.get(alarm);
		a.start = false;
	}

	public final int getAlarmRemainStep(int alarm) {
		Alarm a = alarms.get(alarm);
		return a.remainSteps;
	}

	public final boolean isAlarmStart(int alarm) {
		Alarm a = alarms.get(alarm);
		return a.start;
	}

	protected final void goAlarm() {
		for (int i = 0; i < alarms.size(); i++) {
			Alarm a = alarms.get(i);
			if (a.start) {
				if (a.remainSteps > 0) {
					a.remainSteps -= 1;
					if (a.remainSteps == 0) {
						callEvent(EventsListener.EVENT_ONALARM, i);
						if (a.repeat) {
							a.remainSteps = a.setSteps;
						} else {
							a.start = false;
						}
					}
				} else {
					a.start = false;
					a.remainSteps = 0;
				}
			}
		}
	}

	private class Alarm {
		public boolean start = false;
		public int setSteps = 0;
		public int remainSteps = 0;
		public boolean repeat = false;
	}

}
