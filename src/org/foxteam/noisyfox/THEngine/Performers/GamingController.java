/**
 * FileName:     GamingController.java
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
 * @ClassName: GamingController
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-7-18 下午11:19:55
 * 
 */
public class GamingController extends FGPerformer {

	private int bgmId;
	boolean bossMode = false;

	boolean stageClear = false;

	HUD hud = null;

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

		hud = new HUD();
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

	@Override
	protected void onAlarm(int whichAlarm) {
		if (whichAlarm == 0) {// stage clear
			Player player = (Player) FGStage.getPerformersByClass(Player.class)[0];
			player.invincible = true;
			player.invincibleFlash = true;
			player.onAnimation = true;
			player.controllable = false;
			player.fire = false;
			stageClear = true;
			FGScreenPlay ani = new FGScreenPlay();
			ani.moveTowardsWait(FGStage.getCurrentStage().getWidth() / 2,
					FGStage.getCurrentStage().getHeight() / 2 + 5
							+ player.getSprite().getOffsetY(),
					(int) (FGStage.getSpeed() * 0.5f));
			ani.jumpTo(FGStage.getCurrentStage().getWidth() / 2, FGStage
					.getCurrentStage().getHeight()
					/ 2
					+ 5
					+ player.getSprite().getOffsetY());
			ani.stop();
			setAlarm(2, (int) (FGStage.getSpeed() * 0.5f), false);
			startAlarm(2);
			player.playAScreenPlay(ani);

		} else if (whichAlarm == 1) {// game over
			hud.toggleFlashText(2);

		} else if (whichAlarm == 2) {
			hud.toggleFlashText(1);
			setAlarm(3, (int) (FGStage.getSpeed() * 5f), false);
			startAlarm(3);

		} else if (whichAlarm == 3) {
			hud.toggleFlashText(0);

			Player player = (Player) FGStage.getPerformersByClass(Player.class)[0];

			FGScreenPlay ani = new FGScreenPlay();
			ani.moveTowardsWait(FGStage.getCurrentStage().getWidth() / 2,
					-player.getSprite().getOffsetY(),
					(int) (FGStage.getSpeed() * 1f));

			player.playAScreenPlay(ani);

			setAlarm(4, (int) (FGStage.getSpeed() * 1f), false);
			startAlarm(4);

		} else if (whichAlarm == 4) {
			FGStage.getPerformersByClass(Player.class)[0].dismiss();

		}
	}

	public GamingController() {
		this.perform(FGStage.getCurrentStage().getStageIndex());
	}

	public void stageClear() {
		if (isAlarmStart(1)) {
			return;
		}
		setAlarm(0, (int) (FGStage.getSpeed() * 2f), false);
		startAlarm(0);
	}

	public void gameOver() {
		stopAlarm(0);
		setAlarm(1, (int) (FGStage.getSpeed() * 2f), false);
		startAlarm(1);
	}

}
