/**
 * FileName:     Enemy_Box_Score.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-25 上午10:12:23
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-25      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Section.Enemys;

import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Bullet;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.BulletPool;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Explosion;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.PowerUp;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.SectionStage;
import org.foxteam.noisyfox.THEngine.Section.Bullets.Bullet_Player;
import org.foxteam.noisyfox.THEngine.Section.PowerUps.PowerUp_Score;

/**
 * @ClassName: Enemy_Box_Score
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-25 上午10:12:23
 * 
 */
public class Enemy_Box_Score extends EnemyOnGround {

	int inX = 0;
	FGSprite boxSprite;

	@Override
	public void prepareEnemy() {
		boxSprite = new FGSprite();
		boxSprite.bindFrames(GlobalResources.FRAMES_ENEMY_BOX_SCORE);
		boxSprite.setOffset(32, 30);
		this.bindSprite(boxSprite);

		FGGraphicCollision co = new FGGraphicCollision();
		co.addRectangle(-32, -30, 67, 50, true);
		this.bindCollisionMask(co);

		this.setHP(10);

		this.requireCollisionDetection(Bullet_Player.class);

		this.motion_set(270, SectionStage.getScrollSpeedV());
	}

	@Override
	protected void onCreate() {
		this.setPosition(inX, -boxSprite.getHeight() + boxSprite.getOffsetY());
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
		PowerUp p = (PowerUp) BulletPool.obtainBullet(PowerUp_Score.class);
		p.createBullet((int) getX(), (int) getY(), 0, 0);
		p.setDepth(getDepth());

		this.dismiss();

		this.bindCollisionMask(null);

		FGGamingThread.score += 100;
	}

	@Override
	public void createEnemy(int x, int y, int... extraConfig) {
		this.setDepth(1000);
		this.perform(FGStage.getCurrentStage().getStageIndex());
		inX = x;
	}

}
