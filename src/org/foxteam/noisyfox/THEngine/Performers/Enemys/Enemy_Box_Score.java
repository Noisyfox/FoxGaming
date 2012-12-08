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
package org.foxteam.noisyfox.THEngine.Performers.Enemys;

import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Performers.Bullet;
import org.foxteam.noisyfox.THEngine.Performers.Explosion;
import org.foxteam.noisyfox.THEngine.Performers.PowerUp;
import org.foxteam.noisyfox.THEngine.Performers.Bullets.Bullet_Player;
import org.foxteam.noisyfox.THEngine.Performers.PowerUps.PowerUp_Score;
import org.foxteam.noisyfox.THEngine.Stages.SectionStage;

/**
 * @ClassName: Enemy_Box_Score
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-25 上午10:12:23
 * 
 */
public class Enemy_Box_Score extends EnemyOnGround {

	int inX = 0;

	@Override
	protected void onCreate() {
		FGSprite boxSprite = new FGSprite();
		boxSprite.bindFrames(GlobalResources.FRAMES_ENEMY_BOX_SCORE);
		boxSprite.setOffset(22, 13);
		this.bindSprite(boxSprite);

		FGGraphicCollision co = new FGGraphicCollision();
		co.addRectangle(-22, -13, 43, 26, true);
		this.bindCollisionMask(co);

		this.setHP(10);

		this.requireCollisionDetection(Bullet_Player.class);

		this.setPosition(inX, -boxSprite.getHeight() + boxSprite.getOffsetY());

		this.motion_set(270, SectionStage.getScrollSpeedV());
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
		PowerUp p = new PowerUp_Score((int) getX(), (int) getY());
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
