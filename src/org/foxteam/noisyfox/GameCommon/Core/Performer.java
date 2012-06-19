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

/**
 * @ClassName: Unit
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-19 下午9:21:05
 * 
 */
public class Performer {
	private EventsListener eventsListener = new EventsListener() {
		@Override
		public void onDraw(Performer unit) {

		}

	};

	private boolean visible = true;
	private int x = 0, y = 0;
	private int deepth = 0;
	private boolean freezed = false;

	public Performer() {

	}
	
	public int getDeepth(){
		return deepth;
	}

	public void setEventsListener(EventsListener eventsListener) {
		this.eventsListener = eventsListener;
	}

	public void callEvent(int event, Object... args) {
		if (eventsListener == null || freezed)
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

	public void perform(int stage) {

	}
}
