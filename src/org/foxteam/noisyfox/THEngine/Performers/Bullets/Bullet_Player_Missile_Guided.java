/**
 * FileName:     Bullet_Player_Missile_Guided.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-17 下午7:31:58
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-17      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers.Bullets;

import java.util.ArrayList;
import java.util.List;

import org.foxteam.noisyfox.FoxGaming.Core.FGMathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGConvertor;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;
import org.foxteam.noisyfox.THEngine.Performers.Explosion;
import org.foxteam.noisyfox.THEngine.Performers.Hitable;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy;

import android.graphics.Canvas;

/**
 * @ClassName: Bullet_Player_Missile_Guided
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-17 下午7:31:58
 * 
 */
public class Bullet_Player_Missile_Guided extends Bullet_Player {
	FGConvertor GCConvertor = new FGConvertor();
	FGSpriteConvertor SpConvertor = new FGSpriteConvertor();

	private static final float TARGET_SEARCH_TIME = 0.5f; // 发射后锁定敌机的时间限制，单位秒，超过这个时间就不会去锁定目标，只会直线飞行

	private boolean searchingTarget = true;
	private Enemy lockedEnemy = null;

	@Override
	protected void onDraw() {
		if (this.getSprite() != null) {
			Canvas c = getCanvas();
			this.getSprite().draw(c, (int) getX(), (int) getY(), SpConvertor);
		}
	}

	@Override
	protected void onCreate() {

		FGSprite bulletSprite = new FGSprite();
		bulletSprite
				.loadFromBitmap(
						org.foxteam.noisyfox.THEngine.R.drawable.bullet_player_missile_guided,
						false);
		bulletSprite.setOffset(4, 10);
		this.bindSprite(bulletSprite);

		FGGraphicCollision co = new FGGraphicCollision();
		int[][] vertex1 = { { 0, -10 }, { -2, -8 }, { -3, 0 }, { 3, 0 },
				{ 2, -8 } };
		co.addPolygon(vertex1, true);
		this.bindCollisionMask(co);

		this.setDamage(5);

		// 开始寻找敌机
		this.setAlarm(0, (int) (FGStage.getSpeed() * TARGET_SEARCH_TIME), false);
		this.startAlarm(0);

	}

	@Override
	protected void onStep() {
		// 搜寻敌机
		if (lockedEnemy != null) {
			if (!lockedEnemy.isPerforming()) {
				lockedEnemy = null;
			} else {
				// 瞄准
				float targetDirection = FGMathsHelper.point_direction(getX(),
						getY(), lockedEnemy.getX(), lockedEnemy.getY());
				float dDir = FGMathsHelper.directionTo(this.direction,
						targetDirection);

				if (Math.abs(dDir) <= 5) {
					this.motion_set(this.direction + dDir, this.speed);
				} else {
					this.motion_set(this.direction + (dDir < 0 ? -5 : 5),
							this.speed);
				}
			}
		} else if (searchingTarget) {
			FGPerformer[] p = FGStage.getPerformersByClass(Enemy.class);
			if (p.length > 0) {
				List<Enemy> avaliableEnemy = new ArrayList<Enemy>();
				for (FGPerformer ap : p) {
					if (!((Hitable) ap).isInvincible()) {
						avaliableEnemy.add((Enemy) ap);
					}
				}
				if (!avaliableEnemy.isEmpty()) {
					lockedEnemy = avaliableEnemy.get(FGMathsHelper.random(0,
							avaliableEnemy.size() - 1));
					searchingTarget = false;
				}
			}
		}

		SpConvertor.setRotation(this.direction - 90);
		GCConvertor.setRotation(this.direction - 90);

		if (this.getCollisionMask() != null) {
			this.getCollisionMask().applyConvertor(GCConvertor);
		}
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {// 停止搜寻敌机
			searchingTarget = false;
		}
	}

	@Override
	protected void onOutOfStage() {
		this.dismiss();
		this.bindCollisionMask(null);
	}

	@Override
	public void hitOn(Hitable target) {
		if (Enemy.class.isInstance(target)) {
			int x = (int) FGMathsHelper.lengthdir_x(this.getSprite()
					.getOffsetY(), this.direction);
			int y = -(int) FGMathsHelper.lengthdir_y(this.getSprite()
					.getOffsetY(), this.direction);
			new Explosion(
					org.foxteam.noisyfox.THEngine.R.drawable.explosion_missile_small,
					5, 1, 0.3f, (int) this.getX() + x, (int) this.getY() + y,
					-1);
			this.dismiss();
		}
	}

	@Override
	public boolean isOutOfStage() {
		boolean b = super.isOutOfStage();

		if (this.getCollisionMask() == null) {
			return b;
		} else {
			if (!b) {
				return false;
			}
		}

		return this
				.collision_rectangle(0, 0,
						FGStage.getCurrentStage().getWidth(), FGStage
								.getCurrentStage().getHeight(), this, false) == null;
	}

	public Bullet_Player_Missile_Guided(int x, int y) {
		this.perform(FGStage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
	}

}
