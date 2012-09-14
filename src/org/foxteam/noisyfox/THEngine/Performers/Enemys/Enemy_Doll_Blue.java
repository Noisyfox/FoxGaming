/**
 * FileName:     Enemy_Doll_Blue.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-28 下午7:53:51
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-28      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers.Enemys;

import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGMathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Performers.Bullet;
import org.foxteam.noisyfox.THEngine.Performers.Explosion;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Enemy_2;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Player;

import android.graphics.Canvas;

/**
 * @ClassName: Enemy_Doll_Blue
 * @Description: 小怪的一种，行为:从屏幕的一侧排成一排竖直进入，后依次从屏幕上部向另一侧飞离,期间向屏幕正中间发射一枚子弹
 * @author: Noisyfox
 * @date: 2012-8-28 下午7:53:51
 * 
 */
public class Enemy_Doll_Blue extends EnemyInAir {

	private FGSprite dollSprite = new FGSprite();
	private FGSpriteConvertor sc = new FGSpriteConvertor();
	private boolean faceLeft = false;// 面朝哪一侧
	private int listNumber = 0;// 该队伍一共多少怪物
	private int myNumber = 0;// 自己排在第几个

	private static float hSpeedMin = 5;
	private float mySpeed = 0;

	private int myY = 0;

	private int myStatus = -1;// -1-等待进入阶段 0-进入阶段 1-等待离开阶段 2-向上返回阶段 3-沿曲线出屏阶段
	private int myFlag = 0;// 贝塞尔路径flag
	private float baseX = 0;
	private float baseY = 0;

	@Override
	protected void onCreate() {

		dollSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.enemy_doll_blue, 12,
				1, false);
		dollSprite.setOffset(dollSprite.getWidth() / 2,
				dollSprite.getHeight() / 2);
		this.bindSprite(dollSprite);

		FGGraphicCollision co = new FGGraphicCollision();
		int[][] vertex = { { 0, -24 }, { -24, 0 }, { 0, 24 }, { 24, 0 } };
		co.addPolygon(vertex, true);
		this.bindCollisionMask(co);

		sc.setScale(-1, 1);

		this.setHP(10);

		this.requireCollisionDetection(Bullet_Player.class);

		if (faceLeft) {
			baseX = FGStage.getCurrentStage().getWidth()
					- dollSprite.getWidth() + dollSprite.getOffsetX() - 20;
		} else {
			baseX = dollSprite.getOffsetX() + 20;
		}
		baseY = 50 + dollSprite.getOffsetY();

		this.setPosition(baseX,
				dollSprite.getOffsetY() - dollSprite.getHeight());

		this.invincible = true;

		myY = (listNumber - myNumber)
				* (dollSprite.getHeight() + 2/* 上下各留1像素 */) + 1
				+ dollSprite.getOffsetY() + 50 + dollSprite.getOffsetY();

		mySpeed = 90f / FGStage.getSpeed();

		this.setAlarm(0, (int) (FGStage.getSpeed() * 0.1f), true);// 播放动画
		this.startAlarm(0);

		this.setAlarm(
				1,
				(int) ((float) (myNumber - 1) * (float) dollSprite.getHeight() / mySpeed) + 1,
				false);// 等待进入
		this.startAlarm(1);

	}

	@Override
	protected void onStep() {
		switch (myStatus) {
		case -1:
			break;
		case 0:
			this.invincible = false;
			this.motion_setSpeed(0, mySpeed);
			if (getY() >= myY) {
				this.setPosition(baseX, myY);
				this.motion_setSpeed(0, 0);
				myStatus++;
				this.setAlarm(1, (int) (FGStage.getSpeed() * 1.5f), false);// 等待离开
				this.startAlarm(1);

				this.setAlarm(2, (int) (FGStage.getSpeed() * 2.5f), false);// 发射子弹
				this.startAlarm(2);
			}
			break;

		case 1:
			break;
		case 2:
			this.motion_setSpeed(0, -mySpeed);
			if (getY() <= baseY) {
				this.setPosition(baseX, baseY);
				this.motion_setSpeed(0, 0);
				myStatus++;
			}
			break;

		case 3:
			myFlag = GlobalResources.PATHBEZIER3_ENEMY_DOOL_BLUE
					.nextPositionFlag(myFlag, mySpeed);
			if (myFlag == GlobalResources.PATHBEZIER3_ENEMY_DOOL_BLUE
					.getMaxFlag()) {
				this.motion_set(faceLeft ? 180 : 0, mySpeed);
			} else {
				float dx = GlobalResources.PATHBEZIER3_ENEMY_DOOL_BLUE
						.getX(myFlag);
				float dy = GlobalResources.PATHBEZIER3_ENEMY_DOOL_BLUE
						.getY(myFlag);
				dy -= 50;
				float newX = 0;
				float newY = baseY + dy;
				if (faceLeft) {
					newX = baseX - dx;
				} else {
					newX = baseX + dx;
				}
				this.motion_set(FGMathsHelper.point_direction(getX(), getY(),
						newX, newY), FGMathsHelper.point_distance(getX(),
						getY(), newX, newY));
			}
			break;
		}
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {// 播放动画
			int curFrm = dollSprite.getCurrentFrame();
			if (Math.abs(this.hspeed) > hSpeedMin) {// 有水平移动
				if (curFrm < 5) {
					dollSprite.setCurrentFrame(5);
				} else if (curFrm == 11) {
					dollSprite.setCurrentFrame(8);
				} else {
					dollSprite.nextFrame();
				}
			} else {
				if (curFrm > 7) {
					dollSprite.setCurrentFrame(7);
				} else if (curFrm < 5) {
					if (curFrm == 4) {
						dollSprite.setCurrentFrame(1);
					} else {
						dollSprite.nextFrame();
					}
				} else {
					dollSprite.previousFrame();
				}
			}

		} else if (whichAlarm == 1) {// 等待进入以及等待返回
			myStatus++;
		} else if (whichAlarm == 2) {// 发射子弹
			Bullet b = new Bullet_Enemy_2((int) this.getX(), (int) this.getY(),
					FGMathsHelper.point_direction(getX(), getY(), FGStage
							.getCurrentStage().getWidth() / 2, FGStage
							.getCurrentStage().getHeight() / 2),
					110f / FGStage.getSpeed());
			b.setDepth(this.getDepth() - 1);
		}
	}

	@Override
	public boolean isOutOfStage() {
		return super.isOutOfStage()
				&& (this.getX() < 0 || this.getX() > FGStage.getCurrentStage()
						.getWidth());
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
		this.bindCollisionMask(null);
	}

	@Override
	protected void onDraw() {
		Canvas c = this.getCanvas();
		if (!faceLeft) {
			this.getSprite().draw(c, (int) this.getX(), (int) this.getY());
		} else {
			this.getSprite().draw(c, (int) this.getX(), (int) this.getY(), sc);
		}
	}

	@Override
	protected void Explosion(Bullet bullet) {
		new Explosion(
				org.foxteam.noisyfox.THEngine.R.drawable.explosion_normal, 7,
				1, 0.5f, (int) this.getX(), (int) this.getY(), -1);
		this.dismiss();

		this.bindCollisionMask(null);

		FGGamingThread.score += 14;
	}

	@Override
	public void createEnemy(int x, int y, int... extraConfig) {
		this.perform(FGStage.getCurrentStage().getStageIndex());
		faceLeft = extraConfig[0] != 0;
		listNumber = extraConfig[1];
		myNumber = extraConfig[2];
	}

}
