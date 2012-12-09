/**
 * FileName:     Explosion.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-14 下午4:08:21
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-14      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;

/**
 * @ClassName: Explosion
 * @Description: 爆炸特效类
 * @author: Noisyfox
 * @date: 2012-8-14 下午4:08:21
 * 
 */
public class Explosion extends FGPerformer {

	FGSprite s = new FGSprite();
	float frameSpeed = 0f;
	int turns = 1;

	@Override
	protected void onCreate() {
		this.setAlarm(0, (int) frameSpeed, true);
		this.startAlarm(0);
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {
			if (s.getCurrentFrame() == s.getFrameCount() - 1 && --turns <= 0) {
				this.dismiss();
			} else {
				s.nextFrame();
			}
		}
	}

	@Override
	protected void onStep() {
		this.setPosition(this.getX(),
				this.getY() + SectionStage.getScrollSpeedV());
	}

	public Explosion(FGFrame frames, int turns, float lastTime, int x, int y,
			int depth) {
		if (turns < 1) {
			throw new IllegalArgumentException(
					"turns must be larger than zero!");
		}
		s.bindFrames(frames);
		s.setOffset(s.getWidth() / 2, s.getHeight() / 2);
		this.turns = turns;
		frameSpeed = lastTime / (float) turns
				/ (float) (frames.getFrameCount() - 1)
				* FGStage.getCurrentStage().getStageSpeed();
		this.setDepth(depth);
		this.perform(FGStage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
		this.bindSprite(s);
	}

}
