/**
 * FileName:     Player.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-18 下午11:11:59
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-18      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;

/**
 * @ClassName: Player
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-18 下午11:11:59
 * 
 */
public class Player extends Performer {

	private EventsListener eventsListener = new EventsListener() {

		@Override
		public void onCreate(Performer performer) {
			// TODO Auto-generated method stub
			super.onCreate(performer);
		}

		@Override
		public void onDestory(Performer performer) {
			// TODO Auto-generated method stub
			super.onDestory(performer);
		}

		@Override
		public void onTouch(Performer performer, int whichfinger, int x, int y) {
			// TODO Auto-generated method stub
			super.onTouch(performer, whichfinger, x, y);
		}

		@Override
		public void onTouchPress(Performer performer, int whichfinger, int x,
				int y) {
			// TODO Auto-generated method stub
			super.onTouchPress(performer, whichfinger, x, y);
		}

	};

	@Override
	protected void onDraw(Performer performer) {

	}

	public Player() {
		this.setEventsListener(eventsListener);

	}

}
