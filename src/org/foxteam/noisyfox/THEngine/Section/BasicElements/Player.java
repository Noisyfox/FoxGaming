/**
 * FileName:     Player.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-18 下午11:11:59
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-18      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Section.Bullets.Bullet_Enemy;
import org.foxteam.noisyfox.THEngine.Section.Bullets.Bullet_Player;
import org.foxteam.noisyfox.THEngine.Section.Bullets.Bullet_Player_Missile_Guided;
import org.foxteam.noisyfox.THEngine.Section.Bullets.Bullet_Player_Missile_Manual;
import org.foxteam.noisyfox.THEngine.Section.Bullets.Bullet_Player_Normal;
import org.foxteam.noisyfox.THEngine.Section.Enemys.Enemy;
import org.foxteam.noisyfox.THEngine.Section.Enemys.EnemyInAir;
import org.foxteam.noisyfox.THEngine.Section.PowerUps.PowerUp_Missile;

import android.os.Bundle;

/**
 * @ClassName: Player
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-18 下午11:11:59
 * 
 */
public class Player extends Hitable {

	FGViews mainView = null;

	FGPointF meOnScreen = new FGPointF();
	FGPointF fingerPressStart = new FGPointF();
	FGPointF meStart = new FGPointF();

	public boolean controllable = true;

	public boolean onAnimation = false;

	static int remainLife = 3;
	FGSprite playerSprite = new FGSprite();
	boolean invincibleFlash = true;

	FGScreenPlay birthAni = new FGScreenPlay();

	FGGraphicCollision myCollisionMask = new FGGraphicCollision();

	Class<?> myMissile = null;
	int missile_level = 1;

	public boolean fire = true;

	public void loadState(Bundle savedState) {
		remainLife = savedState.getInt("THEA_player_remainLife", 0);
		missile_level = savedState.getInt("THEA_player_missileLevel", 1);
		switch (savedState.getInt("THEA_player_missileType", 0)) {
		case 0:
			myMissile = null;
			break;
		case 1:
			myMissile = Bullet_Player_Missile_Guided.class;
			break;
		case 2:
			myMissile = Bullet_Player_Missile_Manual.class;
			break;
		}
	}

	public void saveState(Bundle savedState) {
		savedState.putInt("THEA_player_remainLife", remainLife);
		savedState.putInt("THEA_player_missileType", myMissile == null ? 0
				: (myMissile == Bullet_Player_Missile_Guided.class ? 1 : 2));// 导弹类型，0无1跟踪2非跟踪
		savedState.putInt("THEA_player_missileLevel", missile_level);
	}

	@Override
	protected void onCreate() {

		mainView = FGStage.getCurrentStage().getView();

		playerSprite.bindFrames(GlobalResources.FRAMES_PLAYER);
		playerSprite.setOffset(playerSprite.getWidth() / 2,
				playerSprite.getHeight() / 2);
		this.bindSprite(playerSprite);

		birthAni.moveTowardsWait(
				FGStage.getCurrentStage().getWidth() / 2,
				FGStage.getCurrentStage().getHeight()
						+ playerSprite.getOffsetY() + 40,
				(int) (1.5f * FGStage.getSpeed()));
		birthAni.moveTowardsWait(
				FGStage.getCurrentStage().getWidth() / 2,
				FGStage.getCurrentStage().getHeight()
						- playerSprite.getHeight() + playerSprite.getOffsetY()
						- 40, (int) (0.5f * FGStage.getSpeed()));
		birthAni.stop();

		// 添加碰撞检测遮罩
		int[][] vertex1 = { { -21, -7 }, { -23, 4 }, { 23, 4 }, { 21, -7 } };
		myCollisionMask.addPolygon(vertex1, true);
		int[][] vertex2 = { { -11, 4 }, { -10, 19 }, { 0, 28 }, { 10, 19 },
				{ 11, 4 } };
		myCollisionMask.addPolygon(vertex2, true);
		int[][] vertex3 = { { 0, -28 }, { -17, -7 }, { 17, -7 } };
		myCollisionMask.addPolygon(vertex3, true);

		this.requireCollisionDetection(EnemyInAir.class);
		this.requireCollisionDetection(Bullet_Enemy.class);
		this.requireCollisionDetection(PowerUp.class);

		this.setPosition(FGStage.getCurrentStage().getWidth() / 2, FGStage
				.getCurrentStage().getHeight() + playerSprite.getOffsetY() + 40);

		birth();

	}

	@Override
	protected void onStep() {
		float kw = mainView.getWidthFromScreen() / mainView.getWidthFromStage();
		float kh = mainView.getHeightFromScreen()
				/ mainView.getHeightFromStage();

		if (!onAnimation) {

			float view_width = mainView.getWidthFromScreen();
			float v_left = playerSprite.getOffsetX() * kw;
			float v_right = playerSprite.getWidth() * kw - v_left;

			float per = (meOnScreen.getX() - v_left)
					/ (view_width - v_left - v_right);

			this.setPosition(0, meOnScreen.getY() / kh);
			updateViewK(per);

		} else {

			updateViewX(getX());

			meOnScreen.setPosition((getX() - mainView.getXFromStage()) * kw,
					getY() * kh);

		}
	}

	@Override
	protected void onTouch(int whichfinger, int x, int y) {
		if (whichfinger == 0) {
			if (!controllable) {
				fingerPressStart.setPosition(x, y);
				meStart.setPosition(meOnScreen.getX(), meOnScreen.getY());
			} else {

				float view_width = mainView.getWidthFromScreen();
				float view_height = mainView.getHeightFromScreen();
				float kw = view_width / mainView.getWidthFromStage();
				float kh = view_height / mainView.getHeightFromStage();
				float v_left = playerSprite.getOffsetX() * kw;
				float v_right = playerSprite.getWidth() * kw - v_left;
				float v_top = playerSprite.getOffsetY() * kh;
				float v_bottom = playerSprite.getHeight() * kh - v_top;

				float dx = x - fingerPressStart.getX();
				float dy = y - fingerPressStart.getY();

				// 先计算飞机在屏幕上的最终位置
				float player_x = meStart.getX() + dx;
				float player_y = meStart.getY() + dy;

				if (player_x < v_left)
					player_x = v_left;
				else if (player_x > view_width - v_right)
					player_x = view_width - v_right;
				if (player_y < v_top)
					player_y = v_top;
				else if (player_y > view_height - v_bottom)
					player_y = view_height - v_bottom;

				meOnScreen.setPosition(player_x, player_y);

				// 计算实际位移
				float rdx = player_x - meStart.getX();
				float rdy = player_y - meStart.getY();
				float ddx = dx - rdx;
				float ddy = dy - rdy;
				fingerPressStart.setPosition(fingerPressStart.getX() + ddx,
						fingerPressStart.getY() + ddy);

			}
		}
	}

	@Override
	protected void onTouchPress(int whichfinger, int x, int y) {
		if (whichfinger == 0) {
			fingerPressStart.setPosition(x, y);
			meStart.setPosition(meOnScreen.getX(), meOnScreen.getY());
		}
	}

	/**
	 * 将飞机保持在屏幕水平方向k处
	 * 
	 * @param k
	 *            水平方向的百分比
	 */
	private void updateViewK(float k) {

		if (k < 0)
			k = 0;
		else if (k > 1)
			k = 1;

		float player_x = 0;
		float view_x = 0;
		float stage_width = FGStage.getCurrentStage().getWidth();
		float view_width = mainView.getWidthFromStage();

		float s_left = playerSprite.getOffsetX();
		float s_right = playerSprite.getWidth() - s_left;

		player_x = (stage_width - s_left - s_right) * k + s_left;
		view_x = player_x - (view_width - s_left - s_right) * k - s_left;

		this.setPosition(player_x, getY());
		mainView.setPositionFromStage(view_x, mainView.getYFromStage());

	}

	/**
	 * 将飞机保持在 stage 水平方向x处
	 * 
	 * @param x
	 *            横坐标
	 */
	private void updateViewX(float x) {

		float s_left = playerSprite.getOffsetX();
		float s_right = playerSprite.getWidth() - s_left;
		float stage_width = FGStage.getCurrentStage().getWidth();
		if (x < s_left)
			x = s_left;
		else if (x > stage_width - s_right)
			x = stage_width - s_right;

		float player_x = 0;
		float view_x = 0;
		float view_width = mainView.getWidthFromStage();

		float k = (x - s_left) / (stage_width - s_left - s_right);

		player_x = x;
		view_x = player_x - (view_width - s_left - s_right) * k - s_left;

		this.setPosition(player_x, getY());
		mainView.setPositionFromStage(view_x, mainView.getYFromStage());

	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0 && fire) {
			Bullet_Player b = (Bullet_Player) BulletPool
					.obtainBullet(Bullet_Player_Normal.class);

			b.createBullet((int) this.getX(), (int) this.getY()
					- this.getSprite().getOffsetY(), 0, 0);
			b.setDepth(this.getDepth() + 1);

		} else if (whichAlarm == 1) {// 无敌时间已到
			this.invincible = false;
			this.stopAlarm(3);

		} else if (whichAlarm == 2) {// 出生后的动画播放完成
			this.bindCollisionMask(myCollisionMask);
			controllable = true;
			// 发射子弹
			this.setAlarm(0, (int) (FGStage.getSpeed() * 0.2f), true);
			this.startAlarm(0);

			if (myMissile != null) {
				this.setAlarm(4, (int) (FGStage.getSpeed() * 1.0f), true);
				this.startAlarm(4);
			}

			onAnimation = false;
		} else if (whichAlarm == 3) {// 无敌闪烁
			invincibleFlash = !invincibleFlash;

		} else if (whichAlarm == 4 && fire) {// 发射导弹
			if (myMissile != null) {
				float dDeg = 0;
				if (myMissile == Bullet_Player_Missile_Guided.class) {
					dDeg = 30;
				} else if (myMissile == Bullet_Player_Missile_Manual.class) {
					dDeg = 0;
				}

				Bullet_Player b = (Bullet_Player) BulletPool
						.obtainBullet(myMissile);
				b.createBullet((int) this.getX() + 10, (int) this.getY(), 0, 0);
				b.setDepth(this.getDepth() + 1);
				b.motion_set(90 - dDeg, 200f / FGStage.getSpeed());

				b = (Bullet_Player) BulletPool.obtainBullet(myMissile);
				b.createBullet((int) this.getX() - 10, (int) this.getY(), 0, 0);
				b.setDepth(this.getDepth() + 1);
				b.motion_set(90 + dDeg, 200f / FGStage.getSpeed());
			}

		}
	}

	@Override
	protected void onDraw() {
		if (!this.invincible || invincibleFlash) {
			super.onDraw();
		}
	}

	@Override
	protected void onCollisionWith(FGPerformer target) {
		if (Bullet_Enemy.class.isInstance(target)) {
			this.hitBy((Bullet) target);

		} else if (PowerUp.class.isInstance(target)) {
			((Bullet) target).hitOn(this);

		} else if (Enemy.class.isInstance(target)) {// 处理敌机碰撞
			if (!invincible) {
				Explosion(null);
				if (!((Hitable) target).invincible) {
					((Hitable) target)
							.setHP(((Hitable) target).remainHP() - 50);
					if (((Hitable) target).remainHP() < 0) {
						((Hitable) target).Explosion(null);
					}
				}
			}

		}
	}

	@Override
	protected void Explosion(Bullet bullet) {
		new Explosion(GlobalResources.FRAMES_EXPLOSION_NORMAL, 1, 0.5f,
				(int) this.getX(), (int) this.getY(), -1);

		FGVibrator.vibrate(500);

		// 丢下奖励
		PowerUp p = (PowerUp) BulletPool.obtainBullet(PowerUp_Missile.class);
		p.createBullet((int) getX(), (int) getY(), 0, 0);
		p.setDepth(getDepth() + 1);

		if (--remainLife < 0) {
			this.dismiss();
			((SectionStage) FGStage.getCurrentStage()).gameOver();
		} else {

			this.setPosition(this.getX(), FGStage.getCurrentStage().getHeight()
					+ playerSprite.getOffsetY() + 40);

			myMissile = null;// 重置火力等级
			missile_level = 1;
			birth();
		}
	}

	private void setInvincible(float seconds) {
		this.invincible = true;
		this.setAlarm(1, (int) (FGStage.getSpeed() * seconds), false);
		this.startAlarm(1);
		this.setAlarm(3, (int) (FGStage.getSpeed() * 0.1f), true);
		this.startAlarm(3);
	}

	private void birth() {
		this.stopAlarm(0);
		this.stopAlarm(4);
		this.bindCollisionMask(null);
		controllable = false;
		setInvincible(5.0f);

		this.setAlarm(2, (int) (FGStage.getSpeed() * 2.0f), false);
		this.startAlarm(2);
		onAnimation = true;

		this.setHP(10);
		this.playAScreenPlay(birthAni);
	}

	public void getPowerUp(Class<?> bulletType) {
		FGGamingThread.score += 100;

		if (bulletType == Bullet_Player_Missile_Guided.class
				|| bulletType == Bullet_Player_Missile_Manual.class) {
			updateMissile(bulletType);
		}

	}

	private void updateMissile(Class<?> missileType) {
		if (myMissile == null) {
			myMissile = missileType;

			this.setAlarm(4, (int) (FGStage.getSpeed() * 1.0f), true);
			this.startAlarm(4);
		} else {
			if (myMissile == missileType) {
				if (missile_level == 1) {// 满级

				} else {
					missile_level++;
				}
			} else {
				myMissile = missileType;
			}
		}
	}

}
