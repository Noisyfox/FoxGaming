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
import org.foxteam.noisyfox.FoxGaming.G2D.FGBackground;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Performers.Button_GameStart;
import org.foxteam.noisyfox.THEngine.Performers.Button_HighScore;

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
		this.setSize(FGGamingThread.getScreenHeight(),
				FGGamingThread.getScreenWidth());

		FGBackground bkg = new FGBackground();

		bkg.loadFromBitmap(
				org.foxteam.noisyfox.THEngine.R.drawable.background_menu, false);
		bkg.setAdaptation(FGBackground.ADAPTATION_SMART);
		bkg.setDrawMode(FGBackground.ADAPTATION_OPTION_DRAW_SINGLE);
		bkg.setAlignment(FGBackground.ADAPTATION_OPTION_ALIGNMENT_CENTER_HORIZONTAL_BOTTOM);
		bkg.setScaleMode(FGBackground.ADAPTATION_OPTION_SCALE_MAXUSAGE);
		setBackground(bkg);

		FGGamingThread.score = 0;
		this.setStageSpeed(30);

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

	}

	private class MenuController extends FGPerformer {

		FGSprite touchText = new FGSprite();
		boolean seeText = false;
		boolean buttonShown = false;

		@Override
		protected void onDraw() {
			if (seeText)
				touchText.draw(getCanvas(), getWidth() / 2, getHeight() - 20);
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
