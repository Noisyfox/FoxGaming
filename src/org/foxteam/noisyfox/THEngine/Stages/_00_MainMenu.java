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
import org.foxteam.noisyfox.THEngine.Performers.Button_GameStart;
import org.foxteam.noisyfox.THEngine.Performers.Tester;

/**
 * @ClassName: _00_MainMenu
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-17 上午11:30:37
 * 
 */
public class _00_MainMenu extends FGStage {

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

		FGButton bGameStart = new Button_GameStart();
		bGameStart.perform(this.getStageIndex());
		bGameStart.setPosition(this.getWidth() / 2, this.getHeight()
				- bGameStart.getHeight() / 2 - 5);
		FGGamingThread.score = 0;
		this.setStageSpeed(10);
		
		FGPerformer p = new Tester();
		p.perform(this.getStageIndex());

	}

}
