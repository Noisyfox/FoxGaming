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
package org.foxteam.noisyfox.THEngine.Performers.Enemys;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;
import org.foxteam.noisyfox.THEngine.Performers.Bullet;
import org.foxteam.noisyfox.THEngine.Performers.Explosion;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Enemy_1;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Player;

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

		speed = 20f / Stage.getSpeed();

		this.motion_set(frmL ? 0 : -180, speed);
		this.motion_add(270, Stage.getCurrentBackground().getVSpeed());

		this.setAlarm(0, (int) (Stage.getSpeed() * 3f), true);// 发射子弹
		this.startAlarm(0);

		this.setAlarm(1, (int) (Stage.getSpeed() * 0.1f), true);// 播放动画
		this.startAlarm(1);

		GraphicCollision co = new GraphicCollision();
		if (frmL) {
			sc.setScale(-1, 1);

			co.addCircle(3, -5, 11, true);
			co.addCircle(0, -11, 9, true);
			co.addCircle(2, 5, 11, true);
			co.addCircle(15, 1, 8, true);
			co.addCircle(-11, 1, 8, true);
			co.addCircle(20, -7, 4, true);
			co.addCircle(-18, -6, 4, true);
			co.addCircle(5, 16, 5, true);
		} else {
			co.addCircle(-3, -5, 11, true);
			co.addCircle(0, -11, 9, true);
			co.addCircle(-2, 5, 11, true);
			co.addCircle(-15, 1, 8, true);
			co.addCircle(11, 1, 8, true);
			co.addCircle(-20, -7, 4, true);
			co.addCircle(18, -6, 4, true);
			co.addCircle(-5, 16, 5, true);
		}
		this.bindCollisionMask(co);

		this.setHP(10);

		this.requireCollisionDetection(Bullet_Player.class);
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

		this.getCollisionMask().draw(this.getCanvas());
	}

	@Override
	protected void onCollisionWith(Performer target) {
		if (Bullet_Player.class.isInstance(target)) {
			this.hitBy((Bullet) target);
		}
	}

	@Override
	protected void Explosion(Bullet bullet) {
		new Explosion(
				org.foxteam.noisyfox.THEngine.R.drawable.explosion_normal, 7,
				0.5f, (int) this.getX(), (int) this.getY());
		this.dismiss();

		GamingThread.score += 100;
	}

	public Enemy_Duck(int y, boolean comeFromLeft) {
		inY = y;
		frmL = comeFromLeft;
	}

}
