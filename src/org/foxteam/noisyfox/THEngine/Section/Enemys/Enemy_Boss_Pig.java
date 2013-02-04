/**
 * FileName:     Enemy_Boss_Pig.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-24 上午11:32:12
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-24      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Section.Enemys;

import org.foxteam.noisyfox.FoxGaming.Core.FGEventsListener;
import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGScreenPlay;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Bullet;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.BulletPool;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Explosion;
import org.foxteam.noisyfox.THEngine.Section.Bullets.Bullet_Enemy_2;
import org.foxteam.noisyfox.THEngine.Section.Bullets.Bullet_Player;

/**
 * @ClassName: Enemy_Boss_Pig
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-24 上午11:32:12
 * 
 */
public class Enemy_Boss_Pig extends EnemyInAir {

	FGGraphicCollision myCollisionMask = new FGGraphicCollision();
	FGScreenPlay myAni = new FGScreenPlay();
	boolean getReady = false;
	boolean fire2 = true;
	FGSprite pigSprite;

	@Override
	public void createEnemy(int x, int y, int... extraConfig) {
		this.perform(FGStage.getCurrentStage().getStageIndex());
	}

	@Override
	public void prepareEnemy() {
		pigSprite = new FGSprite();
		pigSprite.bindFrames(GlobalResources.FRAMES_ENEMY_PIG);
		pigSprite
				.setOffset(pigSprite.getWidth() / 2, pigSprite.getHeight() / 2);
		this.bindSprite(pigSprite);

		this.setAlarm(0, (int) (FGStage.getSpeed() * 0.1f), true);// 播放动画
		this.startAlarm(0);

		int[][] vertex1 = { { 0, -29 }, { -20, 0 }, { -15, 30 }, { 15, 30 },
				{ 20, 0 } };
		myCollisionMask.addPolygon(vertex1, true);
		int[][] vertex2 = { { -16, -12 }, { -30, -29 }, { -50, -7 }, { -21, 4 } };
		myCollisionMask.addPolygon(vertex2, true);
		int[][] vertex3 = { { 16, -12 }, { 30, -29 }, { 50, -7 }, { 21, 4 } };
		myCollisionMask.addPolygon(vertex3, true);
		this.bindCollisionMask(myCollisionMask);

		this.setHP(1000);

		this.requireCollisionDetection(Bullet_Player.class);

		this.invincible = true;

		float myVSpeed = 90f / FGStage.getSpeed();
		float myHSpeed = 50f / FGStage.getSpeed();
		int dXmax = FGStage.getCurrentStage().getWidth() / 2
				- pigSprite.getOffsetX() - 10;

		myAni.jumpTo(FGStage.getCurrentStage().getWidth() / 2, FGStage
				.getCurrentStage().getHeight() / 4);
		myAni.moveTowards(180, myHSpeed);
		myAni.wait((int) (dXmax / myHSpeed));
		myAni.moveTowards(0, myHSpeed);
		myAni.wait((int) (dXmax / myHSpeed * 2));
		myAni.moveTowards(180, myHSpeed);
		myAni.wait((int) (dXmax / myHSpeed * 2));
		myAni.moveTowards(0, myHSpeed);
		myAni.wait((int) (dXmax / myHSpeed));
		myAni.jumpTo(FGStage.getCurrentStage().getWidth() / 2, FGStage
				.getCurrentStage().getHeight() / 4);
		myAni.moveTowards(270, myVSpeed);
		myAni.wait((int) (FGStage.getCurrentStage().getHeight() / 4 / myVSpeed));
		myAni.stop();
		myAni.wait((int) (FGStage.getSpeed() * 0.7f));
		myAni.moveTowards(90, myVSpeed);
		myAni.wait((int) (FGStage.getCurrentStage().getHeight() / 4 / myVSpeed));
		myAni.stop();

		this.setPosition(FGStage.getCurrentStage().getWidth() / 2,
				pigSprite.getOffsetY() - pigSprite.getHeight());
		this.motion_set(270, myVSpeed);

		requireEventFeature(FGEventsListener.EVENT_ONSTEP
				| FGEventsListener.EVENT_ONALARM);
	}

	@Override
	protected void onStep() {
		if (!getReady) {
			if (getY() >= FGStage.getCurrentStage().getHeight() / 4) {
				getReady = true;
				this.invincible = false;
			}
		} else {
			int step = myAni.getRemainNumber();
			if (step == 0) {
				this.playAScreenPlay(myAni);
				this.setAlarm(1, (int) (FGStage.getSpeed() * 1f), true);// 发射第一种子弹
				this.startAlarm(1);
				fire2 = true;

			} else if (step == 5) {
				this.stopAlarm(1);
			} else if (step == 3 && fire2) {
				// 发射第二种子弹
				for (int i = 0; i < 8; i++) {
					Bullet b = BulletPool.obtainBullet(Bullet_Enemy_2.class);
					b.createBullet((int) this.getX(), (int) this.getY(),
							110f / FGStage.getSpeed(), 45 * i);
					b.setDepth(this.getDepth() - 1);
				}
				fire2 = false;

			}
		}
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {// 播放动画
			this.getSprite().nextFrame();
		} else if (whichAlarm == 1) {// 发射子弹
			Bullet b = BulletPool.obtainBullet(Bullet_Enemy_2.class);
			b.createBullet((int) this.getX(), (int) this.getY()
					- this.getSprite().getOffsetY()
					+ this.getSprite().getHeight(), 110f / FGStage.getSpeed(),
					270);
			b.setDepth(this.getDepth() + 1);
		}
	}

	@Override
	protected void Explosion(Bullet bullet) {
		new Explosion(GlobalResources.FRAMES_EXPLOSION_BOSS, 1, 0.5f,
				(int) this.getX(), (int) this.getY(), -1);
		this.dismiss();

		this.bindCollisionMask(null);

		FGGamingThread.score += 1000;

	}

}
