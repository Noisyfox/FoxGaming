/**
 * FileName:     Bullet_Enemy_3.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-29 上午10:52:00
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-29      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Section.Bullets;

import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGConvertor;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Hitable;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Player;

/**
 * @ClassName: Bullet_Enemy_3
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-29 上午10:52:00
 * 
 */
public class Bullet_Enemy_3 extends Bullet_Enemy {
	float mySpeed = 0f;
	float myDirection = 0f;
	FGConvertor GCConvertor = new FGConvertor();
	FGSpriteConvertor SpConvertor = new FGSpriteConvertor();

	@Override
	protected void onDraw() {
		if (this.getSprite() != null) {
			this.getSprite().draw((int) getX(), (int) getY(), SpConvertor);
		}
	}

	@Override
	protected void onCreate() {

		SpConvertor.setRotation(myDirection - 90);
		GCConvertor.setRotation(myDirection - 90);
		this.getCollisionMask().applyConvertor(GCConvertor);

		this.motion_set(myDirection, mySpeed);
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
	}

	@Override
	public void hitOn(Hitable target) {
		if (Player.class.isInstance(target)) {
			this.dismiss();
		}
	}

	public Bullet_Enemy_3() {

		FGSprite bulletSprite = new FGSprite();
		bulletSprite.bindFrames(GlobalResources.FRAMES_BULLET_ENEMY_3);
		bulletSprite.setOffset(bulletSprite.getWidth() / 2,
				bulletSprite.getHeight() / 2);
		this.bindSprite(bulletSprite);

		FGGraphicCollision co = new FGGraphicCollision();
		int[][] vertex1 = { { 0, -6 }, { -4, -4 }, { -5, -2 }, { -6, 2 },
				{ -6, 9 }, { 6, 9 }, { 6, 2 }, { 5, -2 }, { 4, -4 } };
		co.addPolygon(vertex1, true);
		this.bindCollisionMask(co);

		this.setDamage(11);
	}

	@Override
	public void createBullet(int x, int y, float speed, float direction,
			float... extraConfig) {
		this.perform(FGStage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
		mySpeed = speed;
		myDirection = direction;

	}

	@Override
	public void recycleBullet() {
		mySpeed = 0f;
		myDirection = 0f;
	}

}
