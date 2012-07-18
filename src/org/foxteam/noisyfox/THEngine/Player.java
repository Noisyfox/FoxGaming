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
import org.foxteam.noisyfox.FoxGaming.G2D.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @ClassName: Player
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-18 下午11:11:59
 * 
 */
public class Player extends Performer {

	private Point fingerStart = null;
	private Point fingerStartPerformer = null;

	private EventsListener eventsListener = new EventsListener() {

		@Override
		public void onCreate(Performer performer) {
			Bitmap b = BitmapFactory.decodeResource(GameCore.getMainContext()
					.getResources(),
					org.foxteam.noisyfox.THEngine.R.drawable.player);

			Sprite playerSprite = new Sprite();
			playerSprite.loadFromBitmap(b);
			playerSprite.setOffset(playerSprite.getWidth() / 2,
					playerSprite.getHeight() / 2);

			performer.bindSprite(playerSprite);
			performer.setPosition(GamingThread.getScreenWidth() / 2,
					GamingThread.getScreenHeight() - playerSprite.getHeight()
							/ 2);

		}

		@Override
		public void onDestory(Performer performer) {
			// TODO Auto-generated method stub
			super.onDestory(performer);
		}

		@Override
		public void onTouch(Performer performer, int whichfinger, int x, int y) {
			performer.setPosition(
					fingerStartPerformer.getX() + x - fingerStart.getX(),
					fingerStartPerformer.getY() + y - fingerStart.getY());
		}

		@Override
		public void onTouchPress(Performer performer, int whichfinger, int x,
				int y) {
			if (whichfinger == 0) {
				fingerStart = new Point(x, y);
				fingerStartPerformer = new Point(performer.getX(),
						performer.getY());
			}
		}

	};

	public Player() {
		this.setEventsListener(eventsListener);

	}

}
