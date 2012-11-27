/**
 * FileName:     Bullet_Player_Normal.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-17 下午7:19:29
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-17      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers.Bullets;

import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Performers.Explosion;
import org.foxteam.noisyfox.THEngine.Performers.Hitable;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy;

/**
 * @ClassName: Bullet_Player_Normal
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-17 下午7:19:29
 * 
 */
public class Bullet_Player_Normal extends Bullet_Player {

	@Override
	protected void onCreate() {

		FGSprite bulletSprite = new FGSprite();
		bulletSprite.bindFrames(GlobalResources.FRAMES_BULLET_PLAYER_NORMAL);
		bulletSprite.setOffset(bulletSprite.getWidth() / 2 + 1, 0);

		this.bindSprite(bulletSprite);

		FGGraphicCollision co = new FGGraphicCollision();
		co.addCircle(0, 8, 5, true);
		this.bindCollisionMask(co);

		this.setDamage(10);

		this.motion_set(90, 300f / FGStage.getSpeed());
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
	}

	public Bullet_Player_Normal(int x, int y) {
		this.perform(FGStage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
	}

	@Override
	public void hitOn(Hitable target) {
		if (Enemy.class.isInstance(target)) {
			new Explosion(
					GlobalResources.FRAMES_EXPLOSION_BULLET_PLAYER_NORMAL, 1,
					0.3f, (int) this.getX(), (int) this.getY()
							- this.getSprite().getOffsetY(), -1);
			this.dismiss();
		}
	}

}
