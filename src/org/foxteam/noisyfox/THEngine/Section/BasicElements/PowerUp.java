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
package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import org.foxteam.noisyfox.FoxGaming.Core.FGMathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;

/**
 * @ClassName: PowerUp
 * @Description: 火力升级
 * @author: Noisyfox
 * @date: 2012-8-17 下午7:07:44
 * 
 */
public abstract class PowerUp extends Bullet {

	int myType = 0;
	private int typeNumber = 1;
	private float typeInterval = 0.0f;
	private boolean isManualSpeed = false;
	private float myVSpeed = 0f;
	private float myHspeed = 0f;

	@Override
	protected void onCreate() {
		if (this.typeInterval != 0) {
			this.setAlarm(1, (int) (FGStage.getSpeed() * this.typeInterval),
					true);
			this.startAlarm(1);
		} else {
			this.stopAlarm(1);
		}

		if (typeNumber != 1) {
			myType = FGMathsHelper.random(0, typeNumber - 1);
			onTypeChange(myType);
		}

		if (isManualSpeed) {
			this.motion_setSpeed(myHspeed, myVSpeed);

		} else {
			this.setAlarm(0, (int) (FGStage.getSpeed() * 3.0f), true);
			this.startAlarm(0);

			this.motion_set(FGMathsHelper.random(0, 359),
					50f / FGStage.getSpeed());
		}
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {
			this.motion_set(FGMathsHelper.random(0, 359),
					50f / FGStage.getSpeed());

		} else if (whichAlarm == 1) {
			if (typeNumber != 1) {
				if (++myType > typeNumber - 1) {
					myType = 0;
				}
				onTypeChange(myType);
			}

		}
	}

	@Override
	protected void onStep() {
		if (!isManualSpeed) {
			// 判断当前位置
			int left = getSprite().getOffsetX() + 20;
			int top = getSprite().getOffsetY() + 20;
			int right = FGStage.getCurrentStage().getWidth()
					+ (getSprite() == null ? 0
							: (getSprite().getOffsetX() - getSprite()
									.getWidth())) - 20;
			int bottom = FGStage.getCurrentStage().getHeight()
					+ (getSprite() == null ? 0
							: (getSprite().getOffsetY() - getSprite()
									.getHeight())) - 20;

			if (getX() < left) {
				if (getY() < top && direction <= 270) {
					this.motion_set(FGMathsHelper.random(271, 359),
							50f / FGStage.getSpeed());
				} else if (getY() > bottom && direction >= 90) {
					this.motion_set(FGMathsHelper.random(1, 89),
							50f / FGStage.getSpeed());
				} else if (direction >= 90 && direction <= 270) {
					this.motion_set(FGMathsHelper.degreeIn360(FGMathsHelper
							.random(-89, 89)), 50f / FGStage.getSpeed());
				}
			} else if (getX() > right) {
				if (getY() < top && (direction <= 180 || direction >= 270)) {
					this.motion_set(FGMathsHelper.random(181, 269),
							50f / FGStage.getSpeed());
				} else if (getY() > bottom
						&& (direction <= 90 || direction >= 180)) {
					this.motion_set(FGMathsHelper.random(91, 179),
							50f / FGStage.getSpeed());
				} else if (direction <= 90 || direction >= 270) {
					this.motion_set(FGMathsHelper.random(91, 269),
							50f / FGStage.getSpeed());
				}
			} else if (getY() < top && direction <= 180) {
				this.motion_set(FGMathsHelper.random(181, 359),
						50f / FGStage.getSpeed());
			} else if (getY() > bottom && direction >= 180) {
				this.motion_set(FGMathsHelper.random(1, 179),
						50f / FGStage.getSpeed());
			}
		}
	}

	public PowerUp() {
	}

	@Override
	public boolean isOutOfStage() {
		return super.isOutOfStage()
				&& this.getY() > FGStage.getCurrentStage().getHeight();
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
		this.bindCollisionMask(null);
	}

	public final void defineTypes(int typeNumber, float intervalTime) {
		if (typeNumber <= 0) {
			throw new IllegalArgumentException(
					"typenumber must be larger than zero!");
		}
		if (intervalTime < 0) {
			throw new IllegalArgumentException(
					"intervalTime can't be smaller than zero!");
		}
		this.typeNumber = typeNumber;
		this.typeInterval = intervalTime;

		if (this.typeInterval != 0) {
			this.setAlarm(1, (int) (FGStage.getSpeed() * this.typeInterval),
					true);
			this.startAlarm(1);
		} else {
			this.stopAlarm(1);
		}
	}

	public abstract void onTypeChange(int type);

	public final void setMovement(boolean randomSpeed, float vspeed,
			float hspeed) {
		this.isManualSpeed = !randomSpeed;
		this.myHspeed = hspeed;
		this.myVSpeed = vspeed;
	}

	@Override
	public void createBullet(int x, int y, float speed, float direction,
			float... extraConfig) {
		this.perform(FGStage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);

	}

	@Override
	public void recycleBullet() {
		// TODO Auto-generated method stub

	}

}
