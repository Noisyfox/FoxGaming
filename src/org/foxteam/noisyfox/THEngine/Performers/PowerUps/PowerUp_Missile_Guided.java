/**
 * FileName:     PowerUp_Missile_Guided.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-19 下午4:46:38
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-19      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers.PowerUps;

import org.foxteam.noisyfox.FoxGaming.G2D.GraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;
import org.foxteam.noisyfox.THEngine.Performers.Hitable;
import org.foxteam.noisyfox.THEngine.Performers.Player;
import org.foxteam.noisyfox.THEngine.Performers.PowerUp;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Player_Missile_Guided;

/**
 * @ClassName: PowerUp_Missile_Guided
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-19 下午4:46:38
 * 
 */
public class PowerUp_Missile_Guided extends PowerUp {

	public PowerUp_Missile_Guided(int x, int y) {
		super(x, y);
		Sprite bulletSprite = new Sprite();
		bulletSprite
				.loadFromBitmap(
						org.foxteam.noisyfox.THEngine.R.drawable.powerup_missile_guided,
						false);
		bulletSprite.setOffset(bulletSprite.getWidth() / 2,
				bulletSprite.getHeight() / 2);
		this.bindSprite(bulletSprite);

		GraphicCollision co = new GraphicCollision();
		co.addRectangle(-bulletSprite.getWidth() / 2,
				-bulletSprite.getHeight() / 2, bulletSprite.getWidth(),
				bulletSprite.getHeight());
		this.bindCollisionMask(co);
	}

	@Override
	public void hitOn(Hitable target) {
		if (Player.class.isInstance(target)) {
			((Player) target).getPowerUp(Bullet_Player_Missile_Guided.class);
		}
		this.dismiss();
		this.bindCollisionMask(null);
	}

}
