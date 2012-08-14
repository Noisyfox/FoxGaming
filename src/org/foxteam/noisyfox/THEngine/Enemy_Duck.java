/**
 * FileName:     Enemy_Duck.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-12 下午7:20:17
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-12      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;

import android.graphics.Canvas;

/**
 * @ClassName: Enemy_Duck
 * @Description: 从屏幕的一侧水平移动到另一侧，均匀抛下静止子弹
 * @author: Noisyfox
 * @date: 2012-8-12 下午7:20:17
 * 
 */
public class Enemy_Duck extends Enemy {

	private int inY = 0;
	private boolean frmL = false;

	private float speed = 0;

	SpriteConvertor sc = new SpriteConvertor();

	@Override
	protected void onCreate() {
		Sprite duckSprite = new Sprite();
		duckSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.enemy_duck, 10, 1,
				false);
		duckSprite.setOffset(duckSprite.getWidth() / 2,
				duckSprite.getHeight() / 2);
		this.bindSprite(duckSprite);

		this.setPosition(
				frmL ? -(duckSprite.getWidth() - duckSprite.getOffsetX())
						: Stage.getCurrentStage().getWidth()
								+ duckSprite.getOffsetX(), inY);

		speed = 50f / Stage.getSpeed();

		this.setAlarm(0, (int) (Stage.getSpeed() * 1f), true);// 发射子弹
		this.startAlarm(0);

		this.setAlarm(1, (int) (Stage.getSpeed() * 0.1f), true);// 播放动画
		this.startAlarm(1);

		if (frmL) {
			sc.setScale(-1, 1);
		}
	}

	@Override
	protected void onStep() {
		this.setPosition(this.getX() + (frmL ? speed : -speed), this.getY()
				+ Stage.getCurrentBackground().getVSpeed());
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {// 发射子弹
			Bullet b = new Bullet_Enemy_1((int) this.getX(), (int) this.getY()
					- this.getSprite().getOffsetY()
					+ this.getSprite().getHeight(), Stage
					.getCurrentBackground().getVSpeed());
			b.setDepth(this.getDepth() + 1);
		} else if (whichAlarm == 1) {// 播放动画
			this.getSprite().nextFrame();
		}
	}

	@Override
	public boolean isOutOfStage() {
		return super.isOutOfStage()
				&& ((frmL && this.getX() > Stage.getCurrentStage().getWidth() / 2) || (!frmL && this
						.getX() < Stage.getCurrentStage().getWidth() / 2));
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
	}

	@Override
	protected void onDraw() {
		Canvas c = this.getCanvas();
		if (!frmL) {
			this.getSprite().draw(c, (int) this.getX(), (int) this.getY());
		} else {
			this.getSprite().draw(c, (int) this.getX(), (int) this.getY(), sc);
		}
	}

	public Enemy_Duck(int y, boolean comeFromLeft) {
		inY = y;
		frmL = comeFromLeft;
	}

}