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
public class SystemController extends FGPerformer {

	private int bgmId;

	@Override
	protected void onCreate() {
		bgmId = FGSimpleBGM
				.loadBGM(org.foxteam.noisyfox.THEngine.R.raw.test_bgm);
		FGSimpleBGM.play(bgmId, true);

		// 计算缩放比率
		float k = (float) FGGamingThread.getScreenWidth() * 1.15f / 320f;

		FGStage.getCurrentStage().setSize(
				(int) ((float) FGGamingThread.getScreenHeight() / k), 320);

		FGViews v = new FGViews();

		v.setSizeFromScreen(FGGamingThread.getScreenWidth(),
				FGGamingThread.getScreenHeight());
		v.setSizeFromStage((int) ((float) FGGamingThread.getScreenWidth() / k),
				(int) ((float) FGGamingThread.getScreenHeight() / k));

		v.setPositionFromScreen(0, 0);
		v.setPositionFromStage(
				(FGStage.getCurrentStage().getWidth() - v.getWidthFromStage()) / 2,
				0);

		v.setAngleFromStage(0);
		FGStage.getCurrentStage().addView(v);

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
			FGStage.previousStage();
		}
	}

	@Override
	protected void onStageEnd() {
		FGSimpleBGM.stop();
	}

	public SystemController() {
		this.perform(FGStage.getCurrentStage().getStageIndex());
	}

}
