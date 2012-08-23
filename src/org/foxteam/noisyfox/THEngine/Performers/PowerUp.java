/**
 * FileName:     PowerUp.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-17 下午7:07:44
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-17      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.MathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.Stage;
import org.foxteam.noisyfox.FoxGaming.G2D.GraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;

/**
 * @ClassName: PowerUp
 * @Description: 火力升级
 * @author: Noisyfox
 * @date: 2012-8-17 下午7:07:44
 * 
 */
public abstract class PowerUp extends Bullet {

	@Override
	protected void onCreate() {
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
				+ (getSprite() == null ? 0
						: (getSprite().getOffsetX() - getSprite().getWidth()))
				- 20;
		int bottom = Stage.getCurrentStage().getHeight()
				+ (getSprite() == null ? 0
						: (getSprite().getOffsetY() - getSprite().getHeight()))
				- 20;

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

	public PowerUp(int x, int y) {
		this.perform(Stage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
	}

}
