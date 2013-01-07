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
package org.foxteam.noisyfox.THEngine.Section.Bullets;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Hitable;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Player;

/**
 * @ClassName: Bullet_Enemy_1
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-13 下午3:16:20
 * 
 */
public class Bullet_Enemy_1 extends Bullet_Enemy {
	float mySpeed = 0f;
	float myDirection = 0f;

	@Override
	protected void onCreate() {
		this.motion_set(myDirection, mySpeed);
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
	}

	public Bullet_Enemy_1() {
		FGSprite bulletSprite = new FGSprite();
		bulletSprite.bindFrames(GlobalResources.FRAMES_BULLET_ENEMY_1);
		bulletSprite.setOffset(bulletSprite.getWidth() / 2,
				bulletSprite.getHeight() / 2);

		this.bindSprite(bulletSprite);

		FGGraphicCollision co = new FGGraphicCollision();
		co.addCircle(0, 0, 10, true);
		this.bindCollisionMask(co);

		this.setDamage(11);
	}

	@Override
	public void hitOn(Hitable target) {
		if (Player.class.isInstance(target)) {
			this.dismiss();
		}
	}

	@Override
	public void recycleBullet() {
		mySpeed = 0f;
		myDirection = 0f;
	}

	@Override
	public void createBullet(int x, int y, float speed, float direction,
			float... extraConfig) {
		this.perform(FGStage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
		mySpeed = speed;
		myDirection = direction;
	}

}
