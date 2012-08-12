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
import org.foxteam.noisyfox.FoxGaming.G2D.*;

/**
 * @ClassName: PlayerBullet
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-19 下午3:11:09
 * 
 */
public class PlayerBullet extends Bullet {

	GraphicCollision co;

	private EventsListener eventsListener = new EventsListener() {

		@Override
		public void onCreate(Performer performer) {

			Sprite bulletSprite = new Sprite();
			bulletSprite.loadFromBitmap(
					org.foxteam.noisyfox.THEngine.R.drawable.bullet, false);
			bulletSprite.setOffset(bulletSprite.getWidth() / 2 + 1, 0);

			performer.bindSprite(bulletSprite);

			co = new GraphicCollision();
			co.addCircle(0, 8, 5, true);
			// MyDebug.print(bulletSprite.getWidth() + "");
			performer.bindCollisionMask(co);
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

	@Override
	protected void onDraw(Performer performer) {
		super.onDraw(performer);
		co.draw(performer.getCanvas());
	}

	public PlayerBullet(int x, int y) {
		this.setEventsListener(eventsListener);
		this.perform(Stage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
	}

}
