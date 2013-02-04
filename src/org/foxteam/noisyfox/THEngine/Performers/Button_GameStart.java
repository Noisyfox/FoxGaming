/**
 * FileName:     Button_GameStart.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-8-17 下午2:54:30
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-8-17      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.FGButton;
import org.foxteam.noisyfox.FoxGaming.Core.FGEventsListener;
import org.foxteam.noisyfox.FoxGaming.Core.FGGameActivity;
import org.foxteam.noisyfox.THEngine.GlobalResources;
import org.foxteam.noisyfox.THEngine.Section.BasicElements.SectionStage;

import android.view.KeyEvent;

/**
 * @ClassName: Button_GameStart
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-8-17 下午2:54:30
 * 
 */
public class Button_GameStart extends FGButton {

	public Button_GameStart() {
		super(170, 53, GlobalResources.FRAMES_BUTTON_GAMESTART);
		setTouchSize(200, 100);

		requireEventFeature(FGEventsListener.EVENT_ONKEYRELEASE);
	}

	@Override
	public void onClick() {
		SectionStage.initSectionStage();
		SectionStage.startSection();
	}

	@Override
	protected void onKeyRelease(int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FGGameActivity.getGameCore().gameEnd();
		}
	}

}
