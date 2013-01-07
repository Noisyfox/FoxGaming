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
package org.foxteam.noisyfox.THEngine.Section.Bullets;

import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.*;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Explosion;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Hitable;
import org.foxteam.noisyfox.THEngine.Section.Enemys.Enemy;

/**
 * @ClassName: Bullet_Player_Missile_Manual
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-23 下午10:16:38
 * 
 */
public class Bullet_Player_Missile_Manual extends Bullet_Player {

	static FGParticleSystem pSystem = new FGParticleSystem();
	FGParticleEmitter pEmitter = new FGParticleEmitter();

	@Override
	protected void onCreate() {
		FGStage.getCurrentStage().managedParticleSystem_requireManaged(pSystem,
				depth + 1);
		pSystem.bindParticleEmitter(pEmitter);
	}

	@Override
	protected void onDraw() {
		super.onDraw();
	}

	@Override
	protected void onStep() {
		super.onStep();
		pEmitter.setRegion((int) getX() - 2, (int) getY(), (int) getX() + 2,
				(int) getY() + 2, FGParticleRegionShape.rectangle,
				FGParticleRegionDistribution.linear);
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
		this.bindCollisionMask(null);
	}

	@Override
	public void hitOn(Hitable target) {
		if (Enemy.class.isInstance(target)) {
			new Explosion(GlobalResources.FRAMES_EXPLOSION_MISSILE_SMALL, 1,
					0.3f, (int) this.getX(), (int) this.getY()
							- this.getSprite().getOffsetY(), -1);
			this.dismiss();
		}
	}

	public Bullet_Player_Missile_Manual() {

		pEmitter.stream(GlobalResources.PARTICLE_TYPE_MILLSILSMOKE, -1);

		FGSprite bulletSprite = new FGSprite();
		bulletSprite
				.bindFrames(GlobalResources.FRAMES_BULLET_PLAYER_MISSILE_MANUAL);
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
	public void createBullet(int x, int y, float speed, float direction,
			float... extraConfig) {
		this.perform(FGStage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
	}

	@Override
	public void recycleBullet() {
		pSystem.unbindParticleEmitter(pEmitter);
	}

}
