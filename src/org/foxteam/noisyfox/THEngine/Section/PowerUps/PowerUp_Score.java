/**
 * FileName:     PowerUp_Score.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-25 上午10:37:09
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-25      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Section.PowerUps;

import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Explosion;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Hitable;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.Player;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.PowerUp;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.SectionStage;

/**
 * @ClassName: PowerUp_Score
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-25 上午10:37:09
 * 
 */
public class PowerUp_Score extends PowerUp {
	FGSprite scoreSprite;

	public PowerUp_Score() {

		scoreSprite = new FGSprite();
		scoreSprite.bindFrames(GlobalResources.FRAMES_POWERUP_SCORE);
		scoreSprite.setOffset(scoreSprite.getWidth() / 2,
				scoreSprite.getHeight() / 2);
		this.bindSprite(scoreSprite);

		FGGraphicCollision co = new FGGraphicCollision();
		co.addRectangle(-scoreSprite.getWidth() / 2,
				-scoreSprite.getHeight() / 2, scoreSprite.getWidth(),
				scoreSprite.getHeight());
		this.bindCollisionMask(co);

		setMovement(false, SectionStage.getScrollSpeedV(),
				SectionStage.getScrollSpeedH());

		this.defineTypes(5, 0.1f);
	}

	@Override
	public void onTypeChange(int type) {
		scoreSprite.nextFrame();
	}

	@Override
	public void hitOn(Hitable target) {
		if (Player.class.isInstance(target)) {
			new Explosion(GlobalResources.FRAMES_EXPLOSION_FLASHTEXT_SCORE_500,
					5, 0.5f, (int) this.getX(), (int) this.getY(), getDepth());

			FGGamingThread.score += 500;
			this.dismiss();
		}
	}

}
