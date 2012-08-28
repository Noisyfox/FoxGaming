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

import org.foxteam.noisyfox.FoxGaming.Core.Stage;
import org.foxteam.noisyfox.FoxGaming.G2D.GraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;
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

	@Override
	protected void onCreate() {

		Sprite bulletSprite = new Sprite();
		bulletSprite
				.loadFromBitmap(
						org.foxteam.noisyfox.THEngine.R.drawable.bullet_player_missile_manual,
						false);
		bulletSprite.setOffset(4, 10);
		this.bindSprite(bulletSprite);

		GraphicCollision co = new GraphicCollision();
		int[][] vertex1 = { { 0, -10 }, { -2, -8 }, { -3, 0 }, { 3, 0 },
				{ 2, -8 } };
		co.addPolygon(vertex1, true);
		this.bindCollisionMask(co);

		this.setDamage(10);

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
		this.perform(Stage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
	}

}
