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
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Player_Missile_Manual;

/**
 * @ClassName: PowerUp_Missile_Guided
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-19 下午4:46:38
 * 
 */
public class PowerUp_Missile extends PowerUp {

	private int missileType = 0;// 导弹种类，0跟踪导弹，1非跟踪导弹
	Sprite missileSprite = new Sprite();

	public PowerUp_Missile(int x, int y) {
		super(x, y);

		missileSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.powerup_missile, 2, 1,
				false);
		missileSprite.setOffset(missileSprite.getWidth() / 2,
				missileSprite.getHeight() / 2);
		this.bindSprite(missileSprite);

		GraphicCollision co = new GraphicCollision();
		co.addRectangle(-missileSprite.getWidth() / 2,
				-missileSprite.getHeight() / 2, missileSprite.getWidth(),
				missileSprite.getHeight());
		this.bindCollisionMask(co);

		this.defineTypes(2, 2.5f);
	}

	@Override
	public void hitOn(Hitable target) {
		if (Player.class.isInstance(target)) {
			((Player) target)
					.getPowerUp(missileType == 0 ? Bullet_Player_Missile_Guided.class
							: Bullet_Player_Missile_Manual.class);

			this.dismiss();
			this.bindCollisionMask(null);
		}
	}

	public void onTypeChange(int type) {
		missileType = type;
		missileSprite.setCurrentFrame(missileType);
	}

}
