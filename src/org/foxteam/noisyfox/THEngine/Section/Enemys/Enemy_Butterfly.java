/**
 * FileName:     Enemy_Butterfly.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-23 下午3:39:45
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-23      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Section.Enemys;

import org.foxteam.noisyfox.FoxGaming.Core.FGEventsListener;
import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGMathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Bullet;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.BulletPool;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Explosion;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Player;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.PowerUp;
import org.foxteam.noisyfox.THEngine.Section.Bullets.Bullet_Enemy_1;
import org.foxteam.noisyfox.THEngine.Section.Bullets.Bullet_Player;
import org.foxteam.noisyfox.THEngine.Section.PowerUps.PowerUp_Missile;

/**
 * @ClassName: Enemy_Butterfly
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-23 下午3:39:45
 * 
 */
public class Enemy_Butterfly extends EnemyInAir {

	int inX = 0;// 从哪个位置进入屏幕
	FGSprite butterflySprite;

	@Override
	public void prepareEnemy() {
		butterflySprite = new FGSprite();
		butterflySprite.bindFrames(GlobalResources.FRAMES_ENEMY_BUTTERFLY);
		butterflySprite.setOffset(butterflySprite.getWidth() / 2,
				butterflySprite.getHeight() / 2);
		this.bindSprite(butterflySprite);

		FGGraphicCollision co = new FGGraphicCollision();
		co.addCircle(-2, 4, 15, true);
		co.addCircle(-15, 14, 6, true);
		co.addCircle(13, 14, 6, true);
		co.addCircle(-11, 22, 4, true);
		co.addCircle(14, 22, 4, true);
		co.addCircle(-15, -6, 10, true);
		co.addCircle(15, -8, 10, true);
		this.bindCollisionMask(co);

		this.setHP(100);

		this.requireCollisionDetection(Bullet_Player.class);
	}

	@Override
	protected void onCreate() {

		this.setAlarm(0, (int) (FGStage.getSpeed() * 0.3f), true);// 播放动画
		this.startAlarm(0);

		this.setPosition(inX,
				-butterflySprite.getHeight() + butterflySprite.getOffsetY());

		requireEventFeature(FGEventsListener.EVENT_ONSTEP
				| FGEventsListener.EVENT_ONALARM
				| FGEventsListener.EVENT_ONOUTOFSTAGE);

	}

	@Override
	protected void onStep() {
		if (getY() < FGStage.getCurrentStage().getHeight() / 5) {
			motion_set(270, 90f / FGStage.getSpeed());
			this.setAlarm(1, (int) (FGStage.getSpeed() * 2f), true);// 发射子弹
			this.startAlarm(1);

		} else if (getY() > FGStage.getCurrentStage().getHeight() / 2) {
			motion_set(270, 90f / FGStage.getSpeed());
			this.stopAlarm(1);

		} else {
			if (Math.abs(getX() - inX) > 50) {
				if (inX < FGStage.getCurrentStage().getWidth() / 2) {
					this.motion_setSpeed(-15f / FGStage.getSpeed(),
							4f / FGStage.getSpeed());
				} else {
					this.motion_setSpeed(15f / FGStage.getSpeed(),
							4f / FGStage.getSpeed());
				}
			} else {
				if (inX < FGStage.getCurrentStage().getWidth() / 2) {
					if (getX() <= inX) {
						this.motion_setSpeed(15f / FGStage.getSpeed(),
								4f / FGStage.getSpeed());
					}
				} else {
					if (getX() >= inX) {
						this.motion_setSpeed(-15f / FGStage.getSpeed(),
								4f / FGStage.getSpeed());
					}
				}
			}
		}
	}

	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {// 播放动画
			this.getSprite().nextFrame();

		} else if (whichAlarm == 1) {// 发射子弹
			if (FGStage.getPerformersByClass(Player.class).length > 0) {
				FGPerformer p = FGStage.getPerformersByClass(Player.class)[0];
				float playerDir = FGMathsHelper.degreeIn360(FGMathsHelper
						.point_direction(getX(), getY(), p.getX(), p.getY()));

				Bullet b = BulletPool.obtainBullet(Bullet_Enemy_1.class);
				b.createBullet((int) this.getX(), (int) this.getY(),
						110f / FGStage.getSpeed(), playerDir);
				b.setDepth(this.getDepth() - 1);

				b = BulletPool.obtainBullet(Bullet_Enemy_1.class);
				b.createBullet((int) this.getX(), (int) this.getY(),
						110f / FGStage.getSpeed(),
						FGMathsHelper.degreeIn360(playerDir + 30));
				b.setDepth(this.getDepth() - 1);

				b = BulletPool.obtainBullet(Bullet_Enemy_1.class);
				b.createBullet((int) this.getX(), (int) this.getY(),
						110f / FGStage.getSpeed(),
						FGMathsHelper.degreeIn360(playerDir - 30));
				b.setDepth(this.getDepth() - 1);
			}

		}
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

	@Override
	protected void Explosion(Bullet bullet) {
		new Explosion(GlobalResources.FRAMES_EXPLOSION_NORMAL, 1, 0.5f,
				(int) this.getX(), (int) this.getY(), -1);

		PowerUp p = (PowerUp) BulletPool.obtainBullet(PowerUp_Missile.class);
		p.createBullet((int) getX(), (int) getY(), 0, 0);
		p.setDepth(getDepth() + 1);

		this.dismiss();

		this.bindCollisionMask(null);

		FGGamingThread.score += 100;
	}

	@Override
	public void createEnemy(int x, int y, int... extraConfig) {
		this.perform(FGStage.getCurrentStage().getStageIndex());
		inX = x;
	}

}
