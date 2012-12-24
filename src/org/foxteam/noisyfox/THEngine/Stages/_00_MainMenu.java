/**
 * FileName:     _00_MainMenu.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-17 上午11:30:37
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-17      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Stages;

import org.foxteam.noisyfox.FoxGaming.Core.FGButton;
import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.Background.FGScreenAdaptatedBackground;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Performers.Button_GameStart;
import org.foxteam.noisyfox.THEngine.Performers.Button_HighScore;
import org.foxteam.noisyfox.THEngine.Performers.StageSwitchEffect;

/**
 * @ClassName: _00_MainMenu
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-17 上午11:30:37
 * 
 */
public class _00_MainMenu extends FGStage {

	private boolean _1stin = true;

	@Override
	protected void onCreate() {
		setSize(FGGamingThread.getScreenHeight(),
				FGGamingThread.getScreenWidth());

		FGScreenAdaptatedBackground bkg = new FGScreenAdaptatedBackground();

		bkg.bindFrame(GlobalResources.FRAMES_BACKGROUND_MAINMENU);

		if ((float) FGGamingThread.getScreenHeight()
				/ (float) FGGamingThread.getScreenWidth() > (float) GlobalResources.FRAMES_BACKGROUND_MAINMENU
				.getHeight()
				/ (float) GlobalResources.FRAMES_BACKGROUND_MAINMENU.getWidth()) {
			bkg.setAlignType(
					FGScreenAdaptatedBackground.Horizon_Align_Type.Center,
					FGScreenAdaptatedBackground.Vertical_Align_Type.Stretch);
		} else {
			bkg.setAlignType(
					FGScreenAdaptatedBackground.Horizon_Align_Type.Stretch,
					FGScreenAdaptatedBackground.Vertical_Align_Type.Top);
		}

		setBackground(bkg);

		FGGamingThread.score = 0;
		setStageSpeed(30);

		if (_1stin) {
			_1stin = false;
			FGPerformer controller = new MenuController();
			controller.perform(stageIndex);
		} else {
			FGButton bGameStart = new Button_GameStart();
			bGameStart.perform(stageIndex);
			bGameStart.setPosition(getWidth() / 4,
					getHeight() - bGameStart.getHeight() / 2 - 5);

			FGButton bHighScore = new Button_HighScore();
			bHighScore.perform(stageIndex);
			bHighScore.setPosition(getWidth() / 4 * 3,
					getHeight() - bHighScore.getHeight() / 2 - 5);
		}

		new StageSwitchEffect();

	}

	private class MenuController extends FGPerformer {

		FGSprite touchText = new FGSprite();
		boolean seeText = false;
		boolean buttonShown = false;

		@Override
		protected void onDraw() {
			if (seeText)
				touchText.draw(getWidth() / 2, getHeight() - 20);
		}

		@Override
		protected void onAlarm(int whichAlarm) {
			if (whichAlarm == 0) {
				seeText = !seeText;
			}
		}

		@Override
		protected void onCreate() {
			touchText
					.bindFrames(GlobalResources.FRAMES_TOUCH_SCREEN_TO_CONTINUE);
			touchText
					.setOffset(touchText.getWidth() / 2, touchText.getHeight());

			setAlarm(0, (int) (0.5 * getSpeed()), true);
			startAlarm(0);
		}

		@Override
		protected void onTouchPress(int whichfinger, int x, int y) {
			if (buttonShown)
				return;

			buttonShown = true;
			stopAlarm(0);
			seeText = false;

			FGButton bGameStart = new Button_GameStart();
			bGameStart.perform(stageIndex);
			bGameStart.setPosition(getWidth() / 4,
					getHeight() - bGameStart.getHeight() / 2 - 5);

			FGButton bHighScore = new Button_HighScore();
			bHighScore.perform(stageIndex);
			bHighScore.setPosition(getWidth() / 4 * 3,
					getHeight() - bHighScore.getHeight() / 2 - 5);
		}

	}

}
