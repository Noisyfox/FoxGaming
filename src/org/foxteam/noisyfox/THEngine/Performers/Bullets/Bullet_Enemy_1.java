/**
 * FileName:     Bullet_Enemy_1.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-13 下午3:16:20
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-13      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers.Bullets;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;
import org.foxteam.noisyfox.THEngine.Performers.Player;

/**
 * @ClassName: Bullet_Enemy_1
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-13 下午3:16:20
 * 
 */
public class Bullet_Enemy_1 extends Bullet_Enemy {
	float speed = 0f;

	@Override
	protected void onCreate() {

		Sprite bulletSprite = new Sprite();
		bulletSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.bullet_enemy_1, false);
		bulletSprite.setOffset(bulletSprite.getWidth() / 2,
				bulletSprite.getHeight() / 2);

		this.bindSprite(bulletSprite);

		GraphicCollision co = new GraphicCollision();
		co.addCircle(0, 0, 10, true);
		this.bindCollisionMask(co);

		this.requireCollisionDetection(Player.class);
	}

	@Override
	protected void onStep() {
		this.setPosition(this.getX(), this.getY() + speed);
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

	@Override
	protected void onCollisionWith(Performer target) {
		if (Player.class.isInstance(target)) {
			this.dismiss();
		}
	}

	public Bullet_Enemy_1(int x, int y, float speed) {
		this.perform(Stage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
		this.speed = speed;
	}

}
