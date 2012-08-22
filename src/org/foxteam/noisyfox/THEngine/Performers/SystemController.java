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
package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.*;

import android.view.KeyEvent;

/**
 * @ClassName: SystemControl
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-18 下午11:19:55
 * 
 */
public class SystemController extends Performer {

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

		new HUD();

		// this.setAlarm(0, (int) (Stage.getSpeed() * 1f), true);// 创建 鸭子敌人
		// this.startAlarm(0);
	}

	@Override
	protected void onAlarm(int whichAlarm) {
		// if (whichAlarm == 0) {// 创建 鸭子敌人
		// (new Enemy_Duck()).createEnemy(0, MathsHelper.random(0, Stage
		// .getCurrentStage().getHeight() / 3), MathsHelper.random(0,
		// 11) > 5 ? 0 : 1);
		// }
	}

	@Override
	protected void onKeyRelease(int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Stage.previousStage();
		}
	}

	@Override
	protected void onStageEnd() {
		SimpleBGM.stop();
	}

	public SystemController() {
		this.perform(Stage.getCurrentStage().getStageIndex());
	}

}
