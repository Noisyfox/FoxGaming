/**
 * FileName:     Enemy_Fly.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-22 下午4:48:57
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-22      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers.Enemys;

import org.foxteam.noisyfox.FoxGaming.Core.GamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.MathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.Performer;
import org.foxteam.noisyfox.FoxGaming.Core.ScreenPlay;
import org.foxteam.noisyfox.FoxGaming.Core.Stage;
import org.foxteam.noisyfox.FoxGaming.G2D.Convertor;
import org.foxteam.noisyfox.FoxGaming.G2D.GraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;
import org.foxteam.noisyfox.FoxGaming.G2D.SpriteConvertor;
import org.foxteam.noisyfox.THEngine.Performers.Bullet;
import org.foxteam.noisyfox.THEngine.Performers.Explosion;
import org.foxteam.noisyfox.THEngine.Performers.Player;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Enemy_1;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Player;

import android.graphics.Canvas;

/**
 * @ClassName: Enemy_Fly
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-22 下午4:48:57
 * 
 */
public class Enemy_Fly extends EnemyInAir {

	Convertor GCConvertor = new Convertor();
	SpriteConvertor SpConvertor = new SpriteConvertor();
	boolean canFire = false;// 是否发射子弹
	int inX = 0;// 从哪个位置进入屏幕
	int maxY = 0;// 进入屏幕的深度
	float myDirection = 270;// 底部的朝向，同时也是发射子弹的方向
	ScreenPlay myMovement = new ScreenPlay();

	static final float STAYTIME = 2.0f;// 进入屏幕后停留的时间，单位秒

	@Override
	protected void onCreate() {
		Sprite flySprite = new Sprite();
		flySprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.enemy_fly, 10, 1,
				false);
		flySprite
				.setOffset(flySprite.getWidth() / 2, flySprite.getHeight() / 2);
		this.bindSprite(flySprite);

		this.setAlarm(0, (int) (Stage.getSpeed() * 0.1f), true);// 播放动画
		this.startAlarm(0);
		if (canFire) {
			this.setAlarm(1, (int) (Stage.getSpeed() * 2.5f), true);// 发射子弹
			this.startAlarm(1);
		}

		GraphicCollision co = new GraphicCollision();
		co.addCircle(0, 0, 12, true);
		co.addCircle(15, -5, 7, true);
		co.addCircle(-15, -5, 7, true);
		co.addCircle(12, 5, 5, true);
		co.addCircle(-12, 5, 5, true);
		this.bindCollisionMask(co);

		this.setHP(10);

		this.requireCollisionDetection(Bullet_Player.class);

		this.setPosition(inX, -flySprite.getHeight() + flySprite.getOffsetY());

		float mySpeed = 90f / Stage.getSpeed();

		myMovement.moveTowards(270, mySpeed);
		myMovement
				.wait((int) ((flySprite.getHeight() - flySprite.getOffsetY() + maxY) / mySpeed));
		myMovement.stop();
		myMovement.wait((int) (STAYTIME * Stage.getSpeed()));
		myMovement.moveTowards(90, mySpeed);
		this.playAScreenPlay(myMovement);
	}

	@Override
	protected void onStep() {
		if (myMovement.getRemainNumber() > 0) {
			if (Stage.getPerformersByClass(Player.class).length > 0) {
				Performer p = Stage.getPerformersByClass(Player.class)[0];
				myDirection = MathsHelper.degreeIn360(MathsHelper
						.point_direction(getX(), getY(), p.getX(), p.getY()));
			}
		} else {
			this.stopAlarm(1);

			if (myDirection != 90) {
				float rDir = MathsHelper.directionTo(myDirection, 90);
				if (Math.abs(rDir) <= 5) {
					myDirection = MathsHelper.degreeIn360(myDirection + rDir);
				} else {
					myDirection = MathsHelper.degreeIn360(myDirection
							+ (rDir > 0 ? 5 : -5));
				}
			}
		}

		SpConvertor.setRotation(myDirection - 270);
		GCConvertor.setRotation(myDirection - 270);

		if (this.getCollisionMask() != null) {
			this.getCollisionMask().applyConvertor(GCConvertor);
		}
	}

	@Override
	protected void onDraw() {
		Canvas c = this.getCanvas();
		this.getSprite().draw(c, (int) this.getX(), (int) this.getY(),
				SpConvertor);
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {// 播放动画
			this.getSprite().nextFrame();

		} else if (whichAlarm == 1) {// 发射子弹
			Bullet b = new Bullet_Enemy_1(
					(int) (this.getX() + MathsHelper.lengthdir_x(this
							.getSprite().getHeight()
							- this.getSprite().getOffsetY(), myDirection)),
					(int) (this.getY() - MathsHelper.lengthdir_y(this
							.getSprite().getHeight()
							- this.getSprite().getOffsetY(), myDirection)),
					myDirection, 110f / Stage.getSpeed());
			b.setDepth(this.getDepth() + 1);

		}
	}

	@Override
	public boolean isOutOfStage() {
		return super.isOutOfStage() && myMovement.getRemainNumber() == 0;
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
		this.bindCollisionMask(null);
	}

	@Override
	protected void Explosion(Bullet bullet) {
		new Explosion(
				org.foxteam.noisyfox.THEngine.R.drawable.explosion_normal, 7,
				1, 0.5f, (int) this.getX(), (int) this.getY());
		this.dismiss();

		this.bindCollisionMask(null);

		GamingThread.score += 14;
	}

	@Override
	public void createEnemy(int x, int y, int... extraConfig) {
		this.perform(Stage.getCurrentStage().getStageIndex());
		canFire = extraConfig[0] == 0;
		maxY = extraConfig[1];
		inX = x;
	}

}
