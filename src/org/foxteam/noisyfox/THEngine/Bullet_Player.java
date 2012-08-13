/**
 * FileName:     Bullet_Player.java
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
 * @ClassName: Bullet_Player
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-19 下午3:11:09
 * 
 */
public class Bullet_Player extends Bullet {

	@Override
	protected void onCreate(Performer performer) {

		Sprite bulletSprite = new Sprite();
		bulletSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.bullet, false);
		bulletSprite.setOffset(bulletSprite.getWidth() / 2 + 1, 0);

		performer.bindSprite(bulletSprite);

		GraphicCollision co = new GraphicCollision();
		co.addCircle(0, 8, 5, true);
		// MyDebug.print(bulletSprite.getWidth() + "");
		performer.bindCollisionMask(co);

		performer.requireCollisionDetection(Enemy.class);
	}

	@Override
	protected void onStep(Performer performer) {
		performer.setPosition(performer.getX(),
				performer.getY() - 300f / Stage.getSpeed());

		if (performer.getY() + performer.getSprite().getHeight()
				- performer.getSprite().getOffsetY() < 0) {
			performer.dismiss();
		}
	}

	@Override
	protected void onDraw(Performer performer) {
		super.onDraw(performer);
		performer.getCollisionMask().draw(performer.getCanvas());
	}

	public Bullet_Player(int x, int y) {
		this.perform(Stage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
	}

}
