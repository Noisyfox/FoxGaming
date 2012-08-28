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
package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.*;
import org.foxteam.noisyfox.FoxGaming.G2D.*;

/**
 * @ClassName: Explosion
 * @Description: 爆炸特效类
 * @author: Noisyfox
 * @date: 2012-8-14 下午4:08:21
 * 
 */
public class Explosion extends Performer {

	Sprite s = new Sprite();
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
		this.setPosition(this.getX(), this.getY()
				+ Stage.getCurrentBackground().getVSpeed());
	}

	public Explosion(int resId, int frameNumber, int turns, float lastTime,
			int x, int y, int depth) {
		if (turns < 1) {
			throw new IllegalArgumentException(
					"turns must be larger than zero!");
		}
		s.loadFromBitmap(resId, frameNumber, 1, false);
		s.setOffset(s.getWidth() / 2, s.getHeight() / 2);
		this.turns = turns;
		frameSpeed = lastTime / (float) turns / (float) (frameNumber - 1)
				* Stage.getCurrentStage().getStageSpeed();
		this.setDepth(depth);
		this.perform(Stage.getCurrentStage().getStageIndex());
		this.setPosition(x, y);
		this.bindSprite(s);
	}

}
