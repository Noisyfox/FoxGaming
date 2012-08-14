/**
 * FileName:     SystemControl.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-7-18 下午11:19:55
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-7-18      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine;

import org.foxteam.noisyfox.FoxGaming.Core.*;

/**
 * @ClassName: SystemControl
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-18 下午11:19:55
 * 
 */
public class SystemControl extends Performer {

	private int bgmId;

	@Override
	protected void onCreate() {
		bgmId = SimpleBGM.loadBGM(org.foxteam.noisyfox.THEngine.R.raw.test_bgm);
		SimpleBGM.play(bgmId, true);

		// 计算缩放比率
		float k = (float) GamingThread.getScreenWidth() * 1.15f / 320f;

		Stage.getCurrentStage().setSize(
				(int) ((float) GamingThread.getScreenHeight() / k), 320);

		Views v = new Views();

		v.setSizeFromScreen(GamingThread.getScreenWidth(),
				GamingThread.getScreenHeight());
		v.setSizeFromStage((int) ((float) GamingThread.getScreenWidth() / k),
				(int) ((float) GamingThread.getScreenHeight() / k));

		v.setPositionFromScreen(0, 0);
		v.setPositionFromStage(
				(Stage.getCurrentStage().getWidth() - v.getWidthFromStage()) / 2,
				0);

		v.setAngleFromStage(0);
		Stage.getCurrentStage().addView(v);

		Player p = new Player();
		p.perform(Stage.getCurrentStage().getStageIndex());

		new HUD();

		this.setAlarm(0, (int) (Stage.getSpeed() * 2f), true);// 创建 鸭子敌人
		this.startAlarm(0);
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {// 创建 鸭子敌人
			Enemy e = new Enemy_Duck(MathsHelper.random(0, Stage
					.getCurrentStage().getHeight() / 2), MathsHelper.random(0,
					11) > 5);
			e.perform(Stage.getCurrentStage().getStageIndex());
		}
	}

	@Override
	protected void onScreenSizeChanged(int width, int height) {
		// MyDebug.print("Screen size changed");

		// Stage.getCurrentStage().setSize(height, 320);
		// Views v = Stage.getCurrentStage().getView(0);
		// v.setSizeFromScreen(GamingThread.getScreenWidth(),
		// GamingThread.getScreenHeight());
		// v.setSizeFromStage(GamingThread.getScreenWidth(),
		// GamingThread.getScreenHeight());

	}

	@Override
	protected void onStep() {
	}

	@Override
	protected void onTouchRelease(int whichfinger) {
		if (whichfinger == 1) {
			GameActivity.getGameCore().gameEnd();
		}
	}

	@Override
	protected void onDraw() {

	}

	public SystemControl() {
	}

}
