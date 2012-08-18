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

import org.foxteam.noisyfox.FoxGaming.Core.MathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.Performer;
import org.foxteam.noisyfox.FoxGaming.Core.Stage;
import org.foxteam.noisyfox.FoxGaming.G2D.Convertor;
import org.foxteam.noisyfox.FoxGaming.G2D.GraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;
import org.foxteam.noisyfox.FoxGaming.G2D.SpriteConvertor;
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
	Convertor GCConvertor = new Convertor();
	SpriteConvertor SpConvertor = new SpriteConvertor();

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

		Sprite bulletSprite = new Sprite();
		bulletSprite
				.loadFromBitmap(
						org.foxteam.noisyfox.THEngine.R.drawable.bullet_player_missile_guided,
						false);
		bulletSprite.setOffset(4, 10);
		this.bindSprite(bulletSprite);

		GraphicCollision co = new GraphicCollision();
		int[][] vertex1 = { { 0, -10 }, { -2, -8 }, { -3, 0 }, { 3, 0 },
				{ 2, -8 } };
		co.addPolygon(vertex1, true);
		this.bindCollisionMask(co);

		this.setDamage(5);

		// this.motion_set(90, 200f / Stage.getSpeed());

		// 开始寻找敌机
		this.setAlarm(0, (int) (Stage.getSpeed() * TARGET_SEARCH_TIME), false);
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
				float targetDirection = MathsHelper.point_direction(getX(),
						getY(), lockedEnemy.getX(), lockedEnemy.getY());
				float dDir = MathsHelper.directionTo(this.direction,
						targetDirection);

				if (Math.abs(dDir) <= 1) {
					this.motion_set(this.direction + dDir, this.speed);
				} else {
					this.motion_set(this.direction + (dDir < 0 ? -5 : 5),
							this.speed);
				}
			}
		} else if (searchingTarget) {
			Performer[] p = Stage.getPerformersByClass(Enemy.class);
			if (p.length > 0) {
				lockedEnemy = (Enemy) p[MathsHelper.random(0, p.length - 1)];
				searchingTarget = false;
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

		return this.collision_rectangle(0, 0, Stage.getCurrentStage()
				.getWidth(), Stage.getCurrentStage().getHeight(), this, false) == null;
	}

	public Bullet_Player_Missile_Guided(int x, int y) {
		this.perform(Stage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
	}

}
