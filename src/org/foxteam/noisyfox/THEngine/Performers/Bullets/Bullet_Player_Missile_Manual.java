/**
 * FileName:     Bullet_Player_Missile_Manual.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-23 下午10:16:38
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-23      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers.Bullets;

import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.*;
import org.foxteam.noisyfox.THEngine.Performers.Explosion;
import org.foxteam.noisyfox.THEngine.Performers.Hitable;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy;

/**
 * @ClassName: Bullet_Player_Missile_Manual
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-23 下午10:16:38
 * 
 */
public class Bullet_Player_Missile_Manual extends Bullet_Player {

	FGParticleSystem pSystem = new FGParticleSystem();
	FGParticleType pType = new FGParticleType();
	FGParticleEmitter pEmitter = new FGParticleEmitter();

	@Override
	protected void onCreate() {

		FGSprite particleSprite = new FGSprite();
		particleSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.particle_missilesmoke,
				false);
		particleSprite.setOffset(3, 3);
		pType.setSprite(particleSprite);
		pType.setLifeTime(15, 25);
		pType.setSize(0.8, 1.5, -0.03, 0.01);
		pType.setOrientation(0, 359.9f, 0, 1, false);
		pType.setAlpha(1.0, 0.0);
		pEmitter.stream(pType, -2);
		pSystem.bindParticleEmitter(pEmitter);

		FGSprite bulletSprite = new FGSprite();
		bulletSprite
				.loadFromBitmap(
						org.foxteam.noisyfox.THEngine.R.drawable.bullet_player_missile_manual,
						false);
		bulletSprite.setOffset(4, 10);
		this.bindSprite(bulletSprite);

		FGGraphicCollision co = new FGGraphicCollision();
		int[][] vertex1 = { { 0, -10 }, { -2, -8 }, { -3, 0 }, { 3, 0 },
				{ 2, -8 } };
		co.addPolygon(vertex1, true);
		this.bindCollisionMask(co);

		this.setDamage(10);

	}

	@Override
	protected void onDraw() {
		super.onDraw();
		pSystem.draw(getCanvas());
	}

	@Override
	protected void onStep() {
		super.onStep();
		pEmitter.setRegion((int) getX() - 2, (int) getY(), (int) getX() + 2,
				(int) getY() + 2, FGParticleRegionShape.rectangle,
				FGParticleRegionDistribution.linear);
		pSystem.update();
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
		this.bindCollisionMask(null);
	}

	@Override
	public void hitOn(Hitable target) {
		if (Enemy.class.isInstance(target)) {
			new Explosion(
					org.foxteam.noisyfox.THEngine.R.drawable.explosion_missile_small,
					5, 1, 0.3f, (int) this.getX(), (int) this.getY()
							- this.getSprite().getOffsetY(), -1);
			this.dismiss();
		}
	}

	public Bullet_Player_Missile_Manual(int x, int y) {
		this.perform(FGStage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
	}

}
