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

import org.foxteam.noisyfox.FoxGaming.Core.Stage;
import org.foxteam.noisyfox.FoxGaming.G2D.GraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;
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

		Sprite bulletSprite = new Sprite();
		bulletSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.bullet_player_normal,
				false);
		bulletSprite.setOffset(bulletSprite.getWidth() / 2 + 1, 0);

		this.bindSprite(bulletSprite);

		GraphicCollision co = new GraphicCollision();
		co.addCircle(0, 8, 5, true);
		this.bindCollisionMask(co);

		this.setDamage(10);

		this.motion_set(90, 300f / Stage.getSpeed());
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
	}

	public Bullet_Player_Normal(int x, int y) {
		this.perform(Stage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
	}

	@Override
	public void hitOn(Hitable target) {
		if (Enemy.class.isInstance(target)) {
			new Explosion(
					org.foxteam.noisyfox.THEngine.R.drawable.explosion_bullet_player_normal,
					10, 0.3f, (int) this.getX(), (int) this.getY()
							- this.getSprite().getOffsetY());
			this.dismiss();
		}
	}

}
