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
package org.foxteam.noisyfox.THEngine.Performers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Enemy;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Player;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Player_Missile_Guided;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Player_Normal;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.Enemy;
import org.foxteam.noisyfox.THEngine.Performers.Enemys.EnemyInAir;

/**
 * @ClassName: Player
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-18 下午11:11:59
 * 
 */
public class Player extends Hitable {

	Views mainView = null;

	Point meOnScreen = null;
	Point fingerPressStart = null;
	Point meStart = null;

	boolean controllable = true;

	boolean onAnimation = false;

	static int remainLife = 3;
	Sprite playerSprite = new Sprite();
	boolean invincibleFlash = true;

	ScreenPlay birthAni = new ScreenPlay();

	GraphicCollision myCollisionMask = new GraphicCollision();

	Class<?> myMissile = null;
	int missile_level = 1;

	@Override
	protected void onStep() {
		if (!onAnimation) {
			float per = meOnScreen.getX() / mainView.getWidthFromScreen();

			mainView.setPositionFromStage(
					(Stage.getCurrentStage().getWidth() - mainView
							.getWidthFromStage()) * per, 0);

			this.setPosition(mainView.coordinateScreen2Stage_X(
					meOnScreen.getX(), meOnScreen.getY()), mainView
					.coordinateScreen2Stage_Y(meOnScreen.getX(),
							meOnScreen.getY()));
		} else {
			meOnScreen.setPosition(mainView.coordinateStage2Screen_X(
					(int) this.getX(), (int) this.getY()), mainView
					.coordinateStage2Screen_Y((int) this.getX(),
							(int) this.getY()));

			float per = meOnScreen.getX() / mainView.getWidthFromScreen();

			mainView.setPositionFromStage(
					(Stage.getCurrentStage().getWidth() - mainView
							.getWidthFromStage()) * per, 0);
		}
	}

	@Override
	protected void onCreate() {
		remainLife = 3;

		mainView = Stage.getCurrentStage().getView(0);

		playerSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.player, false);
		playerSprite.setOffset(playerSprite.getWidth() / 2,
				playerSprite.getHeight() / 2);
		this.bindSprite(playerSprite);

		fingerPressStart = new Point();
		meStart = new Point();
		meOnScreen = new Point();

		birthAni.moveTowardsWait(Stage.getCurrentStage().getWidth() / 2,
				Stage.getCurrentStage().getHeight() + playerSprite.getOffsetY()
						+ 40, (int) (1.5f * Stage.getSpeed()));

		birthAni.moveTowardsWait(Stage.getCurrentStage().getWidth() / 2, Stage
				.getCurrentStage().getHeight()
				- playerSprite.getHeight()
				+ playerSprite.getOffsetY() - 40, (int) (0.5f * Stage
				.getSpeed()));
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

		this.setPosition(Stage.getCurrentStage().getWidth() / 2, Stage
				.getCurrentStage().getHeight() + playerSprite.getOffsetY() + 40);

		birth();

	}

	@Override
	protected void onDestory() {
		// TODO Auto-generated method stub
		super.onDestory();
	}

	@Override
	protected void onTouch(int whichfinger, int x, int y) {
		if (whichfinger == 0) {
			if (!controllable) {
				fingerPressStart.setPosition(x, y);
				meStart.setPosition(meOnScreen.getX(), meOnScreen.getY());
			} else {
				int dx = x - fingerPressStart.getX();
				int dy = y - fingerPressStart.getY();
				int myX = meStart.getX() + dx;
				int myY = meStart.getY() + dy;
				int realX = mainView.coordinateScreen2Stage_X(myX, myY);
				int realY = mainView.coordinateScreen2Stage_Y(myX, myY);

				if (realX < this.getSprite().getOffsetX()) {

					if (realX < this.getSprite().getOffsetX() - 5) {
						int fingerXReal = mainView.coordinateScreen2Stage_X(
								fingerPressStart.getX(),
								fingerPressStart.getY());
						int fingerYReal = mainView.coordinateScreen2Stage_Y(
								fingerPressStart.getX(),
								fingerPressStart.getY());

						fingerPressStart.setPosition(mainView
								.coordinateStage2Screen_X(
										fingerXReal
												- this.getSprite().getOffsetX()
												+ realX, fingerYReal),
								fingerPressStart.getY());
					}

					realX = this.getSprite().getOffsetX();

				} else if (realX > Stage.getCurrentStage().getWidth()
						- this.getSprite().getWidth()
						+ this.getSprite().getOffsetX()) {

					if (realX > Stage.getCurrentStage().getWidth()
							- this.getSprite().getWidth()
							+ this.getSprite().getOffsetX() + 5) {
						int fingerXReal = mainView.coordinateScreen2Stage_X(
								fingerPressStart.getX(),
								fingerPressStart.getY());
						int fingerYReal = mainView.coordinateScreen2Stage_Y(
								fingerPressStart.getX(),
								fingerPressStart.getY());

						fingerPressStart
								.setPosition(
										mainView.coordinateStage2Screen_X(
												fingerXReal
														+ realX
														- (Stage.getCurrentStage()
																.getWidth()
																- this.getSprite()
																		.getWidth() + this
																.getSprite()
																.getOffsetX()),
												fingerYReal), fingerPressStart
												.getY());
					}

					realX = Stage.getCurrentStage().getWidth()
							- this.getSprite().getWidth()
							+ this.getSprite().getOffsetX();

				}

				if (realY < this.getSprite().getOffsetY()) {

					if (realY < this.getSprite().getOffsetY() - 5) {
						int fingerXReal = mainView.coordinateScreen2Stage_X(
								fingerPressStart.getX(),
								fingerPressStart.getX());
						int fingerYReal = mainView.coordinateScreen2Stage_Y(
								fingerPressStart.getY(),
								fingerPressStart.getY());

						fingerPressStart.setPosition(fingerPressStart.getX(),
								mainView.coordinateStage2Screen_Y(fingerXReal,
										fingerYReal
												- this.getSprite().getOffsetY()
												+ realY));
					}

					realY = this.getSprite().getOffsetY();

				} else if (realY > Stage.getCurrentStage().getHeight()
						- this.getSprite().getHeight()
						+ this.getSprite().getOffsetY()) {

					if (realY > Stage.getCurrentStage().getHeight()
							- this.getSprite().getHeight()
							+ this.getSprite().getOffsetY() + 5) {
						int fingerXReal = mainView.coordinateScreen2Stage_X(
								fingerPressStart.getX(),
								fingerPressStart.getX());
						int fingerYReal = mainView.coordinateScreen2Stage_Y(
								fingerPressStart.getY(),
								fingerPressStart.getY());

						fingerPressStart
								.setPosition(
										fingerPressStart.getX(),
										mainView.coordinateStage2Screen_Y(
												fingerXReal,
												fingerYReal
														+ realY
														- (Stage.getCurrentStage()
																.getHeight()
																- this.getSprite()
																		.getHeight() + this
																.getSprite()
																.getOffsetY())));
					}

					realY = Stage.getCurrentStage().getHeight()
							- this.getSprite().getHeight()
							+ this.getSprite().getOffsetY();

				}

				myX = mainView.coordinateStage2Screen_X(realX, realY);
				myY = mainView.coordinateStage2Screen_Y(realX, realY);

				meOnScreen.setPosition(myX, myY);
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

	@Override
	protected void onTouchRelease(int whichfinger) {
		if (whichfinger == 0) {

		}
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {
			Bullet_Player b = new Bullet_Player_Normal((int) this.getX(),
					(int) this.getY() - this.getSprite().getOffsetY());
			b.setDepth(this.getDepth() + 1);

		} else if (whichAlarm == 1) {// 无敌时间已到
			this.invincible = false;
			this.stopAlarm(3);

		} else if (whichAlarm == 2) {// 出生后的动画播放完成
			this.bindCollisionMask(myCollisionMask);
			controllable = true;
			// 发射子弹
			this.setAlarm(0, (int) (Stage.getSpeed() * 0.2f), true);
			this.startAlarm(0);

			if (myMissile != null) {
				this.setAlarm(4, (int) (Stage.getSpeed() * 1.0f), true);
				this.startAlarm(4);
			}

			onAnimation = false;
		} else if (whichAlarm == 3) {// 无敌闪烁
			invincibleFlash = !invincibleFlash;

		} else if (whichAlarm == 4) {// 发射导弹
			if (myMissile != null) {
				try {
					Class<?>[] pTypes = new Class[] { int.class, int.class };
					Constructor<?> ctor = myMissile.getConstructor(pTypes);
					Bullet_Player b = null;
					Object[] arg = new Object[] { (int) this.getX() + 10,
							(int) this.getY() };
					b = (Bullet_Player) ctor.newInstance(arg);
					b.setDepth(this.getDepth() + 1);
					b.motion_set(60, 200f / Stage.getSpeed());

					Object[] arg2 = new Object[] { (int) this.getX() - 10,
							(int) this.getY() };
					b = (Bullet_Player) ctor.newInstance(arg2);
					b.setDepth(this.getDepth() + 1);
					b.motion_set(120, 200f / Stage.getSpeed());
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	protected void onCollisionWith(Performer target) {
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
		new Explosion(
				org.foxteam.noisyfox.THEngine.R.drawable.explosion_normal, 7,
				0.5f, (int) this.getX(), (int) this.getY());

		if (--remainLife < 0) {
			this.dismiss();
		} else {

			this.setPosition(this.getX(), Stage.getCurrentStage().getHeight()
					+ playerSprite.getOffsetY() + 40);

			myMissile = null;// 重置火力等级
			missile_level = 1;
			birth();
		}
	}

	private void setInvincible(float seconds) {
		this.invincible = true;
		this.setAlarm(1, (int) (Stage.getSpeed() * seconds), false);
		this.startAlarm(1);
		this.setAlarm(3, (int) (Stage.getSpeed() * 0.1f), true);
		this.startAlarm(3);
	}

	private void birth() {
		this.stopAlarm(0);
		this.stopAlarm(4);
		this.bindCollisionMask(null);
		controllable = false;
		setInvincible(5.0f);

		this.setAlarm(2, (int) (Stage.getSpeed() * 2.0f), false);
		this.startAlarm(2);
		onAnimation = true;

		this.setHP(10);
		this.playAScreenPlay(birthAni);
	}

	public void getPowerUp(Class<?> bulletType) {
		GamingThread.score += 100;

		if (bulletType == Bullet_Player_Missile_Guided.class) {
			updateMissile(bulletType);
		}

	}

	private void updateMissile(Class<?> missileType) {
		if (myMissile == null) {
			myMissile = missileType;

			this.setAlarm(4, (int) (Stage.getSpeed() * 1.0f), true);
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
