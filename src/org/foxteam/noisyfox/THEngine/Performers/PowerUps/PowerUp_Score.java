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
package org.foxteam.noisyfox.THEngine.Performers.PowerUps;

import org.foxteam.noisyfox.FoxGaming.Core.GamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.Stage;
import org.foxteam.noisyfox.FoxGaming.G2D.GraphicCollision;
import org.foxteam.noisyfox.FoxGaming.G2D.Sprite;
import org.foxteam.noisyfox.THEngine.Performers.Hitable;
import org.foxteam.noisyfox.THEngine.Performers.Player;
import org.foxteam.noisyfox.THEngine.Performers.PowerUp;

/**
 * @ClassName: PowerUp_Score
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-25 上午10:37:09
 * 
 */
public class PowerUp_Score extends PowerUp {

	public PowerUp_Score(int x, int y) {
		super(x, y);

		Sprite scoreSprite = new Sprite();
		scoreSprite.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.powerup_score, false);
		scoreSprite.setOffset(scoreSprite.getWidth() / 2,
				scoreSprite.getHeight() / 2);
		this.bindSprite(scoreSprite);

		GraphicCollision co = new GraphicCollision();
		co.addRectangle(-scoreSprite.getWidth() / 2,
				-scoreSprite.getHeight() / 2, scoreSprite.getWidth(),
				scoreSprite.getHeight());
		this.bindCollisionMask(co);

		setMovement(false, Stage.getCurrentBackground().getVSpeed(), Stage
				.getCurrentBackground().getHSpeed());
	}

	@Override
	public void onTypeChange(int type) {
	}

	@Override
	public void hitOn(Hitable target) {
		if (Player.class.isInstance(target)) {
			GamingThread.score += 1000;
		}
		this.dismiss();
		this.bindCollisionMask(null);
	}

}
