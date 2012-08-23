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
package org.foxteam.noisyfox.THEngine.Performers.Enemys;

import org.foxteam.noisyfox.FoxGaming.Core.GamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.Stage;
import org.foxteam.noisyfox.FoxGaming.G2D.GraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;
import org.foxteam.noisyfox.THEngine.Performers.Bullet;
import org.foxteam.noisyfox.THEngine.Performers.Explosion;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Player;
import org.foxteam.noisyfox.THEngine.Performers.PowerUps.PowerUp_Missile_Guided;

/**
 * @ClassName: Enemy_Butterfly
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-23 下午3:39:45
 * 
 */
public class Enemy_Butterfly extends EnemyInAir {

	int inX = 0;// 从哪个位置进入屏幕

	@Override
	protected void onCreate() {
		Sprite butterflySprite = new Sprite();
		butterflySprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.enemy_butterfly, 4, 1,
				false);
		butterflySprite.setOffset(butterflySprite.getWidth() / 2,
				butterflySprite.getHeight() / 2);
		this.bindSprite(butterflySprite);

		this.setAlarm(0, (int) (Stage.getSpeed() * 0.1f), true);// 播放动画
		this.startAlarm(0);

		GraphicCollision co = new GraphicCollision();
		co.addCircle(-2, -4, 15, true);
		co.addCircle(-15, -14, 6, true);
		co.addCircle(13, -14, 6, true);
		co.addCircle(-11, -22, 4, true);
		co.addCircle(14, -22, 4, true);
		co.addCircle(-15, 6, 10, true);
		co.addCircle(15, 8, 10, true);
		this.bindCollisionMask(co);

		this.setHP(100);

		this.requireCollisionDetection(Bullet_Player.class);

		this.setPosition(inX,
				-butterflySprite.getHeight() + butterflySprite.getOffsetY());
		
		float mySpeed = 90f / Stage.getSpeed();
	}
	
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {// 播放动画
			this.getSprite().nextFrame();

		}
	}

	@Override
	protected void Explosion(Bullet bullet) {
		new Explosion(
				org.foxteam.noisyfox.THEngine.R.drawable.explosion_normal, 7,
				0.5f, (int) this.getX(), (int) this.getY());

		new PowerUp_Missile_Guided((int) getX(), (int) getY())
				.setDepth(getDepth() + 1);

		this.dismiss();

		this.bindCollisionMask(null);

		GamingThread.score += 100;
	}

	@Override
	public void createEnemy(int x, int y, int... extraConfig) {
		this.perform(Stage.getCurrentStage().getStageIndex());
		inX = x;
	}

}
