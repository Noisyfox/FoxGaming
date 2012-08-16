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
package org.foxteam.noisyfox.THEngine.Performers.Bullets;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;
import org.foxteam.noisyfox.THEngine.Performers.Bullet;
import org.foxteam.noisyfox.THEngine.Performers.Hitable;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy;

/**
 * @ClassName: Bullet_Player
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-19 下午3:11:09
 * 
 */
public class Bullet_Player extends Bullet {

	@Override
	protected void onCreate() {

		Sprite bulletSprite = new Sprite();
		bulletSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.bullet, false);
		bulletSprite.setOffset(bulletSprite.getWidth() / 2 + 1, 0);

		this.bindSprite(bulletSprite);

		GraphicCollision co = new GraphicCollision();
		co.addCircle(0, 8, 5, true);
		this.bindCollisionMask(co);

		this.setDamage(11);
	}

	@Override
	protected void onStep() {
		this.setPosition(this.getX(), this.getY() - 300f / Stage.getSpeed());
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
	}

	@Override
	protected void onDraw() {
		super.onDraw();
		this.getCollisionMask().draw(this.getCanvas());
	}

	public Bullet_Player(int x, int y) {
		this.perform(Stage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
	}

	@Override
	public void hitOn(Hitable target) {
		if (Enemy.class.isInstance(target)) {
			this.dismiss();
		}
	}

}
