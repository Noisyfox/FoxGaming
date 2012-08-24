/**
 * FileName:     Bullet_Enemy_2.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-24 上午10:26:05
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-24      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers.Bullets;

import org.foxteam.noisyfox.FoxGaming.Core.Stage;
import org.foxteam.noisyfox.FoxGaming.G2D.GraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;
import org.foxteam.noisyfox.THEngine.Performers.Hitable;
import org.foxteam.noisyfox.THEngine.Performers.Player;

/**
 * @ClassName: Bullet_Enemy_2
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-24 上午10:26:05
 * 
 */
public class Bullet_Enemy_2 extends Bullet_Enemy {

	float mySpeed = 0f;
	float myDirection = 0f;

	@Override
	protected void onCreate() {

		Sprite bulletSprite = new Sprite();
		bulletSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.bullet_enemy_2, false);
		bulletSprite.setOffset(bulletSprite.getWidth() / 2,
				bulletSprite.getHeight() / 2);

		this.bindSprite(bulletSprite);

		GraphicCollision co = new GraphicCollision();
		co.addCircle(0, 0, 11, true);
		this.bindCollisionMask(co);

		this.setDamage(11);
		this.motion_set(myDirection, mySpeed);
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
	}

	public Bullet_Enemy_2(int x, int y, float direction, float speed) {
		this.perform(Stage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
		mySpeed = speed;
		myDirection = direction;
	}

	@Override
	public void hitOn(Hitable target) {
		if (Player.class.isInstance(target)) {
			this.dismiss();
		}
	}

}
