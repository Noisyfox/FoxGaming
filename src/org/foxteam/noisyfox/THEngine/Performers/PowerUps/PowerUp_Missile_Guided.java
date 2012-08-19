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

import org.foxteam.noisyfox.FoxGaming.Core.MathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.Stage;
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
	}

	@Override
	protected void onCreate() {
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

		this.setAlarm(0, (int) (Stage.getSpeed() * 3.0f), true);
		this.startAlarm(0);

		this.motion_set(MathsHelper.random(0, 359), 50f / Stage.getSpeed());
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {
			this.motion_set(MathsHelper.random(0, 359), 50f / Stage.getSpeed());
		}
	}

	@Override
	protected void onStep() {
		// 判断当前位置
		int left = getSprite().getOffsetX() + 20;
		int top = getSprite().getOffsetY() + 20;
		int right = Stage.getCurrentStage().getWidth()
				+ getSprite().getOffsetX() - getSprite().getWidth() - 20;
		int bottom = Stage.getCurrentStage().getHeight()
				+ getSprite().getOffsetY() - getSprite().getHeight() - 20;

		if (getX() < left) {
			if (getY() < top && direction <= 270) {
				this.motion_set(MathsHelper.random(271, 359),
						50f / Stage.getSpeed());
			} else if (getY() > bottom && direction >= 90) {
				this.motion_set(MathsHelper.random(1, 89),
						50f / Stage.getSpeed());
			} else if (direction >= 90 && direction <= 270) {
				this.motion_set(
						MathsHelper.degreeIn360(MathsHelper.random(-89, 89)),
						50f / Stage.getSpeed());
			}
		} else if (getX() > right) {
			if (getY() < top && (direction <= 180 || direction >= 270)) {
				this.motion_set(MathsHelper.random(181, 269),
						50f / Stage.getSpeed());
			} else if (getY() > bottom && (direction <= 90 || direction >= 180)) {
				this.motion_set(MathsHelper.random(91, 179),
						50f / Stage.getSpeed());
			} else if (direction <= 90 || direction >= 270) {
				this.motion_set(MathsHelper.random(91, 269),
						50f / Stage.getSpeed());
			}
		} else if (getY() < top && direction <= 180) {
			this.motion_set(MathsHelper.random(181, 359),
					50f / Stage.getSpeed());
		} else if (getY() > bottom && direction >= 180) {
			this.motion_set(MathsHelper.random(1, 179), 50f / Stage.getSpeed());
		}
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
