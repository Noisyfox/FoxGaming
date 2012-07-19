/**
 * FileName:     PlayerBullet.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-19 下午3:11:09
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-19      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @ClassName: PlayerBullet
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-19 下午3:11:09
 * 
 */
public class PlayerBullet extends Bullet {
	private EventsListener eventsListener = new EventsListener() {

		@Override
		public void onCreate(Performer performer) {
			Bitmap b = BitmapFactory.decodeResource(GameCore.getMainContext()
					.getResources(),
					org.foxteam.noisyfox.THEngine.R.drawable.bullet);

			Sprite bulletSprite = new Sprite();
			bulletSprite.loadFromBitmap(b);
			bulletSprite.setOffset(bulletSprite.getWidth() / 2, 0);

			performer.bindSprite(bulletSprite);
		}

		@Override
		public void onStep(Performer performer) {
			if (performer.getY() + performer.getSprite().getHeight()
					- performer.getSprite().getOffsetY() < 0) {
				performer.dismiss();
			}
			
			performer.setPosition(performer.getX(), performer.getY() - 300f
					/ Stage.getSpeed());
		}

	};

	public PlayerBullet(int x, int y) {
		this.setEventsListener(eventsListener);
		this.perform(Stage.getCurrentStage());
		this.setPosition(x, y);
	}

}
